package com.sbp.rest.client.controllers;

import com.sbp.rest.transients.NormalPayloadDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/client/restClient")
public class RestClientController {

    private static final String TARGET_HOST = "http://localhost:8085";
    private final RestClient restClient;

    public RestClientController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/getNoParam")
    public String callExternalServiceNoParam() {
        return restClient
                .get()
                .uri(TARGET_HOST + "/server/getNoParam")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/getWithDelay")
    public String getWithDelay() {

        long startTime = System.currentTimeMillis();

        restClient
                .get()
                .uri(TARGET_HOST + "/server/getWithDelay")
                .retrieve()
                .body(String.class);
        long duration = System.currentTimeMillis() - startTime;

        String message = "Ressource i now free after " + duration + " millis";

        return message;
    }

    @GetMapping("/getWithParam")
    public String callExternalServiceWithParam(@RequestParam String param1, @RequestParam String param2) {
        return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8085)
                        .path("/server/getWithParam")
                        .queryParam("param1", param1)
                        .queryParam("param2", param2)
                        .build())
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/postWithBody")
    public ResponseEntity callExternalServicePost(@RequestParam(defaultValue = "5") int value) {
        URI uri = URI.create(TARGET_HOST + "/server/postWithBody");
        NormalPayloadDTO payload = NormalPayloadDTO.builder().value(value).build() ;

        try {
            ResponseEntity<Void> response = restClient
                    .post()
                    .uri(uri)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Error from server: " + response.getBody());
            }
        }  catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body("Error occurred: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
