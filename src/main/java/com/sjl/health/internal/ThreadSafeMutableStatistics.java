package com.sjl.health.internal;

import java.util.concurrent.atomic.*;

import com.sjl.health.*;
import com.sjl.health.internal.immutable.*;

public class ThreadSafeMutableStatistics implements MutableStatistics {

	private Clock clock;
	private Instant created;
	private AtomicLong counter;
	
	public ThreadSafeMutableStatistics(Clock aClock) {
		clock = aClock;
		created = ImmutableInstant.create(clock.now());
		counter = new AtomicLong();
	}
	
	@Override
	public long getOccurrenceCount() {
		return counter.get();
	}

	/**
	 * @return a "live" view of the frequency - repeated calls to getHertz
	 * will return updated values. To get a static value at the time of the
	 * call, use snapshot().getFrequency()
	 */
	@Override
	public Frequency getFrequency() {
		return new Frequency() {
			@Override
			public double getHertz() {
				return (counter.get() / getPeriod().getMilliseconds()) / 1000d;
			}
		};
	}

	/**
	 * @return a "live" view of the duration - repeated calls to getEnd or
	 * getMilliseconds will return updated values. To get a static value at the
	 * time of the call, use snapshot.getDuration()
	 */
	@Override
	public TimePeriod getPeriod() {
		return new TimePeriod() {
			@Override
			public Instant getStart() {
				return created;
			}

			@Override
			public Instant getEnd() {
				return clock.now();
			}

			@Override
			public long getMilliseconds() {
				return getEnd().getClockTime() - created.getClockTime();
			}
		};
	}

	@Override
	public long increment() {
		return counter.incrementAndGet();
	}

	/**
	 * @return a point in time immutable snapshot of the statistics
	 */
	@Override
	public Statistics snapshot() {
		final Instant _end = clock.now();
		final long _count = counter.get();
		
		return ImmutableStatistics.create(
			ImmutableTimePeriod.create(created, _end), _count);
	}	
}
