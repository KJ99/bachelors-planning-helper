create table cache_items (
    id int not null primary key auto_increment,
    item_key varchar(255) not null,
    tag varchar(64) default null,
    item_value text default null,
    created_at datetime not null default current_timestamp,
    updated_at datetime default null,
    expires_at datetime not null
);