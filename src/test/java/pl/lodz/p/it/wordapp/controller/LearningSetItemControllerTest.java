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
import java.nio.charset.StandardCharsets;
import java.util.List;
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
import pl.lodz.p.it.wordapp.controller.dto.LearningSetItemDto;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext
class LearningSetItemControllerTest {

    private final String createLearningSetItemDtoFormat = "{" +
        "\"term\": \"%s\"," +
        "\"translation\": \"%s\"" +
        "}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // region LearningSetItemController::all
    @Test
    @WithAnonymousUser
    void allAsAnonymousUserPublicSetTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1/items"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetItemDto> responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(responseBody).isNotNull()
                                .hasSize(4)
                                .allSatisfy(dto -> assertThat(dto).isNotNull());
    }

    @Test
    @WithUserDetails("user3")
    void allAsUserWithPermissionsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1/items"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetItemDto> responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(responseBody).isNotNull()
                                .hasSize(4)
                                .allSatisfy(dto -> assertThat(dto).isNotNull());
    }

    @Test
    @WithUserDetails("user4")
    void allAsUserWithoutPermissionsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1/items"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetItemDto> responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(responseBody).isNotNull()
                                .hasSize(4)
                                .allSatisfy(dto -> assertThat(dto).isNotNull());
    }

    @Test
    @WithAnonymousUser
    void allAsAnonymousUserPrivateSetTest() throws Exception {
        mockMvc.perform(get("/sets/2/items"))
               .andDo(print())
               .andExpect(status().isForbidden())
               .andExpect(content().string("You have no access to this set"));
    }

    @Test
    @WithUserDetails("user1")
    void allAsUserPrivateSetWithPermissionsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/2/items"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        List<LearningSetItemDto> responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(responseBody).isNotNull()
                                .hasSize(3)
                                .allSatisfy(dto -> assertThat(dto).isNotNull());
    }

    @Test
    @WithUserDetails("user3")
    void allAsUserPrivateSetWithoutPermissionsTest() throws Exception {
        mockMvc.perform(get("/sets/2/items"))
               .andDo(print())
               .andExpect(status().isForbidden())
               .andExpect(content().string("You have no access to this set"));
    }

    @Test
    @WithAnonymousUser
    void allAsUserSetNotExistsTest() throws Exception {
        mockMvc.perform(get("/sets/20/items"))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(content().string("Learning set with id=20 does not exist"));
    }
    // endregion

    // region LearningSetItemController::one
    @Test
    @WithAnonymousUser
    void oneAsAnonymousUserPublicSetTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1/items/1"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        LearningSetItemDto responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() { }
        );

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getLearningSetItemKeySetID()).isEqualTo(1L);
        assertThat(responseBody.getLearningSetItemKeyItemID()).isEqualTo(1L);
        assertThat(responseBody.getTerm()).isNotNull().isEqualTo("hallo");
        assertThat(responseBody.getTranslation()).isNotNull().startsWith("cze").hasSize(5);
    }

    @Test
    @WithUserDetails("user2")
    void oneAsUserWithPermissionsPublicSetTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1/items/2"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        LearningSetItemDto responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getLearningSetItemKeySetID()).isEqualTo(1L);
        assertThat(responseBody.getLearningSetItemKeyItemID()).isEqualTo(2L);
        assertThat(responseBody.getTerm()).isNotNull().isEqualTo("r Hund");
        assertThat(responseBody.getTranslation()).isNotNull().contains("pies");
    }

    @Test
    @WithUserDetails("user4")
    void oneAsUserWithoutPermissionsPublicSetTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/1/items/3"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        LearningSetItemDto responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { }
        );

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getLearningSetItemKeySetID()).isEqualTo(1L);
        assertThat(responseBody.getLearningSetItemKeyItemID()).isEqualTo(3L);
        assertThat(responseBody.getTerm()).isNotNull().isEqualTo("ja");
        assertThat(responseBody.getTranslation()).isNotNull().contains("tak");
    }

    @Test
    @WithAnonymousUser
    void oneAsAnonymousUserPrivateSetTest() throws Exception {
        mockMvc.perform(get("/sets/2/items/1"))
               .andDo(print())
               .andExpect(status().isForbidden())
               .andExpect(content().string("You have no access to this set"));
    }

    @Test
    @WithUserDetails("user2")
    void oneAsUserWithPermissionsPrivateSetTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/sets/2/items/1"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                     .andReturn();

        LearningSetItemDto responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), LearningSetItemDto.class
        );

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getLearningSetItemKeySetID()).isEqualTo(2L);
        assertThat(responseBody.getLearningSetItemKeyItemID()).isEqualTo(1L);
        assertThat(responseBody.getTerm()).isNotNull().isEqualTo("e Katze");
        assertThat(responseBody.getTranslation()).isNotNull().isEqualTo("cat");
    }

    @Test
    @WithUserDetails("user3")
    void oneAsUserWithoutPermissionsPrivateTest() throws Exception {
        mockMvc.perform(get("/sets/2/items/1"))
               .andDo(print())
               .andExpect(status().isForbidden())
               .andExpect(content().string("You have no access to this set"));
    }
    // endregion

    // region LearningSetItemController::create
    @Test
    @WithAnonymousUser
    void createAsAnonymousUserTest() throws Exception {
        String createItemDto = String.format(createLearningSetItemDtoFormat, "new term", "translation");
        mockMvc.perform(post("/sets/1/items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemDto))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user1")
    void createAsUserWithPermissionsTest() throws Exception {
        String createItemDto = String.format(createLearningSetItemDtoFormat, "new term", "translation");
        MvcResult mvcResult = mockMvc.perform(post("/sets/1/items")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(createItemDto))
                                     .andDo(print())
                                     .andExpect(status().isCreated())
                                     .andReturn();
        LearningSetItemDto responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), LearningSetItemDto.class
        );

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getLearningSetItemKeyItemID()).isEqualTo(5L);
        assertThat(responseBody.getLearningSetItemKeySetID()).isEqualTo(1L);
        assertThat(responseBody.getTerm()).isNotNull().isEqualTo("new term");
        assertThat(responseBody.getTranslation()).isNotNull().isEqualTo("translation");
    }

    @Test
    @WithUserDetails("user3")
    void createAsUserWithoutPermissionsTest() throws Exception {
        String createItemDto = String.format(createLearningSetItemDtoFormat, "new term", "translation");
        mockMvc.perform(post("/sets/1/items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemDto))
               .andDo(print())
               .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("user2")
    void createBadRequestTest() throws Exception {
        String createItemDto = String.format(createLearningSetItemDtoFormat, "", "translation");
        mockMvc.perform(post("/sets/1/items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createItemDto))
               .andDo(print())
               .andExpect(status().isBadRequest());
    }
    // endregion

    // region LearningSetItemController::replace
    @Test
    @WithUserDetails("user2")
    void replaceAsUserWithPermissionsTest() throws Exception {
        String createItemDto = String.format(createLearningSetItemDtoFormat, "new term", "translation");
        MvcResult mvcResult = mockMvc.perform(put("/sets/1/items/1")
                                                  .content(createItemDto)
                                                  .contentType(MediaType.APPLICATION_JSON))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andReturn();

        LearningSetItemDto responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), LearningSetItemDto.class
        );

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getLearningSetItemKeySetID()).isEqualTo(1L);
        assertThat(responseBody.getLearningSetItemKeyItemID()).isEqualTo(1L);
        assertThat(responseBody.getTerm()).isNotNull().isEqualTo("new term");
        assertThat(responseBody.getTranslation()).isNotNull().isEqualTo("translation");
    }

    @Test
    @WithUserDetails("user3")
    void replaceAsUserWithoutPermissionsTest() throws Exception {
        String createItemDto = String.format(createLearningSetItemDtoFormat, "new term", "translation");
        mockMvc.perform(put("/sets/1/items/1")
                            .content(createItemDto)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void replaceAsAnonymousUserTest() throws Exception {
        String createItemDto = String.format(createLearningSetItemDtoFormat, "new term", "translation");
        mockMvc.perform(put("/sets/1/items/1")
                            .content(createItemDto)
                            .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }
    // endregion

    // region LearningSetItemController::remove
    @Test
    @WithAnonymousUser
    void removeAsAnonymousUserTest() throws Exception {
        mockMvc.perform(delete("/sets/1/items/1"))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user1")
    void removeAsUserWithPermissionsTest() throws Exception {
        mockMvc.perform(delete("/sets/1/items/1"))
               .andDo(print())
               .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("user4")
    void removeAsUserWithoutPermissionsTest() throws Exception {
        mockMvc.perform(delete("/sets/1/items/1"))
               .andDo(print())
               .andExpect(status().isForbidden());
    }
    // endregion
}
