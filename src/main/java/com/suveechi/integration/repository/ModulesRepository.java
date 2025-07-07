package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.Modules;
import com.suveechi.integration.interfaces.ModuleSubModulesList;
import com.suveechi.integration.interfaces.RoleAssignModule_SubModule_Actions;

import jakarta.transaction.Transactional;

public interface ModulesRepository extends JpaRepository<Modules, Integer>{

	@Transactional
	@Modifying
	@Query(value="INSERT INTO SUBMODULE_MASTER (submodule_name,module_id,created_by,created_date) VALUES (:submodule_name, :moduleId, :createdBy, GETDATE())", nativeQuery = true)
	int createSubModules( String submodule_name, int moduleId, String createdBy);

	
	@Query(value="select mm.module_name, mm.module_id, sm.submodule_name, sm.id as submodule_id from MODULE_MASTER mm inner join SUBMODULE_MASTER sm on sm.module_id = mm.module_id", nativeQuery = true)
	List<ModuleSubModulesList> getAllModulesAndSubModules();


	@Transactional
	@Modifying
	@Query(value="INSERT INTO ROLEASSIGN_MASTER (role_id,module_id,submodule_id,action_create,action_update,action_delete,action_read,action_verify,action_release,action_reject,created_by,created_date)"
			+ "VALUES (:role_id,:module_id,:submodule_id,:action_create,:action_update,:action_delete,:action_read,:action_verify,:action_release,:action_reject,:created_by,GETDATE())", nativeQuery = true)
	int AssignModulesForRoles(String role_id, String module_id, String submodule_id, String action_create,
			String action_update, String action_delete, String action_read, String action_verify, String action_release,
			String action_reject, String created_by);

	@Transactional
	@Modifying
	@Query(value="UPDATE ROLEASSIGN_MASTER SET role_id =:role_id,module_id = :module_id,submodule_id = :submodule_id,action_create = :action_create,action_update = :action_update,action_delete = :action_delete,action_read = :action_read,action_verify = :action_verify,action_release  = :action_release ,action_reject = :action_reject,modified_by = :modified_by,modified_date =  GETDATE() where id = :id", nativeQuery = true)
	int updateModulesForRoles(String role_id, String module_id, String submodule_id, String action_create,
			String action_update, String action_delete, String action_read, String action_verify, String action_release,
			String action_reject, String modified_by, String id);


	@Query(value = "select * from ROLEASSIGN_MASTER where is_delete = 0", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> getListModuleandSubModuleActions();


	@Query(value = "select distinct  mm.module_name, sm.submodule_name,ram.* from ROLEASSIGN_MASTER ram\r\n"
			+ "inner join MODULE_MASTER mm on mm.module_id = ram.module_id\r\n"
			+ "inner join SUBMODULE_MASTER sm on sm.module_id = mm.module_id where ram.id = :id", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> serachModuleSubModulesActionsListById(String id);


	@Transactional
	@Modifying
	@Query(value="UPDATE SUBMODULE_MASTER SET submodule_name = :submodule_name,module_id = :module_id, modified_by =:modified_by, modified_date = GETDATE() where id =:id  ", nativeQuery = true)
	int updateSubModules(String submodule_name, int module_id, String modified_by, int id);


	@Query(value = "select mm.module_name,mm.module_id,sm.submodule_name from MODULE_MASTER mm inner join SUBMODULE_MASTER sm on sm.module_id = mm.module_id where mm.module_id = :module_id", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> serachModuleSubModules(String module_id);

}
