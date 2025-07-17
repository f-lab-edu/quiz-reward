package quiz.reward.quiz.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class QuizDao {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> findReadyQuizzes(LocalDateTime now) {
        String sql = "SELECT * FROM quiz WHERE status = 'READY' AND open_time <= ?";
        return jdbcTemplate.queryForList(sql, now);
    }

    public void updateStatusToOpen(Long quizId) {
        String sql = "UPDATE quiz SET status = 'OPEN' WHERE id = ?";
        jdbcTemplate.update(sql, quizId);
    }
}