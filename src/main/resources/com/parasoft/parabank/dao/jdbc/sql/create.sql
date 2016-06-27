-- Drop tables used to enforce referential integrity in the reverse order that
-- they are created.  That is, a table with a foreign key constraint must be
-- dropped before the table that the foreign key references.

-- HyperSQL and MySQL both support "DROP TABLE name CASCADE", which would
-- avoid the need to order the DROP TABLE statements, but in the current MySQL
-- (5.x) the CASCADE keyword does nothing for a DROP TABLE statement.

DROP TABLE IF EXISTS Stock;
DROP TABLE IF EXISTS Company;
DROP TABLE IF EXISTS Positions;
DROP TABLE IF EXISTS Transaction;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS Customer;

CREATE TABLE Customer (
  id INTEGER NOT NULL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  address VARCHAR(45) NOT NULL,
  city VARCHAR(20) NOT NULL,
  state VARCHAR(20) NOT NULL,
  zip_code VARCHAR(20) NOT NULL,
  phone_number VARCHAR(20) NOT NULL,
  ssn VARCHAR(15) NOT NULL,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  UNIQUE (username)
);

CREATE TABLE Account (
  id INTEGER NOT NULL PRIMARY KEY,
  customer_id INTEGER NOT NULL,
  type INTEGER NOT NULL,
  balance DECIMAL(19,4),
  
  FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

DROP TABLE IF EXISTS News;

CREATE TABLE News (
  id INTEGER NOT NULL PRIMARY KEY,
  date DATE,
  headline VARCHAR(50),
  story VARCHAR(255)
);

DROP TABLE IF EXISTS Parameter;

CREATE TABLE Parameter (
  name VARCHAR(50) NOT NULL,
  value VARCHAR(255)
);

DROP TABLE IF EXISTS Sequence;

CREATE TABLE Sequence ( name VARCHAR(15), next_id INTEGER );

CREATE TABLE Transaction (
  id INTEGER NOT NULL PRIMARY KEY,
  account_id INTEGER NOT NULL,
  type INTEGER NOT NULL,
  date DATE,
  amount DECIMAL(19,4),
  description VARCHAR(255),
  
  FOREIGN KEY (account_id) REFERENCES Account(id)
);

CREATE TABLE Positions (
  position_id INTEGER NOT NULL PRIMARY KEY,
  customer_id INTEGER NOT NULL,
  name VARCHAR(255) NOT NULL,
  symbol VARCHAR(10) NOT NULL,
  shares INTEGER NOT NULL,
  purchase_price DECIMAL(19,4) NOT NULL,
  
  FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

CREATE TABLE Company (
  symbol VARCHAR(10) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE Stock (
  id INTEGER NOT NULL PRIMARY KEY,
  symbol VARCHAR(10) NOT NULL,
  date DATE,
  closing_price DECIMAL(19,4),
  
  FOREIGN KEY (symbol) REFERENCES Company(symbol)
);