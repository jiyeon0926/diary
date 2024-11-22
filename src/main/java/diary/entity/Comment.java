package diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @Setter
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<GoodComment> goodComment;

    public Comment(String content) {
        this.content = content;
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
