package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.impl.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("/{id}/get")
    public Student findStudent(@PathVariable("id") long id) {
        return studentService.findStudent(id);
    }

    @DeleteMapping("/{id}/remove")
    public Student removeStudent(@PathVariable("id") long id) {
        return studentService.removeStudent(id);
    }

    @PutMapping("/{id}/update")
    public Student updateStudent(@PathVariable("id")long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @GetMapping("/get/by-age")
    public List<Student> findAllByAge(@RequestParam("age") int age) {
        return studentService.findAllByAge(age);
    }

    @GetMapping("/get/by-age-between")
    public List<Student> findByAgeBetween(@RequestParam("ageMin") int ageMin,
                                          @RequestParam("ageMax") int ageMax){
        return studentService.findByAgeBetween(ageMin, ageMax);
    }

    @GetMapping("/{id}/get/faculty")
    public Faculty findFacultyByStudentId(@PathVariable("id") long id){
        return studentService.findFacultyByStudentId(id);
    }

    // Количество всех студентов
    @GetMapping("/count")
    public ResponseEntity<Long> getStudentCount() {
        return ResponseEntity.ok(studentService.getStudentCount());
    }

    // Средний возраст студентов
    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageStudentAge() {
        return ResponseEntity.ok(studentService.getAverageStudentAge());
    }

    // Последние 5 студентов
    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }
}
