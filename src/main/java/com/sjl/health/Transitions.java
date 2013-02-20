package com.sjl.health;

import com.sjl.health.internal.*;

public class Transitions {

	static class TransitionBuilder {
		private String from;
		private String to;
		private Condition when;
		private Configuration states;
		
		public TransitionBuilder(String aFrom) {
			from = aFrom;
		}
		
		public TransitionBuilder to(String aTo) {
			to = aTo;
			return this;
		}
		
		public TransitionBuilder when(Condition aCondition) {
			when = aCondition;
			return this;
		}
		
		public void states(Configuration aStates) {
			states = aStates;
		}
		
		public String getFrom() {
			return from;
		}
		
		public String getTo() {
			return to;
		}
		
		public Condition getWhen() {
			return when;
		}
		
		public Transition get() {
			return new Transition() {
				@Override
				public State attempt(Statistics aSuccess, Statistics aFailure) {
					return when.test(aSuccess, aFailure) ? 
						states.newStateInstance(to): 
						states.newStateInstance(from);
				}
			};
		}
	}
	
	public static TransitionBuilder from(String aStateName) {
		return new TransitionBuilder(aStateName);
	}
	
}
