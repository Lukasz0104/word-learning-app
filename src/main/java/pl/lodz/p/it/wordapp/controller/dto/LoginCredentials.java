package pl.lodz.p.it.wordapp.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginCredentials {
    @NotBlank(message = "Username must not be empty")
    private String username;

    @NotBlank(message = "Password must not be empty")
    private String password;
}
