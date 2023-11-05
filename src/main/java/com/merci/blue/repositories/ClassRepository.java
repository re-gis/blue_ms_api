package com.merci.blue.repositories;

import com.merci.blue.entities.Class;
import com.merci.blue.entities.Student;
import com.merci.blue.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findByClassnameIgnoreCase(String classname);

    Optional<Class> findByTutor(Teacher t);

    Optional<Class> findByClassname(String classname);
}
