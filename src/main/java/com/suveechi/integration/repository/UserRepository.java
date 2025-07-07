package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.RoleAssignModule_SubModule_Actions;
import com.suveechi.integration.interfaces.UserInterface;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "SELECT u.username, u.user_id, r.role_code as role, r.role_id, u.factory_id FROM USERS_MASTER AS u "
			+ "LEFT OUTER JOIN ROLE_MASTER AS r ON r.role_id = u.role_id "
			+ "WHERE u.is_active = 1  AND u.is_admin_locked = 0 AND u.username = :username AND u.password = :encryptedPassword", nativeQuery = true)
	UserInterface getUserLogin(String username, String encryptedPassword);

	@Query(value = "select is_loggedin from USERS_MASTER where user_id = :user_id and password = :encryptedPassword", nativeQuery = true)
	boolean checkWhetherUserIsLoggedin(String user_id, String encryptedPassword);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER set is_loggedin = 1 where user_id = :user_id", nativeQuery = true)
	int updateLoggedIn(String user_id);

	@Query(value = "select is_active from USERS_MASTER where user_id = :user_id", nativeQuery = true)
	boolean isActive(String user_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET login_attempt_count = :login_attempt_count where username = :username", nativeQuery = true)
	int updateLoginCount(String username, int login_attempt_count);

	@Query(value = "select login_attempt_count from USERS_MASTER where username = :username", nativeQuery = true)
	int getLogInCount(String username);

	@Transactional
	@Modifying
	@Query(value = "update USERS_MASTER set is_active = 0 where username = :username", nativeQuery = true)
	int updateIsActiveBasedOnUnsuccessfullogin(String username);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER set is_active = 0  where last_loggedin <= GETDATE() - INTERVAL 30 DAY", nativeQuery = true)
	int serUsersInactive();

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER set login_attempt_count = 0 where user_id = :user_id", nativeQuery = true)
	int updateLoginCountToZero(String user_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER set is_loggedin = 0 where user_id = :user_id", nativeQuery = true)
	int userLogout(String user_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO USERS_MASTER (user_id,password,username,email,mobile_number,created_by, created_date,role_id, factory_id) VALUES (:user_id,:encryptPassword,:username,:email,:mobile_number,:created_by,GETDATE(),:role_id, :factory_id)", nativeQuery = true)
	int userCreateUser(String user_id, String encryptPassword, String username, String email, String mobile_number,
			String created_by, String role_id, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET user_id=:user_id,password=:encryptPassword,username=:username,email=:email,mobile_number=:mobile_number,modified_by=:created_by,role_id=:role_id , factory_id = :factory_id where id =:id", nativeQuery = true)
	int userUpdateUser(String user_id, String encryptPassword, String username, String email, String mobile_number,
			String created_by, String role_id, String factory_id, String id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET modified_by = :modified_by, modified_date=GETDATE(),is_admin_locked = 1 where id = :id", nativeQuery = true)
	int userBlockUser(String id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET modified_by = :modified_by, modified_date=GETDATE(),is_active = 1 where id = :id", nativeQuery = true)
	int userActive(String id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE USERS_MASTER SET modified_by = :modified_by, modified_date=GETDATE(),is_delete = 1 where id = :id", nativeQuery = true)
	int userDelete(String id, String modified_by);

	@Query(value = "select * from USERS_MASTER where is_delete = 0", nativeQuery = true)
	List<UserInterface> listUsers();

	@Query(value = "select * from USERS_MASTER where id = :id", nativeQuery = true)
	UserInterface serachUserById(String id);

	
	@Query(value = "select distinct  um.username,um.email,rm.role_name,mm.module_id,mm.module_name,sm.id as submodule_id,sm.submodule_name,ram.action_create,ram.action_delete,\r\n"
			+ "ram.action_read,ram.action_reject,ram.action_release,ram.action_update,ram.action_verify\r\n"
			+ " from USERS_MASTER um\r\n"
			+ " inner join ROLE_MASTER rm on rm.role_id = um.role_id\r\n"
			+ "inner join ROLEASSIGN_MASTER ram on ram.role_id = um.role_id\r\n"
			+ "inner join MODULE_MASTER mm on mm.module_id = ram.module_id\r\n"
			+ "inner join SUBMODULE_MASTER sm on sm.module_id = mm.module_id where um.user_id = :user_id", nativeQuery = true)
	List<RoleAssignModule_SubModule_Actions> getLoginRoleCredtianls(String user_id);

}
