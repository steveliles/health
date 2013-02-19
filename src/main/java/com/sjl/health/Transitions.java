package com.sjl.health;

public class Transitions {

	static class TransitionDef {
		private String from;
		private String to;
		private Condition when;
		
		public TransitionDef(String aFrom) {
			from = aFrom;
		}
		
		public TransitionDef to(String aTo) {
			to = aTo;
			return this;
		}
		
		public TransitionDef when(Condition aCondition) {
			when = aCondition;
			return this;
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
	}
	
	public static TransitionDef from(String aStateName) {
		return new TransitionDef(aStateName);
	}
	
}
