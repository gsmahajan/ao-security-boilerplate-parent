package in.apporchid.flinkcep.common;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class SecEvent {

	public enum EventType { LenelAccessControl , VirtualPrivateNetwork , NetFlow, ThermoMeter};

	private final LocalDateTime eventReceivedAt = LocalDateTime.now();

	private EventType eventType;
	public SecEvent(EventType eventType) {
		super();
		this.eventType = eventType;
	}

	public LocalDateTime getEventReceivedAt() {
		return eventReceivedAt;
	}
	public EventType getEventType() {
		return eventType;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
