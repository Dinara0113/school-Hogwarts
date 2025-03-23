-- Создание таблицы Person (Люди)
CREATE TABLE IF NOT EXISTS Person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT CHECK (age > 0) NOT NULL,
    has_license BOOLEAN NOT NULL -- Признак наличия прав
);

-- Создание таблицы Car (Машины)
CREATE TABLE IF NOT EXISTS Car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) CHECK (price >= 0) NOT NULL
);

-- Создание таблицы связей (чтобы несколько людей могли пользоваться одной машиной)
CREATE TABLE IF NOT EXISTS Person_Car (
    person_id INT NOT NULL,
    car_id INT NOT NULL,
    PRIMARY KEY (person_id, car_id),
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES Car(id) ON DELETE CASCADE
);

