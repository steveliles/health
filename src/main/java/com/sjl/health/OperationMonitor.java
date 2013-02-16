package com.sjl.health;

public interface OperationMonitor {

	public void success();
	public void failure(Throwable aCause);

}
