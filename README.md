Instructions
Clone the repository and run the main file CommerceApplication.java
Use PostMan tool to test and access the endpoints

Features
Product Management: Create, read, update, and delete (CRUD) operations for product listings.
User Management: Secure user authentication and authorization using JWT.
Order Processing: Manage customer orders and order history.
API Documentation: RESTful API endpoints for seamless frontend integration.
Cloud Deployment: Hosted on AWS for scalability, high availability, and security, deployed for testing.
Agile Development: Iterative development with continuous feedback and improvements.

Security RBAC(Role-Based Access Control) roles are (USER, ADMIN, SELLER):
*loggedIn user: default role assigned ("USER")
*loggedIn user: User can be Admin("ADMIN") to manage products, orders and addresses.
*LoggedIn user: User can be Seller("SELLER") to list the products on the Web App

*NOTE: seller side role implementation is not yet completed, re-desgining the seller role 

Technologies Used
Java: Core programming language.
Spring Boot: Framework for building the backend application.
PostgreSQL: Relational database for data storage and management.
JWT (JSON Web Tokens): Secure authentication for user sessions.
AWS: Cloud deployment and services.
Maven: Dependency management and build tool.

Getting Started
Prerequisites
Java 17 or higher
PostgreSQL
Maven
AWS account (for deployment)
Git
