package diary.controller.dto;

import diary.entity.Follow;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FollowerResponseDto {

    private Long followerId;
    private LocalDateTime createdAt;

    public FollowerResponseDto(Follow follow) {
        this.followerId = follow.getFollower().getId();
        this.createdAt = follow.getCreatedAt();
    }
}
