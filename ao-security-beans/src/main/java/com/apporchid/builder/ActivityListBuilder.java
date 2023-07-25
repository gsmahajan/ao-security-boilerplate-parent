package com.apporchid.builder;

import java.util.Arrays;
import java.util.Objects;

import com.apporchid.beans.Activity;
import com.apporchid.beans.ActivityList;
import com.apporchid.beans.LenelAccessEvent;
import com.apporchid.beans.VpnEvent;

public class ActivityListBuilder {

	ActivityList activityList = null;

	ActivityListBuilder _this;

	public ActivityListBuilder() {
		super();
		activityList = new ActivityList();
	}

	public ActivityListBuilder attachVpnEvent(VpnEvent event) {
		if(Objects.isNull(activityList.getVpnEvents())){
			activityList.setVpnEvents(Arrays.asList(event));
		}
		activityList.getVpnEvents().addAll(Arrays.asList(event));
		return _this;
	}

	public ActivityListBuilder attachLenelEvent(LenelAccessEvent event) {
		if(Objects.isNull(activityList.getLenelEvents())){
			activityList.setLenelEvents(Arrays.asList(event));
		}
		activityList.getLenelEvents().addAll(Arrays.asList(event));
		return _this;
	}

	public ActivityListBuilder addActivity(Activity activity) {
		if(Objects.isNull(activityList.getActivities())){
			activityList.setActivities(Arrays.asList(activity));
			return _this;
		}
		activityList.getActivities().add(activity);
		return _this;
	}

	public ActivityList build() {
		return this.activityList;
	}
}