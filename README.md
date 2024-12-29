# SpringSecurity Project

This project is a Backend REST API developed using Spring Boot and Spring Security. It incorporates JWT-based authentication, OAuth2 login with Google, email confirmation, and password recovery functionalities.

## Purpose

The primary goal of this project is to provide a secure and robust authentication and authorization system for backend services. It demonstrates how to integrate various security features, including:

- **JWT Authentication**: Enables stateless authentication for RESTful services.
- **OAuth2 with Google**: Allows users to log in using their Google accounts.
- **Email Confirmation**: Ensures that users verify their email addresses during registration.
- **Forgotten Password**: Provides functionality for users to reset their passwords securely again with email confirmation.
- *Exception Handling*: The project incorporates comprehensive exception handling to effectively manage all types of errors that may arise during interactions with the security system.

## How to Use

### Prerequisites

Ensure you have the following installed:

- **Java 21** (I guess other versions might work as well)
- **Maven**
- **MySQL**

### Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/BelisAliosmanova/SpringSecurity.git
   cd SpringSecurity
   ```

2. **Configure the database:**

   - Create a MySQL database named `springSecurity`. (Or use this `jdbc:mysql://localhost:3306/springSecurity?createDatabaseIfNotExist=true` for an easier approach.)
   - Update the `application.yaml` file with your database credentials:

     ```properties (for example)
       datasource:
         url: ${MYSQL_URL} // jdbc:mysql://localhost:3306/springSecurity?createDatabaseIfNotExist=true
         username: ${MYSQLUSER} // root
         password: ${MYSQLPASSWORD} // blank
         driver-class-name: com.mysql.cj.jdbc.Driver
     ```

3. **Configure email settings:**

   - Update the `application.yaml` file with your email SMTP server details to enable email confirmation and password reset functionalities.
   ```properties
             mail:
      host: smtp.gmail.com
      port: 465
      protocol: smtps
      username: ${GMAIL_USERNAME} // your email SMTP server details
      password: ${GMAIL_PASSWORD} // your email SMTP server details
      properties:
        mail:
          transport:
            protocol: smtps
          smtps:
            auth: true
            starttls:
              enable: true
            timeout: 8000
     ```

4. **Configure Google OAuth2:**

   - Obtain OAuth2 credentials from the [Google Developer Console](https://console.developers.google.com/).
   - Update the `application.yaml` file with your Google client ID and client secret:

     ```properties
         oauth2:
          resourceserver:
            opaquetoken:
             introspection-uri: https://www.googleapis.com
             client-id: ${GOOGLE_CLIENT_ID}  // your Google client ID
             client-secret: ${GOOGLE_CLIENT_SECRET} // your Google client secret
     ```

5. **Build and run the application:**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Using the Endpoints. How to work with them?

### User Registration
   - After a user registers, they will receive a **confirmation email**. Clicking the link in the email **enables** their account in the database.
   - If the user doesn't confirm their email - they can't use the profile and the created user will be deleted after 24hours. (UserCleanupScheduler makes that)
   <img src="https://github.com/user-attachments/assets/5e097644-016c-4c30-b145-d956f636fe05" width="700px" />

   
### User Login
   - With the proper email and password, you recieve the JTW tokens 
   <img src="https://github.com/user-attachments/assets/69a5f555-4057-4dfb-8940-62d94a07a4f3" width="700px" />


### Forgotten Password
   - When a user provides their email, the system sends an email containing a link with a token to reset their password.
   - Here is an example of how the link shoud look: `http://localhost:8080/api/v1/auth/registrationConfirm?token=889f5170-25b9-4bee-a49a-4e927bfe7d59`
   <img src="https://github.com/user-attachments/assets/28478ef9-ec32-4686-bb0a-2acacfb85409" width="700px" />
   <img src="https://github.com/user-attachments/assets/a2c0cfed-3954-42ff-ba70-440c7df84732" width="700px" />

### OAuth2 
  - Accessing `/api/v1/oauth2/url/google` will generate a link that opens the Google OAuth2 window, allowing you to select the profile you wish to log in with.
   <img src="https://github.com/user-attachments/assets/9dae55c5-9aa3-4a97-8020-2c41c4354669" width="700px" />
   <img src="https://github.com/user-attachments/assets/5c394d10-3bc4-436c-98b9-c871a1460f24" width="700px" />
   
  - After selecting the email you want to log in with, the system generates a link containing a **code** that you will need to use for the `/authenticate/google` endpoint.
  - Example of the generated link: `http://localhost:8081/process-oauth2?code=4%2F0AanRRrsJm6RGgnffRwKEezRW_1riG-4-LszeX8qYJ0dTclo-fawM7YNtC7SoJocuVvxmxg&scope=email+profile+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=1&prompt=none`
   <img src="https://github.com/user-attachments/assets/4037a346-26d7-4508-9a30-b9cb60543633" width="700px" />



