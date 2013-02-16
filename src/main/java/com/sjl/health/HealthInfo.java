package com.sjl.health;

import java.util.*;

public interface HealthInfo {
	
	public StateInfo getCurrentState();
	public History getHistory();
	
	public void addListener(HealthListener aListener);
	public void removeListener(HealthListener aListener);
	
	public interface StateInfo {		
		public String getName();
		public Date getWhenChanged();
		public Issue getWhyChanged();
		public Issues getDistinctIssues();	
	}
	
	public interface History extends InternallyIterable<StateInfo> {}
}
