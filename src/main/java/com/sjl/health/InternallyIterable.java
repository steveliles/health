package com.sjl.health;

public interface InternallyIterable<T> {

	public <R> R each(Callback<T, R> aCallback);
	
	interface Callback<T, R> {
		public R with(T aT);		
	}
}
