package quiz.reward.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import quiz.reward.quiz.dao.QuizDao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QuizService {

    private final QuizDao quizDao;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void openScheduledQuizzes() {
        List<Map<String, Object>> quizzes = quizDao.findReadyQuizzes(LocalDateTime.now());

        for (Map<String, Object> quiz : quizzes) {
            Long id = ((Number) quiz.get("id")).longValue();
            quizDao.updateStatusToOpen(id);

            try {
                String key = "quiz:open:" + id;
                String value = objectMapper.writeValueAsString(quiz);
                redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(10));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("퀴즈 Redis 캐싱 실패", e);
            }
        }
    }
}
