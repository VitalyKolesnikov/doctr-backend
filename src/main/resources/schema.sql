DROP TABLE IF EXISTS visits;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS clinics;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 1000;

CREATE TABLE users
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    username    VARCHAR NOT NULL,
    password    VARCHAR NOT NULL,
    first_name  VARCHAR NOT NULL,
    middle_name VARCHAR,
    last_name   VARCHAR NOT NULL,
    email       VARCHAR NOT NULL,
    phone       VARCHAR NOT NULL,
    birth_date  DATE,
    created     TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated     TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    status      VARCHAR             DEFAULT 'ACTIVE'
);
CREATE
    UNIQUE INDEX user_unique_email_idx ON users (email);

CREATE TABLE roles
(
    id      INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name    VARCHAR NOT NULL,
    created TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    status  VARCHAR             DEFAULT 'ACTIVE'
);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE patients
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    first_name  VARCHAR NOT NULL,
    middle_name VARCHAR,
    last_name   VARCHAR NOT NULL,
    email       VARCHAR,
    phone       VARCHAR,
    birth_date  DATE,
    info        VARCHAR,
    doctor_id   INTEGER NOT NULL,
    created     TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated     TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    status      VARCHAR             DEFAULT 'ACTIVE',
    FOREIGN KEY (doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE clinics
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name      VARCHAR NOT NULL,
    phone     VARCHAR,
    address   VARCHAR,
    doctor_id INTEGER NOT NULL,
    created   TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated   TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    status    VARCHAR             DEFAULT 'ACTIVE',
    FOREIGN KEY (doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE visits
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    doctor_id  INTEGER NOT NULL,
    patient_id INTEGER NOT NULL,
    clinic_id  INTEGER NOT NULL,
    visit_date DATE,
    cost       INTEGER,
    percent    INTEGER,
    child      BOOLEAN             DEFAULT FALSE,
    first      BOOLEAN             DEFAULT FALSE,
    info       VARCHAR,
    created    TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated    TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    status     VARCHAR             DEFAULT 'ACTIVE',
    FOREIGN KEY (doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    FOREIGN KEY (clinic_id) REFERENCES clinics (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE reminders
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    doctor_id  INTEGER NOT NULL,
    patient_id INTEGER NOT NULL,
    date       DATE,
    text       VARCHAR,
    created    TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated    TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    status     VARCHAR             DEFAULT 'ACTIVE',
    FOREIGN KEY (doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE ON UPDATE RESTRICT
);
