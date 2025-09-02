CREATE SEQUENCE IF NOT EXISTS category_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE categories
(
    id         BIGINT       NOT NULL       DEFAULT nextval('category_id_seq'),
    name      VARCHAR(200) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uk_categories_name UNIQUE (name)
);

ALTER TABLE bookmarks ADD COLUMN category_id BIGINT DEFAULT NULL;

INSERT INTO categories (name) VALUES ('Java');
