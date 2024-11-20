package diary.service;

import diary.config.PasswordEncoder;
import diary.entity.User;
import diary.repository.UserRepository;
import diary.responseDto.UserResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    // 회원가입 메서드
    public void signup(String email, String username, String password) {

        // 이메일 형식 조건
        String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // 비밀번호 조건. (대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함, 최소 8글자 이상 20글자까지)
        String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$";

        // 이메일패턴과 비밀번호패턴 객체 생성
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
        Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

        // 이메일과 비밀번호가 유효한지 체크
        boolean isEmailValid = emailPattern.matcher(email).matches();
        boolean isPasswordValid = passwordPattern.matcher(password).matches();

        // 만약 이메일과 비밀번호가 유효하지 않다면 400 상태코드 throw
        if (!isEmailValid || !isPasswordValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함, 최소 8글자 이상 20글자까지");
        }

        // 중복된 사용자 아이디인지 체크 400 상태코드 throw
        boolean isExistsEmail = userRepository.existsByEmail(email);

        // 중복된 사용자 아이디로 가입하는 경우
        if (isExistsEmail) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodingPassword = passwordEncoder.encode(password);

        // db에 저장
        User user = new User(email, username, encodingPassword);
        userRepository.save(user);
    }


    // 유저가 존재하는지 확인
    public UserResponseDto findById(Long id) {
        // ㅇ
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 아닙니다.");
        }

        User findUser = optionalUser.get();

        return new UserResponseDto(findUser.getEmail());
    }
}
