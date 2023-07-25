package com.apporchid.beans;

import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Immutable Temperature Event class. The process control system creates these events. The
 * TemperatureEventHandler picks these up and processes them.
 */
public class TemperatureEvent {

	/** Temperature in Celcius. */
	private int temperature;

	/** Time temerature reading was taken. */
	private Date timeOfReading;

	private final LocalDateTime eventOccuredAt = LocalDateTime.now();

	/**
	 * Single value constructor.
	 * @param value Temperature in Celsius.
	 */
	/**
	 * Temeratur constructor.
	 * @param temperature Temperature in Celsius
	 * @param timeOfReading Time of Reading
	 */
	public TemperatureEvent(int temperature, Date timeOfReading) {
		this.temperature = temperature;
		this.timeOfReading = timeOfReading;
	}

	/**
	 * Get the Temperature.
	 * @return Temperature in Celsius
	 */
	public int getTemperature() {
		return temperature;
	}

	/**
	 * Get time Temperature reading was taken.
	 * @return Time of Reading
	 */
	public Date getTimeOfReading() {
		return timeOfReading;
	}


	public LocalDateTime getEventOccuredAt() {
		return eventOccuredAt;
	}

	@Override
	public String toString() {
		return "TemperatureEvent [" + ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE) + "]";
	}

}
