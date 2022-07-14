package pl.lodz.p.it.wordapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.wordapp.model.AccessRole;

public interface AccessRoleRepository extends JpaRepository<AccessRole, Long> {
    AccessRole findBySet_IdAndUser_Id(Long setId, Long userId);
}
