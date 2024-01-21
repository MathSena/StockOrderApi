# STOCK ORDER API

## Description

This is a REST API for stock order management.
It was developed using Java 8, Spring Boot, PostgreSQL and Maven.

## Technical requirements

- Java (version 8)
- PostgreSQL (version 15.4-1)
- Spring Boot (version 3.1.4)
- Maven (version 3)

## Installation and Configuration

### Installing PostgreSQL

1. **Download PostgreSQL**: Download the PostgreSQL installer for Windows from
   the [official PostgreSQL website - 15.4-1-windows-x64.exe](https://get.enterprisedb.com/postgresql/postgresql-15.4-1-windows-x64.exe).

2. **Installation Process**: Run the downloaded installer and follow the on-screen instructions to complete the
   installation. Note down the superuser (postgres) password during installation.

### Starting PostgreSQL

1. **Open Command Prompt as Administrator**: Open the Command Prompt as an administrator.

2. **Navigate to PostgreSQL Directory**: Navigate to the PostgreSQL installation directory (usually located
   at `C:\Program Files\PostgreSQL\<version>`).

3. **Add bin Directory to PATH**: Inside the directory, locate the `bin` folder and add its full path to your PATH
   environment variable. Replace `<version>` with your actual PostgreSQL version.

   ```shell
   setx PATH "%PATH%;C:\Program Files\PostgreSQL\<version>\bin"

### Creating the Database

1. **Open Command Prompt as Administrator**: Open the Command Prompt as an administrator.

2. **Connect as Superuser**: Use the `psql` utility to connect to PostgreSQL as the superuser. Enter the password set
   during installation when prompted.

   ```shell
   psql -U postgres

   CREATE DATABASE stock-api;

### Configuring Database Connection in application.properties

1. **Open `application.properties`**: Open the `application.properties` file in your Spring Boot project.

2. **Database Connection Properties**: Configure the database connection properties in the `application.properties`
   file:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/api_2023_pt
    spring.datasource.username=postgres
    spring.datasource.password=postgresql
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

## Starting the Application

To start the Spring Boot application in IntelliJ IDEA or Eclipse, follow these steps:

**IntelliJ IDEA:**

1. Open IntelliJ IDEA and load your project.

2. Navigate to the project's root directory where the `pom.xml` file is located.

3. Open a terminal within IntelliJ IDEA by clicking on "View" -> "Tool Windows" -> "Terminal."

4. In the terminal, run the following command to build the project:

   ```shell
   mvn clean install

   java -jar target/pt-0.0.1-SNAPSHOT.jar
   
   (CONTROL+C) to shutdown app

5. **OR Execute on file explorer target/pt-0.0.1-SNAPSHOT.jar**

## Clone Repository GIT

To obtain the source code of the application, follow these steps:

1. Open the terminal or command prompt.

2. Execute the following command to clone the GIT repository:

   ```bash
   git clone URL-DO-REPOSITÓRIO

# Project Configuration

### Configuring SMTP Email for Gmail

To enable email notifications and sending emails through Gmail, follow these steps:

1. Open the project's configuration file (e.g., `application.properties` ).

2. Find the email configuration section in the file.

3. Update the following properties with your Gmail email address and password:

   ```yaml
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your.email@gmail.com
   spring.mail.password=your-email-password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true


