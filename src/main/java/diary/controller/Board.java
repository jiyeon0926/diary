package diary.controller;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.catalina.User;

@Getter
@Entity
@Table(name="board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "longtext")
    private String content;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;



}
