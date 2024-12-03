create table board_tag
(
    board_id bigint not null,
    tag_id  bigint not null,
    primary key (board_id, tag_id),
    constraint FKik449po5lbsfb3ugbpmdg7x2s
        foreign key (board_id) references board (id),
    constraint FKim7h59g4iafss40a7y2kgr0qk
        foreign key (tag_id) references tag (id)
);