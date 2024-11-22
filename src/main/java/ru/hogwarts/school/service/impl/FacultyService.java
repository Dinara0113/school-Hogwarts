package ru.hogwarts.school.service.impl;

import org.springframework.web.bind.annotation.RequestParam;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyService {
    Faculty addFaculty(Faculty faculty);

    Faculty removeFaculty(long id);

    Faculty findFaculty(long id);

    Faculty updateFaculty(long id, Faculty facultyForUpdate);

    List<Faculty> getAllByColor(String color);
}

