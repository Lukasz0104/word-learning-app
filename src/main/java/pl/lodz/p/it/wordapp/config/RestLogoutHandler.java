package pl.lodz.p.it.wordapp.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.wordapp.service.JWTService;

@Component
@RequiredArgsConstructor
public class RestLogoutHandler implements LogoutHandler {

    private final JWTService jwtService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String token = request.getHeader("Authorization");

        jwtService.invalidateToken(token);
    }
}
