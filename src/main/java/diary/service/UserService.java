package diary.service;

import diary.config.PasswordEncoder;
import diary.entity.User;
import diary.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    // 회원가입 메서드
    public void signup(String email, String username, String password) {

        // 이메일 형식 조건
        String EMAIL_REGEX = "/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$/i";

        // 비밀번호 조건. (대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함, 최소 8글자 이상 20글자까지)
        String PASSWORD_REGEX = "/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/";


        // 비밀번호 암호화
        String encodingPassword = passwordEncoder.encode(password);

        // db에 저장
        User user = new User(email, username, encodingPassword);
        userRepository.save(user);
    }
}
