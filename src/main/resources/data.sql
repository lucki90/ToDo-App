INSERT INTO users (username,password, enabled) VALUES
('admin','{noop}admin',true),
('tom','{noop}tom123', true);

INSERT INTO roles (username, role_name) VALUES
('admin', 'ROLE_ADMIN'),
('tom', 'ROLE_USER');