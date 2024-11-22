package diary.repository;

import diary.entity.GoodBoard;
import diary.entity.UserBoardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodBoardRepository extends JpaRepository<GoodBoard, UserBoardId> {

    Optional<GoodBoard> findByBoardIdAndUserId(Long boardId, Long userId);

    // 보드 id랑 유저 id로 찾기
    default GoodBoard findByBoardAndUser(Long boardId, Long userId) {
        return findByBoardIdAndUserId(boardId, userId).orElse(null);
    }
}
