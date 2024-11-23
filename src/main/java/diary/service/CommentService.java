package diary.service;

import diary.controller.dto.CommentUpdateResponseDto;
import diary.entity.Board;
import diary.entity.Comment;
import diary.entity.User;
import diary.repository.BoardRepository;
import diary.repository.CommentRepository;
import diary.repository.ProfileRepository;
import diary.controller.dto.CommentFindAllResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    public CommentFindAllResponseDto saveComment(Long boardId, Long userId, String content) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        User user = profileRepository.findUserByidOrElseThrow(userId);

        Comment comment = new Comment(content);
        comment.setBoard(board);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        return new CommentFindAllResponseDto(savedComment.getId(), savedComment.getContent(), savedComment.getCreatedAt(), savedComment.getModifiedAt());
    }

    // 댓글 조회
    public List<CommentFindAllResponseDto> findAllByBoardId(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    // 댓글 수정
    @Transactional
    public CommentUpdateResponseDto updateComment(Long boardId, Long commentId, Long userId, Long writerId, String content) {
        Comment comment = commentRepository.findCommentByBoardIdAndUserId(boardId, userId, writerId, commentId);

        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "작성한 댓글이 없습니다.");
        }

        Long commentUserId = comment.getUser().getId();
        Long boardUserId = comment.getBoard().getUser().getId();

        if (!commentUserId.equals(userId) && !boardUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글을 수정할 권한이 없습니다.");
        }

        comment.updateComment(content);

        return commentRepository.findCommentWithLike(comment.getId());
    }

    // 댓글 삭제
    public void deleteComment(Long boardId, Long commentId, Long userId, Long writerId) {
        Comment comment = commentRepository.findCommentByBoardIdAndUserId(boardId, userId, writerId, commentId);

        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "작성한 댓글이 없습니다.");
        }

        Long commentUserId = comment.getUser().getId();
        Long boardUserId = comment.getBoard().getUser().getId();

        if (!commentUserId.equals(userId) && !boardUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글을 수정할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // 작성자 조회
    public Long findUserIdByBoardId(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        return board.get().getUser().getId();
    }
}
