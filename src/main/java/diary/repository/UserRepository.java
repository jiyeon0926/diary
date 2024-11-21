package diary.repository;

import diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 user 찾기 (없으면 null)
    Optional<User> findIdByEmail(String email);

    // 이메일로 password 찾기 (없으면 null)
    Optional<User> findPasswordByEmail(String email);

    // 이메일로 중복 확인 (이 메소드는 JPA가 자동으로 구현)
    boolean existsByEmail(String email);

    // 이메일로 user 찾기
    default User findIdByEmailElseThrow(String email) {
        return findIdByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

}
