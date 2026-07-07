package com.sentinelai.producer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //this class handles HTTP requests
public class DemoController {

    @GetMapping("/trigger-error") // when someone calls this URL, run this method
    public String triggerError() {
        throw new RuntimeException("Simulated error from Sentinel-AI producer!"); // deliberately throws an exception — this is what AOP will intercept later
    }
}