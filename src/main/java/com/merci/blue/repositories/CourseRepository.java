package com.merci.blue.repositories;

import com.merci.blue.entities.Course;
import com.merci.blue.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCoursename(String coursename);
}
