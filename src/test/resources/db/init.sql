DROP TABLE settings IF EXISTS;
DROP TABLE tax IF EXISTS;
DROP TABLE usersandroles IF EXISTS;
DROP TABLE user IF EXISTS;
DROP TABLE role IF EXISTS;
DROP TABLE modified_expense IF EXISTS;
DROP TABLE modified_reading IF EXISTS;
DROP TABLE payment IF EXISTS;
DROP TABLE invoice IF EXISTS;
DROP TABLE meter_reading IF EXISTS;
DROP TABLE expense IF EXISTS;
DROP TABLE schedule IF EXISTS;
DROP TABLE device IF EXISTS;
DROP TABLE account IF EXISTS;
DROP TABLE address IF EXISTS;
DROP TABLE customer IF EXISTS;


CREATE TABLE account (
  id INTEGER IDENTITY PRIMARY KEY,
  number VARCHAR(8) NOT NULL,
  address_id INTEGER NOT NULL,
  purok TINYINT NOT NULL,
  status VARCHAR(45) NOT NULL,
  customer_id INTEGER NOT NULL,
  account_type VARCHAR(45) DEFAULT NULL,
  account_standing_balance DECIMAL(9,2) DEFAULT 0.00 NOT NULL ,
  status_updated BIT NOT NULL
);
CREATE UNIQUE INDEX account_number ON account(number);
CREATE INDEX customer_idx ON account(customer_id);
CREATE INDEX accountAddress_idx ON account(address_id);

CREATE TABLE address (
  id INTEGER IDENTITY PRIMARY KEY,
  brgy VARCHAR(45) NOT NULL,
  location_code INTEGER NOT NULL,
  account_prefix VARCHAR(2) NOT NULL,
  accounts_count INTEGER NOT NULL,
  due_day INTEGER NOT NULL
);

CREATE TABLE customer (
  id INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(45) NOT NULL,
  lastname VARCHAR(100) NOT NULL,
  middle_name VARCHAR(45) NOT NULL,
  gender CHAR(1) NOT NULL,
  contact_number VARCHAR(50) DEFAULT NULL,
  family_members_count INTEGER NOT NULL,
  occupation VARCHAR(45) NOT NULL
);

CREATE TABLE device (
  id INTEGER IDENTITY PRIMARY KEY,
  owner_id INTEGER NOT NULL,
  meter_code VARCHAR(45) NOT NULL,
  brand VARCHAR(45) NOT NULL,
  active BIT NOT NULL,
  last_reading INTEGER NOT NULL,
  start_date DATE DEFAULT NULL,
  end_date DATE DEFAULT NULL
);
CREATE INDEX owner_idx ON device(owner_id);

CREATE TABLE expense (
  id INTEGER IDENTITY PRIMARY KEY,
  type TINYINT NOT NULL,
  schedule_id INTEGER NOT NULL,
  amount DECIMAL(9,2) NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  created_by_user varchar(45) NOT NULL,
  modified_by_user varchar(45) NOT NULL,
  modification_time TIMESTAMP NOT NULL,
  version SMALLINT NOT NULL,
);
CREATE INDEX schedule_idx ON expense(schedule_id);

CREATE TABLE invoice (
  id INTEGER IDENTITY PRIMARY KEY,
  account_id INTEGER NOT NULL,
  reading_id INTEGER NOT NULL,
  gross_charge DECIMAL(9,2) DEFAULT NULL,
  net_charge DECIMAL(9,2) NOT NULL,
  remaining_total DECIMAL(9,2) NOT NULL,
  status VARCHAR(16) NOT NULL,
  schedule_id INTEGER NOT NULL,
  basic DECIMAL(9,2) NOT NULL,
  discount DECIMAL(9,2) NOT NULL,
  dep_fund DECIMAL(9,2) NOT NULL,
  sys_loss DECIMAL(9,2) NOT NULL,
  arrears DECIMAL(9,2) NOT NULL,
  penalty DECIMAL(9,2) NOT NULL,
  others DECIMAL(9,2) NOT NULL,
  due_date DATE NOT NULL
);
CREATE INDEX account_idx ON invoice(account_id);
CREATE INDEX invoice_schedule_idx ON invoice(schedule_id);
CREATE INDEX reading_idx ON invoice(reading_id);

CREATE TABLE meter_reading (
  id INTEGER IDENTITY PRIMARY KEY,
  account_id INTEGER NOT NULL,
  reading_value INTEGER NOT NULL,
  consumption INTEGER NOT NULL,
  schedule_id BIGINT NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  created_by_user VARCHAR(45) NOT NULL,
  modification_time TIMESTAMP NOT NULL,
  modified_by_user VARCHAR(45) NOT NULL,
  version BIGINT NOT NULL
);
CREATE INDEX account_readingx ON meter_reading(account_id);
CREATE INDEX reading_schedulex ON meter_reading(schedule_id);

CREATE TABLE modified_expense (
  id INTEGER IDENTITY PRIMARY KEY,
  expense_id INTEGER NOT NULL,
  schedule_id INTEGER NOT NULL,
  type TINYINT NOT NULL,
  amount DECIMAL(9,2) NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  created_by_user VARCHAR(45) NOT NULL
);
CREATE INDEX modexpense_idx ON modified_expense(expense_id);
CREATE INDEX modexpense_schedulex ON modified_expense(schedule_id);

CREATE TABLE modified_reading (
  id INTEGER IDENTITY PRIMARY KEY,
  reading_id INTEGER NOT NULL,
  schedule_id INTEGER NOT NULL,
  consumption SMALLINT NOT NULL,
  reading_value SMALLINT NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  created_by_user VARCHAR(45) NOT NULL
);
CREATE INDEX reading_orig_idx ON modified_reading(reading_id);
CREATE INDEX reading_orig_schedule_idx ON modified_reading(schedule_id);

