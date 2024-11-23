package diary.controller;

import diary.entity.User;
import diary.requestDto.LoginRequestDto;
import diary.requestDto.SignupRequestDto;
import diary.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    UserService userService;

    // 회원가입 api
    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody SignupRequestDto requestDto) {

        // 서비스 레이어의 signup 메서드. email, username, password 를 전달
        userService.signup(
                requestDto.getEmail(),
                requestDto.getUsername(),
                requestDto.getPassword()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/checkuser")
    public ResponseEntity<Void> checkUser(@RequestBody LoginRequestDto requestDto, HttpSession session) {

        // 세션에서 유저 정보 가져오기
        User loginUser = (User) session.getAttribute("loginUser");

        // 유저정보에서 이메일 가져오기
        String sessionEmail = loginUser.getEmail();
        // 이메일, 비밀번호 받은거
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 만약 로그인 된 유저의 이메일과 입력받은 이메일이 다르면 400 상태코드 throw
        if (!sessionEmail.equals(email)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 확인
        userService.checkPassword(email, password);

        // 비밀번호가 일치하면 세션에 allowResign을 true로 저장
        session.setAttribute("allowResign", true);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> resign(HttpServletRequest request) {

        HttpSession session = request.getSession();

        // 세션에서 allowResign을 받아옴
        Boolean allowResign = (Boolean) session.getAttribute("allowResign");

        // 만약 allowResign이 없거나 false면 401 상태코드 throw
        if (allowResign == null || !allowResign) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 로그인 되어있지 않으면 400 상태코드 throw
        if (session.getAttribute("loginUser") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 세션에서 유저 id 찾기
        User loginUser = (User) session.getAttribute("loginUser");
        Long userId = loginUser.getId();

        // 유저 삭제
        userService.deleteUser(userId);

        // 세션 삭제
        session.invalidate();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
