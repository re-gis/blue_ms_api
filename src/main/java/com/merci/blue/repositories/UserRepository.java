package com.merci.blue.repositories;

import com.merci.blue.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCode(String username);

    Optional<User> findByLastnameAndFirstname(String lastname, String firstname);
}
