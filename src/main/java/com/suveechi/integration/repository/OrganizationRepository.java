package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.BankMasterInterface;
import com.suveechi.integration.interfaces.BusinessUnitInterface;
import com.suveechi.integration.interfaces.OrganizationMasterInterface;

import jakarta.transaction.Transactional;

public interface OrganizationRepository extends JpaRepository<User, Integer> {

	/* ORGANIZATION START */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ORGANIZATION_MASTER (org_name,registered_address,business_address,gst_number,location,created_by,created_date,state_id, factory_id)"
			+ " VALUES (:org_name,:registered_address,:business_address, :gst_number,:location,:created_by, GETDATE(), :state_id, :factory_id)", nativeQuery = true)
	int addOrganization(String org_name, String registered_address, String business_address, String gst_number,
			String location, String created_by, int state_id, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ORGANIZATION_MASTER SET org_name= :org_name, registered_address= :registered_address, business_address= :business_address,"
			+ "gst_number = :gst_number, location= :location, modified_by = :modified_by,modified_date = GETDATE(), state_id = :state_id WHERE org_id = :org_id", nativeQuery = true)
	int updateOrganization(String org_name, String registered_address, String business_address, String gst_number,
			String location, String modified_by, int state_id, String org_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ORGANIZATION_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE  org_id = :org_id", nativeQuery = true)
	int deleteOrganization(String modified_by, String org_id);

	@Query(value = "SELECT * FROM ORGANIZATION_MASTER WHERE is_delete = 0 and factory_id = :factory_id ", nativeQuery = true)
	List<OrganizationMasterInterface> getOrganizationList(String factory_id);

	@Query(value = "SELECT * FROM ORGANIZATION_MASTER WHERE org_id = :org_id ", nativeQuery = true)
	OrganizationMasterInterface searchOrganizationById(String org_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ORGANIZATION_MASTER_HISTORY (org_id,org_name,registered_address,business_address,gst_number,location,created_by,created_date,modified_by,"
			+ "modified_date,action,transaction_date)  select org_id,org_name,registered_address,business_address,gst_number,location,created_by,created_date,:modified_by,"
			+ " GETDATE(), 'UPDATE', GETDATE() FROM ORGANIZATION_MASTER where org_id = :org_id", nativeQuery = true)
	int addOrganizationHistory(String modified_by, String org_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ORGANIZATION_MASTER_HISTORY (org_id,org_name,registered_address,business_address,gst_number,location,created_by,created_date,modified_by,"
			+ "modified_date,action,transaction_date,deleted_date, deleted_by)  select org_id,org_name,registered_address,business_address,gst_number,location,created_by,created_date,modified_by,"
			+ " modified_date, 'DELETE', GETDATE(),GETDATE(),:modified_by FROM ORGANIZATION_MASTER where org_id = :org_id", nativeQuery = true)
	int deleteOrganizationHistory(String modified_by, String org_id);

	/* ORGANIZATION END */
	/* BANK START */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BANK_MASTER (business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date, factory_id) "
			+ "values (:business_unit_id,:bank_name,:account_number,:ifsc_code,:branch_address,:state_id,:country_id,:city,:swift_code,:branch_code,:created_by,GETDATE(),:factory_id)", nativeQuery = true)
	int addBankAccount(String business_unit_id, String bank_name, String account_number, String ifsc_code,
			String branch_address, String state_id, String country_id, String city, String swift_code,
			String branch_code, String created_by, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE BANK_MASTER SET business_unit_id = :business_unit_id, bank_name = :bank_name, account_number=:account_number,"
			+ "ifsc_code = :ifsc_code, branch_address =:branch_address,state_id=:state_id,country_id = :country_id, city =:city,"
			+ "swift_code = :swift_code,branch_code=:branch_code,modified_by=:modified_by, modified_date = GETDATE() WHERE account_id = :account_id", nativeQuery = true)
	int updateBankAccount(String business_unit_id, String bank_name, String account_number, String ifsc_code,
			String branch_address, String state_id, String country_id, String city, String swift_code,
			String branch_code, String modified_by, String account_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE BANK_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE  account_id = :account_id", nativeQuery = true)
	int deleteBankAccount(String modified_by, String account_id);

	@Query(value = "SELECT bm.*, s.state_name, c.country_name FROM BANK_MASTER bm JOIN STATE_MASTER s on bm.state_id = s.id "
			+ " join COUNTRY_MASTER as c on bm.country_id = c.id WHERE bm.is_delete = 0 and bm.factory_id = :factory_id ", nativeQuery = true)
	List<BankMasterInterface> getBankAccountList(String factory_id);

	@Query(value = "SELECT bm.*, s.state_name, c.country_name FROM BANK_MASTER bm JOIN STATE_MASTER s on bm.state_id = s.id join COUNTRY_MASTER as c on bm.country_id = c.id WHERE bm.account_id = :account_id ", nativeQuery = true)
	BankMasterInterface searchBankAccountById(String account_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BANK_HISTORY_MASTER (account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,modified_by,modified_date,action,transaction_date) select account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from BANK_MASTER where account_id = :account_id", nativeQuery = true)
	int InsterBankAccountHistory(String modified_by, String account_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BANK_HISTORY_MASTER (account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,modified_by,modified_date,action,transaction_date,deletd_by,deleted_date) select account_id,business_unit_id,bank_name,account_number,ifsc_code,branch_address,state_id,country_id,city,swift_code,branch_code,created_by,created_date,modified_by,modified_date,'delete',GETDATE(),:modified_by,GETDATE() from BANK_MASTER where account_id = :account_id", nativeQuery = true)
	int updateBankAccountHistory(String modified_by, String account_id);

	/* BANK END */
	/* BUSINESS UINT START */
	@Transactional
	@Modifying
	@Query(value = "INSERT into business_units(org_id,business_unit_name,gst_number,location,state_id,created_by,created_date,bu_code, factory_id)"
			+ "VALUES(:org_id,:business_unit_name,:gst_number,:location,:state_id,:created_by,GETDATE(),:value, :factory_id)", nativeQuery = true)
	int addBusinessUnits(String org_id, String business_unit_name, String gst_number, String location, int state_id,
			String created_by, int value, String factory_id);

	@Query(value = "select bu.*,sm.state_name from business_units bu inner join STATE_MASTER sm on sm.id = bu.state_id where bu.factory_id =:factory_id and bu.is_delete= 0", nativeQuery = true)
	List<BusinessUnitInterface> getAllBussinessUnits(String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE business_units  SET org_id=:org_id ,business_unit_name = :business_unit_name, gst_number = :gst_number, location = :location,state_id =:state_id, modified_by = :modified_by, modified_date= GETDATE() WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int updatebusinessUnits(String org_id, String business_unit_name, String gst_number, String location, int state_id,
			String modified_by, String business_unit_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE business_units  SET org_id=:org_id ,business_unit_name = :business_unit_name, location = :location,state_id =:state_id, modified_by = :modified_by, modified_date= GETDATE() WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int updatebusinessUnitswithoutGstNumberColumn(String org_id, String business_unit_name, String location,
			int state_id, String modified_by, String business_unit_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE business_units  SET  modified_by=:modified_by, modified_date=GETDATE() , is_delete = 1 WHERE business_unit_id=:business_unit_id", nativeQuery = true)
	int deletebusinessUnits(String modified_by, String business_unit_id);

	@Query(value = "SELECT b.business_unit_id, b.business_unit_name,b.gst_number, b.location,b.org_id,b.state_id, b.factory_id FROM business_units b  WHERE b.business_unit_id = :business_unit_id", nativeQuery = true)
	BusinessUnitInterface findBusinessUnit(String business_unit_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BUSINESS_UNITS_HISTORY (business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date, modified_by, modified_date, action, transaction_date) "
			+ "SELECT business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE() "
			+ "FROM business_units WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int insertRecordBusinessUnitHistory(String business_unit_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO BUSINESS_UNITS_HISTORY (business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date,modified_by,modified_date, deleted_by, deleted_date, action, transaction_date) "
			+ "SELECT business_unit_id, org_id, business_unit_name, gst_number, location, created_by, created_date, modified_by,  modified_date ,:modified_by, GETDATE(), 'DELETE', GETDATE() "
			+ "FROM business_units WHERE business_unit_id = :business_unit_id", nativeQuery = true)
	int insertRecordBusinessDeleteUnitHistory(String business_unit_id, String modified_by);

	@Query(value = "select Count(*) from BUSINESS_UNITS", nativeQuery = true)
	int getCount();

	@Query(value = "select count(*) from CONTRACT_MASTER where regd_office_id = :org_id and is_locked = 1", nativeQuery = true)
	int checkOrgIdPresentInContractMaster(String org_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where bid = :bid and is_locked = 1", nativeQuery = true)
	int checkBusinessIdPresentInContractMaster(String bid);

	@Query(value = "select count(*) from CONTRACT_MASTER where bank_details_id = :bank_details_id and is_locked = 1", nativeQuery = true)
	int checkBankIdPresentInContractMaster(String bank_details_id);

}
