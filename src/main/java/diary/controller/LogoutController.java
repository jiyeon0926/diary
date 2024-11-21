package diary.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/logout")
@AllArgsConstructor
public class LogoutController {

    @PostMapping
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        // 로그인하지 않으면 HttpSession이 null로 반환된다.
        HttpSession session = request.getSession(false);

        // 세션이 존재하면 -> 로그인이 된 경우
        if(session != null && session.getAttribute("loginUser") != null) {
            session.invalidate(); // 해당 세션(데이터)을 삭제한다.
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
