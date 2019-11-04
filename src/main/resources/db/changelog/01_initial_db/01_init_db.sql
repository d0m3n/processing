-- liquibase formatted sql

-- changeset domen.konda:01_initial_db__01_init_db.sql
-- --------------------------------------------------------
-- Init script
-- --------------------------------------------------------
create sequence hibernate_sequence start 1 increment 1;

create table revinfo (
  rev integer not null primary key,
  revtstmp bigint not null
);

create table doctor (
  id bigint not null primary key,
  version int not null default 0,

  department varchar(255) not null,

  created_at timestamp not null default now(),
  created_by varchar(255) not null default 'SYSTEM',
  modified_at timestamp not null default now(),
  modified_by varchar(255) not null default 'SYSTEM'
);

create table doctor_aud (
  id bigint not null,
  rev integer not null,
  revtype smallint not null,

  department varchar(255),

  created_at timestamp,
  created_by varchar(255),
  modified_at timestamp,
  modified_by varchar(255),
  primary key (id, rev),
  constraint fk$doctor_aud$rev foreign key (rev) references revinfo(rev)
);

create table patient (
  id bigint not null primary key,
  version int not null default 0,

  doctor_id bigint not null,
  first_name varchar(255) not null,
  last_name varchar(255) not null,

  created_at timestamp not null default now(),
  created_by varchar(255) not null default 'SYSTEM',
  modified_at timestamp not null default now(),
  modified_by varchar(255) not null default 'SYSTEM'
);

create table patient_aud (
  id bigint not null,
  rev integer not null,
  revtype smallint not null,

  doctor_id bigint,
  first_name varchar(255),
  last_name varchar(255),

  created_at timestamp,
  created_by varchar(255),
  modified_at timestamp,
  modified_by varchar(255),
  primary key (id, rev),
  constraint fk$patient_aud$rev foreign key (rev) references revinfo(rev)
);

create table disease (
  id bigint not null primary key,
  version int not null default 0,

  name varchar(255) not null,

  created_at timestamp not null default now(),
  created_by varchar(255) not null default 'SYSTEM',
  modified_at timestamp not null default now(),
  modified_by varchar(255) not null default 'SYSTEM'
);
create unique index disease_name_uindex on disease (name);


create table disease_aud (
  id bigint not null,
  rev integer not null,
  revtype smallint not null,

  name varchar(255),

  created_at timestamp,
  created_by varchar(255),
  modified_at timestamp,
  modified_by varchar(255),
  primary key (id, rev),
  constraint fk$disease_aud$rev foreign key (rev) references revinfo(rev)
);

create table document_report (
  id bigint not null primary key,
  version int not null default 0,

  execution_time bigint,
  doctor_id bigint,
  error varchar(255),
  document_source varchar(50),

  created_at timestamp not null default now(),
  created_by varchar(255) not null default 'SYSTEM',
  modified_at timestamp not null default now(),
  modified_by varchar(255) not null default 'SYSTEM'
);

create table document_report_aud (
  id bigint not null,
  rev integer not null,
  revtype smallint not null,

  execution_time bigint,
  doctor_id bigint,
  error varchar(255),
  document_source varchar(50),

  created_at timestamp,
  created_by varchar(255),
  modified_at timestamp,
  modified_by varchar(255),
  primary key (id, rev),
  constraint fk$document_report_aud$rev foreign key (rev) references revinfo(rev)
);

create table disease_patient (
  disease_id bigint not null,
  patient_id bigint not null
);

create table disease_patient_aud (
  rev integer not null,
  revtype smallint not null,
  disease_id bigint not null,
  patient_id bigint not null,
  constraint fk$disease_patient_aud$rev foreign key (rev) references revinfo(rev)
);