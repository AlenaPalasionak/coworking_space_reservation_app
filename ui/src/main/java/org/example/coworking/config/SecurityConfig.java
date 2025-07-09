package org.example.coworking.config;

import org.example.coworking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)//where to look for a user
                .passwordEncoder(passwordEncoder())//how to check a pass
                .and()
                .build();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/coworking-spaces").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/coworking-spaces/**").hasRole("ADMIN")
                        .requestMatchers("/api/reservations/admin").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/reservations").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/reservations/customer").hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/coworking-spaces").authenticated()

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

