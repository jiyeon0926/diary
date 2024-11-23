package diary.repository;

import diary.controller.dto.CommentFindAllResponseDto;
import diary.controller.dto.CommentUpdateResponseDto;
import diary.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 댓글 전체 조회
    @Query("SELECT new diary.controller.dto.CommentFindAllResponseDto(c.id, c.content, c.createdAt, c.modifiedAt, COUNT(gc.comment.id)) FROM Comment c LEFT JOIN GoodComment gc ON c.id = gc.comment.id GROUP BY c.id, c.content, c.createdAt, c.modifiedAt ORDER BY COUNT(gc.comment.id) DESC, c.modifiedAt DESC")
    List<CommentFindAllResponseDto> findAllByBoardId(Long boardId);

    // 좋아요 포함 특정 댓글 조회
    @Query("SELECT new diary.controller.dto.CommentUpdateResponseDto(c.id, c.content, c.modifiedAt, COUNT(gc.comment.id)) FROM Comment c LEFT JOIN GoodComment gc ON c.id = gc.comment.id WHERE c.id = :commentId")
    CommentUpdateResponseDto findCommentWithLike(@Param("commentId") Long commentId);

    // 특정 댓글 가져오기
    @Query("SELECT c FROM Comment c INNER JOIN Board b ON c.board.id = :boardId WHERE (c.user.id = :userId OR b.user.id = :writerId) AND c.id = :commentId")
    Comment findCommentByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") Long userId, @Param("writerId") Long writerId, @Param("commentId") Long commentId);
}
