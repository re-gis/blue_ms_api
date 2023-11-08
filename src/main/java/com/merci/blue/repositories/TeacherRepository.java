package com.merci.blue.repositories;

import com.merci.blue.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findOneByContact(String contact);

    Optional<Teacher> findByFirstnameAndLastname(String firstname, String lastname);

    Optional<Teacher> findOneById(Long teacherId);
}
