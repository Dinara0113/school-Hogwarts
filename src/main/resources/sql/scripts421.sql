-- Создание таблицы Faculty (Факультеты)
CREATE TABLE IF NOT EXISTS Faculty (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    color VARCHAR(50) NOT NULL
);

-- Создание таблицы Student (Студенты)
CREATE TABLE IF NOT EXISTS Student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT CHECK (age > 0) NOT NULL,
    faculty_id INT,
    avatar_url TEXT, -- Поле для аватарки
    FOREIGN KEY (faculty_id) REFERENCES Faculty(id) ON DELETE SET NULL
);

