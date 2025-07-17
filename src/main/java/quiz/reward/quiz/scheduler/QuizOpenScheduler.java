package quiz.reward.quiz.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quiz.reward.quiz.service.QuizService;

@Component
@RequiredArgsConstructor
public class QuizOpenScheduler {

    private final QuizService quizService;

    @Scheduled(fixedRate = 5000)
    public void openQuizzes() {
        quizService.openScheduledQuizzes();
    }
}


