package pl.lodz.p.it.wordapp.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.expirationTime}")
    long expirationTime;

    @Value("${jwt.secret}")
    String secret;

    /**
     * Empty endpoint to enable authentication in SwaggerUI.
     *
     * @param ignoredCredentials User's credentials
     */
    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginCredentials ignoredCredentials) { }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(@Valid @RequestBody RegistrationDto registrationDto)
        throws UserAlreadyExistsException, EmailAddressAlreadyTakenException {
        accountService.registerUser(registrationDto, passwordEncoder);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshToken(HttpServletResponse response,
                             Principal principal) {
        // TODO invalidate old token
        String token = JWT.create()
                          .withSubject(principal.getName())
                          .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                          .sign(Algorithm.HMAC256(secret));
        response.addHeader("Authorization", "Bearer " + token);
    }
}
