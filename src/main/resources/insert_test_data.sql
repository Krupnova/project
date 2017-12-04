-- insert test users
INSERT INTO users (user_login, user_password) VALUES ('emp', 'emp');
INSERT INTO users (user_login, user_password) VALUES ('emp1', 'emp1');
INSERT INTO users (user_login, user_password) VALUES ('emp2', 'emp2');
INSERT INTO users (user_login, user_password) VALUES ('emp3', 'emp3');
INSERT INTO users (user_login, user_password) VALUES ('admin','admin');

-- insert test employees
INSERT INTO employees (employee_first_name, employee_last_name, employee_email, team_lead) VALUES ('Patrick', 'Stewart','alexsmit19@rambler.ru', null);
INSERT INTO employees (employee_first_name, employee_last_name, employee_email, team_lead) VALUES ('Bobby', 'Anderson','alexsmit19@rambler.ru', 1);
INSERT INTO employees (employee_first_name, employee_last_name, employee_email, team_lead) VALUES ('Joseph', 'Scott','alexsmit19@rambler.ru', 1);
INSERT INTO employees (employee_first_name, employee_last_name, employee_email, team_lead) VALUES ('Paul', 'Sanchez','alexsmit19@rambler.ru', null);
INSERT INTO employees (employee_first_name, employee_last_name, employee_email, team_lead, isAdmin) VALUES ('Jennifer', 'Lawrence','alexsmit19@rambler.ru', null, 'true');

-- insert test workingTimes

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-05-29 08:00:00.0', '2017-05-29 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-05-30 08:00:00.0', '2017-05-30 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-05-31 08:00:00.0', '2017-05-31 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-06-01 08:00:00.0', '2017-06-01 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-06-02 08:00:00.0', '2017-06-02 16:00:00.0', FALSE );

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-05-29 08:00:00.0', '2017-05-29 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-05-30 08:00:00.0', '2017-05-30 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-05-31 08:00:00.0', '2017-05-31 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-06-01 08:00:00.0', '2017-06-01 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-06-02 08:00:00.0', '2017-06-02 16:00:00.0', FALSE );

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-05-29 08:00:00.0', '2017-05-29 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-05-30 08:00:00.0', '2017-05-30 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-05-31 08:00:00.0', '2017-05-31 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-06-01 08:00:00.0', '2017-06-01 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-06-02 08:00:00.0', '2017-06-02 16:00:00.0', FALSE );

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-05-29 08:00:00.0', '2017-05-29 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-05-30 08:00:00.0', '2017-05-30 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-05-31 08:00:00.0', '2017-05-31 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-06-01 08:00:00.0', '2017-06-01 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-06-02 08:00:00.0', '2017-06-02 16:00:00.0', FALSE );

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-06-05 08:00:00.0', '2017-06-05 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-06-06 08:00:00.0', '2017-06-06 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-06-07 08:00:00.0', '2017-06-07 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-06-08 08:00:00.0', '2017-06-08 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (1, '2017-06-09 08:00:00.0', '2017-06-09 16:00:00.0', FALSE );

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-06-05 08:00:00.0', '2017-06-05 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-06-06 08:00:00.0', '2017-06-06 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-06-07 08:00:00.0', '2017-06-07 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-06-08 08:00:00.0', '2017-06-08 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (2, '2017-06-09 08:00:00.0', '2017-06-09 16:00:00.0', FALSE );

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-06-05 08:00:00.0', '2017-06-05 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-06-06 08:00:00.0', '2017-06-06 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-06-07 08:00:00.0', '2017-06-07 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-06-08 08:00:00.0', '2017-06-08 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (3, '2017-06-09 08:00:00.0', '2017-06-09 16:00:00.0', FALSE );

INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-06-05 08:00:00.0', '2017-06-05 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-06-06 08:00:00.0', '2017-06-06 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-06-07 08:00:00.0', '2017-06-07 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-06-08 08:00:00.0', '2017-06-08 16:00:00.0', FALSE );
INSERT INTO working_time (employee_id, working_time_start, working_time_end, holiday) VALUES (4, '2017-06-09 08:00:00.0', '2017-06-09 16:00:00.0', FALSE );