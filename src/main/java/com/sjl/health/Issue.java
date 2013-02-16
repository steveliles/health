package com.sjl.health;

import java.util.*;

public interface Issue {

	public Date getWhenFirstOccurred();
	public Date getMostRecentOccurrence();
	
	public Statistics getStatistics();
	
	public Throwable getCause();
	
	interface Statistics {
		public int getOccurrenceCount();
		public Frequency getFrequency();
	}
}
