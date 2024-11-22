package diary.repository;

import diary.entity.GoodComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodCommentRepository extends JpaRepository<GoodComment, Long> {

    Optional<GoodComment> findByCommentIdAndUserId(Long commentId, Long userId);

    // 댓글 id랑 유저 id로 찾기
    default GoodComment findByCommentAndUser(Long commentId, Long userId) {
        return findByCommentIdAndUserId(commentId, userId).orElse(null);
    }

}
