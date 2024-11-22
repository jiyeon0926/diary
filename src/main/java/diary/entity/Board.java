package diary.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;


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
    private String title;

    @Column(nullable = false)
    private String weather;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Board(){

    }

    public Board(String title, String content, String weather,User user) {
        this.title = title;
        this.content = content;
        this.weather = weather;
        this.user = user;
    }

    public void setUser(){
        this.user = new User();
    }


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodBoard> goodBoards; // GoodBoard와의 관계



    public void update(String title, String content, String weather,User user) {
        this.title = title;
        this.content = content;
        this.weather = weather;
        this.user = user;
    }
}
