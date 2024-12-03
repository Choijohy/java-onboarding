create table user
(
    id       bigint auto_increment
        primary key,
    email    varchar(255) not null,
    password varchar(255) not null,
    constraint UKoshmjvr6wht0bg9oivn75aajr
        unique (email)
);

create table board
(
    id      bigint auto_increment
        primary key,
    user_id bigint       null,
    title   varchar(255) not null,
    content text         not null,
    constraint FKe8bj5yd6jrd9e5326rm8yk2ls
        foreign key (user_id) references user (id)
);

create table board_like
(
    board_id bigint not null,
    user_id  bigint not null,
    primary key (board_id, user_id),
    constraint FK3kd6t8lwcva1v1et2pxwjkf30
        foreign key (board_id) references board (id),
    constraint FKd1hqo0ef49ktc686ix1qby6md
        foreign key (user_id) references user (id)
);

create table comment
(
    board_id bigint not null,
    id       bigint auto_increment
        primary key,
    user_id  bigint not null,
    content  text   not null,
    constraint FKa4c86tfj8m0x641s0geuhyorn
        foreign key (board_id) references board (id),
    constraint FKsn1eiuccx1w2fdlj42s1kl75w
        foreign key (user_id) references user (id)
);

create table comment_likes
(
    comment_id bigint not null,
    user_id    bigint not null,
    primary key (comment_id, user_id),
    constraint FKdif8lcdsxynr5dpgdtlgfvlep
        foreign key (user_id) references user (id),
    constraint FKr3urcxjxxfo7rlfbnxfll81he
        foreign key (comment_id) references comment (id)
);
