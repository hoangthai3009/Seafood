INSERT INTO role (role_id, description, role_name)
SELECT '1', 'Quản trị viên', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_name = 'ADMIN');

INSERT INTO role (role_id, description, role_name)
SELECT '2', 'Người dùng', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE role_name = 'USER');

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

INSERT INTO user (user_id, email,fullname, phone, username, password)
SELECT '1', 'user1@gmail.com', 'Người dùng 1', '0123456789', 'user1', 'user1'
WHERE NOT EXISTS(SELECT 1 FROM user WHERE fullname = 'Người dùng 1');

INSERT INTO `bill` (`id`, `created_at`, `total_price`, `user_id`)
SELECT '1', '2023-07-01', '2000000', '1'
WHERE NOT EXISTS(SELECT 1 FROM bill WHERE id = '1' );

INSERT INTO `bill` (`id`, `created_at`, `total_price`, `user_id`)
SELECT '2', '2023-08-01', '4000000', '1'
WHERE NOT EXISTS(SELECT 1 FROM bill WHERE id = '2' );

INSERT INTO `bill` (`id`, `created_at`, `total_price`, `user_id`)
SELECT '3', '2023-09-01', '2800000', '1'
WHERE NOT EXISTS(SELECT 1 FROM bill WHERE id = '3' );

INSERT INTO `bill` (`id`, `created_at`, `total_price`, `user_id`)
SELECT '4', '2023-07-01', '4800000', '1'
WHERE NOT EXISTS(SELECT 1 FROM bill WHERE id = '4' );

