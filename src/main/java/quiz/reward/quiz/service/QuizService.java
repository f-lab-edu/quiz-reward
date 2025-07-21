package quiz.reward.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import quiz.reward.quiz.dao.QuizDao;
import quiz.reward.quiz.model.Quiz;

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
        List<Quiz> quizzes = quizDao.findReadyQuizzes(LocalDateTime.now());

        for (Quiz quiz : quizzes) {
            Long id = quiz.getId();
            quizDao.updateStatusToOpen(id);

            try {
                String key = "quiz:open:" + id;
                String value = objectMapper.writeValueAsString(quiz);
                long minute = 120;

                redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(minute));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("퀴즈 Redis 캐싱 실패", e);
            }
        }
    }
}
