create table plannings (
    id int not null primary key auto_increment,
    team_id int not null,
    title varchar(255) not null,
    start_at datetime not null,
    timezone varchar(64) not null,
    status varchar(64) not null,
    created_at datetime not null default current_timestamp,
    updated_at datetime default null
);