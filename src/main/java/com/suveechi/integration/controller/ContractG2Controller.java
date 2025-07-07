package com.suveechi.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class ContractG2Controller {

    @Autowired
    @Qualifier("misDataSource") // Ensures the correct DataSource is injected
    private DataSource misDataSource;
    
    @Autowired
    @Qualifier("misDataSource2") // Ensures the correct DataSource is injected
    private DataSource misDataSource2;
    
    @Autowired
    @Qualifier("jimsDataSource") // Ensures the correct DataSource is injected
    private DataSource jimsDataSource;

    @GetMapping("/contract/listcontractg2")
    public @ResponseBody Map<String, Object> getfetchEmployeeDataFromMIS() {
        String sql = "SELECT contract_id, descr, JobCode FROM Contracts";
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> employees = new ArrayList<>();

        try (Connection connection = misDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            System.out.println("Connected to the database");

            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process all rows in the result set
            while (resultSet.next()) {
                Map<String, String> employeeData = new HashMap<>();
                employeeData.put("contract_id", resultSet.getString("contract_id"));
                employeeData.put("descr", resultSet.getString("descr"));
                employeeData.put("JobCode", resultSet.getString("JobCode"));
               // employeeData.put("contact", resultSet.getString("contact"));
               // employeeData.put("email", resultSet.getString("email"));

                employees.add(employeeData); // Add each employee data map to the list
            }

            // Check if employees were found
            if (employees.isEmpty()) {
                System.out.println("No employees found in the database.");
                response.put("message", "No employees found");
            } else {
                System.out.println("Employees found: " + employees.size());
            }

            // Add the list of employees to the response map
            response.put("employees", employees);

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database error occurred");
        }

        return response; // Return the map which will be automatically converted to JSON
    }
    
    
    
    @GetMapping("/contract/listcontractg2unique")
    public @ResponseBody Map<String, Object> getfetchEmployeeDataFrom() {
        String sql = "SELECT contract_id, descr, JobCode FROM Contracts";
        String assignContractList = "SELECT contract_id FROM CONTRACT_MASTER ";
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> employees = new ArrayList<>();

        try (Connection connection = misDataSource.getConnection();
        	Connection jimsConnection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             PreparedStatement assignContractListStmt = jimsConnection.prepareStatement(assignContractList)) {

            System.out.println("Connected to the database");

            ResultSet resultContact = assignContractListStmt.executeQuery();
            
            Set<String> values = new HashSet<String>();
            while(resultContact.next()) {
            	values.add(resultContact.getString("contract_id"));
            }
            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process all rows in the result set
            while (resultSet.next()) {
                Map<String, String> employeeData = new HashMap<>();
                if(!values.contains(resultSet.getString("contract_id"))) {
                	 employeeData.put("contract_id", resultSet.getString("contract_id"));
                     employeeData.put("descr", resultSet.getString("descr"));
                     employeeData.put("JobCode", resultSet.getString("JobCode"));
                    // employeeData.put("contact", resultSet.getString("contact"));
                    // employeeData.put("email", resultSet.getString("email"));

                     employees.add(employeeData); // Add each employee data map to the list
                }
               
            }
            
            // Check if employees were found
            if (employees.isEmpty()) {
                System.out.println("No employees found in the database.");
                response.put("message", "No employees found");
            } else {
                System.out.println("Employees found: " + employees.size());
            }

            // Add the list of employees to the response map
            response.put("employees", employees);

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database error occurred");
        }

        return response; // Return the map which will be automatically converted to JSON
    }
    
//    @GetMapping("/contract/allload/{contract_id}/{milestone_name}")
//    public @ResponseBody Map<String, Object> getfetchloaddetailS(@PathVariable int contract_id, @PathVariable String milestone_name) {
//        String sql = "select tload_id,loadno,contract_id from Tra_Loads where contract_id = "+ contract_id ;
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, String>> employees = new ArrayList<>();
//
//        try (Connection connection = misDataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            System.out.println("Connected to the database");
//
//            // Execute query
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            // Process all rows in the result set
//            while (resultSet.next()) {
//                Map<String, String> employeeData = new HashMap<>();
//                employeeData.put("tload_id", resultSet.getString("tload_id"));
//                employeeData.put("loadno", resultSet.getString("loadno"));
//                employeeData.put("contract_id", resultSet.getString("contract_id"));
//               // employeeData.put("email", resultSet.getString("email"));
//
//                employees.add(employeeData); // Add each employee data map to the list
//            }
//            // Check if employees were found
//            if (employees.isEmpty()) {
//                System.out.println("No employees found in the database.");
//                response.put("message", "No employees found");
//            } else {
//                System.out.println("Employees found: " + employees.size());
//            }
//
//            // Add the list of employees to the response map
//            if(milestone_name.equals("Supply of Material"))
//            	response.put("loads", employees);
//            else
//            	response.put("loads", "MSI");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("error", "Database error occurred");
//        }
//
//        return response; // Return the map which will be automatically converted to JSON
//    }
    
    @GetMapping("/contract/allload/{contract_id}/{milestone_name}")
    public @ResponseBody Map<String, Object> getfetchloaddetailS(@PathVariable int contract_id, @PathVariable String milestone_name) {
        String traLoadsSql = "SELECT tload_id, loadno FROM Tra_Loads WHERE contract_id = ?";
        String qsPackingMasterSql = "SELECT load_id FROM QSPACKING_MASTER WHERE con_id = ?";
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> employees = new ArrayList<>();

        try (Connection misConnection = misDataSource.getConnection();
             Connection jimsConnection = jimsDataSource.getConnection();
             PreparedStatement traLoadsStmt = misConnection.prepareStatement(traLoadsSql);
             PreparedStatement qsPackingMasterStmt = jimsConnection.prepareStatement(qsPackingMasterSql)) {

            System.out.println("Connected to both databases");

            // Fetch existing loadno values from QSPACKING_MASTER
            qsPackingMasterStmt.setInt(1, contract_id); // Set parameter for QSPACKING_MASTER query
            ResultSet qsResultSet = qsPackingMasterStmt.executeQuery();
            Set<String> existingLoadNos = new HashSet<>();
            while (qsResultSet.next()) {
                existingLoadNos.add(qsResultSet.getString("load_id"));
            }
            for (String loadNo : existingLoadNos) {
                System.out.println(loadNo);
            }

            // Execute query for Tra_Loads
            traLoadsStmt.setInt(1, contract_id); // Set parameter for Tra_Loads query
            ResultSet traResultSet = traLoadsStmt.executeQuery();
            while (traResultSet.next()) {
                String tload_id = traResultSet.getString("tload_id");
                if (!existingLoadNos.contains(tload_id.trim())) { // Only add if not in QSPACKING_MASTER
                    Map<String, String> employeeData = new HashMap<>();
                    employeeData.put("load_id", traResultSet.getString("tload_id"));
                    employeeData.put("lot_no", traResultSet.getString("loadno"));
                    employees.add(employeeData); // Add filtered data to the list
                }
            }

            // Check if employees were found
            if (employees.isEmpty()) {
              //  System.out.println("No loads found in the database.");
                response.put("message", "No loads found");
            } else {
              //  System.out.println("Loads found: " + employees.size());
                response.put("loads", employees);
            }

            // Add a default response based on milestone_name
            if (!milestone_name.equals("Supply of Material")) {
                response.put("loads", "MSI");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database error occurred");
        }

        return response; // Return the map which will be automatically converted to JSON
    }
//=============================================================
    @GetMapping("/contract/listcontractg2/factory2")
    public @ResponseBody Map<String, Object> getfetchEmployeeDataFromMIS2() {
        String sql = "SELECT contract_id, descr, JobCode FROM Contracts";
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> employees = new ArrayList<>();

        try (Connection connection = misDataSource2.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            System.out.println("Connected to the database");

            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process all rows in the result set
            while (resultSet.next()) {
                Map<String, String> employeeData = new HashMap<>();
                employeeData.put("contract_id", resultSet.getString("contract_id"));
                employeeData.put("descr", resultSet.getString("descr"));
                employeeData.put("JobCode", resultSet.getString("JobCode"));
               // employeeData.put("contact", resultSet.getString("contact"));
               // employeeData.put("email", resultSet.getString("email"));

                employees.add(employeeData); // Add each employee data map to the list
            }

            // Check if employees were found
            if (employees.isEmpty()) {
                System.out.println("No employees found in the database.");
                response.put("message", "No employees found");
            } else {
                System.out.println("Employees found: " + employees.size());
            }

            // Add the list of employees to the response map
            response.put("employees", employees);

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database error occurred");
        }

        return response; // Return the map which will be automatically converted to JSON
    }
    
    
    
    @GetMapping("/contract/listcontractg2unique/factory2")
    public @ResponseBody Map<String, Object> getfetchEmployeeDataFrom2() {
        String sql = "SELECT contract_id, descr, JobCode FROM Contracts";
        String assignContractList = "SELECT contract_id FROM CONTRACT_MASTER  where factory_id = 2";
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> employees = new ArrayList<>();

        try (Connection connection = misDataSource2.getConnection();
        	Connection jimsConnection = jimsDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             PreparedStatement assignContractListStmt = jimsConnection.prepareStatement(assignContractList)) {

            System.out.println("Connected to the database");

            ResultSet resultContact = assignContractListStmt.executeQuery();
            
            Set<String> values = new HashSet<String>();
            while(resultContact.next()) {
            	values.add(resultContact.getString("contract_id"));
            }
            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process all rows in the result set
            while (resultSet.next()) {
                Map<String, String> employeeData = new HashMap<>();
                if(!values.contains(resultSet.getString("contract_id"))) {
                	 employeeData.put("contract_id", resultSet.getString("contract_id"));
                     employeeData.put("descr", resultSet.getString("descr"));
                     employeeData.put("JobCode", resultSet.getString("JobCode"));
                    // employeeData.put("contact", resultSet.getString("contact"));
                    // employeeData.put("email", resultSet.getString("email"));

                     employees.add(employeeData); // Add each employee data map to the list
                }
               
            }
            
            // Check if employees were found
            if (employees.isEmpty()) {
                System.out.println("No employees found in the database.");
                response.put("message", "No employees found");
            } else {
                System.out.println("Employees found: " + employees.size());
            }

            // Add the list of employees to the response map
            response.put("employees", employees);

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database error occurred");
        }

        return response; // Return the map which will be automatically converted to JSON
    }
    
//    @GetMapping("/contract/allload/{contract_id}/{milestone_name}")
//    public @ResponseBody Map<String, Object> getfetchloaddetailS(@PathVariable int contract_id, @PathVariable String milestone_name) {
//        String sql = "select tload_id,loadno,contract_id from Tra_Loads where contract_id = "+ contract_id ;
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, String>> employees = new ArrayList<>();
//
//        try (Connection connection = misDataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            System.out.println("Connected to the database");
//
//            // Execute query
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            // Process all rows in the result set
//            while (resultSet.next()) {
//                Map<String, String> employeeData = new HashMap<>();
//                employeeData.put("tload_id", resultSet.getString("tload_id"));
//                employeeData.put("loadno", resultSet.getString("loadno"));
//                employeeData.put("contract_id", resultSet.getString("contract_id"));
//               // employeeData.put("email", resultSet.getString("email"));
//
//                employees.add(employeeData); // Add each employee data map to the list
//            }
//            // Check if employees were found
//            if (employees.isEmpty()) {
//                System.out.println("No employees found in the database.");
//                response.put("message", "No employees found");
//            } else {
//                System.out.println("Employees found: " + employees.size());
//            }
//
//            // Add the list of employees to the response map
//            if(milestone_name.equals("Supply of Material"))
//            	response.put("loads", employees);
//            else
//            	response.put("loads", "MSI");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.put("error", "Database error occurred");
//        }
//
//        return response; // Return the map which will be automatically converted to JSON
//    }
    
    @GetMapping("/contract/allload/factory2/{contract_id}/{milestone_name}")
    public @ResponseBody Map<String, Object> getfetchloaddetailS2(@PathVariable int contract_id, @PathVariable String milestone_name) {
        String traLoadsSql = "SELECT tload_id, loadno FROM Tra_Loads WHERE contract_id = ?";
        String qsPackingMasterSql = "SELECT load_id FROM QSPACKING_MASTER WHERE con_id = ?  and factory_id = 2";
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> employees = new ArrayList<>();

        try (Connection misConnection = misDataSource2.getConnection();
             Connection jimsConnection = jimsDataSource.getConnection();
             PreparedStatement traLoadsStmt = misConnection.prepareStatement(traLoadsSql);
             PreparedStatement qsPackingMasterStmt = jimsConnection.prepareStatement(qsPackingMasterSql)) {

            System.out.println("Connected to both databases");

            // Fetch existing loadno values from QSPACKING_MASTER
            qsPackingMasterStmt.setInt(1, contract_id); // Set parameter for QSPACKING_MASTER query
            ResultSet qsResultSet = qsPackingMasterStmt.executeQuery();
            Set<String> existingLoadNos = new HashSet<>();
            while (qsResultSet.next()) {
                existingLoadNos.add(qsResultSet.getString("load_id"));
            }
            for (String loadNo : existingLoadNos) {
                System.out.println(loadNo);
            }

            // Execute query for Tra_Loads
            traLoadsStmt.setInt(1, contract_id); // Set parameter for Tra_Loads query
            ResultSet traResultSet = traLoadsStmt.executeQuery();
            while (traResultSet.next()) {
                String tload_id = traResultSet.getString("tload_id");
                if (!existingLoadNos.contains(tload_id.trim())) { // Only add if not in QSPACKING_MASTER
                    Map<String, String> employeeData = new HashMap<>();
                    employeeData.put("load_id", traResultSet.getString("tload_id"));
                    employeeData.put("lot_no", traResultSet.getString("loadno"));
                    employees.add(employeeData); // Add filtered data to the list
                }
            }

            // Check if employees were found
            if (employees.isEmpty()) {
              //  System.out.println("No loads found in the database.");
                response.put("message", "No loads found");
            } else {
              //  System.out.println("Loads found: " + employees.size());
                response.put("loads", employees);
            }

            // Add a default response based on milestone_name
            if (!milestone_name.equals("Supply of Material")) {
                response.put("loads", "MSI");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", "Database error occurred");
        }

        return response; // Return the map which will be automatically converted to JSON
    }
    
}
