package com.example.websocketdemo.model;

/**
 * @author RH0161
 *
 */
public class SurveillanceGossipMessage {
	private SeverityType severityType;
	public SeverityType getSeverityType() {
		return severityType;
	}

	public void setSeverityType(SeverityType severityType) {
		this.severityType = severityType;
	}

	private String content;
	private String sender;


	public enum SeverityType {
		MONITOR,		//is an Event
		WARNING,		//is a Threat
		CRITICAL		//is an Alert
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
