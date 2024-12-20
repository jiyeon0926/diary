package diary.controller;

import diary.controller.dto.CommentUpdateResponseDto;
import diary.entity.User;
import diary.controller.dto.CommentRequestDto;
import diary.controller.dto.CommentFindAllResponseDto;
import diary.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentFindAllResponseDto> saveComment(@PathVariable Long boardId,
                                                                 @Valid @RequestBody CommentRequestDto requestDto,
                                                                 HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        CommentFindAllResponseDto responseDto = commentService.saveComment(boardId, user.getId(), requestDto.getContent());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 댓글 조회
    @GetMapping
    public List<CommentFindAllResponseDto> findAllByBoardId(@PathVariable Long boardId) {
        return commentService.findAllByBoardId(boardId);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment(@PathVariable Long boardId,
                                                                  @PathVariable Long commentId,
                                                                  @Valid @RequestBody CommentRequestDto requestDto,
                                                                  HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Long writerId = commentService.findUserIdByBoardId(boardId);

        CommentUpdateResponseDto responseDto = commentService.updateComment(boardId, commentId, user.getId(), writerId, requestDto.getContent());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        Long writerId = commentService.findUserIdByBoardId(boardId);

        commentService.deleteComment(boardId, commentId, user.getId(), writerId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
