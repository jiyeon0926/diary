package diary.responseDto;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String email;

    public UserResponseDto(String email) {
        this.email = email;
    }
}
