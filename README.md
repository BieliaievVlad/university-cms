# University CMS  

**University CMS** is a Spring Boot-based web application designed for management of university-related data.  
The system enables authorized users to perform CRUD (Create, Read, Update, Delete) operations on courses, instructors, and students via an intuitive user interface.  
It incorporates secure authentication mechanisms

## Requirements 

- Java 17 (or later)  
- Maven  
- PostgreSQL  
- Docker Desktop

## Build
 
1. Run the following command from the root directory of the project:
 
 		mvn clean package

## Setup  
1. Create a PostgreSQL database named `univercity_cms`
2. Default configuration:  

		spring.application.name=University CMS  
		spring.datasource.url=jdbc:postgresql://localhost:5432/university_cms  
		spring.datasource.username=postgres  
		spring.datasource.password=123456  
		spring.flyway.url=jdbc:postgresql://localhost:5432/university_cms  
		spring.flyway.user=postgres  
		spring.flyway.password=123456  

## Run
1. Run the application from the `target` directory using the following command (adjust database credentials if needed):

    	java -Dspring.datasource.username=postgres -Dspring.datasource.password=123456 -Dspring.flyway.user=postgres -Dspring.flyway.password=123456 -Dspring.config.name=application -jar university-cms-0.0.1-SNAPSHOT.jar
   
By default, the application will be available at:

	http://localhost:8080  
	
Admin Account Credentials:

	login: admin
	password:admin
