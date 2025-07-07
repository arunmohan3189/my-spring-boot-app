package com.suveechi.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suveechi.integration.interfaces.GSTStateMasterInterface;
import com.suveechi.integration.interfaces.MileStoneMasterInterface;
import com.suveechi.integration.interfaces.OtherTypeMasterInterface;
import com.suveechi.integration.interfaces.ServiceCodeMasterInterface;
import com.suveechi.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.suveechi.integration.interfaces.TypeMasterInterface;
import com.suveechi.integration.interfaces.UOMMasterInterface;
import com.suveechi.integration.interfaces.VendorForScrapMasterInterface;
import com.suveechi.integration.interfaces.WorkOrderMasterInterface;
import com.suveechi.integration.repository.MasterRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class MasterController {
	
	@Autowired
	private MasterRepository masterRepository;
	/* SHIPMENT DELIVER CONDITION START */
	
	@PostMapping("/master/addshipmentdelivery")
	public @ResponseBody Map<String,Object> createShipmentDelivery(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String shipment_mode = val.get("shipment_mode");
			String delivery_condition = val.get("delivery_condition");
			String created_by = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ; 
			int count = masterRepository.insertShipmentDelivery(shipment_mode,delivery_condition,created_by,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_ShipmentDelivery");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message",e.getMessage());
		}
		return response;
	}
	
	@GetMapping("/master/searchshipmentdelivery/{si_id}")
	public @ResponseBody Map<String,Object> serachShipmentDeliveryId(@PathVariable String si_id){
		Map<String, Object> response = new HashMap<String, Object>();
		ShipmentDeliveryConditionInterfaces shipmentDeliveryConditionInterfaces = null;
		try {			
			shipmentDeliveryConditionInterfaces = masterRepository.searchShipmentDeliveryId(si_id);
			response.put("message", (shipmentDeliveryConditionInterfaces != null)?"success":"failure");
			response.put("status",  (shipmentDeliveryConditionInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_ShipmentDelivery");
			response.put("DATA", shipmentDeliveryConditionInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updateshipmentdelivery")
	public @ResponseBody Map<String,Object> updateShipmentDelivery(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		int check = 0;
		try {
			String shipment_mode = val.get("shipment_mode");
			String delivery_condition = val.get("delivery_condition");
			String modified_by = val.get("modified_by");
			String si_id = val.get("si_id");
			check = masterRepository.checkShipmentConditionIdPresentInContractMaster(si_id);
			if(check > 0) {
				response.put("message","Invoice Already Generated Not Able to Update");
				return response;
			}
			check = masterRepository.checkDeliveryConditionIdPresentInContractMaster(si_id);
			if(check > 0) {
				response.put("message","Invoice Already Generated Not Able to Update");
				return response;
			}
			masterRepository.insertShipmentDeliveryHistory(modified_by,si_id);
			count = masterRepository.updateShipmentDeliveryRecord(shipment_mode,delivery_condition,modified_by,si_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_ShipmentDelivery");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/master/deleteshipmentdelivery")
    public @ResponseBody Map<String,Object> deleteShipmentDelivery(@RequestBody Map<String,String> val){
        Map<String, Object> response = new HashMap<String, Object>();
        try {
        	 String modified_by = val.get("modified_by");
        	 String si_id = val.get("si_id");
        	 masterRepository.updateShipmentDeliveryHistory(modified_by, si_id);
            int count = masterRepository.deleteshipmentdelivery(modified_by, si_id);
            response.put("message", (count > 0)?"success":"failure");
            response.put("status", (count > 0)?"yes":"no");
            response.put("action", "Delete_Record_In_ShipmentDelivery");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	@GetMapping("/master/listshipmentdelivery/{factory_id}")
	public @ResponseBody Map<String,Object> listShipmentDelivery(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<ShipmentDeliveryConditionInterfaces> shipmentDeliveryConditionInterfaces = null;
		try {
			shipmentDeliveryConditionInterfaces = masterRepository.listShipmentDeliveryRecord(factory_id);
			response.put("message", (shipmentDeliveryConditionInterfaces != null)?"success":"failure");
			response.put("status", (shipmentDeliveryConditionInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_ShipmentDelivery");
			response.put("Data", shipmentDeliveryConditionInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	/* SHIPMENT DELIVER CONDITION END */
	/* 1. GSTSTATEMASTER( ScreenName = CREATE GST STATE) ----> START*/
	@PostMapping("/master/addgstmaster")
	public @ResponseBody Map<String,Object> createGstState(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String sapBuCode = val.get("sap_bucode");
			String stateCode = val.get("state_code");
			String stateGstNo = val.get("state_gstno");
			String createdBy = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertGSTMasterRecord(sapBuCode, stateCode, stateGstNo, createdBy,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_GSTMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/master/searchgstmaster/{buId}")
	public @ResponseBody Map<String,Object> serachGstStateById(@PathVariable String buId){
		Map<String, Object> response = new HashMap<String, Object>();
		GSTStateMasterInterface gstStateMasterInterface = null;
		try {			
			gstStateMasterInterface = masterRepository.searchGSTById(buId);
			response.put("message", (gstStateMasterInterface != null)?"success":"failure");
			response.put("status",  (gstStateMasterInterface != null)?"yes":"no");
			response.put("action", "Search_Record_In_GSTMASTER");
			response.put("DATA", gstStateMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updategstmaster")
	public @ResponseBody Map<String,Object> updateGstState(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String sapBuCode = val.get("sap_bucode");
			String stateCode = val.get("state_code");
			String stateGstNo = val.get("state_gstno");
			String modifiedBy = val.get("modified_by");
			String buId = val.get("bu_id");
			masterRepository.insertGSTMasterToHistory(buId,modifiedBy);
			int count = masterRepository.updateGSTMasterRecord(sapBuCode, stateCode, stateGstNo, modifiedBy,buId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_GSTMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/master/deletegstmaster")
    public @ResponseBody Map<String,Object> deleteGstState(@RequestParam String modified_by, @RequestParam String bu_id){
        
        Map<String, Object> response = new HashMap<String, Object>();
        try {
    
            int c = masterRepository.deleteGSTMasterToHistory(modified_by, bu_id);
            int count = masterRepository.deleteGSTMasterRecord(modified_by, bu_id);
            response.put("message", (count > 0)?"success":"failure");
            response.put("status", (count > 0)?"yes":"no");
            response.put("action", "Delete_Record_In_GSTMASTER");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	@GetMapping("/master/listgstmaster/{factory_id}")
	public @ResponseBody Map<String,Object> listGstState(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<GSTStateMasterInterface> gstStateMasterInterface = null;
		try {
			gstStateMasterInterface = masterRepository.listGSTMasterRecord(factory_id);
			response.put("message", (gstStateMasterInterface != null)?"success":"failure");
			response.put("status", (gstStateMasterInterface != null)?"yes":"no");
			response.put("action", "List_Record_In_GSTMASTER");
			response.put("Data", gstStateMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/* 1. GSTSTATEMASTER (ScreenName = CREATE GST STATE)----> END*/
	
	/* 2. MILESTONEMASTER (ScreenName = CREATE MILESTONE)----> START*/
	@PostMapping("/master/addmilestonemaster")
	public @ResponseBody Map<String,Object> createMileStone(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {	
			String milestoneCode = val.get("milestone_code");
			String milestoneName = val.get("milestone_name");
			String milestoneDesc = val.get("milestone_desc");
			String createdBy = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertMilestoneRecord(milestoneCode, milestoneName, milestoneDesc, createdBy,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_MILESTONEMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updatemilestonemaster")
	public @ResponseBody Map<String,Object> updateMileStone(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String milestoneCode = val.get("milestone_code");
			String milestoneName = val.get("milestone_name");
			String milestoneDesc = val.get("milestone_desc");
			String modifiedBy = val.get("modified_by");
			String milestoneId = val.get("milestone_id");
			masterRepository.insertMileStoneToHistory(milestoneId, modifiedBy);
			int count = masterRepository.updateMilestoneRecord(milestoneCode, milestoneName, milestoneDesc, modifiedBy,milestoneId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_MILESTONEMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/deletemilestonemaster")
	public @ResponseBody Map<String,Object> deleteMileStone(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modifiedBy = val.get("modified_by");
			String milestoneId = val.get("milestone_id");
			int c = masterRepository.deleteMileStoneToHistory(milestoneId, modifiedBy);
			int count = masterRepository.deleteMilestoneRecord(modifiedBy, milestoneId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Delete_Record_In_MILESTONEMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	@GetMapping("/master/searchmilestonemaster/{milestoneId}")
	public @ResponseBody Map<String,Object> serachmilestonemaster(@PathVariable String milestoneId){
		Map<String, Object> response = new HashMap<String, Object>();
		MileStoneMasterInterface mileStoneMasterInterface = null;
		try {			
			mileStoneMasterInterface = masterRepository.searchMileStoneById(milestoneId);
			response.put("message", (mileStoneMasterInterface != null)?"success":"failure");
			response.put("status",  (mileStoneMasterInterface != null)?"yes":"no");
			response.put("action", "Search_Record_In_MILESTONEMASTER");
			response.put("DATA", mileStoneMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/master/listmilestonemaster/{factory_id}")
	public @ResponseBody Map<String,Object> listMileStone(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<MileStoneMasterInterface> mileStoneMasterInterfaces = null;
		try {
			mileStoneMasterInterfaces = masterRepository.listMilestoneRecords(factory_id);
			response.put("message", (mileStoneMasterInterfaces != null)?"success":"failure");
			response.put("status", (mileStoneMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_MILESTONEMASTER");
			response.put("Data", mileStoneMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/* 2.  MILESTONEMASTER (ScreenName = CREATE MILESTONE)----> END*/
	
	/* 3. OTHERTYPEMASTER (ScreenName = OTHERTYPE)----> START*/
	@PostMapping("/master/addothertypemaster")
	public @ResponseBody Map<String,Object> createOtherType(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {	
			String type = val.get("type");
			String createdBy = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertOtherType(type, createdBy,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_OtherType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updateothertypemaster")
	public @ResponseBody Map<String,Object> updateOtherType(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String type = val.get("type");
			String modifiedBy = val.get("modified_by");
			String otherTypeId = val.get("othertype_id");
			masterRepository.insertOtherTypeToHistory(otherTypeId, modifiedBy);
			int count = masterRepository.updateOtherType(type, modifiedBy, otherTypeId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_OtherType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	@GetMapping("/master/searchothertypemaster/{otherTypeId}")
	public @ResponseBody Map<String,Object> serachOtherTypeById(@PathVariable String otherTypeId){
		Map<String, Object> response = new HashMap<String, Object>();
		OtherTypeMasterInterface otherTypeMasterInterfaces = null;
		try {			
			otherTypeMasterInterfaces = masterRepository.searchOtherTypeById(otherTypeId);
			response.put("message", (otherTypeMasterInterfaces != null)?"success":"failure");
			response.put("status",  (otherTypeMasterInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_OtherType");
			response.put("DATA", otherTypeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	@PostMapping("/master/deleteothertypemaster")
	public @ResponseBody Map<String,Object> deleteOtherType(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modifiedBy = val.get("modified_by");
			String otherTypeId = val.get("othertype_id");
			int c =masterRepository.deleteOtherTypeToHistory(otherTypeId, modifiedBy);
			int count = masterRepository.deleteOtherType(modifiedBy, otherTypeId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_OtherType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/master/listothertypemaster/{factory_id}")
	public @ResponseBody Map<String,Object> listOtherType(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<OtherTypeMasterInterface> otherTypeMasterInterfaces = null;
		try {
			otherTypeMasterInterfaces = masterRepository.listOtherTypes(factory_id);
			response.put("message", (otherTypeMasterInterfaces != null)?"success":"failure");
			response.put("status", (otherTypeMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_OtherType");
			response.put("Data", otherTypeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/* 3. OTHERTYPEMASTER (ScreenName = OTHERTYPE )----> END*/
	
	/* 4. SERVICECODEMASTER (ScreenName = Create ServiceCode) ----> START*/
	@PostMapping("/master/addservicecodemaster")
	public @ResponseBody Map<String,Object> createServiceCode(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {	
			String serviceType = val.get("service_type");
			String serviceDescription = val.get("service_description");
			String serviceCode = val.get("service_code");
			String createdBy = val.get("created_by");
			String status = val.get("status");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertServiceCodeRecord(serviceType, serviceDescription, serviceCode,createdBy,status,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_ServiceCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updateservicecodemaster")
	public @ResponseBody Map<String,Object> updateServiceCode(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		int count =0;
		int check = 0;
		try {
			String serviceType = val.get("service_type");
			String serviceDescription = val.get("service_description");
			String serviceCode = val.get("service_code");
			String modifiedBy = val.get("modified_by");
			String servicecodeId = val.get("servicecode_id");
			String status = val.get("status");
			check = masterRepository.checkServiceCodeIdPresentInContractMaster(servicecodeId);
			if(check == 1) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			check = masterRepository.checkHSNCodeIdPresentInContractMaster(servicecodeId);
			if(check == 1) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			masterRepository.insertServiceCodeToHistory(servicecodeId, modifiedBy);
			count = masterRepository.updateServiceCodeRecord(serviceType, serviceDescription, serviceCode, modifiedBy, status,servicecodeId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_ServiceCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/master/deleteservicecodemaster")
	public @ResponseBody Map<String,Object> deleteServiceCode(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modifiedBy = val.get("modified_by");
			String servicecodeId = val.get("servicecode_id");
			masterRepository.deleteServiceCodeToHistory(servicecodeId, modifiedBy);
			int count = masterRepository.updateServiceCodeInActiveRecord(modifiedBy, servicecodeId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Delete_Record_In_ServiceCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/*
	 * @PostMapping("/master/inctiveservicecodemaster") public @ResponseBody
	 * Map<String,Object> inActiveServiceCode(@RequestBody Map<String,String> val){
	 * Map<String, Object> response = new HashMap<String, Object>(); try { String
	 * modifiedBy = val.get("modified_by"); String servicecodeId =
	 * val.get("servicecode_by"); int count =
	 * masterRepository.updateServiceCodeInActiveRecord(modifiedBy, servicecodeId);
	 * response.put("message", (count > 0)?"success":"failure");
	 * response.put("status", (count > 0)?"yes":"no"); response.put("action",
	 * "Delete_Record_In_ServiceCode"); } catch (Exception e) { e.printStackTrace();
	 * } return response; }
	 */
	@GetMapping("/master/searchservicecode/{servicecodeId}")
	public @ResponseBody Map<String,Object> serachServiceCodeById(@PathVariable String servicecodeId){
		Map<String, Object> response = new HashMap<String, Object>();
		ServiceCodeMasterInterface serviceCodeMasterInterfaces = null;
		try {			
			serviceCodeMasterInterfaces = masterRepository.searchServiceById(servicecodeId);
			response.put("message", (serviceCodeMasterInterfaces != null)?"success":"failure");
			response.put("status",  (serviceCodeMasterInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_ServiceCode");
			response.put("DATA", serviceCodeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	@GetMapping("/master/listservicecodemaster/{factory_id}")
	public @ResponseBody Map<String,Object> listServiceCode(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<ServiceCodeMasterInterface> serviceCodeMasterInterfaces = null;
		try {
			serviceCodeMasterInterfaces = masterRepository.listServiceCodeRecords(factory_id);
			response.put("message", (serviceCodeMasterInterfaces != null)?"success":"failure");
			response.put("status", (serviceCodeMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_ServiceCode");
			response.put("Data", serviceCodeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/* 4. SERVICECODEMASTER (ScreenName = Create ServiceCode) ----> END*/
	
	/* 5. TYPEMASTER (ScreenName = Type Master) ----> START*/
	@PostMapping("/master/addtypemaster")
	public @ResponseBody Map<String,Object> createTypeMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {	
			String scrapType = val.get("scrap_type");
			String scrapName = val.get("scrap_name");
			String createdBy = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertScrapTypeRecord(scrapType, scrapName, createdBy,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_TypeMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updatetypemaster")
	public @ResponseBody Map<String,Object> updateTypeMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String scrapType = val.get("scrap_type");
			String scrapName = val.get("scrap_name");
			String modifiedBy = val.get("modified_by");
			String typeId = val.get("type_id");
			masterRepository.insertTypeMasteToHistory(typeId, modifiedBy);
			int count = masterRepository.updateScrapTypeRecord(scrapType, scrapName, modifiedBy, typeId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_TypeMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/deletetypemaster")
	public @ResponseBody Map<String,Object> deleteTypeMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modifiedBy = val.get("modified_by");
			String typeId = val.get("type_id");
			masterRepository.deleteTypeMasteToHistory(typeId, modifiedBy);
			int count = masterRepository.deleteScrapTypeRecord(modifiedBy, typeId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_TypeMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/master/searchtypemaster/{typeId}")
	public @ResponseBody Map<String,Object> serachTypeMasterById(@PathVariable String typeId){
		Map<String, Object> response = new HashMap<String, Object>();
		TypeMasterInterface typeMasterInterfaces = null;
		try {			
			typeMasterInterfaces = masterRepository.searchTypeMasterId(typeId);
			response.put("message", (typeMasterInterfaces != null)?"success":"failure");
			response.put("status",  (typeMasterInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_TypeMaster");
			response.put("DATA", typeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/master/listtypemaster/{factory_id}")
	public @ResponseBody Map<String,Object> listTypeMaster(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<TypeMasterInterface> typeMasterInterfaces = null;
		try {
			typeMasterInterfaces = masterRepository.listScrapTypeRecords(factory_id);
			response.put("message", (typeMasterInterfaces != null)?"success":"failure");
			response.put("status", (typeMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_TypeMaster");
			response.put("Data", typeMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/* 5. TypeEMASTER (ScreenName = TypeMaster)----> END*/
	
	/* 6. UNITMEASUREMENTMASTER (ScreenName = Unit Measurement)-----> START*/
	@PostMapping("/master/addunitmeasurementmaster")
	public @ResponseBody Map<String,Object> createUnitMeasurementMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String unitName = val.get("unit_name");
			String createdBy = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertInvoiceUnitRecord(unitName, createdBy, factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_UnitMeasurementMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updateunitmeasurementmaster")
	public @ResponseBody Map<String,Object> updateUnitMeasurementMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String unitName = val.get("unit_name");
			String modifiedBy = val.get("modified_by");
			String unitId = val.get("unit_id");
			masterRepository.insertUOMMasteToHistory(unitId, modifiedBy);
			int count = masterRepository.updateInvoiceUnitRecord(unitName, modifiedBy,unitId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_UnitMeasurementMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/deleteunitmeasurementmaster")
	public @ResponseBody Map<String,Object> deleteUnitMeasurementMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modifiedBy = val.get("modified_by");
			String unitId = val.get("unit_id");
			masterRepository.deleteUOMMasteToHistory(unitId, modifiedBy);
			int count = masterRepository.deleteInvoiceUnitRecord(modifiedBy, unitId);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_UnitMeasurementMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/master/listunitmeasurementmaster/{factory_id}")
	public @ResponseBody Map<String,Object> listUnitMeasurementMaster(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<UOMMasterInterface> uomMasterInterfaces = null;
		try {
			uomMasterInterfaces = masterRepository.listInvoiceUnitRecords(factory_id);
			response.put("message", (uomMasterInterfaces != null)?"success":"failure");
			response.put("status", (uomMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_UnitMeasurementMaster");
			response.put("Data", uomMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/master/searchunitmaster/{unitId}")
	public @ResponseBody Map<String,Object> serachUnitMeasurementMasterById(@PathVariable String unitId){
		Map<String, Object> response = new HashMap<String, Object>();
		UOMMasterInterface uomMasterInterfaces = null;
		try {			
			uomMasterInterfaces = masterRepository.searchUOMMasterId(unitId);
			response.put("message", (uomMasterInterfaces != null)?"success":"failure");
			response.put("status",  (uomMasterInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_UnitMeasurementMaste");
			response.put("DATA", uomMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/* UNITMEASUREMENTMASTER (ScreenName = Unit Measurement)-----> END*/
	
	/* 7. VENDORFORSCRAPMASTER (ScreenName = Vendor for scrap)-----> START*/
	@PostMapping("/master/addvendorforscrapmaster")
	public @ResponseBody Map<String,Object> createVendorForScrapMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String vendor_name = val.get("vendor_name");
			String vendor_state_id = val.get("vendor_state_id");
			String vendor_city = val.get("vendor_city");
			String vendor_desc = val.get("vendor_desc");
			String created_by = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertScrapVendorRecord(vendor_name, vendor_state_id, vendor_city, vendor_desc, created_by,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_VendorForScrapMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updatevendorforscrapmaster")
	public @ResponseBody Map<String,Object> updateVendorForScrapMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String vendor_name = val.get("vendor_name");
			String vendor_state_id = val.get("vendor_state_id");
			String vendor_city = val.get("vendor_city");
			String vendor_desc = val.get("vendor_desc");
			String modified_by = val.get("modified_by");
			String ven_id = val.get("ven_id");
			masterRepository.insertScrapVendorToHistory(ven_id, modified_by);
			int count = masterRepository.updateScrapVendorRecord(vendor_name, vendor_state_id, vendor_city, vendor_desc, modified_by, ven_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_VendorForScrapMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/master/listvendorforscrapmaster/{factory_id}")
	public @ResponseBody Map<String,Object> listVendorForScrapMaster(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<VendorForScrapMasterInterface> vendorForScrapMasterInterfaces = null;
		try {
			vendorForScrapMasterInterfaces = masterRepository.listScrapVendorRecord(factory_id);
			response.put("message", (vendorForScrapMasterInterfaces != null)?"success":"failure");
			response.put("status", (vendorForScrapMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_VendorForScrapMaster");
			response.put("Data", vendorForScrapMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/master/searchvendorforscrapmaster/{ven_id}")
	public @ResponseBody Map<String,Object> searchScrapVendorRecord(@PathVariable String ven_id){
		Map<String, Object> response = new HashMap<String, Object>();
		VendorForScrapMasterInterface vendorForScrapMasterInterface = null;
		try {			
			vendorForScrapMasterInterface = masterRepository.searchScrapVendorRecordById(ven_id);
			response.put("message", (vendorForScrapMasterInterface != null)?"success":"failure");
			response.put("status",  (vendorForScrapMasterInterface != null)?"yes":"no");
			response.put("action", "Search_Record_In_UnitMeasurementMaste");
			response.put("DATA", vendorForScrapMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/deletevendorforscrapmaster")
	public @ResponseBody Map<String,Object> deletevendorforscrapmaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modifiedBy = val.get("modified_by");
			String ven_id = val.get("ven_id");
			masterRepository.deleteScrapVendoreToHistory(ven_id, modifiedBy);
			int count = masterRepository.deleteScrapVendorRecord(modifiedBy, ven_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_UnitMeasurementMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/* 7. VENDORFORSCRAPMASTER (ScreenName = vendor for scrap)-----> END*/
	/*8.  WORKORDERMASTER (ScreenName = work order)-----> START*/
	@PostMapping("/master/addworkordermaster")
	public @ResponseBody Map<String,Object> createWorkOrderMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String workorder_no = val.get("workorder_no");
			String docket_no = val.get("docket_no");
			String work_date = val.get("work_date");
			String dock_date = val.get("dock_date");
			String created_by = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0" ;
			int count = masterRepository.insertWorkOrderRecord(workorder_no, docket_no, work_date, dock_date, created_by,factory_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_WorkOrderMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/updateworkordermaster")
	public @ResponseBody Map<String,Object> updateWorkOrderMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		int check = 0;
		try {
			String workorder_no = val.get("workorder_no");
			String docket_no = val.get("docket_no");
			String work_date = val.get("work_date");
			String dock_date = val.get("dock_date");
			String modified_by = val.get("modified_by");
			String work_id = val.get("work_id");
			check = masterRepository.checkWorkIdPresentInContractMaster(work_id);
			if(check > 0) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			masterRepository.insertWorkOrderToHistory(work_id, modified_by);
			count = masterRepository.updateWorkOrderRecord(workorder_no, docket_no, work_date, dock_date, modified_by, work_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_WorkOrderMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/master/listworkordermaster/{factory_id}")
	public @ResponseBody Map<String,Object> listWorkOrderMaster(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<WorkOrderMasterInterface> workOrderMasterInterfaces = null;
		try {
			workOrderMasterInterfaces = masterRepository.listWorkOrderRecords(factory_id);
			response.put("message", (workOrderMasterInterfaces != null)?"success":"failure");
			response.put("status", (workOrderMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_WorkOrderMaster");
			response.put("Data", workOrderMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/master/searchworkordermaster/{work_id}")
	public @ResponseBody Map<String,Object> searchworkorderRecord(@PathVariable String work_id){
		Map<String, Object> response = new HashMap<String, Object>();
		WorkOrderMasterInterface workOrderMasterInterface = null;
		try {			
			workOrderMasterInterface = masterRepository.searchWorkOrderById(work_id);
			response.put("message", (workOrderMasterInterface != null)?"success":"failure");
			response.put("status",  (workOrderMasterInterface != null)?"yes":"no");
			response.put("action", "Search_Record_In_WorkOrderMaster");
			response.put("DATA", workOrderMasterInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/master/deleteworkordermaster")
	public @ResponseBody Map<String,Object> deleteWorkOrderMaster(@RequestBody Map<String,String> val){
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modifiedBy = val.get("modified_by");
			String work_id = val.get("work_id");
			masterRepository.deleteWorkOrderToHistory(work_id, modifiedBy);
			int count = masterRepository.deleteWorkOrderRecord(modifiedBy, work_id);
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "Update_Record_In_WorkOrderMaster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	/* WORKORDERMASTER ----> END*/
}
