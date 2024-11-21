package diary.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class UserBoardId {

    private Long userId;
    private Long boardId;

    public UserBoardId(Long userId, Long boardId) {
        this.userId = userId;
        this.boardId = boardId;
    }
}
