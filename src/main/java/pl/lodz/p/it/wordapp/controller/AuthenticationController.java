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
import pl.lodz.p.it.wordapp.exception.EmailAddressAlreadyTakenException;
import pl.lodz.p.it.wordapp.exception.UserAlreadyExistsException;
import pl.lodz.p.it.wordapp.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginCredentials credentials) { }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(@Valid @RequestBody RegistrationDto registrationDto)
        throws UserAlreadyExistsException, EmailAddressAlreadyTakenException {
        accountService.registerUser(registrationDto, passwordEncoder);
    }
}
