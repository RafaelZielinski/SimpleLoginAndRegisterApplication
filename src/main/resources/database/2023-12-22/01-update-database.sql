--liquibase formatted sql
--changeset rafaelzielinski:1

ALTER DATABASE securedatabase
SET TIMEZONE TO 'Europe/Warsaw';