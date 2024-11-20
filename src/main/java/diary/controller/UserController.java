package diary.controller;

import diary.requestDto.SignupRequestDto;
import diary.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
