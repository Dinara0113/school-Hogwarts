package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);


    private final StudentRepository studentRepository;

    private final FacultyRepository facultyRepository;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Student addStudent(Student student) {
        logger.info("Was invoked method to add student: {}", student);
        return studentRepository.save(student);

    }

    @Override
    public Student removeStudent(long id) {
        logger.warn("Attempt to remove student with id: {}", id);

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
        logger.debug("Finding students between ages {} and {}", ageMin, ageMax);

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

    @Override
    public long getStudentCount() {
        return studentRepository.countStudents();
    }

    @Override
    public double getAverageStudentAge() {
        return studentRepository.averageStudentAge();
    }

    @Override
    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }


    @Override
    public List<String> getStudentNamesStartingWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name != null && name.toUpperCase().startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }

    @Override
    public double getAverageAgeByStream() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }


}
