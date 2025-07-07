package com.suveechi.integration.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suveechi.integration.entity.QSPacking;
import com.suveechi.integration.interfaces.QSPackingInterfaces;
import com.suveechi.integration.interfaces.QSPackingItemsInterfaces;
import com.suveechi.integration.interfaces.QSPacking_QSPackingItem_LIST_INTERFACES;
import com.suveechi.integration.repository.QSPackingRepository;

@CrossOrigin
@Controller
@RequestMapping("/jssl")
public class QSPackingController {

	@Autowired
	QSPackingRepository qsPackingRepository;
	
	@Value("${file.upload-dir}")
	private String uploadDir;

	 public String getUploadDir() {
	        return uploadDir;
	    }
	
	@PostMapping(value = "/qspackingmaster/add")
	public @ResponseBody Map<String, Object> createQSPackingMaster(@RequestParam("file") MultipartFile file,
			   @RequestParam("val") String valueDataJson, @RequestParam("obj") String objDataJson) {
	    Map<String, Object> response = new HashMap<String, Object>();
	    Map<String, String> val = null;
	    List<Map<String, String>> obj = null;
	    
	    try {
	        // Convert the 'val' and 'obj' JSON strings into Java objects
	        ObjectMapper objectMapper = new ObjectMapper();
	        String uniqueFileName = null;
	        val = objectMapper.readValue(valueDataJson, new TypeReference<Map<String, String>>() {});
	        obj = objectMapper.readValue(objDataJson, new TypeReference<List<Map<String, String>>>() {});
			int valCount = 0;
			int count = 0;
			String  uploadedFilePath = null;
			long maxSize = 10 * 1024 * 1024;
			// Extract the 'obj' part, which is a list of maps
			
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().equals("application/pdf")) {
					response.put("message", "Only PDF files are allowed.");
					response.put("status", "no");
					return response;
				}
				 // Check the file size manually
		        if (file.getSize() > maxSize) {
		            response.put("message", "File size exceeds the maximum limit of 10MB.");
		            response.put("status", "no");
		            return response;
		        }
				  // Generate a unique file name
                uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs(); // Create the directory if it doesn't exist
                }

                // Save the file
                File serverFile = new File(directory, uniqueFileName);
                if (serverFile.exists()) {
                	response.put("message", "File already exists! Please provide another File.");
                    return response;
                }
                file.transferTo(serverFile);

