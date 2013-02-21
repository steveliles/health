package com.sjl.health;

public class Exceptions {

	public static Condition ratioExceeds(final double aRatio) {
		return new Condition() {
			@Override
			public boolean test(Statistics aSuccess, Statistics aFailure) {
				return aRatio > (aFailure.getOccurrenceCount() / aSuccess.getOccurrenceCount());
			}			
		};
	}
	
	public static Condition ratioFallsBelow(final double aRatio) {
		return new Condition() {
			@Override
			public boolean test(Statistics aSuccess, Statistics aFailure) {
				return aRatio < (aFailure.getOccurrenceCount() / aSuccess.getOccurrenceCount());
			}			
		};
	}
	
}
