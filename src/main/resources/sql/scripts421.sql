CREATE TABLE IF NOT EXISTS Faculty(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    color VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    faculty_id INT NULL,
    FOREIGN KEY (faculty_id) REFERENCES Faculty(id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS Person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    has_license BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS Car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Person_Car (
    person_id INT REFERENCES Person(id) ON DELETE CASCADE,
    car_id INT REFERENCES Car(id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, car_id)
);

SELECT s.name AS student_name, s.age, f.name AS faculty_name
FROM Student s
JOIN Faculty f ON s.faculty_id = f.id;

SELECT s.name AS student_name, s.age
FROM Student s
WHERE s.avatar IS NOT NULL;

