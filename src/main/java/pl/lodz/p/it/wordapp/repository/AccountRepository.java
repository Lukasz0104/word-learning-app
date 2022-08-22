package pl.lodz.p.it.wordapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.wordapp.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmailAddress(String emailAddress);
}
