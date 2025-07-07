package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.GSTStateMasterInterface;
import com.suveechi.integration.interfaces.MileStoneMasterInterface;
import com.suveechi.integration.interfaces.OtherTypeMasterInterface;
import com.suveechi.integration.interfaces.ServiceCodeMasterInterface;
import com.suveechi.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.suveechi.integration.interfaces.TypeMasterInterface;
import com.suveechi.integration.interfaces.UOMMasterInterface;
import com.suveechi.integration.interfaces.VendorForScrapMasterInterface;
import com.suveechi.integration.interfaces.WorkOrderMasterInterface;

import jakarta.transaction.Transactional;

public interface MasterRepository extends JpaRepository<User, Integer> {

	/* GST MASTER -> START */
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO GST_MASTER (sap_bucode, state_code, state_gstno, created_by, created_date,factory_id) VALUES (:sapBuCode, :stateCode, :stateGstNo, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertGSTMasterRecord(String sapBuCode, String stateCode, String stateGstNo, String createdBy, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE GST_MASTER SET sap_bucode = :sapBuCode, state_code = :stateCode, state_gstno = :stateGstNo, modified_by = :modifiedBy, modified_date = GETDATE() WHERE bu_id = :buId", nativeQuery = true)
	int updateGSTMasterRecord(String sapBuCode, String stateCode, String stateGstNo, String modifiedBy, String buId);

	@Query(value = "SELECT * FROM GST_MASTER WHERE is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<GSTStateMasterInterface> listGSTMasterRecord(String factory_id);
	

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO GST_MASTER_HISTORY (bu_id, sap_bucode, state_code, state_gstno, created_by, created_date, modified_by, modified_date, action, transaction_date,factory_id) " 
	        + "SELECT bu_id, sap_bucode, state_code, state_gstno, created_by, created_date, :modifiedBy, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM GST_MASTER WHERE bu_id = :buId", nativeQuery = true)
	int insertGSTMasterToHistory( String buId,String modifiedBy);


	@Modifying
    @Transactional
    @Query(value = "INSERT INTO GST_MASTER_HISTORY (bu_id, sap_bucode, state_code, state_gstno, created_by, created_date, modified_by, modified_date, action,transaction_date, deleted_date, deleted_by) " 
            + "SELECT bu_id, sap_bucode, state_code, state_gstno, created_by, created_date, modified_by, modified_date, 'DELETE', GETDATE(), GETDATE(), :modified_by " 
            + "FROM GST_MASTER " 
            + "WHERE bu_id = :bu_id", nativeQuery = true)
    int deleteGSTMasterToHistory(String modified_by, String bu_id);


	@Query(value="select * from GST_MASTER where bu_id = :buId and is_delete = 0", nativeQuery = true)
	GSTStateMasterInterface searchGSTById(String buId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE GST_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE bu_id = :buId", nativeQuery = true)
	int deleteGSTMasterRecord(String modifiedBy,String buId);
	/* GST MASTER -> END */

	/* MILESTONE MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO MILESTONE_MASTER (milestone_code, milestone_name, milestone_desc, created_by, created_date,factory_id) VALUES (:milestoneCode, :milestoneName, :milestoneDesc, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertMilestoneRecord(String milestoneCode, String milestoneName, String milestoneDesc, String createdBy, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE MILESTONE_MASTER SET milestone_code = :milestoneCode, milestone_name = :milestoneName, milestone_desc = :milestoneDesc, modified_by = :modifiedBy, modified_date = GETDATE() WHERE milestone_id = :milestoneId", nativeQuery = true)
	int updateMilestoneRecord(String milestoneCode, String milestoneName, String milestoneDesc, String modifiedBy,
			String milestoneId);

	@Query(value = "SELECT * FROM MILESTONE_MASTER where is_delete = 0  and factory_id = :factory_id", nativeQuery = true)
	List<MileStoneMasterInterface> listMilestoneRecords(String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE MILESTONE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE milestone_id = :milestoneId", nativeQuery = true)
	int deleteMilestoneRecord(String modifiedBy,String milestoneId);

	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO MILESTONE_MASTER_HISTORY (milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, modified_by, modified_date, statuses, action,transaction_date,factory_id)\r\n"
			+ "SELECT milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, :modifiedBy, GETDATE(), statuses, 'UPDATE', GETDATE(),factory_id FROM MILESTONE_MASTER WHERE milestone_id = :milestoneId;", nativeQuery = true)
	int insertMileStoneToHistory(String milestoneId,String modifiedBy);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO MILESTONE_MASTER_HISTORY (milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, modified_by, modified_date, statuses, action,transaction_date,deleted_date,deleted_by)\r\n"
			+ "SELECT milestone_id, milestone_code, milestone_name, milestone_desc, created_by, created_date, modified_by, modified_date, statuses, 'DELETE',GETDATE(),GETDATE(), :modifiedBy \r\n"
			+ "FROM MILESTONE_MASTER\r\n"
			+ "WHERE milestone_id = :milestoneId;", nativeQuery = true)
	int deleteMileStoneToHistory(String milestoneId,String modifiedBy);
	
	@Query(value="select * from MILESTONE_MASTER where milestone_id = :milestoneId  and is_delete = 0", nativeQuery = true)
	MileStoneMasterInterface searchMileStoneById(String milestoneId);
	/* MILESTONE MASTER -> END */

	/* OTHERTYPE MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO OTHERTYPE_MASTER (type, created_by, created_date,factory_id) VALUES (:type, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertOtherType(String type, String createdBy, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE OTHERTYPE_MASTER SET type = :type, modified_by = :modifiedBy, modified_date = GETDATE() WHERE othertype_id = :othertypeId", nativeQuery = true)
	int updateOtherType(String type, String modifiedBy, String othertypeId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO OTHERTYPE_MASTER_HISTORY (othertype_id, type, created_by, created_date, modified_by, modified_date, action,transaction_date,factory_id)\r\n"
			+ "SELECT othertype_id, type, created_by, created_date, :modifiedBy, GETDATE(), 'UPDATE',GETDATE(),factory_id FROM OTHERTYPE_MASTER WHERE othertype_id = :othertypeId", nativeQuery = true)
	int insertOtherTypeToHistory(String othertypeId,String modifiedBy);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO OTHERTYPE_MASTER_HISTORY (othertype_id, type, created_by, created_date, modified_by, modified_date, action,transaction_date,deleted_date,deleted_by)\r\n"
			+ "SELECT othertype_id, type, created_by, created_date, modified_by, modified_date, 'DELETE',GETDATE(),GETDATE(),:modifiedBy \r\n"
			+ " FROM OTHERTYPE_MASTER\r\n"
			+ "WHERE othertype_id = :othertypeId", nativeQuery = true)
	int deleteOtherTypeToHistory(String othertypeId,String modifiedBy);


	@Query(value = "SELECT * FROM OTHERTYPE_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<OtherTypeMasterInterface> listOtherTypes(String factory_id);
	
	@Query(value="select * from OTHERTYPE_MASTER where othertype_id = :otherTypeId  and is_delete = 0", nativeQuery = true)
	OtherTypeMasterInterface searchOtherTypeById(String otherTypeId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE OTHERTYPE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE othertype_id = :othertypeId", nativeQuery = true)
	int deleteOtherType(String modifiedBy,String othertypeId);

	/* OTHERTYPE MASTER -> END */

	/* SERVICECODE MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERVICECODE_MASTER (service_type, service_description, service_code, created_by, created_date,is_active,status,factory_id) VALUES (:serviceType, :serviceDescription, :serviceCode, :createdBy, GETDATE(), 1,:status,:factory_id)", nativeQuery = true)
	int insertServiceCodeRecord(String serviceType, String serviceDescription, String serviceCode, String createdBy, String status, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERVICECODE_MASTER SET service_type = :serviceType, service_description = :serviceDescription, service_code = :serviceCode, modified_by = :modifiedBy, modified_date = GETDATE(),status = :status WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int updateServiceCodeRecord(String serviceType, String serviceDescription, String serviceCode,String modifiedBy, String status,String servicecodeId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERVICECODE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_active = 0 WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int updateServiceCodeInActiveRecord(String modifiedBy, String servicecodeId);

	
	@Query(value = "SELECT * FROM SERVICECODE_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<ServiceCodeMasterInterface> listServiceCodeRecords(String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERVICECODE_MASTER_HISTORY (servicecode_id, service_type, service_description, service_code, status, created_by, created_date, modified_by, modified_date, action,transaction_date,factory_id)\r\n"
			+ "SELECT servicecode_id, service_type, service_description, service_code, status, created_by, created_date, :modifiedBy, GETDATE(), 'UPDATE',GETDATE(),factory_id FROM SERVICECODE_MASTER WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int insertServiceCodeToHistory(String servicecodeId,String modifiedBy);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERVICECODE_MASTER_HISTORY (servicecode_id, service_type, service_description, service_code, status, created_by, created_date, modified_by, modified_date, action,transaction_date,deleted_date,deleted_by)\r\n"
			+ "SELECT servicecode_id, service_type, service_description, service_code, status, created_by, created_date, modified_by, modified_date, 'UPDATE',GETDATE(),GETDATE(),:modifiedBy\r\n"
			+ "FROM SERVICECODE_MASTER\r\n"
			+ "WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int deleteServiceCodeToHistory(String servicecodeId,String modifiedBy);

	
	@Modifying
	@Transactional
	@Query(value = "UPDATE SERVICECODE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE servicecode_id = :servicecodeId", nativeQuery = true)
	int deleteServiceCodeRecord(String modifiedBy,String servicecodeId);
	
	@Query(value = "SELECT * FROM SERVICECODE_MASTER where servicecode_id = :servicecodeId and is_delete = 0", nativeQuery = true)
	ServiceCodeMasterInterface searchServiceById(String servicecodeId);



	/* SERVICECODE MASTER -> END */

	/* TYPEMASTER -> START */
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPTYPE_MASTER (scrap_type, scrap_name, created_by, created_date,factory_id) VALUES (:scrapType, :scrapName, :createdBy, GETDATE(), :factory_id)", nativeQuery = true)
	int insertScrapTypeRecord(String scrapType, String scrapName, String createdBy, String factory_id);

	@Modifying
	@Transactional	
	@Query(value = "UPDATE SCRAPTYPE_MASTER SET scrap_type = :scrapType, scrap_name = :scrapName, modified_by = :modifiedBy, modified_date = GETDATE() WHERE type_id = :typeId", nativeQuery = true)
	int updateScrapTypeRecord(String scrapType, String scrapName, String modifiedBy, String typeId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPTYPE_MASTER SET modified_by = :modifiedBy, modified_date = GETDATE(), is_delete = 1 WHERE type_id = :typeId", nativeQuery = true)
	int deleteScrapTypeRecord(String modifiedBy,String typeId);
	@Query(value = "SELECT * FROM SCRAPTYPE_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<TypeMasterInterface> listScrapTypeRecords(String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPTYPE_MASTER_HISTORY(type_id,scrap_type,scrap_name, created_by, created_date, modified_by, modified_date,action,transaction_date,factory_id)\r\n"
			+ "select type_id,scrap_type,scrap_name,created_by,created_date, :modifiedBy, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM SCRAPTYPE_MASTER WHERE type_id = :typeId", nativeQuery = true)
	int insertTypeMasteToHistory(String typeId,String modifiedBy);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPTYPE_MASTER_HISTORY(type_id,scrap_type,scrap_name, created_by, created_date, modified_by, modified_date,action,transaction_date,\r\n"
			+ "deleted_date,deleted_by) select type_id,scrap_type,scrap_name,created_by,created_date, modified_by, modified_date, 'DELETE', GETDATE(),GETDATE(),:modifiedBy FROM SCRAPTYPE_MASTER WHERE\r\n"
			+ "type_id = :typeId\r\n"
			+ "", nativeQuery = true)
	int deleteTypeMasteToHistory(String typeId,String modifiedBy);
	@Query(value = "SELECT * FROM SCRAPTYPE_MASTER where type_id = :typeId and is_delete = 0", nativeQuery = true)
	TypeMasterInterface searchTypeMasterId(String typeId);

	/* TYPEMASTER -> END */

	/* UOM_MASTER MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO UOM_MASTER ( unit_name, created_by, created_date,factory_id) VALUES (:unitName, :createdBy, GETDATE(),:factory_id)", nativeQuery = true)
	int insertInvoiceUnitRecord(String unitName, String createdBy, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE UOM_MASTER SET  unit_name = :unitName, modified_by = :modifiedBy, modified_date = GETDATE() WHERE unit_id = :unitId", nativeQuery = true)
	int updateInvoiceUnitRecord(String unitName, String modifiedBy, String unitId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE UOM_MASTER SET is_delete = 1, modified_by = :modifiedBy, modified_date = GETDATE() WHERE unit_id = :unitId", nativeQuery = true)
	int deleteInvoiceUnitRecord(String modifiedBy, String unitId);

	@Query(value = "SELECT * FROM UOM_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<UOMMasterInterface> listInvoiceUnitRecords(String factory_id);

	@Query(value = "SELECT * FROM UOM_MASTER where unit_id = :unitId and is_delete = 0", nativeQuery = true)
	UOMMasterInterface searchUOMMasterId(String unitId);
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO UOM_MASTER_HISTORY(unit_id, unit_name,created_by,created_date, modified_by, modified_date,action,transaction_date,factory_id) \r\n"
			+ "select unit_id, unit_name,created_by,created_date, :modifiedBy, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM UOM_MASTER WHERE\r\n"
			+ "unit_id = :unitId", nativeQuery = true)
	int insertUOMMasteToHistory(String unitId,String modifiedBy);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO UOM_MASTER_HISTORY(unit_id, unit_name,created_by,created_date, modified_by, modified_date,action,transaction_date,deleted_date,deleted_by) \r\n"
			+ "select unit_id, unit_name,created_by,created_date, :modifiedBy, GETDATE(), 'DELETE', GETDATE(),GETDATE(),:modifiedBy FROM UOM_MASTER WHERE\r\n"
			+ "unit_id = :unitId", nativeQuery = true)
	int deleteUOMMasteToHistory(String unitId,String modifiedBy);
	/* UOM_MASTER MASTER -> END */

	/* VENDORFORSCRAP MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPVENDOR_MASTER ( vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by, created_date,factory_id) VALUES \r\n"
			+ "(:vendor_name,:vendor_state_id,:vendor_city,:vendor_desc, :created_by, GETDATE(),:factory_id)", nativeQuery = true)
	int insertScrapVendorRecord(String vendor_name, String vendor_state_id,String vendor_city, String vendor_desc,String created_by, String factory_id);


	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPVENDOR_MASTER SET  vendor_name = :vendor_name, vendor_state_id = :vendor_state_id, vendor_city = :vendor_city, vendor_desc = :vendor_desc, modified_by = :modified_by,modified_date = GETDATE() WHERE ven_id = :ven_id", nativeQuery = true)
	int updateScrapVendorRecord(String vendor_name, String vendor_state_id,String vendor_city, String vendor_desc,String modified_by,String ven_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPVENDOR_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE ven_id = :ven_id", nativeQuery = true)
	int deleteScrapVendorRecord(String modified_by, String ven_id);


	@Query(value = "SELECT * FROM SCRAPVENDOR_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<VendorForScrapMasterInterface> listScrapVendorRecord(String factory_id);


	@Query(value = "SELECT * FROM SCRAPVENDOR_MASTER where ven_id = :ven_id and is_delete = 0", nativeQuery = true)
	VendorForScrapMasterInterface searchScrapVendorRecordById(String ven_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPVENDOR_MASTER_HISTORY(ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, modified_by, modified_date,action,transaction_date,factory_id) \r\n"
				+ "select ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM SCRAPVENDOR_MASTER WHERE\r\n"
				+ "ven_id = :ven_id", nativeQuery = true)
		int insertScrapVendorToHistory(String ven_id,String modified_by);
		
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPVENDOR_MASTER_HISTORY(ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, modified_by, modified_date,action,transaction_date,deleted_date,deleted_by) \r\n"
				+ "select ven_id,vendor_name,vendor_state_id,vendor_city,vendor_desc, created_by,created_date, :modified_by, GETDATE(), 'DELETE', GETDATE(),GETDATE(),:modified_by FROM SCRAPVENDOR_MASTER WHERE\r\n"
				+ "ven_id = :ven_id", nativeQuery = true)
		int deleteScrapVendoreToHistory(String ven_id,String modified_by);
	/*VENDORFORSCRAP  MASTER -> END */

	/*WORKORDERMASTER MASTER -> START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO WORKORDER_MASTER ( workorder_no,docket_no,work_date,dock_date, created_by, created_date,factory_id) VALUES \r\n"
			+ "(:workorder_no,:docket_no,:work_date,:dock_date, :created_by, GETDATE(),:factory_id)", nativeQuery = true)
	int insertWorkOrderRecord(String workorder_no, String docket_no,String work_date, String dock_date,String created_by, String factory_id);


	@Modifying
	@Transactional
	@Query(value = "UPDATE WORKORDER_MASTER SET  workorder_no = :workorder_no, docket_no = :docket_no, work_date = :work_date, dock_date = :dock_date, modified_by = :modified_by,modified_date = GETDATE() WHERE work_id = :work_id", nativeQuery = true)
	int updateWorkOrderRecord(String workorder_no, String docket_no,String work_date, String dock_date,String modified_by,String work_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE WORKORDER_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() WHERE work_id = :work_id", nativeQuery = true)
	int deleteWorkOrderRecord(String modified_by, String work_id);


	@Query(value = "SELECT * FROM WORKORDER_MASTER where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<WorkOrderMasterInterface> listWorkOrderRecords(String factory_id);


	@Query(value = "SELECT * FROM WORKORDER_MASTER where work_id = :work_id and is_delete = 0", nativeQuery = true)
	WorkOrderMasterInterface searchWorkOrderById(String work_id);
		
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO WORKORDER_MASTER_HISTORY(work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, modified_by, modified_date,action,transaction_date,factory_id) \r\n"
				+ "select work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE(),factory_id FROM WORKORDER_MASTER WHERE\r\n"
				+ "work_id = :work_id", nativeQuery = true)
		int insertWorkOrderToHistory(String work_id,String modified_by);
		
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO WORKORDER_MASTER_HISTORY(work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, modified_by, modified_date,action,transaction_date,deleted_date,deleted_by) \r\n"
				+ "select work_id,workorder_no,docket_no,work_date,dock_date, created_by,created_date, :modified_by, GETDATE(), 'DELETE', GETDATE(),GETDATE(),:modified_by FROM WORKORDER_MASTER WHERE\r\n"
				+ "work_id = :work_id", nativeQuery = true)
		int deleteWorkOrderToHistory(String work_id,String modified_by);


	/*WORKORDERMASTER MASTER -> END */

	/* DELIVERY CONDITION MASTER -> START */
	
	@Transactional
	@Modifying
	@Query(value ="INSERT INTO SHIPMENT_DELIVERY_CONDITION (shipment_mode,delivery_condition,created_by,created_date,factory_id) VALUES (:shipment_mode,:delivery_condition,:created_by,GETDATE(), :factory_id)", nativeQuery = true)
	int insertShipmentDelivery(String shipment_mode, String delivery_condition, String created_by, String factory_id);

	@Query(value="select *  from SHIPMENT_DELIVERY_CONDITION where si_id = :si_id", nativeQuery = true)
	ShipmentDeliveryConditionInterfaces searchShipmentDeliveryId(String si_id);

	@Transactional
	@Modifying
	@Query(value ="UPDATE SHIPMENT_DELIVERY_CONDITION SET shipment_mode =:shipment_mode, delivery_condition =:delivery_condition, modified_by=:modified_by, modified_date = GETDATE() where si_id = :si_id", nativeQuery =  true)
	int updateShipmentDeliveryRecord(String shipment_mode, String delivery_condition, String modified_by, String si_id);

	
	@Transactional
	@Modifying
	@Query(value="UPDATE SHIPMENT_DELIVERY_CONDITION SET is_delete = 1,modified_by=:modified_by, modified_date = GETDATE() where si_id = :si_id", nativeQuery = true)
	int deleteshipmentdelivery(String modified_by, String si_id);

	@Query(value="select * from SHIPMENT_DELIVERY_CONDITION where is_delete = 0 and factory_id = :factory_id", nativeQuery = true)
	List<ShipmentDeliveryConditionInterfaces> listShipmentDeliveryRecord(String factory_id);

	
	@Transactional
	@Modifying
	@Query(value ="INSERT INTO SHIPMENT_DELIVERY_CONDITION_HISTORY (si_id,shipment_mode,delivery_condition,created_by,created_date,modified_by,modified_date,action,transaction_date,factory_id)"
			+ " select si_id, shipment_mode, delivery_condition, created_by, created_date, :modified_by, GETDATE() , 'UPDATE',GETDATE(),factory_id from SHIPMENT_DELIVERY_CONDITION where si_id =:si_id", nativeQuery = true)
	void insertShipmentDeliveryHistory(String modified_by, String si_id);
	
	
	@Transactional
	@Modifying
	@Query(value ="INSERT INTO SHIPMENT_DELIVERY_CONDITION_HISTORY (si_id,shipment_mode,delivery_condition,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date)"
			+ " select si_id, shipment_mode, delivery_condition, created_by, created_date, modified_by, modified_date, 'DELETE',GETDATE(),:modified_by,GETDATE() from SHIPMENT_DELIVERY_CONDITION where si_id =:si_id", nativeQuery = true)
	void updateShipmentDeliveryHistory(String modified_by, String si_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where shipment_mode_id = :shipment_mode_id and is_locked = 1", nativeQuery = true)
	int checkShipmentConditionIdPresentInContractMaster(String shipment_mode_id);
	
	@Query(value = "select count(*) from CONTRACT_MASTER where delivery_condition_id = :delivery_condition_id and is_locked = 1", nativeQuery = true)
	int checkDeliveryConditionIdPresentInContractMaster(String delivery_condition_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where work_id = :work_id and is_locked = 1", nativeQuery = true)
	int checkWorkIdPresentInContractMaster(String work_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where s_code = :servicecode_id and is_locked = 1", nativeQuery = true)
	int checkServiceCodeIdPresentInContractMaster(String servicecode_id);
	
	@Query(value = "select count(*) from CONTRACT_MASTER where h_code = :servicecode_id and is_locked = 1", nativeQuery = true)
	int checkHSNCodeIdPresentInContractMaster(String servicecode_id);
	/* DELIVERY CONDITION MASTER -> END */

	/* MASTER -> START */

	/* MASTER -> END */

}
