package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.StateInterface;

import jakarta.transaction.Transactional;

@Repository
public interface StateRepository extends JpaRepository<User, Integer> {

	@Modifying
	@Transactional
	@Query(value = "INSERT into STATE_MASTER(state_code,state_name,state_id,created_by,created_date, factory_id)"
			+ "VALUES(:state_code,:state_name,:state_id,:created_by,GETDATE(), :factory_id)", nativeQuery = true)
	int addStateDetails(String state_code, String state_name, String state_id, String created_by, String factory_id);

	@Query(value = "SELECT id, state_code, state_name, state_id, factory_id, country_id FROM STATE_MASTER WHERE is_delete=0 and factory_id = :factory_id", nativeQuery = true)
	List<StateInterface> getStates(String factory_id);

	@Query(value = "SELECT id,state_code,state_name,state_id, factory_id, country_id FROM STATE_MASTER WHERE is_delete=0  AND id=:id", nativeQuery = true)
	StateInterface getStateById(String id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE STATE_MASTER SET is_delete=1 WHERE id=:state_id", nativeQuery = true)
	int deleteState(@Param("state_id") String state_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE STATE_MASTER SET state_code = :state_code, state_name=:state_name, modified_by=:modified_by, state_id = :state_id, modified_date= GETDATE() WHERE id=:id", nativeQuery = true)
	int UpdateState(@Param("state_code") String state_code, @Param("state_name") String name,
			@Param("modified_by") String modified_by, @Param("state_id") String state_id, @Param("id") String id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO STATE_MASTER_HISTORY (state_id, state_code, state_name, deleted_by,\r\n"
			+ "deleted_date, transaction_date, action)\r\n"
			+ "SELECT id, state_code, state_name, :modified_by, GETDATE(),GETDATE(), 'UPDATE'  \r\n"
			+ "FROM STATE_MASTER\r\n" + "WHERE id = :state_id", nativeQuery = true)
	int insertToHistoryTable(String state_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO STATE_MASTER_HISTORY (state_id, state_code, state_name, deleted_by,\r\n"
			+ "deleted_date, transaction_date, action)\r\n"
			+ "SELECT id, state_code, state_name, :parsedUserId, GETDATE(),GETDATE(), 'DELETE'  \r\n"
			+ "FROM STATE_MASTER\r\n" + "WHERE id = :parsedStateId", nativeQuery = true)
	int moveToHistoryBeforeDelete(int parsedUserId, int parsedStateId);

	@Query(value = "SELECT id, state_code, state_name, state_id, factory_id, country_id from STATE_MASTER where country_id = :country_id and is_delete = 0 ", nativeQuery = true)
	List<StateInterface> getStateByCountry(String country_id);

	@Query(value = "select count(id) from STATE_MASTER where factory_id = :factory_id", nativeQuery = true)
	int getStateByCount(String factory_id);

}
