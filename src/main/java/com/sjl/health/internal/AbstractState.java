package com.sjl.health.internal;

import com.sjl.health.*;
import com.sjl.health.internal.immutable.*;

public abstract class AbstractState implements State {

	private final String name;
	private final Issue whyChanged;
	private final IssueTracker issueTracker;
	private final MutableStatistics success;
	private final MutableStatistics failure;
	
	public AbstractState(
		String aName, Issue aWhyChanged, IssueTracker aTracker, Clock aClock) {
		name = aName;
		whyChanged = ImmutableIssue.create(aWhyChanged);
		issueTracker = aTracker;
		success = new ThreadSafeMutableStatistics(aClock);
		failure = new ThreadSafeMutableStatistics(aClock);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Instant getWhenChanged() {
		return whyChanged.getMostRecentOccurrence();
	}

	@Override
	public Issue getWhyChanged() {
		return whyChanged;
	}

	@Override
	public Issues getDistinctIssues() {
		return issueTracker.getDistinctIssues();
	}
	
	@Override
	public Statistics getSuccessStats() {
		return success.snapshot();
	}
	
	@Override
	public Statistics getFailureStats() {
		return failure.snapshot();
	}
	
	@Override
	public Statistics getTotalStats() {
		Statistics _success = success.snapshot();
		Statistics _failure = failure.snapshot();
		
		Instant _start = ImmutableInstant.earliest(
			_success.getPeriod().getStart(),
			_failure.getPeriod().getStart());
		
		Instant _end = ImmutableInstant.latest(
			_success.getPeriod().getEnd(),
			_failure.getPeriod().getEnd());
		
		return ImmutableStatistics.create(
			ImmutableTimePeriod.create(_start, _end),
			_success.getOccurrenceCount() + _failure.getOccurrenceCount());
	}

	@Override
	public State success() {
		success.increment();
		return this;
	}

	@Override
	public State failure(Throwable aThrowable) {
		failure.increment();
		issueTracker.log(aThrowable);
		return this;
	}
}
