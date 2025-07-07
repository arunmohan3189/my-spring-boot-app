//package com.suveechi.integration.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//
//import com.suveechi.integration.entity.QSChallanPacking;
//import com.suveechi.integration.interfaces.QSPackingInterfaces;
//import com.suveechi.integration.interfaces.QSPackingItemsInterfaces;
//import com.suveechi.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
//
//import jakarta.transaction.Transactional;
//
//public interface QSChallanRepository extends JpaRepository<QSChallanPacking, Integer> {
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO QSCHALLAN_PACKINGNOTEITEM_MASTER(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id,created_date) 	VALUES (:qty,:per_kgs,:unit_price,:total,:UOM_id,:type_id,:pices,:created_by, :pn_id,GETDATE())", nativeQuery = true)
//	int insertQSChallanPackingItemRecord(String qty, String per_kgs, String unit_price, String total, String uOM_id,
//			String type_id, String pices, String created_by, String pn_id);
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	void insterQSChallanPackingMasterHistoryRecord(String modified_by, String pn_id);
//	
//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE QSPACKING_MASTER SET con_id = :con_id, load_id = :load_id, filepath = :filepath,freight = :freight, milestone_id = :milestone_id, taxexemstatus = :taxexemstatus, taxexemamount = :taxexemamount, modified_by = :modified_by, modified_date = GETDATE(), lot_no = :lot_no WHERE pn_id = :pn_id ", nativeQuery = true)
//	int updateQSChallanPackingMasterRecord(String con_id, String load_id, String filepath,String freight, String milestone_id, String taxexemstatus, String taxexemamount,String modified_by, String lot_no, String pn_id);
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	void insterQSChallanPackingItenMasterHistoryRecord(String modified_by, String slno);
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	int updateQSChallanPackingItemsRecord(String qty, String per_kgs, String unit_price, String total, String uOM_id,
//			String type_id, String pices, String modified_by, String pn_id, String slno);
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	void updateQSChallanPackingMasterHistoryRecord(String modified_by, String pn_id);
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	int delteQSChallanPackingMasterRecord(String pn_id, String modified_by);
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	void updateQSChallanPackingItemMasterHistoryRecord(String modified_by, String pn_id);
//
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	int delteQSChallanPackingItemMasterRecord(String pn_id, String modified_by);
//
//	
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO ", nativeQuery = true)
//	int delteQSChallanPackingItemRecord(String slno, String modified_by);
//
//	
//	List<QSPackingInterfaces> listQSChallanPAckingMasterRecord();
//
//	List<QSPackingItemsInterfaces> listQSChallanPAckingItemMasterRecord();
//
//	List<QSPacking_QSPackingItem_LIST_INTERFACES> searchQSChallanPackingById(String pn_id);
//
//	QSPackingItemsInterfaces searchQSChallanPackingItemById(String slno);
//
//
//
//}
