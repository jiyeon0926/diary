package diary.service;

import diary.config.PasswordEncoder;
import diary.entity.User;
import diary.repository.UserRepository;
import diary.responseDto.LoginResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public LoginResponseDto login(String email, String password) {

        // 입력받은 이메일로 비밀번호 찾기
        Optional<User> findPassword = userRepository.findPasswordByEmail(email);

        // 만약 비밀번호가 없다면 400 상태코드 throw
        if (findPassword.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록된 회원이 아닙니다.");
        }

        // 비밀번호 일치 여부
        boolean isPasswordMatches = passwordEncoder.matches(password, findPassword.get().getPassword());

        // 비밀번호가 일치 하지 않으면 401 상태코드 throw
        if (!isPasswordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 다릅니다.");
        }

        // db에서 이메일과 비밀번호로 유저 찾기
        User findUser = userRepository.findIdByEmailElseThrow(email);


        // 찾은 유저의 id값을 반환
        return new LoginResponseDto(findUser.getId());
    }
}
