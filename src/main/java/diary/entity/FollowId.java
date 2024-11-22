package diary.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
public class FollowId implements Serializable {

    private Long followerId;
    private Long followeeId;

    public FollowId(Long followerId, Long followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }
}
