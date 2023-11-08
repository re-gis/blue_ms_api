package com.merci.blue.repositories;

import com.merci.blue.entities.Teacher;
import com.merci.blue.entities.User;
import com.merci.blue.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCode(String username);

    Optional<User> findByLastnameAndFirstname(String lastname, String firstname);

    Optional<User> findByLastnameAndFirstnameAndRoleNot(String lastname, String firstname, ERole role);

    Optional<User> findByLastnameAndFirstnameAndRole(String lastname, String firstname, ERole teacher);
}
