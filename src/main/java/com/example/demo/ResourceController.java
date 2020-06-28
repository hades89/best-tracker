package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.example.model.Notification;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

	@Autowired
	private ApplicationEventPublisher publisher;
	
	  private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	@PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('CREATE')")
	@GetMapping
	public String get() {
		System.out.println("Name: "+SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println("Principle: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		System.out.println("Credentials: "+SecurityContextHolder.getContext().getAuthentication().getCredentials());
		System.out.println("Authorities: "+SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		CrawlerLoadedEvent event = new CrawlerLoadedEvent();
		publisher.publishEvent(event);
		
//		AnotherEvent event2 = new AnotherEvent();
//		publisher.publishEvent(event2);
		System.out.println(Thread.currentThread().getName()+"Done");
		return "resource success";
	}
	
  

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
            SseEmitter emitter = new SseEmitter(100000L);
            this.emitters.add(emitter);
           
            emitter.onCompletion(() -> {
            	System.out.println("On Complete");
            	this.emitters.remove(emitter);
            });
            emitter.onTimeout(() -> {
            	System.out.println("On Timeout");
                    emitter.complete(); 
                    this.emitters.remove(emitter);
            });
            

            return emitter;
    }

    @EventListener
    public void onNotification(Notification notification) {
            List<SseEmitter> deadEmitters = new ArrayList<>();
//            System.out.println(emitters.size());
            this.emitters.forEach(emitter -> {
                    try {
                    	SseEventBuilder builder = SseEmitter.event()
                                .data(notification)
                                .id("1")
                                .name("eventName")
                                .reconnectTime(10_000L);
                           emitter.send(builder.build());
                    } catch (Exception e) {
                           deadEmitters.add(emitter);
                    }
            });
            this.emitters.remove(deadEmitters);
    }
}
