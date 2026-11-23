package org.example.cpluspluslearningsite.сontroller;

import lombok.RequiredArgsConstructor;
import org.example.cpluspluslearningsite.entity.Submission;
import org.example.cpluspluslearningsite.repository.CommentRepository;
import org.example.cpluspluslearningsite.repository.SubmissionRepository;
import org.example.cpluspluslearningsite.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final CommentRepository commentRepository;

    @GetMapping
    public String adminDashboard(Model model) {
        List<Submission> recentSubmissions = submissionRepository.findTop15ByOrderBySubmittedAtDesc();
        long successCount = recentSubmissions.stream()
                .filter(s -> "SUCCESS".equals(s.getStatus()))
                .count();

        model.addAttribute("users", userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        model.addAttribute("submissions", recentSubmissions);
        model.addAttribute("comments", commentRepository.findTop15ByOrderByCreatedAtDesc());

        // Статистика для верхних карточек
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalSubmissions", submissionRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        model.addAttribute("successCount", successCount);

        return "admin";
    }
}