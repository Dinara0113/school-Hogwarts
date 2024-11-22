package ru.hogwarts.school.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class FacultyServiceImpl implements FacultyService {
    private static long facultyCounter = 0;
    private final Map<Long, Faculty> facultyRepository = new HashMap<>();

    @PostConstruct
    public void init() {
        addFaculty(new Faculty("Гриффиндор", "Красный"));
        addFaculty(new Faculty("Когтевран", "Синий"));
        addFaculty(new Faculty("Пуффендуй", "Жёлтый"));
        addFaculty(new Faculty("Слизерин", "Зелёный"));
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(facultyCounter++);
        facultyRepository.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty removeFaculty(long id) {
        return facultyRepository.remove(id);
    }

    @Override
    public Faculty findFaculty(long id) {
        return facultyRepository.get(id);
    }

    @Override
    public Faculty updateFaculty(long id, Faculty facultyForUpdate) {
        facultyForUpdate.setId(id);
        return  facultyRepository.put(id, facultyForUpdate);
    }

    @Override
    public List<Faculty> getAllByColor(String color) {
        return facultyRepository.values()
                .stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();
    }
}
