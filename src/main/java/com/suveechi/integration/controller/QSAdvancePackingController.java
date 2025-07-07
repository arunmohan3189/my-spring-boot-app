package com.suveechi.integration.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suveechi.integration.entity.QSAdavancePackingNote;
import com.suveechi.integration.interfaces.QSAdvancePackingInterfaces;
import com.suveechi.integration.interfaces.QSAdvancePackingItemsInterfaces;
import com.suveechi.integration.interfaces.QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES;
import com.suveechi.integration.repository.QSAdvancePackingRepository;

@CrossOrigin
@Controller
@RequestMapping("/jssl")
public class QSAdvancePackingController {

	@Autowired
	QSAdvancePackingRepository qsAdvancePackingRepository;
	
	@PostMapping("/qsadvancepackingmaster/add")
	public @ResponseBody Map<String,Object> createQSAdvancePackingMaster(@RequestBody Map<String, Object> data){
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> val = null;
		List<Map<String, String>> obj = null;
		try {
			   // Extract the 'val' part from the data
	        val = (Map<String, String>) data.get("val");
	        int valCount = 0;
	        int count = 0;
	        // Extract the 'obj' part, which is a list of maps
	        obj = (List<Map<String, String>>) data.get("obj");
	        if((val != null && !val.isEmpty()) && (obj != null  &&  !obj.isEmpty())) {
			String con_id  = val.get("con_id");
			String filepath = val.get("filepath");
			String milestone_id = val.get("milestone_id");
			String created_by = val.get("created_by");
			int factory_id = val.containsKey("factory_id") ? Integer.parseInt(val.get("factory_id")) : 0;
			LocalDateTime time = LocalDateTime.now();
			QSAdavancePackingNote qsAdvancePacking = new QSAdavancePackingNote();
			qsAdvancePacking.setConId(con_id);
			qsAdvancePacking.setFilepath(filepath);
			qsAdvancePacking.setMilestone_id(Integer.parseInt(milestone_id));
			qsAdvancePacking.setCreatedBy(created_by);
			qsAdvancePacking.setCreated_date(time);
			qsAdvancePacking.setFactory_id(factory_id);
			QSAdavancePackingNote objQSAdvancePacking =  qsAdvancePackingRepository.save(qsAdvancePacking);
			count = objQSAdvancePacking.getPnId();
			valCount = 0;
			for (Map<String, String> item : obj) {
				String qty = item.get("qty");
				String per_kgs = item.get("per_kgs");
				String unit_price = item.get("unit_price");
				String total = item.get("total");
				String UOM_id = item.get("unit_id");
				String type_id = item.get("type_id");
				String pices = item.get("pices");
				String pn_id = String.valueOf(count);
			valCount =	qsAdvancePackingRepository.insertQSAdvancePackingItemRecord(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id);
			}
	        }
			response.put("message", (count > 0 && valCount > 0 )?"success":"failure");
			response.put("status", (count > 0  && valCount > 0)?"yes":"no");
			response.put("action", "Insert_Record_In_QSADVANCEPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	@PostMapping("/qsadvancepackingmaster/update")
	public @ResponseBody Map<String,Object> updateQSAdvancePackingMaster(@RequestBody Map<String, Object> data){
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> val = null;
		List<Map<String, String>> obj = null;
		try {
			   // Extract the 'val' part from the data
	        val = (Map<String, String>) data.get("val");
	        int valCount = 0;
	        QSAdavancePackingNote objQSAdvancePacking = null;
	        // Extract the 'obj' part, which is a list of maps
	       obj = (List<Map<String, String>>) data.get("obj");
	       if((val != null && !val.isEmpty()) && (obj != null  &&  !obj.isEmpty())) {
	    	   String con_id  = val.get("con_id");
				String filepath = val.get("filepath");
				String milestone_id = val.get("milestone_id");
				String modified_by = val.get("modified_by");
				String pn_id = val.get("pn_id");
				LocalDateTime time = LocalDateTime.now();
				QSAdavancePackingNote qsAdvancePacking = new QSAdavancePackingNote();
				qsAdvancePacking.setConId(con_id);
				qsAdvancePacking.setFilepath(filepath);
				qsAdvancePacking.setMilestone_id(Integer.parseInt(milestone_id));
				qsAdvancePacking.setModified_by(modified_by);
				qsAdvancePacking.setModified_date(time);
				qsAdvancePacking.setPnId(Integer.parseInt(pn_id));
				objQSAdvancePacking =  qsAdvancePackingRepository.save(qsAdvancePacking);
			//qsPackingRepository.insterQSPackingMasterHistoryRecord(modified_by, pn_id);
			for (Map<String, String> item : obj) {
				String qty = item.get("qty");
				String per_kgs = item.get("per_kgs");
				String unit_price = item.get("unit_price");
				String total = item.get("total");
				String UOM_id = item.get("unit_id");
				String type_id = item.get("type_id");
				String pices = item.get("pices");
				String slno = item.get("slno");
			//	qsPackingRepository.insterQSPackingItenMasterHistoryRecord(modified_by, slno);
			valCount =	qsAdvancePackingRepository.updateQSAdvancePackingItemsRecord(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,modified_by,pn_id,slno);
			}			
	        }
			response.put("message", (objQSAdvancePacking != null && valCount > 0)? "success":"failure");
			response.put("status", (objQSAdvancePacking != null && valCount > 0) ? "yes":"no");
			response.put("action", "UPDATE_Record_In_QSADVANCEPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	
	@PostMapping("/qsadvancepackingmaster/delete") // history is pending
    public @ResponseBody Map<String,Object> deleteQSAdvancePackingRecord(@RequestBody Map<String,String> val){
        
        Map<String, Object> response = new HashMap<String, Object>();
        try {
        	String modified_by = val.get("modified_by");
			String pn_id = val.get("pn_id");
		//	qsAdvancePackingRepository.updateQSPackingMasterHistoryRecord(modified_by, pn_id);
        	int count = qsAdvancePackingRepository.delteQSAdvancePackingMasterRecord(pn_id, modified_by);
        //	qsAdvancePackingRepository.updateQSPackingItemMasterHistoryRecord(modified_by, pn_id);
        	int count1 = qsAdvancePackingRepository.delteQSAdvancePackingItemMasterRecord(pn_id, modified_by);
            response.put("message", (count > 0 && count1 > 0)?"success":"failure");
            response.put("status", (count > 0 && count1 > 0)?"yes":"no");
            response.put("action", "DELETE_Record_In_QSADVANCEPACKINGMASTER");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	
	@PostMapping("/qsadvancepackingitemmaster/delete") // history is pending
    public @ResponseBody Map<String,Object> deleteQSAdvancePackingItemRecord(@RequestBody Map<String,String> val){
        
        Map<String, Object> response = new HashMap<String, Object>();
        try {
        	String modified_by = val.get("modified_by");
			String slno = val.get("slno");
        	int count = qsAdvancePackingRepository.delteQSAdvancePackingItemMasterRecord(slno, modified_by);
            response.put("message", (count > 0)?"success":"failure");
            response.put("status", (count > 0)?"yes":"no");
            response.put("action", "DELETE_Record_In_QSADVANCEPACKINGMASTER");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	
	@GetMapping("/qsadvancepackingnotemaster/list/{factory_id}")
	public @ResponseBody Map<String,Object> listQSAdvancePackinglist(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSAdvancePackingInterfaces> qsAdvancePackingInterfaces = null;
		List<QSAdvancePackingItemsInterfaces> qsAdvancePackingItemsInterfaces = null;
		try {
			qsAdvancePackingInterfaces = qsAdvancePackingRepository.listQSAdvancePAckingMasterRecord(factory_id);
			qsAdvancePackingItemsInterfaces = qsAdvancePackingRepository.listQSAdvancePAckingItemMasterRecord(factory_id);
			response.put("QSADVANCEPACKING", qsAdvancePackingInterfaces);
			response.put("QSADVANCEPACKING_ITEM", qsAdvancePackingItemsInterfaces);
			response.put("message", (qsAdvancePackingInterfaces != null && qsAdvancePackingInterfaces != null)?"success":"failure");
			response.put("status", (qsAdvancePackingItemsInterfaces != null && qsAdvancePackingItemsInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_QSADVANCEPACKINGNOTEMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/qsadvancepackingnotemastersearch/{pn_id}")
	public @ResponseBody Map<String,Object> serachQSAdvancePackingId(@PathVariable String pn_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSAdvancePacking_QSAdvancePackingItem_LIST_INTERFACES> qsAdvancePackingInterfaces = null;
		try {			
			qsAdvancePackingInterfaces = qsAdvancePackingRepository.searchQSAdvancePackingById(pn_id);
			response.put("message", (qsAdvancePackingInterfaces != null)?"success":"failure");
			response.put("status",  (qsAdvancePackingInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_QSADVANCEPACKINGNOTEITEMMASTER");
			response.put("DATA", qsAdvancePackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/qsadvancepackingnoteitemmastersearch/{slno}")
	public @ResponseBody Map<String,Object> serachQSAdvancePackingItemId(@PathVariable String slno){
		Map<String, Object> response = new HashMap<String, Object>();
		QSAdvancePackingItemsInterfaces qsAdvancePackingItemsInterfaces = null;
		try {			
			qsAdvancePackingItemsInterfaces = qsAdvancePackingRepository.searchQSAdvancePackingItemById(slno);
			response.put("message", (qsAdvancePackingItemsInterfaces != null)?"success":"failure");
			response.put("status",  (qsAdvancePackingItemsInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_QSADVANCEPACKINGITEMMASTER");
			response.put("DATA", qsAdvancePackingItemsInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
