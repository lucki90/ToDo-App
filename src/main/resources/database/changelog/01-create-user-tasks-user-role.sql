--liquibase formatted sql
--changeset luckit:1

CREATE TABLE todo_app.tasks
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT,
    task_subject   VARCHAR(400) NOT NULL,
    task_details VARCHAR(400),
    task_deadline timestamp
);

--changeset luckit:2
CREATE TABLE todo_app.user
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT,
    owner_login   VARCHAR(400) NOT NULL,
    password VARCHAR(400),
    first_name VARCHAR(400),
    last_name VARCHAR(400),
    email VARCHAR(400),
    phone_number VARCHAR(400),
    task_deadline timestamp
);
--changeset luckit:3
CREATE TABLE todo_app.user_role
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(400),
    description VARCHAR(400)
);

--changeset luckit:4
ALTER TABLE todo_app.tasks
    ADD CONSTRAINT tasks_user_id
        FOREIGN KEY (user_id) REFERENCES todo_app.user(id);

--changeset luckit:5
ALTER TABLE todo_app.user
    ADD CONSTRAINT user_user_role_id
        FOREIGN KEY (role_id) REFERENCES todo_app.user_role(id);

