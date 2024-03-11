package com.example.runway;

import java.util.Arrays;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
@OpenAPIDefinition(servers = {@Server(url = "${server.servlet.context-path}", description = "Default Server URL")})
public class RunwayApplication implements ApplicationListener<ApplicationReadyEvent> {
    private final Environment environment;

    public RunwayApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(RunwayApplication.class, args);
        long heapSize = Runtime.getRuntime().totalMemory();
        log.info("HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){
        log.info("applicationReady status Active Profiles =" + Arrays.toString(environment.getActiveProfiles()));
    }

}
