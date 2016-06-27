INSERT INTO Customer (id, first_name, last_name, address, city, state, zip_code, phone_number, ssn, username, password) VALUES (12212, 'John', 'Smith', '1431 Main St', 'Beverly Hills', 'CA', '90210', '310-447-4121', '622-11-9999', 'john', 'demo');
INSERT INTO Customer (id, first_name, last_name, address, city, state, zip_code, phone_number, ssn, username, password) VALUES (12323, 'Bob', 'Parasoft', '101 E Huntington Dr', 'Monrovia', 'CA', '91016', '626-256-3680', '123-45-6789', 'parasoft', 'demo');

INSERT INTO Account (id, customer_id, type, balance) VALUES (12345, 12212, 0, -2300.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (12456, 12212, 0,    10.45);
INSERT INTO Account (id, customer_id, type, balance) VALUES (12567, 12212, 0,   100.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (12678, 12212, 1,  -100.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (12789, 12212, 0,   100.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (12900, 12212, 0,     0.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (13011, 12212, 0,   100.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (13122, 12212, 0,  1100.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (13233, 12212, 0,   100.00);
INSERT INTO Account (id, customer_id, type, balance) VALUES (13344, 12212, 1,  1231.10);
INSERT INTO Account (id, customer_id, type, balance) VALUES (54321, 12212, 0,  1351.12);
INSERT INTO Account (id, customer_id, type, balance) VALUES (13455, 12323, 0,  2014.76);

INSERT INTO Positions (position_id, customer_id, name, symbol, shares, purchase_price) VALUES (12345, 12212, 'AMR Corporation', 'AAR', 20, '23.53');
INSERT INTO Positions (position_id, customer_id, name, symbol, shares, purchase_price) VALUES (12346, 12212, 'General Cable Corporation', 'BGC', 10, '44.68');
INSERT INTO Positions (position_id, customer_id, name, symbol, shares, purchase_price) VALUES (12347, 12212, 'CIGNA Corporation', 'CI', 45, '42.95');

INSERT INTO News (id, date, headline, story) VALUES (1, '2010-01-01', 'Happy New Year', 'Happy New Year from all of us at ParaBank.');
INSERT INTO News (id, date, headline, story) VALUES (2, '2010-08-20', 'ParaBank 2.0 Coming Soon', 'Our website has been serving our customers for over five years, but it is now time to look ahead to the next generation of online banking. Unfortunately our upgrade means that online banking will temporarily offline.  We apologize for any inconvenience.');
INSERT INTO News (id, date, headline, story) VALUES (3, '2010-08-27', 'ParaBank Is Offline', 'As part of our ongoing commitment to quality for our customers, ParaBank will be offline for the next few weeks as we upgrade our systems. Look for the enhanced "ParaBank 2.0" to be available soon.  Again, we apologize for any inconvenience.');
INSERT INTO News (id, date, headline, story) VALUES (4, '2010-09-13', 'New! Online Account Transfers', 'ParaBank is excited to announce our new online account transfer feature. You can now use our award-winning website to transfer money between your accounts.');
INSERT INTO News (id, date, headline, story) VALUES (5, '2010-09-13', 'New! Online Bill Pay', 'ParaBank is excited to announce our new online bill pay feature. Utility payments may now be paid easily through our automated system.');
INSERT INTO News (id, date, headline, story) VALUES (6, '2010-09-13', 'ParaBank Is Now Re-Open', 'After extensive internal upgrades, ParaBank is ready to announce that their award-winning website is available again. Look for "ParaBank 2.0" to be continuously upgraded with all of the latest features you would expect from the leading online demo bank.');

INSERT INTO Parameter (name, value) VALUES ('endpoint', '');
INSERT INTO Parameter (name, value) VALUES ('initialBalance', '515.50');
INSERT INTO Parameter (name, value) VALUES ('minimumBalance', '100.00');
INSERT INTO Parameter (name, value) VALUES ('loanProvider', 'ws');
INSERT INTO Parameter (name, value) VALUES ('loanProcessor', 'funds');
INSERT INTO Parameter (name, value) VALUES ('loanProcessorThreshold', '20');
INSERT INTO Parameter (name, value) VALUES ('soap_endpoint', '');
INSERT INTO Parameter (name, value) VALUES ('rest_endpoint', '');
INSERT INTO Parameter (name, value) VALUES ('accessmode', 'jdbc');

INSERT INTO Sequence (name, next_id) VALUES ('Customer', 12434);
INSERT INTO Sequence (name, next_id) VALUES ('Account', 13566);
INSERT INTO Sequence (name, next_id) VALUES ('Transaction', 14476);
INSERT INTO Sequence (name, next_id) VALUES ('Position', 13017);
INSERT INTO Sequence (name, next_id) VALUES ('Stock', 111);

INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12145, 12345, 0, '2009-12-11',  300.00, 'Check # 1111');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12256, 12345, 1, '2009-12-12',  100.00, 'Check # 1211');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12367, 12678, 1, '2010-07-19',  100.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12478, 12789, 0, '2010-07-19',  100.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12589, 12345, 1, '2010-08-19', 1000.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12700, 54321, 0, '2010-08-19', 1000.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12811, 54321, 1, '2010-08-19',  100.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (12922, 12900, 0, '2010-08-19',  100.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13033, 12900, 1, '2010-08-19',  100.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13144, 13011, 0, '2010-08-19',  100.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13255, 12345, 1, '2010-08-23',  100.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13366, 13122, 0, '2010-08-23',  100.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13477, 12345, 1, '2010-08-23', 1000.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13588, 13122, 0, '2010-08-23', 1000.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13699, 12345, 1, '2010-08-24',    0.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13810, 13122, 1, '2010-08-24',    0.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (13921, 12678, 1, '2010-08-26',  100.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (14032, 13233, 1, '2010-08-26',  100.00, 'Funds Transfer Received');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (14143, 12345, 1, '2010-08-26', 1000.00, 'Bill Payment to Bank of America Visa');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (14254, 13344, 1, '2010-08-31', 1000.00, 'Funds Transfer Sent');
INSERT INTO Transaction (id, account_id, type, date, amount, description) VALUES (14365, 13344, 1, '2010-08-31', 1000.00, 'Funds Transfer Received');

INSERT INTO Company (symbol, name) VALUES ('AAR', 'AMR Corporation');
INSERT INTO Company (symbol, name) VALUES ('BGC', 'General Cable Corporation');
INSERT INTO Company (symbol, name) VALUES ('CI', 'CIGNA Corporation');