package com.project.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {
@Bean
    public Rassilka ras(){
    return  new Rassilka();
}
}
