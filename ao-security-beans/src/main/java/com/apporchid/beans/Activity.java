package com.apporchid.beans;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.apporchid.EventSourceType;

/**
 * Activity instance for Security
 * 
 * @author RH0161
 *
 */
@Entity
public class Activity {

	@GeneratedValue
	private long id;
	
	private EventSourceType eventSourceType;
	private long epochTimeStart;
	private long epochTimeEnd;
	private Map<String, Integer> activityMetrics;

	public Activity() {
		super();
	}

	public Activity(EventSourceType eventSourceType, long epochTimeStart, long epochTimeEnd, Map<String, Integer> activityMetrics) {
		super();
		this.eventSourceType = eventSourceType;
		this.epochTimeStart = epochTimeStart;
		this.epochTimeEnd = epochTimeEnd;
		this.activityMetrics = activityMetrics;
	}


	public EventSourceType getEventSourceType() {
		return eventSourceType;
	}
	public void setSourceEventType(EventSourceType eventSourceType) {
		this.eventSourceType = eventSourceType;
	}
	public long getId() {
		return id;
	}
	public long getEpochTimeStart() {
		return epochTimeStart;
	}
	public void setEpochTimeStart(long epochTimeStart) {
		this.epochTimeStart = epochTimeStart;
	}
	public long getEpochTimeEnd() {
		return epochTimeEnd;
	}
	public void setEpochTimeEnd(long epochTimeEnd) {
		this.epochTimeEnd = epochTimeEnd;
	}
	public Map<String, Integer> getActivityMetrics() {
		return activityMetrics;
	}
	public void setActivityMetrics(Map<String, Integer> activityMetrics) {
		this.activityMetrics = activityMetrics;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}