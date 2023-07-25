package com.apporchid.beans;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Immutable LenelAccess Event class. The process control system creates these
 * events. The LenelAccessEventHandler picks these up and processes them.
 */
public class LenelAccessEvent { // Ugly but need to flee through EPL

	public enum AccessType {
		LENEL_ENTER_GRANTED, LENEL_ENTER_DECLINED, LENEL_EXIT_GRANTED, LENEL_EXIT_DECLINE;
		public static AccessType random() {
			return RandomUtils.nextBoolean() ? AccessType.LENEL_ENTER_GRANTED : LENEL_EXIT_GRANTED;
		}
	};
	

	public LenelAccessEvent() {
		super();
	}

	private LocalDateTime accessTime;
	private AccessType accessType;
	private String personName;
	private String accessInformation;
	private String gate;
	private String address;
	private String badgeId;

	public LenelAccessEvent(String eventSource, LocalDateTime accessTime, AccessType accessType, String personName,
			Long lenelAccessId, String accessInformation, String gate, String address, String badgeId) {
		this.accessTime = accessTime;
		this.accessType = accessType;
		this.personName = personName;
		this.accessInformation = accessInformation;
		this.gate = gate;
		this.address = address;
		this.badgeId = badgeId;
	}

	public void setAccessTime(LocalDateTime accessTime) {
		this.accessTime = accessTime;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public void setAccessInformation(String accessInformation) {
		this.accessInformation = accessInformation;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBadgeId(String badgeId) {
		this.badgeId = badgeId;
	}

	public LocalDateTime getAccessTime() {
		return accessTime;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public String getPersonName() {
		return personName;
	}

	public String getAccessInformation() {
		return accessInformation;
	}

	public String getGate() {
		return gate;
	}

	public String getAddress() {
		return address;
	}

	public String getBadgeId() {
		return badgeId;
	}

	@Override
	public String toString() {
		return "LenelAccessEvent =>" + ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
