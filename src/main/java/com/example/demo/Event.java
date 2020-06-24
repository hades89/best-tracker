package com.example.demo;

public abstract class Event {

	private String eventType;

	public Event(String eventType) {
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}
}
