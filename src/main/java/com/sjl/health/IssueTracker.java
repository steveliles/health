package com.sjl.health;

public interface IssueTracker {

	public Issues getDistinctIssues();
	public void log(Throwable aCause);
	
}
