package diary.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class UserCommentId {

    private Long userId;
    private Long commentId;

    public UserCommentId(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
