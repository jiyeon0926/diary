package diary.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name="user")
public class User  extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "is_valid")
    private Boolean isValid=true;

    public User(){

    }
}
