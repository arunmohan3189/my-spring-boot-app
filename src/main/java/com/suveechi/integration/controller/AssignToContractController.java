package com.suveechi.integration.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suveechi.integration.interfaces.AssignToContract;
import com.suveechi.integration.interfaces.BankMasterInterface;
import com.suveechi.integration.interfaces.BusinessUnitInterface;
import com.suveechi.integration.interfaces.ContractListFromContractInterfaces;
import com.suveechi.integration.interfaces.InvoiceConsigneeAddressInterface;
import com.suveechi.integration.interfaces.MileStoneAssignedContractListInterfaces;
import com.suveechi.integration.interfaces.ServiceCodeMasterInterface;
import com.suveechi.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.suveechi.integration.interfaces.TaxMasterInterface;
import com.suveechi.integration.interfaces.WorkOrderMasterInterface;
import com.suveechi.integration.repository.AssignToContractRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class AssignToContractController {

	@Autowired
	public AssignToContractRepository assignToContractRepository;

	private int parseInteger(String value, int defaultValue) {
		try {
			return value != null ? Integer.parseInt(value) : defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue; // Default value for invalid integers
		}
	}

	@PostMapping("/contract/add")
	public @ResponseBody Map<String, Object> createContract(@RequestBody Map<String, Object> data) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Map<String, String> val = (Map<String, String>) data.get("val");
			int contract_id = parseInteger(val.get("contract_id"), 0);
			int bid = parseInteger(val.get("bid"), 0);
			String invoice_type_calculation = val.get("invoice_type_calculation");
			String percentage_value = val.get("percentage_value") != null ? val.get("percentage_value") : null;
			int invoice_to_id = parseInteger(val.get("invoice_to_id"), 0);
			int consignee_id = parseInteger(val.get("consignee_id"), 0);
			int shipment_mode_id = parseInteger(val.get("shipment_mode_id"), 0);
			int delivery_condition_id = parseInteger(val.get("delivery_condition_id"), 0);
			int bank_details_id = parseInteger(val.get("bank_details_id"), 0);
			int work_id = parseInteger(val.get("work_id"), 0);
			int regd_office_id = parseInteger(val.get("regd_office_id"), 0);
			int s_code = parseInteger(val.get("s_code"), 0);
			int h_code = parseInteger(val.get("h_code"), 0);
			String contract_name = val.get("contract_name") != null ? val.get("contract_name") : null;
			String product_desc_id = val.get("product_desc_id") != null ? val.get("product_desc_id") : null;
			String export = val.get("export") != null ? val.get("export") : null; // Allow null for nullable Strings
			String tax_ex_inc = val.get("tax_ex_inc") != null ? val.get("tax_ex_inc") : null;
			String taxable = val.get("taxable") != null ? val.get("taxable") : null;
			String non_taxable = val.get("non_taxable") != null ? val.get("non_taxable") : null;
			String tax_payable = val.get("tax_payable") != null ? val.get("tax_payable") : null;
			String freight_advance_recovery = val.get("freight_advance_recovery") != null ? val.get("freight_advance_recovery")	: null;
			String area_no = val.get("area_no") != null ? val.get("area_no") : null;
			String lot_no = val.get("lot_no") != null ? val.get("lot_no") : null;
			String containter_no = val.get("containter_no") != null ? val.get("containter_no") : null;
			String epcgno = val.get("epcgno") != null ? val.get("epcgno") : null;
			String export_title_text = val.get("export_title_text") != null ? val.get("export_title_text") : null;
			String created_by = val.get("created_by") != null ? val.get("created_by") : null;
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
			// Extract the 'obj' part, which is a list of maps
			List<Map<String, Object>> taxAdd = (List<Map<String, Object>>) (data.get("taxAdd") != null ? data.get("taxAdd") : new ArrayList<>()) ;
			for (Map<String, Object> item : taxAdd) {
				int tax_id = parseInteger(String.valueOf(item.get("tax_id")), 0);
				assignToContractRepository.insertTaxContract(contract_id, tax_id,factory_id,created_by);
			}
			int count = assignToContractRepository.createContractInfo(contract_id, bid, invoice_type_calculation,
					percentage_value, invoice_to_id, consignee_id, shipment_mode_id, delivery_condition_id,
					product_desc_id, bank_details_id, work_id, regd_office_id, s_code, h_code, export, tax_ex_inc,
					taxable, non_taxable, tax_payable, freight_advance_recovery, area_no, lot_no, containter_no, epcgno,
					export_title_text, created_by,contract_name,factory_id);
			response.put("action", "INSERT_CONTRACT");
			response.put("message", (count > 0) ? "Success" : "failed");
			response.put("status", (count > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/contract/update")
	public @ResponseBody Map<String, Object> updateContract(@RequestBody Map<String, Object> data) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Map<String, String> val = (Map<String, String>) data.get("val");
			int contract_id = parseInteger(val.get("contract_id"), 0);
			int bid = parseInteger(val.get("bid"), 0);
			String invoice_type_calculation = val.get("invoice_type_calculation");
			String percentage_value = val.get("percentage_value") != null ? val.get("percentage_value") : null;
			int invoice_to_id = parseInteger(val.get("invoice_to_id"), 0);
			int consignee_id = parseInteger(val.get("consignee_id"), 0);
			int shipment_mode_id = parseInteger(val.get("shipment_mode_id"), 0);
			int delivery_condition_id = parseInteger(val.get("delivery_condition_id"), 0);
			int bank_details_id = parseInteger(val.get("bank_details_id"), 0);
			int work_id = parseInteger(val.get("work_id"), 0);
			int regd_office_id = parseInteger(val.get("regd_office_id"), 0);
			int s_code = parseInteger(val.get("s_code"), 0);
			int h_code = parseInteger(val.get("h_code"), 0);
			String product_desc_id = val.get("product_desc_id") != null ? val.get("product_desc_id") : null;
			String contract_name = val.get("contract_name") != null ? val.get("contract_name") : null;
			String export = val.get("export") != null ? val.get("export") : null; // Allow null for nullable Strings
			String tax_ex_inc = val.get("tax_ex_inc") != null ? val.get("tax_ex_inc") : null;
			String taxable = val.get("taxable") != null ? val.get("taxable") : null;
			String non_taxable = val.get("non_taxable") != null ? val.get("non_taxable") : null;
			String tax_payable = val.get("tax_payable") != null ? val.get("tax_payable") : null;
			String freight_advance_recovery = val.get("freight_advance_recovery") != null ? val.get("freight_advance_recovery") : null;
			String area_no = val.get("area_no") != null ? val.get("area_no") : null;
			String lot_no = val.get("lot_no") != null ? val.get("lot_no") : null;
			String containter_no = val.get("containter_no") != null ? val.get("containter_no") : null;
			String epcgno = val.get("epcgno") != null ? val.get("epcgno") : null;
			String export_title_text = val.get("export_title_text") != null ? val.get("export_title_text") : null;
			String modified_by = val.get("modified_by") != null ? val.get("modified_by") : null;
			int con_slno = parseInteger(val.get("con_slno"), 0);
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
			int check = assignToContractRepository.checkContractIdisLocked(con_slno);
			if(check > 0) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			// Extract the 'obj' part, which is a list of maps
			//assignToContractRepository.deleteContractIdInAssignToContract(contract_id);
			List<Map<String, Object>> taxAdd = (List<Map<String, Object>>) (data.get("taxAdd") != null ? data.get("taxAdd") : new ArrayList<>()) ;
			for (Map<String, Object> item : taxAdd) {
				int tax_id = parseInteger(String.valueOf(item.get("tax_id")), 0);
				String added = (String) item.get("added");
				String removed = (String) item.get("removed");
				if(added.equalsIgnoreCase("yes")) {
					assignToContractRepository.insertTaxContract(contract_id, tax_id,factory_id,modified_by);
				}
				if(removed.equalsIgnoreCase("yes")) {
					assignToContractRepository.moveToHistoryTaxContract(tax_id,contract_id,modified_by);
					assignToContractRepository.deleteTaxContract(tax_id,contract_id);
				}
				
			}
//			List<Map<String, Object>> taxDelete = (List<Map<String, Object>>) (data.get("taxDelete") != null ? data.get("taxDelete") : new ArrayList<>());
//			for (Map<String, Object> item : taxDelete) {
//				int id = parseInteger(String.valueOf(item.get("id")), 0);
//				assignToContractRepository.deleteTaxContract(id);
//			}
			assignToContractRepository.insertContracrtHistory(con_slno, modified_by);
			int count = assignToContractRepository.updateContractInfo(contract_id, bid, invoice_type_calculation,
					percentage_value, invoice_to_id, consignee_id, shipment_mode_id, delivery_condition_id,
					product_desc_id, bank_details_id, work_id, regd_office_id, s_code, h_code, export, tax_ex_inc,
					taxable, non_taxable, tax_payable, freight_advance_recovery, area_no, lot_no, containter_no, epcgno,
					export_title_text, modified_by,contract_name, con_slno);
			response.put("action", "UPDATE_CONTRACT");
			response.put("message", (count > 0) ? "Success" : "failed");
			response.put("status", (count > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/contract/delete")
	public @ResponseBody Map<String, Object> deleteContract(@RequestBody Map<String, Object> data) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Map<String, String> val = (Map<String, String>) data.get("val");
			String modified_by = val.get("modified_by");
			String con_slno = val.get("con_slno");
			assignToContractRepository.updateContracrHistory(con_slno, modified_by);
			int count = assignToContractRepository.updateContractDelete(modified_by, con_slno);
			List<Map<String, Object>> taxDelete = (List<Map<String, Object>>) (data.get("taxDelete") != null ? data.get("taxDelete") : new ArrayList<>());
			for (Map<String, Object> item : taxDelete) {
				int id = parseInteger(String.valueOf(item.get("id")), 0);
				assignToContractRepository.deleteTaxContract(id);
			}
			response.put("action", "DELETE_CONTRACT");
			response.put("message", (count > 0) ? "Success" : "failed");
			response.put("status", (count > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listcontract/{factory_id}")
	public @ResponseBody Map<String, Object> listContract(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<AssignToContract> count = assignToContractRepository.getContractList(factory_id);
			response.put("action", "LIST WORK ORDER");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/contract/listcontractfromcontract/{factory_id}")
	public @ResponseBody Map<String, Object> listContractFromContract(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ContractListFromContractInterfaces> count = assignToContractRepository.getContractListFromContract(factory_id);
			response.put("action", "LIST CONTRACT FROM CONTRACT TABLE");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
//	@GetMapping("/contract/listcontractg2")
//	public @ResponseBody Map<String, Object> listContractg2() {
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<ContractG2Interfaces> count =  null;
//		try {
//			count = assignToContractRepository.getContractListG2();
//			response.put("action", "LIST Contracts ID");
//			response.put("message", (count != null) ? "Success" : "failed");
//			response.put("status", (count != null) ? "yes" : "no");
//			response.put("Data", count);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}

//	@GetMapping("/contract/listcontractg2unique")
//	public @ResponseBody Map<String, Object> listContractUniqueG2() {
//		Map<String, Object> response = new HashMap<String, Object>();
//		List<ContractG2Interfaces> count =  null;
//		try {
//			count = assignToContractRepository.getContractListUnique();
//			response.put("action", "LIST Contracts ID");
//			response.put("message", (count != null) ? "Success" : "failed");
//			response.put("status", (count != null) ? "yes" : "no");
//			response.put("Data", count);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
		
	@GetMapping("/contract/searchcontract/{con_slno}")
	public @ResponseBody Map<String, Object> searContractById(@PathVariable String con_slno) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<AssignToContract> assignToContract = null;
		try {
			assignToContract = assignToContractRepository.searchContractinInvoice(con_slno);
			response.put("message", (assignToContract != null) ? "success" : "failure");
			response.put("status", (assignToContract != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_Contract_LIST");
			response.put("DATA", assignToContract);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listworkorder/{factory_id}")
	public @ResponseBody Map<String, Object> listWorkOrder(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<WorkOrderMasterInterface> count = assignToContractRepository.getWorkOrderList(factory_id);
			response.put("action", "LIST WORK ORDER");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listbankname/{factory_id}")
	public @ResponseBody Map<String, Object> listbankname(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<BankMasterInterface> count = assignToContractRepository.getBankNameList(factory_id);
			response.put("action", "LIST BANK NAME");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listbusinessunit/{factory_id}")
	public @ResponseBody Map<String, Object> listBusinessUnit(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<BusinessUnitInterface> count = assignToContractRepository.getBusinessUnitList(factory_id);
			response.put("action", "LIST BUSINESS UNIT NAME");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/list_hsn_servicecode/{factory_id}")
	public @ResponseBody Map<String, Object> listServiceCode(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ServiceCodeMasterInterface> count = assignToContractRepository.getServiceCode(factory_id);
			response.put("action", "LIST SERVICE HSN CODE");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listinvoiceconsinee/{factory_id}")
	public @ResponseBody Map<String, Object> listInvoiceConsinee(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<InvoiceConsigneeAddressInterface> count = assignToContractRepository.getInvoiceConsinee(factory_id);
			response.put("action", "LIST INVOICE CONSINEE ADDRESS");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listdeliveryshipment/{factory_id}")
	public @ResponseBody Map<String, Object> listShipmentDeliveryCondition(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<ShipmentDeliveryConditionInterfaces> count = assignToContractRepository.getShipmentDeliveryCondition(factory_id);
			response.put("action", "LIST DELIVERY SHIPMENT CONDITION");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/contract/listtaxmaster")
	public @ResponseBody Map<String, Object> listTaxMaster() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<TaxMasterInterface> count = assignToContractRepository.getTaxMaster();
			response.put("action", "LIST TAX MASTER");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("Data", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/milestoneassigncontract")
	public @ResponseBody Map<String, Object> insertMilestoneForContract(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String created_by = val.get("created_by");
			String milestone_id = val.get("milestone_id");
			String contract_id = val.get("contract_id");
			String factory_id = val.get("factory_id");
			int value = assignToContractRepository.checkDuplicateMilestone(milestone_id,contract_id,factory_id);
			if(value == 0) {
				int count = assignToContractRepository.assignMileStoneToContract(milestone_id,contract_id,created_by,factory_id);
				response.put("action", "ADD_MILESTONE_FOR_CONTRACT");
				response.put("message", (count > 0) ? "Success" : "failed");
				response.put("status", (count > 0) ? "yes" : "no");
			}else {
			response.put("action", "ADD_MILESTONE_FOR_CONTRACT");
			response.put("message", (value > 0) ? "MileStone Already Exits For That Contractor" : "failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/litcontractmaster/{contract_id}/{factory_id}")
	public @ResponseBody Map<String, Object> listContractFromContractMaster(@PathVariable int contract_id,@PathVariable int factory_id) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<MileStoneAssignedContractListInterfaces> count = assignToContractRepository.listContractMaster(contract_id,factory_id);
			response.put("action", "LIST_FROM_CONTRACT_MASTER");
			response.put("message", (count != null) ? "Success" : "failed");
			response.put("status", (count != null) ? "yes" : "no");
			response.put("DATA", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
