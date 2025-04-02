# Insurance System with DB - Clean Code & Documentation

## 📌 Overview
This project is an **Insurance Management System** developed using **Java** and **Object-Oriented Programming (OOP) principles**, following clean code practices. The system is designed to manage employees, departments, and their insurance details efficiently, while ensuring that the code remains clean, maintainable, and scalable. It also integrates a **database connection** for persistent storage.

## 📁 Project Structure
```plaintext
src/
│── BeneficiaryFamilyMembers.java  # Represents family members of an employee, adhering to SOLID principles
│── Department.java                # Manages department details, with clear method and variable names
│── DepartmentType.java            # Defines department types in a clean, reusable manner
│── Employee.java                  # Defines employee attributes and relationships, following single responsibility
│── Insurance.java                 # Handles insurance details, maintaining separation of concerns
│── Main.java                      # Main entry point for the program, organized for clarity
│── Person.java                    # Base class for employees and beneficiaries, utilizing inheritance
│── Connector.java                 # Manages database connections with a clean abstraction
│── data_base.sql                 # SQL file for setting up the database schema with data integrity
│── .gitignore                    # Git ignore file
│── Insurance System ERD.jpg      # Entity-Relationship Diagram for the system, aiding in clarity
│── InsuranceSystemWithDB.iml     # Project configuration file
```
## 🖼️ ERD Diagram
The system has a full ERD **Entity-Relationship Diagram (ERD)**.

## 🎯 Features
- **Encapsulation & Inheritance**: `Person` is the base class, while `Employee` and `BeneficiaryFamilyMembers` extend it, ensuring reusability.
- **Department Management**: Employees belong to departments, each with a specific `DepartmentType`, improving organization and clarity.
- **Insurance Tracking**: Employees and their family members have insurance coverage, efficiently managed by the `Insurance` class.
- **Database Integration**: Utilizes SQL (`data_base.sql`) for structured data storage and reliable persistence.
- **Modular Design**: Each entity is managed separately in dedicated classes, following **Single Responsibility Principle (SRP)**.
- **Database Connectivity**: `Connector.java` handles communication with the database, abstracting the complexity of database operations.

## 🛠️ Technologies Used
- **Java**
- **MySQL (or PostgreSQL) for database storage**
- **JDBC for database connection**
- **Object-Oriented Programming (OOP) Principles**

## 🚀 How to Run
1. **Clone the repository**:
   ```sh
   git clone https://github.com/abdalkarimnael/Insurance_System_Using_OOP_Concepts.git
   cd Insurance_System_Using_OOP_Concepts

2. **Set up the database**:
   - Import `data_base.sql` into your MySQL/PostgreSQL database.
   - Configure the database connection in `connector.java`.
3. **Compile the Java files**:
   ```sh
   javac src/*.java
   ```
4. **Run the program**:
   ```sh
   java src.Main
   ```   
## 📌 Code Quality & Clean Code Practices
- **Meaningful Variable & Method Names**: All variable and method names are clear and descriptive, making the code easy to understand and maintain.
- **Consistent Formatting**: The code follows consistent indentation, spacing, and naming conventions for readability.
- **Modular Design**: Each class and method has a single responsibility, adhering to the **Single Responsibility Principle**.
- **Error Handling**: Proper error handling is implemented to ensure the program behaves predictably even when facing unexpected situations.
- **Comments & Documentation**: Every class, method, and critical code block is documented with clear, concise comments explaining their purpose and functionality.

## 📌 Future Enhancements
- Add a **User Authentication System**.
- Implement a **Graphical User Interface (GUI)** for better interaction.
- Extend functionalities with **policy and claims management**.
- Improve database security and optimization.
---
Feel free to contact with me!
