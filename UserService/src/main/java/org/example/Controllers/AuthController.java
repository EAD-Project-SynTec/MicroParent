package org.example.Controllers;

import org.example.DTO.EditUser;
import org.example.DTO.LoginRequest;
import org.example.DTO.SignUpRequest;
import org.example.Exceptions.AlreadyExistsException;
import org.example.Exceptions.InvalidFormatException;
import org.example.Exceptions.NoUserException;
import org.example.Exceptions.UnauthorizedException;
import org.example.Models.User;
import org.example.Services.AuthService;
import org.example.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/user/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Mono<ResponseEntity<User>> createUser(@RequestBody SignUpRequest user) {
        return userService.createUser(user)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    if (e instanceof AlreadyExistsException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null));
                    } else if (e instanceof InvalidFormatException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                    }
                });
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<String>> logUser(@RequestBody LoginRequest loginData) {
        return authService.logUser(loginData.email, loginData.password)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    if (ex instanceof UnauthorizedException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error"));
                    }
                });
    }


    // Update password
    @PutMapping("/password/{email}")
    public Mono<ResponseEntity<String>> updatePassword(@PathVariable String email) {
        return authService.setUpdatePassword(email)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    if (ex instanceof NoUserException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user exists"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error"));
                    }
                });
    }

    // Update User
    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody EditUser userDetails) {
        try {
            User updatedUser = userService.updateUser(email, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            System.out.println("Error: "+e);
            return ResponseEntity.notFound().build();
        }
    }



}
