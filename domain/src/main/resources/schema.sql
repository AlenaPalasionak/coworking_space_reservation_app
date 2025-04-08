
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TYPE public.user_role AS ENUM ('ADMIN', 'CUSTOMER');

CREATE TABLE public.users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     TEXT             NOT NULL,
    password TEXT             NOT NULL,
    role     public.user_role NOT NULL
);

CREATE TABLE public.facilities
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE public.coworking_spaces
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    admin_id BIGINT           NOT NULL,
    price    DOUBLE PRECISION NOT NULL,
    type     VARCHAR(50)      NOT NULL,

    FOREIGN KEY (admin_id) REFERENCES public.users (id) ON DELETE CASCADE
);

CREATE TABLE public.coworking_space_facilities
(
    coworking_space_id BIGINT REFERENCES public.coworking_spaces (id) ON DELETE CASCADE,
    facility_id        BIGINT REFERENCES public.facilities (id) ON DELETE CASCADE,
    PRIMARY KEY (coworking_space_id, facility_id)
);

CREATE TABLE public.reservations
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id        BIGINT    NOT NULL REFERENCES public.users (id) ON DELETE CASCADE,
    coworking_space_id BIGINT    NOT NULL REFERENCES public.coworking_spaces (id) ON DELETE CASCADE,
    start_time         TIMESTAMP NOT NULL,
    end_time           TIMESTAMP NOT NULL
);

INSERT INTO public.facilities (type)
VALUES ('PARKING'),
       ('WIFI'),
       ('KITCHEN'),
       ('PRINTER'),
       ('CONDITIONING');

INSERT INTO public.users (name, password, role)
VALUES ('a', crypt('1', gen_salt('bf')), 'ADMIN'),
       ('c', crypt('3', gen_salt('bf')), 'CUSTOMER');