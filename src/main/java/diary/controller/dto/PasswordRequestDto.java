package diary.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class PasswordRequestDto {

    private final String password;

    @JsonCreator
    public PasswordRequestDto(String password) {
        this.password = password;
    }
}
