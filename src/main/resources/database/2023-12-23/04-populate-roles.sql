--liquibase formatted sql
--changeset rafaelzielinski:4

INSERT INTO ROLES (name, permission)
VALUES ('ROLE_USER', 'READ:USER,READ:CUSTOMER'),
       ('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
       ('ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
       ('ROLE_SYSADMIN',
        'READ:USER:READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');