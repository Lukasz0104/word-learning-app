package pl.lodz.p.it.wordapp.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.LoginCredentials;
import pl.lodz.p.it.wordapp.controller.dto.RegistrationDto;
import pl.lodz.p.it.wordapp.repository.AccountRepository;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginCredentials credentials) {
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(@Valid @RequestBody RegistrationDto registrationDto) {
        accountRepository.save(registrationDto.mapToAccount(encoder));
    }

}
