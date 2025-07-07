package com.suveechi.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.QSAdavancePackingNote;
import com.suveechi.integration.interfaces.Advance_QSPacking_QSPackingItem_LIST_INTERFACES;
import com.suveechi.integration.interfaces.InvoiceMasterInterface;
import com.suveechi.integration.interfaces.QSAdvancePackingInterfaces;
import com.suveechi.integration.interfaces.QSAdvancePackingItemsInterfaces;
import com.suveechi.integration.interfaces.QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES;

import jakarta.transaction.Transactional;

public interface QSAdvancePackingRepository extends JpaRepository<QSAdavancePackingNote, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSADVANCE_PACKINGNOTEITEM_MASTER (qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE())",nativeQuery = true)
	int insertQSAdvancePackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id);

	

	@Transactional
	@Modifying
	@Query(value = "UPDATE QSADVANCE_PACKINGNOTEITEM_MASTER SET qty = :qty,per_kgs = :per_kgs,unit_price = :unit_price ,total = :total,"
			+ " UOM_id = :UOM_id,type_id = :type_id,pices = :pices,modified_by = :modified_by, pn_id = :pn_id, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int updateQSAdvancePackingItemsRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,String type_id, String pices, String modified_by,String pn_id, String slno);


	@Transactional
	@Modifying
	@Query(value =  "update QSADVANCE_PACKINGNOTE_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteQSAdvancePackingMasterRecord(String pn_id, String modified_by);
	
	
	@Transactional
	@Modifying
	@Query(value =  "update QSADVANCE_PACKINGNOTEITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteQSAdvancePackingItemMasterRecord(String pn_id, String modified_by);
	
	
	
	@Query(value = "select qm.*,mm.milestone_name,cm.contract_name,mm.milestone_code from QSADVANCE_PACKINGNOTE_MASTER qm \r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = qm.contract_id\r\n"
			+ "where qm.is_delete=0 and qm.factory_id = :factory_id", nativeQuery = true)
	List<QSAdvancePackingInterfaces> listQSAdvancePAckingMasterRecord(String factory_id);
	
	
	@Query(value = "select qim.*, qim.uom_id as unit_id from QSADVANCE_PACKINGNOTEITEM_MASTER qim where is_delete=0 and factory_id = :factory_id", nativeQuery = true)
	List<QSAdvancePackingItemsInterfaces> listQSAdvancePAckingItemMasterRecord(String factory_id);

	
	
	@Query(value = "select qm.pn_id,qm.contract_id,qm.filepath,qim.qty, qim.per_kgs, qim.unit_price, qim.total,qim.pices as pice,qim.slno,\r\n"
			+ "uom.unit_name, sm.scrap_name,uom.unit_id,sm.type_id, mm.milestone_name,mm.milestone_id from QSADVANCE_PACKINGNOTE_MASTER qm\r\n"
			+ "inner join QSADVANCE_PACKINGNOTEITEM_MASTER qim on qim.pn_id = qm.pn_id\r\n"
			+ "inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id\r\n"
			+ "inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id\r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id where qim.pn_id =  :pn_id and qim.is_delete = 0 and qm.is_delete = 0", nativeQuery = true)
List<QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES> searchQSAdvancePackingById(String pn_id);


@Query(value = "select * from QSADVANCE_PACKINGNOTEITEM_MASTER where is_delete=0 where slno = :slno", nativeQuery = true)
QSAdvancePackingItemsInterfaces searchQSAdvancePackingItemById(String slno);


@Modifying
@Transactional
@Query(value=" INSERT INTO ADVANCEINVOICE_MASTER(contract_id, load_id, contract_slno , product_desc, remarks, date_of_notification, date_val, bg_type,	date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,contract_name,pn_id,created_by,created_date)"
		+ "VALUES (:contract_id, :load_id, :contract_slno , :product_desc, :remarks, :date_of_notification, :date_val, :bg_type,	:date_of_issue, :reference_no, :lc_number,:supply_place, :s_t_exempted, :lr_docketno, :bg_no, :date_of_expiry, :date_of_ref, :lc_issue_date, :contract_name, :pn_id, :created_by, GETDATE())", nativeQuery = true)
int insertAdvanceInvoice(int contract_id, String load_id, int contract_slno , String product_desc, String remarks,
		String date_of_notification, String date_val, String bg_type, String date_of_issue, String reference_no,
		String lc_number, String supply_place, String s_t_exempted, String lr_docketno, String bg_no,
		String date_of_expiry, String date_of_ref, String lc_issue_date, String contract_name, String pn_id,
		String created_by);


@Modifying
@Transactional
@Query(value="update ADVANCEINVOICE_MASTER SET contract_id = :contract_id, load_id = :load_id,contract_slno  = :contract_slno ,product_desc = :product_desc,remarks = :remarks,date_of_notification = :date_of_notification,\r\n"
		+ "date_val = :date_val,bg_type = :bg_type,date_of_issue = :date_of_issue,reference_no =  :reference_no,lc_number = :lc_number,supply_place = :supply_place,\r\n"
		+ "s_t_exempted = :s_t_exempted,lr_docketno = :lr_docketno,bg_no =  :bg_no,date_of_expiry = :date_of_expiry,date_of_ref = :date_of_ref,lc_issue_date = :lc_issue_date,\r\n"
		+ "contract_name = :contract_name,pn_id = :pn_id, modified_by = :modified_by, modified_date = GETDATE() where id = :id", nativeQuery = true)
int updateAdvanceInvoice(int contract_id, String load_id, int contract_slno , String product_desc, String remarks,
		String date_of_notification, String date_val, String bg_type, String date_of_issue, String reference_no,
		String lc_number, String supply_place, String s_t_exempted, String lr_docketno, String bg_no,
		String date_of_expiry, String date_of_ref, String lc_issue_date, String contract_name, String pn_id,
		String modified_by, int id);


//@Query(value="", nativeQuery = true)
//List<String> getLastId(int con_id);


@Query(value = "select * from ADVANCEINVOICE_MASTER WHERE is_delete = 0", nativeQuery = true)
List<InvoiceMasterInterface> listAdvanceInvoiceMasterInfo();


@Query(value=" select count(*) from ADVANCEINVOICE_MASTER ", nativeQuery =  true)
int getRowCount();

@Transactional
@Modifying
@Query(value=" update  ADVANCEINVOICE_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where id = :id", nativeQuery =  true)
int deleteAdvanceInvoice(int id, String modified_by);


@Query(value=" select pn_id from ADVANCEINVOICE_MASTER where id = :id", nativeQuery =  true)
int getPnId(int id);


@Query(value = "select * from ADVANCEINVOICE_MASTER where id = :id", nativeQuery =  true)
InvoiceMasterInterface getInfo(int id);


@Query(value = "select qm.*,qpm.slno,qpm.UOM_id,qpm.qty,qpm.per_kgs,qpm.unit_price,qpm.total,qpm.type_id,qpm.pices from QSADVANCE_PACKINGNOTE_MASTER qm\r\n"
		+ "inner join QSADVANCE_PACKINGNOTEITEM_MASTER qpm on qpm.pn_id = qm.pn_id where qm.pn_id = 1", nativeQuery = true)
List<Advance_QSPacking_QSPackingItem_LIST_INTERFACES> getAdvancePackingAndItemNote(String pn_id);


@Query(value = "select invoice_series from SERIES_MASTER where is_advance =1 and (status is NULL or status ='Open') and \r\n"
		+ "state_id = (select bu.state_id from ADVANCEINVOICE_MASTER im\r\n"
		+ "inner join CONTRACT_MASTER cm on cm.con_slno = im.contract_slno\r\n"
		+ "inner join business_units bu on bu.business_unit_id = cm.regd_office_id\r\n"
		+ "inner join STATE_MASTER sm on sm.id = bu.state_id where im.id = :id)", nativeQuery = true)
Optional<Long> getAdvanceSeriesNumberbasedOnId(int id);

@Query(value = "SELECT MAX(invoice_no) AS max_invoice_no FROM ADVANCEINVOICE_MASTER", nativeQuery = true)
Optional<Long> getInvoiceNumber();


@Transactional
@Modifying
@Query(value = " UPDATE ADVANCEINVOICE_MASTER set verified_status =1, verified_by =:verified_by ,invoice_no = :invoice_no ,verified_date = GETDATE() where id  = :id", nativeQuery = true)
int updateAdvanceInvoiceVerification(String verified_by, String invoice_no, int id);

}
