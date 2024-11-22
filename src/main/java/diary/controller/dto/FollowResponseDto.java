package diary.controller.dto;

import diary.entity.Follow;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FollowResponseDto {

    private Long followeeId;
    private LocalDateTime createdAt;

    public FollowResponseDto(Follow follow) {
        this.followeeId = follow.getFollowee().getId();
        this.createdAt = follow.getCreatedAt();
    }
}
