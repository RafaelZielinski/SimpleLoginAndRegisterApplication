--liquibase formatted sql
--changeset rafaelzielinski:10

ALTER TABLE USERS
ALTER COLUMN age SET NOT NULL,
ALTER COLUMN age SET DEFAULT 1;
