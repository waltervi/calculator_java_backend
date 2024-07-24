# calculator_java_backend
calculator_java_backend

## TLDR

### Required software
* java 21
* maven
* docker

### How to run docker database
You will need to install docker, (docker desktop recommended)
Inside project directory run:

    $ docker compose up

### How to see data saved in mysql database
All required data to login inside the file -> docker-compose.yml

YOU MUST MANUALLY CREATED THE DATABASE NAMED -> calculator.

By either creating it with a took or running the script in ./db_scritps/create_database.sql


### How to run project locally
Inside project directory run:

    $ mvn clean package
    $ cd target
    $ java -jar CalculatorBackend-0.0.1-SNAPSHOT.jar

This will run the backend server in port 8080




## Design ideas , I have time to read
### Libraries used
* Java 21
* Spring Boot 3
* docker to run the database locally

### Allow easy move to microservices
The whole project can be easily moved to microservices architecture, since it is divided intro 3 subdirectories each one defining an specific part of the project:

- authentication
- operations
- records

Communication between these 3 subdirectories is done via a Service class, no DAO/Repositories are being shared.

### Not using spring security, session less server with custom JWT
I decided to create my own implementation of a JWT token and exclude spring boot from the implementation since sometimes it brings more configuration problems than allowing rapid development.

Implementation of Custom JWT Token in TokenService.java

### Configuration in application.properties

* token.secret  -> key to encrypt tokens
* token.salt    -> salt used in token encryption
* token.key     -> each token inside it, contains these characters.

The remaining entries are self descriptive.
* token.defaultDurationInDays=30
* balance.defaultValue=50
* spring.datasource.url=jdbc:mysql://localhost:3306/calculator
* spring.datasource.username=root
* spring.datasource.password=admincalculator
* spring.jpa.hibernate.ddl-auto=update

#### Operations costs must be a valid double value
* cost_addition=1
* cost_substraction=2
* cost_multiplication=3
* cost_division=4
* cost_square_root=5
* cost_random_string=6

### Testcases
Testcases are are sort of full happy path test of all the APIs a user would use to run the project.



## Known issues

### Moved from Cookies to Http Headers
The project was originally using Cookies with httpOnly option set to hold the JWT token. But since we are not using the same domain name for the backed, we added CORS to the backend, and CORS is not well received by browsers, which don't send Cookies to CORS allowing servers.
So for this matter, we changed the Cookie management to simple HTTP Headers.

### Database connection with root
It should be an specific user, not root. 

### Not using HTTPS
No time to set this up with AWS and maven.

### Fixed setup on costs for operations
In order to avoid manually running scripts to define operations costs, we delete all current operations costs at startup, and recreate all those values.
This allows easy setup.
Values for costs are in application.properties.

