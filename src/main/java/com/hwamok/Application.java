package com.hwamok;

import com.hwamok.event.SlackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class Application {

  private final ApplicationEventPublisher eventPublisher;

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    eventPublisher.publishEvent(new SlackEvent("Api Started"));
  }


  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}