-- Таблица пользователей
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Таблица модулей курса
CREATE TABLE course_modules (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    level VARCHAR(30) NOT NULL,
    order_index INT NOT NULL
);

-- Таблица уроков внутри модуля + теория
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    module_id BIGINT NOT NULL REFERENCES course_modules(id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    theory_content TEXT NOT NULL,
    starter_code TEXT,
    expected_output TEXT,
    order_index INT NOT NULL
);

-- таблица отправленных решений
CREATE TABLE submissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    submitted_code TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- начальные данные
INSERT INTO course_modules (title, description, level, order_index) VALUES
('Основы C++', 'Переменные, типы данных, циклы, условия и базовый ввод/вывод (iostream).', 'Новичок', 1),
('Память и Указатели', 'Указатели, ссылки, динамическая память (Stack vs Heap) и умные указатели.', 'Средний', 2),
('ООП в C++', 'Классы, объекты, инкапсуляция, наследование, полиморфизм и виртуальные функции.', 'Средний', 3),
('Стандартная библиотека (STL)', 'Контейнеры (vector, map, set), итераторы, алгоритмы и шаблоны.', 'Продвинутый', 4),
('Современный C++ (C++20/23)', 'Move-семантика, лямбда-выражения, многопоточность и концепты.', 'Профи', 5);

-- тестовые уроки
INSERT INTO lessons (module_id, title, theory_content, starter_code, expected_output, order_index) VALUES
(1, 'Привет, Мир!', 'Разбираем структуру первой программы на C++ и подключение библиотеки <iostream>.', '#include <iostream>\n\nint main() {\n    // Напишите ваш код здесь\n    return 0;\n}', 'Hello, World!', 1),
(1, 'Переменные и типы данных', 'Изучаем типы int, double, char и bool, а также размер занимаемой ими памяти.', '#include <iostream>\n\nint main() {\n    int a = 5;\n    std::cout << a;\n    return 0;\n}', '5', 2);