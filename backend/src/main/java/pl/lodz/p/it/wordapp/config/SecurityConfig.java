package pl.lodz.p.it.wordapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import pl.lodz.p.it.wordapp.service.AccountService;
import pl.lodz.p.it.wordapp.service.JWTService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableScheduling
@EnableAsync
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final RestAuthenticationSuccessHandler successHandler;

    private final AccountService userDetailsService;
    private final JWTService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        return http.csrf().disable()
                   .authorizeRequests()
                   .antMatchers("/v3/api-docs/**").permitAll()
                   .antMatchers("/swagger-ui/**").permitAll()
                   .antMatchers("/h2-console/**").permitAll()
                   .antMatchers("/register").permitAll()
                   .antMatchers(HttpMethod.HEAD, "/users").permitAll()
                   .antMatchers(HttpMethod.GET, "/users").authenticated()
                   .antMatchers(HttpMethod.GET).permitAll()
                   .anyRequest().hasRole("USER")
                   .and()
                   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   .and()
                   .addFilter(authenticationFilter(authManager))
                   .addFilter(new JwtAuthorizationFilter(authManager, userDetailsService, jwtService))
                   .exceptionHandling()
                   .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                   .and()
                   .logout()
                   .addLogoutHandler(new RestLogoutHandler(jwtService))
                   .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                   .and()
                   .headers().frameOptions().disable()
                   .and()
                   .build();
    }

    public JsonObjectAuthenticationFilter authenticationFilter(AuthenticationManager authManager) {
        JsonObjectAuthenticationFilter authenticationFilter = new JsonObjectAuthenticationFilter(objectMapper);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationManager(authManager);
        return authenticationFilter;
    }
}
