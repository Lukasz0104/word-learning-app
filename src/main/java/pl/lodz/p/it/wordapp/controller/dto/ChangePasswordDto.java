package pl.lodz.p.it.wordapp.controller.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotBlank
    private String oldPassword;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String confirmNewPassword;

    @AssertTrue(message = "Confirm password does not match new password")
    private boolean isConfirmPasswordTheSame() {
        return Objects.equals(newPassword.toLowerCase(), confirmNewPassword.toLowerCase());
    }
}
