   insert into cache_items (item_key, tag, item_value, expires_at) values
    ('key-1', 'USER_PROFILE', '{"first_name": "Francis"}', '3000-12-12 22:22:22'),
    ('key-1', 'USER_PROFILE', '{"first_name": "John"}', '1000-12-12 22:22:22'),
    ('key-2', 'USER_PROFILE', '{"first_name": "Ronald"}', '1000-12-12 22:22:22'),
    ('uid-100', 'USER_PROFILE', '{"first_name": "John", "last_name": "Doe", "id": "uid-100"}', '3000-12-12 22:22:22'),
    ('1:uid-100', 'TEAM_MEMBER', '{"user_id": "uid-100", "roles": ["ADMIN", "PRODUCT_OWNER"]}', '3000-12-12 22:22:22'),
    ('1:uid-100', 'TEAM_MEMBER', '{"user_id": "uid-100", "roles": ["ADMIN", "PRODUCT_OWNER"]}', '1000-12-12 22:22:22'),
    ('100', 'TEAM', '{"id": 100, "name": "First Local Team"}', '3000-12-12 22:22:22'),
    ('100', 'TEAM', '{"id": 100, "name": "First Expired Team"}', '1000-12-12 22:22:22');

    insert into plannings (id, team_id, title, start_at, timezone, status, created_at) values
        (1, 1, 'Planning Scheduled', '2030-01-01 23:50:00', 'Europe/Warsaw', 'SCHEDULED', '2020-01-01 00:00:00'),
        (2, 1, 'Planning Finished', '2021-01-01 23:50:00', 'Europe/Warsaw', 'FINISHED', '2020-01-01 00:00:00'),
        (3, 1, 'Planning Scheduled', '2028-01-01 23:50:00', 'Europe/Warsaw', 'SCHEDULED', '2020-01-01 00:00:00'),
        (4, 1, 'Planning Progressing', '2031-01-01 23:50:00', 'Europe/Warsaw', 'PROGRESSING', '2020-01-01 00:00:00'),
        (5, 1, 'Planning Progressing', '2031-01-01 23:50:00', 'Europe/Warsaw', 'PROGRESSING', '2020-01-01 00:00:00'),
        (6, 1, 'Planning Voting', '2031-01-01 23:50:00', 'Europe/Warsaw', 'VOTING', '2020-01-01 00:00:00'),
        (7, 1, 'Planning Voting', '2031-01-01 23:50:00', 'Europe/Warsaw', 'VOTING', '2020-01-01 00:00:00'),
        (100, 120, 'To export', '2011-01-01 23:50:00', 'Europe/Warsaw', 'FINISHED', '2011-01-01 00:00:00');

insert into items (id, title, description, focused, planning_id, created_at, estimation) values
    (1, 'Some Item', '', 0, 1, '2020-01-01 12:12:12', null),
    (2, 'Some Item', '', 1, 1, '2020-01-01 13:12:12', null),
    (3, 'Some Item', '', 0, 4, '2020-01-01 14:12:12', null),
    (4, 'Some Item', '', 1, 4, '2020-01-01 15:12:12', null),
    (5, 'Some Item', '', 0, 4, '2020-01-01 16:12:12', null),
    (6, 'Some Item', '', 0, 5, '2020-01-01 17:12:12', null),
    (7, 'Some Item', '', 1, 5, '2020-01-01 18:12:12', null),
    (8, 'Some Item', '', 0, 7, '2020-01-01 18:12:12', null),
    (9, 'Some Item', '', 0, 7, '2020-01-01 18:12:12', null),
    (10, 'Some Item', '', 1, 7, '2020-01-01 18:12:12', null),
    (11, 'Some Item', '', 0, 100, '2020-01-01 18:12:12', 'L'),
    (12, 'Some Item', '', 0, 100, '2020-01-01 18:12:12', 'S');

insert into votes (id, user_id, item_id, estimation_value) values
    (1, 'uid-2', 10, 'XL');