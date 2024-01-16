--liquibase formatted sql
--changeset rafaelzielinski:7

ALTER TABLE UserRoles
ADD CONSTRAINT unique_userId_contraint
UNIQUE (user_id);