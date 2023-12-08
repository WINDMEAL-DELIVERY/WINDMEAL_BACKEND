package com.windmeal.model.event;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventConfig {

    private final ApplicationContext applicationContext;

    @PostConstruct
    public void eventsInitializer(){
        EventPublisher.setPublisher(applicationContext);
    }
}
