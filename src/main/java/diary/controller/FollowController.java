package diary.controller;

import diary.entity.User;
import diary.controller.dto.FollowResponseDto;
import diary.controller.dto.FollowerResponseDto;
import diary.service.FollowService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/following")
    public ResponseEntity<Void> follow(@PathVariable Long userId, HttpSession session) {
        followService.follow((User) session.getAttribute("loginUser"), userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 팔로우 취소
    @DeleteMapping("/following")
    public ResponseEntity<Void> unfollow(@PathVariable Long userId, HttpSession session) {
        followService.unfollow((User) session.getAttribute("loginUser"), userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 팔로우 전체 조회
    @GetMapping("/following")
    public List<FollowResponseDto> findByFolloweeId(@PathVariable Long userId) {
        return followService.findAllByFollowList(userId);
    }

    // 팔로워 전체 조회
    @GetMapping("/follower")
    public List<FollowerResponseDto> findByFollowerId(@PathVariable Long userId) {
        return followService.findAllByFollowerList(userId);
    }
}
