package com.sjl.health;

public interface HealthListener {

	public void onChange(Health.State aFrom, Health.State aTo);
	
}
