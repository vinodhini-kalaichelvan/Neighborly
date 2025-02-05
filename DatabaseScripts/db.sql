CREATE DATABASE neighbourly;
USE neighbourly;



CREATE TABLE resident (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- Stores the hashed password
);


-