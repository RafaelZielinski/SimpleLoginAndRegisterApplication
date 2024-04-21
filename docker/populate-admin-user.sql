insert into users (first_name, last_name, email, age, password, enabled, non_locked, using_mfa)
values ('Rafał', 'Zieliński', 'rafekzielinski@wp.pl', 27, '$2a$12$0QRtA1obnkqw00B8l82lLeKMCQI67MihFWQu/Q1NmUgfM.mA/Fqla', true, true, false);
insert into userroles (user_id, role_id) values((select id from users where email = 'rafekzielinski@wp.pl'), 1);