package com.suveechi.integration.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class EmployeeController {

	@Autowired
	@Qualifier("misDataSource") // Ensures the correct DataSource is injected
	private DataSource misDataSource;

	@GetMapping("/employee/allEmployee")
	public @ResponseBody Map<String, Object> getfetchEmployeeDataFromMIS() {
		String sql = "SELECT empcode, name, contact, email FROM Employee";
		Map<String, Object> empmap = new HashMap<>();
		List<String> emplist = new ArrayList<>();

		try (Connection connection = misDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			System.out.println("Connected to the database");

			// Execute query
			ResultSet resultSet = preparedStatement.executeQuery();

			// Ensure the resultSet has rows
			if (resultSet.next()) {
				// Process all rows in the result set
				do {
					String empcode = resultSet.getString("empcode");
					String name = resultSet.getString("name");
					String contact = resultSet.getString("contact");
					String email = resultSet.getString("email");

					// Debugging: print employee data
					System.out.println("empcode is: " + empcode);
					String employeeData = "EmpCode: " + empcode + ", Name: " + name + ", Contact: " + contact
							+ ", Email: " + email;
					emplist.add(employeeData); // Add the string to the list

					// Print employee data for debugging
					System.out.println(employeeData);

				} while (resultSet.next()); // Continue looping through the result set until no rows remain
			} else {
				// If no data is found
				System.out.println("No employees found in the database.");
			}

			// Add the list of employees to the map
			empmap.put("employees", emplist);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return empmap; // Return the map with the list of employee data
	}
}
