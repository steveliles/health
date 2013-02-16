package com.sjl.health;

import com.sjl.health.HealthInfo.*;

public interface HistoryManager {

	public History get();
	
	public void add(StateInfo aStateInfo);
	
}
