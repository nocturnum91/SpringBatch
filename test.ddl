--- DB1 TABLE DDL
create table tb_member
(
    id               varchar(64)  not null
        primary key,
    name             varchar(60)  not null,
    email            varchar(254) not null,
    join_date        datetime default CURRENT_TIMESTAMP null,
    last_access_date datetime default CURRENT_TIMESTAMP null
);

--- DB2 TABLE DDL
create table tb_member
(
    id               varchar(64)  not null
        primary key,
    name             varchar(60)  not null,
    email            varchar(254) not null,
    join_date        timestamp default CURRENT_TIMESTAMP,
    last_access_date timestamp default CURRENT_TIMESTAMP
);