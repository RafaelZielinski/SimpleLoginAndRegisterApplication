--liquibase formatted sql
--changeset rafaelzielinski:9

DROP TABLE IF EXISTS Books;

CREATE TABLE Books
(
  id SERIAL NOT NULL PRIMARY KEY ,
  title VARCHAR(50) NOT NULL,
  author VARCHAR(50) NOT NULL,
  genre VARCHAR(50) NOT NULL,
  publisher VARCHAR(50) NOT NULL,
  pages INTEGER CHECK (pages > 0),
  CONSTRAINT UQ_Books_title UNIQUE (title)
);