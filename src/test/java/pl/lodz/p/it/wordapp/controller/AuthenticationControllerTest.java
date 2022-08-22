package pl.lodz.p.it.wordapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
        String loginCredentials = String.format(loginCredentialsDtoFormat, "user5", "secure password");
        String registrationDto = String.format(registrationDtoFormat, "user5", "user5@gmail.com", "secure password");
        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginCredentials))
               .andDo(print())
               .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/register")
                            .content(registrationDto)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isAccepted())
               .andExpect(content().string(""));

        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginCredentials))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(header().exists("Authorization"));
    }

    @Test
    void registerBadRequestTest() throws Exception {
        String registrationDto = String.format(registrationDtoFormat, "user5", "user@", "weak");

        MvcResult mvcResult = mockMvc.perform(post("/register")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(registrationDto))
                                     .andDo(print())
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
               .andDo(print())
               .andExpect(status().isConflict())
               .andExpect(content().string("Account with this username already exists"));
    }

    @Test
    void registerEmailExistsTest() throws Exception {
        String registrationDto = String.format(registrationDtoFormat, "user5", "user4@d.com", "secure password");

        mockMvc.perform(post("/register")
                            .content(registrationDto)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
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
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }

    @Test
    void loginSuccessTest() throws Exception {
        String loginCredentials = String.format(loginCredentialsDtoFormat, "user3", "password");

        MvcResult mvcResult = mockMvc.perform(post("/login")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(loginCredentials))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        String header = mvcResult.getResponse().getHeader("Authorization");

        assertThat(header).isNotNull()
                          .contains("Bearer ");
    }
    // endregion
}
