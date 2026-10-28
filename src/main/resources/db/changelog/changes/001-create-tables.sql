--liquibase formatted sql

--changeset author:001-create-users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at DATE NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX uq_users_username_active
    ON users (username) WHERE is_deleted = FALSE;

CREATE UNIQUE INDEX uq_users_email_active
    ON users (email) WHERE is_deleted = FALSE;

--changeset author:002-create-category
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

--changeset author:003-create-products
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    price NUMERIC(12,2) NOT NULL CHECK (price > 0),
    average_rating DOUBLE PRECISION NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL REFERENCES category (id),
    created_at DATE NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_products_category ON products (category_id);

--changeset author:004-create-reviews
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (id),
    product_id BIGINT NOT NULL REFERENCES products (id),
    created_at DATE NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_reviews_user_product UNIQUE (user_id, product_id)
);

CREATE INDEX idx_reviews_product ON reviews (product_id);

--changeset author:005-create-price-history
CREATE TABLE price_history (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products (id),
    old_price NUMERIC(12,2) NOT NULL,
    new_price NUMERIC(12,2) NOT NULL,
    changed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_price_history_product
    ON price_history (product_id, changed_at);
