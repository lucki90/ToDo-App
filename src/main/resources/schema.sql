DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
username VARCHAR(45) NOT NULL,
password VARCHAR(45) NOT NULL,
enabled TINYINT NOT NULL DEFAULT 1,
PRIMARY KEY (username)
);

CREATE TABLE roles(
user_role_id int(11) NOT NULL AUTO_INCREMENT,
username VARCHAR(45) NOT NULL,
role_name VARCHAR(45) NOT NULL,
PRIMARY KEY (user_role_id),
UNIQUE KEY uni_username_role(role_name, username),
KEY fk_username_idx(username),
CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users(username)
);