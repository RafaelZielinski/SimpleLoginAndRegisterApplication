-- liquibase formatted sql
-- changeset rafaelzielinski:11

DROP TABLE IF EXISTS TwoFactorVerifications;

CREATE TABLE TwoFactorVerifications (
  id SERIAL PRIMARY KEY NOT NULL,
  user_id INTEGER NOT NULL,
  code VARCHAR(10) NOT NULL,
  expire_date TIMESTAMP NOT NULL,
  FOREIGN KEY (user_id) REFERENCES Users(id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT UQ_TwoFactorVerifications_User_Id UNIQUE (user_id),
  CONSTRAINT UQ_TwoFactorVerifications_Code UNIQUE (code)
);