package com.sjl.health;

public interface State extends HealthInfo.StateInfo {

	public State success();
	
	public State failure(Throwable aThrowable);
	
}
