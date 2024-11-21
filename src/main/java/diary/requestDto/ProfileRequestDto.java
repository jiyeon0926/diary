package diary.requestDto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private final Long id;
    private final String password;
    private final String username;

    public ProfileRequestDto(Long id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }
}
