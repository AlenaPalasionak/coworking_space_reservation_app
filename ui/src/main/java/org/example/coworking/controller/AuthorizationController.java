package org.example.coworking.controller;

import jakarta.validation.Valid;
import org.example.coworking.dto.LoginRequest;
import org.example.coworking.entity.User;
import org.example.coworking.entity.UserRole;
import org.example.coworking.repository.exception.EntityNotFoundException;
import org.example.coworking.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            String role = loginRequest.getRole();
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            Class<? extends User> roleClass = userRole.getUserClass();

            User user = authorizationService.authenticate(
                    loginRequest.getUsername(),
                    loginRequest.getPassword(),
                    roleClass);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
