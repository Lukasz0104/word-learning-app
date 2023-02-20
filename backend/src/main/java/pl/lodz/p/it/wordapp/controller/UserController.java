package pl.lodz.p.it.wordapp.controller;

import javax.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.wordapp.controller.dto.ChangeEmailAddressDto;
import pl.lodz.p.it.wordapp.controller.dto.ChangePasswordDto;
import pl.lodz.p.it.wordapp.controller.dto.UserDto;
import pl.lodz.p.it.wordapp.exception.EmailAddressAlreadyTakenException;
import pl.lodz.p.it.wordapp.exception.EmailAddressNotDifferentException;
import pl.lodz.p.it.wordapp.exception.IncorrectEmailAddressException;
import pl.lodz.p.it.wordapp.exception.IncorrectPasswordException;
import pl.lodz.p.it.wordapp.exception.NewPasswordNotDifferentException;
import pl.lodz.p.it.wordapp.service.UserService;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAllUsers(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size) {
        name = (name != null && !name.isBlank()) ? name : "";
        int pageNo = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? size : 30;

        return userService.findAll(name, PageRequest.of(pageNo, pageSize));
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto)
        throws IncorrectPasswordException, NewPasswordNotDifferentException {
        userService.changePassword(changePasswordDto);
    }

    @PutMapping("/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmailAddress(@RequestBody @Valid ChangeEmailAddressDto changeEmailAddressDto)
        throws EmailAddressAlreadyTakenException, EmailAddressNotDifferentException, IncorrectEmailAddressException {
        userService.changeEmailAddress(changeEmailAddressDto);
    }
}
