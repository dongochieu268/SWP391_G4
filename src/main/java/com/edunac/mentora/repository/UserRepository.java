package com.edunac.mentora.repository;

import com.edunac.mentora.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
