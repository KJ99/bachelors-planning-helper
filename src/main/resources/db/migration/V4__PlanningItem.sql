create table items (
    id int not null primary key auto_increment,
    title varchar(255) not null,
    description text default null,
    focused tinyint(1) not null,
    planning_id int not null,
    created_at datetime not null default current_timestamp,
    updated_at datetime default null,

    foreign key (planning_id)
        references plannings(id)
        on delete cascade
);