package pl.lodz.p.it.wordapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.lodz.p.it.wordapp.controller.dto.RegistrationDto;
import pl.lodz.p.it.wordapp.controller.dto.UserDto;
import pl.lodz.p.it.wordapp.model.Account;
import pl.lodz.p.it.wordapp.repository.AccountRepository;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String changePasswordDtoFormat = "{" +
                                                   "\"oldPassword\": \"%s\"," +
                                                   "\"newPassword\": \"%s\"," +
                                                   "\"confirmNewPassword\": \"%s\"" +
                                                   "}";

    private final String changeEmailAddressDtoFormat = "{" +
                                                       "\"oldEmailAddress\": \"%s\"," +
                                                       "\"newEmailAddress\": \"%s\"," +
                                                       "\"confirmNewEmailAddress\": \"%s\"" +
                                                       "}";

    // region findAllUsers
    @Test
    @WithAnonymousUser
    void findAllUsersAsAnonymousUserTest() throws Exception {
        mockMvc.perform(get("/users"))

               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user1")
    void findAllUsersAsUserNoParamsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users"))

                                     .andExpect(status().isOk())
                                     .andReturn();

        List<UserDto> users = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(users).isNotNull()
                         .hasSize(3);
    }

    @Test
    @WithUserDetails("user1")
    void findAllUsersAsUserWithNameParamTest() throws Exception {
        addTestUsers();

        MvcResult mvcResult = mockMvc.perform(get("/users")
                                                  .param("name", "An"))

                                     .andExpect(status().isOk())
                                     .andReturn();

        List<UserDto> users = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(users).isNotNull()
                         .hasSize(16);
    }

    @Test
    @WithUserDetails("user1")
    void findAllUsersAsUserWithPaginationTest() throws Exception {
        addTestUsers();

        MvcResult mvcResult;
        List<UserDto> users;

        mvcResult = mockMvc.perform(get("/users")
                                        .param("page", "1")
                                        .param("size", "16"))

                           .andExpect(status().isOk())
                           .andReturn();

        users = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(users).isNotNull()
                         .hasSize(16);

        mvcResult = mockMvc.perform(get("/users")
                                        .param("page", "3")
                                        .param("size", "16"))

                           .andExpect(status().isOk())
                           .andReturn();

        users = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(users).isNotNull()
                         .hasSize(11); // 7 * 8 + 3 - 4 * 16
    }

    @Test
    @WithUserDetails("user1")
    void findAllUsersAsUserWithPaginationInvalidParamsTest() throws Exception {
        addTestUsers();

        MvcResult mvcResult = mockMvc.perform(get("/users")
                                                  .param("page", "-1")
                                                  .param("size", "0"))

                                     .andExpect(status().isOk())
                                     .andReturn();

        List<UserDto> users = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(users).isNotNull()
                         .hasSize(30);
    }

    @Test
    @WithUserDetails("user1")
    void findAllUsersAsUserWithPaginationAndNameParamTest() throws Exception {
        addTestUsers();

        MvcResult mvcResult;
        List<UserDto> users;

        mvcResult = mockMvc.perform(get("/users")
                                        .param("name", "ma")
                                        .param("page", "0")
                                        .param("size", "15"))

                           .andExpect(status().isOk())
                           .andReturn();

        users = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(users).isNotNull()
                         .hasSize(15);

        mvcResult = mockMvc.perform(get("/users")
                                        .param("name", "MA")
                                        .param("page", "1")
                                        .param("size", "15"))

                           .andExpect(status().isOk())
                           .andReturn();

        users = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(users).isNotNull()
                         .hasSize(9); // 3 * 8 - 15
    }
    // endregion

    // region updatePassword
    @Test
    @WithAnonymousUser
    void updatePasswordAsAnonymousUserTest() throws Exception {
        String dto = String.format(changePasswordDtoFormat, "pass", "new password", "new password");

        mockMvc.perform(put("/users/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user1")
    void updatePasswordAsUser() throws Exception {
        String dto = String.format(changePasswordDtoFormat, "abc", "new password", "new password");

        mockMvc.perform(put("/users/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isNoContent());

        // attempt to log in with new password
        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                     "\"username\": \"user1\"," +
                                     "\"password\": \"new password\"" +
                                     "}"))

               .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("user1")
    void updatePasswordAsUserPasswordsNotMatchingTest() throws Exception {
        String dto = String.format(changePasswordDtoFormat, "abc", "new password", "different password");

        mockMvc.perform(put("/users/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isBadRequest())
               .andExpect(content().string(
                   "{\"confirmPasswordTheSame\":\"Confirm password does not match new password\"}"));
    }
    // endregion

    // region updateEmailAddress
    @Test
    @WithAnonymousUser
    void upateEmailAddressAsAnonymousUserTest() throws Exception {
        String dto = String.format(changeEmailAddressDtoFormat, "user1@a.com", "user1@new.com", "user1@new.com");

        mockMvc.perform(put("/users/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user1")
    void updateEmailAddressAsUserTest() throws Exception {
        String dto = String.format(changeEmailAddressDtoFormat, "user1@a.com", "user1@new.com", "user1@new.com");

        mockMvc.perform(put("/users/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("user1")
    void updateEmailAddressAsUserWrongEmailAddressTest() throws Exception {
        String dto = String.format(changeEmailAddressDtoFormat, "user1@A.com", "user1@new.com", "user1@new.com");

        mockMvc.perform(put("/users/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user1")
    void updateEmailAddressAsUserBodyNotValidTest() throws Exception {
        String dto = String.format(changeEmailAddressDtoFormat, "user1@a.com", "user1", "user1@new.com");

        mockMvc.perform(put("/users/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user1")
    void updateEmailAddressAsUserConfirmEmailAddressNotMatchingTest() throws Exception {
        String dto = String.format(changeEmailAddressDtoFormat, "user1@a.com", "user1@new.com", "user1@new.com.pl");

        mockMvc.perform(put("/users/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user1")
    void updateEmailAddressAsUserEmailAddressTakenTest() throws Exception {
        String dto = String.format(changeEmailAddressDtoFormat, "user1@a.com", "user2@b.com", "user2@b.com");

        mockMvc.perform(put("/users/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isConflict())
               .andExpect(content().string("This email address is already taken"));
    }

    @Test
    @WithUserDetails("user1")
    void updateEmailAddressAsUserNewEmailAddressTheSameTest() throws Exception {
        String dto = String.format(changeEmailAddressDtoFormat, "user1@a.com", "user1@a.com", "user1@a.com");

        mockMvc.perform(put("/users/email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))

               .andExpect(status().isBadRequest())
               .andExpect(content().string("New email address must differ from the old email address"));
    }
    // endregion

    private void addTestUsers() {
        List<Account> testUsers = new ArrayList<>();
        RegistrationDto registrationDto;

        var names = new String[] { "Anna", "Andrew", "Daniel", "David", "Maria", "Mark", "Matthew" };

        for (var name : names) {
            for (int i = 0; i < 8; i++) {
                registrationDto = new RegistrationDto(String.format("%s%d", name, i),
                                                      String.format("%s%d@gmail.com", name.toLowerCase(), i),
                                                      "password"
                );

                testUsers.add(registrationDto.mapToAccount(passwordEncoder));
            }
        }

        accountRepository.saveAll(testUsers);
    }
}
