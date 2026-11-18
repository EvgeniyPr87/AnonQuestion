package anon_questions_back.repository;

import anon_questions_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByVkUserId(Long vkUserId);

    Optional<User> findByLinkSlug(String linkSlug);


}
