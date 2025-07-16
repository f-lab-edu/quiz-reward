package quiz.reward.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RewardRedisService {

    private final StringRedisTemplate redisTemplate;

    public Long allocateReward(String quizId, String userId, List<Integer> rewardPlan) {
        String luaScript = loadLuaScript("lua/reward_allocation.lua");

        List<String> keys = Arrays.asList(
                "quiz:" + quizId + ":participants",
                "quiz:" + quizId + ":rewards"
        );

        List<String> args = new ArrayList<>();
        args.add(userId);
        args.add(String.valueOf(System.currentTimeMillis()));

        for (int i = 0; i < rewardPlan.size(); i++) {
            args.add(String.valueOf(rewardPlan.get(i)));
        }

        Long reward = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Long.class),
                keys,
                args.toArray()
        );

        log.info("유저 {}에게 지급된 보상: {}", userId, reward);
        return reward;
    }

    private String loadLuaScript(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Lua 스크립트 로딩 실패", e);
        }
    }
}

