package pl.lodz.p.it.wordapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;
import pl.lodz.p.it.wordapp.model.LearningSet;
import pl.lodz.p.it.wordapp.repository.LearningSetRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext
class LearningSetControllerTest {

    private final static String createLearningSetDtoJsonFormat = "{" +
        "\"title\": \"%s\"," +
        "\"publiclyVisible\": %b," +
        "\"termLanguage\": \"%s\"," +
        "\"translationLanguage\": \"%s\"" +
        "}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LearningSetRepository setRepository;

    // region LearningSetController::all
    @Test
    @WithUserDetails("user1")
    void allAsUserWithoutParamsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(3)
                        .allSatisfy(item -> assertThat(item).isNotNull());

        assertThat(sets.get(0)).isNotNull()
                               .matches(LearningSetDetailsDto::isPubliclyVisible)
                               .matches(dto -> Objects.equals(dto.getTermLanguage(), "de"))
                               .matches(dto -> Objects.equals(dto.getTranslationLanguage(), "pl"));

        assertThat(sets.get(1)).isNotNull()
                               .matches(dto -> !dto.isPubliclyVisible())
                               .matches(dto -> Objects.equals(dto.getTermLanguage(), "de"))
                               .matches(dto -> Objects.equals(dto.getTranslationLanguage(), "en"));

