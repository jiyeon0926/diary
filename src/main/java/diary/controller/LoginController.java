package diary.controller;

import diary.entity.User;
import diary.requestDto.LoginRequestDto;
import diary.responseDto.LoginResponseDto;
import diary.service.LoginService;
import diary.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {
        
        // 이메일과 비밀번호를 받아서 db에 저장된 user 컬럼(객체)을 가져온다
        LoginResponseDto loginResponseDto = loginService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // 가져온 객체에서 id를 가져온다
        Long userId = loginResponseDto.getId();

        // 만약 id가 null이면 회원가입하지 않음
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 회원정보를 가져옴
        User loginUser = userService.findById(userId);
        log.info(loginUser.toString());

        // session에 loginUser 저장
        session.setAttribute("loginUser", loginUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
