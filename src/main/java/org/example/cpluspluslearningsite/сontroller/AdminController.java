package org.example.cpluspluslearningsite.сontroller;

import lombok.RequiredArgsConstructor;
import org.example.cpluspluslearningsite.entity.Submission;
import org.example.cpluspluslearningsite.entity.User;
import org.example.cpluspluslearningsite.repository.CommentRepository;
import org.example.cpluspluslearningsite.repository.SubmissionRepository;
import org.example.cpluspluslearningsite.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final CommentRepository commentRepository;

    @GetMapping
    public String adminDashboard(
            @RequestParam(value = "query", required = false) String query,
            Model model) {

        List<Submission> recentSubmissions = submissionRepository.findTop15ByOrderBySubmittedAtDesc();
        long successCount = recentSubmissions.stream()
                .filter(s -> "SUCCESS".equals(s.getStatus()))
                .count();

        // Логика поиска пользователей:
        List<User> users;
        if (query != null && !query.trim().isEmpty()) {
            String trimmedQuery = query.trim();
            Long userId = null;
            try {
                // Если ввели число, то ищем и по ID тоже
                userId = Long.parseLong(trimmedQuery);
            } catch (NumberFormatException ignored) {
                // Не число — значит ищем только по нику
            }
            users = userRepository.searchByIdOrUsername(userId, trimmedQuery);
        } else {
            users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        model.addAttribute("users", users);
        model.addAttribute("searchQuery", query); // возвращаем в строку поиска то, что ввел админ
        model.addAttribute("submissions", recentSubmissions);
        model.addAttribute("comments", commentRepository.findTop15ByOrderByCreatedAtDesc());

        // Сводка
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalSubmissions", submissionRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        model.addAttribute("successCount", successCount);

        return "admin";
    }
}