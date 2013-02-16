package com.sjl.health;

import java.util.*;

public interface Health {
	
	public State getCurrentState();
	public History getHistory();
	
	public void addListener(HealthListener aListener);
	public void removeListener(HealthListener aListener);
	
	public interface State {		
		public String getName();
		public Date getWhenChanged();
		public Issue getWhyChanged();
		public Issues getDistinctIssues();	
	}
	
	public interface History extends InternallyIterable<State> {}
}
