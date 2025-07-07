package com.suveechi.integration.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.Invoice;
import com.suveechi.integration.interfaces.InvoiceMasterInterface;
import com.suveechi.integration.interfaces.InvoiceTypeInterfaces;

import jakarta.transaction.Transactional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

	@Query(value="select type from INVOICE_TYPE_MASTER where is_delete = 0 ", nativeQuery = true)
	List<String> listInvoiceMaster();

	@Query(value="select servicecode_id, service_type from SERVICECODE_MASTER where service_type = 'SERVICE CODE' and factory_id =:factory_id", nativeQuery = true)
	List<String> listServiceCode(String factory_id);

	@Query(value="select servicecode_id, service_type from SERVICECODE_MASTER where service_type = 'HSN CODE' and factory_id =:factory_id", nativeQuery = true)
	List<String> listHSNCode(String factory_id);
	
	
	@Transactional
	@Modifying
	@Query(value ="update QSPACKING_ITEM_MASTER SET SType1 = :stype, HType = :hcode  modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery = true)
	int updateQSPAckingItem(String stype, String hcode, String pn_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value ="INSERT INTO INVOICE_TAXENTRY_DETAILS(tax_id,tax_per,invoice_id,advtax,baltax,recovery,tadv,tax_value,created_by,created_date)"
			+ " VALUES (:tax_id,:tax_per,:invoice_id,:advtax,:baltax,:recovery,:tadv,:tax_value,:created_by,GETDATE())", nativeQuery = true)
	void addTaxInvoiceEntry(String tax_id, String tax_per, String invoice_id, String advtax, String baltax,
			String recovery, String tadv, String tax_value,String created_by);

	
	@Transactional
	@Modifying
	@Query(value ="UPDATE INVOICE_TAXENTRY_DETAILS set tax_id = :tax_id,tax_per = :tax_per,invoice_id = :invoice_id,advtax = :advtax,baltax = :baltax,recovery = :recovery,tadv = :tadv,tax_value = :tax_value,modified_by = :modified_by,GETDATE() where slno = :slno)", nativeQuery = true)
	void updateTaxInvoiceEntry(String tax_id, float tax_per, String invoice_id, String advtax, String baltax,
			String recovery, String tadv, float tax_value,String modified_by, String slno);
	@Transactional
	@Modifying
	@Query(value ="update QSPACKING_ITEM_MASTER set SCode = :service_code_id, HCode =:hsn_code_id, inc_type = :type_id, modified_by = :created_by, modified_date =  GETDATE() where slno = :slno", nativeQuery = true)
	void addPackingNoteItems(String service_code_id, String hsn_code_id, String type_id, String created_by,String slno);

	@Query(value="select * from INVOICE_TYPE", nativeQuery = true)
	List<InvoiceTypeInterfaces> listInvoiceType();

	


//	@Transactional
//	@Modifying
//	@Query(value ="INSERT INTO INVOICE_NUMBER(contract_id, load_id, pd1, transporter, vehicle_no, LRDock_no, LRDock_date, st_exempt, notification_date, verification, verified_emp, verification_date, status, remarks, status_emp, status_date, pn_id, release, invoice_type, Rpd1, taxinex, nadv, adv, bal, recoveryamt, recovery1, tadv1, tnadv, bgtype, bgnumber, referenceno, doi, doe, gstrmk, dor, LCReferenceno, LCIssuedate, frightinex, vendorid, supplyofplace, invoicetype, vendorname, vendorinvno, vendorqty, vendorinvdate, rdate, created_by, created_date"
//			+ " VALUES (:contract_id, :load_id, :pd1, :transporter, :vehicle_no, :LRDock_no, :LRDock_date, :st_exempt, :notification_date, :verification, :verified_emp, :verification_date, :status, :remarks, :status_emp, :status_date, :pn_id, :release, :invoice_type, :Rpd1, :taxinex, :nadv, :adv, :bal, :recoveryamt, :recovery1, :tadv1, :tnadv, :bgtype, :bgnumber, :referenceno, :doi, :doe, :gstrmk, :dor, :LCReferenceno, :LCIssuedate, :frightinex, :vendorid, :supplyofplace, :invoicetype, :vendorname, :vendorinvno, :vendorqty, :vendorinvdate, :rdate, :created_by, GETDATE())", nativeQuery = true)
//	void addInvoiceInfo(String contract_id, String load_id, String pd1, String transporter, String vehicle_no,String LRDock_no, String LRDock_date, String st_exempt, String notification_date, String verification,String verified_emp, String verification_date, String status, String remarks, String status_emp,String status_date, String pn_id, String release, String invoice_type, String Rpd1, String taxinex,	String nadv, String adv, String bal, String recoveryamt, String recovery1, String tadv1, String tnadv,
//			String bgtype, String bgnumber, String referenceno, String doi, String doe, String gstrmk, String dor,
//			String lCReferenceno, String lCIssuedate, String frightinex, String vendorid, String supplyofplace,
//			String invoicetype, String vendorname, String vendorinvno, String vendorqty, String vendorinvdate,
//			String rdate, String created_by);

	@Transactional
	@Modifying
	@Query(value ="INSERT INTO INVOICE_NUMBER(con_slno,slno,product_desc,remarks,date_of_notification,date_val,bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,created_date ) "
			+ " VALUES (:con_slno,:slno,:product_desc,:remarks,:date_of_notification,:date_val,:bg_type,:date_of_issue,:reference_no,:lc_number,:supply_place,:s_t_exempted,:lr_docketno,:bg_no,:date_of_expiry,:date_of_ref,:lc_issue_date,:created_by,GETDATE())", nativeQuery = true)
	int addInvoiceNumber(String con_slno, String slno, String product_desc, String remarks,
			String date_of_notification, String date_val, String bg_type, String date_of_issue, String reference_no,
			String lc_number, String supply_place, String s_t_exempted, String lr_docketno, String bg_no,
			String date_of_expiry, String date_of_ref, String lc_issue_date,String created_by);



	@Query(value = "select im.*, qm.lot_no from INVOICE_MASTER im\r\n"
			+ "inner join QSPACKING_MASTER qm on qm.load_id = im.load_id\r\n"
			+ " WHERE im.is_delete = 0 and im.factory_id =:factory_id", nativeQuery = true)
	List<InvoiceMasterInterface> listInvoiceMasterInfo(String factory_id);

	@Query(value = "select * from INVOICE_MASTER WHERE id = :id", nativeQuery = true)
	InvoiceMasterInterface listSearchById(String id);

	@Transactional
	@Modifying
	@Query(value = "update INVOICE_MASTER SET is_delete = 1 where id = :id", nativeQuery = true)
	void listDeleteById(String id);

	@Transactional
	@Modifying
	@Query(value = " UPDATE INVOICE_MASTER set rejected_by =:rejected_by , rejected_date = GETDATE(), is_delete = 1 where id  = :id", nativeQuery = true)
	void updateRejectedById(String rejected_by, String id);

	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO INVOICE_MASTER_HISTORY (id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,qs_packing_item_slno,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,action,transcation,factory_id) "
			+ "select id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,qs_packing_item_slno,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,:modified_by,GETDATE(),verified_by,verified_date,verified_status,rejected_by,rejected_date,'UPDATE', GETDATE(),factory_id from INVOICE_MASTER where id = :id"
			+ "", nativeQuery =  true)
	void insertInvoiceHistory(String modified_by, int id);
	
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO INVOICE_MASTER_HISTORY (id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,qs_packing_item_slno,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,action,transcation,factory_id,non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt) "
			+ "select id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,qs_packing_item_slno,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,:verified_by,GETDATE(),verified_status,rejected_by,rejected_date,'VERIFICATION', GETDATE(),factory_id,non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt from INVOICE_MASTER where id = :id"
			+ "", nativeQuery =  true)
	void verificationInvoiceHistory(String verified_by, int id);
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO INVOICE_MASTER_HISTORY (id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,qs_packing_item_slno,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,action,transcation,deleted_by,deleted_date) "
			+ "select id,invoice_no,contract_id,load_id,invoice_type,contract_slno,contract_name,qs_packing_item_slno,product_desc,remarks,date_of_notification,date_val,\r\n"
			+ "bg_type,date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,created_by,\r\n"
			+ "created_date,modified_by,modified_date,verified_by,verified_date,verified_status,rejected_by,rejected_date,'REJECTED', GETDATE(),:deleted_by, GETDATE() from INVOICE_MASTER where id = :id"
			+ "", nativeQuery =  true)
	void rejectInvoiceHistory(String deleted_by, int id);

	@Query(value = "select load_id, lot_no from QSPACKING_MASTER where con_id = :con_id and factory_id = :factory_id", nativeQuery = true)
	List<Map<Integer,String>> getLoadIdQsPacking(int con_id, int factory_id);

	@Query(value = "select invoice_series from SERIES_MASTER where is_gst =1 and (status is NULL or status ='Open') and \r\n"
			+ "state_id = (select bu.state_id from INVOICE_MASTER im\r\n"
			+ "inner join CONTRACT_MASTER cm on cm.con_slno = im.contract_slno\r\n"
			+ "inner join business_units bu on bu.business_unit_id = cm.regd_office_id\r\n"
			+ "inner join STATE_MASTER sm on sm.id = bu.state_id where im.id = :id)", nativeQuery = true)
	Optional<Long> getSeriesNumberbasedOnId(int id);
	
	@Query(value = "SELECT MAX(invoice_no) AS max_invoice_no FROM INVOICE_MASTER", nativeQuery = true)
	Optional<Long> getInvoiceNumber();

	@Query(value = "select * from INVOICE_MASTER WHERE is_delete = 0 and invoice_no is NULL", nativeQuery = true)
	List<InvoiceMasterInterface> listInvoiceVerificationInfo();

	
	@Query(value="select bgid, description  from BGTYPE_MASTER where is_delete = 0", nativeQuery = true)
	List<Map<String,String>> bgTypeList();

	@Transactional
	@Modifying
	@Query(value = " UPDATE INVOICE_MASTER SET product_desc = :product_desc,remarks = :remarks,date_of_notification = :date_of_notification,\r\n"
			+ "date_val = :date_val,bg_type = :bg_type,date_of_issue = :date_of_issue,reference_no = :reference_no,\r\n"
			+ "lc_number = :lc_number,supply_place = :supply_place,lr_docketno = :lr_docketno,bg_no = :bg_no,date_of_expiry = :date_of_expiry,\r\n"
			+ "date_of_ref = :date_of_ref,lc_issue_date = :lc_issue_date,modified_by = :modified_by, modified_date = GETDATE(),s_t_exempted = :s_t_exempted where id = :id ", nativeQuery = true)
	int updateInvoiceEntryInfo(String product_desc, String remarks, String date_of_notification,
			String date_val, String bg_type, String date_of_issue, String reference_no, String lc_number,
			String supply_place, String lr_docketno, String bg_no, String date_of_expiry, String date_of_ref,
			String lc_issue_date, String modified_by,String s_t_exempted, int id);

	
	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * " UPDATE INVOICE_MASTER SET invoice_type = :invoice_type,product_desc = :product_desc,remarks = :remarks,date_of_notification = :date_of_notification,\r\n"
	 * +
	 * "date_val = :date_val,bg_type = :bg_type,date_of_issue = :date_of_issue,reference_no = :reference_no,\r\n"
	 * +
	 * "lc_number = :lc_number,supply_place = :supply_place,lr_docketno = :lr_docketno,bg_no = :bg_no,date_of_expiry = :date_of_expiry,\r\n"
	 * +
	 * "date_of_ref = :date_of_ref,lc_issue_date = :lc_issue_date,modified_by = :modified_by, modified_date = GETDATE(),load_id = :load_id,s_t_exempted = :s_t_exempted where id = :id "
	 * , nativeQuery = true) int updateInvoiceEntryInfo(String invoice_type, String
	 * product_desc, String remarks, String date_of_notification, String date_val,
	 * String bg_type, String date_of_issue, String reference_no, String lc_number,
	 * String supply_place, String lr_docketno, String bg_no, String date_of_expiry,
	 * String date_of_ref, String lc_issue_date, String modified_by,String
	 * load_id,String s_t_exempted, int id);
	 */
	@Transactional
	@Modifying
	@Query(value = " UPDATE INVOICE_MASTER set non_tax_adv = :non_tax_adv, tax_adv = :tax_adv, total =:total,payable_by_customer = :payable_by_customer, payable_to_dept = :payable_to_dept,open_tax_adv =:open_tax_adv, open_non_tax_adv = :open_non_tax_adv, recovery_amt =:recovery_amt, verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE(), is_release = 0 where id  = :id", nativeQuery = true)
	int updateInvoiceVerificationDetails(String non_tax_adv, String tax_adv, String total, String payable_by_customer,
			String payable_to_dept, String open_tax_adv, String open_non_tax_adv, String recovery_amt,
			String verified_by, String invoice_no, int id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_TAXENTRY_DETAILS(tax_id,tax_per,tax_value,adv_tax,tax_payable_by_customer,tax_payable_to_dept,t_adv,invoice_id,created_by,created_date) VALUES (:tax_id,:tax_per,:tax_value,:adv_tax,:tax_payable_by_customer,:tax_payable_to_dept,:t_adv,:invoice_id,:created_by,GETDATE())", nativeQuery = true)
	void insertINVOICE_TAXENTRY_DETAILS(String tax_id, String tax_per, String tax_value, String adv_tax,
			String tax_payable_by_customer, String tax_payable_to_dept, String t_adv, String invoice_id,String created_by);
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from INVOICE_TAXENTRY_DETAILS where invoice_id = :invoice_id", nativeQuery = true)
	void detele_INVOICE_TAXENTRY_DETAILS(String invoice_id);



	@Transactional
	@Modifying
	@Query(value ="UPDATE INVOICE_MASTER set is_release = 1,released_by =:released_by,released_datetime = GETDATE() where id = :id", nativeQuery = true)
	int UpdateReleaseById(String id, String released_by);

	@Query(value="select invoice_no from INVOICE_MASTER WHERE id = :id", nativeQuery = true)
	Optional<Long> getInvoiceValue(int id);

	@Query(value="select contract_id from INVOICE_MASTER WHERE id = :id and factory_id = :factory_id", nativeQuery = true)
	long getContractIdFromInvoiceMaster(int id, String factory_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation, deleted_by, deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, :modified_by, GETDATE(), verified_by, verified_date, verified_status, rejected_by, rejected_date,'UPDATE',  GETDATE(), deleted_by, deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistory(String id, String modified_by);

	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date,'RELEASED',GETDATE(), factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, :released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistoryisReleased(String id, String released_by);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation,deleted_by,deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date,'DELETED',GETDATE(),:deleted_by,GETDATE(),    factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release,  released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistoryisDelete(String id, String deleted_by);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO INVOICE_MASTER_HISTORY (id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, verified_by, verified_date, verified_status, rejected_by, rejected_date, action, transcation, deleted_by, deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime)"
			+ 									"select id, invoice_no, contract_id, load_id, invoice_type, contract_slno, contract_name, qs_packing_item_slno, product_desc, remarks, date_of_notification, date_val, bg_type, date_of_issue, reference_no, lc_number, supply_place, s_t_exempted, lr_docketno, bg_no, date_of_expiry, date_of_ref, lc_issue_date, created_by, created_date, modified_by, modified_date, :verified_by, GETDATE(), verified_status, rejected_by, rejected_date,'VERIFIED',GETDATE(),   deleted_by, deleted_date, factory_id, non_tax_adv, tax_adv, total, payable_by_customer, payable_to_dept, open_tax_adv, open_non_tax_adv, recovery_amt, is_release, released_by, released_datetime from INVOICE_MASTER where id = :id", nativeQuery = true)
	int insertIntoInvoiceHistoryisVerified(String id, String verified_by);
	//List<InvoiceMasterInterface> listSearchById(String id); 2910800066

	@Query(value = "delete from INVOICE_MASTER WHERE id = :id", nativeQuery = true)
	void deleteInvoiceMasterBasedOnId(String id);
}
