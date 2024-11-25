package diary.repository;

import diary.entity.Board;
import diary.controller.dto.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {



    //기간별 게시글 조회
    @Query("SELECT new diary.controller.dto.BoardResponseDto(b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt, COUNT(gb.board.id)) FROM Board b " +
            "LEFT JOIN GoodBoard gb ON b.id = gb.board.id " +
            "WHERE b.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt "
    )
    Page<BoardResponseDto> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);


    //팔로우한 사람들의 게시글 기간별 조회
    @Query("SELECT new diary.controller.dto.BoardResponseDto(b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt, COUNT(gb.board.id)) FROM Board b " +
           "LEFT JOIN GoodBoard gb ON b.id = gb.board.id " +
            "INNER JOIN Follow f ON b.user.id = f.followee.id " +
            "WHERE f.follower.id = :id AND b.createdAt " +
            "BETWEEN :startDate AND :endDate " +
            "GROUP BY b.id ")
    Page<BoardResponseDto> findByFollowingUsersAndPeriod(@Param("id")Long id, @Param("startDate")LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    //단건조회
    default Board findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 아이디 값입니다."+id));
    }

    @Query("SELECT new diary.controller.dto.BoardResponseDto(" +
            "b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt, COUNT(gb.board.id)) " +
            "FROM Board b LEFT JOIN GoodBoard gb ON b.id = gb.board.id " +
            "WHERE b.id = :id " +
            "GROUP BY b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt")
    Optional<BoardResponseDto> findByIdWithGoods(@Param("id") Long id);

    default BoardResponseDto  findByIdWhitGoodCountOrElseThrow(Long id){
        return findByIdWithGoods(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "찾을 수 없는 아이디 값입니다."+id));
    }

    //전체 게시물 좋아요 많은순 조회
    @Query("SELECT new diary.controller.dto.BoardResponseDto" +
            "(b.id, b.title, b.content, b.weather,b.createdAt,b.modifiedAt, COUNT(gb.board.id))  " +
            "FROM Board b LEFT JOIN GoodBoard  gb " +
            "ON b.id = gb.board.id GROUP BY b.id " +
            "ORDER BY COUNT(gb.board.id) DESC")
    Page<BoardResponseDto> findAllOrderByGoodConut(Pageable pageable);

    //기간별 게시물 좋아요 많은순 조회
    @Query("SELECT new diary.controller.dto.BoardResponseDto(b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt, COUNT(gb.board.id)) FROM Board b " +
            "LEFT JOIN GoodBoard gb ON b.id = gb.board.id " +
            "WHERE b.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt ")
    Page<BoardResponseDto> findPostsByPeriodAndGood(@Param("startDate")LocalDateTime startDateTime, @Param("endDate")LocalDateTime endDateTime, Pageable pageable);

    //팔로우 하고있는 유저의 게시글 전체 조회
    @Query("SELECT new diary.controller.dto.BoardResponseDto(b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt, COUNT(gb.board.id)) FROM Board b " +
            "LEFT JOIN GoodBoard gb ON b.id = gb.board.id " +
            "WHERE b.user.id IN (SELECT f.followee.id FROM Follow f WHERE f.follower.id = :id) " +
            "GROUP BY b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt " +
            "ORDER BY b.modifiedAt DESC")
    Page<BoardResponseDto> findAllByFollowingUsers(@Param("id") Long id, Pageable pageable);

    //팔로우한 유저들의 전체 게시글 좋아요 많은순 조회
    @Query("SELECT new diary.controller.dto.BoardResponseDto(b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt, COUNT(gb.board.id)) FROM Board b " +
            "LEFT JOIN GoodBoard gb ON b.id = gb.board.id " +
            "WHERE b.user.id IN (SELECT f.followee.id FROM Follow f WHERE f.follower.id = :id) " +
            "GROUP BY b.id, b.title, b.content, b.weather, b.createdAt, b.modifiedAt " +
            "ORDER BY COUNT(gb.id) DESC")
    Page<BoardResponseDto> findAllByFollowingUsersGood(@Param("id")Long id,Pageable pageable);

    //팔로우한 유저들의 기간별 게시글 좋아요 많은순 조회
    @Query("SELECT new diary.controller.dto.BoardResponseDto(b.id, b.title, b.content, b.weather,b.createdAt,b.modifiedAt, COUNT(gb.board.id)) " +
            "FROM Board b INNER JOIN Follow f ON b.user.id = f.followee.id " +
            "LEFT JOIN GoodBoard gb ON b.id = gb.board.id " +
            "WHERE f.follower.id = :id " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY b.id " +
            "ORDER BY COUNT(gb.id) DESC")
    Page<BoardResponseDto> findByFollowingUsersAndPeriodAndGood(@Param("id")Long id ,@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,Pageable pageable);


}
