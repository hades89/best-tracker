package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CrawlerLoadedEventListener {

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@EventListener({CrawlerLoadedEvent.class})
	@Async
	@LogExecutionTime
	public void handleCrawlerLoadedEvent(Event event) throws InterruptedException {
		System.out.println(Thread.currentThread().getName()+" CrawlerLoaded handled: "+event.getEventType());
		Thread.sleep(10000);
		System.out.println(Thread.currentThread().getName()+" finish sleeping");


//		AnotherEvent event2 = new AnotherEvent();
//		publisher.publishEvent(event2);
	}
	
	@EventListener(AnotherEvent.class)
	public void handleAnotherEvent(Event event) throws InterruptedException {
		System.out.println("CrawlerLoaded2 handled: "+event.getEventType());
		Thread.sleep(10000);
		System.out.println("finish sleeping");
		
	
	}
}
