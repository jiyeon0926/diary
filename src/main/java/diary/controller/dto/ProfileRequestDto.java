package diary.controller.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private final String password;
    private final String username;

    public ProfileRequestDto(String password, String username) {
        this.password = password;
        this.username = username;
    }
}
