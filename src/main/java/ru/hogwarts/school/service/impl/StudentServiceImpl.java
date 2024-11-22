package ru.hogwarts.school.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class StudentServiceImpl implements StudentService {
    private static long studentCounter = 1;
    private final Map<Long, Student> studentRepository = new HashMap<>();

    @PostConstruct
    public void init() {
        addStudent(new Student("Гарри Поттер", 18));
        addStudent(new Student("Полумна Лавгуд", 19));
        addStudent(new Student("Рон Уизли", 19));
        addStudent(new Student("Драко Малфой", 18));
    }

    @Override
    public Student addStudent(Student student) {
        student.setId(studentCounter++);
        studentRepository.put(student.getId(), student);
        return student;
    }

    @Override
    public Student removeStudent(long id) {
        return studentRepository.remove(id);
    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.get(id);
    }

    @Override
    public Student updateStudent(long id, Student studentForUpdate) {
        studentForUpdate.setId(id);
        return  studentRepository.put(id, studentForUpdate);
    }

    @Override
    public List<Student> findAllByAge(int age) {
        return studentRepository.values().stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }

}
