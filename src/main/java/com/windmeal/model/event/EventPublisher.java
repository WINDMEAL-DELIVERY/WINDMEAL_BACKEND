package com.windmeal.model.event;


import org.springframework.context.ApplicationEventPublisher;

public class EventPublisher {

    private static ApplicationEventPublisher eventPublisher;

    public static void setPublisher(ApplicationEventPublisher publisher){
        EventPublisher.eventPublisher = publisher;
    }

    public static void publish(Event event){
        if(eventPublisher != null){
            eventPublisher.publishEvent(event);
        }
    }
}
