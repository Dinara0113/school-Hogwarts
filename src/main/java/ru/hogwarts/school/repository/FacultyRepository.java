package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);

    @Query("SELECT f FROM Faculty f JOIN f.students s WHERE s.id = :studentId")
    Faculty findByStudentId(@Param("studentId") long studentId);

}
