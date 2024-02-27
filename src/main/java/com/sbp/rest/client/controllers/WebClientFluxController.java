package com.sbp.rest.client.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping(path = "/client/webClientFlux")
public class WebClientFluxController {

    private static final String TARGET_HOST = "http://localhost:8085/server/flux";
    private final WebClient webClient;
    // Facon Thread safe de retenir mon flux
    private final AtomicReference<Disposable> disposableRef = new AtomicReference<>();


    public WebClientFluxController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(TARGET_HOST).build();
    }

    public Flux<String> consumeMessages() {
        return this.webClient.get()
                .uri("/getSimple")
                .retrieve()
                .bodyToFlux(String.class);
    }

    @GetMapping("/simpleSub/start")
    public String getFlux() {

        Disposable disposable = consumeMessages().subscribe(
                message -> System.out.println("Received: " + message),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Stream completed")
        );

        disposableRef.set(disposable);

        return "Flux started";
    }

    @GetMapping("/simpleSub/stop")
    public String stopFlux() {
        Disposable disposable = disposableRef.getAndSet(null);
        if (disposable != null) {
            disposable.dispose();
        }

        return "Flux stopped";
    }

}
