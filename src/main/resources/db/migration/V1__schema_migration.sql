CREATE TABLE limits (
                        id BIGSERIAL PRIMARY KEY,
                        client_id BIGSERIAL NOT NULL,
                        daily_limit DECIMAL(10, 2) NOT NULL DEFAULT 10000.00
);
