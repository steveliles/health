package com.sjl.health.internal;

import com.sjl.health.*;

// TODO
public class InMemoryIssueTrackerFactory implements IssueTrackerFactory {

	@Override
	public IssueTracker newTracker() {
		return new IssueTracker() {
			@Override
			public Issues getDistinctIssues() {
				return null;
			}

			@Override
			public void log(Throwable aCause) {
				System.err.println("issue - " + aCause.getMessage());	
			}
		};
	}
	
}
