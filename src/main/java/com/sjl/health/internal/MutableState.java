package com.sjl.health.internal;

import com.sjl.health.*;
import com.sjl.health.internal.immutable.*;

public class MutableState implements State {

	private final String name;
	private final Issue whyChanged;
	private final IssueTracker issueTracker;
	
	private final MutableStatistics success;
	private final MutableStatistics failure;
	
	private final Transition promoter;
	private final Transition demoter;
	
	public MutableState(
		String aName, Issue aWhyChanged, IssueTracker aTracker, 
		Transition aPromoter, Transition aDemoter, Clock aClock) {
		name = aName;
		whyChanged = ImmutableIssue.create(aWhyChanged);
		issueTracker = aTracker;
		
		success = new ThreadSafeMutableStatistics(aClock);
		failure = new ThreadSafeMutableStatistics(aClock);
		
		promoter = aPromoter;
		demoter = aDemoter;
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
		return ImmutableStatistics.combine(
			success.snapshot(), failure.snapshot());
	}

	@Override
	public State success() {
		success.increment();
		return promoter.attempt(success, failure);
	}

	@Override
	public State failure(Throwable aThrowable) {
		failure.increment();
		issueTracker.log(aThrowable);
		return demoter.attempt(success, failure);
	}
}
