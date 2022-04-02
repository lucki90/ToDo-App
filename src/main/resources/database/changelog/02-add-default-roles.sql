--liquibase formatted sql
--changeset luckit:6


INSERT INTO todo_app.user_role
(role, description)
SELECT 'ROLE_ADMIN', 'Access to all data'
WHERE NOT EXISTS(
        SELECT role FROM todo_app.user_role WHERE role = 'ROLE_ADMIN'
    );

--changeset luckit:7
INSERT INTO todo_app.user_role
(role, description)
SELECT 'ROLE_USER', 'Access to one user data'
WHERE NOT EXISTS(
        SELECT role FROM todo_app.user_role WHERE role = 'ROLE_USER'
    );
