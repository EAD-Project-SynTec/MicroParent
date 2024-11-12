package org.example.Services;

import org.example.Models.User;
import org.example.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Create User
    public User createUser(User user) {
        return userRepository.save(user);
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
            user.setUserName(userDetails.getUserName());
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