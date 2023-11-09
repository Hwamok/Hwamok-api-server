package com.hwamok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HwamokApiServerApplication {

  public static void main(String[] args) {
    System.out.println("테스트 입니다.");
    SpringApplication.run(HwamokApiServerApplication.class, args);
  }

}
