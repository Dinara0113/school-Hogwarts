package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Факультет не найден")
public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(long id) {
        super("Факультет: %s не найден".formatted(id));
    }
}
