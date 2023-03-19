-- 1. add uuid_id columns to each table
ALTER TABLE clinics
    ADD COLUMN IF NOT EXISTS uuid_id UUID UNIQUE;

ALTER TABLE patients
    ADD COLUMN IF NOT EXISTS uuid_id UUID UNIQUE;

ALTER TABLE reminders
    ADD COLUMN IF NOT EXISTS uuid_id UUID UNIQUE;

ALTER TABLE roles
    ADD COLUMN IF NOT EXISTS uuid_id UUID UNIQUE;

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS uuid_id UUID UNIQUE;

ALTER TABLE visits
    ADD COLUMN IF NOT EXISTS uuid_id UUID UNIQUE;

-- 2. populate uuid ids
UPDATE clinics
SET uuid_id = gen_random_uuid()
WHERE uuid_id IS NULL;

UPDATE patients
SET uuid_id = gen_random_uuid()
WHERE uuid_id IS NULL;

UPDATE reminders
SET uuid_id = gen_random_uuid()
WHERE uuid_id IS NULL;

UPDATE roles
SET uuid_id = gen_random_uuid()
WHERE uuid_id IS NULL;

UPDATE users
SET uuid_id = gen_random_uuid()
WHERE uuid_id IS NULL;

UPDATE visits
SET uuid_id = gen_random_uuid()
WHERE uuid_id IS NULL;

-- 3. add uuid columns for foreign keys
ALTER TABLE clinics
    ADD COLUMN IF NOT EXISTS uuid_doctor_id UUID;

ALTER TABLE patients
    ADD COLUMN IF NOT EXISTS uuid_doctor_id UUID;

ALTER TABLE reminders
    ADD COLUMN IF NOT EXISTS uuid_doctor_id UUID;
ALTER TABLE reminders
    ADD COLUMN IF NOT EXISTS uuid_patient_id UUID;

ALTER TABLE user_roles
    ADD COLUMN IF NOT EXISTS uuid_user_id UUID;
ALTER TABLE user_roles
    ADD COLUMN IF NOT EXISTS uuid_role_id UUID;

ALTER TABLE visits
    ADD COLUMN IF NOT EXISTS uuid_clinic_id UUID;
ALTER TABLE visits
    ADD COLUMN IF NOT EXISTS uuid_doctor_id UUID;
ALTER TABLE visits
    ADD COLUMN IF NOT EXISTS uuid_patient_id UUID;

-- 4. populate uuid ids of foreign keys

-- 4.1 clinics / doctor_id
WITH subquery AS (SELECT id, uuid_id
                  FROM users)
UPDATE clinics
SET uuid_doctor_id = subquery.uuid_id
FROM subquery
WHERE uuid_doctor_id IS NULL
  AND clinics.doctor_id = subquery.id;

-- 4.2 patients / doctor_id
WITH subquery AS (SELECT id, uuid_id
                  FROM users)
UPDATE patients
SET uuid_doctor_id = subquery.uuid_id
FROM subquery
WHERE uuid_doctor_id IS NULL
  AND patients.doctor_id = subquery.id;

-- 4.3.1 reminders / doctor_id
WITH subquery AS (SELECT id, uuid_id
                  FROM users)
UPDATE reminders
SET uuid_doctor_id = subquery.uuid_id
FROM subquery
WHERE uuid_doctor_id IS NULL
  AND reminders.doctor_id = subquery.id;

-- 4.3.2 reminders / patient_id
WITH subquery AS (SELECT id, uuid_id
                  FROM patients)
UPDATE reminders
SET uuid_patient_id = subquery.uuid_id
FROM subquery
WHERE uuid_patient_id IS NULL
  AND reminders.patient_id = subquery.id;

-- 4.4.1 user_roles / user_id
WITH subquery AS (SELECT id, uuid_id
                  FROM users)
UPDATE user_roles
SET uuid_user_id = subquery.uuid_id
FROM subquery
WHERE uuid_user_id IS NULL
  AND user_roles.user_id = subquery.id;

-- 4.4.2 user_roles / role_id
WITH subquery AS (SELECT id, uuid_id
                  FROM roles)
UPDATE user_roles
SET uuid_role_id = subquery.uuid_id
FROM subquery
WHERE uuid_role_id IS NULL
  AND user_roles.role_id = subquery.id;

-- 4.5.1 visits / clinic_id
WITH subquery AS (SELECT id, uuid_id
                  FROM clinics)
UPDATE visits
SET uuid_clinic_id = subquery.uuid_id
FROM subquery
WHERE uuid_clinic_id IS NULL
  AND visits.clinic_id = subquery.id;