CREATE TABLE payment (
  id INTEGER IDENTITY PRIMARY KEY,
  account_id INTEGER NOT NULL,
  invoice_id INTEGER NOT NULL,
  schedule_id INTEGER NOT NULL,
  date DATE NOT NULL,
  amount_paid DECIMAL(9,2) NOT NULL,
  invoice_total DECIMAL(9,2) NOT NULL,
  or_number VARCHAR(20) NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  created_by_user VARCHAR(45) NOT NULL,
  modified_by_user VARCHAR(45) NOT NULL,
  version SMALLINT NOT NULL,
  modification_time TIMESTAMP NOT NULL
);
CREATE INDEX account_paymentx ON payment(account_id);
CREATE INDEX invoice_paymentx ON payment(invoice_id);
CREATE INDEX payment_schedulex ON payment(schedule_id);

CREATE TABLE role (
  id INTEGER IDENTITY PRIMARY KEY,
  role_name VARCHAR(255) NOT NULL,
  description VARCHAR(45) NOT NULL
);

CREATE TABLE schedule (
  id INTEGER IDENTITY PRIMARY KEY,
  month SMALLINT NOT NULL,
  year SMALLINT NOT NULL
);

CREATE TABLE settings (
  id TINYINT IDENTITY PRIMARY KEY,
  system_loss_rate DECIMAL(5,2) NOT NULL,
  depreciation_fund_rate DECIMAL(5,2) NOT NULL,
  pes DECIMAL(5,2) NOT NULL,
  basic_rate DECIMAL(5,2) NOT NULL,
  min_system_loss DECIMAL(5,2) NOT NULL,
  min_depreciation_fund DECIMAL(5,2) NOT NULL,
  min_basic DECIMAL(5,2) NOT NULL,
  penalty DECIMAL(5,2) NOT NULL,
  debts_allowed TINYINT NOT NULL
);

CREATE TABLE tax (
  id TINYINT IDENTITY PRIMARY KEY,
  code VARCHAR(45) NOT NULL,
  description VARCHAR(255) NOT NULL,
  value DECIMAL(9,2) NOT NULL
);

CREATE TABLE user (
  id SMALLINT IDENTITY PRIMARY KEY,
  full_name VARCHAR(45) NOT NULL,
  username VARCHAR(16) NOT NULL,
  password VARCHAR(100) NOT NULL,
  type VARCHAR(45) NOT NULL,
  status VARCHAR(45) NOT NULL,
  creation_time TIMESTAMP NOT NULL,
  modification_time TIMESTAMP NOT NULL,
  created_by_user VARCHAR(45) NOT NULL,
  modified_by_user VARCHAR(45) NOT NULL,
  version SMALLINT NOT NULL
);

CREATE TABLE usersandroles (
  user_id SMALLINT NOT NULL,
  role_id SMALLINT NOT NULL
);
CREATE INDEX user_idx ON usersandroles(user_id);
CREATE INDEX role_idx ON usersandroles(role_id);


ALTER TABLE account ADD CONSTRAINT accountAddress FOREIGN KEY (address_id) REFERENCES address (id) ON UPDATE CASCADE;
ALTER TABLE account ADD CONSTRAINT customer FOREIGN KEY (customer_id) REFERENCES customer (id) ON UPDATE CASCADE;

ALTER TABLE device ADD CONSTRAINT owner FOREIGN KEY (owner_id) REFERENCES account (id) ON UPDATE CASCADE;

ALTER TABLE expense ADD CONSTRAINT expense_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (id) ON UPDATE CASCADE;

ALTER TABLE invoice ADD CONSTRAINT account_invoice FOREIGN KEY (account_id) REFERENCES account (id) ON UPDATE CASCADE;
ALTER TABLE invoice ADD CONSTRAINT invoice_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (id) ON UPDATE CASCADE;
ALTER TABLE invoice ADD CONSTRAINT reading_invoice FOREIGN KEY (reading_id) REFERENCES meter_reading (id) ON UPDATE CASCADE;

ALTER TABLE meter_reading ADD CONSTRAINT account_meter_reading FOREIGN KEY (account_id) REFERENCES account (id) ON UPDATE CASCADE;
ALTER TABLE meter_reading ADD CONSTRAINT reading_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (id) ON UPDATE CASCADE;

ALTER TABLE modified_expense ADD CONSTRAINT modifiedExpenseSched FOREIGN KEY (schedule_id) REFERENCES schedule (id) ON UPDATE CASCADE;
ALTER TABLE modified_expense ADD CONSTRAINT originalExpense FOREIGN KEY (expense_id) REFERENCES expense (id) ON UPDATE CASCADE;

ALTER TABLE modified_reading ADD CONSTRAINT reading_original FOREIGN KEY (reading_id) REFERENCES meter_reading (id) ON UPDATE CASCADE;
ALTER TABLE modified_reading ADD CONSTRAINT reading_original_shcedule FOREIGN KEY (schedule_id) REFERENCES schedule (id) ON UPDATE CASCADE;

ALTER TABLE payment ADD CONSTRAINT account_payment FOREIGN KEY (account_id) REFERENCES account (id) ON UPDATE CASCADE;
ALTER TABLE payment ADD CONSTRAINT invoice_payment FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON UPDATE CASCADE;
ALTER TABLE payment ADD CONSTRAINT payment_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (id) ON UPDATE CASCADE;

ALTER TABLE usersandroles ADD CONSTRAINT role FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE CASCADE;
ALTER TABLE usersandroles ADD CONSTRAINT user FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE CASCADE;