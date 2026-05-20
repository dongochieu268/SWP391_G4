package com.edunac.mentora.service;

import com.edunac.mentora.domain.User;
import com.edunac.mentora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // REGISTER
    public void register(
            String fullName,
            String email,
            String password
    ) {

        if (userRepository.existsByEmail(email)) {

            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFullName(fullName);

        user.setEmail(email);

        user.setPassword(password);

        user.setRoleId(3);

        user.setStatus("ACTIVE");

        user.setEmailVerified(true);

        userRepository.save(user);
    }

    // LOGIN
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {

            return null;
        }

        if (!user.getPassword().equals(password)) {

            return null;
        }

        if (!user.getStatus().equals("ACTIVE")) {

            return null;
        }

        return user;
    }
}
