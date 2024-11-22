package diary.repository;

import diary.entity.FollowId;
import diary.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // 팔로우 관계 확인
    default Follow findByFollowerAndFollowee(Long followerId, Long followeeId) {
        return findByFollowerIdAndFolloweeId(followerId, followeeId).orElse(null);
    }

    // 팔로워 전체 조회
    List<Follow> findByFolloweeIdOrderByCreatedAtDesc(Long userId);

    // 팔로우 전체 조회
    List<Follow> findByFollowerIdOrderByCreatedAtDesc(Long userId);
}
