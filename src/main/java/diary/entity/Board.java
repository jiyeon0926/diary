package diary.entity;

import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
@Table(name="board")
public class Board  extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @Column(nullable = false)
    private String weather;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    public Board(){

    }

}
