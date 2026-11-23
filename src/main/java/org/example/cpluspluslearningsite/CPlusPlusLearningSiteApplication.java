package org.example.cpluspluslearningsite;

import org.example.cpluspluslearningsite.entity.Comment;
import org.example.cpluspluslearningsite.entity.Lesson;
import org.example.cpluspluslearningsite.entity.Submission;
import org.example.cpluspluslearningsite.entity.User;
import org.example.cpluspluslearningsite.repository.CommentRepository;
import org.example.cpluspluslearningsite.repository.LessonRepository;
import org.example.cpluspluslearningsite.repository.SubmissionRepository;
import org.example.cpluspluslearningsite.repository.UserRepository;
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
            SubmissionRepository submissionRepo,
            CommentRepository commentRepo,
            PasswordEncoder encoder) {
        return args -> {
            // Если база уже заполнена пользователями, пропускаем
            if (userRepo.count() > 0) {
                return;
            }

            // 1. Создаем Админа (логин: admin, пароль: admin123)
            User admin = userRepo.save(User.builder()
                    .username("admin")
                    .email("admin@cpplearn.local")
                    .passwordHash(encoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .createdAt(LocalDateTime.now().minusDays(10))
                    .build());

            // 2. Создаем Обычного пользователя 1 (логин: student, пароль: user123)
            User student1 = userRepo.save(User.builder()
                    .username("student")
                    .email("student@cpplearn.local")
                    .passwordHash(encoder.encode("user123"))
                    .role("ROLE_USER")
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build());

            // 3. Создаем Обычного пользователя 2 (логин: alex_cpp, пароль: user123)
            User student2 = userRepo.save(User.builder()
                    .username("alex_cpp")
                    .email("alex@cpplearn.local")
                    .passwordHash(encoder.encode("user123"))
                    .role("ROLE_USER")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build());

            // Берем уроки, созданные первой миграцией Flyway
            List<Lesson> lessons = lessonRepo.findAll();
            if (lessons.size() >= 2) {
                Lesson lesson1 = lessons.get(0);
                Lesson lesson2 = lessons.get(1);

                // 4. Добавляем тестовые выполненные задания (Submissions)
                submissionRepo.saveAll(List.of(
                        Submission.builder()
                                .user(student1)
                                .lesson(lesson1)
                                .submittedCode("#include <iostream>\nint main() {\n    std::cout << \"Hello, World!\" << std::endl;\n    return 0;\n}")
                                .status("SUCCESS")
                                .submittedAt(LocalDateTime.now().minusHours(5))
                                .build(),
                        Submission.builder()
                                .user(student2)
                                .lesson(lesson1)
                                .submittedCode("#include <iostream>\nint main() {\n    std::cout << \"Hello\" // забыл точку с запятой\n    return 0;\n}")
                                .status("COMPILE_ERROR")
                                .submittedAt(LocalDateTime.now().minusHours(2))
                                .build(),
                        Submission.builder()
                                .user(student2)
                                .lesson(lesson2)
                                .submittedCode("#include <iostream>\nint main() {\n    int a = 10;\n    std::cout << a;\n    return 0;\n}")
                                .status("WRONG_ANSWER")
                                .submittedAt(LocalDateTime.now().minusMinutes(45))
                                .build(),
                        Submission.builder()
                                .user(student1)
                                .lesson(lesson2)
                                .submittedCode("#include <iostream>\nint main() {\n    int a = 5;\n    std::cout << a;\n    return 0;\n}")
                                .status("SUCCESS")
                                .submittedAt(LocalDateTime.now().minusMinutes(15))
                                .build()
                ));

                // 5. Добавляем тестовые комментарии
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
                                .content("@alex_cpp В заголовочных файлах (.h) категорически не рекомендуется, чтобы не засорять глобальное пространство имен.")
                                .createdAt(LocalDateTime.now().minusMinutes(30))
                                .build()
                ));
            }

            System.out.println(">>> Тестовые данные загружены!");
            System.out.println(">>> Админ: admin / admin123");
            System.out.println(">>> Юзер:  student / user123");
        };
    }
}