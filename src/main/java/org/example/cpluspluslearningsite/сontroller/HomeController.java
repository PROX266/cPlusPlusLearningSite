package org.example.cpluspluslearningsite.сontroller;

import lombok.RequiredArgsConstructor;
import org.example.cpluspluslearningsite.repository.TopicRepository;
import org.example.cpluspluslearningsite.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("modules", topicRepository.findAllByOrderByOrderIndexAsc());
        long count = userRepository.count() + 1000;
        model.addAttribute("studentsCount", String.valueOf(count));
        return "index";
    }
}