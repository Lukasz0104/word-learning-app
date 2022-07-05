package pl.lodz.p.it.wordapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.config.LoginCredentials;

@RestController
public class AuthenticationController {

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials credentials) {
    }

    @PostMapping("/register")
    public void register() {

    }

}
