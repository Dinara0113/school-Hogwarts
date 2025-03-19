package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    private final FacultyRepository facultyRepository;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);

    }

    @Override
    public Student removeStudent(long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return student;
    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public Student updateStudent(long id, Student studentForUpdate) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentForUpdate.setId(id);
        return  studentRepository.save(studentForUpdate);
    }

    @Override
    public List<Student> findAllByAge(int age) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }

    @Override
    public List<Student> findByAgeBetween(int ageMin, int ageMax){
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    @Override
    public Faculty findFacultyByStudentId(long id){
        return facultyRepository.findByStudentId(id);
    }

    @Override
    public List <Student> findByFacultyId(long id) {
        return studentRepository.findByFacultyId(id);
    }

    public long getStudentCount() {
        return studentRepository.countStudents();
    }

    public double getAverageStudentAge() {
        return studentRepository.averageStudentAge();
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }

}
