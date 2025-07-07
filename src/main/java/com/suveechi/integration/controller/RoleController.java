package com.suveechi.integration.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suveechi.integration.entity.Modules;
import com.suveechi.integration.interfaces.AllRolesInterface;
import com.suveechi.integration.interfaces.ModuleSubModulesList;
import com.suveechi.integration.interfaces.RoleAssignModule_SubModule_Actions;
import com.suveechi.integration.interfaces.UserInterface;
import com.suveechi.integration.repository.ModulesRepository;
import com.suveechi.integration.repository.RoleRepository;


@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class RoleController {

	@Autowired
	private RoleRepository rolerepo;

	@Autowired
	private ModulesRepository modulesRepo;
	@PostMapping("/role/addRole")
	public @ResponseBody Map<String, Object> addRole(@RequestBody Map<String, String> roleDetails) {
	    Map<String, Object> responseMap = new HashMap<>();

	    try {
	        // Extract fixed fields
	        String role_name = roleDetails.getOrDefault("role_name", "");
	        String role_code = roleDetails.getOrDefault("role_code", "");
	        String created_by = roleDetails.getOrDefault("created_by", "");

	        // Extract dynamic fields with default "0"
	   

	        // Pass all these extracted values to the repository
	        int addRolerecord = rolerepo.addRole(role_name, role_code, created_by);

	        // Response
	        responseMap.put("message", (addRolerecord > 0) ? "Success" : "Role Not Added");
	        responseMap.put("status", (addRolerecord > 0) ? "yes" : "no");
	        responseMap.put("action", "AddRole");

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseMap.put("message", "Error occurred: " + e.getMessage());
	        responseMap.put("status", "no");
	    }

	    return responseMap;
	}

	@PostMapping("/role/updateRole")
	public @ResponseBody Map<String, Object> updateRole(@RequestBody Map<String, String> roleDetails) {
	    Map<String, Object> responseMap = new HashMap<>();

	    try {
	        // Extract fixed fields
	    	String role_id = roleDetails.getOrDefault("role_id", "0");
	        String role_name = roleDetails.getOrDefault("role_name", "");
	        String role_code = roleDetails.getOrDefault("role_code", "");
	        String modified_by = roleDetails.getOrDefault("modified_by", "");

	        // Extract dynamic fields with default "0"
	             
	        rolerepo.moveRoleToHistoryTable(role_id, modified_by);
	        // Pass all these extracted values to the repository
	        int updateRolerecord = rolerepo.updateRole(role_name, role_code, modified_by,role_id);

	        // Response
	        responseMap.put("message", (updateRolerecord > 0) ? "Success" : "Role Not Updated");
	        responseMap.put("status", (updateRolerecord > 0) ? "yes" : "no");
	        responseMap.put("action", "UpdateRole");

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseMap.put("message", "Error occurred: " + e.getMessage());
	        responseMap.put("status", "no");
	    }

	    return responseMap;
	}
	@DeleteMapping("/role/deleteRole")
	public @ResponseBody Map<String, Object> deleteRole(@RequestParam String role_id, @RequestParam String user_id) {

		Map<String, Object> deleteRolemap = new HashMap<String, Object>();
		try {
			rolerepo.deleteRoleToHistoryTable(role_id, user_id);
			int deleteRolerecord = rolerepo.deleteRole(role_id);
			deleteRolemap.put("message", (deleteRolerecord > 0) ? "Success" : "Role Not Deleted");
			deleteRolemap.put("status", (deleteRolerecord > 0) ? "yes" : "no");
			deleteRolemap.put("action", "DeleteRole");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleteRolemap;
	}

	@DeleteMapping("/role/deleteRoleInBulk")
	public @ResponseBody Map<String, Object> deleteRoleInBulk(@RequestParam List<String> role_id) {
		Map<String, Object> deleteRoleInBulkmap = new HashMap<String, Object>();
		try {
			int deleteRoleInBulkrecord = rolerepo.deleteRoleInBulk(role_id);
			deleteRoleInBulkmap.put("message", (deleteRoleInBulkrecord > 0) ? "Success" : "Role Not Deleted");
			deleteRoleInBulkmap.put("status", (deleteRoleInBulkrecord > 0) ? "yes" : "no");
			deleteRoleInBulkmap.put("action", "DeleteRole");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleteRoleInBulkmap;
	}

	@GetMapping("/role/getAllRoles")
	public @ResponseBody Map<String, Object> getAllRoles() {

		Map<String, Object> allRolesmap = new HashMap<String, Object>();
		List<AllRolesInterface> allroleslist = null;
		try {

			allroleslist = rolerepo.getAllRoles();

			allRolesmap.put("message", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "Success" : "Failure");
			allRolesmap.put("status", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "yes" : "no");
			allRolesmap.put("action", "DistrictInfo");

			if ((allroleslist != null) && (!allroleslist.isEmpty())) {
				allRolesmap.put("DistrictDetails", allroleslist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allRolesmap;
	}
	
	@PostMapping("/addModule")
	public @ResponseBody Map<String, Object> addModule(@RequestParam String moduleName, @RequestParam List<String> subModule, @RequestParam String createdBy) {
		int count =0;
		Map<String, Object> createModule = new HashMap<String, Object>();
		try {
			Modules module = new Modules();
			module.setModuleName(moduleName);
			module.setCreatedBy(createdBy);
			Modules value =  modulesRepo.save(module);
			int moduleId = value.getId();
			for(String submodule_name : subModule) {
				count  = modulesRepo.createSubModules(submodule_name,moduleId, createdBy);
			}
			createModule.put("action", "Create_Module");
			createModule.put("message", count > 0 ? "Success" : "Failure");
			createModule.put("status", count > 0 ? "YES" : "NO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  createModule;
	}
	
	/* for update clarification is required
	 * 1. only we are going to add or remove the submodules
	 * 2. if we want to add submodule for that module is fine
	 * 3. if we want to remove that if it's assigned to any other roles that time?
	 * 4. We want to give is_delete option bcz once we assign to that role's number of users how?  
	 */
//	@PostMapping("/updateModule")
//	public @ResponseBody Map<String, Object> updateModule(@RequestParam String moduleName, @RequestParam List<Map<String,String>> subModule, @RequestParam String modifiedBy,@RequestParam int id) {
//		int count =0;
//		Map<String, Object> createModule = new HashMap<String, Object>();
//		try {
//			Modules module = new Modules();
//			module.setModuleName(moduleName);
//			module.setModifiedBy(modifiedBy);
//			module.setModifiedDate(LocalDateTime.now());
//			module.setId(id);
//			Modules value =  modulesRepo.save(module);
//			int moduleId = value.getId();
//			for(Map<String,String> submodule_name : subModule) {
//				String name = submodule_name.getOrDefault("submodule_name", "NO");
//				String submodule_id = submodule_name.getOrDefault("submodule_id", "NO");
//				String remove = submodule_name.getOrDefault("removed", "NO");
//				if(submodule_id.equalsIgnoreCase("NO")) {
//					modulesRepo.createSubModules(name,moduleId, modifiedBy);
//				}
//				if(remove.equalsIgnoreCase("YES")) {
//					
//				}
////				else {
////					int ids = Integer.valueOf(submodule_id); 
////					count  = modulesRepo.updateSubModules(name,moduleId, modifiedBy,ids);
////				}
//				
//			}
//			createModule.put("action", "Update_Module");
//			createModule.put("message", count > 0 ? "Success" : "Failure");
//			createModule.put("message", count > 0 ? "YES" : "NO");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return  createModule;
//	}
	@GetMapping("/modules/getAllmodulesandsubmodules")
	public @ResponseBody Map<String, Object> getAllModulesandSubmodules() {

		Map<String, Object> allRolesmap = new HashMap<String, Object>();
		List<ModuleSubModulesList> allroleslist = null;
		try {

			allroleslist = modulesRepo.getAllModulesAndSubModules();

			allRolesmap.put("message", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "Success" : "");
			allRolesmap.put("status", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "yes" : "no");
			allRolesmap.put("action", "ModuleList");

			if ((allroleslist != null) && (!allroleslist.isEmpty())) {
				allRolesmap.put("DistrictDetails", allroleslist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allRolesmap;
	}
	
	@GetMapping("/modules/search/{id}")
	public @ResponseBody Map<String, Object> userSearch(@PathVariable String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<RoleAssignModule_SubModule_Actions> userInterface = null;
		try {
			userInterface = modulesRepo.serachModuleSubModules(id);
			response.put("Data", userInterface);
			response.put("message", (userInterface != null) ? "Search Module & Submodule List Success" : "Search Module & Submodule List Failed");
			response.put("status", (userInterface != null)? "yes" : "no");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/assignModuleToRole/add")
	public @ResponseBody Map<String, Object> assignModulesToRole(@RequestBody List<Map<String,String>> val) {
		int count =0;
		Map<String, Object> createModule = new HashMap<String, Object>();
		
		try { // for each submodules UI have to send same way 
			if(val != null && !val.isEmpty()) {
				for(Map<String,String> data : val) {
					String role_id = data.get("role_id");
					String module_id = data.get("module_id");
					String submodule_id = data.get("submodule_id");
					String action_create = data.get("action_create");
					String action_update = data.get("action_update");
					String action_delete = data.get("action_delete");
					String action_read = data.get("action_read");
					String action_verify = data.get("action_verify");
					String action_release = data.get("action_release");
					String action_reject = data.get("action_reject");
					String created_by = data.get("created_by");
					count = modulesRepo.AssignModulesForRoles(role_id,module_id,submodule_id,action_create,action_update,action_delete,action_read,action_verify,action_release,action_reject,created_by);
				}
			}
			createModule.put("action", "AssignModuleToRole");
			createModule.put("message", count > 0 ? "Success" : "Failure");
			createModule.put("message", count > 0 ? "YES" : "NO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  createModule;
	}
	
	@PostMapping("/assignModuleToRole/update")
	public @ResponseBody Map<String, Object> updateModulesToRole(@RequestBody List<Map<String,String>> val) {
		int count =0;
		Map<String, Object> createModule = new HashMap<String, Object>();
		
		try { // for each submodules UI have to send same way 
			if(val != null && !val.isEmpty()) {
				for(Map<String,String> data : val) {
					String role_id = data.get("role_id");
					String module_id = data.get("module_id");
					String submodule_id = data.get("submodule_id");
					String action_create = data.get("action_create");
					String action_update = data.get("action_update");
					String action_delete = data.get("action_delete");
					String action_read = data.get("action_read");
					String action_verify = data.get("action_verify");
					String action_release = data.get("action_release");
					String action_reject = data.get("action_reject");
					String modified_by = data.get("modified_by");
					String id = data.get("id");
					count = modulesRepo.updateModulesForRoles(role_id,module_id,submodule_id,action_create,action_update,action_delete,action_read,action_verify,action_release,action_reject,modified_by,id);
					
				}
			}
			createModule.put("action", "UpdateAssignModuleToRole");
			createModule.put("message", count > 0 ? "Success" : "Failure");
			createModule.put("message", count > 0 ? "YES" : "NO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  createModule;
	}
	
	@GetMapping("/assignModuleToRole/list")
	public @ResponseBody Map<String, Object> listModulesToRole() {
		Map<String, Object> createModule = new HashMap<String, Object>();
		
		try { // for each submodules UI have to send same way 
			
			List<RoleAssignModule_SubModule_Actions> ls = modulesRepo.getListModuleandSubModuleActions();
			createModule.put("action", "ListAssignModuleToRole");
			createModule.put("message",ls != null ? "Success" : "Failure");
			createModule.put("message", ls != null ? "YES" : "NO");
			createModule.put("data", ls != null ? ls : "NULL");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  createModule;
	}
	
	
	@GetMapping("/assignModuleToRole/search/{id}")
	public @ResponseBody Map<String, Object> moduleSearchById(@PathVariable String id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<RoleAssignModule_SubModule_Actions> userInterface = null;
		try {
			userInterface = modulesRepo.serachModuleSubModulesActionsListById(id);
			response.put("Data", userInterface);
			response.put("message", (userInterface != null) ? "Search AssignModuleToRole Success" : "Search AssignModuleToRole Failed");
			response.put("status", (userInterface != null)? "yes" : "no");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	
	
/*
 @PostMapping("/role/addRole")
	public @ResponseBody Map<String, Object> addRole(@RequestBody Map<String, String> roleDetails) {
	    Map<String, Object> responseMap = new HashMap<>();

	    try {
	        // Extract fixed fields
	        String role_name = roleDetails.getOrDefault("role_name", "");
	        String role_code = roleDetails.getOrDefault("role_code", "");
	        String created_by = roleDetails.getOrDefault("created_by", "");

	        // Extract dynamic fields with default "0"
	        int invoice_module = Integer.parseInt(roleDetails.getOrDefault("invoice_module", "0"));
	        int qs_submodule = Integer.parseInt(roleDetails.getOrDefault("qs_submodule", "0"));
	        int invoice_submodule = Integer.parseInt(roleDetails.getOrDefault("invoice_submodule", "0"));
	        int masters_submodule = Integer.parseInt(roleDetails.getOrDefault("masters_submodule", "0"));
	        int scrap_submodule = Integer.parseInt(roleDetails.getOrDefault("scrap_submodule", "0"));
	        int reports_submodule = Integer.parseInt(roleDetails.getOrDefault("reports_submodule", "0"));
	        int read = Integer.parseInt(roleDetails.getOrDefault("read_access", "0"));
	        int read_write = Integer.parseInt(roleDetails.getOrDefault("read_write_access", "0"));
	        int verify = Integer.parseInt(roleDetails.getOrDefault("verify_access", "0"));

	        // Pass all these extracted values to the repository
	        int addRolerecord = rolerepo.addRole(
	            role_name, role_code, created_by,
	            invoice_module, qs_submodule, invoice_submodule,
	            masters_submodule, scrap_submodule, reports_submodule,
	            read, read_write, verify
	        );

	        // Response
	        responseMap.put("message", (addRolerecord > 0) ? "Success" : "Role Not Added");
	        responseMap.put("status", (addRolerecord > 0) ? "yes" : "no");
	        responseMap.put("action", "AddRole");

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseMap.put("message", "Error occurred: " + e.getMessage());
	        responseMap.put("status", "no");
	    }

	    return responseMap;
	}

	@PostMapping("/role/updateRole")
	public @ResponseBody Map<String, Object> updateRole(@RequestBody Map<String, String> roleDetails) {
	    Map<String, Object> responseMap = new HashMap<>();

	    try {
	        // Extract fixed fields
	    	String role_id = roleDetails.getOrDefault("role_id", "0");
	        String role_name = roleDetails.getOrDefault("role_name", "");
	        String role_code = roleDetails.getOrDefault("role_code", "");
	        String modified_by = roleDetails.getOrDefault("modified_by", "");

	        // Extract dynamic fields with default "0"
	        int invoice_module = Integer.parseInt(roleDetails.getOrDefault("invoice_module", "0"));
	        int qs_submodule = Integer.parseInt(roleDetails.getOrDefault("qs_submodule", "0"));
	        int invoice_submodule = Integer.parseInt(roleDetails.getOrDefault("invoice_submodule", "0"));
	        int masters_submodule = Integer.parseInt(roleDetails.getOrDefault("masters_submodule", "0"));
	        int scrap_submodule = Integer.parseInt(roleDetails.getOrDefault("scrap_submodule", "0"));
	        int reports_submodule = Integer.parseInt(roleDetails.getOrDefault("reports_submodule", "0"));
	        int read = Integer.parseInt(roleDetails.getOrDefault("read_access", "0"));
	        int read_write = Integer.parseInt(roleDetails.getOrDefault("read_write_access", "0"));
	        int verify = Integer.parseInt(roleDetails.getOrDefault("verify_access", "0"));

	        
	        rolerepo.moveRoleToHistoryTable(role_id, modified_by);
	        // Pass all these extracted values to the repository
	        int updateRolerecord = rolerepo.updateRole(
	            role_name, role_code, modified_by,
	            invoice_module, qs_submodule, invoice_submodule,
	            masters_submodule, scrap_submodule, reports_submodule,
	            read, read_write, verify,role_id
	        );

	        // Response
	        responseMap.put("message", (updateRolerecord > 0) ? "Success" : "Role Not Updated");
	        responseMap.put("status", (updateRolerecord > 0) ? "yes" : "no");
	        responseMap.put("action", "UpdateRole");

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseMap.put("message", "Error occurred: " + e.getMessage());
	        responseMap.put("status", "no");
	    }

	    return responseMap;
	}
	@DeleteMapping("/role/deleteRole")
	public @ResponseBody Map<String, Object> deleteRole(@RequestParam String role_id, @RequestParam String user_id) {

		Map<String, Object> deleteRolemap = new HashMap<String, Object>();
		try {

			rolerepo.deleteRoleToHistoryTable(role_id, user_id);

			int deleteRolerecord = rolerepo.deleteRole(role_id);

			deleteRolemap.put("message", (deleteRolerecord > 0) ? "Success" : "Role Not Deleted");
			deleteRolemap.put("status", (deleteRolerecord > 0) ? "yes" : "no");
			deleteRolemap.put("action", "DeleteRole");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleteRolemap;
	}

	@DeleteMapping("/role/deleteRoleInBulk")
	public @ResponseBody Map<String, Object> deleteRoleInBulk(@RequestParam List<String> role_id) {

		Map<String, Object> deleteRoleInBulkmap = new HashMap<String, Object>();

		try {

			int deleteRoleInBulkrecord = rolerepo.deleteRoleInBulk(role_id);

			deleteRoleInBulkmap.put("message", (deleteRoleInBulkrecord > 0) ? "Success" : "Role Not Deleted");
			deleteRoleInBulkmap.put("status", (deleteRoleInBulkrecord > 0) ? "yes" : "no");
			deleteRoleInBulkmap.put("action", "DeleteRole");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleteRoleInBulkmap;
	}

	@GetMapping("/role/getAllRoles")
	public @ResponseBody Map<String, Object> getAllRoles() {

		Map<String, Object> allRolesmap = new HashMap<String, Object>();
		List<AllRolesInterface> allroleslist = null;
		try {

			allroleslist = rolerepo.getAllRoles();

			allRolesmap.put("message", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "Success" : "");
			allRolesmap.put("status", ((allroleslist != null) && (!allroleslist.isEmpty())) ? "yes" : "no");
			allRolesmap.put("action", "DistrictInfo");

			if ((allroleslist != null) && (!allroleslist.isEmpty())) {
				allRolesmap.put("DistrictDetails", allroleslist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return allRolesmap;
	}
 */
}
