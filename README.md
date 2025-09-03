# Neighborly Software Application

![Neighborly Dashboard](https://github.com/user-attachments/assets/6fc525a4-2a41-4105-a7f0-115e987e28b1)

## Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Dependencies](#dependencies)
- [Usage Scenario](#usage-scenario)
- [Build and Deployment - Backend](#Backend-setup)
- [Build and Deployment - Frontend](#Frontend-setup)
- [Project Structure](#Project-structure)
- [API Documentation](#api-documentation)
- [Test-Driven Development](#test-driven-development)
- [Test Coverage](#test-coverage)
- [Design Principles](#Design-Principles)
- [Smell Analysis Report](#Smell-analysis-report)
- [Contribution Guidelines](#contribution-guidelines)

---

## About the Project
The Community Neighborhood Software Application is a platform designed 
to foster stronger neighborhood connections by providing essential community-driven 
features. This application enables residents to engage in local activities, 
request assistance, manage parking rentals, and book public spaces efficiently.

---

## Features

### Help Requests
- Community members can request and offer help for various tasks.
- Categories include emergency assistance, household help, and more. 
- Residents can handle request operations including viewing, editing, and deletion.

### Parking Rentals
- Residents can rent out their available parking spaces.
- A booking system ensures smooth transactions between users. 
- Residents can easily track and review their complete rental history.

### Booking Public Places
- Residents can conveniently track and reserve open spaces via a time-slot-enabled calendar view.
- Community spaces include halls, parks, and other public areas.
- A community manager oversees space availability and handles approval processes.
- The system supports event planning and resolves booking conflicts.
- Email alerts notify residents about the approval or denial of public space requests.
- Residents can view available events and choose to participate.

### System Roles
- **Admin**: Manages the overall platform, neighborhoods, and approvals.
- **Community Manager**: Oversees a specific neighborhood, approves member requests, and ensures smooth operations.
- **Resident Member**: Can request help, rent parking spaces, book public areas, and interact with other members.

---

## Tech Stack
- **Frontend**: React (Node.js-based)
- **Backend**: Spring Boot (Java)
- **Database**: MySQL (with Hibernate for ORM)
- **Authentication**: Spring Security with JWT
- **Containerization**: Docker (for deployment and microservices management)
- **Project Management**: GitLab

---

## Dependencies

### Frontend Dependencies
- **@testing-library/jest-dom ^5.17.0**
- **@testing-library/react ^13.4.0**
- **@testing-library/user-event ^13.5.0**
- **axios ^1.7.9**
- **cra-template 1.2.0**
- **formik ^2.4.6**
- **framer-motion ^12.5.0**
- **jwt-decode ^4.0.0**
- **lucide-react ^0.474.0**
- **react ^18.3.1**
- **react-calendar ^5.1.0**
- **react-dom ^18.3.1**
- **react-router-dom ^7.3.0**
- **react-scripts ^5.0.1**
- **web-vitals ^2.1.4**
- **yup ^1.6.1**

### Backend Dependencies
- **json 20210307**
- **spring-boot-starter-actuator**
- **spring-boot-starter-web**
- **spring-boot-starter-security**
- **spring-boot-starter-data-jpa**
- **mysql-connector-j**
- **h2**
- **jjwt-api 0.11.5**
- **jjwt-impl 0.11.5**
- **jjwt-jackson 0.11.5**
- **lombok**
- **hibernate-validator**
- **spring-boot-starter-mail**
- **spring-boot-starter-test**
- **junit-jupiter-engine**
- **mockito-core**
- **mockito-junit-jupiter**
- **junit-platform-launcher**

### Required Tools and Libraries
- **Java JDK 21**
- **Maven** (Build automation)
- **MySQL** (Database Management)
- **Spring Boot Framework**
- **Git** (Version control)

### Maven Dependencies
All required dependencies are listed in the `pom.xml` file.

To install all dependencies:
  ```sh
     mvn clean install
  ```

---

## Usage Scenario

## 1. User Authentication

- ### Registration
Users are directed to the registration page where they can enter 
details such as full name, email ID and password.<br>
**Note:** An admin do not need to register, as his/her details are already present in the backend, allowing easy login directly.

- ### Login
Once the user registration is complete by verifying through otp, they can log
in to the platform. After logging in, they can create or join a community, 
with their name, address, pincode and other details.<br>
**Note:** Admins can log in directly without needing approval.

- ### Forgot Password
If a user forgets their password, they can reset it by clicking on 'Forgot Password.' 
A reset link will be sent to their registered email, allowing them to set a new password.<br>
**Note:** Admins can reset their passwords similarly.

## 2. Create or Join Community

- ### Create Community
A user can register to create a community by providing the name, address, pincode and other required details as long as it does not already exist on the platform.
After registration, the admin must approve the request. Once approved, the user who created the community becomes its Community Manager.<br> 
**Note:** The address and pincode must be entered correctly.

- ### Join Community
Users can request to join an existing community by providing the community name, address, pincode, and other required details.
Once the request is submitted, it must be approved by the Community Manager of that community. Upon approval, the user is granted Resident status.<br>
**Note:** The address and pincode must be entered correctly.

## 3. Help Requests
Residents can create help requests, which must be approved by the Community Manager before they become visible.
Residents also have the ability to edit or delete their own requests.<br>
**Note:** Only the Resident who created the request can edit or delete it.

## 4. Parking Rentals
Residents can list their available parking spaces, and other residents can browse and search for parking using filters.
Residents may reserve any available parking space, and all booking details are recorded and viewable in the booking history.<br>
**Note:** Only the Resident who owns the parking space can edit or delete the listing.

## 5. Booking Public Spaces
Residents can view available spaces with time slots in the calendar and book events accordingly.
The Community Manager is responsible for approving requests and resolving booking conflicts. Once a booking is approved or denied, the Resident will receive a notification via their registered email.
After approval, the event will be displayed on the Resident Dashboard. Residents can also edit or delete their event bookings.<br>
**Note:** Only one slot can be booked per day.

## 6. Personalized Dashboard
Admins, Community Managers, and Residents each have personalized dashboards.<br>
Admins and Community Managers have a notification bar for managing approvals and denials.

---

## Build and Deployment 
* Clone the repository:
   ```sh
   git clone https://git.cs.dal.ca/courses/2025-winter/csci-5308/group10
   ```
### Backend Setup
1. Navigate to the backend directory:
   ```sh
   cd backend
   ```
2. Setup the environment:
   navigate to the resources folder.
   ```sh
   cd src/main/resources
   ```
   
  Configure `application.yml` with database details.

3. Configure MySQL:
- Install MySQL locally.
- Create a database named neighbourly.
- Update env.properties with database credentials.

4. Build and run the Spring Boot application:
   ```sh
   mvn spring-boot:run
   ```

### Frontend Setup
1. Navigate to the frontend directory:
   ```sh
   cd Frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the development server:
   ```sh
   npm start
   ```

The application will be accessible at:<br>
[Neighborly Application](http://172.17.1.194:3000)

## Project Structure
```sh
  group10/
  ├── Frontend/ # React frontend application
  ├── Backend[Neighborly]/ # Spring Boot backend application
  ├── .gitlab-ci.yml # CI/CD setup
  └── README.md # Project documentation
```
---

## API Documentation
 API documentation is currently being developed and will be available in future updates.

---

## Test-Driven Development
- We followed Test-Driven Development (TDD) in our project.
- We wrote tests iteratively as part of the development 
  process before pushing them as a single commit.
- All the test cases can be found in the test folder of the backend directory.

---

## Test Coverage
- In our backend, we have focused on ensuring strong test coverage, 
particularly for the service files.
- The test suite includes comprehensive unit tests for various service 
classes, verifying their functionality and ensuring that the business 
logic is properly implemented. 
- The service files cover critical features
such as booking spaces, help-requests, user notification, parking rentals,
verification. 
- After running the tests, we achieved 78% line coverage:
![image](https://github.com/user-attachments/assets/dfe0a480-9cc7-4433-af66-ea1984d9b565)

--- 

## Design Principles
- This project follows the SOLID principles of object-oriented design 
to ensure clean, maintainable, and scalable code:

- Single Responsibility Principle – Each class has a single, well-defined responsibility<br>
Example: Separate services for business logic and controllers for handling API requests.

- Open/Closed Principle – The code is open for extension but closed for modification<br>
Example: Strategy patterns for role-based actions or notifications.

- Liskov Substitution Principle – Subtypes can be substituted without breaking the functionality<br>
Example: Interfaces for user roles and permissions.

- Interface Segregation Principle – Interfaces are kept specific and minimal<br> 
Example: Services implement only the required methods.

- Dependency Inversion Principle – High-level modules do not depend on low-level modules; both depend on abstractions<br> 
Example: Service layer depends on repository interfaces.

---

## Smell Analysis Report
link

--- 

##  Contribution Guidelines
We welcome contributions! Please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature:
   ```sh
   git checkout -b <feature/feature-name> 
   ```
3. Commit your changes:
- Stage your changes:
  ```sh
  git add . 
  ```
- Commit your changes:
  ```sh
  git commit -m "Add/Fix:[brief description of changes]"
  ```
4. Push changes to your fork:
   ```sh
   git push origin <feature/feature-name> 
   ```
5. Submit a pull request for review.

---

## Team Members
- Vinodhini Kalaichelvan
- Malav Sachinkumar Shah
- Prosper Nkasiobi Collins

## License
This project is licensed under the MIT License.

---

## Acknowledgments

Special thanks to:
- Dalhousie University
- CSCI 5308 Course Staff

---

##  Future Enhancements
- **Real-time Chat** for neighborhood discussions.
- **AI-based Recommendation System** for suggesting relevant community activities.
- **Mobile App Version** for better accessibility.

