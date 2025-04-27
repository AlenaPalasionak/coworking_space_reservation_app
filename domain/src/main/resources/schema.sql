CREATE EXTENSION IF NOT EXISTS pgcrypto;


CREATE TABLE public.users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     TEXT NOT NULL,
    password TEXT NOT NULL,
    role     TEXT NOT NULL
);


CREATE TABLE public.coworking_spaces
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    admin_id BIGINT           NOT NULL,
    price    DOUBLE PRECISION NOT NULL,
    type     VARCHAR(50)      NOT NULL,

    FOREIGN KEY (admin_id) REFERENCES public.users (id) ON DELETE CASCADE
);

CREATE TABLE coworking_space_facilities
(
    coworking_space_id BIGINT NOT NULL,
    facility           TEXT   NOT NULL,
    PRIMARY KEY (coworking_space_id, facility),
    FOREIGN KEY (coworking_space_id) REFERENCES public.coworking_spaces (id) ON DELETE CASCADE
);

CREATE TABLE public.reservations
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id        BIGINT    NOT NULL REFERENCES public.users (id) ON DELETE CASCADE,
    coworking_space_id BIGINT    NOT NULL REFERENCES public.coworking_spaces (id) ON DELETE CASCADE,
    start_time         TIMESTAMP NOT NULL,
    end_time           TIMESTAMP NOT NULL
);

INSERT INTO public.users (name, password, role)
VALUES ('a', crypt('1', gen_salt('bf')), 'ADMIN'),
       ('c', crypt('3', gen_salt('bf')), 'CUSTOMER');