package org.example.Services;

import org.example.Exceptions.AlreadyExistsException;
import org.example.Exceptions.InvalidFormatException;
import org.example.Models.User;
import org.example.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    // Create User
    public Mono<Optional<User>> createUser(User user) {
        return authService.addUser(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getRole())
                .flatMap(result -> {
                    try {
                        User savedUser = userRepository.save(user);
                        return Mono.just(Optional.of(savedUser));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Unexpected error", e));
                    }
                })
                .onErrorResume(e -> {
                    if (e instanceof AlreadyExistsException) {
                        return Mono.error(e);
                    } else if (e instanceof InvalidFormatException) {
                        return Mono.error(new InvalidFormatException(e.getMessage()));
                    } else {
                        return Mono.error(new RuntimeException("Unexpected error"));
                    }
                })
                .defaultIfEmpty(Optional.empty());
    }

    // Get User by Email
    public Optional<User> getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findById(email);
        System.out.println("userOptional: "+userOptional);
        return userOptional;
    }

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update User
    public User updateUser(String email, User userDetails) {
        return userRepository.findById(email).map(user -> {
            user.setPassword(userDetails.getPassword());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setAddressLine1(userDetails.getAddressLine1());
            user.setAddressLine2(userDetails.getAddressLine2());
            user.setAddressLine3(userDetails.getAddressLine3());
            user.setProfilePhoto(userDetails.getProfilePhoto());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Delete User
    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }
}
