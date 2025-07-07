package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.ContractListFromContractInterfaces;
import com.suveechi.integration.interfaces.OpeningBalanceInterfaces;

import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;

public interface OpeningBalanceRepository extends JpaRepository<User, Integer> {

	@Transactional
	@Modifying
	@Query(value="INSERT INTO ADVANCEPACKINGNOTE (con_id,load_id,created_by,created_date) Values (:con_id,:load_id,:created_by,GETDATE())", nativeQuery = true)
	int createOpeningBalanceRecord(String con_id, String load_id, String created_by);

	@Transactional
	@Modifying
	@Query(value="INSERT INTO ADVANCEPACKINGNOTE_ITEM (pn_id,description,total,tax_total,net_total,created_by,created_date,factory_id) Values (:pn_id,:description,:total,:tax_total,:net_total,:created_by,GETDATE(),:factory_id)", nativeQuery = true)
//	@Query(value="INSERT INTO ADVANCEPACKINGNOTE_ITEM (pn_id, description, total, tax_total, net_total, created_by, created_date) VALUES ((SELECT TOP 1 pn_id FROM ADVANCEPACKINGNOTE ORDER BY pn_id DESC), :description, :total, :tax_total, :net_total, :created_by, GETDATE())", nativeQuery = true)
	void createOpeningBalanceItemRecord(int pn_id,String description, String total, String tax_total,String net_total, String created_by, int factory_id);

	@Transactional
	@Modifying
	@Query(value="UPDATE ADVANCEPACKINGNOTE SET con_id = :con_id, modified_by = :modified_by, modified_date = GETDATE() WHERE pn_id = :pn_id   ", nativeQuery = true)
	int updateOpeningBalanceRecord(String con_id,  String modified_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value=" UPDATE ADVANCEPACKINGNOTE_ITEM SET pn_id = :pn_id,description =:description,total= :total,tax_total=:tax_total,net_total=:net_total, modified_by = :modified_by, modified_date = GETDATE() WHERE slno = :slno", nativeQuery = true)
	void updateOpeningBalanceItemRecord(String pn_id, String description, String total, String tax_total,String net_total, String modified_by, String slno);

	
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO ADVANCEPACKINGNOTE_HISTORY (pn_id,con_id,load_id,created_by,created_date,modified_by,modified_date,action)"
			+ " select pn_id,con_id,load_id,created_by,created_date,:modified_by,GETDATE(),'UPDATE' from ADVANCEPACKINGNOTE where pn_id = :pn_id", nativeQuery = true)
	void insertOpeningBalanceHistory(String pn_id, String modified_by);

	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO ADVANCEPACKINGNOTE_ITEM_HISTORY (slno,pn_id,total,net_total,tax_total,description,created_by,created_date,modified_by,modified_date,action)"
			+ " select slno, pn_id,total,net_total,tax_total,description,created_by,created_date,:modified_by,GETDATE(),'UPDATE' from ADVANCEPACKINGNOTE_ITEM where slno = :slno", nativeQuery = true)
	void insertOpeningBalaneItemHistory(String slno, String modified_by);

	@Query(value = "select apni.description, apni.total, apni.net_total, apni.tax_total, apn.con_id, apni.created_by, apni.created_date,cm.contract_name,apni.factory_id  from ADVANCEPACKINGNOTE_ITEM apni\r\n"
			+ "inner join ADVANCEPACKINGNOTE apn on apn.pn_id = apni.pn_id \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = apn.con_id\r\n"
			+ "where apni.is_delete = 0 and apni.factory_id = :factory_id", nativeQuery = true)
	List<OpeningBalanceInterfaces> getOpeningBalanceList(String factory_id);
	
	
	@Query(value ="select apni.description,apni.slno, apni.total, apni.net_total, apni.tax_total, apn.con_id  from ADVANCEPACKINGNOTE_ITEM apni\r\n"
			+ "inner join ADVANCEPACKINGNOTE apn on apn.pn_id = apni.pn_id where apni.is_delete = 0 and apni.slno = :slno", nativeQuery = true)
	OpeningBalanceInterfaces searchOpeningBalanceById(String slno);
	
	
	@Query(value =  "select contract_id, contract_name from  CONTRACT_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<ContractListFromContractInterfaces> getContractListFromContractInfo(String factory_id);

}
