# Enterprise E-Commerce Application

An enterprise-grade e-commerce platform built with a microservice architecture for scalability, performance, and maintainability.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Architecture](#architecture)
- [Setup and Installation](#setup-and-installation)
---

## Technologies Used

- **Backend**: Spring Boot (Java)
- **Frontend**: React.js
- **Database**: 
  - MySQL (Relational Data)
  - MongoDB (NoSQL for specific use cases)
- **Message Queue**: RabbitMQ
- **Containerization**: Docker
- **Authentication**: JWT (JSON Web Tokens) with Role-Based Access Control (RBAC)
- **Testing**: JUnit, Mockito

---

## Features

### Sellers
- **Product Management**: Add, update, and delete products.
- **Inventory Management**: Track and update stock for products.
- **Order Management**: Manage and fulfill customer orders.

### Customers
- **Search, Filter, and Categorize Products**: Easily find products.
- **Shopping Cart**: Add, update, and remove items.
- **Order Management**: Place orders and view order history.

### Additional Features
- **Message Queue**: RabbitMQ for managing order requests asynchronously.
- **Inventory Management**: Automatic updates to product inventory after orders.
- **Unit Testing**: Comprehensive tests for critical components.

---

## Architecture

The system uses a **microservice architecture** with the following services:

- **AuthService**: Manages user authentication and authorization (JWT-based).
- **ProductService**: Handles product-related operations for sellers.
- **OrderService**: Manages order placement and status updates.
- **InventoryService**: Keeps track of product availability.
- **NotificationService**: Sends notifications to users for order updates.

Each microservice is containerized using Docker and communicates asynchronously via RabbitMQ.

---

## Setup and Installation

### Prerequisites
- **Java 17+**
- **Node.js 18+**
- **MySQL** and **MongoDB**
- **Docker** and **Docker Compose**

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/ecommerce-app.git
 
