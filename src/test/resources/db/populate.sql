INSERT INTO address (id, brgy, location_code, account_prefix, accounts_count, due_day) VALUES
(1, 'Cogtong', 1, '01', 572, 20),
(2, 'Tawid', 1, '02', 199, 20),
(3, 'Can-olin', 1, '03', 428, 19),
(4, 'Cadapdapan', 2, '04', 117, 16),
(5, 'Tambongan', 2, '05', 327, 16),
(6, 'Abihilan', 2, '06', 119, 17),
(7, 'La Union', 3, '07', 257, 15),
(8, 'Panadtaran', 3, '08', 172, 15),
(9, 'Poblacion', 4, '09', 899, 10),
(10, 'Boyoan', 4, '10', 42, 10),
(11, 'Pagahat', 5, '11', 111, 15);

INSERT INTO customer (id, first_name, lastname, middle_name, gender, contact_number, family_members_count, occupation) VALUES
(1, 'Alberto', 'Bernales', 'Golosinda', 'M', '09123456789', 4, 'MPDC'),
(2, 'Magdalena', 'Bernales', 'Jamorol', 'F', '09123456789', 4, 'Teacher');

INSERT INTO account (id, number, address_id, purok, status, customer_id, account_type, account_standing_balance, status_updated) VALUES
(1, '09-00897', 9, 1, 'ACTIVE', 1, NULL, '10.00', b'1'),
(2, '09-00898', 9, 2, 'ACTIVE', 2, NULL, '235.00', b'1');

INSERT INTO device (id, owner_id, meter_code, brand, active, last_reading, start_date, end_date) VALUES
(1, 1, '9182ASDASD', 'EVER', b'1', 15, '2017-01-24', NULL),
(2, 2, '121ASDSD', 'EVER', b'1', 20, '2017-01-24', NULL);

INSERT INTO schedule (id, month, year) VALUES
(1, 12, 2016),
(2, 10, 2016),
(3, 11, 2016);

INSERT INTO meter_reading (id, account_id, reading_value, consumption, schedule_id, creation_time, created_by_user, modification_time, modified_by_user, version) VALUES
(1, 1, 10, 10, 2, '2017-01-24 16:35:37', 'developer', '2017-01-24 16:38:00', 'developer', 1),
(2, 2, 15, 15, 2, '2017-01-24 16:35:44', 'developer', '2017-01-24 16:37:35', 'developer', 1),
(3, 1, 15, 5, 3, '2017-01-24 16:49:13', 'developer', '2017-01-24 16:49:13', 'developer', 0),
(4, 2, 20, 5, 3, '2017-01-24 16:49:32', 'developer', '2017-01-24 16:49:32', 'developer', 0);

INSERT INTO modified_reading (id, reading_id, schedule_id, consumption, reading_value, creation_time, created_by_user) VALUES
(1, 2, 1, 15, 15, '2017-01-24 16:37:35', 'developer'),
(2, 1, 1, 10, 10, '2017-01-24 16:38:00', 'developer');

INSERT INTO invoice (id, account_id, reading_id, gross_charge, net_charge, remaining_total, status, schedule_id, basic, discount, dep_fund, sys_loss, arrears, penalty, others, due_date) VALUES
(1, 1, 1, NULL, '115.00', '0.00', 'FULLYPAID', 2, '100.00', '0.00', '5.00', '5.00', '0.00', '0.00', '5.00', '2016-11-10'),
(2, 2, 2, NULL, '175.00', '175.00', 'DEBT', 2, '155.00', '0.00', '7.50', '7.50', '0.00', '0.00', '5.00', '2016-11-10'),
(3, 1, 3, NULL, '60.00', '10.00', 'PARTIALLYPAID', 3, '50.00', '0.00', '2.50', '2.50', '0.00', '0.00', '5.00', '2016-12-10'),
(4, 2, 4, NULL, '235.00', '235.00', 'DEBT', 3, '50.00', '0.00', '2.50', '2.50', '175.00', '0.00', '5.00', '2016-12-10');

INSERT INTO payment (id, account_id, invoice_id, schedule_id, date, amount_paid, invoice_total, or_number, creation_time, created_by_user, modified_by_user, version, modification_time) VALUES
(10, 1, 1, 2, '2016-11-01', '115.00', '115.00', '1212121', '2017-01-24 16:46:44', 'developer', 'developer', 0, '2017-01-24 16:46:44'),
(11, 1, 3, 3, '2016-12-02', '50.00', '60.00', '4444123', '2017-01-24 16:50:48', 'developer', 'developer', 0, '2017-01-24 16:50:48');

INSERT INTO role (id, role_name, description) VALUES
(1, 'ACCOUNTS', 'Customers and Accounts'),
(2, 'READINGS', 'Meter Readings, Expenses, Discount'),
(3, 'REPORTS', 'Bills and Reports'),
(4, 'PAYMENTS', 'Payments'),
(5, 'SYSTEM', 'System Users');

INSERT INTO user (id, full_name, username, password, type, status, creation_time, modification_time, created_by_user, modified_by_user, version) VALUES
(1, 'Jasper Bernales', 'developer', '$2a$10$LvDVKusRSmMloHpVEC76yezPitaVReTXBRexN8KJvNUVwurvLhnVy', 'SUPERUSER', 'ACTIVE', '2016-08-01 00:00:00', '2016-10-02 13:27:30', 'developer', 'developer', 5),
(2, 'Encoder Tester', 'encoder', '$2a$10$dAI0RsSHB9RW8NB0JQ3T1OmgIOREQ.k3/Ibfh5zQNteDISPtsWarC', 'ENCODER', 'ACTIVE', '2017-01-24 17:05:09', '2017-01-24 17:05:09', 'developer', 'developer', 0),
(3, 'Treasurer Tester', 'treasurer', '$2a$10$PWBAlaZeZFjdZSu6.8Fsqe0gyy9iwOjhRI.fenD/oJVOUJ7CFDED6', 'TREASURER', 'ACTIVE', '2017-01-24 17:05:36', '2017-01-24 17:05:36', 'developer', 'developer', 0),
(4, 'Reports Tester', 'reports', '$2a$10$31rw95FvUTFSqWPnDgnCcuNtCj6zaG28CHU04kDCeD7rn9Iod4Vj2', 'REPORTS_VIEWER', 'ACTIVE', '2017-01-24 17:06:01', '2017-01-24 17:06:01', 'developer', 'developer', 0);

INSERT INTO usersandroles (user_id, role_id) VALUES
(1, 2),
(1, 5),
(1, 1),
(1, 3),
(1, 4),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(3, 3),
(3, 4),
(4, 3);

INSERT INTO settings (id, system_loss_rate, depreciation_fund_rate, pes, basic_rate, min_system_loss, min_depreciation_fund, min_basic, penalty, debts_allowed) VALUES
(1, 0.50, 0.50, 5.00, 10.00, 2.50, 2.50, 50.00, 0.10, 3);

INSERT INTO tax (id, code, description, value) VALUES
(1, 'MRT', 'Meter Reading Tax', '0.00');
