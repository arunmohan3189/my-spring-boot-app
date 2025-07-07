//package com.suveechi.integration.controller;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.suveechi.integration.entity.QSChallanPacking;
//import com.suveechi.integration.interfaces.QSPackingInterfaces;
//import com.suveechi.integration.interfaces.QSPackingItemsInterfaces;
//import com.suveechi.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
//import com.suveechi.integration.repository.QSChallanRepository;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/jssl")
//public class QSChallanController {
//	
//	@Autowired
//	QSChallanRepository qsChallanRepository;
//	
//	
//	@PostMapping("/qspackingmaster/add")
//	public @ResponseBody Map<String,Object> createQSPackingMaster(@RequestBody Map<String, Object> data){
//		Map<String, Object> response = new HashMap<String, Object>();
//		Map<String, String> val = null;
//		List<Map<String, String>> obj = null;
//		try {
//			   // Extract the 'val' part from the data
//	        val = (Map<String, String>) data.get("val");
//	        int valCount = 0;
//	        int count = 0;
//	        // Extract the 'obj' part, which is a list of maps
//	        obj = (List<Map<String, String>>) data.get("obj");
//	        if((val != null && !val.isEmpty()) && (obj != null  &&  !obj.isEmpty())) {
//			String con_id  = val.get("con_id");
//			String load_id = val.get("load_id");
//			String lot_no = val.get("lot_no");
//			String filepath = val.get("filepath");
//			String freight = val.get("freight");
//			String milestone_id = val.get("milestone_id");
//			String taxexemstatus = val.get("taxexemstatus");
//			String taxexemamount = val.get("taxexemamount");
//			String created_by = val.get("created_by");
//			LocalDateTime time = LocalDateTime.now();
//			QSChallanPacking qschallanpacking = new QSChallanPacking();
//			qschallanpacking.setConId(con_id);
//			qschallanpacking.setLoadId(load_id);
//			qschallanpacking.setFilepath(filepath);
//			qschallanpacking.setFreight(freight);
//			qschallanpacking.setMilestone_id(Integer.parseInt(milestone_id));
//			qschallanpacking.setTaxexemstatus(taxexemstatus);
//			qschallanpacking.setTaxexemamount(taxexemamount);
//			qschallanpacking.setCreatedBy(created_by);
//			qschallanpacking.setCreated_date(time);
//			qschallanpacking.setLotNo(lot_no);
//			QSChallanPacking objQSPacking =  qsChallanRepository.save(qschallanpacking);
//			count = objQSPacking.getPnId();
//			valCount = 0;
//			
//			//int count = qsChallanRepository.insertQSPackingMasterRecord(con_id,load_id,filepath,transport_name,vechile_no,freight,milestone_id,taxexemstatus,taxexemamount,created_by);
//			
//			for (Map<String, String> item : obj) {
//				String qty = item.get("qty");
//				String per_kgs = item.get("per_kgs");
//				String unit_price = item.get("unit_price");
//				String total = item.get("total");
//				String UOM_id = item.get("unit_id");
//				String type_id = item.get("type_id");//inc_type changed to type_id
//				String pices = item.get("pices");
//				String pn_id = String.valueOf(count);
//			valCount =	qsChallanRepository.insertQSChallanPackingItemRecord(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,created_by,pn_id);
//			}
//	        }
//			response.put("message", (count > 0 && valCount > 0 )?"success":"failure");
//			response.put("status", (count > 0  && valCount > 0)?"yes":"no");
//			response.put("action", "Insert_Record_In_QSCHALLANPACKINGMASTER");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//	
//	
//	@PostMapping("/qspackingmaster/update")
//	public @ResponseBody Map<String,Object> updateQSPackingMaster(@RequestBody Map<String, Object> data){
//		Map<String, Object> response = new HashMap<String, Object>();
//		Map<String, String> val = null;
//		List<Map<String, String>> obj = null;
//		try {
//			   // Extract the 'val' part from the data
//	        val = (Map<String, String>) data.get("val");
//	        int valCount = 0;
//	        int count = 0;
//	        // Extract the 'obj' part, which is a list of maps
//	       obj = (List<Map<String, String>>) data.get("obj");
//	       if((val != null && !val.isEmpty()) && (obj != null  &&  !obj.isEmpty())) {
//			String con_id  = val.get("con_id");
//			String load_id = val.get("load_id");
//			String lot_no = val.get("lot_no");
//			String filepath = val.get("filepath");
//			String freight = val.get("freight");
//			String milestone_id = val.get("milestone_id");
//			String milestone_name = val.get("milestone_name");
//			String taxexemstatus = val.get("taxexemstatus");
//			String taxexemamount = val.get("taxexemamount");
//			String modified_by = val.get("modified_by");
//			String pn_id = val.get("pn_id");
//		//	qsChallanRepository.insterQSChallanPackingMasterHistoryRecord(modified_by, pn_id);
//			count = qsChallanRepository.updateQSChallanPackingMasterRecord(con_id, load_id,filepath,freight, milestone_id, taxexemstatus, taxexemamount, modified_by,lot_no, pn_id);
//			for (Map<String, String> item : obj) {
//				String qty = item.get("qty");
//				String per_kgs = item.get("per_kgs");
//				String unit_price = item.get("unit_price");
//				String total = item.get("total");
//				String UOM_id = item.get("unit_id");
//				String type_id = item.get("type_id");//inc_type changed to type_id
//				String pices = item.get("pices");
//				String slno = item.get("slno");
//			//	qsChallanRepository.insterQSChallanPackingItenMasterHistoryRecord(modified_by, slno);
//			valCount =	qsChallanRepository.updateQSChallanPackingItemsRecord(qty,per_kgs,unit_price,total,UOM_id,type_id,pices,modified_by,pn_id,slno);
//			}			
//	        }
//			response.put("message", (count > 0 && valCount > 0)? "success":"failure");
//			response.put("status", (count > 0 && valCount > 0) ? "yes":"no");
//			response.put("action", "UPDATE_Record_In_QSCHALLANPACKINGMASTER");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@PostMapping("/qspackingmaster/delete")
//    public @ResponseBody Map<String,Object> deleteQsPackingRecord(@RequestBody Map<String,String> val){
//        
//        Map<String, Object> response = new HashMap<String, Object>();
//        try {
//        	String modified_by = val.get("modified_by");
//			String pn_id = val.get("pn_id");
//			qsChallanRepository.updateQSChallanPackingMasterHistoryRecord(modified_by, pn_id);
//        	int count = qsChallanRepository.delteQSChallanPackingMasterRecord(pn_id, modified_by);
//        	qsChallanRepository.updateQSChallanPackingItemMasterHistoryRecord(modified_by, pn_id);
//        	int count1 = qsChallanRepository.delteQSChallanPackingItemMasterRecord(pn_id, modified_by);
//            response.put("message", (count > 0 && count1 > 0)?"success":"failure");
//            response.put("status", (count > 0 && count1 > 0)?"yes":"no");
//            response.put("action", "DELETE_Record_In_QSCHALLANPACKINGMASTER");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return response;
//    }
//	
//	@PostMapping("/qspackingitemmaster/delete")
//    public @ResponseBody Map<String,Object> deleteQsPackingItemRecord(@RequestBody Map<String,String> val){
//        
//        Map<String, Object> response = new HashMap<String, Object>();
//        try {
//        	String modified_by = val.get("modified_by");
//			String slno = val.get("slno");
//        	int count = qsChallanRepository.delteQSChallanPackingItemRecord(slno, modified_by);
//            response.put("message", (count > 0)?"success":"failure");
//            response.put("status", (count > 0)?"yes":"no");
//            response.put("action", "DELETE_Record_In_QSCHALLANPACKINGMASTER");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return response;
//    }
//	
//	@GetMapping("/qspackingmaster/list")
//	public @ResponseBody Map<String,Object> listQSPackinglist(){
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<QSPackingInterfaces> qsPackingInterfaces = null;
//		List<QSPackingItemsInterfaces> qsPackingItemsInterfaces = null;
//		try {
//			qsPackingInterfaces = qsChallanRepository.listQSChallanPAckingMasterRecord();
//			qsPackingItemsInterfaces = qsChallanRepository.listQSChallanPAckingItemMasterRecord();
//			response.put("qschallanpacking", qsPackingInterfaces);
//			response.put("QSCHALLANPACKING_ITEM", qsPackingItemsInterfaces);
//			response.put("message", (qsPackingInterfaces != null && qsPackingItemsInterfaces != null)?"success":"failure");
//			response.put("status", (qsPackingInterfaces != null && qsPackingItemsInterfaces != null)?"yes":"no");
//			response.put("action", "List_Record_In_QSCHALLANPACKINGMASTER");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//	
//	@GetMapping("/qspackingmastersearch/{pn_id}")
//	public @ResponseBody Map<String,Object> serachQSPackingId(@PathVariable String pn_id){
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
//		try {			
//			qsPackingInterfaces = qsChallanRepository.searchQSChallanPackingById(pn_id);
//			response.put("message", (qsPackingInterfaces != null)?"success":"failure");
//			response.put("status",  (qsPackingInterfaces != null)?"yes":"no");
//			response.put("action", "Search_Record_In_QSCHALLANPACKINGMASTER");
//			response.put("DATA", qsPackingInterfaces);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//	
//	@GetMapping("/qspackingitemmastersearch/{slno}")
//	public @ResponseBody Map<String,Object> serachQSPackingItemId(@PathVariable String slno){
//		Map<String, Object> response = new HashMap<String, Object>();
//		QSPackingItemsInterfaces qsPackingItemsInterfaces = null;
//		try {			
//			qsPackingItemsInterfaces = qsChallanRepository.searchQSChallanPackingItemById(slno);
//			response.put("message", (qsPackingItemsInterfaces != null)?"success":"failure");
//			response.put("status",  (qsPackingItemsInterfaces != null)?"yes":"no");
//			response.put("action", "Search_Record_In_QSCHALLANPACKINGITEMMASTER");
//			response.put("DATA", qsPackingItemsInterfaces);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//	
//}
