package diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="follow")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Follow {

    @EmbeddedId
    private ClassId id;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User follower;


    @ManyToOne
    @MapsId("followeeId")
    @JoinColumn(name = "followee_id", referencedColumnName = "id")
    private User followee;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Follow(ClassId id, User follower, User followee) {
        this.id = id;
        this.follower = follower;
        this.followee = followee;
    }
}
