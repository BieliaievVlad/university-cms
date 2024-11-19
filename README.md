# University CMS  

Приложение предназначено для взаимодействия с базой данных Postgres.  

## Требования  

- Java 17 (или более поздняя версия)  
- Maven  
- PostgreSQL  

## Установка  
1. Создайте базу данных PostgreSQL 'univercity_cms'  
2. Настройки по умолчанию:  

	spring.application.name=university_cms  

	spring.datasource.url=jdbc:postgresql://localhost:5432/university_cms  
	spring.datasource.username=postgres  
	spring.datasource.password=123456  

	spring.flyway.url=jdbc:postgresql://localhost:5432/university_cms  
	spring.flyway.user=postgres  
	spring.flyway.password=123456  

##Запуск
1. Запустите приложение, при необходиморсти указав параметры подключения к базе данных через командную строку из каталога 'target'.  
Например:  

	java -Dspring.datasource.username=postgres -Dspring.datasource.password=123456 -Dspring.flyway.user=postgres -Dspring.flyway.password=123456 -Dspring.config.name=application -jar university-cms-0.0.1-SNAPSHOT.jar

По умолчанию приложение доступно по адресу:  

	http://localhost:8080