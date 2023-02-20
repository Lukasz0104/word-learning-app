package pl.lodz.p.it.wordapp.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.wordapp.model.AccessRole;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({ "userId", "username" })
public class UserPermissionsDto extends PermissionsDto {
    private Long userId;
    private String username;

    public UserPermissionsDto(AccessRole accessRole) {
        super(accessRole);
        this.userId = accessRole.getUser().getId();
        this.username = accessRole.getUser().getUsername();
    }
}
