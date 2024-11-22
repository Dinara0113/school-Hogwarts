package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class StudentServiceImplTest {

    private final StudentServiceImpl studentService = new StudentServiceImpl();


    @Test
    @DisplayName("Добавление студента")
    void addStudent() {
        Student expected = new Student("test", 18);

        //test
        Student actual = studentService.addStudent(expected);

        //check
        assertEquals(actual, expected);
    }

    @DisplayName("Удаление студента")
    @Test
    void removeStudent() {
        Student expected = new Student("test", 18);
        Student savedStudent = studentService.addStudent(expected);

        //test
        Student actual = studentService.removeStudent(savedStudent.getId());

        //check
        assertEquals(actual, savedStudent);
        Student student = studentService.findStudent(savedStudent.getId());
        assertNull(student);
    }

    @DisplayName("Нахождение студента")
    @Test
    void findStudent() {
        Student expected = new Student("test", 18);
        Student savedStudent = studentService.addStudent(expected);

        //test
        Student actual = studentService.findStudent(savedStudent.getId());

        //check
        assertEquals(actual, savedStudent);
    }

    @DisplayName("Изменение студента")
    @Test
    void updateStudent() {
        Student student = new Student("test", 18);
        Student savedStudent = studentService.addStudent(student);
        Student expected = new Student("test", 19);

        //test
        studentService.updateStudent(savedStudent.getId(), expected);

        //check
        Student actual = studentService.findStudent(savedStudent.getId());
        assertEquals(actual, expected);
    }

    @DisplayName("Нахождение всех студентов по возрасту")
    @Test
    void findAllByAge() {
        int age = 19;
        Student student = new Student("test", 18);
        Student expected = new Student("test2", age);
        Student expected2 = new Student("test3", age);
        studentService.addStudent(student);
        studentService.addStudent(expected);
        studentService.addStudent(expected2);


        //test
        List<Student> actual = studentService.findAllByAge(age);

        //check
        assertThat(actual).containsAll(List.of(expected, expected2));
    }
}