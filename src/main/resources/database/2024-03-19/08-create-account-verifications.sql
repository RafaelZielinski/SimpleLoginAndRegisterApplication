--liquibase formatted sql
--changeset rafaelzielinski:8

DROP TABLE IF EXISTS AccountVerifications;

CREATE TABLE AccountVerifications
(
    id         SERIAL PRIMARY KEY NOT NULL,
    user_id    BIGINT             NOT NULL,
    url        VARCHAR(255)       NOT NULL,
    device     varchar(100) DEFAULT NULL,
    ip_address VARCHAR(100) DEFAULT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_AccountVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_AccountVerifications_User_Url UNIQUE (url)
);
