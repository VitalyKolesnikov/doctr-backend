DELETE FROM patients;
DELETE FROM user_roles;
DELETE FROM roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 1000;

INSERT INTO users (username, password, first_name, middle_name, last_name, email, phone, birth_date)
VALUES ('nastya', '$2y$12$bjmCKwn/pjo.suvNTJeuluYlF0aDERqM2qE1eq1MPd6QPFrczYmPW', 'Анастасия', 'Леонидовна',
        'Колесникова', 'doclogvinova@ya.ru', '+7-966-043-1809', '1989-06-23'),
       ('katya', '$2y$12$xqESEK6d5PxiICEXkgHAU.MzD/McAC0rP24owv0QKuF7PCkN2iK.a', 'Екатерина', 'Юрьевна',
        'Шестакова', 'katya@ya.ru', '+7-966-085-1567', '1990-12-14');

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO user_roles VALUES (1000, 1002), (1001, 1002);

INSERT INTO patients (first_name, middle_name, last_name, email, phone, birth_date, info, doctor_id)
VALUES ('Виталий', 'Сергеевич', 'Колесников', 'kvs07@ya.ru', '+7-917-551-2444', '1988-04-09', 'Муж', 1000),
       ('Дмитрий', 'Юрьевич', 'Остапенко', 'dimka@ya.ru', '+7-917-123-5566', '1988-09-01', 'Брат мужа', 1000),
       ('Олег', 'Вениаминович', 'Пришвин', 'oleg@ya.ru', '+7-917-123-7788', '1990-10-05', 'Друг мужа', 1000),
       ('Екатерина', NULL, 'Басманова', 'basmanova@ya.ru', '+7-917-123-8899', '1988-12-05', 'Подруга', 1001);