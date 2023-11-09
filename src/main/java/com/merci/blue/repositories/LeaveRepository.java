package com.merci.blue.repositories;

import com.merci.blue.entities.Leave;
import com.merci.blue.entities.Teacher;
import com.merci.blue.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findAllByStatus(EStatus st);

    Optional<Leave> findByTeacher(Teacher t);
}
