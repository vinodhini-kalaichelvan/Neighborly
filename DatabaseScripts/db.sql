-- User Table (Stores all users: Regular Users, Residents, Community Managers, and Admins)
CREATE TABLE user (
id INT AUTO
_
INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
email VARCHAR(255) UNIQUE NOT NULL,
password VARCHAR(255) NOT NULL,
-- Stores the hashed password
contact VARCHAR(255),
neighbourhood
_
id INT DEFAULT NULL,
-- NULL if user hasn’t joined a community
address VARCHAR(255) DEFAULT NULL,
-- Only for residents and community managers
user
_
type ENUM('USER'
,
'RESIDENT'
,
'COMMUNITY
_
MANAGER'
,
'ADMIN') NOT NULL,
--
Defines user role
FOREIGN KEY (neighbourhood
_
id) REFERENCES neighbourhood(id) ON DELETE SET
NULL
);
-- Neighbourhood Table
CREATE TABLE neighbourhood (
id INT AUTO
_
INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
location VARCHAR(255),
admin
_
id INT,
FOREIGN KEY (admin
_
id) REFERENCES user(id) ON DELETE SET NULL
);
-- Post Table (Linked to the unified user table)
CREATE TABLE post (
post
_
id INT AUTO
_
INCREMENT PRIMARY KEY,
user
_
id INT,
-- References user table
neighbourhood
_
id INT,
post
_
type VARCHAR(255),
post
_
content TEXT,
date
_
time DATETIME,
approved BOOLEAN,
FOREIGN KEY (user
_
id) REFERENCES user(id) ON DELETE CASCADE,
FOREIGN KEY (neighbourhood
_
id) REFERENCES neighbourhood(id) ON DELETE
CASCADE
);
-- Amenities Table
CREATE TABLE amenities (
amenity_
id INT AUTO
_
INCREMENT PRIMARY KEY,
neighbourhood
_
id INT,
name VARCHAR(255),
available
_
time
_
slots TEXT,
FOREIGN KEY (neighbourhood
_
id) REFERENCES neighbourhood(id) ON DELETE
CASCADE
);
-- Parking Rentals Table (Linked to the unified user table)
CREATE TABLE parking_
rentals (
rental
_
id INT AUTO
_
INCREMENT PRIMARY KEY,
neighbourhood
_
id INT,
user
_
id INT,
-- Changed resident
_
id to user
_
id
spot
_
number VARCHAR(255),
start
_
time DATETIME,
end
_
time DATETIME,
price DECIMAL(10, 2),
status ENUM('Booked'
,
'Available'
,
'Cancelled'),
FOREIGN KEY (neighbourhood
_
id) REFERENCES neighbourhood(id) ON DELETE
CASCADE,
FOREIGN KEY (user
_
id) REFERENCES user(id) ON DELETE CASCADE
);
-- Public Place Bookings Table (Linked to the unified user table)
CREATE TABLE public
_place
_
bookings (
booking_
id INT AUTO
_
INCREMENT PRIMARY KEY,
neighbourhood
_
id INT,
user
_
id INT,
-- Changed resident
_
id to user
_
id
place
_
name VARCHAR(255),
date DATE,
time
_
slot VARCHAR(255),
status ENUM('Pending'
,
'Approved'
,
'Rejected'),
FOREIGN KEY (neighbourhood
_
id) REFERENCES neighbourhood(id) ON DELETE
CASCADE,
FOREIGN KEY (user
_
id) REFERENCES user(id) ON DELETE CASCADE
);
-- Help Requests Table (Linked to the unified user table)
CREATE TABLE help_
requests (
request
_
id INT AUTO
_
INCREMENT PRIMARY KEY,
user
_
id INT,
-- Changed resident
_
id to user
_
id
neighbourhood
_
id INT,
request
_
type VARCHAR(255),
description TEXT,
status ENUM('Open'
,
'In Progress'
,
'Resolved'),
created
_
at DATETIME,
updated
_
at DATETIME,
FOREIGN KEY (user
_
id) REFERENCES user(id) ON DELETE CASCADE,
FOREIGN KEY (neighbourhood
_
id) REFERENCES neighbourhood(id) ON DELETE
CASCADE
);