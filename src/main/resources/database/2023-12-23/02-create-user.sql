--liquibase formatted sql
--changeset rafaelzielinski:2

DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    id           SERIAL      NOT NULL PRIMARY KEY,
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    email        VARCHAR(50) NOT NULL,
    age          INTEGER CHECK ( age BETWEEN 1 AND 100),
    password     VARCHAR(50) NOT NULL,
    enabled      BOOLEAN     NOT NULL DEFAULT FALSE,
    non_locked   BOOLEAN     NOT NULL DEFAULT TRUE,
    using_mfa    BOOLEAN     NOT NULL DEFAULT FALSE,
    created_date TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT UQ_Users_Email UNIQUE (EMAIL)
);
