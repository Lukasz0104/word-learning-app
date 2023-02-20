package pl.lodz.p.it.wordapp.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.wordapp.service.JWTService;

@Component
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        Optional<String> token = jwtService.generateToken(authentication.getName());

        token.ifPresent((tokenValue) -> {
            response.addHeader("Authorization", "Bearer " + tokenValue);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        });
    }
}
