package pl.lodz.p.it.wordapp.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.wordapp.model.Account;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationDto {
    @NotBlank(message = "Username must not be empty")
    private String username;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "You must give a valid email address")
    private String emailAddress;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public Account mapToAccount(PasswordEncoder encoder) {
        Account acc = new Account();
        acc.setUsername(username);
        acc.setEmailAddress(emailAddress);
        acc.setPassword(encoder.encode(password));

        return acc;
    }
}
