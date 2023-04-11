package pl.lodz.p.it.wordapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import pl.lodz.p.it.wordapp.service.JWTService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

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
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response,
                             Principal principal) {
        String oldToken = request.getHeader("Authorization");

        Optional<String> token = jwtService.generateToken(principal.getName());
        token.ifPresent(newToken -> {
            response.addHeader("Authorization", "Bearer " + newToken);
            response.addHeader("Access-Control-Expose-Headers", "*");
        });

        jwtService.invalidateToken(oldToken);
    }

    /**
     * Empty endpoint to enable logging out in SwaggerUI.
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() { }
}
