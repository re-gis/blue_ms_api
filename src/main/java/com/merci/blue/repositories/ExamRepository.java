package com.merci.blue.repositories;

import com.merci.blue.entities.Course;
import com.merci.blue.entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByCourseAndLevelAndTermIgnoreCase(Course course, String level, String term);
}
