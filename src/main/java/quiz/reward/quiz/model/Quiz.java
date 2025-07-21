package quiz.reward.quiz.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Quiz {
    private Long id;
    private String title;
    private String question;
    private String correctAnswer;
    private String status;
    private LocalDateTime openTime;
    private String rewardPlanJson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}