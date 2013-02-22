package com.sjl.health;

public class Exceptions {

	public static Condition ratioExceeds(final double aRatio) {
		return new Condition() {
			@Override
			public boolean test(Statistics aSuccess, Statistics aFailure) {
				double _ratio = (aSuccess.getOccurrenceCount() == 0) ?
					1d : (aFailure.getOccurrenceCount() / aSuccess.getOccurrenceCount());
				return aRatio > _ratio;
			}
			
			@Override
			public String toString() {
				return "[ratio-exceeds:" + aRatio + "]";
			}
		};
	}
	
	public static Condition ratioFallsBelow(final double aRatio) {
		return new Condition() {
			@Override
			public boolean test(Statistics aSuccess, Statistics aFailure) {
				double _ratio = (aSuccess.getOccurrenceCount() == 0) ?
					1d : (aFailure.getOccurrenceCount() / aSuccess.getOccurrenceCount());
				return aRatio < _ratio;
			}
			
			@Override
			public String toString() {
				return "[ratio-below:" + aRatio + "]";
			}
		};
	}
	
}
