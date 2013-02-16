package com.sjl.health.impl;

import org.jmock.*;
import org.junit.*;

import com.sjl.health.*;

public class InMemoryHealthTest {

	private Mockery ctx;
	
	private Exception expected;
	private InMemoryHealth health;
	private State state1, state2;
	private HistoryManager history;
	private HealthListener listener;
	
	@Before
	public void setup() {
		ctx = new Mockery();
		
		expected = new Exception();
		state1 = ctx.mock(State.class, "s1");
		state2 = ctx.mock(State.class, "s2");
		history = ctx.mock(HistoryManager.class);
		listener = ctx.mock(HealthListener.class);
		
		health = new InMemoryHealth(state1, history);
		health.addListener(listener);
	}
	
	@After
	public void teardown() {
		ctx.assertIsSatisfied();
	}
	
	@Test
	public void invokesStateOnSuccess() {
		ctx.checking(new Expectations() {{
			oneOf(state1).success(); will(returnValue(state1));
		}});
		
		health.success();
	}
	
	@Test
	public void invokesStateOnFailure() {
		ctx.checking(new Expectations() {{
			oneOf(state1).failure(expected);
			will(returnValue(state1));
		}});
		
		health.failure(expected);
	}
	
	@Test
	public void notifiesListenersOfSuccessStateChange() {
		ctx.checking(new Expectations() {{
			ignoring(history);
			
			oneOf(state1).success();
			will(returnValue(state2));
			
			oneOf(listener).onChange(state1, state2);
		}});
		
		health.success();
	}
	
	@Test
	public void notifiesListenersOfFailureStateChange() {
		ctx.checking(new Expectations() {{
			ignoring(history);
			
			oneOf(state1).failure(expected);
			will(returnValue(state2));
			
			oneOf(listener).onChange(state1, state2);
		}});
		
		health.failure(expected);
	}
}
