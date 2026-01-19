package com.process.clash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ClashApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClashApplication.class, args);
  }

}
