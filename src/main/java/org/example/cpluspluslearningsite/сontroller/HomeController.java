package com.example.cpplearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    // Временная модель для карточек курсов
    public record CourseModule(String title, String description, String level, int lessonsCount) {}

    @GetMapping("/")
    public String home(Model model) {
        List<CourseModule> modules = List.of(
                new CourseModule("Основы C++", "Переменные, типы данных, циклы, условия и базовый ввод/вывод (iostream).", "Новичок", 14),
                new CourseModule("Память и Указатели", "Указатели, ссылки, динамическая память (Stack vs Heap) и умные указатели.", "Средний", 10),
                new CourseModule("ООП в C++", "Классы, объекты, инкапсуляция, наследование, полиморфизм и виртуальные функции.", "Средний", 16),
                new CourseModule("Стандартная библиотека (STL)", "Контейнеры (vector, map, set), итераторы, алгоритмы и шаблоны.", "Продвинутый", 12),
                new CourseModule("Современный C++ (C++17/20/23)", "Move-семантика, лямбда-выражения, многопоточность и концепты.", "Профи", 8)
        );

        model.addAttribute("modules", modules);
        model.addAttribute("studentsCount", "12,450");

        return "index"; //Ищет файл index.html в папке templates
    }
}