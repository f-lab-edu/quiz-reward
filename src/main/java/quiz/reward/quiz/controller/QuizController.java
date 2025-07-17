package quiz.reward.quiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentQuiz() {
        Set<String> keys = redisTemplate.keys("quiz:open:*");

        if (keys == null || keys.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "현재 오픈된 퀴즈가 없습니다."));
        }

        // 여러 개일 경우 가장 먼저 하나만 가져오기
        String quizKey = keys.iterator().next();
        String quizJson = redisTemplate.opsForValue().get(quizKey);

        try {
            Map<String, Object> quiz = objectMapper.readValue(quizJson, Map.class);
            return ResponseEntity.ok(quiz);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "퀴즈 데이터 파싱 실패"));
        }
    }
}
