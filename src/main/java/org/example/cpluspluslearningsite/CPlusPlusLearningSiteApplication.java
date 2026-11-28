package org.example.cpluspluslearningsite;

import org.example.cpluspluslearningsite.entity.*;
import org.example.cpluspluslearningsite.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class CPlusPlusLearningSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CPlusPlusLearningSiteApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedTestData(
            UserRepository userRepo,
            LessonRepository lessonRepo,
            ExerciseRepository exerciseRepo,
            SubmissionRepository submissionRepo,
            CommentRepository commentRepo,
            UserProgressRepository progressRepo,
            PasswordEncoder encoder) {
        return args -> {
            if (userRepo.count() > 0) {
                return;
            }

            User admin = userRepo.save(User.builder()
                    .username("admin")
                    .email("admin@cpplearn.local")
                    .passwordHash(encoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .createdAt(LocalDateTime.now().minusDays(10))
                    .build());

            User student1 = userRepo.save(User.builder()
                    .username("student")
                    .email("student@cpplearn.local")
                    .passwordHash(encoder.encode("user123"))
                    .role("ROLE_USER")
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build());

            User student2 = userRepo.save(User.builder()
                    .username("alex_cpp")
                    .email("alex@cpplearn.local")
                    .passwordHash(encoder.encode("user123"))
                    .role("ROLE_USER")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build());

            List<Lesson> lessons = lessonRepo.findAll();
            List<Exercise> exercises = exerciseRepo.findAll();

            if (exercises.size() >= 2) {
                Exercise ex1 = exercises.get(0);
                Exercise ex2 = exercises.get(1);

                submissionRepo.saveAll(List.of(
                        Submission.builder()
                                .user(student1)
                                .exercise(ex1)
                                .submittedCode("#include <iostream>\nint main() {\n    std::cout << \"Hello, World!\";\n    return 0;\n}")
                                .status("SUCCESS")
                                .compilerOutput("All test cases passed (1/1)")
                                .submittedAt(LocalDateTime.now().minusHours(5))
                                .build(),
                        Submission.builder()
                                .user(student2)
                                .exercise(ex1)
                                .submittedCode("#include <iostream>\nint main() {\n    std::cout << \"Hello\"\n    return 0;\n}")
                                .status("COMPILE_ERROR")
                                .compilerOutput("error: expected ';' before 'return'")
                                .submittedAt(LocalDateTime.now().minusHours(2))
                                .build(),
                        Submission.builder()
                                .user(student2)
                                .exercise(ex2)
                                .submittedCode("#include <iostream>\nint main() {\n    int a = 10;\n    std::cout << a;\n    return 0;\n}")
                                .status("WRONG_ANSWER")
                                .compilerOutput("Test #1 failed: expected '5', got '10'")
                                .submittedAt(LocalDateTime.now().minusMinutes(45))
                                .build(),
                        Submission.builder()
                                .user(student1)
                                .exercise(ex2)
                                .submittedCode("#include <iostream>\nint main() {\n    int a = 5;\n    std::cout << a;\n    return 0;\n}")
                                .status("SUCCESS")
                                .compilerOutput("All test cases passed (1/1)")
                                .submittedAt(LocalDateTime.now().minusMinutes(15))
                                .build()
                ));
            }

            if (!lessons.isEmpty()) {
                Lesson lesson1 = lessons.getFirst();

                // Отмечаем прогресс прохождения первого урока у student1
                progressRepo.save(UserProgress.builder()
                        .user(student1)
                        .lesson(lesson1)
                        .completedAt(LocalDateTime.now().minusHours(4))
                        .build());

                commentRepo.saveAll(List.of(
                        Comment.builder()
                                .user(student1)
                                .lesson(lesson1)
                                .content("Отличный урок! Наконец-то понял, зачем нужен std:: перед cout.")
                                .createdAt(LocalDateTime.now().minusHours(4))
                                .build(),
                        Comment.builder()
                                .user(student2)
                                .lesson(lesson1)
                                .content("Подскажите, а можно использовать using namespace std; в реальных проектах?")
                                .createdAt(LocalDateTime.now().minusHours(1))
                                .build(),
                        Comment.builder()
                                .user(admin)
                                .lesson(lesson1)
                                .content("@alex_cpp В заголовочных файлах (.h) категорически не рекомендуется.")
                                .createdAt(LocalDateTime.now().minusMinutes(30))
                                .build()
                ));
            }

            System.out.println(">>> База данных готова и наполнена тестовыми данными!");
        };
    }
}