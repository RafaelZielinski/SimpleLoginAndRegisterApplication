--liquibase formatted sql
--changeset rafaelzielinski:3

DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles
(
    id         SERIAL       NOT NULL PRIMARY KEY,
    name       VARCHAR(50)  NOT NULL,
    permission VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Roles_Name UNIQUE (name)
);