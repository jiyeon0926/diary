package diary.repository;

import diary.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 댓글 유무 판별
    default Comment findCommentByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    // 수정일 내림차순 조회
    List<Comment> findAllByBoardIdOrderByModifiedAtDesc(Long boardId);

    // boardId, userId, commentId를 통해 해당 댓글 찾기
    @Query("SELECT c FROM Comment c INNER JOIN Board b ON c.board.id = :boardId WHERE (c.user.id = :userId OR b.user.id = :writerId) AND c.id = :commentId")
    Comment findCommentByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") Long userId, @Param("writerId") Long writerId, @Param("commentId") Long commentId);
}
