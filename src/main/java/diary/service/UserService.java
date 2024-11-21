package diary.service;

import diary.config.PasswordEncoder;
import diary.entity.User;
import diary.repository.UserRepository;
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

        // 중복된 사용자 아이디인지 / 이미 탈퇴한 회원인지 체크
        boolean isExistsEmail = userRepository.existsByEmail(email);

        // 중복된 사용자 아이디로 가입하는 경우 400 상태코드 throw
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
    public User findById(Long id) {

        // 유저 id로 유저 찾기
        Optional<User> optionalUser = userRepository.findById(id);

        // 유저가 비어있으면 404 상태코드 throw
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 아닙니다.");
        }

        // 유저가 있으면 유저를 반환
        return optionalUser.get();
    }

    // 비밀번호 일치 여부
    public void checkPassword(String email, String password) {

        // 이메일로 유저 찾기
        User findUser = userRepository.findByEmailElseThrow(email);

        // 비밀번호 일치 여부
        boolean isPasswordMatches = passwordEncoder.matches(password, findUser.getPassword());

        // 비밀번호가 일치 하지 않으면 401 상태코드 throw
        if (!isPasswordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 다릅니다.");
        }
    }

    public void deleteUser(Long id) {

        // id로 유저 찾기
        User findUser = userRepository.findByIdElseThrow(id);

        // 유저 삭제
        int updateRow = userRepository.updateIsValidById(findUser.getId());

        if (updateRow == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원이거나 회원이 없습니다.");
        }
    }
}
