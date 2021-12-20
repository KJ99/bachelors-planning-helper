create table votes (
    id int not null primary key auto_increment,
    user_id varchar(255) not null,
    estimation_value varchar(10) not null,
    item_id int not null,
    foreign key (item_id)
        references items(id)
        on delete cascade
);