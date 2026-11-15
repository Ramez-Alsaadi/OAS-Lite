/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.ramez.oas.oas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder,
            @Value("${OAS_ADMIN_USERNAME}") String adminusername,
            @Value("${OAS_ADMIN_PASSWORD}") String adminpassword,
            @Value("${OAS_EMPLOYEE_USERNAME}") String employeeUsername,
            @Value("${OAS_EMPLOYEE_PASSWORD}") String employeePassword)
            {
        UserDetails admin = User.withUsername(adminusername)
                .password(passwordEncoder.encode(adminpassword))
                .roles("ADMIN")
                .build();
        UserDetails employee = User.withUsername(employeeUsername)
                .password(passwordEncoder.encode(employeePassword))
                .roles("EMPLOYEE")
                .build();
        return new InMemoryUserDetailsManager(admin, employee);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // الملفات الثابتة
                .requestMatchers(
                    "/offline-attendance.js",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll()
                // واجهات إدارة الموظفين للمدير فقط
                    .requestMatchers("/api/employees/**").hasRole("ADMIN")
                    .requestMatchers("/api/attendance/**").hasRole("ADMIN")
                // بقية صفحات التطبيق تتطلب تسجيل الدخول
                .anyRequest().authenticated()
            )
            // استخدام صفحة تسجيل الدخول الافتراضية
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}


