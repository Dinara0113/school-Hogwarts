package ru.hogwarts.school.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    @DisplayName("Корректно добавляет факультет")
    void addFaculty() {
        Faculty faculty = new Faculty("Gryffindor", "Red");

        ResponseEntity<Faculty> response = testRestTemplate.postForEntity(
                "/faculty/add",
                faculty,
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
    }

    @Test
    @DisplayName("Корректно удаляет факультет")
    void removeFaculty() {
        Faculty faculty = facultyRepository.save(new Faculty("Slytherin", "Green"));

        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                "/faculty/" + faculty.getId() + "/remove",
                HttpMethod.DELETE,
                null,
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
    }

    @Test
    @DisplayName("Корректно находит факультет по ID")
    void findFaculty() {
        Faculty faculty = facultyRepository.save(new Faculty("Ravenclaw", "Blue"));

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                "/faculty/" + faculty.getId() + "/get",
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
    }

    @Test
    @DisplayName("Корректно обновляет факультет")
    void updateFaculty() {
        Faculty faculty = facultyRepository.save(new Faculty("Hufflepuff", "Yellow"));
        Faculty updatedFaculty = new Faculty("Hufflepuff", "Gold");

        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                "/faculty/" + faculty.getId() + "/update",
                HttpMethod.PUT,
                new HttpEntity<>(updatedFaculty),
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("Gold");
    }

    @Test
    @DisplayName("Корректно получает факультеты по цвету")
    void getAllByColor() {
        facultyRepository.save(new Faculty("Gryffindor", "Red"));
        facultyRepository.save(new Faculty("Slytherin", "Green"));

        ResponseEntity<Faculty[]> response = testRestTemplate.getForEntity(
                "/faculty/get/by-color?color=Red",
                Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Gryffindor");
    }

    @Test
    @DisplayName("Корректно находит факультет по цвету или имени")
    void getFacultyByColorOrName() {
        facultyRepository.save(new Faculty("Gryffindor", "Red"));
        facultyRepository.save(new Faculty("Hufflepuff", "Yellow"));

        ResponseEntity<Faculty[]> response = testRestTemplate.getForEntity(
                "/faculty/get/by-color-or-name?color=Yellow&name=Gryffindor",
                Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }
}
