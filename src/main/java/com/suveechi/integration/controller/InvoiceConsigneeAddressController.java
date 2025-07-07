
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suveechi.integration.interfaces.InvoiceConsigneeAddressInterface;
import com.suveechi.integration.repository.InvoiceConsigneeAdressRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class InvoiceConsigneeAddressController {

	@Autowired
	private InvoiceConsigneeAdressRepository invoiceconsigneerepo;

	Logger logger = LogManager.getLogger(InvoiceConsigneeAddressController.class);

	@PostMapping("/invoiceConsigneeAddress/addInvoiceConsigneeAddress")
	public @ResponseBody Map<String, Object> addInvoiceConsigneeAddress(
			@RequestBody Map<String, String> addInvoConsigneeMap) {

		logger.info("EXECUTING METHOD :: addInvoiceConsigneeAddress");

		int count = 0;
		String address = addInvoConsigneeMap.get("address");
		String city = addInvoConsigneeMap.get("city");
		String district = addInvoConsigneeMap.get("district");
		String state_id = addInvoConsigneeMap.get("state_id");
		String country_id = addInvoConsigneeMap.get("country_id");
		String created_by = addInvoConsigneeMap.get("created_by");
		String is_invoice = addInvoConsigneeMap.get("is_invoice");
		String is_consignee = addInvoConsigneeMap.get("is_consignee");
		String gst_no = addInvoConsigneeMap.get("gst_no");
		String pan_no = addInvoConsigneeMap.get("pan_no");
		String pin_no = addInvoConsigneeMap.get("pin_no");
		String factory_id = addInvoConsigneeMap.containsKey("factory_id") ? addInvoConsigneeMap.get("factory_id") : "0";

		Map<String, Object> addInvoiceConsigneeAddressmap = new HashMap<String, Object>();
		try {
			count = invoiceconsigneerepo.countNumberOfRows();
			count = (count == 0) ? (count + 1) : (count + 1);

			int addInvoiceConsigneeAddress = invoiceconsigneerepo.addInvoiceAddress(address, city, district, state_id,
					country_id, pin_no, created_by, is_invoice, is_consignee, count, gst_no, pan_no, factory_id);

			addInvoiceConsigneeAddressmap.put("message",
					(addInvoiceConsigneeAddress > 0) ? "Success" : "Invoice/Consignee Address Not created");
			addInvoiceConsigneeAddressmap.put("status", (addInvoiceConsigneeAddress > 0) ? "yes" : "no");
			addInvoiceConsigneeAddressmap.put("action", "AddInvoice/ConsigneeAddress");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: addInvoiceConsigneeAddress  -> " + e.getMessage());

		}

		return addInvoiceConsigneeAddressmap;
	}

	@PostMapping("/invoiceConsigneeAddress/updateInvoiceConsigneeAddress")
	public @ResponseBody Map<String, Object> updateInvoiceConsigneeAddress(
			@RequestBody Map<String, String> updateInvoConsigneeAddressMap) {

		logger.info("EXECUTING METHOD :: updateInvoiceConsigneeAddress");

		int count = 0;
		int check = 0;
		String address = updateInvoConsigneeAddressMap.get("address");
		String city = updateInvoConsigneeAddressMap.get("city");
		String district = updateInvoConsigneeAddressMap.get("district");
		String state_id = updateInvoConsigneeAddressMap.get("state_id");
		String country_id = updateInvoConsigneeAddressMap.get("country_id");
		String modified_by = updateInvoConsigneeAddressMap.get("modified_by");
		String is_invoice = updateInvoConsigneeAddressMap.get("is_invoice");
		String is_consignee = updateInvoConsigneeAddressMap.get("is_consignee");
		String address_id = updateInvoConsigneeAddressMap.get("address_id");
		String gst_no = updateInvoConsigneeAddressMap.get("gst_no");
		String pan_no = updateInvoConsigneeAddressMap.get("pan_no");
		String pin_no = updateInvoConsigneeAddressMap.get("pin_no");
		Map<String, Object> updateInvoConAddressmap = new HashMap<String, Object>();

		try {
			if (is_consignee.equals("1")) {
				check = invoiceconsigneerepo.checkConsigneeAddressIdPresentInContractMaster(address_id);
				if (check > 0) {
					updateInvoConAddressmap.put("message", "Invoice Already Generated Not Able to Update");
					return updateInvoConAddressmap;
				}
			}
			if (is_invoice.equals("1")) {
				check = invoiceconsigneerepo.checkInvoiceAddressIdPresentInContractMaster(address_id);
				if (check > 0) {
					updateInvoConAddressmap.put("message", "Invoice Already Generated Not Able to Update");
					return updateInvoConAddressmap;
				}
			}

			int moveExistingtoHistoryTable = invoiceconsigneerepo.moveToHistoryTable(modified_by, address_id);
			count = invoiceconsigneerepo.updateAddressRecord(address, city, district, state_id, country_id, modified_by,
					is_invoice, is_consignee, address_id, gst_no, pan_no, pin_no);

			updateInvoConAddressmap.put("message", (count > 0) ? "Success" : "Invoice/Consignee Address Not updated");
			updateInvoConAddressmap.put("status", (count > 0) ? "yes" : "no");
			updateInvoConAddressmap.put("action", "UpdateInvoice/ConsigneeAddress");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateInvoiceConsigneeAddress  -> " + e.getMessage());

		}
		return updateInvoConAddressmap;
	}

	@PostMapping("/invoiceConsigneeAddress/deleteInvoiceConsigneeAddress")
	public @ResponseBody Map<String, Object> deleteInvoiceOrConsigneeAddress(@RequestParam String address_id,
			@RequestParam String user_id) {

		logger.info("EXECUTING METHOD :: deleteInvoiceOrConsigneeAddress");

		Map<String, Object> deleteInvoiceConsigneeAddressmap = new HashMap<String, Object>();

		try {

			int moveExistingBeforeDeletingrecord = invoiceconsigneerepo.moveToHistoryTableBeforeDelete(address_id,
					user_id);

			int deleteInvoiceConsigneeAddrecord = invoiceconsigneerepo.deleteInvConsigneeAddress(address_id, user_id);

			deleteInvoiceConsigneeAddressmap.put("message",
					(deleteInvoiceConsigneeAddrecord > 0) ? "Success" : "Invoice/Consignee Address Not deleted");
			deleteInvoiceConsigneeAddressmap.put("status", (deleteInvoiceConsigneeAddrecord > 0) ? "yes" : "no");
			deleteInvoiceConsigneeAddressmap.put("action", "DeleteInvoice/ConsigneeAddress");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteInvoiceOrConsigneeAddress  -> " + e.getMessage());

		}
		return deleteInvoiceConsigneeAddressmap;
	}

	@GetMapping("invoiceConsigneeAddress/getInvoiceAddress/{factory_id}")
	public @ResponseBody Map<String, Object> getInvoiceConsigneeAddress(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: getInvoiceConsigneeAddress");

		Map<String, Object> allAddressmap = new HashMap<String, Object>();
		List<InvoiceConsigneeAddressInterface> invoiceAddlist = null;
		try {
			invoiceAddlist = invoiceconsigneerepo.getInvoiceAddressDetails(factory_id);

			allAddressmap.put("message", ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) ? "Success"
					: "Invoice Address not available");
			allAddressmap.put("status", ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) ? "yes" : "no");
			allAddressmap.put("action", "InvoiceAddress");

			if ((invoiceAddlist != null) && (!invoiceAddlist.isEmpty())) {
				allAddressmap.put("InvoiceAddress", invoiceAddlist);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getInvoiceConsigneeAddress  -> " + e.getMessage());

		}
		return allAddressmap;
	}

	@GetMapping("invoiceConsigneeAddress/getConsigneeAddress/{factory_id}")
	public @ResponseBody Map<String, Object> getConsigneeAddress(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: getConsigneeAddress");

		Map<String, Object> allAddressmap = new HashMap<String, Object>();
		List<InvoiceConsigneeAddressInterface> consigneeAddlist = null;
		try {
			consigneeAddlist = invoiceconsigneerepo.getConsigneeAddressDetails(factory_id);

			allAddressmap.put("message", ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) ? "Success"
					: "Consignee Address Not Available");
			allAddressmap.put("status", ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) ? "yes" : "no");
			allAddressmap.put("action", "ConsigneeAddress");

			if ((consigneeAddlist != null) && (!consigneeAddlist.isEmpty())) {
				allAddressmap.put("ConsigneeAddress", consigneeAddlist);
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getConsigneeAddress  -> " + e.getMessage());
		}
		return allAddressmap;
	}

	@GetMapping("invoiceConsigneeAddress/getAddressById/{address_id}")
	public @ResponseBody Map<String, Object> getConsigneeAddressById(@PathVariable String address_id) {

		logger.info("EXECUTING METHOD :: getConsigneeAddressById");

		Map<String, Object> consigneeAddressByIdmap = new HashMap<String, Object>();
		InvoiceConsigneeAddressInterface consigneeAddressById = null;

		try {

			logger.info(
					"EXECUTING METHOD :: getConsigneeAddressById :: BEFORE executing getConsigneeAddressDetailsById");

			consigneeAddressById = invoiceconsigneerepo.getConsigneeAddressDetailsById(address_id);

			logger.info(
					"EXECUTING METHOD :: getConsigneeAddressById :: AFTER executing getConsigneeAddressDetailsById");

			consigneeAddressByIdmap.put("message",
					(consigneeAddressById != null) ? "Success" : "Invoice/Consignee Address By Id not available");
			consigneeAddressByIdmap.put("status", (consigneeAddressById != null) ? "yes" : "no");
			consigneeAddressByIdmap.put("action", "Invoice/ConsigneeAddressNotAvailable");

			if (consigneeAddressById != null) {
				ObjectMapper mapper = new ObjectMapper();
				consigneeAddressByIdmap
						.putAll(mapper.convertValue(consigneeAddressById, new TypeReference<Map<String, Object>>() {
						}));
				mapper.clearProblemHandlers();
				mapper = null;
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getConsigneeAddressById  -> " + e.getMessage());
		}
		return consigneeAddressByIdmap;

	}

}
