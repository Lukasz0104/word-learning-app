package pl.lodz.p.it.wordapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.wordapp.model.AccessRole;

public interface AccessRoleRepository extends JpaRepository<AccessRole, Long> {
    Optional<AccessRole> findBySet_IdAndUser_Id(Long setId, Long userId);

    List<AccessRole> findBySet_IdOrderById(Long setId);
}
