package com.sjl.health.impl;

import org.jmock.*;
import org.junit.*;

import com.sjl.health.*;

public class AbstractDynamicProxyingHealthServiceTest {

	private interface SomeInterface {
		public void method1();
		public void method2();
	}

	private class ExpectedException extends RuntimeException{};
	
	private class SomeClass implements SomeInterface {
		private boolean invoked;
		
		@Override
		public void method1() {
			invoked = true;
		}
		
		@Override
		public void method2() {
			throw new ExpectedException();
		}
		
		public boolean wasInvoked() {
			return invoked;
		}
	}

	private Mockery ctx;
	private UpdateableHealth health;
	private HealthService healthService;
	private SomeClass uninstrumented;
	private SomeInterface instrumented;
	
	@Before
	public void setup() {
		ctx = new Mockery();
		health = ctx.mock(UpdateableHealth.class);
		healthService = new AbstractDynamicProxyingHealthService() {
			@Override
			protected UpdateableHealth newUpdateableHealth() {
				return health;
			}			
		};
		uninstrumented = new SomeClass();
		instrumented = healthService.monitor(uninstrumented);
	}
	
	@After
	public void teardown() {
		ctx.assertIsSatisfied();
	}
	
	@Test
	public void providesAccessToHealthInfoOfInstrumentedComponents() {
		Assert.assertNotNull(healthService.get(instrumented));
	}
	
	@Test
	public void providesAccessToHealthInfoOfInstrumentedComponentsViaRefToUninstrumented() {
		Assert.assertNotNull(healthService.get(uninstrumented));
	}
	
	@Test
	public void returnsNullForUnmonitoredObjects() {
		Assert.assertNull(healthService.get(new Object()));
	}
	
	@Test
	public void invokingMethodOnInstrumentedClassInvokesUnderlyingObject() {
		ctx.checking(new Expectations() {{
			ignoring(health);
		}});
		instrumented.method1();
		Assert.assertTrue(uninstrumented.wasInvoked());
	}
	
	@Test
	public void tracksSuccessfulMethodInvocations() {
		ctx.checking(new Expectations() {{
			oneOf(health).success();
		}});
		instrumented.method1();		
	}
	
	@Test
	public void tracksUnsuccessfulMethodInvocations() {
		ctx.checking(new Expectations() {{
			oneOf(health).failure(with(any(ExpectedException.class)));
		}});
		
		try {
			instrumented.method2();
		} catch (ExpectedException anExc) {
			// expected!
		}
	}
}
