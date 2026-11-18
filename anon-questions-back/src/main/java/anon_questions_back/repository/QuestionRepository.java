package anon_questions_back.repository;

import anon_questions_back.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}
