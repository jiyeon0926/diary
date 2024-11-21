package diary.repository;

import diary.entity.Board;
import diary.responseDto.FollowResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
//    팔로우 중인 사용자의 게시글 조회
//    @Query("SELECT b FROM Board b INNER JOIN Follow f ON b.user.id = f.followee.id WHERE f.follower.id = :myId order by b.modifiedAt desc")
//    List<Board> findAllByFollowerId(@Param("myId") Long myId);

    @Query("SELECT b FROM Board b WHERE b.user.id IN (SELECT f.followee.id FROM Follow f WHERE f.follower.id = :userId)")
    Page<Board> findAllByFollowingUsers(@Param("userId") Long userId, Pageable pageable);

    Page<Board> findAllByUserIdIn(List<FollowResponseDto> userIds, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.createdAt BETWEEN :startDate AND :endDate")
    List<Board> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT b FROM Board b INNER JOIN Follow f ON b.user.id= f.followee.id WHERE f.follower.id = :id AND b.createdAt BETWEEN :startDate AND :endDate")
    List<Board> findByFollowingUsersAndPeriod(Long id, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
