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
import ru.hogwarts.school.service.impl.FacultyService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    void addFaculty() throws Exception {
        String facultyJson = "{\"name\":\"Gryffindor\",\"color\":\"Red\"}";

        mockMvc.perform(post("/faculty/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facultyJson))
                .andExpect(status().isOk());
    }

    @Test
    void removeFaculty() throws Exception {
        mockMvc.perform(delete("/faculty/1/remove"))
                .andExpect(status().isOk());
    }

    @Test
    void findFaculty() throws Exception {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        Mockito.when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Gryffindor")))
                .andExpect(jsonPath("$.color", is("Red")));
    }

    @Test
    void updateFaculty() throws Exception {
        String facultyJson = "{\"name\":\"Slytherin\",\"color\":\"Green\"}";

        mockMvc.perform(put("/faculty/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facultyJson))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByColor() throws Exception {
        Mockito.when(facultyService.getAllByColor("Red"))
                .thenReturn(List.of(new Faculty("Gryffindor", "Red")));

        mockMvc.perform(get("/faculty/get/by-color?color=Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Gryffindor")));
    }

    @Test
    void getFacultyByColorOrName() throws Exception {
        Mockito.when(facultyService.getFacultyByColorOrName("Red", "Gryffindor"))
                .thenReturn(List.of(new Faculty("Gryffindor", "Red")));

        mockMvc.perform(get("/faculty/get/by-color-or-name?color=Red&name=Gryffindor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Gryffindor")));
    }
}
