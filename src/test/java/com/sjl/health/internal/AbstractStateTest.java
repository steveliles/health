package com.sjl.health.internal;

import org.jmock.*;
import org.junit.*;

import com.sjl.health.*;

public class AbstractStateTest {

	private Mockery ctx;
	private Clock clock;
	private IssueTracker tracker;
	private Issue issue;
	private Instant instant;
	private Transition promoter, demoter;
	
	@Before
	public void setup() {
		ctx = new Mockery();
		clock = ctx.mock(Clock.class);
		issue = ctx.mock(Issue.class);
		tracker = ctx.mock(IssueTracker.class);
		instant = ctx.mock(Instant.class);
		promoter = ctx.mock(Transition.class, "promoter");
		demoter = ctx.mock(Transition.class, "demoter");
	}
	
	@After
	public void teardown() {
		ctx.assertIsSatisfied();
	}
	
	@Test
	public void usesOriginatingIssueOccurrenceInstantForWhenChangedValue() {
		ctx.checking(new Expectations() {{
			oneOf(issue).getMostRecentOccurrence();
			will(returnValue(instant));
			
			oneOf(instant).getClockTime();
			will(returnValue(12L));
			
			ignoring(issue);
			ignoring(clock);			
		}});
		
		State _s = new AbstractState(
			"test", issue, tracker, promoter, demoter, clock){};
		Assert.assertEquals(12L, _s.getWhenChanged().getClockTime());
	}
	
	@Test
	public void collectsIssuesViaSuppliedTracker() {
		final Exception _exception = new Exception();
		ctx.checking(new Expectations(){{
			ignoring(issue);
			ignoring(clock);
			ignoring(demoter);
			oneOf(tracker).log(_exception);
		}});
		
		State _s = new AbstractState(
			"test", issue, tracker, promoter, demoter, clock){};
		_s = _s.failure(_exception);
	}
}
