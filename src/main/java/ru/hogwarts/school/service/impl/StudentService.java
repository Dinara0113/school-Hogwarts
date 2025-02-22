package ru.hogwarts.school.service.impl;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);

    Student removeStudent(long id);

    Student findStudent(long id);

    Student updateStudent(long id, Student studentForUpdate);

    List<Student> findAllByAge( int age);

    List<Student> findByAgeBetween(int ageMin, int ageMax);

    Faculty findFacultyByStudentId( long id);

    List <Student>  findByFacultyId(long id);
}
