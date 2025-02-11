
# change this based on your DB you are using
use neighbourly;



-- User Table (Stores all users: Regular Users, Residents, Community Managers, and Admins)
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) UNIQUE NOT NULL,
                      password VARCHAR(255) NOT NULL, -- Stores the hashed password
                      contact VARCHAR(255),
                      neighbourhood_id INT DEFAULT NULL, -- NULL if user hasn’t joined a community
                      address VARCHAR(255) DEFAULT NULL, -- Only for residents and community managers
                      user_type ENUM('USER', 'RESIDENT', 'COMMUNITY_MANAGER', 'ADMIN') NOT NULL, -- Defines user role
                      FOREIGN KEY (neighbourhood_id) REFERENCES neighbourhood(id) ON DELETE SET NULL
);

-- Neighbourhood Table
CREATE TABLE neighbourhood (
                               neighbourhood_id INT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               location VARCHAR(255),
                               admin_id INT,
                               FOREIGN KEY (admin_id) REFERENCES user(id) ON DELETE SET NULL
);

-- Post Table (Linked to the unified user table)
CREATE TABLE post (
                      post_id INT AUTO_INCREMENT PRIMARY KEY,
                      user_id INT, -- References user table
                      neighbourhood_id INT,
                      post_type VARCHAR(255),
                      post_content TEXT,
                      date_time DATETIME,
                      approved BOOLEAN,
                      FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                      FOREIGN KEY (neighbourhood_id) REFERENCES neighbourhood(id) ON DELETE CASCADE
);

-- Amenities Table
CREATE TABLE amenities (
                           amenity_id INT AUTO_INCREMENT PRIMARY KEY,
                           neighbourhood_id INT,
                           name VARCHAR(255),
                           available_time_slots TEXT,
                           FOREIGN KEY (neighbourhood_id) REFERENCES neighbourhood(id) ON DELETE CASCADE
);

-- Parking Rentals Table (Linked to the unified user table)
CREATE TABLE parking_rentals (
                                 rental_id INT AUTO_INCREMENT PRIMARY KEY,
                                 neighbourhood_id INT,
                                 user_id INT, -- Changed resident_id to user_id
                                 spot_number VARCHAR(255),
                                 start_time DATETIME,
                                 end_time DATETIME,
                                 price DECIMAL(10, 2),
                                 status ENUM('Booked', 'Available', 'Cancelled'),
                                 FOREIGN KEY (neighbourhood_id) REFERENCES neighbourhood(id) ON DELETE CASCADE,
                                 FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Public Place Bookings Table (Linked to the unified user table)
CREATE TABLE public_place_bookings (
                                       booking_id INT AUTO_INCREMENT PRIMARY KEY,
                                       neighbourhood_id INT,
                                       user_id INT, -- Changed resident_id to user_id
                                       place_name VARCHAR(255),
                                       date DATE,
                                       time_slot VARCHAR(255),
                                       status ENUM('Pending', 'Approved', 'Rejected'),
                                       FOREIGN KEY (neighbourhood_id) REFERENCES neighbourhood(id) ON DELETE CASCADE,
                                       FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Help Requests Table (Linked to the unified user table)
CREATE TABLE help_requests (
                               request_id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT, -- Changed resident_id to user_id
                               neighbourhood_id INT,
                               request_type VARCHAR(255),
                               description TEXT,
                               status ENUM('Open', 'In Progress', 'Resolved'),
                               created_at DATETIME,
                               updated_at DATETIME,
                               FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                               FOREIGN KEY (neighbourhood_id) REFERENCES neighbourhood(id) ON DELETE CASCADE
);

