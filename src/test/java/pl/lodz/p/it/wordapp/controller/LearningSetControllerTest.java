package pl.lodz.p.it.wordapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.lodz.p.it.wordapp.controller.dto.LearningSetDetailsDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase
class LearningSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("user1")
    void allAsUserWithoutParamsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/sets"))
                                     .andDo(print())
                                     .andExpect(status().isOk())
                                     .andExpect(content().contentType("application/json"))
                                     .andReturn();

        List<LearningSetDetailsDto> responseBody = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() { });

        assertThat(responseBody).isNotNull()
                                .hasSize(3)
                                .allSatisfy(item -> assertThat(item).isNotNull());

        assertThat(responseBody.get(0)).isNotNull()
                                       .matches(LearningSetDetailsDto::isPubliclyVisible)
                                       .matches(dto -> Objects.equals(dto.getTermLanguage(), "de"))
                                       .matches(dto -> Objects.equals(dto.getTranslationLanguage(), "pl"));

        assertThat(responseBody.get(1)).isNotNull()
                                       .matches(dto -> !dto.isPubliclyVisible())
                                       .matches(dto -> Objects.equals(dto.getTermLanguage(), "de"))
                                       .matches(dto -> Objects.equals(dto.getTranslationLanguage(), "en"));

        assertThat(responseBody.get(2)).isNotNull()
                                       .matches(dto -> !dto.isPubliclyVisible())
                                       .matches(dto -> Objects.equals(dto.getTermLanguage(), "pl"))
                                       .matches(dto -> Objects.equals(dto.getTranslationLanguage(), "en"));
    }
}
