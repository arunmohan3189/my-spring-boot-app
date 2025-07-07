package com.suveechi.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.suveechi.integration.interfaces.BankMasterInterface;
import com.suveechi.integration.interfaces.BusinessUnitInterface;
import com.suveechi.integration.interfaces.OrganizationMasterInterface;
import com.suveechi.integration.repository.OrganizationRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class OrganizationController {

	Logger logger = LogManager.getLogger(OrganizationController.class);

	@Autowired
	private OrganizationRepository organizationRepository;

	/** ORGANIZATION START **/
	@PostMapping("/addorganization")
	public @ResponseBody Map<String, Object> createOrganization(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: createOrganization");

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String org_name = val.get("org_name");
			String registered_address = val.get("registered_address");
			String business_address = val.get("business_address");
			String gst_number = val.get("gst_number");
			String location = val.get("location");
			int state_id = Integer.parseInt(val.get("state_id"));
			String created_by = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";

			int count = organizationRepository.addOrganization(org_name, registered_address, business_address,
					gst_number, location, created_by, state_id, factory_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_OrganizationMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createOrganization  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/updateorganization")
	public @ResponseBody Map<String, Object> updateOrganization(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: updateOrganization");

		Map<String, Object> response = new HashMap<String, Object>();
		int check = 0;
		int count = 0;
		try {
			String org_name = val.get("org_name");
			String registered_address = val.get("registered_address");
			String business_address = val.get("business_address");
			String gst_number = val.get("gst_number");
			String location = val.get("location");
			int state_id = Integer.parseInt(val.get("state_id"));
			String modified_by = val.get("modified_by");
			String org_id = val.get("org_id");
			check = organizationRepository.checkOrgIdPresentInContractMaster(org_id);
			if (check == 1) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			if (check > 1) {
				response.put("message", "Please Create New Master and Assign for that contract");
				return response;
			}
			organizationRepository.addOrganizationHistory(modified_by, org_id);
			count = organizationRepository.updateOrganization(org_name, registered_address, business_address,
					gst_number, location, modified_by, state_id, org_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_OrganizationMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateOrganization  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/deleteorganization")
	public @ResponseBody Map<String, Object> deleteOrganization(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: deleteOrganization");

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String org_id = val.get("org_id");
			organizationRepository.deleteOrganizationHistory(modified_by, org_id);
			int count = organizationRepository.deleteOrganization(modified_by, org_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "delete_Record_In_OrganizationMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteOrganization  -> " + e.getMessage());

		}
		return response;
	}

	@GetMapping("/listorganization/{factory_id}")
	public @ResponseBody Map<String, Object> listOrganization(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: listOrganization");

		Map<String, Object> response = new HashMap<String, Object>();
		List<OrganizationMasterInterface> organizationMaster = null;
		try {
			organizationMaster = organizationRepository.getOrganizationList(factory_id);
			response.put("message", (organizationMaster.size() > 0) ? "success" : "Organization List not available");
			response.put("status", (organizationMaster.size() > 0) ? "yes" : "no");
			response.put("Data", organizationMaster);
			response.put("action", "List_Record_In_OrganizationMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: listOrganization  -> " + e.getMessage());

		}
		return response;
	}

	@GetMapping("/searchorganziation/{org_id}")
	public @ResponseBody Map<String, Object> searchorganziationId(@PathVariable String org_id) {

		logger.info("EXECUTING METHOD :: searchorganziationId");

		Map<String, Object> response = new HashMap<String, Object>();
		OrganizationMasterInterface organizationMaster = null;
		try {
			organizationMaster = organizationRepository.searchOrganizationById(org_id);
			response.put("message", (organizationMaster != null) ? "success" : "failure");
			response.put("status", (organizationMaster != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_OrganizationMASTER");
			response.put("DATA", organizationMaster);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: searchorganziationId  -> " + e.getMessage());

		}
		return response;
	}

	/** ORGANIZATION END **/
	/** BANK START **/
	@PostMapping("/addbankaccount")
	public @ResponseBody Map<String, Object> createBankAccount(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: createBankAccount");

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String business_unit_id = val.get("business_unit_id");
			String bank_name = val.get("bank_name");
			String account_number = val.get("account_number");
			String ifsc_code = val.get("ifsc_code");
			String branch_address = val.get("branch_address");
			String state_id = val.get("state_id");
			String country_id = val.get("country_id");
			String city = val.get("city");
			String swift_code = val.get("swift_code");
			String branch_code = val.get("branch_code");
			String created_by = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
			int count = organizationRepository.addBankAccount(business_unit_id, bank_name, account_number, ifsc_code,
					branch_address, state_id, country_id, city, swift_code, branch_code, created_by, factory_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_BankMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createBankAccount  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/updatebankaccount")
	public @ResponseBody Map<String, Object> updateBankAccount(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: updateBankAccount");

		Map<String, Object> response = new HashMap<String, Object>();
		int count = 0;
		int check = 0;
		try {
			String business_unit_id = val.get("business_unit_id");
			String bank_name = val.get("bank_name");
			String account_number = val.get("account_number");
			String ifsc_code = val.get("ifsc_code");
			String branch_address = val.get("branch_address");
			String state_id = val.get("state_id");
			String country_id = val.get("country_id");
			String city = val.get("city");
			String swift_code = val.get("swift_code");
			String branch_code = val.get("branch_code");
			String modified_by = val.get("modified_by");
			String account_id = val.get("account_id");
			check = organizationRepository.checkBankIdPresentInContractMaster(account_id);
			if (check == 1) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			if (check > 1) {
				response.put("message", "Please Create New Master and Assign for that contract");
				return response;
			}
			organizationRepository.InsterBankAccountHistory(modified_by, account_id);
			count = organizationRepository.updateBankAccount(business_unit_id, bank_name, account_number, ifsc_code,
					branch_address, state_id, country_id, city, swift_code, branch_code, modified_by, account_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_BankMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateBankAccount  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/deletebankaccount")
	public @ResponseBody Map<String, Object> deleteBankAccount(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: deleteBankAccount");

		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String modified_by = val.get("modified_by");
			String account_id = val.get("account_id");
			organizationRepository.updateBankAccountHistory(modified_by, account_id);
			int count = organizationRepository.deleteBankAccount(modified_by, account_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "delete_Record_In_BankMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteBankAccount  -> " + e.getMessage());

		}
		return response;
	}

	@GetMapping("/listbankaccount/{factory_id}")
	public @ResponseBody Map<String, Object> listBankAccount(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: listBankAccount");

		Map<String, Object> response = new HashMap<String, Object>();
		List<BankMasterInterface> bankMasterInterfaces = null;
		try {
			bankMasterInterfaces = organizationRepository.getBankAccountList(factory_id);
			response.put("message", (bankMasterInterfaces != null) ? "success" : "failure");
			response.put("status", (bankMasterInterfaces != null) ? "yes" : "no");
			response.put("Data", bankMasterInterfaces);
			response.put("action", "List_Record_In_BankMaster");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: listBankAccount  -> " + e.getMessage());

		}
		return response;
	}

	@GetMapping("/searchbankaccount/{account_id}")
	public @ResponseBody Map<String, Object> searchBankAccountId(@PathVariable String account_id) {

		logger.info("EXECUTING METHOD :: searchBankAccountId");

		Map<String, Object> response = new HashMap<String, Object>();
		BankMasterInterface bankMasterInterface = null;
		try {
			bankMasterInterface = organizationRepository.searchBankAccountById(account_id);
			response.put("message", (bankMasterInterface != null) ? "success" : "failure");
			response.put("status", (bankMasterInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_BankMASTER");
			response.put("DATA", bankMasterInterface);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: searchBankAccountId  -> " + e.getMessage());

		}
		return response;
	}

	/** BANK END **/

	/** BUSINESS UNIT START **/
	@PostMapping("/businessunits/add")
	private @ResponseBody Map<String, Object> createBussinessUnits(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: createBussinessUnits");
		Map<String, Object> addBusinessUnitsMap = new HashMap<String, Object>();
		int value = 1100;
		try {
			int count = organizationRepository.getCount();
			String org_id = val.get("org_id");
			String business_unit_name = val.get("business_unit_name");
			String gst_number = val.get("gst_number");
			String location = val.get("location");
			int state_id = Integer.parseInt(val.get("state_id"));
			String created_by = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";
			value = value + count;
			int unitscount = organizationRepository.addBusinessUnits(org_id, business_unit_name, gst_number, location,
					state_id, created_by, value, factory_id);
			addBusinessUnitsMap.put("action", "AddBusinessUnits");
			addBusinessUnitsMap.put("message", (unitscount > 0) ? "Success" : "Business units details not added");
			addBusinessUnitsMap.put("status", (unitscount > 0) ? "yes" : "no");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: createBussinessUnits  -> " + e.getMessage());
			addBusinessUnitsMap.put("message", e.getMessage());
			addBusinessUnitsMap.put("status", "no");
		}
		return addBusinessUnitsMap;
	}

	@PostMapping("/businessunits/update")
	public @ResponseBody Map<String, Object> updateBusinessUnit(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: updateBusinessUnit");

		Map<String, Object> response = new HashMap<>();
		String gst_number = null;
		int count = 0;
		int check = 0;
		try {
			String business_unit_id = val.get("business_unit_id");
			String org_id = val.get("org_id");
			String business_unit_name = val.get("business_unit_name");
			gst_number = val.get("gst_number");
			String location = val.get("location");
			int state_id = Integer.parseInt(val.get("state_id"));
			String modified_by = val.get("modified_by");
			check = organizationRepository.checkBusinessIdPresentInContractMaster(business_unit_id);
			if (check == 1) {
				response.put("message", "Invoice Already Generated Not Able to Update");
				return response;
			}
			if (check > 1) {
				response.put("message", "Please Create New Master and Assign for that contract");
				return response;
			}
			organizationRepository.insertRecordBusinessUnitHistory(business_unit_id, modified_by);
			if (gst_number != null) {
				count = organizationRepository.updatebusinessUnits(org_id, business_unit_name, gst_number, location,
						state_id, modified_by, business_unit_id);
			} else {
				count = organizationRepository.updatebusinessUnitswithoutGstNumberColumn(org_id, business_unit_name,
						location, state_id, modified_by, business_unit_id);
			}

			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_BankMASTER");
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateBusinessUnit  -> " + e.getMessage());

			response.put("Message", e.getMessage());

		}
		return response;
	}

	@GetMapping("/listbusinessunits/{factory_id}")
	private @ResponseBody Map<String, Object> getAllBusinessUnits(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: getAllBusinessUnits");

		Map<String, Object> getAllBusinessUnitsMap = new HashMap<String, Object>();

		try {
			List<BusinessUnitInterface> getAllBusinessUnitslists = organizationRepository
					.getAllBussinessUnits(factory_id);
			getAllBusinessUnitsMap.put("action", "GetAllBusinessUnits");
			getAllBusinessUnitsMap.put("message",
					(getAllBusinessUnitslists.size() > 0) ? "Success" : "No Business units");
			getAllBusinessUnitsMap.put("status", (getAllBusinessUnitslists.size() > 0) ? "yes" : "no");
			getAllBusinessUnitsMap.put("Data", getAllBusinessUnitslists);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllBusinessUnits  -> " + e.getMessage());

		}
		return getAllBusinessUnitsMap;
	}

	@GetMapping("/searchbusinessunits/{business_unit_id}")
	public @ResponseBody Map<String, Object> findBusinessUnitById(@PathVariable String business_unit_id) {

		logger.info("EXECUTING METHOD :: findBusinessUnitById");

		Map<String, Object> response = new HashMap<>();
		BusinessUnitInterface businessUnitsInterface = null;
		try {
			businessUnitsInterface = organizationRepository.findBusinessUnit(business_unit_id);
			response.put("message", (businessUnitsInterface != null) ? "success" : "failure");
			response.put("status", (businessUnitsInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_BankMASTER");
			response.put("Data", businessUnitsInterface);
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: findBusinessUnitById  -> " + e.getMessage());

		}
		return response;
	}

	@PostMapping("/businessunits/delete")
	public @ResponseBody Map<String, Object> deleteBusinessUnit(@RequestBody Map<String, String> val) {
		logger.info("EXECUTING METHOD :: deleteBusinessUnit");
		Map<String, Object> response = new HashMap<>();
		try {
			String modified_by = val.get("modified_by");
			String business_unit_id = val.get("business_unit_id");
			organizationRepository.insertRecordBusinessDeleteUnitHistory(business_unit_id, modified_by);
			int count = organizationRepository.deletebusinessUnits(modified_by, business_unit_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_BankMASTER");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteBusinessUnit  -> " + e.getMessage());

		}
		return response;
	}

	/** BUSINESS UNIT END **/
}
