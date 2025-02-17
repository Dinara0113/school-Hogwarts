package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Optional;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;


    private final Faker faker = new Faker();

    @Test
    @DisplayName("Корректно добавляет студента в базу данных")
    void addStudent() {

        Student student = new Student(faker.harryPotter().character(), nextInt());

        //test
        ResponseEntity<Student> response = testRestTemplate.postForEntity(
                configureUrl( "/student/add"),
                student,
                Student.class);

        //check
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Student body = response.getBody();
        assertStudentsEquals(body, student, "id");

        Student actual = studentRepository.findById(body.getId()).orElseGet(org.junit.jupiter.api.Assertions::fail);
        assertStudentsEquals(actual, student, "id");

    }



    @Test
    @DisplayName("Корректно находит студента в базе данных")
    void findStudent() {
        Student student = new Student(faker.harryPotter().character(), nextInt());
        studentRepository.save(student);

        //test
         ResponseEntity<Student> response = testRestTemplate.getForEntity(
                configureUrl("/{id}/get"), // можно и так: /student/{id}/get, убрав /student из метода configureUrl
                Student.class,
                student.getId());

        //check
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Student body = response.getBody();
        assertStudentsEquals(body, student, "id");
        assertThat(body.getId()).isNotNull();

    }


    @Test
    @DisplayName("Корректно удаляет студента из базы данных")
    void removeStudent() {
        Student student = new Student(faker.harryPotter().character(), nextInt());
        studentRepository.save(student);

        //test
        ResponseEntity<Student> response = testRestTemplate.exchange(
                configureUrl("/{id}/remove"),
                HttpMethod.DELETE,
                null,
                Student.class,
                student.getId());

        //check
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        Student body = response.getBody();
        assertStudentsEquals(body, student, "id");

        Student actual = studentRepository.findById(body.getId()).orElse(null);
        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("Корректно обновляет студента в базе данных")
    void updateStudent() {
        Student student = new Student(faker.harryPotter().character(), nextInt());
        studentRepository.save(student);

        Student newStudent = new Student(faker.harryPotter().character(), nextInt());

        //test
        testRestTemplate.put(
                configureUrl("/{id}/update"), newStudent, student.getId());

        //check
        Student actual = studentRepository.findById(student.getId()).orElseGet(org.junit.jupiter.api.Assertions::fail);
        assertStudentsEquals(actual, newStudent, "id");
    }

    @Test
    @DisplayName("Корректно находит студентов по возрасту")
    void findAllByAge() {
        int age = nextInt(18, 25);
        Student student1 = studentRepository.save(new Student(faker.harryPotter().character(), age));
        Student student2 = studentRepository.save(new Student(faker.harryPotter().character(), age));
        studentRepository.save(new Student(faker.harryPotter().character(), nextInt(26, 30))); // Другой возраст

        // test
        ResponseEntity<Student[]> response = testRestTemplate.getForEntity(
                configureUrl("/get/by-age?age=" + age),
                Student[].class);

        // check
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).extracting(Student::getId).contains(student1.getId(), student2.getId());
    }

    @Test
    @DisplayName("Корректно находит студентов в возрастном диапазоне")
    void findByAgeBetween() {
        int ageMin = 20, ageMax = 30;
        Student student1 = studentRepository.save(new Student(faker.harryPotter().character(), 22));
        Student student2 = studentRepository.save(new Student(faker.harryPotter().character(), 28));
        studentRepository.save(new Student(faker.harryPotter().character(), 35)); // Вне диапазона

        // test
        ResponseEntity<Student[]> response = testRestTemplate.getForEntity(
                configureUrl("/get/by-age-between?ageMin=" + ageMin + "&ageMax=" + ageMax),
                Student[].class);

        // check
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).extracting(Student::getId).contains(student1.getId(), student2.getId());
    }

    @Test
    @DisplayName("Корректно находит факультет по ID студента")
    void findFacultyByStudentId() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Student student = new Student(faker.harryPotter().character(), nextInt());
        student.setFaculty(faculty);
        studentRepository.save(student);

        // test
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                configureUrl("/{id}/get/faculty"),
                Faculty.class,
                student.getId());

        // check
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());
    }

//    private String configureUrl(String path) {
//        return "http://localhost:%d%s".formatted( + port + path);
//    }

    private String configureUrl(String endpoint) {
        return String.format("http://localhost:%d/student%s", port, endpoint); // Вместо "%d" используем порт как число
    }


    private void assertStudentsEquals(Student expected, Student actual, String... ignoringEquals) {
        ObjectAssert<Student> anAssert = assertThat(expected);

        if (ignoringEquals.length > 0) {
            anAssert.usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(actual);
        } else {
            anAssert.isEqualTo(actual);
        }
    }
}