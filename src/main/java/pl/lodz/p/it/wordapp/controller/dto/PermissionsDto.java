package pl.lodz.p.it.wordapp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.wordapp.model.AccessRole;
import pl.lodz.p.it.wordapp.model.Role;

@Getter
@NoArgsConstructor
public class PermissionsDto {
    private boolean ableToRead = false;
    private boolean ableToProposeChanges = false;
    private boolean ableToEdit = false;
    private boolean ableToManage = false;

    public PermissionsDto(Role role) {
        switch (role) {
            case READER -> ableToRead = true;
            case PROPOSER -> {
                ableToRead = true;
                ableToProposeChanges = true;
            }
            case EDITOR -> {
                ableToRead = true;
                ableToProposeChanges = true;
                ableToEdit = true;
            }
            case OWNER -> {
                ableToRead = true;
                ableToProposeChanges = true;
                ableToEdit = true;
                ableToManage = true;
            }
        }
    }

    public PermissionsDto(AccessRole accessRole) {
        this(accessRole.getRole());
    }
}
