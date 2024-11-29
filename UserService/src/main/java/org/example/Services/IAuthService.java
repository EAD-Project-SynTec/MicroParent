package org.example.Services;

import reactor.core.publisher.Mono;

public interface IAuthService {
    Mono<String> createUser(String token, String firstName, String lastName, String email, String password);
    Mono<String> clientLogin();
    Mono<Boolean> assignRole(String token,String userIdNo, String role);
    Mono<Boolean> resetPassword(String userId);
}
