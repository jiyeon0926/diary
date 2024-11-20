package diary.repository;

import diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 중복 확인 (이 메소드는 JPA가 자동으로 구현)
    boolean existsByEmail(String email);
}
