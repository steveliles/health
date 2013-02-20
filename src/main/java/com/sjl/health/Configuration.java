package com.sjl.health;

import com.sjl.health.internal.*;

public interface Configuration extends InitialStateFactory {
	
	public State newStateInstance(String aStateName);
	
}
