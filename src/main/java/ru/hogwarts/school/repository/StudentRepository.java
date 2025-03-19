package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAgeBetween(int ageMin, int ageMax);

    List<Student> findByFacultyId(long id);

    // Получить количество всех студентов
    @Query("SELECT COUNT(s) FROM Student s")
    long countStudents();

    // Получить средний возраст студентов
    @Query("SELECT AVG(s.age) FROM Student s")
    double averageStudentAge();

    // Получить 5 последних студентов (по id)
    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    List<Student> findLastFiveStudents();

}
