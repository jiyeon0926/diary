package diary.controller.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private final String email;

    private final String username;

    private final String password;

    public SignupRequestDto(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
