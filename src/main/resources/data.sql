DELETE
FROM patients;
DELETE
FROM clinics;
DELETE
FROM visits;
DELETE
FROM user_roles;
DELETE
FROM roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 1000;

INSERT INTO users (username, password, first_name, middle_name, last_name, email, phone, birth_date)
VALUES ('nastya', '$2y$12$bjmCKwn/pjo.suvNTJeuluYlF0aDERqM2qE1eq1MPd6QPFrczYmPW', 'Анастасия', 'Леонидовна',
        'Колесникова', 'doclogvinova@ya.ru', '+7(966)043-1809', '1989-06-23'),
       ('katya', '$2y$12$xqESEK6d5PxiICEXkgHAU.MzD/McAC0rP24owv0QKuF7PCkN2iK.a', 'Екатерина', 'Юрьевна',
        'Шестакова', 'katya@ya.ru', '+7-966-085-1567', '1990-12-14');

INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO user_roles
VALUES (1000, 1002),
       (1001, 1002);

INSERT INTO clinics (name, phone, address, doctor_id)
VALUES ('Babenkoff', '+7(499)134-4850', 'г. Москва, ул. Академика Пилюгина, 6', 1000), /*1004*/
       ('Ст-ка Элит', '+7(499)180-1319', 'г. Москва, ул. Амундсена, 19', 1000); /*1005*/

INSERT INTO patients (first_name, middle_name, last_name, email, phone, birth_date, info, doctor_id)
VALUES ('Виталий', 'Сергеевич', 'Колесников', 'kvs07@ya.ru', '+7(917)551-2444', '1988-04-09', 'Муж', 1000), /*1006*/
       ('Дмитрий', 'Юрьевич', 'Остапенко', 'dimka@ya.ru', '+7(917)123-5566', '1988-09-01', 'Брат мужа', 1000), /*1007*/
       ('Олег', 'Вениаминович', 'Пришвин', 'oleg@ya.ru', '+7(917)123-7788', '1990-10-05', 'Друг мужа', 1000), /*1008*/
       ('Екатерина', null, 'Басманова', 'basmanova@ya.ru', '+7(917)123-8899', '1988-12-05', 'Подруга', 1001), /*1009*/
       ('Ололоша', 'Батькович', 'Бутуз', null, null, '2010-05-05', 'Пацанчик', 1000); /*1010*/

INSERT INTO visits (doctor_id, patient_id, clinic_id, visit_date, cost, percent, child, first, info)
VALUES (1000, 1006, 1004, '2020-10-15', 13400, 25, false, true, 'Каналы 26'),
       (1000, 1006, 1005, '2021-01-23', 2700, 25, false, false, 'Пломба 25'),
       (1000, 1007, 1004, '2021-02-12', 4200, 25, false, false, 'Пломба 34'),
       (1000, 1010, 1004, '2021-01-07', 500, 30, true, true, 'Осмотр');

INSERT INTO reminders (doctor_id, patient_id, date, text)
VALUES (1000, 1006, '2021-02-24', 'Прийти на осмотр'),
       (1000, 1007, '2021-02-20', 'Прийти на чистку');