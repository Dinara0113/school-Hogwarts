package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/util")
public class UtilController {

    @GetMapping("/sum")
    public int getParallelSum() {
        return IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }
}
