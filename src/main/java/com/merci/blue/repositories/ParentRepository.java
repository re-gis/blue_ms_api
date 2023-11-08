package com.merci.blue.repositories;

import com.merci.blue.entities.Parent;
import com.merci.blue.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findOneByContact(String contact);

    Optional<Parent> findByStudent(Student t);
}
