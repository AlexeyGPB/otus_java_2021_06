-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table addresses
(
    id     bigint not null primary key,
    street varchar(256)
);

create table clients
(
    id         bigint not null primary key,
    name       varchar(50),
    address_id bigint references addresses(id)
);

create table phones
(
    id     bigint not null primary key,
    number varchar(50),
    client_id bigint references clients(id)
);





