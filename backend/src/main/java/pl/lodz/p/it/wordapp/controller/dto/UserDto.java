package pl.lodz.p.it.wordapp.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({ "id", "username" })
public class UserDto {
    private Long id;
    private String username;
}
