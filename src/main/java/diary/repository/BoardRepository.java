package diary.repository;

import diary.entity.Board;
import diary.responseDto.BoardResponseDto;
import diary.responseDto.FollowResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    default Board findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 아이디 값입니다."+id));
    }

    @Query("SELECT new diary.responseDto.BoardResponseDto(b.id, b.title, b.content, b.weather,b.createdAt,b.modifiedAt, COUNT(gb.board.id))  FROM Board b LEFT JOIN GoodBoard  gb ON b.id = gb.board.id GROUP BY b.id ORDER BY COUNT(gb.board.id) DESC")
    Page<BoardResponseDto> findAllOrderByGoodConut(Pageable pageable);

    @Query("SELECT new diary.responseDto.BoardResponseDto(b.id, b.title, b.content, b.weather,b.createdAt,b.modifiedAt, COUNT(gb.board.id)) FROM Board b LEFT JOIN GoodBoard  gb ON b.id = gb.board.id WHERE b.createdAt BETWEEN :startDate AND :endDate GROUP BY b.id ORDER BY COUNT(gb.id) DESC")
    Page<BoardResponseDto> findPostsByPeriodAndGood(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,Pageable pageable);


}
