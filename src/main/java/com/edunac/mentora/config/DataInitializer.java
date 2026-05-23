package com.edunac.mentora.config;

import com.edunac.mentora.models.Role;
import com.edunac.mentora.models.User;
import com.edunac.mentora.repository.RoleRepository;
import com.edunac.mentora.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        ensureRole("ADMIN");
        ensureRole("TEACHER");
        ensureRole("STUDENT");

        Map<String, DemoAccount> accounts = new LinkedHashMap<>();
        accounts.put("admin@mentora.com", new DemoAccount("System Admin", "ADMIN", "admin123"));
        accounts.put("teacher@mentora.com", new DemoAccount("Demo Teacher", "TEACHER", "teacher123"));
        accounts.put("admin@gmail.com", new DemoAccount("Admin Gmail", "ADMIN", "123456"));
        accounts.put("teacher@gmail.com", new DemoAccount("Teacher Gmail", "TEACHER", "123456"));

        accounts.forEach((email, demo) -> syncUser(email, demo));
    }

    private void syncUser(String email, DemoAccount demo) {
        Role role = roleRepository.findByName(demo.roleName())
                .orElseThrow(() -> new IllegalStateException("Role not found: " + demo.roleName()));

        String encodedPassword = passwordEncoder.encode(demo.rawPassword());

        userRepository.findByEmail(email).ifPresentOrElse(existing -> {
            existing.setPassword(encodedPassword);
            existing.setRole(role);
            existing.setStatus("ACTIVE");
            existing.setFullName(demo.fullName());
            userRepository.save(existing);
        }, () -> {
            User user = new User();
            user.setEmail(email);
            user.setFullName(demo.fullName());
            user.setPassword(encodedPassword);
            user.setRole(role);
            user.setStatus("ACTIVE");
            user.setEmailVerified(true);
            userRepository.save(user);
        });
    }

    private void ensureRole(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            roleRepository.save(new Role(name));
        }
    }

    private record DemoAccount(String fullName, String roleName, String rawPassword) {
    }
}
