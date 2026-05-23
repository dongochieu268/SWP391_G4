package com.edunac.mentora.config;

import com.edunac.mentora.security.MentoraUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/login/process",
                                "/assets/**",
                                "/css/**",
                                "/js/**",
                                "/style.css",
                                "/error"
                        ).permitAll()
                        .requestMatchers("/admin/**", "/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/lecturer/**", "/api/lecturer/**").hasAnyRole("ADMIN", "TEACHER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login/process")
                        .usernameParameter("email")
                        .successHandler((request, response, authentication) -> {
                            MentoraUserDetails user = (MentoraUserDetails) authentication.getPrincipal();
                            String role = user.getUser().getRole().getName();
                            if ("ADMIN".equals(role)) {
                                response.sendRedirect("/admin/subjects");
                            } else if ("TEACHER".equals(role)) {
                                response.sendRedirect("/lecturer/subjects");
                            } else {
                                response.sendRedirect("/login?error=role");
                            }
                        })
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
