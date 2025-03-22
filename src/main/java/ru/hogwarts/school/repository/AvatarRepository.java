package ru.hogwarts.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;


public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> getByStudentId(long studentId);
    Page<Avatar> findAll(Pageable pageable);

}
