package diary.controller;

import diary.entity.User;
import diary.service.GoodCommentService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/comments/{commentId}/good")
@AllArgsConstructor
public class GoodCommentController {
    GoodCommentService goodCommentService;

    // 댓글 좋아요
    @PostMapping
    public ResponseEntity<Void> goodComment(@PathVariable Long commentId, HttpSession session) {

        // 로그인한 유저 받아오기
        User loginUser = (User) session.getAttribute("loginUser");

        // 로그인유저 id 찾기
        Long userId = loginUser.getId();

        goodCommentService.doGoodComment(userId, commentId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 댓글에 좋아요 취소
    @DeleteMapping
    public ResponseEntity<Void> unGoodComment(@PathVariable Long commentId, HttpSession session) {

        // 로그인한 유저 받아오기
        User loginUser = (User) session.getAttribute("loginUser");

        // 로그인유저 id 찾기
        Long userId = loginUser.getId();

        goodCommentService.undoGoodComment(userId, commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
