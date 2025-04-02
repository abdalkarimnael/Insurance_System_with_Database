drop database data_base;
create database data_base;
use data_base;

CREATE TABLE departmentType(dType CHAR(20) PRIMARY KEY);

CREATE TABLE department(departmentNumber CHAR(7) PRIMARY KEY,
address CHAR(20), dType CHAR(20) NOT NULL, FOREIGN KEY(dType)REFERENCES departmentType(dType));

CREATE TABLE insurance (insuranceNumber CHAR(7) PRIMARY KEY, startDate DATE, availableDate DATE, expireDate DATE,
departmentNumber CHAR(7) NOT NULL, FOREIGN KEY (departmentNumber) REFERENCES department (departmentNumber));

CREATE TABLE employee_dep (employeeId CHAR(7) PRIMARY KEY, employeeName CHAR(30),
dateOfBirth DATE,  address CHAR(20), departmentNumber CHAR(7) NOT NULL, insuranceNumber CHAR(7) NOT NULL,
FOREIGN KEY (departmentNumber) REFERENCES department (departmentNumber), FOREIGN KEY(insuranceNumber) REFERENCES insurance(insuranceNumber));
 
CREATE TABLE BeneficiaryFamilyMembers(memberId CHAR(7), memberName CHAR(30), DOB DATE, 
employeeId CHAR(7), relation CHAR(20), PRIMARY KEY(employeeId, memberId), FOREIGN KEY (employeeId) REFERENCES employee_dep(employeeId));

DROP TABLE BeneficiaryFamilyMembers;

DELETE FROM departmentType WHERE dType = "f";
DELETE FROM department WHERE departmentNumber = "100";

SELECT * FROM departmentType ORDER BY dType DESC;
SELECT * FROM department;
SELECT * FROM insurance;
SELECT * FROM employee_dep;
SELECT * FROM BeneficiaryFamilyMembers;

DELETE FROM employee_dep WHERE employeeId = "3";


SELECT * 
FROM departmentType
LEFT JOIN department ON departmentType.dType = department.dType
UNION
SELECT * FROM departmentType
RIGHT JOIN department ON departmentType.dType = department.dType;


DROP TABLE department;
SET FOREIGN_KEY_CHECKS=0; -- to disable them

CALL GetAllEmployees();
CALL getEmployeeCountByDepartment();
CALL getMembersCount("3");

CREATE VIEW view_insurance AS
SELECT *
FROM insurance;

SELECT * FROM view_insurance;
INSERT INTO view_insurance VALUES("11", "2002-2-2", "2002-2-15", "2002-2-15", "1");
DELETE FROM view_insurance WHERE insuranceNumber = "11";



-- deallocate prepare 
/*
SELECT Emp_id AS ID, CONCAT(ED.Emp_name, ', ', ED.address, ', ', ED.Dep_type, ', ', D.address) AS Information 
FROM employee_dep AS ED, department AS D
WHERE ED.address = D.address;

SELECT e.Emp_id, e.Emp_name, department.Dep_type FROM employee_dep AS e
INNER JOIN department ON e.Dep_type = department.Dep_type;
SELECT e.Emp_id, e.Emp_name, department.Dep_type FROM employee_dep AS e
RIGHT JOIN department ON e.Dep_type = department.Dep_type;

SELECT * FROM department ORDER BY department.Dep_type DESC;

DELETE FROM department WHERE Dep_type = "ffff";*/