package com.suveechi.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suveechi.integration.interfaces.Advance_QSPacking_QSPackingItem_LIST_INTERFACES;
import com.suveechi.integration.interfaces.AssignToContract;
import com.suveechi.integration.interfaces.InvoiceMasterInterface;
import com.suveechi.integration.repository.AssignToContractRepository;
import com.suveechi.integration.repository.InvoiceRepository;
import com.suveechi.integration.repository.QSAdvancePackingRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class AdvanceInvoiceController {
	
	@Autowired
	public AssignToContractRepository assignToContractRepository;
	@Autowired
	public QSAdvancePackingRepository qsAdvancePackingRepository;
	@Autowired
	public InvoiceRepository invoiceRepository;
	
	@PostMapping("/advanceinvoice/add")
	public @ResponseBody Map<String, Object> createAdvanceInvoice(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int contract_id = Integer.parseInt(val.get("contract_id"));
			String load_id = val.get("load_id");
			int  contract_slno   =  Integer.parseInt(val.get("contract_slno"));
			String created_by = val.get("created_by");
			String product_desc = val.get("product_desc");
			String remarks = val.get("remarks");
			String  date_of_notification  = val.get("date_of_notification");
			String  date_val = val.get("date_val");
			String  bg_type  = val.get("bg_type");
			String  date_of_issue  = val.get("date_of_issue");
			String  reference_no = val.get("reference_no");
			String  lc_number  = val.get("lc_number");
			String  supply_place = val.get("supply_place");
			String  s_t_exempted = val.get("s_t_exempted");
			String  lr_docketno = val.get("lr_docketno");
			String  bg_no = val.get("bg_no");
			String  date_of_expiry  = val.get("date_of_expiry");
			String  date_of_ref = val.get("date_of_ref");
			String  lc_issue_date = val.get("lc_issue_date");
			String contract_name = val.get("contract_name");
			String pn_id = val.get("pn_id");
			int count =  qsAdvancePackingRepository.insertAdvanceInvoice(contract_id, load_id, contract_slno , product_desc, remarks, date_of_notification, date_val, bg_type,
			date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,contract_name,pn_id,created_by);
			response.put("action", "ADVANCEINVOICE_ADD");
			response.put("message",	(count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/advanceinvoice/update")
	public @ResponseBody Map<String, Object> updateAdvanceInvoice(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int contract_id = Integer.parseInt(val.get("contract_id"));
			String load_id = val.get("load_id");
			int  contract_slno   =  Integer.parseInt(val.get("contract_slno"));
			String modified_by = val.get("modified_by");
			String product_desc = val.get("product_desc");
			String remarks = val.get("remarks");
			String  date_of_notification  = val.get("date_of_notification");
			String  date_val = val.get("date_val");
			String  bg_type  = val.get("bg_type");
			String  date_of_issue  = val.get("date_of_issue");
			String  reference_no = val.get("reference_no");
			String  lc_number  = val.get("lc_number");
			String  supply_place = val.get("supply_place");
			String  s_t_exempted = val.get("s_t_exempted");
			String  lr_docketno = val.get("lr_docketno");
			String  bg_no = val.get("bg_no");
			String  date_of_expiry  = val.get("date_of_expiry");
			String  date_of_ref = val.get("date_of_ref");
			String  lc_issue_date = val.get("lc_issue_date");
			String contract_name = val.get("contract_name");
			String pn_id = val.get("pn_id");
			int  id  =  Integer.parseInt(val.get("id"));
			int count =  qsAdvancePackingRepository.updateAdvanceInvoice(contract_id, load_id, contract_slno , product_desc, remarks, date_of_notification, date_val, bg_type,
			date_of_issue,reference_no,lc_number,supply_place,s_t_exempted,lr_docketno,bg_no,date_of_expiry,date_of_ref,lc_issue_date,contract_name,pn_id,modified_by,id);
			response.put("action", "ADVANCEINVOICE_UPDATE");
			response.put("message",	(count > 0) ? "Success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/advanceinvoice/list")
	public @ResponseBody Map<String,Object> advanceInvoicelist(){
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceMasterInterface> invoiceMasterInterfaces = null;
		try {
			invoiceMasterInterfaces = qsAdvancePackingRepository.listAdvanceInvoiceMasterInfo();
			response.put("message", (invoiceMasterInterfaces != null)?"success":"failure");
			response.put("status", (invoiceMasterInterfaces != null)?"yes":"no");
			response.put("action", "List_Record_In_ADVANCEINVOICE_MASTER");
			response.put("Data", invoiceMasterInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	@GetMapping("/advanceinvoicemaster/getloadid")
	public @ResponseBody Map<String, Object> serachListLoadId() {
		Map<String, Object> response = new HashMap<String, Object>();
		String invoiceValue = "ADV-";
		try {
			int numberOfRows = qsAdvancePackingRepository.getRowCount();
			if(numberOfRows == 0) {
				invoiceValue = "ADV-1";
				numberOfRows++;
			}else {
				numberOfRows = numberOfRows+1;
				invoiceValue = invoiceValue+numberOfRows;
			}
			response.put("message", (numberOfRows > 0) ? "success" : "failure");
			response.put("status", (numberOfRows > 0) ? "yes" : "no");
			response.put("action", "ListLoadId_Record");
			response.put("data", invoiceValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/advanceinvoicemaster/delete/{id}/{modified_by}")
	public @ResponseBody Map<String, Object> deleteId(@PathVariable int id,@PathVariable String modified_by) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			int pn_id = qsAdvancePackingRepository.getPnId(id);
			int numberOfRows = qsAdvancePackingRepository.deleteAdvanceInvoice(id,modified_by);
			qsAdvancePackingRepository.delteQSAdvancePackingMasterRecord(String.valueOf(pn_id), modified_by);
			qsAdvancePackingRepository.delteQSAdvancePackingItemMasterRecord(String.valueOf(pn_id), modified_by);
			response.put("message", (numberOfRows > 0) ? "success" : "failure");
			response.put("status", (numberOfRows > 0) ? "yes" : "no");
			response.put("action", "DELETE_ADVANCEINVOICE_RECORD");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	@GetMapping("/advanceinvoicemaster/search/{id}/{contract_id}/{factory_id}")
	public @ResponseBody Map<String, Object> searchId(@PathVariable int id, @PathVariable int contract_id, @PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<AssignToContract> assignToContract = null;
		List<Advance_QSPacking_QSPackingItem_LIST_INTERFACES> qsPackingInterfaces = null;
		InvoiceMasterInterface invoiceMasterInterfaces = null;
		try {
			invoiceMasterInterfaces = qsAdvancePackingRepository.getInfo(id);
			String pn_id = (invoiceMasterInterfaces != null) ? invoiceMasterInterfaces.getPn_id() : "0";			
			qsPackingInterfaces = qsAdvancePackingRepository.getAdvancePackingAndItemNote(pn_id);
			assignToContract = assignToContractRepository.searchContract(String.valueOf(contract_id),factory_id);
			response.put("message", (assignToContract != null &&qsPackingInterfaces != null && invoiceMasterInterfaces != null) ? "success" : "failure");
			response.put("status", (assignToContract != null &&qsPackingInterfaces != null && invoiceMasterInterfaces != null) ? "yes" : "no");
			response.put("action", "SEARCH_ADVANCEINVOICE_RECORD");
			response.put("AdvanceInvoice_DATA", invoiceMasterInterfaces);
			response.put("AdvanceQSPACKING_DATA", qsPackingInterfaces);
			response.put("ASSIGN_TO_CONTRACT_DATA", assignToContract);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/advanceinvoice/verification")
	public @ResponseBody Map<String, Object> invoiceVerfication(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		String newSeriesNumber = null;
		try {
			String verified_by =val.get("verified_by");
			int id = Integer.parseInt(val.get("id"));	
			Optional<Long> optionalSeriesNumber = qsAdvancePackingRepository.getAdvanceSeriesNumberbasedOnId(id);
			Optional<Long> optionalInvoiceNumber = qsAdvancePackingRepository.getInvoiceNumber();
			long invoiceNumber = optionalInvoiceNumber.orElse((long) 0);
			long serieNumber = optionalSeriesNumber.orElse((long) 0);
			
			if(invoiceNumber  == 0) {
				String num = String.valueOf(serieNumber);
				if(num != null && !num.equals("0")) {
					int size = num.lastIndexOf("00000");
					String partAfterZero = null;
					if(size != -1) {
						  partAfterZero = num.substring(size + 5); 
						int  a = Integer.parseInt(partAfterZero);
						a = a+1;
						partAfterZero = String.valueOf(a);
					}
					Long incrementedValue = Long.parseLong(partAfterZero);
					newSeriesNumber = num.substring(0, size + 5) + incrementedValue;
				count = qsAdvancePackingRepository.updateAdvanceInvoiceVerification(verified_by,newSeriesNumber,id);
				}
				else {
					response.put("Generate_Series_Number","Please Generate Series Number" );
					return response;
				}
				
			}else {
				if(serieNumber > 0) {
				String num = String.valueOf(invoiceNumber);
				int size = num.lastIndexOf("00000");
				String partAfterZero = null;
				if(size != -1) {
					  partAfterZero = num.substring(size + 5);// total length from start to till 5(00000) (+5 means number of Zeros)
					int  a = Integer.parseInt(partAfterZero);
					a = a+1;
					partAfterZero = String.valueOf(a);
				}
				Long incrementedValue = Long.parseLong(partAfterZero);
				newSeriesNumber = num.substring(0, size + 5) + incrementedValue;
				count = qsAdvancePackingRepository.updateAdvanceInvoiceVerification(verified_by,newSeriesNumber,id);
				//invoiceRepository.verificationInvoiceHistory(verified_by, id);
				}
				else {
					response.put("Generate_Series_Number","Please Generate Series Number" );
					return response;
				}
			}
			
			response.put("message", (count > 0)?"success":"failure");
			response.put("status", (count > 0)?"yes":"no");
			response.put("action", "VERIFICATION_Record_In_INVOICE_MASTER");
			response.put("Invoice_NO", newSeriesNumber);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
