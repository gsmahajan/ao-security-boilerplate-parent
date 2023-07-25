package com.apporchid.beans;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ActivityList {

	List<VpnEvent> vpnEvents;
	List<LenelAccessEvent> lenelEvents;

	List<Activity> activities;

	public ActivityList() {
		super();
	}

	public List<VpnEvent> getVpnEvents() {
		return vpnEvents;
	}

	public void setVpnEvents(List<VpnEvent> vpnEvents) {
		this.vpnEvents = vpnEvents;
	}

	public List<LenelAccessEvent> getLenelEvents() {
		return lenelEvents;
	}

	public void setLenelEvents(List<LenelAccessEvent> lenelEvents) {
		this.lenelEvents = lenelEvents;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}