alter table items drop column estimation;
alter table items add column estimation varchar(10) default null;