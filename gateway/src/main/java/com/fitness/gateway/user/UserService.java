package com.fitness.gateway.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final WebClient.Builder webClientBuilder;

    public Mono<Boolean> validateUser(String userId) {
        String url = "http://USER-SERVICE/api/users/" + userId + "/validate";
        log.info("Calling User Validation API for userId: {}", userId);

            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class, e -> {
                    	if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                    		return Mono.error(new RuntimeException("User Not Found: "+ userId));
                    	else if(e.getStatusCode() == HttpStatus.BAD_REQUEST)
                    		return Mono.error(new RuntimeException("Invalid Request: "+ userId));
                    	return Mono.error(new RuntimeException("Unexpected error: "+ e.getMessage()));
                    });
    }
    
    public Mono<UserResponse> registerUser(RegisterRequest request) {
    	String url = "http://USER-SERVICE/api/users/register";
        log.info("Calling User Registration API for email: {}", request.getEmail());

            return webClientBuilder.build()
                    .post()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .onErrorResume(WebClientResponseException.class, e -> {
                    	if(e.getStatusCode() == HttpStatus.BAD_REQUEST)
                    		return Mono.error(new RuntimeException("Bad Request: "+ e.getMessage()));
                    	else if(e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                    		return Mono.error(new RuntimeException("Internal server error: "+ e.getMessage()));
                    	return Mono.error(new RuntimeException("Unexpected error: "+ e.getMessage()));
                    });
    }
}
