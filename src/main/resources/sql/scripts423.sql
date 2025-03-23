-- Запрос 1: Получить имя, возраст студентов и название их факультета
SELECT
    s.name AS student_name,
    s.age,
    f.name AS faculty_name
FROM Student s
JOIN Faculty f ON s.faculty_id = f.id;

-- Запрос 2: Получить студентов, у которых есть аватарки
SELECT
    s.name AS student_name,
    s.age,
    s.avatar_url
FROM Student s
WHERE s.avatar_url IS NOT NULL;
