package diary.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }

    // 비밀번호 검증
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        // BCrypt의 verifyer를 사용하여 비밀번호를 검증
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword).verified;
    }
}