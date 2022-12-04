create table "user"
(
    id       serial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    enabled  boolean
);

create table role
(
    id        serial primary key,
    role_name varchar(255) not null unique
);

create table user_role
(
    user_id int,
    role_id int,
    constraint fk_user_id foreign key (user_id) references "user" (id),
    constraint fk_role_id foreign key (role_id) references role (id)
);
