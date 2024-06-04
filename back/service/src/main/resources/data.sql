INSERT INTO AUTHORITY (name)
VALUES ('ROLE_MANAGER'); --1
INSERT INTO AUTHORITY (name)
VALUES ('ROLE_ADMIN'); --2

INSERT INTO members (email, name, surname, password, authority_id, is_enabled)
VALUES ('admin@gmail.com', 'Bandjelo', 'Kumara',
        '$2a$12$O/NJDsbdC7Fzs1eZofJUcuH3VHQAwj5hJbyF9PoI5xg9fZrljtQau', 2, true);
INSERT INTO ADMINS(id)
VALUES (1);--admin021


INSERT INTO members (email, name, surname, password, authority_id, is_enabled)
VALUES ('miki@gmail.com', 'Tupatu', 'Serbedzija',
        '$2a$12$O/NJDsbdC7Fzs1eZofJUcuH3VHQAwj5hJbyF9PoI5xg9fZrljtQau', 1, true);
INSERT INTO MANAGER(id)
VALUES (2);--admin021


-- filters
INSERT INTO FILTER (min_price, max_price, team, position)
VALUES (10, 100, 'Minnesota Timberwolves', 5);

INSERT INTO FILTER (min_price, max_price, team, position)
VALUES (10, 100, 'Golden State Warriors', 1);

INSERT INTO FILTER (min_price, max_price, team, position)
VALUES (10, 100, 'Denver Nuggets', 5);

INSERT INTO FILTER (min_price, max_price, team, position)
VALUES (10, 100, 'Los Angeles Lakers', 1);