package com.Instagram.Dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DummyApplication {

  public static void main(String[] args) {
    SpringApplication.run(DummyApplication.class, args);
  }

  @Bean
  CommandLineRunner checkCacheManager(CacheManager cacheManager) {
    return args -> {
      System.out.println("Cache Manager: " + cacheManager.getClass().getName());
    };
  }
}
