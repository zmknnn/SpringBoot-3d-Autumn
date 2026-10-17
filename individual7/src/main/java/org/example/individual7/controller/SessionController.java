
package org.example.individual7.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @GetMapping("/session/public")
    public String publicEndpoint() {
        return "This endpoint is public";
    }

    @GetMapping("/session/private")
    public String privateEndpoint() {
        return "This endpoint is private. You are authenticated!";
    }
}
