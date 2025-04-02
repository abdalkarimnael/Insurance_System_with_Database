import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    /***
     * This is the main method
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("\t\tWelcome to the Insurance System");
        System.out.println("\t\t-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        Scanner input = new Scanner(System.in);
        Scanner in = new Scanner(System.in);
        int iteration=1;
        while(iteration==1){
            displayMainMenu();
            if (makeMainMenuChoice(in, input)) return;
        }

    }

    /***
     * This boolean method to make different choices based on the user input
     * @param in
     * @param input
     * @return true or false
     */
    private static boolean makeMainMenuChoice(Scanner in, Scanner input) {
        int choice;
        choice = in.nextInt();
        switch (choice){
            case 1: //Add new department type
                System.out.println("Department type: (Ex: Ministries)");
                String typeOfDepartment = input.nextLine();
                addDepartmentType(typeOfDepartment);
                break;
            case 2: //Add new department
                addNewDepartment();
                break;
            case 3: // Add an insurance
                addAnInsurance(input);
                break;
            case 4: // Add an employee
                addAnEmployee(input);
                break;
            case 5: /// Display all departments information
                displayAllDepartments();
                break;
            case 6: /// Search about an employee by his/her id
                searchByEmployeeId(input);
                break;
            case 7: /// Update a specific data field for a specific employee
                updateEmployeeInformation(input);
                break;
            case 8: /// Display family members for an employee
                if (displayFamilyMembers(input)) return true;
                break;
            case 9: /// Display all employees
                displayAllEmployees();
                break;
            case 10: //// Delete one member of an employee
                if (deleteEmployeeFamilyMember(input)) return true;
                break;
            case 11: /// Display the number of employees in each department
                getEmployeeCountByDepartment();
                break;
            case 12: //// To display the number of members for a specific employee
                getBeneficiaryMembersCount(input);
                break;
            case 13: //// Exit from the system
                diaplaySuccessMessage("Thanks for using our system! Exit Successfully ^_- !");
                exit(0);
            default: //// any other choice
                displayNotAvailable();
                break;
        }
        return false;
    }

    /***
     * This method is to get the beneficiary family members of a specific employee.
     * @param input
     */
    private static void getBeneficiaryMembersCount(Scanner input) {
        try{
            String id = getEmployeeId(input);
            Connection conn = getConnection();

            if (!checkIfEmployeeFound(conn, id)){
                System.out.println("Sorry, this employee doesn't exist!!");
                return;
            }

            ResultSet result = makeGetMembersCount(conn, id);

            extractMembersCount(result, id);

            closeConnection(conn);
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to extract the count field value
     * @param result
     * @param id
     * @throws SQLException
     */
    private static void extractMembersCount(ResultSet result, String id) throws SQLException {
        while(result.next()){
            String count = result.getString(1);
            System.out.println("Employee Id:: "+ id +" ---- Number of beneficiary family members: "+count);
        }
    }

    /***
     * This method to make a query to get the number of employees for a specific employee
     * @param conn
     * @param id
     * @return The result set
     * @throws SQLException
     */
    private static ResultSet makeGetMembersCount(Connection conn, String id) throws SQLException {
        String query = "CALL getMembersCount(?)";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, id);
        ResultSet result = statement.executeQuery();
        return result;
    }

    /***
     * This void method to calculate the number of employees in each department
     */
    private static void getEmployeeCountByDepartment() {
        try{
            Connection conn = getConnection();
            makeGetCountEmployeeQuery(conn);
            closeConnection(conn);
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to make a query to get the employee count.
     * @param conn
     * @throws SQLException
     */
    private static void makeGetCountEmployeeQuery(Connection conn) throws SQLException {
        String query = "CALL getEmployeeCountByDepartment()";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet1 = statement.executeQuery();
        extractCountInformation(resultSet1);
    }

    /***
     * This method to extract the data fields content and the data of records from the query result.
     * @param resultSet1
     * @throws SQLException
     */
    private static void extractCountInformation(ResultSet resultSet1) throws SQLException {
        while(resultSet1.next()){
            String departmentNumber = resultSet1.getString(1);
            String employeeCount = resultSet1.getString(2);
            System.out.println("Department Number: "+departmentNumber+" ---- Number of Employees: "+employeeCount);
        }
    }

    /***
     * This boolean method to delete a specific employee family member by his/her id.
     * @param input
     * @return true or false based on the existence.
     */
    private static boolean deleteEmployeeFamilyMember(Scanner input) {
        try{
            Connection conn = getConnection();

            String id = getEmployeeId(input);
            if(!checkIfEmployeeFound(conn, id)){
                System.out.println("This employee isn't found!");
            }
            else {
                String memberId = getMemberId(input);
                if (checkIfMemberFound(conn, id, memberId)){
                    deleteTheMember(conn, id, memberId);
                }
                else{
                    System.out.println("This family member isn't found!");
                }
            }
        }
        catch (Exception e){
            displayError(e);
        }
        return false;
    }

    /***
     * This void method to make the query to delete the member.
     * @param conn
     * @param id
     * @param memberId
     * @throws SQLException
     */
    private static void deleteTheMember(Connection conn, String id, String memberId) throws SQLException {
        String sql = "DELETE FROM BeneficiaryFamilyMembers WHERE employeeId = ? AND memberId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, id);
        statement.setString(2, memberId);
        statement.execute();
    }

    /***
     * This method to check if the member is foundor not.
     * @param conn
     * @param id
     * @param memberId
     * @return true or false based on the existence.
     * @throws SQLException
     */
    private static boolean checkIfMemberFound(Connection conn, String id, String memberId) throws SQLException {
        String check = "SELECT * FROM BeneficiaryFamilyMembers WHERE employeeId = ? AND memberId = ?";
        PreparedStatement ps1 = conn.prepareStatement(check);
        ps1.setString(1, id);
        ps1.setString(2, memberId);
        ResultSet rs1 = ps1.executeQuery();
        if(isFilled(rs1)){
            System.out.println("This member is found!");
            return true;
        }
        return false;
    }

    /***
     * This mrthof to get the member id as an input from the user.
     * @param input
     * @return member id as string.
     */
    private static String getMemberId(Scanner input) {
        System.out.println("Enter member id: ");
        String memberId = input.nextLine();
        return memberId;
    }

    /***
     * This method to display all employees information
     */
    private static void displayAllEmployees() {
        try{
            Connection conn = getConnection();

            ResultSet rs = makeDisplayAllEmployeesQuery(conn);
            extractAllEmployeesInformation(rs);
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to extract all method information
     * @param rs
     * @throws SQLException
     */
    private static void extractAllEmployeesInformation(ResultSet rs) throws SQLException {
        while(rs.next()){
            System.out.println("Id: "+ rs.getString(1)+" *** Name: "+ rs.getString(2)+" ***" +
                    " Date of Birth: "+ rs.getString(3)+" *** Address: "+ rs.getString(4)+" *** Department Number: "+ rs.getString(5)+ " *** Insurance Number: "+rs.getString(6));
        }
    }

    /***
     * This method to make a query to get the information of each employee
     * @param conn
     * @return Result set
     * @throws SQLException
     */
    private static ResultSet makeDisplayAllEmployeesQuery(Connection conn) throws SQLException {
        String query = "CALL GetAllEmployees()";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        return rs;
    }

    /***
     * This method is to display a family members for a specific employee.
     * @param input
     * @return true or false based on the employee existence.
     */
    private static boolean displayFamilyMembers(Scanner input) {
        try {
            String id = getEmployeeId(input);

            Connection conn = getConnection();

            if(!checkIfEmployeeFound(conn, id)){
                System.out.println("This employee doesn't found!");
                return true;
            }
            PreparedStatement statement = getPreparedStatementForMembers(conn, id);
            getMembersInformation(statement);

        }
        catch (Exception e){
            displayError(e);
        }
        return false;
    }

    /***
     * This void method is to extract the member data.
     * @param statement
     * @throws SQLException
     */
    private static void getMembersInformation(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.executeQuery();
        int flag = 0;
        while(rs.next()){
            flag = 1;
            String memberId = rs.getString(1);
            String memberName = rs.getString(2);
            String dateOfBirth = rs.getString(3);
            String employeeId = rs.getString(4);
            System.out.println("Member No."+ memberId+" **** Member name: "+memberName+" **** Date of birth: "+
                    dateOfBirth+ "**** Parent Id: " + employeeId);
        }
        if(!isFilled(rs) && flag == 0){
            System.out.println("This employee doesn't have any family members!");
        }
    }

    /***
     * This method is to make a query and return a prepare statement.
     * @param conn
     * @param id
     * @return prepare statement.
     * @throws SQLException
     */
    private static PreparedStatement getPreparedStatementForMembers(Connection conn, String id) throws SQLException {
        String query = "SELECT * FROM BeneficiaryFamilyMembers WHERE employeeId = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, id);
        return statement;
    }

    /***
     * This void method to display the main menu of the system
     */
    private static void displayMainMenu() {
        System.out.println("--------------------------------");
        System.out.print("1. Add department types.\t\t\t\t\t\t2. Add a department.\n3. Add an insurance.\t\t\t\t\t\t\t4. Add a new employee." +
                "\n5. Display all departments.\t\t\t\t\t\t6. Search for a specific employee by his/her ID.\n7. Update an employee info by his/her ID.\t" +
                "\t8. Display the family members of an employee.\n9. Display All Employees.\t\t\t\t\t\t10. Delete a specific employee's member.\n11. Display the number of employees \n    in each department.\t\t\t\t\t\t\t12. Display the number of family members for a specific employee.\n13. Exit.\n>>>Your choice: ");
    }

    /***
     * This is a void method to update a specific data field of employee information for a specific employee
     * @param input
     */
    private static void updateEmployeeInformation(Scanner input) {
        try{
            String id = getEmployeeId(input);
            Connection conn = getConnection();
            if(!checkIfEmployeeFound(conn, id)){
                System.out.println("This employee doesn't found!");
                return;
            }
            displayUpdateEmployeeChoices();
            compareUpdateChoiceInput(input, conn, id);
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to choose the proper choice based on the user input
     * @param input
     * @param conn
     * @param id
     * @throws SQLException
     */
    private static void compareUpdateChoiceInput(Scanner input, Connection conn, String id) throws SQLException {
        int list = getUpdateChoice();

        if(list == 1) {
            updateName(input, conn, id);
        } else if (list == 2) {
            updateDateOfBirth(input, conn, id);
        } else if (list == 3) {
            updateAddress(input, conn, id);
        } else if (list == 4) {
            updateDepartmentNumber(input, conn, id);
        } else if (list == 5) {
            updateInsuranceNumber(input, conn, id);
        }
        else{
            displayNotAvailable();
        }
    }

    /***
     * This is a void method to display a not available message
     */
    private static void displayNotAvailable() {
        System.out.println("This choice isn't available!");
    }

    /***
     * This void method to update the insurance number of a specific employee
     * @param input
     * @param conn
     * @param id
     * @throws SQLException
     */
    private static void updateInsuranceNumber(Scanner input, Connection conn, String id) throws SQLException {
        System.out.println("New Insurance Number: ");
        String newInsuranceNumber = input.nextLine();
        if(!checkIfInsuranceFound(conn, newInsuranceNumber)){
            System.out.println("Department Number isn't found!");
        }
        checkIfInsuranceAssigned(input, conn, newInsuranceNumber);
        makeUpdateInsuranceNumberQuery(conn, id, newInsuranceNumber);
        diaplaySuccessMessage("Insurance Number updated Successfully!");
    }

    /***
     * This is a void method to make a query to update the insurance number
     * @param conn
     * @param id
     * @param newInsuranceNumber
     * @throws SQLException
     */
    private static void makeUpdateInsuranceNumberQuery(Connection conn, String id, String newInsuranceNumber) throws SQLException {
        String sql = "UPDATE employee_dep SET insuranceNumber = ? WHERE employeeId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, newInsuranceNumber);
        statement.setString(2, id);
        statement.execute();
    }

    /***
     * This ia a void method to update the department number of an employee
     * @param input
     * @param conn
     * @param id
     * @throws SQLException
     */
    private static void updateDepartmentNumber(Scanner input, Connection conn, String id) throws SQLException {
        System.out.println("New Department Number: ");
        String newDepartmentNumber = input.nextLine();
        if(!checkIfDepartmentExist(conn, newDepartmentNumber)){
            System.out.println("This department Number isn't exist, create it!");
            addNewDepartment();
        }
        makeUpdateDepartmentNumberQuery(conn, id, newDepartmentNumber);
        diaplaySuccessMessage("Department Number updated Successfully!");
    }

    /***
     * This is a void method to make a query to update the department number of an employee
     * @param conn
     * @param id
     * @param newDepartmentNumber
     * @throws SQLException
     */
    private static void makeUpdateDepartmentNumberQuery(Connection conn, String id, String newDepartmentNumber) throws SQLException {
        String sql = "UPDATE employee_dep SET departmentNumber = ? WHERE employeeId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, newDepartmentNumber);
        statement.setString(2, id);
        statement.execute();
    }

    /***
     * This is a void method to update the address of an employee
     * @param input
     * @param conn
     * @param id
     * @throws SQLException
     */
    private static void updateAddress(Scanner input, Connection conn, String id) throws SQLException {
        System.out.println("New Address: (Ex: Ramallah)");
        String newAddress = input.nextLine();
        makeUpdateAddressQuery(conn, id, newAddress);
        diaplaySuccessMessage("Address updated Successfully!");
    }

    /***
     * This is a void method to make a query to update the address of an employee
     * @param conn
     * @param id
     * @param newAddress
     * @throws SQLException
     */
    private static void makeUpdateAddressQuery(Connection conn, String id, String newAddress) throws SQLException {
        String sql = "UPDATE employee_dep SET address = ? WHERE employeeId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, newAddress);
        statement.setString(2, id);
        statement.execute();
    }

    /***
     * This is a void method to update the date of birth of an employee
     * @param input
     * @param conn
     * @param id
     * @throws SQLException
     */
    private static void updateDateOfBirth(Scanner input, Connection conn, String id) throws SQLException {
        System.out.println("New Date of Birth: (Ex: yy-mm-dd)");
        String date = input.nextLine();
        makeUpdateDateOfBirthQuery(conn, id, date);
        diaplaySuccessMessage("Date of birth updated Successfully!");
    }

    /***
     * This is a void method to make a query to update the date of birth of an employee
     * @param conn
     * @param id
     * @param date
     * @throws SQLException
     */
    private static void makeUpdateDateOfBirthQuery(Connection conn, String id, String date) throws SQLException {
        String sql = "UPDATE employee_dep SET dateOfBirth = ? WHERE employeeId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, date);
        statement.setString(2, id);
        statement.execute();
    }

    /***
     * This is a void method to update the name of an employee
     * @param input
     * @param conn
     * @param id
     */
    private static void updateName(Scanner input, Connection conn, String id) {
        try{
            System.out.println("New Name: ");
            String newName = input.nextLine();
            makeUpdateNameQuery(conn, id, newName);
            diaplaySuccessMessage("Name updated Successfully!");
        }
        catch(Exception e){
            displayError(e);
        }
    }

    /***
     * This is a void method to make a query to update the name of an employee
     * @param conn
     * @param id
     * @param newName
     * @throws SQLException
     */
    private static void makeUpdateNameQuery(Connection conn, String id, String newName) throws SQLException {
        String sql = "UPDATE employee_dep SET employeeName = ? WHERE employeeId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, newName);
        statement.setString(2, id);
        statement.execute();
    }

    /***
     * This is a method to get the update choice from the user.
     * @return It will return the entered choice.
     */
    private static int getUpdateChoice() {
        Scanner enter = new Scanner(System.in);
        int list = enter.nextInt();
        return list;
    }

    /***
     * This void method to display the update employee information choices
     */
    private static void displayUpdateEmployeeChoices() {
        System.out.println("Choose from the below list: ");
        System.out.println("1. Employee Name.\t2. Date of Birth.\t3. Address.\n4. Department Number.\t5. Insurance Number.");
    }

    /***
     * This method to get an information of an employee
     * @param input
     */
    private static void searchByEmployeeId(Scanner input) {
        try{
            String id = getEmployeeId(input);
            Connection conn = getConnection();
            getSpecificEmployeeInformation(conn, id);
            closeConnection(conn);
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to extract an information for a specific employee from the query result.
     * @param rs
     * @param id
     * @throws SQLException
     */
    private static void extractSpecificEmployeeInformation(ResultSet rs, String id) throws SQLException {
        String insuranceNumber;
        String numberOfDepartment;
        String address;
        String dateOfBirth;
        String employeeId;
        String name;
        while(rs.next()){
            System.out.println("Employee No."+ id +" Information: ");
            employeeId = rs.getString("employeeId");
            name = rs.getString("employeeName");
            dateOfBirth = rs.getString("dateOfBirth");
            address = rs.getString("address");
            numberOfDepartment = rs.getString("departmentNumber");
            insuranceNumber = rs.getString("insuranceNumber");
            System.out.println("Id: "+employeeId+"|| Name: "+ name + "|| Date of Birth: "+dateOfBirth+"|| Address: "+address+"|| Department Number: "+numberOfDepartment+"|| Insurance Number: "+insuranceNumber);
        }
    }

    /***
     * This method to get information for a specific employee using a query.
     * @param conn
     * @param id
     * @throws SQLException
     */
    private static void getSpecificEmployeeInformation(Connection conn, String id) throws SQLException {
        if(!checkIfEmployeeFound(conn, id)){
            System.out.println("This employee isn't found!");
        }
        else {
            String sql = "SELECT * FROM employee_dep WHERE employeeId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            extractSpecificEmployeeInformation(rs, id);
        }
    }

    /***
     * This method to display all departments that found on the system with all its information
     */
    private static void displayAllDepartments() {
        try {
            Connection conn = getConnection();
            ResultSet rs = getDepartmentsResultSet(conn);

            getAllDepartmentsInformation(rs);
            closeConnection(conn);
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to get all departments information
     * @param rs
     * @throws SQLException
     */
    private static void getAllDepartmentsInformation(ResultSet rs) throws SQLException {
        while(rs.next()){
            String depNumber, addr, departmentT;
            depNumber = rs.getString("departmentNumber");
            addr = rs.getString("address");
            departmentT = rs.getString("dType");
            System.out.println("Department Numer: "+depNumber+" - Department Type: "+ departmentT + " - Address: "+addr);
        }
    }

    /***
     * This method to get all departments information by using a query.
     * @param conn
     * @return This will return the result set.
     * @throws SQLException
     */
    private static ResultSet getDepartmentsResultSet(Connection conn) throws SQLException {
        String query = "SELECT * FROM department";
        PreparedStatement statement = conn.prepareStatement(query);

        ResultSet rs = statement.executeQuery();
        return rs;
    }

    /***
     * This void method to add new employee.
     * @param input
     */
    private static void addAnEmployee(Scanner input) {
        String address;
        String numberOfDepartment;
        String name;
        String dateOfBirth;
        String employeeId;
        String insuranceNumber;
        try{
            Connection conn = getConnection();
            employeeId = getEmployeeId(input);

            if (checkIfEmployeeFound(conn, employeeId)) return;


            name = getEmployeeName(input);
            dateOfBirth = getDateOfBirth(input);
            address = getEmployeeAddress(input);


            numberOfDepartment = getDepartmentNumber(input, conn);

            insuranceNumber = getInsuranceNumber(input, conn);

            Employee emp1 = getEmployee(employeeId, name, address, dateOfBirth, numberOfDepartment, insuranceNumber);

            insertNewEmployee(conn, emp1);
//            PreparedStatement ps;

            while(1==1){
                String answer = getBeneficiaryChoice(input);
                if (checkBeneficiaryChoice(answer)) break;
                getMemberInformation result = getMemberInformation(input);
                BeneficiaryFamilyMembers member1 = getBeneficiaryFamilyMember(result, employeeId);
                ///Insert
                insertMember(conn, member1);
            }
            closeConnection(conn);
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to create new record of Beneficiary Family Member and insert it into its table in the dataBase.
     * @param conn
     * @param member1
     * @throws SQLException
     */
    private static void insertMember(Connection conn, BeneficiaryFamilyMembers member1) throws SQLException {
        PreparedStatement ps;
        String sql2 = "INSERT INTO BeneficiaryFamilyMembers(memberId, memberName, DOB, employeeId, relation) VALUES(?, ?, ?, ?, ?)";
        ps = conn.prepareStatement(sql2);
        ps.setString(1, member1.getId());
        ps.setString(2, member1.getName());
        ps.setString(3, member1.getDateOfBirth());
        ps.setString(4, member1.getEmployeeId());
        ps.setString(5, member1.getRelationship());
        ps.execute();
    }

    /***
     * This method to create an object from BeneficiaryFamilyMember Class.
     * @param result
     * @param employeeId
     * @return An object of BeneficiaryFamilyMember class with all his/her information.
     */
    private static BeneficiaryFamilyMembers getBeneficiaryFamilyMember(getMemberInformation result, String employeeId) {
        BeneficiaryFamilyMembers member1 = new BeneficiaryFamilyMembers();
        member1.setId(result.memberId());
        member1.setName(result.name());
        member1.setDateOfBirth(result.dateOfBirth());
        member1.setRelationship(result.relationship());
        member1.setEmployeeId(employeeId);
        return member1;
    }

    /***
     * This method to get all member information.
     * @param input
     * @return All information.
     */
    private static getMemberInformation getMemberInformation(Scanner input) {
        String memberId;
        String dateOfBirth;
        String relationship;
        String name;
        System.out.println("Member Id:");
        memberId = input.nextLine();
        System.out.println("Member Name:");
        name = input.nextLine();
        System.out.println("Date of birth:");
        dateOfBirth = input.nextLine();
        System.out.println("Relationship:");
        relationship = input.nextLine();
        getMemberInformation result = new getMemberInformation(memberId, relationship, name, dateOfBirth);
        return result;
    }

    private record getMemberInformation(String memberId, String relationship, String name, String dateOfBirth) {
    }

    /***
     * This method will take the user answer about his/her need to add members and check if yes or no.
     * @param answer
     * @return true or false based on the user answer.
     */
    private static boolean checkBeneficiaryChoice(String answer) {
        return Objects.equals(answer.toLowerCase(), "n");
    }

    /***
     * This method to get the answer of a user about his/her need to add members or not
     * @param input
     * @return The answer.
     */
    private static String getBeneficiaryChoice(Scanner input) {
        System.out.println("Do you need to add a beneficiary family member? (Y/N)");
        String answer = input.nextLine();
        return answer;
    }

    /***
     * This method to add new employee record to the employee table.
     * @param conn
     * @param emp1
     * @throws SQLException
     */
    private static void insertNewEmployee(Connection conn, Employee emp1) throws SQLException {
        String sql1 = "INSERT INTO employee_dep (employeeId, employeeName,\n" +
                "dateOfBirth,  address, departmentNumber, insuranceNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql1);

        ps.setString(1, emp1.getId());
        ps.setString(2, emp1.getName());
        ps.setString(3, emp1.getDateOfBirth());
        ps.setString(4, emp1.getAddress());
        ps.setString(5, emp1.getDepartmentNumber());
        ps.setString(6, emp1.getInsuranceNumber());
        ps.execute();
    }

    /***
     * This method to create an object from the employee class.
     * @param employeeId
     * @param name
     * @param address
     * @param dateOfBirth
     * @param numberOfDepartment
     * @param insuranceNumber
     * @return employee object.
     */
    private static Employee getEmployee(String employeeId, String name, String address, String dateOfBirth, String numberOfDepartment, String insuranceNumber) {
        Employee emp1 = new Employee();
        emp1.setId(employeeId);
        emp1.setName(name);
        emp1.setAddress(address);
        emp1.setDateOfBirth(dateOfBirth);
        emp1.setDepartmentNumber(numberOfDepartment);
        emp1.setInsuranceNumber(insuranceNumber);
        return emp1;
    }

    /***
     * This method to get the insurance number from the user.
     * @param input
     * @param conn
     * @return insurance number.
     * @throws SQLException
     */
    private static String getInsuranceNumber(Scanner input, Connection conn) throws SQLException {
        String insuranceNumber;
        System.out.println("Insurance Number:");
        insuranceNumber = input.nextLine();
        ///If insurance number doesn't exist
        if(!checkIfInsuranceFound(conn, insuranceNumber)){
            System.out.println("This insurance isn't found, Please add this insurance!");
            addAnInsurance(input);
        }
        //To check if the insurance assigned or not
        /// This because the insurance should not be for more than one employee
        checkIfInsuranceAssigned(input, conn, insuranceNumber);
        return insuranceNumber;
    }

    /***
     * This method to check if the entered insurance is assigned or not.
     * @param input
     * @param conn
     * @param insuranceNumber
     * @throws SQLException
     */
    private static void checkIfInsuranceAssigned(Scanner input, Connection conn, String insuranceNumber) throws SQLException {
        String check = "SELECT insuranceNumber FROM employee_dep WHERE insuranceNumber = ?";
        PreparedStatement ps1 = conn.prepareStatement(check);
        ps1.setString(1, insuranceNumber);
        ResultSet rs1 = ps1.executeQuery();
        if(isFilled(rs1)){
            System.out.println("This insurance is assigned to an employee, create another one!");
            getInsuranceNumber(input, conn);
        }
    }

    /***
     * This method to get the department number from the user.
     * @param input
     * @param conn
     * @return department number.
     * @throws SQLException
     */
    private static String getDepartmentNumber(Scanner input, Connection conn) throws SQLException {
        String numberOfDepartment;
        System.out.println("Department Number:");
        numberOfDepartment = input.nextLine();
        ///If department number doesn't exist
        if(!checkIfDepartmentExist(conn, numberOfDepartment)){
            System.out.println("This department isn't found, Please add this department!");
            addNewDepartment();
        }
        return numberOfDepartment;
    }

    /***
     * This method to get the employee address from the user.
     * @param input
     * @return Employee address.
     */
    private static String getEmployeeAddress(Scanner input) {
        String address;
        System.out.println("Employee address:");
        address = input.nextLine();
        return address;
    }

    /***
     * This method to get the employee date of birth from the user.
     * @param input
     * @return date of birth
     */
    private static String getDateOfBirth(Scanner input) {
        String dateOfBirth;
        System.out.println("Date of birth:");
        dateOfBirth = input.nextLine();
        return dateOfBirth;
    }

    /***
     * This method to get the employee name from the user.
     * @param input
     * @return employee name.
     */
    private static String getEmployeeName(Scanner input) {
        String name;
        System.out.println("Employee Name:");
        name = input.nextLine();
        return name;
    }

    /***
     * This method to get the employee id from the user.
     * @param input
     * @return employee id.
     */
    private static String getEmployeeId(Scanner input) {
        String employeeId;
        System.out.println("Employee Id:");
        employeeId = input.nextLine();
        return employeeId;
    }

    /***
     * This method is to check if the employee is already found or not
     * @param conn
     * @param employeeId
     * @return true or false based on the existence of the employee number
     * @throws SQLException
     */
    private static boolean checkIfEmployeeFound(Connection conn, String employeeId) throws SQLException {
        String check = "SELECT * FROM employee_dep WHERE employeeId = ?";
        PreparedStatement ps1 = conn.prepareStatement(check);
        ps1.setString(1, employeeId);
        ResultSet rs1 = ps1.executeQuery();
        if(isFilled(rs1)){
            System.out.println("This employee is found!");
            return true;
        }
        return false;
    }

    /***
     * This method is to add a new insurance for an employee
     * @param input
     */
    private static void addAnInsurance(Scanner input) {
        try{
            Connection conn = getConnection();
            getInsuranceInformation result = getInsuranceInformation(input, conn);
            if (result == null) return;
            Insurance ins1 = getInsurance(result);
            insertInsuranceRecord(conn, ins1);
            closeConnection(conn);
            diaplaySuccessMessage("Insurance added successfully!!");
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method is to display a success message on screen
     * @param x
     */
    private static void diaplaySuccessMessage(String x) {
        System.out.println(x);
    }

    /***
     * This method is to add new insurance record to the insurance table in dataBase.
     * @param conn
     * @param ins1
     * @throws SQLException
     */
    private static void insertInsuranceRecord(Connection conn, Insurance ins1) throws SQLException {
        String sql = "INSERT INTO insurance (insuranceNumber, startDate, availableDate, expireDate, departmentNumber) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, ins1.getInsurance_num());
        ps.setString(2, ins1.getStartDate());
        ps.setString(3, ins1.getAvailableDate());
        ps.setString(4, ins1.getExpireDate());
        ps.setString(5, ins1.getDepartmentNumber());
        ps.execute();
    }

    /***
     * This method is to get an insurance object with all insurance information.
     * @param result
     * @return An object of the insurance class with its data.
     */
    private static Insurance getInsurance(getInsuranceInformation result) {
        Insurance ins1 = new Insurance();
        ins1.setInsurance_num(result.insuranceNumber());
        ins1.setStartDate(result.startDate());
        ins1.setAvailableDate(result.availableDate());
        ins1.setExpireDate(result.expireDate());
        ins1.setDepartmentNumber(result.numberOfDepartment());
        return ins1;
    }

    /***
     * This method will display an error message and the error statement when any problem occurred.
     * @param e
     */
    private static void displayError(Exception e) {
        System.out.println("An error occurred");
        System.out.println(e);
    }

    /***
     * This method is to get the insurance information from the user.
     * @param input
     * @param conn
     * @return It will return the all insurance information.
     * @throws SQLException
     */
    private static getInsuranceInformation getInsuranceInformation(Scanner input, Connection conn) throws SQLException {
        String startDate;
        String expireDate;
        String availableDate;
        String insuranceNumber;
        String numberOfDepartment;
        System.out.println("Insurance Number:");
        insuranceNumber = input.nextLine();
        if (checkIfInsuranceFound(conn, insuranceNumber)) return null;
        System.out.println("Start Date:");
        startDate = input.nextLine();
        System.out.println("Available Date:");
        availableDate = input.nextLine();
        System.out.println("Expire Date:");
        expireDate = input.nextLine();
        System.out.println("Department Number:");
        numberOfDepartment = input.nextLine();
        ///To check the existence of the entered department.
        if(!checkIfDepartmentExist(conn, numberOfDepartment)){
            System.out.println("This department doesn't exist, Add this department please!!");
            addNewDepartment();
        }
        getInsuranceInformation result = new getInsuranceInformation(numberOfDepartment, insuranceNumber, startDate, expireDate, availableDate);
        return result;
    }

    /***
     *
     * @param numberOfDepartment
     * @param insuranceNumber
     * @param startDate
     * @param expireDate
     * @param availableDate
     */
    private record getInsuranceInformation(String numberOfDepartment, String insuranceNumber, String startDate, String expireDate, String availableDate) {
    }

    /***
     * This method is to check if the entered insurance number is exist or not.
     * @param conn
     * @param insuranceNumber
     * @return true or false based on the existence of insurance number.
     * @throws SQLException
     */
    private static boolean checkIfInsuranceFound(Connection conn, String insuranceNumber) throws SQLException {
        String check = "SELECT * FROM insurance WHERE insuranceNumber = ?";
        PreparedStatement ps1 = conn.prepareStatement(check);
        ps1.setString(1, insuranceNumber);
        ResultSet rs1 = ps1.executeQuery();
        if(isFilled(rs1)){
            System.out.println("This Insurance is found!");
            return true;
        }
        return false;
    }

    /***
     * This method to check if the query return any result or not
     * @param rs
     * @return true if empty or false if not empty
     */
    public static boolean isFilled(ResultSet rs){
        boolean isEmpty = true;
        try {
            while(rs.next()){
                isEmpty = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !isEmpty;
    }

    /***
     * This method to add new department and create a record for it
     * @param typeOfDepartment
     */
    public static void addDepartmentType(String typeOfDepartment){
        try{
            Connection conn = getConnection();

            if (checkIfTypeExist(typeOfDepartment, conn)) return;
            DepartmentType departmentType1 = getDepartmentType(typeOfDepartment);
            // Database insertion
            insertNewDepartmentType(conn, departmentType1);
            closeConnection(conn);
            diaplaySuccessMessage("Department type added successfully!!");
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method to close the connection with dataBase
     * @param conn
     * @throws SQLException
     */
    private static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    /***
     * This method to create an object from the department type class
     * @param typeOfDepartment
     * @return This will return an department type object
     */
    private static DepartmentType getDepartmentType(String typeOfDepartment) {
        DepartmentType departmentType1 = new DepartmentType();
        departmentType1.setDepartmentType(typeOfDepartment);
        return departmentType1;
    }
    /***
     * This method is a void method to insert a new department type record into department type table
     * @param conn
     * @param departmentType1
     * @throws SQLException
     */
    private static void insertNewDepartmentType(Connection conn, DepartmentType departmentType1) throws SQLException {
        String sql = "INSERT INTO departmentType VALUES (?)";
        PreparedStatement ps2 = conn.prepareStatement(sql);
        ps2.setString(1, departmentType1.getDepartmentType());
        ps2.execute();
    }

    /***
     * This method is to check if the entered department type is found or not on the department type table
     * @param typeOfDepartment
     * @param conn
     * @return true or false based on the existence of record
     * @throws SQLException
     */
    private static boolean checkIfTypeExist(String typeOfDepartment, Connection conn) throws SQLException {
        String check = "SELECT * FROM departmentType WHERE dType = ?";
        PreparedStatement ps = conn.prepareStatement(check);
        ps.setString(1, typeOfDepartment);
        ResultSet rs1 = ps.executeQuery();
        if(isFilled(rs1)){
            System.out.println("This type is found!");
            return true;
        }
        return false;
    }

    /***
     * This method is a void method to add new department
     * Will display One of these messages: This department is found, try again! OR Department added successfully!! OR This department type isn't found , try again, Please create this department type!!!
     */
    public static void addNewDepartment(){
        Scanner input = new Scanner(System.in);
        try{
            getDepartmentInformation departmentInformation = getDepartmentInformation(input);
            if (departmentInformation == null) return;
            ///System.out.println(departmentNumber + "--"+typeOfDepartment+"--"+address1);
            Department department = getDepartment(departmentInformation);

            // Database insertion
            insertNewDepartment(departmentInformation, department);
            closeConnection(departmentInformation.conn());
            diaplaySuccessMessage("Department added successfully!!");
        }
        catch (Exception e){
            displayError(e);
        }
    }

    /***
     * This method is to create new record of department and insert it in the department table in dataBase.
     * @param departmentInformation
     * @param department
     * @throws SQLException
     */
    private static void insertNewDepartment(getDepartmentInformation departmentInformation, Department department) throws SQLException {
        String sql = "INSERT INTO department (departmentNumber, dType,address) VALUES (?, ?, ?)";

        PreparedStatement ps = departmentInformation.conn().prepareStatement(sql);

        ps.setString(1, department.getDepartmentNumber());
        ps.setString(2, department.getDepartmentType());
        ps.setString(3, department.getAddress());
        ps.execute();
    }

    /***
     * This method to create an object from the department class, to save the data from its variables.
     * @param departmentInformation
     * @return
     */
    private static Department getDepartment(getDepartmentInformation departmentInformation) {
        Department department = new Department();
        department.setDepartmentNumber(departmentInformation.departmentNumber());
        department.setDepartmentType(departmentInformation.result().typeOfDepartment1());
        department.setAddress(departmentInformation.address1());
        return department;
    }

    /***
     * This method is to get the department information from the user
     * @param input
     * @return The department information.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static getDepartmentInformation getDepartmentInformation(Scanner input) throws ClassNotFoundException, SQLException {
        System.out.println("Department Number: ");
        String departmentNumber = input.nextLine();
        Connection conn = getConnection();
        ///Check
        if (checkIfDepartmentExist(conn, departmentNumber)) return null;

        System.out.println("Department type: (Ex: Ministries)");
        Result result = getResult(input, conn);
        if(!isFilled(result.rs2())){
            System.out.println("This department type isn't found , try again, Please create this department type!!!");
            addDepartmentType(result.typeOfDepartment1());
        }
        System.out.println("Address: (Ex: Ramallah)");
        String address1 = input.nextLine();
        getDepartmentInformation departmentInformation = new getDepartmentInformation(departmentNumber, conn, result, address1);
        return departmentInformation;
    }

    private record getDepartmentInformation(String departmentNumber, Connection conn, Result result, String address1) {
    }

    /***
     * This method is to check if the department type is found or not, and if not found, the method will call
     * another method to create this type.
     * @param input
     * @param conn
     * @return the result of the query
     * @throws SQLException
     */
    private static Result getResult(Scanner input, Connection conn) throws SQLException {
        String typeOfDepartment1 = input.nextLine();
        String check2 = "SELECT dType FROM departmentType WHERE dType = ?";
        PreparedStatement ps2 = conn.prepareStatement(check2);
        ps2.setString(1, typeOfDepartment1);
        ResultSet rs2 = ps2.executeQuery();
        Result result = new Result(typeOfDepartment1, rs2);
        return result;
    }

    private record Result(String typeOfDepartment1, ResultSet rs2) {
    }

    /***
     * This method to check if the entered department is existed or not
     * @param conn
     * @param departmentNumber
     * @return true or false based on the existence of department based on its id
     * @throws SQLException
     */
    private static boolean checkIfDepartmentExist(Connection conn, String departmentNumber) throws SQLException {
        String check = "SELECT * FROM department WHERE departmentNumber = ?";
        PreparedStatement ps1 = conn.prepareStatement(check);
        ps1.setString(1, departmentNumber);
        ResultSet rs1 = ps1.executeQuery();
        if(isFilled(rs1)){
            System.out.println("This department is found!");
            return true;
        }
        return false;
    }
    /***
     * This method to get the connection with dataBase
     * @return an object that as reference to the dataBase
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = connector.a.connectDB();
        return conn;
    }
}