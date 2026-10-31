--liquibase formatted sql

--changeset author:005-add-column-in-price-history
ALTER TABLE price_history ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;