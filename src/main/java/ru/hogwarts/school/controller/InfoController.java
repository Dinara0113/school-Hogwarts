package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final String serverPort;

    public InfoController(@Value("${server.port}") String serverPort) {
        this.serverPort = serverPort;
    }

    @GetMapping("/port")
    public String getPort() {
        return "Application is running on port: " + serverPort;
    }
}
