# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pet (
  id                            bigserial not null,
  name                          varchar(255),
  pet_type                      integer not null,
  description                   varchar(255),
  price                         decimal(10,2),
  product_id                    varchar(255),
  color                         varchar(255),
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint ck_pet_pet_type check ( pet_type in (1,2,3,4,5)),
  constraint pk_pet primary key (id)
);

create table users (
  id                            bigserial not null,
  name                          varchar(255) not null,
  password                      varchar(255) not null,
  email                         varchar(255) not null,
  is_active                     boolean default false not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_users primary key (id)
);


# --- !Downs

drop table if exists pet cascade;

drop table if exists users cascade;

