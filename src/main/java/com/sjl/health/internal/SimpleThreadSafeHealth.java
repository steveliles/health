package com.sjl.health.internal;

import com.sjl.health.*;
import com.sjl.health.internal.immutable.*;

/**
 * TODO: hand-off handling of success/failure to another thread so we don't
 * block the caller - neither for the state change or the listener notification 
 * 
 * @author steve
 */
public class SimpleThreadSafeHealth implements Health {

	private Object lock;
	private State state;
	private HistoryManager history;
	private HealthListeners listeners;
	private StateInvoker success;
	private StateInvoker failure;
	
	public SimpleThreadSafeHealth(State anInitialState, HistoryManager aHistoryManager) {
		state = anInitialState;
		lock = new Object();
		listeners = new HealthListeners();
		history = aHistoryManager;
		
		success = new StateInvoker() {
			protected State invokeImpl(Throwable aMaybe) {
				return state.success();
			}			
		};
		
		failure = new StateInvoker() {
			protected State invokeImpl(Throwable aMaybe) {
				return state.failure(aMaybe);
			}
		};
	}
	
	@Override
	public void reset() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public StateInfo getCurrentState() {
		return state;
	}

	@Override
	public History getHistory() {
		return history.get();
	}

	@Override
	public void addListener(HealthListener aListener) {
		listeners.addListener(aListener);
	}

	@Override
	public void removeListener(HealthListener aListener) {
		listeners.removeListener(aListener);
	}

	@Override
	public void success() {
		success.invoke(null);
	}

	@Override
	public void failure(Throwable aCause) {
		failure.invoke(aCause);
	}
	
	abstract class StateInvoker {
		
		protected abstract State invokeImpl(Throwable aMaybe);
		
		public void invoke(Throwable aMaybe) {
			StateInfo _before, _after = null;
			
			synchronized(lock) {
				_before = state;
				state = invokeImpl(aMaybe);
				_after = state;
				
				if (_before != _after) {
					history.add(_after);
					_before = ImmutableStateInfo.create(_before);
					_after = ImmutableStateInfo.create(_after);
				}
			}
			
			if (!_before.equals(_after))
				listeners.onChange(_before, _after);
		}
	}
}
