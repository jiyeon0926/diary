package diary.service;

import diary.entity.Comment;
import diary.entity.GoodComment;
import diary.entity.User;
import diary.entity.UserCommentId;
import diary.repository.CommentRepository;
import diary.repository.GoodCommentRepository;
import diary.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class GoodCommentService {
    GoodCommentRepository goodCommentRepository;
    CommentRepository commentRepository;
    UserRepository userRepository;

    // 댓글에 좋아요 누르기
    public void doGoodComment(Long userId, Long commentId) {

        // 유저 찾기, 댓글 찾기
        User findUser = userRepository.findByIdElseThrow(userId);
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Long commentUserId = findComment.getUser().getId();

        // 만약 본인이 쓴 댓글이면 400 상태코드 throw
        if (commentUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인이 작성한 댓글입니다");
        }

        // 좋아요댓글 테이블에서 댓글 id와 유저 id로 찾기
        GoodComment findGoodComment = goodCommentRepository.findByCommentAndUser(commentId, userId);

        // 만약 좋아요를 안하면 좋아요하기, 좋아요 했으면 400 상태코드
        if (findGoodComment == null) {
            UserCommentId userCommentId = new UserCommentId(userId, commentId);
            GoodComment goodComment = new GoodComment(userCommentId, findUser, findComment);

            goodCommentRepository.save(goodComment);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요한 댓글입니다");
        }
    }

    // 댓글에 좋아요 취소
    public void undoGoodComment(Long userId, Long commentId) {

        // 댓글 찾기
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 댓글 쓴 유저 id
        Long commentUserId = findComment.getUser().getId();

        // 만약 본인이 쓴 댓글이면 400 상태코드 throw
        if (commentUserId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인이 작성한 댓글입니다");
        }

        // 좋아요댓글 테이블에서 댓글 id와 유저 id로 찾기
        GoodComment findGoodComment = goodCommentRepository.findByCommentAndUser(commentId, userId);

        // 만약 좋아요를 했으면 좋아요취소하기, 좋아요 안했으면 400 상태코드
        if (findGoodComment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요가 이미 취소되어 있습니다");

        } else {
            goodCommentRepository.delete(findGoodComment);
        }

    }
}
