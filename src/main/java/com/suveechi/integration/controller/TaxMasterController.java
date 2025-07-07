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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suveechi.integration.interfaces.TaxMasterInterface;
import com.suveechi.integration.repository.TaxMasterRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class TaxMasterController {

	@Autowired
	private TaxMasterRepository taxMastersRepository;
	
	@PostMapping("/taxmasters/add")
	public @ResponseBody Map<String,Object> createTaxMasters(@RequestBody Map<String,String> val ){
		Map<String, Object> addtaxMastersMap = new HashMap<String, Object>();
		int taxcount=0;
		try {
			String tax_name=val.get("tax_name");
			String tax_per=val.get("tax_per");
			String startdate=val.get("startdate");
			String enddate=val.get("enddate");
			String created_by=val.get("created_by");
			taxcount=taxMastersRepository.addTaxMasters(tax_name, tax_per, startdate, enddate, created_by);
			addtaxMastersMap.put("action", "AddTaxMasters");
			addtaxMastersMap.put("message", (taxcount > 0) ? "Success" : "TaxMasters details not added");
			addtaxMastersMap.put("status", (taxcount > 0) ? "yes" : "no");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return addtaxMastersMap;
	}
	
	
	@GetMapping("/taxmasters/list")
	private @ResponseBody Map<String,Object> getAllTaxMasters(){
		Map<String, Object> getAllTaxMastersMap = new HashMap<String, Object>();
		List<TaxMasterInterface> getAllTaxMasterslists = null;
		try {
			getAllTaxMasterslists =taxMastersRepository.getAllTaxMasters();
			getAllTaxMastersMap.put("action", "GetAllBusinessUnits");
			getAllTaxMastersMap.put("message", (getAllTaxMasterslists.size() > 0) ? "Success" : "No TaxMasters");
			getAllTaxMastersMap.put("status", (getAllTaxMasterslists.size() > 0) ? "yes" : "no");
			getAllTaxMastersMap.put("Data", getAllTaxMasterslists);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return getAllTaxMastersMap;
	}
	
	
	@GetMapping("/searchtaxmasters/{id}")
	private @ResponseBody Map<String,Object> getTaxMastersById(@PathVariable String id){
		Map<String, Object> getTaxMastersByIdMap = new HashMap<String, Object>();
		TaxMasterInterface getTaxMastersByIdlists = null;
		try {
			getTaxMastersByIdlists=taxMastersRepository.getTaxMastersById(id);
			getTaxMastersByIdMap.put("action", "GetTaxMastersById");
			getTaxMastersByIdMap.put("message", (getTaxMastersByIdlists!=null ) ? "Success" : "No TaxMasters");
			getTaxMastersByIdMap.put("status", (getTaxMastersByIdlists!=null) ? "yes" : "no");
			getTaxMastersByIdMap.put("Data", getTaxMastersByIdlists);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return getTaxMastersByIdMap;
	}
	
	
	@PostMapping("/taxmasters/delete")
	private @ResponseBody Map<String,Object> deleteBusinessUnitsById(@RequestBody Map<String,String> val ){
		Map<String, Object> gettaxmastMap = new HashMap<String, Object>();
		try {
			String tax_id = val.get("tax_id");
			String modified_by = val.get("modified_by");
			taxMastersRepository.updateTaxMasterHistory(modified_by, tax_id);
			int taxcount=taxMastersRepository.deleteTaxMastersById(tax_id,modified_by);
			gettaxmastMap.put("action", "DeleteTaxMastersById");
			gettaxmastMap.put("message", (taxcount > 0) ? "Success" : "TaxMasters  not delete!");
			gettaxmastMap.put("status", (taxcount > 0) ? "yes" : "no");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return gettaxmastMap;
	}
	
	
	@PostMapping("/taxmasters/update")
	public @ResponseBody Map<String,Object> UpdateTaxMasters(@RequestBody Map<String,String> val ){
		Map<String, Object> updatetaxMastersMap = new HashMap<String, Object>();
		try {
			String tax_name=val.get("tax_name");
			String tax_per=val.get("tax_per");
			String startdate=val.get("startdate");
			String enddate=val.get("enddate");
			String modified_by=val.get("modified_by");
			String tax_id =val.get("tax_id");
			taxMastersRepository.insertTaxMasterHistory(modified_by, tax_id);
			int taxcount=taxMastersRepository.UpdateTaxMasters(tax_name, tax_per, startdate, enddate, modified_by, tax_id);
			updatetaxMastersMap.put("action", "UpdateTaxMasters");
			updatetaxMastersMap.put("message", (taxcount > 0) ? "Success" : "TaxMasters details not updated");
			updatetaxMastersMap.put("status", (taxcount > 0) ? "yes" : "no");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return updatetaxMastersMap;
	}
}
