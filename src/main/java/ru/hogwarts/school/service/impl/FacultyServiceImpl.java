package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method to add faculty: {}", faculty);

        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty removeFaculty(long id) {
        logger.warn("Attempt to remove faculty with id: {}", id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Faculty not found with id = {}", id);
                    return new FacultyNotFoundException(id);
                });
        facultyRepository.delete(faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(long id) {
        logger.info("Was invoked method to find faculty by id: {}", id);

        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Faculty not found with id = {}", id);
                    return new FacultyNotFoundException(id);
                });
    }

    @Override
    public Faculty updateFaculty(long id, Faculty facultyForUpdate) {
        logger.info("Was invoked method to update faculty with id: {}", id);
        if (!facultyRepository.existsById(id)) {
            logger.error("Faculty not found with id = {}", id);
            throw new FacultyNotFoundException(id);
        }
        facultyForUpdate.setId(id);
        return facultyRepository.save(facultyForUpdate);
    }

    @Override
    public List<Faculty> getAllByColor(String color) {
        logger.debug("Retrieving faculties by color: {}", color);
        return facultyRepository.findAll()
                .stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();
    }

    @Override
    public List<Faculty> getFacultyByColorOrName(String color, String name){
        logger.debug("Retrieving faculties by color or name: {}, {}", color, name);

        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }
}
