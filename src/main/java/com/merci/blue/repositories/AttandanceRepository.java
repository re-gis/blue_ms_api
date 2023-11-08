package com.merci.blue.repositories;

import com.merci.blue.entities.Attandance;
import com.merci.blue.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttandanceRepository extends JpaRepository<Attandance, Long> {
    Optional<Attandance> findByStudent(Student t);
}
