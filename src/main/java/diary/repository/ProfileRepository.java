package diary.repository;

import diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface ProfileRepository extends JpaRepository<User, Long> {
    default User findUserByidOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 회원이 아닙니다."));
    }
}