                // Set the uploaded file path
                uploadedFilePath = serverFile.getAbsolutePath();
			}
			if ((val != null && !val.isEmpty()) && (obj != null && !obj.isEmpty())) {
				String con_id = val.get("con_id");
				String load_id = val.get("load_id");
				String lot_no = val.get("lot_no");
				//String filepath = val.get("filepath");
				String filepath = uniqueFileName;
				String transport_name = val.get("transport_name");
				String vechile_no = val.get("vechile_no");
				String freight = val.get("freight");
				String milestone_id = val.get("milestone_id");
				String milestone_name = val.get("milestone_name");
				String taxexemstatus = val.get("taxexemstatus");
				String taxexemamount = val.get("taxexemamount");
				String created_by = val.get("created_by");
				int factory_id = val.containsKey("factory_id") ? Integer.parseInt(val.get("factory_id")) : 0 ;
				LocalDateTime time = LocalDateTime.now();
				QSPacking qspacking = new QSPacking();
				qspacking.setConId(con_id);
				qspacking.setLoadId(load_id);
				qspacking.setFilepath(filepath);
				qspacking.setTransport_name(transport_name);
				qspacking.setVechile_no(vechile_no);
				qspacking.setFreight(freight);
				qspacking.setMilestone_id(Integer.parseInt(milestone_id));
				qspacking.setTaxexemstatus(taxexemstatus);
				qspacking.setTaxexemamount(taxexemamount);
				qspacking.setCreatedBy(created_by);
				qspacking.setCreated_date(time);
				qspacking.setLotNo(lot_no);
				qspacking.setFactory_id(factory_id);
				QSPacking objQSPacking = qsPackingRepository.save(qspacking);
				count = objQSPacking.getPnId();
				valCount = 0;

				// int count =
				// qsPackingRepository.insertQSPackingMasterRecord(con_id,load_id,filepath,transport_name,vechile_no,freight,milestone_id,taxexemstatus,taxexemamount,created_by);

				for (Map<String, String> item : obj) {
					String qty = item.get("qty");
					String per_kgs = item.get("per_kgs");
					String unit_price = item.get("unit_price");
					String total = item.get("total");
					String UOM_id = item.get("unit_id");
					String type_id = item.get("type_id");// inc_type changed to type_id
					String pices = item.get("pices");
					String pn_id = String.valueOf(count);
					valCount = qsPackingRepository.insertQSPackingItemRecord(qty, per_kgs, unit_price, total, UOM_id,
							type_id, pices, created_by, pn_id,factory_id);
				}
			}
			response.put("message", (count > 0 && valCount > 0) ? "success" : "failure");
			response.put("status", (count > 0 && valCount > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/*@PostMapping("/qspackingmaster/update")
	public @ResponseBody Map<String, Object> updateQSPackingMaster(@RequestParam("file") MultipartFile file,
			   @RequestParam("val") String valueDataJson, @RequestParam("obj") String objDataJson, @RequestParam("addPacking") String addPacking) {
	    Map<String, Object> response = new HashMap<String, Object>();
	    Map<String, String> val = null;
	    List<Map<String, String>> obj = null;
	    List<Map<String, String>> obj1 = null;
	    int checkLocked = 0;
	    String uniqueFileName = null;
	    try {
	        // Convert the 'val' and 'obj' JSON strings into Java objects
	        ObjectMapper objectMapper = new ObjectMapper();
	        
	        val = objectMapper.readValue(valueDataJson, new TypeReference<Map<String, String>>() {});
	        obj = objectMapper.readValue(objDataJson, new TypeReference<List<Map<String, String>>>() {});
	        obj1 = objectMapper.readValue(addPacking, new TypeReference<List<Map<String, String>>>() {});
			int valCount = 0;
			int count = 0;
			String  uploadedFilePath = null;
			// Extract the 'obj' part, which is a list of maps
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().equals("application/pdf")) {
					response.put("message", "Only PDF files are allowed.");
					response.put("status", "no");
					return response;
				}
              uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
             File directory = new File(uploadDir);
             if (!directory.exists()) {
                 directory.mkdirs(); // Create the directory if it doesn't exist
             }

             // Save the file
             File serverFile = new File(directory, uniqueFileName);
             if (serverFile.exists()) {
             	response.put("message", "File already exists! Please provide another File.");
                 return response;
             }
             file.transferTo(serverFile);

             // Set the uploaded file path
           uploadedFilePath = serverFile.getAbsolutePath();
			}
			if ((val != null && !val.isEmpty()) && (obj != null && !obj.isEmpty())) {
				String con_id = val.get("con_id");
				String load_id = val.get("load_id");
				String lot_no = val.get("lot_no");
				String filepath = uniqueFileName;
				String transport_name = val.get("transport_name");
				String vechile_no = val.get("vechile_no");
				String freight = val.get("freight");
				String milestone_id = val.get("milestone_id");
				String milestone_name = val.get("milestone_name");
				String taxexemstatus = val.get("taxexemstatus");
				String taxexemamount = val.get("taxexemamount");
				String modified_by = val.get("modified_by");
				String pn_id = val.get("pn_id");
				checkLocked  = qsPackingRepository.checkIsLocked(pn_id);
				if(checkLocked > 0) {
					response.put("message", "Invoice Already Generated Not Able to Update");
					return response;
				}
				qsPackingRepository.insterQSPackingMasterHistoryRecord(modified_by, pn_id);
				count = qsPackingRepository.updateQSPackingMasterRecord(con_id, load_id, filepath, transport_name,
						vechile_no, freight, milestone_id, taxexemstatus, taxexemamount, modified_by, lot_no, pn_id);
				for (Map<String, String> item : obj) {
					String qty = item.get("qty");
					String per_kgs = item.get("per_kgs");
					String unit_price = item.get("unit_price");
					String total = item.get("total");
					String UOM_id = item.get("unit_id");
					String type_id = item.get("type_id");// inc_type changed to type_id
					String pices = item.get("pices");
					String slno = item.get("slno");
					qsPackingRepository.insterQSPackingItenMasterHistoryRecord(modified_by, slno);
					valCount = qsPackingRepository.updateQsPackingItemsRecord(qty, per_kgs, unit_price, total, UOM_id,
							type_id, pices, modified_by, pn_id, slno);
				}
				if(obj1 != null && !obj1.isEmpty()) {
				for (Map<String, String> item : obj1) {
					String qty = item.get("qty");
					String per_kgs = item.get("per_kgs");
					String unit_price = item.get("unit_price");
					String total = item.get("total");
					String UOM_id = item.get("unit_id");
					String type_id = item.get("type_id");// inc_type changed to type_id
					String pices = item.get("pices");
					qsPackingRepository.insertQSPackingItemRecord(qty, per_kgs, unit_price, total, UOM_id,
							type_id, pices, modified_by, pn_id);
				}
				}
			}
			response.put("message", (count > 0 && valCount > 0) ? "success" : "failure");
			response.put("status", (count > 0 && valCount > 0) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	} */
	@PostMapping("/qspackingmaster/update") // before delete move to history table and delete
	public @ResponseBody Map<String, Object> updateQSPackingMaster(@RequestParam("file") MultipartFile file,
			   @RequestParam("val") String valueDataJson, @RequestParam("obj") String objDataJson) {
	    Map<String, Object> response = new HashMap<String, Object>();
	    Map<String, String> val = null;
	    List<Map<String, String>> obj = null;
		   List<Map<String, String>> obj1 = null;
	    int checkLocked = 0;
	    
	    try {
	        // Convert the 'val' and 'obj' JSON strings into Java objects
	        ObjectMapper objectMapper = new ObjectMapper();
	        String uniqueFileName = null;
	        val = objectMapper.readValue(valueDataJson, new TypeReference<Map<String, String>>() {});
	  	  obj = objectMapper.readValue(objDataJson, new TypeReference<List<Map<String, String>>>() {}); 
			int valCount = 0;
			int count = 0;
			int updateCount = 0;
			if (file != null && !file.isEmpty()) {
				if (!file.getContentType().equals("application/pdf")) {
					response.put("message", "Only PDF files are allowed.");
					response.put("status", "no");
					return response;
				}
				  // Generate a unique file name
             uniqueFileName = UUID.randomUUID() + "_@ALTER@_" + file.getOriginalFilename();
             File directory = new File(uploadDir);
             if (!directory.exists()) {
                 directory.mkdirs(); // Create the directory if it doesn't exist
             }

             // Save the file
             File serverFile = new File(directory, uniqueFileName);
             if (serverFile.exists()) {
             	response.put("message", "File already exists! Please provide another File.");
                 return response;
             }
             file.transferTo(serverFile);
			}
			if ((val != null && !val.isEmpty()) && (obj != null && !obj.isEmpty())) {
				String con_id = val.get("con_id");
				String load_id = val.get("load_id");
				String lot_no = val.get("lot_no");
				String filepath = uniqueFileName;
				String transport_name = val.get("transport_name");
				String vechile_no = val.get("vechile_no");
				String freight = val.get("freight");
				String milestone_id = val.get("milestone_id");
				//String milestone_name = val.get("milestone_name");
				String taxexemstatus = val.get("taxexemstatus");
				String taxexemamount = val.get("taxexemamount");
				String modified_by = val.get("modified_by");
				String pn_id = val.get("pn_id");
				checkLocked  = qsPackingRepository.checkIsLocked(pn_id);
				if(checkLocked > 0) {
					response.put("message", "Invoice Already Generated Not Able to Update");
					return response;
				}
				qsPackingRepository.insterQSPackingMasterHistoryRecord(modified_by, pn_id);
				count = qsPackingRepository.updateQSPackingMasterRecord(con_id, load_id, filepath, transport_name,
							vechile_no, freight, milestone_id, taxexemstatus, taxexemamount, modified_by, lot_no, pn_id);
				for (Map<String, String> item : obj) {
					String qty = item.get("qty");
					String per_kgs = item.get("per_kgs");
					String unit_price = item.get("unit_price");
					String total = item.get("total");
					String UOM_id = item.get("unit_id");
					String type_id = item.get("type_id");// inc_type changed to type_id
					String pices = item.get("pices");
					String isChanged = item.get("isChanged");
					String slno = item.get("slno");
					if(slno.equalsIgnoreCase("no")) { // if slno is no assume that new row going to insert
						valCount =	qsPackingRepository.insertQSPackingItemRecordWIthoutFactory(qty, per_kgs, unit_price, total, UOM_id,
								type_id, pices, modified_by, pn_id);
					}
					if(isChanged.equalsIgnoreCase("yes")) { // only that packing note changed then only move to history and update
						qsPackingRepository.updateQSPackingItemMasterHistoryRecordSlno(modified_by,slno);
						updateCount =	qsPackingRepository.updateQsPackingItemsRecord(qty, per_kgs, unit_price, total, UOM_id, type_id, pices, modified_by, pn_id, slno);
					}					
				}
			}
			response.put("message", (count > 0 && (valCount > 0 || updateCount >0)) ? "success" : "failure");
			response.put("status", (count > 0 && (valCount > 0 || updateCount >0)) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	

	@PostMapping("/qspackingmaster/delete")
	public @ResponseBody Map<String, Object> deleteQsPackingRecord(@RequestBody Map<String, String> val) {

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String pn_id = val.get("pn_id");
			qsPackingRepository.updateIsDeleteQSPackingMasterHistoryRecord(modified_by, pn_id);
			int count = qsPackingRepository.delteQsPackingMasterRecord(pn_id, modified_by);
			qsPackingRepository.updateQSPackingItemMasterHistoryRecord(modified_by, pn_id);
			int count1 = qsPackingRepository.delteQsPackingItemMasterRecord(pn_id, modified_by);
			response.put("message", (count > 0 && count1 > 0) ? "success" : "failure");
			response.put("status", (count > 0 && count1 > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/qspackingitemmaster/delete")
	public @ResponseBody Map<String, Object> deleteQsPackingItemRecord(@RequestBody Map<String, String> val) {

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String slno = val.get("slno");
			qsPackingRepository.IsDeleteQSPackingItemMasterHistoryRecord(modified_by, slno);
			int count = qsPackingRepository.delteQsPackingItemRecord(slno, modified_by);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingmaster/list/{factory_id}")
	public @ResponseBody Map<String, Object> listQSPackinglist(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPackingInterfaces> qsPackingInterfaces = null;
		List<QSPackingItemsInterfaces> qsPackingItemsInterfaces = null;
		try {
			qsPackingInterfaces = qsPackingRepository.listQSPAckingMasterRecord(factory_id);
			qsPackingItemsInterfaces = qsPackingRepository.listQSPAckingItemMasterRecord(factory_id);
			response.put("QSPACKING", qsPackingInterfaces);
			response.put("QSPACKING_ITEM", qsPackingItemsInterfaces);
			response.put("message",
					(qsPackingInterfaces != null && qsPackingItemsInterfaces != null) ? "success" : "failure");
			response.put("status", (qsPackingInterfaces != null && qsPackingItemsInterfaces != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingmastersearch/{pn_id}")
	public @ResponseBody Map<String, Object> serachQSPackingId(@PathVariable String pn_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		try {
			qsPackingInterfaces = qsPackingRepository.searchQSPackingById(pn_id);
			response.put("message", (qsPackingInterfaces != null) ? "success" : "failure");
			response.put("status", (qsPackingInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSPACKINGMASTER");
			response.put("DATA", qsPackingInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingitemmastersearch/{slno}")
	public @ResponseBody Map<String, Object> serachQSPackingItemId(@PathVariable String slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		QSPackingItemsInterfaces qsPackingItemsInterfaces = null;
		try {
			qsPackingItemsInterfaces = qsPackingRepository.searchQSPackingItemById(slno);
			response.put("message", (qsPackingItemsInterfaces != null) ? "success" : "failure");
			response.put("status", (qsPackingItemsInterfaces != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_QSPACKINGITEMMASTER");
			response.put("DATA", qsPackingItemsInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingmasteruom/list")
	public @ResponseBody Map<String, Object> listUOMList() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = qsPackingRepository.listUOM();
			response.put("message", (value != null) ? "success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
			response.put("UOM LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/qspackingmasterscraptype/list/{factory_id}")
	public @ResponseBody Map<String, Object> listScrapType(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> value = null;
		try {
			value = qsPackingRepository.listScrapType(factory_id);
			response.put("message", (value != null) ? "success" : "failure");
			response.put("status", (value != null) ? "yes" : "no");
			response.put("action", "List_Record_In_QSPACKINGMASTER");
			response.put("UOM LIST", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	 @GetMapping("/qspackingmaster/viewpdf/{id}")
	    public ResponseEntity<Resource> viewFile(@PathVariable int id) {
	        try {
	        	Optional<Integer> value = qsPackingRepository.getPn_id(String.valueOf(id));
	        	 if (value.isEmpty()) {
	                 return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                         .body(null);
	             }
	             int valuePnid = value.get();
	             String filepath = qsPackingRepository.getFilePath(valuePnid);
	             String folderPath = getUploadDir().toString();
	             String fileName = filepath; // This is the file name from the database


	          // Concatenate folder path and file name to create the full file path
	             Path path = Paths.get(folderPath, fileName).normalize();
	             
	             // Normalize and validate file path
	          // Validate the full file path
	             if (!Files.exists(path) || !path.startsWith(Paths.get(folderPath).normalize())) {
	                 throw new RuntimeException("File not found or unauthorized access.");
	             }

	             // Encode the file name
	             String encodedFileName = URLEncoder.encode(path.getFileName().toString(), StandardCharsets.UTF_8);

	             // Load the file as a resource
	             Resource resource = new UrlResource(path.toUri());
	             if (!resource.exists() || !resource.isReadable()) {
	                 throw new RuntimeException("File not found or is not readable: " + filepath);
	             }

	             // Set the Content-Disposition header to inline for viewing in the browser
	             String contentType = "application/pdf"; // Assuming it's a PDF file
	             return ResponseEntity.ok()
	                     .contentType(MediaType.parseMediaType(contentType))
	                     .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\"")
	                     .body(resource);

	         } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(null);
	        }
	    }
}
