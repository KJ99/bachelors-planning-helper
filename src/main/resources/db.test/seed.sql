   insert into cache_items (item_key, tag, item_value, expires_at) values
    ('key-1', 'USER_PROFILE', '{"first_name": "Francis"}', '3000-12-12 22:22:22'),
    ('key-1', 'USER_PROFILE', '{"first_name": "John"}', '1000-12-12 22:22:22'),
    ('key-2', 'USER_PROFILE', '{"first_name": "Ronald"}', '1000-12-12 22:22:22'),
    ('uid-100', 'USER_PROFILE', '{"first_name": "John", "last_name": "Doe", "id": "uid-100"}', '3000-12-12 22:22:22'),
    ('1:uid-100', 'TEAM_MEMBER', '{"user_id": "uid-100", "roles": ["ADMIN", "PRODUCT_OWNER"]}', '3000-12-12 22:22:22'),
    ('1:uid-100', 'TEAM_MEMBER', '{"user_id": "uid-100", "roles": ["ADMIN", "PRODUCT_OWNER"]}', '1000-12-12 22:22:22');

    insert into plannings (id, team_id, title, start_at, timezone, status, created_at) values
        (1, 1, 'Planning Scheduled', '2030-01-01 23:50:00', 'Europe/Warsaw', 'SCHEDULED', '2020-01-01 00:00:00'),
        (2, 1, 'Planning Scheduled', '2021-01-01 23:50:00', 'Europe/Warsaw', 'FINISHED', '2020-01-01 00:00:00');