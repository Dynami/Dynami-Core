package org.dynami.core.utils;

import java.util.concurrent.atomic.AtomicLong;

public enum DTime {
	Clock;
	
	public void update(long time) {
		this.time.set(time);
	};
	
	private final AtomicLong time = new AtomicLong();
	
	public long getTime(){
		return time.get();
	}
}
