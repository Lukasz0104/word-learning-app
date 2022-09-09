package pl.lodz.p.it.wordapp.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.controller.dto.ChangeEmailAddressDto;
import pl.lodz.p.it.wordapp.controller.dto.ChangePasswordDto;
import pl.lodz.p.it.wordapp.controller.dto.UserDto;
import pl.lodz.p.it.wordapp.exception.EmailAddressAlreadyTakenException;
import pl.lodz.p.it.wordapp.exception.EmailAddressNotDifferentException;
import pl.lodz.p.it.wordapp.exception.IncorrectEmailAddressException;
import pl.lodz.p.it.wordapp.exception.IncorrectPasswordException;
import pl.lodz.p.it.wordapp.exception.NewPasswordNotDifferentException;
import pl.lodz.p.it.wordapp.model.Account;
import pl.lodz.p.it.wordapp.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;

    public List<UserDto> findAll(String name, Pageable page) {
        final Long currentUserId = AccountService.getCurrentUserId();
        return accountRepository.findByIdNotAndUsernameStartsWithIgnoreCase(currentUserId, name, page);
    }

    public void changePassword(ChangePasswordDto passwordDto)
        throws IncorrectPasswordException, NewPasswordNotDifferentException {
        Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (accountObj instanceof Account account) {
            if (!encoder.matches(passwordDto.getOldPassword(), account.getPassword())) {
                throw new IncorrectPasswordException();
            }

            if (passwordDto.getOldPassword().equals(passwordDto.getNewPassword())) {
                throw new NewPasswordNotDifferentException();
            }

            String newPasswordEncrypted = encoder.encode(passwordDto.getNewPassword());
            account.setPassword(newPasswordEncrypted);

            accountRepository.save(account);
        }
    }

    public void changeEmailAddress(ChangeEmailAddressDto dto)
        throws IncorrectEmailAddressException, EmailAddressNotDifferentException, EmailAddressAlreadyTakenException {
        Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (accountObj instanceof Account account) {
            if (!Objects.equals(account.getEmailAddress(), dto.getOldEmailAddress())) {
                throw new IncorrectEmailAddressException();
            }

            if (Objects.equals(account.getEmailAddress(), dto.getNewEmailAddress())) {
                throw new EmailAddressNotDifferentException();
            }

            if (accountRepository.existsByEmailAddress(dto.getNewEmailAddress())) {
                throw new EmailAddressAlreadyTakenException();
            }

            account.setEmailAddress(dto.getNewEmailAddress());

            accountRepository.save(account);
        }
    }
}
