package pl.lodz.p.it.wordapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DirtiesContext
class AuthenticationControllerTest {

    @Value("${jwt.secret}")
    private String secret;

    private final String registrationDtoFormat = "{" +
                                                 "\"username\": \"%s\"," +
                                                 "\"emailAddress\": \"%s\"," +
                                                 "\"password\": \"%s\"" +
                                                 "}";

    private final String loginCredentialsDtoFormat = "{" +
                                                     "\"username\": \"%s\"," +
                                                     "\"password\": \"%s\"" +
                                                     "}";

    @Autowired
    private MockMvc mockMvc;

    // region AuthenticationController::register
    @Test
    void registerAndLoginSuccessTest() throws Exception {
        String registrationDto = String.format(registrationDtoFormat, "user5", "user5@gmail.com", "secure password");
        String loginCredentials = String.format(loginCredentialsDtoFormat, "user5", "secure password");

        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginCredentials))

               .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/register")
                            .content(registrationDto)
                            .contentType(MediaType.APPLICATION_JSON))

               .andExpect(status().isAccepted())
               .andExpect(content().string(""));

        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginCredentials))

               .andExpect(status().isNoContent())
               .andExpect(header().exists("Authorization"));
    }

    @Test
    void registerBadRequestTest() throws Exception {
        String registrationDto = String.format(registrationDtoFormat, "user5", "user@", "weak");

        MvcResult mvcResult = mockMvc.perform(post("/register")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(registrationDto))

                                     .andExpect(status().isBadRequest())
                                     .andReturn();

        String body = mvcResult.getResponse().getContentAsString();

        assertThat(body).isNotNull()
                        .contains("You must give a valid email address")
                        .contains("Password must be at least 8 characters long");
    }

    @Test
    void registerUserExistsTest() throws Exception {
        String registrationDto = String.format(registrationDtoFormat, "user4", "user5@gmail.com", "strong password");

        mockMvc.perform(post("/register")
                            .content(registrationDto)
                            .contentType(MediaType.APPLICATION_JSON))

               .andExpect(status().isConflict())
               .andExpect(content().string("This username is already taken"));
    }

    @Test
    void registerEmailExistsTest() throws Exception {
        String registrationDto = String.format(registrationDtoFormat, "user5", "user4@d.com", "secure password");

        mockMvc.perform(post("/register")
                            .content(registrationDto)
                            .contentType(MediaType.APPLICATION_JSON))

               .andExpect(status().isConflict())
               .andExpect(content().string("This email address is already taken"));
    }
    // endregion

    // region AuthenticationController::login
    @Test
    void loginFailTest() throws Exception {
        String loginCredentials = String.format(loginCredentialsDtoFormat, "user5", "secure password");

        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginCredentials))

               .andExpect(status().isUnauthorized());
    }

    @Test
    void loginSuccessTest() throws Exception {
        String loginCredentials = String.format(loginCredentialsDtoFormat, "user3", "password");

        MvcResult mvcResult = mockMvc.perform(post("/login")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(loginCredentials))

                                     .andExpect(status().isNoContent())
                                     .andExpect(header().exists("Authorization"))
                                     .andReturn();

        String header = mvcResult.getResponse().getHeader("Authorization");

        assertThat(header).isNotNull()
                          .contains("Bearer ");
    }
    // endregion

    // region AuthenticationController::refreshToken
    @Test
    void refreshTokenTest() throws Exception {
        String credentials = String.format(loginCredentialsDtoFormat, "user1", "abc");

        MvcResult loginResponse = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                                                                .content(credentials))

                                         .andExpect(status().isNoContent())
                                         .andExpect(header().exists("Authorization"))
                                         .andReturn();

        String oldToken = loginResponse.getResponse().getHeader("Authorization");

        assertThat(oldToken).isNotNull()
                            .startsWith("Bearer ");

        // wait a few seconds before refreshing the token
        TimeUnit.SECONDS.sleep(2);

        MvcResult refreshTokenResponse = mockMvc.perform(post("/refresh-token")
                                                             .header("Authorization", oldToken))

                                                .andExpect(status().isNoContent())
                                                .andExpect(header().exists("Authorization"))
                                                .andReturn();

        String newToken = refreshTokenResponse.getResponse().getHeader("Authorization");

        assertThat(newToken).isNotNull()
                            .startsWith("Bearer ")
                            .isNotEqualTo(oldToken);

        // verify that old token is no longer valid
        mockMvc.perform(post("/refresh-token")
                            .header("Authorization", oldToken))

               .andExpect(status().isUnauthorized());
    }
    // endregion

    // region authentication with JWT
    @Test
    void authenticationWithExpiredTokenTest() throws Exception {
        Calendar calendar = new Calendar.Builder().setDate(2000, 1, 1)
                                                  .build();
        String expiredToken = JWT.create()
                                 .withSubject("user1")
                                 .withExpiresAt(calendar.getTime())
                                 .sign(Algorithm.HMAC256(secret));

        mockMvc.perform(get("/users").header("Authorization", "Bearer " + expiredToken))

               .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticationWithAlgorithmMismatchTest() throws Exception {
        String token = JWT.create()
                          .withSubject("user1")
                          .withExpiresAt(Date.from(Instant.now().plusSeconds(30 * 60)))
                          .sign(Algorithm.HMAC384(secret));

        mockMvc.perform(get("/users").header("Authorization", "Bearer " + token))

               .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticationWithInvalidSignatureTest() throws Exception {
        String token = JWT.create()
                          .withSubject("user1")
                          .withExpiresAt(Date.from(Instant.now().plusSeconds(30 * 60)))
                          .sign(Algorithm.HMAC256(secret + "123"));

        mockMvc.perform(get("/users").header("Authorization", "Bearer " + token))

               .andExpect(status().isUnauthorized());
    }
    // endregion

    // region AuthenticationController::logout
    @Test
    void logoutTest() throws Exception {
        String credentials = String.format(loginCredentialsDtoFormat, "user1", "abc");

        MvcResult loginResult = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                                                              .content(credentials))

                                       .andExpect(status().isNoContent())
                                       .andExpect(header().exists("Authorization"))
                                       .andReturn();
        String authToken = loginResult.getResponse().getHeader("Authorization");

        assertThat(authToken).isNotNull()
                             .startsWith("Bearer ");

        mockMvc.perform(post("/logout").header("Authorization", authToken))

               .andExpect(status().isNoContent());

        mockMvc.perform(get("/users").header("Authorization", authToken))

               .andExpect(status().isUnauthorized());
    }
    // endregion
}