-- 4.5.2 visits / doctor_id
WITH subquery AS (SELECT id, uuid_id
                  FROM users)
UPDATE visits
SET uuid_doctor_id = subquery.uuid_id
FROM subquery
WHERE uuid_doctor_id IS NULL
  AND visits.doctor_id = subquery.id;

-- 4.5.3 visits / patient_id
WITH subquery AS (SELECT id, uuid_id
                  FROM patients)
UPDATE visits
SET uuid_patient_id = subquery.uuid_id
FROM subquery
WHERE uuid_patient_id IS NULL
  AND visits.patient_id = subquery.id;

-- 5. change PK in each table, drop old id columns, rename uuid_id columns to id
ALTER TABLE clinics
    DROP CONSTRAINT IF EXISTS clinics_pkey CASCADE,
    ADD PRIMARY KEY (uuid_id),
    DROP COLUMN id;
ALTER TABLE clinics
    RENAME uuid_id TO id;

ALTER TABLE patients
    DROP CONSTRAINT IF EXISTS patients_pkey CASCADE,
    ADD PRIMARY KEY (uuid_id),
    DROP COLUMN id;
ALTER TABLE patients
    RENAME uuid_id TO id;

ALTER TABLE reminders
    DROP CONSTRAINT IF EXISTS reminders_pkey CASCADE,
    ADD PRIMARY KEY (uuid_id),
    DROP COLUMN id;
ALTER TABLE reminders
    RENAME uuid_id TO id;

ALTER TABLE roles
    DROP CONSTRAINT IF EXISTS roles_pkey CASCADE,
    ADD PRIMARY KEY (uuid_id),
    DROP COLUMN id;
ALTER TABLE roles
    RENAME uuid_id TO id;

ALTER TABLE users
    DROP CONSTRAINT IF EXISTS users_pkey CASCADE,
    ADD PRIMARY KEY (uuid_id),
    DROP COLUMN id;
ALTER TABLE users
    RENAME uuid_id TO id;

ALTER TABLE visits
    DROP CONSTRAINT IF EXISTS visits_pkey CASCADE,
    ADD PRIMARY KEY (uuid_id),
    DROP COLUMN id;
ALTER TABLE visits
    RENAME uuid_id TO id;

-- 6. recreate foreign keys, drop old columns, rename new uuid columns
ALTER TABLE clinics
    ADD CONSTRAINT fk_clinics_users FOREIGN KEY (uuid_doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    DROP COLUMN doctor_id;
ALTER TABLE clinics
    RENAME uuid_doctor_id TO doctor_id;

ALTER TABLE patients
    ADD CONSTRAINT fk_patients_users FOREIGN KEY (uuid_doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    DROP COLUMN doctor_id;
ALTER TABLE patients
    RENAME uuid_doctor_id TO doctor_id;

ALTER TABLE reminders
    ADD CONSTRAINT fk_reminders_users FOREIGN KEY (uuid_doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_reminders_patients FOREIGN KEY (uuid_patient_id) REFERENCES patients (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    DROP COLUMN doctor_id,
    DROP COLUMN patient_id;
ALTER TABLE reminders
    RENAME uuid_doctor_id TO doctor_id;
ALTER TABLE reminders
    RENAME uuid_patient_id TO patient_id;

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_users FOREIGN KEY (uuid_user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_user_roles_roles FOREIGN KEY (uuid_role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    DROP COLUMN user_id,
    DROP COLUMN role_id;
ALTER TABLE user_roles
    RENAME uuid_user_id TO user_id;
ALTER TABLE user_roles
    RENAME uuid_role_id TO role_id;

ALTER TABLE visits
    ADD CONSTRAINT fk_visits_clinics FOREIGN KEY (uuid_clinic_id) REFERENCES clinics (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_visits_users FOREIGN KEY (uuid_doctor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    ADD CONSTRAINT fk_visits_patients FOREIGN KEY (uuid_patient_id) REFERENCES patients (id) ON DELETE CASCADE ON UPDATE RESTRICT,
    DROP COLUMN clinic_id,
    DROP COLUMN doctor_id,
    DROP COLUMN patient_id;
ALTER TABLE visits
    RENAME uuid_clinic_id TO clinic_id;
ALTER TABLE visits
    RENAME uuid_doctor_id TO doctor_id;
ALTER TABLE visits
    RENAME uuid_patient_id TO patient_id;

-- 7. drop sequence
DROP SEQUENCE IF EXISTS global_seq;
