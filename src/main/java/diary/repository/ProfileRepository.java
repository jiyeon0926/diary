package diary.repository;

import diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface ProfileRepository extends JpaRepository<User, Long> {
    default User findUserByidOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Does not exist id = " + id));
    }
}
