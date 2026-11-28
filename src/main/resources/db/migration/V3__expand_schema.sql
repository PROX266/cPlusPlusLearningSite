-- 1. Таблица курсов
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    level VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO courses (title, description, level)
VALUES ('Полный курс C++ с нуля до Pro', 'Фундаментальный курс по современному C++20/23 с практикой и задачами.', 'Все уровни');

-- 2. Переименовываем course_modules в topics и привязываем к courses
ALTER TABLE course_modules RENAME TO topics;
ALTER TABLE topics ADD COLUMN course_id BIGINT;
UPDATE topics SET course_id = (SELECT id FROM courses LIMIT 1);
ALTER TABLE topics ALTER COLUMN course_id SET NOT NULL;
ALTER TABLE topics ADD CONSTRAINT fk_topics_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE;

-- 3. Обновляем связь в lessons (module_id -> topic_id)
ALTER TABLE lessons RENAME COLUMN module_id TO topic_id;

-- 4. Таблица блоков контента урока (текст, код, заметки)
CREATE TABLE lesson_contents (

    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    content_type VARCHAR(20) NOT NULL, -- TEXT, CODE, NOTE
    body TEXT NOT NULL,
    order_index INT NOT NULL
);

-- Переносим старую теорию из lessons в lesson_contents
INSERT INTO lesson_contents (lesson_id, content_type, body, order_index)
SELECT id, 'TEXT', theory_content, 1
FROM lessons
WHERE theory_content IS NOT NULL;

-- 5. Таблица категорий (направления/теги задач)
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    description TEXT
);

INSERT INTO categories (name, description) VALUES
    ('Основы C++', 'Синтаксис, ввод-вывод, типы данных и базовые конструкции'),
    ('Память и указатели', 'Работа со стеком, кучей, ссылками и умными указателями'),
    ('ООП', 'Классы, наследование, полиморфизм и инкапсуляция'),
    ('STL и Алгоритмы', 'Контейнеры, итераторы и алгоритмические задачи');

-- 6. Таблица практических заданий (lesson_id может быть NULL для отдельного задачника)
CREATE TABLE exercises (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE SET NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    difficulty VARCHAR(20) NOT NULL, -- EASY, MEDIUM, HARD
    starter_code TEXT,
    order_index INT NOT NULL DEFAULT 1
);

-- Промежуточная таблица: многие-ко-многим между задачами и категориями
CREATE TABLE exercise_categories (
    exercise_id BIGINT NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (exercise_id, category_id)
);

-- 7. Таблица тест-кейсов (входные данные + ожидаемый вывод + скрытый ли тест)
CREATE TABLE test_cases (
    id BIGSERIAL PRIMARY KEY,
    exercise_id BIGINT NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    input_data TEXT NOT NULL DEFAULT '',
    expected_output TEXT NOT NULL,
    is_hidden BOOLEAN NOT NULL DEFAULT FALSE
);

-- Переносим существующие задачи из lessons в exercises и test_cases
INSERT INTO exercises (lesson_id, title, description, difficulty, starter_code, order_index)
SELECT id, 'Практика: ' || title, 'Напишите программу на C++, удовлетворяющую условию урока «' || title || '».', 'EASY', starter_code, 1
FROM lessons
WHERE starter_code IS NOT NULL;

INSERT INTO test_cases (exercise_id, input_data, expected_output, is_hidden)
SELECT e.id, '', l.expected_output, FALSE
FROM exercises e
         JOIN lessons l ON l.id = e.lesson_id
WHERE l.expected_output IS NOT NULL;

-- Привязываем перенесённые задачи к категории "Основы C++"
INSERT INTO exercise_categories (exercise_id, category_id)
SELECT e.id, c.id
FROM exercises e, categories c
WHERE c.name = 'Основы C++';

-- Добавим одну самостоятельную задачу (без привязки к уроку, для Задачника)
INSERT INTO exercises (lesson_id, title, description, difficulty, starter_code, order_index)
VALUES (NULL, 'Сумма двух чисел', 'Считайте два целых числа из std::cin и выведите их сумму.', 'EASY',
        '#include <iostream>\n\nint main() {\n    int a, b;\n    std::cin >> a >> b;\n    // Выведите сумму\n    return 0;\n}', 1);

INSERT INTO test_cases (exercise_id, input_data, expected_output, is_hidden)
SELECT id, '2 3', '5', FALSE FROM exercises WHERE title = 'Сумма двух чисел'
UNION ALL
SELECT id, '-10 25', '15', TRUE FROM exercises WHERE title = 'Сумма двух чисел';

-- 8. Обновляем submissions: привязываем к exercises вместо lessons + добавляем вывод компилятора
ALTER TABLE submissions ADD COLUMN exercise_id BIGINT;
ALTER TABLE submissions ADD COLUMN compiler_output TEXT;

UPDATE submissions s
SET exercise_id = (SELECT e.id FROM exercises e WHERE e.lesson_id = s.lesson_id LIMIT 1);

DELETE FROM submissions WHERE exercise_id IS NULL;

ALTER TABLE submissions ALTER COLUMN exercise_id SET NOT NULL;
ALTER TABLE submissions ADD CONSTRAINT fk_submissions_exercise FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE CASCADE;
ALTER TABLE submissions DROP COLUMN lesson_id;

-- Убираем перенесённые колонки из lessons
ALTER TABLE lessons DROP COLUMN theory_content;
ALTER TABLE lessons DROP COLUMN starter_code;
ALTER TABLE lessons DROP COLUMN expected_output;

-- 9. Таблица прогресса пользователей по урокам (с защитой от дубликатов)
CREATE TABLE user_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    completed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_lesson_progress UNIQUE (user_id, lesson_id)
);