package pl.lodz.p.it.wordapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.wordapp.model.Account;
import pl.lodz.p.it.wordapp.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final AccountRepository repository;

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Retrieve id of the user, that is currently logged in.
     *
     * @return user's ID if user is authenticated, otherwise null.
     */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;

        if (auth.getPrincipal() instanceof Account) {
            userId = ((Account) auth.getPrincipal()).getId();
        }
        return userId;
    }
}
