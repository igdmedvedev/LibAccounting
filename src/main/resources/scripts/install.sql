create sequence person_seq
    as integer;

create table Person
(
    id         integer
        constraint person_pk
            primary key
                                default nextval('person_seq'),
    firstName  varchar not null,
    middleName varchar,
    lastName   varchar not null,
    birthday   date    not null,
    email      varchar
        constraint email_uk
            unique     not null,
    gender     varchar not null,
    createTime date    not null default now()
        constraint check_gender
            check (gender = 'F' or gender = 'M')
);

create sequence book_seq
    as integer;

create table Book
(
    id         integer
        constraint book_pk
            primary key
        default nextval('book_seq'),
    personId   integer  references person (id) on delete set null,
    name       varchar  not null,
    authorName varchar  not null,
    year       smallint not null
);

create sequence librarian_seq
    as integer;

create table Librarian
(
    id       integer
        constraint librarian_pk
            primary key
        default nextval('librarian_seq'),
    username varchar(20)  not null
        constraint username_uk
            unique not null,
    password varchar(200) not null,
    role varchar(100) not null
    /*For normalization, it is worth selecting a separate table "Roles".
    To simplify lets leave it like this*/
);