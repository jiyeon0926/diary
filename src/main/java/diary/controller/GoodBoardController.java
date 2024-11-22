package diary.controller;

import diary.entity.User;
import diary.service.GoodBoardService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/good")
@AllArgsConstructor
public class GoodBoardController {
    GoodBoardService goodBoardService;

    // 게시물에 좋아요 누르기
    @PostMapping()
    public ResponseEntity<Void> goodBoard(@PathVariable Long boardId, HttpSession session) {

        // 로그인한 유저 받아오기
        User loginUser = (User) session.getAttribute("loginUser");

        // 로그인유저 id 찾기
        Long userId = loginUser.getId();

        goodBoardService.doGood(userId, boardId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 게시물에 좋아요 누른거 취소
    @DeleteMapping()
    public ResponseEntity<Void> unGoodBoard(@PathVariable Long boardId, HttpSession session) {

        // 로그인한 유저 받아오기
        User loginUser = (User) session.getAttribute("loginUser");

        // 유저 id 찾기
        Long userId = loginUser.getId();

        goodBoardService.undoGood(userId, boardId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
