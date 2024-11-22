package diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "goodcomment")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class GoodComment {

    @EmbeddedId
    private UserCommentId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;

    public GoodComment(UserCommentId id, User user, Comment comment) {
        this.id = id;
        this.user = user;
        this.comment = comment;
    }


}
