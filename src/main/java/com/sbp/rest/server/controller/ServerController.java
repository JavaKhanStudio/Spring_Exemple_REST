package com.sbp.rest.server.controller;

import com.sbp.rest.transients.NormalPayloadDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/server")
public class ServerController {


    @GetMapping("/getNoParam")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/getWithDelay")
    public String getNoParamWithDelay(@RequestParam(defaultValue = "5000") int delay) throws InterruptedException {
        Thread.sleep(delay);
        return "Hello World";
    }

    @GetMapping("/getWithParam")
    public String invertParams(@RequestParam String param1, @RequestParam String param2) {
        return "Inverted: " + param2 + ", " + param1;
    }

    @PostMapping("/postWithBody")
    public ResponseEntity<String> checkPrice(@RequestBody NormalPayloadDTO requestBody) {
        try {
            int price = requestBody.getValue();
            if (price < 10) {
                return ResponseEntity.ok("OK");
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Value is too high");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid value format");
        }
    }

}
