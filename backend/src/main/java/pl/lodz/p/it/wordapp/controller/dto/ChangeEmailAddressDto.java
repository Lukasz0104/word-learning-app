package pl.lodz.p.it.wordapp.controller.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.Data;

@Data
public class ChangeEmailAddressDto {

    @NotBlank(message = "Email must not be empty")
    @Email(message = "You must provide a valid email address")
    private String oldEmailAddress;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "You must provide a valid email address")
    private String newEmailAddress;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "You must provide a valid email address")
    private String confirmNewEmailAddress;

    @AssertTrue(message = "Confirm email address does not match new email address")
    private boolean isConfirmEmailTheSame() {
        return Objects.equals(newEmailAddress, confirmNewEmailAddress);
    }
}
