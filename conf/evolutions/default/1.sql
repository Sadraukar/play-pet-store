# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pet (
  id                            bigserial not null,
  name                          varchar(255),
  constraint pk_pet primary key (id)
);


# --- !Downs

drop table if exists pet cascade;

