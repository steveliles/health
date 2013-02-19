# Java Component-Health Monitoring

Experimenting with programmatic in-VM monitoring of Java applications, with the following aims:

* Low-overhead real-time monitoring of programmer-specified objects within a Java application
* Flexible health "states" - define your own states and criteria that control state changes (e.g. "ok" when less than 1 exception per 10,000 invocations, or "warn" when method invocation time exceeds 3 seconds, or &hellip;)
* Actionable state-transitions - register listeners that can, say, email you when a component or service enters a "failed" state, or forcibly restart the database connection pool, or &hellip;
* To encourage monitoring as an up-front development activity, and not merely an after-thought that the ops team have to worry about.


What follows is currently just a thought-experiment in what configuring monitoring of a component might look like &hellip;


### Basic Example

Configure four states - "ok", "warn", "failing" and "dead" - described by successively greater error/success ratios. Send email when "warn" state reached, page ops when "failing", restart the service when "dead":

	final HealthService hs = . . .;
	final MyComponent my = . . .;
	
	. . .
	
	final MyComponent myMonitored = _hs.monitor(my,
		Transitions.from("ok").to("warn").when(
			Exceptions.ratioExceeds(1/1000)),
		Transitions.from("warn").to("failing").when(
			Exceptions.ratioExceeds(1/100)),
		Transitions.from("failing").to("dead").when(
			Exceptions.ratioExceeds(1/10))));
	
	. . .
	
	final HealthInfo myHealth = hs.get(myMonitored);
	myHealth.addListener(new HealthListener() {
	    public void onChange(StateInfo aFrom, StateInfo aTo) {
	        if ("warn".equals(aTo.getName())) {
	        	sendMail(. . .);
	        } else if ("failing".equals(aTo.getName())) {
	        	pageOps(. . .);
	        } else if ("dead".equals(aTo.getName())) {
	        	myMonitored.restart();
	        	hs.reset(myMonitored, "ok");
	        }
	    }});
	    
Unless otherwise specified, transitions to higher/better states are automatically wired as the reverse of the downward states, with a 10% tolerance to prevent constant state toggling when conditions are borderline.

### Example with multiple orthogonal state-sets

States for success/error ratio's as in the previous example, and a parallel, orthogonal state-set for monitoring throughput:

	final HealthService hs = . . .;
	final MyComponent my = . . .;
	
	. . .
	
	final MyComponent myHealthy = _hs.monitor(my,
		Transitions.from("ok").to("warn").when(
			Exceptions.ratioExceeds(1/1000)),
		Transitions.from("warn").to("failing").when(
			Exceptions.ratioExceeds(1/100)),
		Transitions.from("failing").to("dead").when(
			Exceptions.ratioExceeds(1/10))));
	
	. . .
	
	final MyComponent mySpeedy = _hs.monitor(myHealthy,
		Transitions.from("fast").to("slow").when(
		    Operators.moreThan(30, Units.PERCENT).of(
				Invocations.areSlowerThan(30, Units.MILLISECONDS))),
		Transitions.from("slow").to("crawling").when(
			Operators.moreThan(70, Units.PERCENT).of(
				Invocations.areSlowerThan(50, Units.MILLISECONDS))));