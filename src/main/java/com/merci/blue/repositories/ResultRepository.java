package com.merci.blue.repositories;

import com.merci.blue.entities.Exam;
import com.merci.blue.entities.Result;
import com.merci.blue.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByStudentAndExam(Student st, Exam e);
}