        assertThat(sets.get(2)).isNotNull()
                               .matches(dto -> !dto.isPubliclyVisible())
                               .matches(dto -> Objects.equals(dto.getTermLanguage(), "pl"))
                               .matches(dto -> Objects.equals(dto.getTranslationLanguage(), "en"));
    }

    @Test
    @WithAnonymousUser
    void allAsAnonymousUserWithoutParamsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(1)
                        .allSatisfy(item -> assertThat(item).isNotNull());
    }

    @Test
    @WithUserDetails("user4")
    void allAsUserWithTranslationLanguageParamTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets").param("translationLanguages", "en"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(2)
                        .allSatisfy(item -> {
                            assertThat(item).isNotNull();
                            assertThat(item.getTranslationLanguage()).isEqualTo("en");
                        });
    }

    @Test
    @WithUserDetails("user1")
    void allAsUserWithTermLanguageParamTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets").param("termLanguages", "de"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(2)
                        .allSatisfy(item -> {
                            assertThat(item).isNotNull();
                            assertThat(item.getTermLanguage()).isEqualTo("de");
                        });
    }

    @Test
    @WithUserDetails("user1")
    void allAsUserWithTitleParamTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets").param("titlePattern", "german"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(1)
                        .allSatisfy(item -> {
                            assertThat(item).isNotNull();
                            assertThat(item.getTitle()).isNotNull()
                                                       .contains("german");
                        });
    }

    @Test
    @WithUserDetails("user2")
    void allAsUserWithTermLanguageAndTranslationLanguageParamTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets").param("termLanguages", "de")
                                                          .param("translationLanguages", "en"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(1)
                        .allSatisfy(item -> {
                            assertThat(item).isNotNull();
                            assertThat(item.getTermLanguage()).isEqualTo("de");
                            assertThat(item.getTranslationLanguage()).isEqualTo("en");
                        });
    }

    @Test
    @WithAnonymousUser
    void allAsAnonymousUserWithPaginationTest() throws Exception {
        // load test data
        List<LearningSet> testSets = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            LearningSet set = new LearningSet();
            set.setPubliclyVisible(true);
            set.setTitle("Set " + i);
            set.setTermLanguage("en");
            set.setTranslationLanguage("pl");
            testSets.add(set);
        }
        setRepository.saveAll(testSets);

        MvcResult mvcResult = mockMvc.perform(get("/sets"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets;
        sets = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(24)
                        .allSatisfy(item -> assertThat(item).isNotNull());

        // same result should be obtained when page is explicitly set to 0
        mvcResult = mockMvc.perform(get("/sets").param("page", "0"))
                           .andDo(print())
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn();

        sets = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(24)
                        .allSatisfy(item -> assertThat(item).isNotNull());

        mvcResult = mockMvc.perform(get("/sets").param("page", "1"))
                           .andDo(print())
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn();

        sets = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(7)
                        .allSatisfy(item -> assertThat(item).isNotNull());
    }

    @Test
    @WithUserDetails("user4")
    void allAsUserWithPaginationTest() throws Exception {
        // load test data
        List<LearningSet> testSets = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            LearningSet set = new LearningSet();
            set.setPubliclyVisible(true);
            set.setTitle("Set " + i);
            set.setTermLanguage("en");
            set.setTranslationLanguage("pl");
            testSets.add(set);
        }
        setRepository.saveAll(testSets);

        MvcResult mvcResult = mockMvc.perform(get("/sets"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetDetailsDto> sets;
        sets = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(24)
                        .allSatisfy(item -> assertThat(item).isNotNull());

        // same result should be obtained when page is explicitly set to 0
        mvcResult = mockMvc.perform(get("/sets").param("page", "0"))
                           .andDo(print())
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn();

        sets = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(24)
                        .allSatisfy(item -> assertThat(item).isNotNull());

        mvcResult = mockMvc.perform(get("/sets").param("page", "1"))
                           .andDo(print())
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn();

        sets = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(sets).isNotNull()
                        .hasSize(9)
                        .allSatisfy(item -> assertThat(item).isNotNull());
    }
    // endregion

    // region LearningSetController::one
    @Test
    @WithUserDetails("user1")
    void oneAsUserSuccessTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        LearningSetDetailsDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                              LearningSetDetailsDto.class
        );

        assertThat(result).isNotNull()
                          .satisfies(dto -> {
                              assertThat(dto.getId()).isEqualTo(1L);
                              assertThat(dto.isPubliclyVisible()).isTrue();
                              assertThat(dto.getTitle()).isNotNull()
                                                        .contains("niemieckie");
                              assertThat(dto.getTermLanguage()).isNotNull()
                                                               .isEqualTo("de");
                              assertThat(dto.getTranslationLanguage()).isNotNull()
                                                                      .isEqualTo("pl");
                          });
    }

    @Test
    @WithUserDetails("user1")
    void oneAsUserPrivateSetSuccessTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/2"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        LearningSetDetailsDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                              LearningSetDetailsDto.class
        );

        assertThat(result).isNotNull()
                          .satisfies(dto -> {
                              assertThat(dto.getId()).isEqualTo(2L);
                              assertThat(dto.isPubliclyVisible()).isFalse();
                              assertThat(dto.getTitle()).isNotNull()
                                                        .contains("animals in");
                              assertThat(dto.getTermLanguage()).isNotNull()
                                                               .isEqualTo("de");
                              assertThat(dto.getTranslationLanguage()).isNotNull()
                                                                      .isEqualTo("en");
                          });
    }

    @Test
    @WithAnonymousUser
    void oneAsAnonymousUserSuccessTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        LearningSetDetailsDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                              LearningSetDetailsDto.class
        );

        assertThat(result).isNotNull()
                          .satisfies(dto -> {
                              assertThat(dto.getId()).isEqualTo(1L);
                              assertThat(dto.isPubliclyVisible()).isTrue();
                              assertThat(dto.getTitle()).isNotNull()
                                                        .contains("niemieckie");
                              assertThat(dto.getTermLanguage()).isNotNull()
                                                               .isEqualTo("de");
                              assertThat(dto.getTranslationLanguage()).isNotNull()
                                                                      .isEqualTo("pl");
                          });
    }

    @Test
    @WithAnonymousUser
    void oneAsAnonymousUserPrivateSetFailTest() throws Exception {
        mockMvc.perform(get("/sets/3"))
               .andDo(print())
               .andExpect(status().isForbidden())
               .andExpect(content().string("You have no access to set with id=3"));
    }
    // endregion

    // region LearningSetController::create
    @Test
    @WithAnonymousUser
    void createAsAnonymousUserTest() throws Exception {
        mockMvc.perform(post("/sets"))
               .andDo(print())
               .andExpect(status().isUnauthorized())
               .andExpect(content().string(""));
    }

    @Test
    @WithUserDetails("user1")
    void createAsUserTest() throws Exception {
        String createLearningSetDtoJSON = String.format(createLearningSetDtoJsonFormat, "new set", true, "es", "pl");

        MvcResult mvcResult = mockMvc.perform(post("/sets")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(createLearningSetDtoJSON))
                                     .andDo(print())
                                     .andExpect(status().isCreated())
                                     .andReturn();

        LearningSetDetailsDto created;

        created = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LearningSetDetailsDto.class);

        assertThat(created).isNotNull()
                           .satisfies(dto -> {
                               assertThat(dto.getId()).isEqualTo(4L);
                               assertThat(dto.isPubliclyVisible()).isTrue();
                               assertThat(dto.getTitle()).isNotNull().isEqualTo("new set");
                               assertThat(dto.getTermLanguage()).isNotNull().isEqualTo("es");
                               assertThat(dto.getTranslationLanguage()).isNotNull().isEqualTo("pl");
                           });
    }

    @Test
    @WithUserDetails("user1")
    void createAsUserBadRequestTest() throws Exception {
        String createLearningSetDtoJSON = String.format(createLearningSetDtoJsonFormat, "", true, "e$", "pol");

        MvcResult mvcResult = mockMvc.perform(post("/sets")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(createLearningSetDtoJSON))
                                     .andDo(print())
                                     .andExpect(status().isBadRequest())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertThat(content).isNotNull()
                           .isNotBlank()
                           .contains("Title cannot be empty")
                           .contains("Term language must be a 2 letter language code")
                           .contains("Translation language must be a 2 letter language code");
    }
    // endregion

    // region LearningSetController::replace
    @Test
    @WithAnonymousUser
    void replaceAsAnonymousUserTest() throws Exception {
        String createLearningSetDtoJSON = String.format(createLearningSetDtoJsonFormat, "new set", true, "es", "pl");
        mockMvc.perform(put("/sets")
                            .content(createLearningSetDtoJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isUnauthorized())
               .andExpect(content().string(""));
    }

    @Test
    @WithUserDetails("user1")
    void replaceAsUserWithOwnerPermissionTest() throws Exception {
        String createLearningSetDtoJSON = String.format(createLearningSetDtoJsonFormat, "updated", true, "es", "pl");
        MvcResult mvcResult = mockMvc.perform(put("/sets/1")
                                                  .content(createLearningSetDtoJSON)
                                                  .contentType(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();
        LearningSetDetailsDto updated = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), LearningSetDetailsDto.class
        );

        assertThat(updated).isNotNull()
                           .satisfies(dto -> {
                               assertThat(dto.getId()).isEqualTo(1L);
                               assertThat(dto.getTitle()).isNotNull().isEqualTo("updated");
                               assertThat(dto.getTermLanguage()).isEqualTo("es");
                               assertThat(dto.getTranslationLanguage()).isEqualTo("pl");
                           });
    }

    @Test
    @WithUserDetails("user3")
    void replaceAsUserWithoutPermissionsTest() throws Exception {
        String createLearningSetDtoJSON = String.format(createLearningSetDtoJsonFormat, "updated", true, "es", "pl");
        mockMvc.perform(put("/sets/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createLearningSetDtoJSON))
               .andDo(print())
               .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("user2")
    void replaceAsUserWithEditorPermissionTest() throws Exception {
        String createLearningSetDtoJSON = String.format(createLearningSetDtoJsonFormat, "updated", true, "es", "pl");
        MvcResult mvcResult = mockMvc.perform(put("/sets/1")
                                                  .content(createLearningSetDtoJSON)
                                                  .contentType(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();
        LearningSetDetailsDto updated = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), LearningSetDetailsDto.class);

        assertThat(updated).isNotNull()
                           .satisfies(dto -> {
                               assertThat(dto.getId()).isEqualTo(1L);
                               assertThat(dto.getTitle()).isNotNull().isEqualTo("updated");
                               assertThat(dto.getTermLanguage()).isEqualTo("es");
                               assertThat(dto.getTranslationLanguage()).isEqualTo("pl");
                           });
    }

    @Test
    @WithUserDetails("user1")
    void replaceAsUserBadRequestTest() throws Exception {
        String createLearningSetDtoJSON = String.format(createLearningSetDtoJsonFormat, "updated", true, "ese", "pl");
        mockMvc.perform(put("/sets/1")
                            .content(createLearningSetDtoJSON)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }
    // endregion

    // region LearningSetController::delete
    @Test
    @WithUserDetails("user1")
    void deleteAsUserWithOwnerPermissionTest() throws Exception {
        mockMvc.perform(delete("/sets/1"))
               .andDo(print())
               .andExpect(status().isNoContent());

        mockMvc.perform(get("/sets/1"))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void deleteAsAnonymousUserTest() throws Exception {
        mockMvc.perform(delete("/sets/1"))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user2")
    void deleteAsUserWithoutPermissionsTest() throws Exception {
        mockMvc.perform(delete("/sets/1"))
               .andDo(print())
               .andExpect(status().isForbidden());
    }
    // endregion
}
