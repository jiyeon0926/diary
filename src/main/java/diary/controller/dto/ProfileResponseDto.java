package diary.controller.dto;

import diary.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private Long id;
    private String username;
    private String email;


    public ProfileResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
