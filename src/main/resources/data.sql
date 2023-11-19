INSERT INTO role (role_id, description, role_name)
SELECT '1', 'Quản trị viên', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_name = 'ADMIN');

INSERT INTO role (role_id, description, role_name)
SELECT '2', 'Người dùng', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_name = 'USER');