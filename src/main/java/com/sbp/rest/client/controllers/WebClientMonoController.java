package com.sbp.rest.client.controllers;

import com.sbp.rest.transients.NormalPayloadDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/client/webClientMono")
public class WebClientMonoController {

    private static final String TARGET_HOST = "http://localhost:8085";
    private final WebClient webClient;

    public WebClientMonoController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(TARGET_HOST).build();
    }

    @GetMapping("/getNoParam")
    public Mono<String> callExternalServiceNoParam() {
        return webClient
                .get()
                .uri("/server/getNoParam")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/getWithDelay")
    public Mono<String> getWithDelay() {
        long startTime = System.currentTimeMillis();

        return webClient
                .get()
                .uri("/server/getWithDelay")
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> {
                    long duration = System.currentTimeMillis() - startTime;
                    return "Resource is now free after " + duration + " millis";
                });
    }

    @GetMapping("/getWithParam")
    public Mono<String> callExternalServiceWithParam(@RequestParam String param1, @RequestParam String param2) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/server/getWithParam")
                        .queryParam("param1", param1)
                        .queryParam("param2", param2)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/postWithBody")
    public Mono<ResponseEntity<?>> callExternalServicePost(@RequestParam(defaultValue = "5") int value) {
        NormalPayloadDTO payload = NormalPayloadDTO.builder().value(value).build();

        return webClient
                .post()
                .uri("/server/postWithBody")
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .map(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(response.getStatusCode()).body("Error from server");
                    }
                })
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException ex = (WebClientResponseException) e;
                        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body("Error occurred: " + ex.getResponseBodyAsString()));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage()));
                });
    }

}
