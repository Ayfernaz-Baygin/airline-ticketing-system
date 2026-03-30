package com.ayfernaz.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "🚀 Airline Gateway is running";
    }

    @GetMapping("/health")
    public String health() {
        return "Gateway OK";
    }
}