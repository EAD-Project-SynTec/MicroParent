package org.example.Services;

import org.example.Exceptions.NoUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AuthService{

    @Autowired
   private final KeycloakAuthService keycloakAuthService;

    public AuthService(KeycloakAuthService keycloakAuthService) {
        this.keycloakAuthService = keycloakAuthService;
    }

    public Mono<String> createUser(String token, String firstName, String lastName, String email, String password) {
        return keycloakAuthService.createUser(token, firstName, lastName, email, password);
    }

}
