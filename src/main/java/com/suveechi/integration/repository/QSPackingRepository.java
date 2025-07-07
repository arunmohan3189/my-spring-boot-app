package com.suveechi.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.suveechi.integration.entity.QSPacking;
import com.suveechi.integration.interfaces.QSPackingInterfaces;
import com.suveechi.integration.interfaces.QSPackingItemsInterfaces;
import com.suveechi.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;

import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;

public interface QSPackingRepository extends JpaRepository<QSPacking, Integer>{
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_MASTER (con_id,load_id,filepath,transport_name,vechile_no,freight,milestone_id,taxexemstatus,taxexemamount,created_by,created_date) "
			+ " VALUES (:con_id,:load_id,:filepath,:transport_name,:vechile_no,:freight,:milestone_id,:taxexemstatus,:taxexemamount,:created_by, GETDATE())", nativeQuery =  true)
	@QueryHints({ @QueryHint(name = "javax.persistence.query.returnGeneratedKeys", value = "true") })
	int insertQSPackingMasterRecord(String con_id, String load_id, String filepath,
			String transport_name, String vechile_no, String freight, String milestone_id, String taxexemstatus,
			String taxexemamount, String created_by);
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE QSPACKING_MASTER SET con_id = :con_id, load_id = :load_id, filepath = :filepath, transport_name = :transport_name,"
			+ " vechile_no = :vechile_no, freight = :freight, milestone_id = :milestone_id, taxexemstatus = :taxexemstatus, taxexemamount = :taxexemamount,"
			+ " modified_by = :modified_by, modified_date = GETDATE(), lot_no = :lot_no WHERE pn_id = :pn_id", nativeQuery =  true)
	int updateQSPackingMasterRecord(String con_id, String load_id,String filepath,
			String transport_name, String vechile_no, String freight, String milestone_id, String taxexemstatus,
			String taxexemamount, String modified_by,String lot_no, String pn_id);


	@Transactional
	@Modifying
	@Query(value =  "update QSPACKING_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteQsPackingMasterRecord(String pn_id, String modified_by);


	@Query(value = "select qm.*,mm.milestone_name,cm.contract_name,mm.milestone_code from QSPACKING_MASTER qm \r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id \r\n"
			+ "inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id\r\n"
			+ "where qm.is_delete=0 and qm.factory_id =:factory_id", nativeQuery = true)
	List<QSPackingInterfaces> listQSPAckingMasterRecord(String factory_id);


		@Query(value = "select distinct qm.pn_id,qm.con_id,qm.load_id,qm.filepath, qm.vechile_no,qm.freight,qm.taxexemamount,\r\n"
				+ "qm.lot_no,cm.contract_name,\r\n"
				+ " qim.qty, qim.per_kgs, qim.unit_price, qim.total,qm.transport_name,\r\n"
				+ " qim.pices as pice, qim.HCode, qim.SCode,qim.slno,qim.inc_type\r\n"
				+ " , uom.unit_name, sm.scrap_name,uom.unit_id,sm.type_id,\r\n"
				+ " mm.milestone_name,mm.milestone_id, qimoj.inc_type\r\n"
				+ " from QSPACKING_MASTER qm\r\n"
				+ "inner join QSPACKING_ITEM_MASTER qim on qim.pn_id = qm.pn_id\r\n"
				+ "left outer join QSPACKING_ITEM_MASTER qimoj on qimoj.pn_id = qm.pn_id\r\n"
				+ "inner join UOM_MASTER uom on uom.unit_id = qim.UOM_id\r\n"
				+ "inner join SCRAPTYPE_MASTER sm on sm.type_id = qim.type_id\r\n"
				+ "inner join CONTRACT_MASTER cm on cm.contract_id = qm.con_id\r\n"
				+ "inner join MILESTONE_MASTER mm on mm.milestone_id = qm.milestone_id where qim.pn_id =  :pn_id and qim.is_delete = 0 and qm.is_delete = 0", nativeQuery = true)
	List<QSPacking_QSPackingItem_LIST_INTERFACES> searchQSPackingById(String pn_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_MASTER_HISTORY ( pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestoneName,taxexemstatus,taxexemamount,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id,lot_no,is_locked)\r\n"
			+ "select pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestone_id,taxexemstatus,taxexemamount,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),factory_id,lot_no,is_locked from QSPACKING_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int insterQSPackingMasterHistoryRecord(String modified_by, String pn_id);


	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_MASTER_HISTORY (pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestoneName,taxexemstatus,taxexemamount,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,lot_no)\r\n"
			+ "select pn_id,con_id,load_id, filepath,transport_name,vechile_no, freight,milestone_id,taxexemstatus,taxexemamount,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,lot_no from QSPACKING_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int updateIsDeleteQSPackingMasterHistoryRecord(String modified_by, String pn_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date,factory_id) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE(),:factory_id)",nativeQuery = true)
	int insertQSPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id,int factory_id);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date) "
			+ " VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE())",nativeQuery = true)
	int insertQSPackingItemRecordWIthoutFactory(String qty, String per_kgs, String unit_price, String total, String UOM_id,
			String type_id, String pices, String created_by,String pn_id);


	@Transactional
	@Modifying
	@Query(value = "UPDATE QSPACKING_ITEM_MASTER SET qty = :qty,per_kgs = :per_kgs,unit_price = :unit_price ,total = :total,"
			+ " UOM_id = :UOM_id,type_id = :type_id,pices = :pices,modified_by = :modified_by, pn_id = :pn_id, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int updateQsPackingItemsRecord(String qty, String per_kgs, String unit_price, String total, String UOM_id,String type_id, String pices, String modified_by,String pn_id, String slno);


	@Transactional
	@Modifying
	@Query(value =  "update QSPACKING_ITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where pn_id = :pn_id", nativeQuery =  true)
	int delteQsPackingItemMasterRecord(String pn_id, String modified_by);

	@Query(value = "select qim.*, qim.uom_id as unit_id from QSPACKING_ITEM_MASTER qim where is_delete=0   and factory_id =:factory_id", nativeQuery = true)
	List<QSPackingItemsInterfaces> listQSPAckingItemMasterRecord(String factory_id);

	
	@Query(value = "select * from QSPACKING_ITEM_MASTER where is_delete=0 where slno = :slno", nativeQuery = true)
	QSPackingItemsInterfaces searchQSPackingItemById(String slno);



	@Transactional
	@Modifying
	@Query(value =  "update QSPACKING_ITEM_MASTER SET is_delete = 1, modified_by =:modified_by, modified_date = GETDATE() where slno = :slno", nativeQuery =  true)
	int delteQsPackingItemRecord(String slno, String modified_by);

	@Query(value="select unit_name from UOM_MASTER where is_delete = 0", nativeQuery = true)
	List<String> listUOM();
	
	@Query(value="select scrap_name from SCRAPTYPE_MASTER where is_delete = 0  and factory_id =:factory_id", nativeQuery = true)
	List<String> listScrapType(String factory_id);

	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY ( slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date)\r\n"
			+ "select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int insterQSPackingItenMasterHistoryRecord(String modified_by, String slno);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'UPDATE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where pn_id = :pn_id", nativeQuery =  true)
	int updateQSPackingItemMasterHistoryRecord(String modified_by, String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int IsDeleteQSPackingItemMasterHistoryRecord(String modified_by, String slno);


	@Transactional
	@Modifying
	@Query(value = "INSERT INTO QSPACKING_ITEM_MASTER_HISTORY (slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id,is_locked,inc_type) select slno,pn_id,qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),factory_id,is_locked,inc_type from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery =  true)
	int updateQSPackingItemMasterHistoryRecordSlno(String modified_by, String slno);
	
	@Query(value ="select TOP 1 pn_id from QSPACKING_ITEM_MASTER where slno = :slno", nativeQuery = true)
	Optional<Integer> getPn_id(String slno);

	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_MASTER SET   is_delete = 1 where pn_id = :pn_id", nativeQuery = true)
	int invoiceQSDelete(int pn_id);


	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_ITEM_MASTER SET   is_delete = 1 where pn_id = :pn_id", nativeQuery = true)
	int invoiceQSItemDelete(int pn_id);
	
	
	@Query(value="select TOP 1 pn_id from QSPACKING_MASTER where con_id = :con_id and load_id = :load_id and factory_id = :factory_id", nativeQuery =  true)
	String getPnIdBasedOnContractandLoad_id(String con_id, String load_id, String factory_id);
	
	@Query(value="select TOP 1 pn_id from QSPACKING_MASTER where con_id = :con_id and load_id = :load_id ", nativeQuery =  true)
	String getPnIdBasedOnContract_id(String con_id,  String load_id);


	@Query(value="select filepath from QSPACKING_MASTER where pn_id = :valuePnid", nativeQuery = true)
	String getFilePath(int valuePnid);


	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_MASTER SET   is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinQSPacking(int pn_id, String locked);


	@Transactional
	@Modifying
	@Query(value="UPDATE QSPACKING_ITEM_MASTER SET  is_locked = :locked where pn_id = :pn_id", nativeQuery = true)
	void updateisLockinQSPackingItem(int pn_id, String locked );


	@Query(value="select count(*) from QSPACKING_MASTER where pn_id = :pn_id and is_locked = 1", nativeQuery = true)
	int checkIsLocked(String pn_id);
	
	
	@Transactional
	@Modifying
	@Query(value="delete from QSPACKING_ITEM_MASTER where pn_id = :pn_id", nativeQuery = true)
	void deleteQSPackingItemBasedOnPnId(String pn_id);
	
	@Transactional
	@Modifying
	@Query(value="delete from QSPACKING_MASTER where pn_id = :pn_id", nativeQuery = true)
	void deleteQSPackingBasedOnPnId(String pn_id);
}
