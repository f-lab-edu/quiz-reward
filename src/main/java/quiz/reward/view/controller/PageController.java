package quiz.reward.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/quiz")
    public String redirectToHtml() {
        return "redirect:/html/quizReward.html";
    }
}
