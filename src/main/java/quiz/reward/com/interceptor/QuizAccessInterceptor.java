package quiz.reward.com.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class QuizAccessInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String uri = request.getRequestURI();

        if (uri.equals("/api/quiz/start")) {
            String quizId = request.getParameter("quizId");
            String key = getParticipationKey(request, quizId);

            Boolean exists = redisTemplate.hasKey(key);

            if (Boolean.TRUE.equals(exists)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            redisTemplate.opsForValue().set(key, "1"); // TTL 없음
        }

        return true;
    }


    private String getParticipationKey(HttpServletRequest request, String quizId) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getRemoteAddr();

        String ua = request.getHeader("User-Agent");
        String raw = ip + "_" + ua;

        String hashed = DigestUtils.md5DigestAsHex(raw.getBytes());
        return String.format("quiz:participated:%s:%s", quizId, hashed);
    }

}

