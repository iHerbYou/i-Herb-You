INSERT INTO user (email, password, name, phone_number, role_code_id, status_code_id, created_at, updated_at)
VALUES (
           'admin@iherbyou.com',
           '$2a$10$yy2lCKmYUTSUlBNrAC2wkeeVcE6/HXL8zwUOUzzpkN2qksb/9shvq',  -- admin1234
           'Master Admin',
           '010-0000-0000',
           (SELECT id FROM code WHERE value = 705),  -- ADMIN_MASTER
           (SELECT id FROM code WHERE value = 712),  -- ACTIVE
           NOW(),
           NOW()
       );