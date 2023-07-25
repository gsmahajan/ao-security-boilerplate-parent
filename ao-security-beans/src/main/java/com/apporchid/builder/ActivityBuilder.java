package com.apporchid.builder;

import java.time.Instant;
import java.util.HashMap;

import com.apporchid.EventSourceType;
import com.apporchid.beans.Activity;

/**
 * Activity Builder
 * 
 * @author RH0161
 *
 */
public class ActivityBuilder {

	private Activity activity;
	
	private ActivityBuilder _this;

	public ActivityBuilder(EventSourceType eventSourceType) {
		super();
		activity = new Activity(); 
		activity.setActivityMetrics(new HashMap<String, Integer>());
		activity.setSourceEventType(eventSourceType);
	}

	public ActivityBuilder withEpochTimeStart(Instant now){
		activity.setEpochTimeStart(now.getEpochSecond());
		return _this;
	}

	public ActivityBuilder withEpochTimeEnd(Instant now){
		activity.setEpochTimeEnd(now.getEpochSecond());
		return _this;
	}

	public ActivityBuilder withCount(String type, Integer count){
		activity.getActivityMetrics().put(type, count);
		return _this;
	}
	
	public Activity build() {
		return activity;
	}
}
