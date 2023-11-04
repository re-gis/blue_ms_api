package com.merci.blue.repositories;

import com.merci.blue.entities.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findByClassname(String classname);
}
