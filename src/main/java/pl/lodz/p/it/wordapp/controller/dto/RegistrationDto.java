package pl.lodz.p.it.wordapp.controller.dto;

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
    private String username;
    private String emailAddress;
    private String password;

    public Account mapToAccount(PasswordEncoder encoder) {
        Account acc = new Account();
        acc.setUsername(username);
        acc.setEmailAddress(emailAddress);
        acc.setPassword(encoder.encode(password));

        return acc;
    }
}
