package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.CountryInterface;

import jakarta.transaction.Transactional;

public interface CountryRepository extends JpaRepository<User, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO COUNTRY_MASTER (country_code, country_name, created_by, created_date, factory_id) VALUES (:country_code, :country_name, :user_id, GETDATE(), :factory_id)", nativeQuery = true)
	int addCountry(String country_code, String country_name, String user_id, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO COUNTRY_MASTER_HISTORY (country_id, country_code, country_name, modified_by, modified_date, transaction_date,action)\r\n"
			+ "SELECT id, country_code, country_name, :user_id, GETDATE(),GETDATE(),'UPDATE' \r\n"
			+ "FROM COUNTRY_MASTER\r\n" + "WHERE id = :country_id", nativeQuery = true)
	int moveCountryToHistory(String country_id, String user_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE COUNTRY_MASTER set country_code = :country_code, country_name = :country_name, modified_by = :user_id, modified_date = GETDATE() where id = :country_id", nativeQuery = true)
	int updateCountry(String country_code, String country_name, String user_id, String country_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO COUNTRY_MASTER_HISTORY (country_id, country_code, country_name, deleted_by, deleted_date, transaction_date,action)\r\n"
			+ "SELECT id, country_code, country_name, :user_id, GETDATE(),GETDATE(), 'DELETE' \r\n"
			+ "FROM COUNTRY_MASTER\r\n" + "WHERE id = :country_id", nativeQuery = true)
	int moveToHistoryTable(String country_id, String user_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE COUNTRY_MASTER set is_delete = 1, modified_by = :user_id, modified_date = GETDATE() where id = :country_id", nativeQuery = true)
	int deleteCountry(String country_id, String user_id);

	@Query(value = "SELECT * from COUNTRY_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<CountryInterface> getAllCountryDetails(String factory_id);

	@Query(value = "SELECT * from COUNTRY_MASTER where is_delete = 0 and id = :country_id", nativeQuery = true)
	CountryInterface getCOuntryBasedOnId(String country_id);

}
