package com.sbp.rest.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping(path = "/server/flux")
public class ServerFluxController {

    @GetMapping(value = "/getSimple", produces = "text/event-stream")
    public Flux<String> sendMessages() {
        return Flux.interval(Duration.ofSeconds(1)) // Génère un signal toutes les 1 secondes
                .map(sequence -> "Message " + sequence); // Attache un message à chaque signal
    }

}
