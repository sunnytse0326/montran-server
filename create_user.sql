create database montran;
CREATE USER 'montran'@'localhost' IDENTIFIED BY 'montran';
grant all on montran.* to 'montran'@'localhost';

use montran;

CREATE TABLE IF NOT EXISTS User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL NOT NULL,
    create_date DATE
);

INSERT INTO User VALUES (1,'Test','User','hash12345678',0,CURDATE())
