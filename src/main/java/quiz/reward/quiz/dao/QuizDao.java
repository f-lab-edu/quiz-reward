package quiz.reward.quiz.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import quiz.reward.quiz.model.Quiz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class QuizDao {

    private static final String QUIZ = "quiz";

    private final JdbcTemplate jdbcTemplate;

    public List<Quiz> findReadyQuizzes(LocalDateTime now) {
        String sql = String.format("SELECT * FROM %s WHERE status = 'READY' AND open_time <= ?", QUIZ);

        return jdbcTemplate.query(sql, new Object[]{now}, (rs, rowNum) ->
                Quiz.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .question(rs.getString("question"))
                        .correctAnswer(rs.getString("correct_answer"))
                        .status(rs.getString("status"))
                        .openTime(rs.getObject("open_time", LocalDateTime.class))
                        .rewardPlanJson(rs.getString("reward_plan_json"))
                        .createdAt(rs.getObject("created_at", LocalDateTime.class))
                        .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                        .build()
        );
    }

    public void updateStatusToOpen(Long quizId) {
        String sql = String.format("UPDATE %s SET status = 'OPEN' WHERE id = ?", QUIZ);
        jdbcTemplate.update(sql, quizId);
    }
}