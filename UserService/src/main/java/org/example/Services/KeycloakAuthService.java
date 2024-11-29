package org.example.Services;

import org.example.DTO.RoleJson;
import org.example.DTO.UserJson;
import org.example.Exceptions.AlreadyExistsException;
import org.example.Exceptions.NoUserException;
import org.example.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakAuthService implements IAuthService {

    private final WebClient webClient;

    @Value("${keycloak.clientId}")
    private String clientId;
    @Value("${keycloak.clientIdNo}")
    private String clientIdNo;
    @Value("${keycloak.clientSecret}")
    private String clientSecret;
    @Value("${keycloak.endpoint.user}")
    private String userEndpoint;
    @Value("${keycloak.endpoint.log}")
    private String logEndpoint;

    @Autowired
    public KeycloakAuthService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public Mono<String> clientLogin() {
        return webClient.post()
                .uri(logEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"))
                .onErrorResume(e ->{
                            return Mono.error(new RuntimeException("Unexpected error", e));
                        }
                );
    }

    @Override
    public Mono<String> userLogin(String email, String password) {
        return webClient.post()
                .uri(logEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("grant_type", "password")
                        .with("username", email)
                        .with("password", password)
                )
                .retrieve()
                .onStatus(status -> status == HttpStatus.UNAUTHORIZED, response -> Mono.error(new UnauthorizedException("Invalid credentials")))
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("access_token"))
                .onErrorResume(ex -> {
                    if (ex instanceof UnauthorizedException) {
                        return Mono.error(ex);
                    } else {
                        System.out.println(ex.getMessage());
                        return Mono.error(new RuntimeException("Unexpected error"));
                    }
                });
    }

    @Override
    public Mono<String> createUser(String token, String firstName, String lastName, String email, String password) {
        UserJson jsonobj = new UserJson(firstName, lastName, email, password);
        return webClient.post()
                .uri(userEndpoint)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue(jsonobj.mainObject.toString())
                .retrieve()
                .onStatus(status -> status == HttpStatus.CONFLICT, response -> Mono.error(new AlreadyExistsException("User already exists")))
                .toBodilessEntity()
                .flatMap(response -> {
                    if (response.getStatusCode() == HttpStatus.CREATED) {
                        String locationHeader = response.getHeaders().getFirst("Location");
                        assert locationHeader != null;
                        String userIdNo = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                        return Mono.just(userIdNo);
                    } else {
                        return Mono.error(new RuntimeException("Failed to create user"));
                    }
                })
                .onErrorResume(ex -> {
                    if (ex instanceof AlreadyExistsException) {
                        return Mono.error(ex);
                    } else {
                        System.out.println(ex.getMessage());
                        return Mono.error(new RuntimeException("Unexpected error"));
                    }
                });
    }

    @Override
    public Mono<Boolean> assignRole(String token, String userIdNo, String role) {
        RoleJson jsonobj = new RoleJson(role);
        System.out.println(jsonobj.mainObject.toString());
        return webClient.post()
                .uri(userEndpoint+"/"+userIdNo+"/role-mappings/clients/"+clientIdNo)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue(jsonobj.mainObject.toString())
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode() == HttpStatus.NO_CONTENT)
                .onErrorResume(ex -> {
                            System.out.println(ex.getMessage());
                            return Mono.error(new RuntimeException("Unexpected error"));
                        }
                );
    }

    @Override
    public Mono<Boolean> resetPassword(String userId, String token) {
        webClient.put()
                .uri(userEndpoint+"/"+userId+"/execute-actions-email")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("[\"UPDATE_PASSWORD\"]"))
                .retrieve()
                .onStatus(status -> status != HttpStatus.NO_CONTENT, response -> Mono.error(new RuntimeException("Unexpected error")))
                .bodyToMono(Map.class)
                .map(responseMap -> "Password updated")
                .onErrorResume(ex -> {
                    return Mono.error(new RuntimeException("Unexpected error"));
                });
        return null;
    }

    @Override
    public Mono<String[]> getUserId(String email, String token) {
        return webClient.get()
                .uri(userEndpoint + "?email=" + email)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(responseMap -> {
                    if (responseMap != null) {
                        String[] ar = new String[2];
                        if (!responseMap.isEmpty()) {
                            ar[1] = responseMap.get(0).get("id").toString();
                            ar[0] = token;
                            return ar;
                        }
                        throw new NoUserException("No user exists");
                    }
                    throw new RuntimeException("Unexpected error");
                })
                .onErrorResume(ex -> {
                    if (ex instanceof NoUserException) {
                        return Mono.error(ex);
                    } else {
                        return Mono.error(new RuntimeException("Unexpected error"));
                    }
                });
    }


}
