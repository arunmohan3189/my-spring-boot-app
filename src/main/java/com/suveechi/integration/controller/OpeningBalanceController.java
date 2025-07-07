package com.suveechi.integration.controller;

import java.time.LocalDateTime;
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

import com.suveechi.integration.entity.AdvancePackingNote;
import com.suveechi.integration.interfaces.ContractListFromContractInterfaces;
import com.suveechi.integration.interfaces.OpeningBalanceInterfaces;
import com.suveechi.integration.repository.AdvancePackingNoteRepository;
import com.suveechi.integration.repository.OpeningBalanceRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class OpeningBalanceController {

	@Autowired
	public OpeningBalanceRepository openingBalanceRepository;
	
	@Autowired
	public AdvancePackingNoteRepository advancePackingNoteRepository;

	@PostMapping("/openingbalance/add")
	public @ResponseBody Map<String, Object> createOpeningBalanceMaster(@RequestBody Map<String, String> data) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String con_id = data.get("con_id");
			String load_id = data.get("load_id");
			String created_by = data.get("created_by");
			String description = data.get("description");
			String total = data.get("total");
			String tax_total = data.get("tax_total");
			String net_total = data.get("net_total");	
			int factory_id = data.containsKey("factory_id") ? Integer.parseInt(data.get("factory_id")) : 0 ;
			AdvancePackingNote advancePackingNote = new AdvancePackingNote();
			advancePackingNote.setConId(con_id);
			advancePackingNote.setCreatedBy(created_by);
			advancePackingNote.setCreatedDate(LocalDateTime.now());
			advancePackingNote.setFactory_id(factory_id);
			AdvancePackingNote advancePackingNote1 = advancePackingNoteRepository.save(advancePackingNote);
			int pn_id = advancePackingNote1.getPnId();
			openingBalanceRepository.createOpeningBalanceItemRecord(pn_id,description,total,tax_total,net_total,created_by,factory_id);
			response.put("message", (advancePackingNote1 != null) ? "success" : "failure");
			response.put("status", (advancePackingNote1 != null) ? "yes" : "no");
			response.put("action", "Insert_Record_In_OPeningBalanceMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@PostMapping("/openingbalance/update")
	public @ResponseBody Map<String, Object> updateOpeningBalanceMaster(@RequestBody Map<String, String> data) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String pn_id =  data.get("pn_id");
			String con_id = data.get("con_id");
			String modified_by = data.get("modified_by");
			String slno =  data.get("slno");
			String description = data.get("description");
			String total = data.get("total");
			String tax_total = data.get("tax_total");
			String net_total = data.get("net_total");	
			openingBalanceRepository.insertOpeningBalanceHistory(pn_id,modified_by);
			openingBalanceRepository.insertOpeningBalaneItemHistory(slno,modified_by);
			int count = openingBalanceRepository.updateOpeningBalanceRecord(con_id, modified_by,pn_id);			
			openingBalanceRepository.updateOpeningBalanceItemRecord(pn_id,description,total,tax_total,net_total,modified_by,slno);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_OPeningBalanceMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
		
	@GetMapping("/listopeningbalance/{factory_id}")
	public @ResponseBody Map<String,Object> listOpeningBalance(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<OpeningBalanceInterfaces> openingBalanceInterfaces = null;
		try {
			openingBalanceInterfaces = openingBalanceRepository.getOpeningBalanceList(factory_id);
			response.put("message", (openingBalanceInterfaces != null)?"success":"failure");
			response.put("status", (openingBalanceInterfaces != null)?"yes":"no");
			response.put("Data", openingBalanceInterfaces);
			response.put("action", "List_Record_In_OPeningBalanceMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/searchopeningbalance/{slno}")
	public @ResponseBody Map<String,Object> searchorganziationId(@PathVariable String slno){
		Map<String, Object> response = new HashMap<String, Object>();
		OpeningBalanceInterfaces openingBalanceInterfaces = null;
		try {			
			openingBalanceInterfaces = openingBalanceRepository.searchOpeningBalanceById(slno);
			response.put("message", (openingBalanceInterfaces != null)?"success":"failure");
			response.put("status",  (openingBalanceInterfaces != null)?"yes":"no");
			response.put("action", "Search_Record_In_OPeningBalanceMASTER");
			response.put("DATA", openingBalanceInterfaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping("/listcontractorfromopeningbalance/{factory_id}")
	public @ResponseBody Map<String,Object> listOpeningBalanceInfo(@PathVariable String factory_id){
		Map<String, Object> response = new HashMap<String, Object>();
		List<ContractListFromContractInterfaces> openingBalanceInterfaces = null;
		try {
			openingBalanceInterfaces = openingBalanceRepository.getContractListFromContractInfo(factory_id);
			response.put("message", (openingBalanceInterfaces != null)?"success":"failure");
			response.put("status", (openingBalanceInterfaces != null)?"yes":"no");
			response.put("Data", openingBalanceInterfaces);
			response.put("action", "List_Record_In_OPeningBalanceCONTRACTORMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
