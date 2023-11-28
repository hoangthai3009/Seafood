INSERT INTO role (id, description, name)
SELECT '1', 'Quản trị viên', 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_ADMIN');

INSERT INTO role (id, description, name)
SELECT '2', 'Người dùng', 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_USER');

INSERT INTO role (id, description, name)
SELECT '3', 'Nhân viên', 'ROLE_MODERATOR'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ROLE_MODERATOR');

INSERT INTO category (id, name)
SELECT '1', 'Cá'
WHERE NOT EXISTS(SELECT 1 FROM category WHERE name = 'Cá');

INSERT INTO category (id, name)
SELECT '2', 'Tôm'
WHERE NOT EXISTS(SELECT 1 FROM category WHERE name = 'Tôm');

INSERT INTO category (id, name)
SELECT '3', 'Mực/Bạch tuộc'
WHERE NOT EXISTS(SELECT 1 FROM category WHERE name = 'Mực/Bạch tuộc');

INSERT INTO category (id, name)
SELECT '4', 'Cua/Ghẹ'
WHERE NOT EXISTS(SELECT 1 FROM category WHERE name = 'Cua/Ghẹ');
