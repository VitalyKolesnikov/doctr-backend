INSERT INTO public.roles (id, name, created, updated, status) VALUES (1002, 'ROLE_USER', '2021-02-21 19:41:10.430535', '2021-02-21 19:41:10.430535', 'ACTIVE');
INSERT INTO public.roles (id, name, created, updated, status) VALUES (1003, 'ROLE_ADMIN', '2021-02-21 19:41:10.430535', '2021-02-21 19:41:10.430535', 'ACTIVE');

INSERT INTO public.users (id, username, password, first_name, middle_name, last_name, email, phone, birth_date, created, updated, status) VALUES (1001, 'admin', '$2a$12$TImIkMgSknPffCH1YFg7m.bg4KvYjA.fcMtj18bXmM8rR2a0qIk5O', 'admin', null, 'admin', 'email', 'phone', null, null, null, 'ACTIVE');

INSERT INTO public.user_roles (user_id, role_id) VALUES (1001, 1003);
