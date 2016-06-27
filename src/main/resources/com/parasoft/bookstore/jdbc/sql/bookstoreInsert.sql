Insert into publisher(id, name) values('1', 'Prentice Hall');
Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('1', '0130384747', 'C++ How to Program (4th Edition)', 'Education', 1, '2002-08-12', 20, 99.99, 'One of the best C++ books');
Insert into author(id, isbn, name) values(1, '0130384747', 'Harvey M. Deitel');
Insert into author(id, isbn, name) values(2, '0130384747', 'Paul J. Deitel');

Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('2', '0130341517', 'Java How to Program (4th Edition)', 'Education', 1, '2001-08-08', 10, 76.00, 'Great for Java beginners');
Insert into author(id, isbn, name) values(10, '0130341517', 'Harvey M. Deitel');
Insert into author(id, isbn, name) values(11, '0130341517', 'Paul J. Deitel');

Insert into publisher(id, name) values('2', 'O''Reilly & Associates');
Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('3', '0596002831', 'Java in a Nutshell, Fourth Edition', 'Education', 2, '2002-03-01', 5, 27.97, 'contains an accelerated introduction to the Java programming language and its key APIs so you can start writing code right away. Covers Java 1.4');
Insert into author(id, isbn, name) values(3, '0596002831', 'David Flanagan');

Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('4', '0130084662', 'Linux Administration Handbook', 'Computer Science', 1, '2002-03-25', 3, 49.99, 'Provides techniques and advice for administering three representative distributions Linux: Red Hat 7.2, SuSE 7.3, and Debian 3.0');
Insert into author(id, isbn, name) values(4, '0130084662', 'Evi Nemeth');
Insert into author(id, isbn, name) values(5, '0130084662', 'Garth Snyder');
Insert into author(id, isbn, name) values(6, '0130084662', 'Trent R. Hein');
Insert into author(id, isbn, name) values(7, '0130084662', 'Adam Boggs');

Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('5', '0596003811', 'Oracle PL/SQL Programming, 3rd Edition', 'Computer Science', 2, '2002-09-01', 4, 38.47, 'An indispensable reference for both novice and experienced PL/SQL developers');
Insert into author(id, isbn, name) values(8, '0596003811', 'Steven Feuerstein');
Insert into author(id, isbn, name) values(9, '0596003811', 'Bill Pribyl');

Insert into publisher(id, name) values('3', 'Sams');
Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('6', '0672317826', 'PowerBuilder 7.0 Unleashed', 'Informatics', 3, '1999-12-09', 0, 34.99, 'excellent one-volume reference to new features in Sybase''s well-known programming tool, with good coverage of its support for distributed and Internet-based computing');
Insert into author(id, isbn, name) values(12, '0672317826', 'Chetney Heiber');

Insert into publisher(id, name) values('4', 'Wiley');
Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('7', '9780470398111', 'The Next Leap in Productivity: What Top Managers Really Need to Know about Information Technology', 'Business Technology', 4, '2009-02-01', 20, 29.99, 'How much should top management really care about IT? That''s the question Adam Kolawa bluntly poses in this feisty and compelling book going far beyond traditional business books written for the CIO community.');
Insert into author(id, isbn, name) values(13, '9780470398111', 'Adam Kolawa');

Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('8', '0470042125', 'Automated Defect Prevention: Best Practices in Software Management', 'Software Management', 4, '2007-09-01', 12, 101.50, 'This book describes an approach to software management based on establishing an infrastructure that serves as the foundation for the project.');
Insert into author(id, isbn, name) values(14, '0470042125', 'Adam Kolawa');
Insert into author(id, isbn, name) values(15, '0470042125', 'Dorota Huizinga');

Insert into book(id, isbn, title, genre, publisher_id, year, stock, price, description) values ('9', '0764548662', 'Bulletproofing Web Applications', 'Web', 4, '2001-06-01', 3, 13.24, 'A road map for how to integrate error prevention and detection into the development process to ensure that Web applications are robust, scalable, efficient and reliable.');
Insert into author(id, isbn, name) values(16, '0764548662', 'Adam Kolawa');
Insert into author(id, isbn, name) values(17, '0764548662', 'Wendell Hicken');
Insert into author(id, isbn, name) values(18, '0764548662', 'Cynthia Dunlop');