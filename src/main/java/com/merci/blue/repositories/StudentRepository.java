package com.merci.blue.repositories;

import com.merci.blue.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findOneByContact(String contact);
}
