package com.sbp.rest.client.controllers;

import com.sbp.rest.transients.NormalPayloadDTO;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/client/restTemplate")
public class RestTemplateController {

    private static final String TARGET_HOST = "http://localhost:8085/server";
    private final RestTemplate restTemplate;

    public RestTemplateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/getError")
    public String getError() {
        String url = TARGET_HOST + "/noPath";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/getWithDelay")
    public String getWithDelay() {

        long startTime = System.currentTimeMillis();

        String url = TARGET_HOST + "/getWithDelay";
        restTemplate.getForObject(url, String.class) ;
        long duration = System.currentTimeMillis() - startTime;

        String message = "Ressource i now free after " + duration + " millis";

        return message;
    }

    @GetMapping("/getNoParam")
    public String callExternalServiceNoParam() {
        String url = TARGET_HOST + "/getNoParam";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/getWithParam")
    public String callExternalServiceWithParam(@RequestParam String param1, @RequestParam String param2) {
        String url = TARGET_HOST + "/getWithParam?param1={param1}&param2={param2}";
        Map<String, String> params = new HashMap<>();
        params.put("param1", param1);
        params.put("param2", param2);
        return restTemplate.getForObject(url, String.class, params);
    }

    @GetMapping("/postWithBody")
    public ResponseEntity<String> callExternalServicePost(@RequestParam(defaultValue = "5") int value) {
        String url = TARGET_HOST + "/postWithBody";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<NormalPayloadDTO> request = new HttpEntity<>(NormalPayloadDTO.builder().value(value).build(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity
                        .status(response.getStatusCode())
                        .body("Error from server: " + response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
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
