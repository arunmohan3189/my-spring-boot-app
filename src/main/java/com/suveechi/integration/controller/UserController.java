package com.suveechi.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suveechi.integration.config.EmailConfig;
//import com.suveechi.integration.config.EmailConfig;
import com.suveechi.integration.config.StringEncrypter;
import com.suveechi.integration.interfaces.RoleAssignModule_SubModule_Actions;
import com.suveechi.integration.interfaces.UserInterface;
import com.suveechi.integration.repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class UserController {

	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private EmailConfig emailSender;

	@PostMapping("/user/userlogin")
	public @ResponseBody Map<String, Object> getUserLogin(@RequestParam String username, @RequestParam String password) {

		Map<String, Object> userloginmap = new HashMap<>();
		UserInterface logininterface = null;
		int updateIsActiveBasedOnLoginCount = 0;
		String user_id = null;
		try {

			String encryptedPassword = StringEncrypter.testEncrypt(password);

			logininterface = userrepo.getUserLogin(username, encryptedPassword);

			if (logininterface == null) {
				int loginCount = userrepo.getLogInCount(username); // Retrieve current login attempt count
				loginCount++;

				// Update the login count in the database
				userrepo.updateLoginCount(username, loginCount);

				// Determine remaining attempts and provide feedback
				int remainingAttempts = 5 - loginCount;

				if (loginCount >= 5) {
					updateIsActiveBasedOnLoginCount = userrepo.updateIsActiveBasedOnUnsuccessfullogin(username);
					userloginmap.put("message", "User is locked. Please request a password reset.");
					userloginmap.put("status", "no");
					userloginmap.put("action", "UsersInfo");
				} else {
					userloginmap.put("message", "Invalid username or password. Please try again.");
					userloginmap.put("status", "no");
					userloginmap.put("loginAttempt", "Number of attempts remaining: " + remainingAttempts);
				}

				return userloginmap;
			}else {
				user_id = logininterface.getUser_id();
			}

			// Check if the user is already logged in
			boolean is_loggedin = userrepo.checkWhetherUserIsLoggedin(user_id, encryptedPassword);
			if (is_loggedin) {
				userloginmap.put("message", "User is already logged in from another device");
				userloginmap.put("status", "no");
				userloginmap.put("action", "UsersInfo");
				return userloginmap;
			}

			// Check if the user is active
			boolean is_active = userrepo.isActive(user_id);
			if (!is_active) {
				userloginmap.put("message", "User is not active, please contact Admin");
				userloginmap.put("status", "no");
				userloginmap.put("action", "UsersInfo");
				return userloginmap;
			}

			// Successful login handling
			userloginmap.put("message", "Success");
			userloginmap.put("status", "yes");
			userloginmap.put("action", "UsersInfo");
			userrepo.updateLoggedIn(user_id);
			List<RoleAssignModule_SubModule_Actions> listData = userrepo.getLoginRoleCredtianls(user_id);
			if (logininterface != null) {
				userloginmap.put("data",listData);
				userloginmap.put("message", "Success");
				userloginmap.put("status", "yes");
				userloginmap.put("action", "UsersInfo");

				int setLogincountToZero = userrepo.updateLoginCountToZero(user_id);
				ObjectMapper mapper = new ObjectMapper();
				userloginmap.putAll(mapper.convertValue(logininterface, new TypeReference<Map<String, Object>>() {
				}));
				mapper.clearProblemHandlers();
				mapper = null;

			}

		} catch (Exception e) {
			e.printStackTrace(); // Replace with proper logging
		}

		return userloginmap;
	}

	@Scheduled(cron = "0 30 6 * * ?")
	public @ResponseBody Map<String, Object> deactivateUser() {

		Map<String, Object> deactivateUseramap = new HashMap<String, Object>();
		int setUserInactiveRecord = 0;

		try {

			setUserInactiveRecord = userrepo.serUsersInactive();

			deactivateUseramap.put("message",
					(setUserInactiveRecord > 0) ? "Success, User is set to not_active" : "No users found");
			deactivateUseramap.put("status", (setUserInactiveRecord > 0) ? "yes" : "no");
			deactivateUseramap.put("action", "DeactiveUsersInfo");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return deactivateUseramap;
	}

	@PostMapping("/user/userlogout")
	public @ResponseBody Map<String, Object> userLogout(@RequestParam String user_id) {

		Map<String, Object> userLogoutmap = new HashMap<String, Object>();

		try {

			int getUserlogoutrecord = userrepo.userLogout(user_id);

			userLogoutmap.put("message", (getUserlogoutrecord > 0) ? "Logout Success" : "Failed");
			userLogoutmap.put("status", (getUserlogoutrecord > 0) ? "yes" : "no");
			userLogoutmap.put("action", "UserLogOut");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userLogoutmap;

	}
	
	@PostMapping("/user/adduser")
	public @ResponseBody Map<String, Object> userCreate(@RequestBody Map<String,String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String user_id = val.get("user_id");
			String  username = val.get("username");
			String  password = val.get("password");
			String  email = val.get("email");
			String mobile_number = val.get("mobile_number");
			String created_by = val.get("created_by");
			String encryptPassword = StringEncrypter.testEncrypt(password);
			String role_id = val.get("role_id");
			String factory_id = val.get("factory_id");
			int count = userrepo.userCreateUser(user_id,encryptPassword,username,email,mobile_number,created_by,role_id,factory_id);
		    int emailSent =	emailSender.sendEmail(email, username, password);
			response.put("message", (count > 0) ? "User Created Success" : "User Created Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("Email Sent", (emailSent > 0) ? "Mail Sent Successfully" : "Mail Sent Failed");
			response.put("action", "UserCreation");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}
	
	@PostMapping("/user/updateuser")
	public @ResponseBody Map<String, Object> userUpdate(@RequestBody Map<String,String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String user_id = val.get("user_id");
			String  username = val.get("username");
			String  password = val.get("password");
			String  email = val.get("email");
			String mobile_number = val.get("mobile_number");
			String created_by = val.get("created_by");
			String encryptPassword = StringEncrypter.testEncrypt(password);
			String role_id = val.get("role_id");
			String factory_id = val.get("factory_id");
			String id = val.get("id");
			int count = userrepo.userUpdateUser(user_id,encryptPassword,username,email,mobile_number,created_by,role_id,factory_id,id);
			response.put("message", (count > 0) ? "Update User Success" : "Update User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserUpdate");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}
	
	@PostMapping("/user/blockuser")
	public @ResponseBody Map<String, Object> userBlock(@RequestBody Map<String,String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String id = val.get("id");
			int count = userrepo.userBlockUser(id,modified_by);
			response.put("message", (count > 0) ? "BLOCK User Success" : "BLOCK User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserBLOCK");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/user/activeuser")
	public @ResponseBody Map<String, Object> userActive(@RequestBody Map<String,String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String id = val.get("id");
			int count = userrepo.userActive(id,modified_by);
			response.put("message", (count > 0) ? "Active User Success" : "Active User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserActive");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/user/deleteuser")
	public @ResponseBody Map<String, Object> userdelete(@RequestBody Map<String,String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String id = val.get("id");
			int count = userrepo.userDelete(id,modified_by);
			response.put("message", (count > 0) ? "Delete User Success" : "Delete User Failed");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UserDelete");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/user/listuser")
	public @ResponseBody Map<String, Object> userList() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<UserInterface> userInterface = null;
		try {
			userInterface = userrepo.listUsers();
			response.put("message", (userInterface != null) ? "List User Success" : "List User Failed");
			response.put("status", (userInterface != null)? "yes" : "no");
			response.put("action", "UserList");
			response.put("Data", userInterface);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/user/search/{id}")
	public @ResponseBody Map<String, Object> userSearch(@PathVariable String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		UserInterface userInterface = null;
		try {
			userInterface = userrepo.serachUserById(id);
			response.put("Data", userInterface);
			response.put("message", (userInterface != null) ? "Search User Success" : "Search User Failed");
			response.put("status", (userInterface != null)? "yes" : "no");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/user/sendmailTest")
	public String  userMailSend(@RequestParam String username,@RequestParam String password,@RequestParam String emailId) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			
			 emailSender.sendEmailLink(emailId, username, password);
	            return "Email sent successfully!";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
