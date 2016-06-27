DROP TABLE IF EXISTS Author;
DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Publisher;

CREATE TABLE Publisher (
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);
  
CREATE TABLE Book (
  id INTEGER NOT NULL PRIMARY KEY,
  isbn VARCHAR(50) NOT NULL,
  title VARCHAR(300) NOT NULL,
  genre VARCHAR(50) NOT NULL,
  publisher_id INTEGER NOT NULL,
  year DATE NOT NULL,
  stock integer NOT NULL,
  price DECIMAL(8,2) NOT NULL,
  description VARCHAR(300) NOT NULL,
  
  FOREIGN KEY (publisher_id) REFERENCES publisher(id),
  CONSTRAINT c_price CHECK(price >= 0),
  CONSTRAINT c_stock CHECK(stock >= 0),
  UNIQUE(isbn)
);

CREATE TABLE Author (
  id INTEGER NOT NULL PRIMARY KEY,
  isbn VARCHAR(50),
  name VARCHAR(100),
  
  FOREIGN KEY (isbn) REFERENCES book (isbn)
);