--liquibase formatted sql
--changeset rafaelzielinski:6

ALTER TABLE Users
ALTER COLUMN password TYPE VARCHAR(255);