DROP TABLE IF EXISTS public.reservations CASCADE;
DROP TABLE IF EXISTS public.reservation_periods CASCADE;
DROP TABLE IF EXISTS public.coworking_space_facilities CASCADE;
DROP TABLE IF EXISTS public.coworking_spaces CASCADE;
DROP TABLE IF EXISTS public.facilities CASCADE;
DROP TABLE IF EXISTS public.coworking_types CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TYPE public.user_role AS ENUM ('ADMIN', 'CUSTOMER');

CREATE TABLE public.users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     TEXT             NOT NULL,
    password TEXT             NOT NULL,
    role     public.user_role NOT NULL
);

INSERT INTO public.users (name, password, role)
VALUES ('a', crypt('1', gen_salt('bf')), 'ADMIN'),
       ('c', crypt('3', gen_salt('bf')), 'CUSTOMER');

CREATE TABLE public.coworking_types
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO public.coworking_types (description)
VALUES ('Open Space'),
       ('Private Office'),
       ('Co Living');

CREATE TABLE public.facilities
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO public.facilities (description)
VALUES ('parking'),
       ('wifi'),
       ('kitchen'),
       ('printer'),
       ('conditioning');

CREATE TABLE public.coworking_spaces
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    admin_id BIGINT           NOT NULL,
    price    DOUBLE PRECISION NOT NULL,
    type_id  BIGINT           NOT NULL,

    FOREIGN KEY (type_id) REFERENCES public.coworking_types (id),
    FOREIGN KEY (admin_id) REFERENCES public.users (id) ON DELETE CASCADE
);

CREATE TABLE public.coworking_space_facilities
(
    coworking_space_id BIGINT REFERENCES public.coworking_spaces (id) ON DELETE CASCADE,
    facility_id        BIGINT REFERENCES public.facilities (id) ON DELETE CASCADE,
    PRIMARY KEY (coworking_space_id, facility_id)
);

CREATE TABLE public.reservation_periods
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    coworking_space_id BIGINT    NOT NULL REFERENCES public.coworking_spaces (id) ON DELETE CASCADE,
    start_time         TIMESTAMP NOT NULL,
    end_time           TIMESTAMP NOT NULL,
    CONSTRAINT reservation_periods_check_time CHECK (start_time < end_time)
);

CREATE TABLE public.reservations
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id        BIGINT NOT NULL,
    coworking_space_id BIGINT NOT NULL,
    period_id          BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES public.users (id) ON DELETE CASCADE,
    FOREIGN KEY (coworking_space_id) REFERENCES public.coworking_spaces (id) ON DELETE CASCADE,
    FOREIGN KEY (period_id) REFERENCES public.reservation_periods (id) ON DELETE CASCADE
);
