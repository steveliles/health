package com.sjl.health;

import com.sjl.health.internal.*;

public class SampleConfiguration {

	interface MyComponent {		
		public String method1();		
		public String method2();		
	}
	
	class MyComponentImpl implements MyComponent {
		public String method1() {
			return "yay";
		}

		@Override
		public String method2() {
			throw new RuntimeException();
		}		
	}
	
	public void basicExample() {
		
		MyComponent _component = new MyComponentImpl();
		HealthService _health = new DynamicProxyingHealthService(
			new HealthFactory() {
				public Health newHealth(InitialStateFactory aStates) {
					return new SimpleThreadSafeHealth(aStates, null); // todo
				}
			}); 
		
		MyComponent _myMonitored = _health.monitor(_component, null); // config goes here
	}
	
}
