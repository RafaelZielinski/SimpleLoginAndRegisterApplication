-- liquibase formatted sql
-- changeset rafaelzielinski:12

ALTER TABLE USERS
ADD COLUMN phone VARCHAR (20) DEFAULT NULL;
