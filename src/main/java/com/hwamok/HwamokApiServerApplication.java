package com.hwamok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HwamokApiServerApplication {

  public static void main(String[] args) {
    System.out.println("슬랙 깃 연결 테스트");
    SpringApplication.run(HwamokApiServerApplication.class, args);
  }

}
