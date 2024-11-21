package diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "goodboard")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class GoodBoard {

    @EmbeddedId
    private UserBoardId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    public GoodBoard(UserBoardId id, User user, Board board) {
        this.id = id;
        this.user = user;
        this.board = board;
    }


}
