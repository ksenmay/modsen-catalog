--liquibase formatted sql

--changeset author:004-update-user-entity
ALTER TABLE users ADD COLUMN refresh_token VARCHAR(255);
ALTER TABLE users ADD COLUMN refresh_token_expiry_time TIMESTAMP;