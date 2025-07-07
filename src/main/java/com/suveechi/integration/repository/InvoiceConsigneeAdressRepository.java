
package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.InvoiceConsigneeAddressInterface;

import jakarta.transaction.Transactional;

public interface InvoiceConsigneeAdressRepository extends JpaRepository<User, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT into INVOICE_CONSIGNEE_ADDRESS_MASTER (address, city, district, state_id, country_id, pin_no, created_by, created_date, is_invoice, is_consignee,code,gst_no,pan_no, factory_id) VALUES (:address, :city, :district, :state_id, :country_id, :pin_no, :created_by, GETDATE(),:is_invoice, :is_consignee, :count,:gst_no,:pan_no, :factory_id)", nativeQuery = true)
	int addInvoiceAddress(String address, String city, String district, String state_id, String country_id,
			String pin_no, String created_by, String is_invoice, String is_consignee, int count, String gst_no,
			String pan_no, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER set address = :address, city = :city, district = :district, state_id = :state_id, country_id = :country_id, modified_by = :modified_by, modified_date = GETDATE(), is_invoice = :is_invoice, is_consignee = :is_consignee,gst_no = :gst_no, pan_no =:pan_no,pin_no = :pin_no  where id = :address_id ", nativeQuery = true)
	int updateAddressRecord(String address, String city, String district, String state_id, String country_id,
			String modified_by, String is_invoice, String is_consignee, String gst_no, String pan_no, String pin_no,
			String address_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_CONSIGNEE_ADDRESS_MASTER_HISTORY (address_id, address, city, district, \r\n"
			+ "state_id,country_id, is_invoice, is_consignee, modified_by, modified_date, transaction_date, action)\r\n"
			+ "SELECT id, address, city, district, state_id, country_id, is_invoice, is_consignee, :modified_by, GETDATE(),GETDATE(), 'UPDATE' \r\n"
			+ "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER\r\n" + "WHERE id = :address_id", nativeQuery = true)
	int moveToHistoryTable(String modified_by, String address_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE INVOICE_CONSIGNEE_ADDRESS_MASTER SET is_delete = 1, modified_by = :user_id,  modified_date = GETDATE() where id = :address_id ", nativeQuery = true)
	int deleteInvConsigneeAddress(String address_id, String user_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_CONSIGNEE_ADDRESS_MASTER_HISTORY (address_id, address, city, district, \r\n"
			+ "state_id,country_id, deleted_by, deleted_date, transaction_date, action)\r\n"
			+ "SELECT id, address, city, district, state_id, country_id,:user_id, GETDATE(),GETDATE(), 'DELETE' \r\n"
			+ "FROM INVOICE_CONSIGNEE_ADDRESS_MASTER\r\n" + "WHERE id = :address_id", nativeQuery = true)
	int moveToHistoryTableBeforeDelete(String address_id, String user_id);

	@Query(value = " select am.id,address, am.city, am.district, am.state_id, sm.state_name, am.country_id, am.gst_no, am.pan_no, am.pin_no, cm.country_name,am.factory_id from  INVOICE_CONSIGNEE_ADDRESS_MASTER am\r\n"
			+ "inner join STATE_MASTER sm on sm.id = am.state_id\r\n"
			+ "inner join COUNTRY_MASTER cm on cm.id = am.country_id\r\n"
			+ "where am.is_delete = 0 and am.is_invoice = 1 and am.factory_id = :factory_id", nativeQuery = true)
	List<InvoiceConsigneeAddressInterface> getInvoiceAddressDetails(String factory_id);

	@Query(value = "select am.id,address, am.city, am.district, am.state_id,sm.state_name, am.country_id, am.gst_no, am.pan_no, am.pin_no, cm.country_name, am.factory_id from  INVOICE_CONSIGNEE_ADDRESS_MASTER am\r\n"
			+ "inner join STATE_MASTER sm on sm.id = am.state_id\r\n"
			+ "inner join COUNTRY_MASTER cm on cm.id = am.country_id\r\n"
			+ "where am.is_delete = 0 and am.is_consignee = 1 and am.factory_id = :factory_id", nativeQuery = true)
	List<InvoiceConsigneeAddressInterface> getConsigneeAddressDetails(String factory_id);

	@Query(value = " select count(*) from INVOICE_CONSIGNEE_ADDRESS_MASTER", nativeQuery = true)
	int countNumberOfRows();

	@Query(value = "select icam.id, icam.address, icam.city, icam.district, sm.state_code, sm.state_name, cm.country_code, cm.country_name,icam.pin_no,  icam.pin_no as pin_code, icam.gst_no, icam.pan_no, icam.is_consignee, icam.factory_id, icam.is_invoice from INVOICE_CONSIGNEE_ADDRESS_MASTER icam\r\n"
			+ "inner join STATE_MASTER sm on sm.id = icam.state_id\r\n"
			+ "inner join COUNTRY_MASTER cm on cm.id = icam.country_id\r\n"
			+ "where icam.id = :address_id and icam.is_delete = 0", nativeQuery = true)
	InvoiceConsigneeAddressInterface getConsigneeAddressDetailsById(String address_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where consignee_id = :address_id and is_locked = 1", nativeQuery = true)
	int checkConsigneeAddressIdPresentInContractMaster(String address_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where invoice_to_id = :address_id and is_locked = 1", nativeQuery = true)
	int checkInvoiceAddressIdPresentInContractMaster(String address_id);

}
