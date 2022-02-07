SMS Platform: A Gateway to Send SMS
============

This platform is based on Event-driven microservice architecture. So far, it has got two services; namely sms gateway service and spam filter service.

The sms gateway service has two modules - customer module and sms module. The customer module primary deals with registering customers. The sms gateway
service will be decomposed into two services (customer service and sms gateway service) in the future. The sms module exposes endpoints so customers can send
sms. And it publishes the sms into two rabbitmq queues with topic exchange. The first queue, named charging-module-queue, is which the charging module
service consumes from. And, the second queue, named spam-filter-queue, is which the spam filter service consumers from. The reason we have two queues is 
the data that goes into the charging module service can be different from the data that goes to the spam filter and finally to the sms delivery server in the
telecom infrastructure in the future.

The spam filter service consumes messages from rabbitmq queue. then filters out sms messages with destination address that don't start with +251 or 00251.
Finally, the message is sent to the delivery server. To do so, spam filter makes use of Spring Integration Java DSL. Message is consumed from rabbitmq inbound
channel adapter, then transformed, filtered and using service activator sent to delivery server.

Since the whole platform is designed based on reactive approach, it is highly scalable. Moreover, it is designed to handle 100,000 requests per second.

Overall System Design
============

![System-Design] (https://photos.app.goo.gl/6bbuPk7ZVT5i1oDz8)

Spam Filter Design
============

![Speam-Filter] (https://photos.google.com/u/1/search/_tra_/photo/AF1QipOE_ejDYjKlghjeB3EpkLn07E1xn36L5YuUwaQ0)

Features Done in SMS-Gateway Service
============
* Registering Customers
* Receiving sms from customers and publish it to charging-module-queue and spam-filter-queue
* Proper Error Handling
* Integration and Unit Test For Persistence, Service, and Presentation layers
* Logging to console

Features Not Done in SMS-Gateway Service
============
* Security
* Auditing
* Logging to file
* Dockerizing the service
* Swagger documentation

Features Done in Spam-Filter Service
============
* Consuming sms messages from rabbitmq queue
* Filtering out spams(Note: spams are destination address that don't start with +251 or 00251)
* Send to delivery server using service activator
* Logging to console

Features Not Done in Spam-Filter Service
============
* Proper Error Handling
* Integration and Unit Test For Persistence, Service, and Presentation layers
* Logging to file
* Security
* Auditing
* Dockerizing the service

Tech stacks used 
============
* Java >= 11 (OpenJDK JVM is tested)
* Spring Framework 5
* Spring Boot 2.5.9
* Spring Webflux
* Spring Data MongoDB Reactive
* Spring AMQP
* Spring Integration
* Logback
* Spring test
* JUnit 5

Requirements
============
* Java >= 11 (OpenJDK JVM is tested)
* MongoDB 5.0
* RabbitMQ 3.9
* Erlang 24.1
* Apache Maven 3.8

Instructions how to run for local development
============

Run the following commands inside the services root directory:
    
`./mvnw spring-boot:run`


Instructions to build and run the JAR file
============
1. Clone the repository or download and extract the archive file to your local directory.
2. Run `./mvnw clean install` to build a modern cloud native fully self contained JAR file which will be created at `target` directory within each service root directory.
3. Run `java -jar target/sms-gateway-0.0.1-SNAPSHOT.jar` from sms-gateway root directory to run the sms gateway service
4. Run `java -jar target/spam-filter-0.0.1-SNAPSHOT.jar` from spam-filter root directory to run the spam filter service

Instructions to execute Integration Tests
============
> Note that the tests will make use of embedded mongodb.

Run the following command from the root directory of sms-gateway:

`./mvnw test`

Testing with Curl
============

Creating Customer:

`curl --location --request POST 'http://localhost:8080/customers' 
--header 'Content-Type: application/json' 
--data-raw '{
"firstName":"John",
"lastName":"Mohamed",
"phoneNumber":"+251944658408",
"email":"john@email.com"
}'`

Sending SMS:

`curl --location --request POST 'http://localhost:8080/sms/send' 
--header 'Content-Type: application/json' 
--data-raw '{
"senderPhone":"+251944658408",
"receiverPhone":"+251944658445",
"text":"Hi"
}'`

Security
============
NOTE: Security is not implemented yet. The platform will have identity provider service, authentication and authorization service. The services 
that connect to rabbitmq will need ssl certificates. JWT will be used for authentication. 

Instructions to pass MongoDB connection data to sms-gateway service
============
The service should be started with the following JVM options:

`-Ddb.url=localhost`
`-Ddb.port=27017`
`-Ddb.name=smsdb`

Instructions to pass RabbitMQ connection data to both sms-gateway service and spam-filter service
============
The service should be started with the following JVM options:

`-Dmq.host=localhost`
`-Dmq.port=5672`
`-Dmq.username=guest`
`-Dmq.password=guest`
