package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.impl.StudentService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void addStudent() throws Exception {
        String studentJson = "{\"name\":\"Harry Potter\",\"age\":20}";

        mockMvc.perform(post("/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk());
    }

    @Test
    void findStudent() throws Exception {
        Student student = new Student("Harry Potter", 20);
        Mockito.when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Harry Potter")))
                .andExpect(jsonPath("$.age", is(20)));
    }

    @Test
    void removeStudent() throws Exception {
        mockMvc.perform(delete("/student/1/remove"))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudent() throws Exception {
        String studentJson = "{\"name\":\"Hermione Granger\",\"age\":21}";

        mockMvc.perform(put("/student/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk());
    }

    @Test
    void findAllByAge() throws Exception {
        Mockito.when(studentService.findAllByAge(20)).thenReturn(List.of(new Student("Harry Potter", 20)));

        mockMvc.perform(get("/student/get/by-age?age=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Harry Potter")));
    }

    @Test
    void findByAgeBetween() throws Exception {
        Mockito.when(studentService.findByAgeBetween(18, 25)).thenReturn(List.of(new Student("Harry Potter", 20), new Student("Ron Weasley", 22)));

        mockMvc.perform(get("/student/get/by-age-between?ageMin=18&ageMax=25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Harry Potter")))
                .andExpect(jsonPath("$[1].name", is("Ron Weasley")));
    }

    @Test
    void findFacultyByStudentId() throws Exception {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Mockito.when(studentService.findFacultyByStudentId(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/1/get/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Gryffindor")))
                .andExpect(jsonPath("$.color", is("Red")));
    }
}
