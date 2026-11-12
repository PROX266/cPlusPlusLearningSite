package org.example.cpluspluslearningsite.controller;

import lombok.RequiredArgsConstructor;
import org.example.cpluspluslearningsite.repository.CourseModuleRepository;
import org.example.cpluspluslearningsite.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CourseModuleRepository moduleRepository;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        // Получаем все модули из PostgreSQL, отсортированные по порядку
        model.addAttribute("modules", moduleRepository.findAllByOrderByOrderIndexAsc());

        // Получаем реальное количество зарегистрированных пользователей (+1000 как заглушка)
        long count = userRepository.count() + 1000;
        model.addAttribute("studentsCount", String.valueOf(count));

        return "index";
    }
}