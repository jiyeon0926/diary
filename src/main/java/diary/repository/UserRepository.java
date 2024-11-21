package diary.repository;

import diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 password 찾기 (없으면 null)
    Optional<User> findByEmail(String email);

    // 유저 탈퇴
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isValid = false WHERE u.id = :id AND u.isValid = true")
    int updateIsValidById(@Param("id") Long id);

    // 이메일로 중복 확인 (이 메소드는 JPA가 자동으로 구현)
    boolean existsByEmail(String email);

    // 이메일로 유저 찾기
    default User findByEmailElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    // 아이디로 유저 찾기
    default User findByIdElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다"));
    }

}
