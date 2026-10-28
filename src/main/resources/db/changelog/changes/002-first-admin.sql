--liquibase formatted sql

--changeset author:006-insert-admin

INSERT INTO users (username, email, password_hash, role, created_at, is_deleted)
VALUES (
        'admin',
        'admin@email.com',
        '$2a$10$ai0PwaRBcWiFHhQWdczia.OOkNCtYZjc1IMuYPrw/1ouo0N99jImK',
        'ADMIN',
        CURRENT_DATE, FALSE
);