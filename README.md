# Montran Application - a server application which demonstrates the flow of currency transfer

Network security has long been regarded as an important issue between server and mobile or website platforms, especially in payment gateway and banking system. Here is an example showing how to build a server platform to handle currency transfer.

As we need a payment service to handle the payment process, we assume that there is another mirco-service API which requires a transaction ID. We built a [GitHub page](https://sunnytse0326.github.io/MockJson/transaction/result.json) which will return following results:
```
{
	"success": true,
	"transactionId": "ds492fu3Jeyac",
	"amount": 1560.5,
	"currency": "HKD"
}
```

# Project Structure
This project included four APIs, which includes register, login, get profile and transaction APIs for the entire process.

In this application, we used Java Spring Boot as core application and MySQL as database structure for the skeleton of architecture. User may need to firstly install mysql and import the `create_user.sql` file from the following command and it will automatically create the database and use table:
``` mysql -p < create_user.sql ```

In the project, we used JWT HMAC using SHA-256 as login credentials. It is an asymmetric algorithm, which only use one private key to generate the signature.

# API Documentation
We use [Swagger 2.0](https://swagger.io/) as the documentation of Montran server. We saved the document and could browse with following links:
<br>[API Document](https://sunnytse0326.github.io/MockJson/transaction/swagger.json)
<br>[Viewing Platform](https://petstore.swagger.io/)

# Test Case
In this project, we added several test cases for JWT checking, password hashing and APIs functionality. We tried several cases in which to ensure the encrypt and decrypt flow.

# Libraries Used
- JJWT<br>
- Web Client<br>
- MySQL Library<br>
- Wire Mock<br>

# Reference
[Spring Boot with JPA](https://spring.io/guides/gs/accessing-data-jpa/)<br>


