package com.suveechi.integration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.suveechi.integration.interfaces.StateInterface;
import com.suveechi.integration.repository.StateRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class StateController {

	@Autowired
	private StateRepository staterepo;

	Logger logger = LogManager.getLogger(StateController.class);

	@PostMapping("/state/createState")
	private @ResponseBody Map<String, Object> createState(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: createState");

		Map<String, Object> addStateMap = new HashMap<String, Object>();
		int statecount = 0;
		try {
			String state_code = val.get("state_code");
			String state_name = val.get("state_name");
			String state_id = val.get("state_id");
			String created_by = val.get("created_by");
			String factory_id = val.containsKey("factory_id") ? val.get("factory_id") : "0";

			logger.info("EXECUTING METHOD :: BEFORE adding State");

			statecount = staterepo.addStateDetails(state_code, state_name, state_id, created_by, factory_id);

			logger.info("EXECUTING METHOD :: AFTER adding State");

			addStateMap.put("action", "CreateState");
			addStateMap.put("message", (statecount > 0) ? "Success" : "State details not added");
			addStateMap.put("status", (statecount > 0) ? "yes" : "no");
		} catch (DataIntegrityViolationException e) { // Handles duplicate entry exception
			addStateMap.put("action", "CreateState");
			addStateMap.put("message", "Duplicate entry detected: " + e.getMessage());
			addStateMap.put("status", "Duplicate_entry");
		} catch (Exception e) { // Handles general exceptions

			logger.error("ERROR IN THE METHOD :: createState  -> " + e.getMessage());

			addStateMap.put("action", "CreateState");
			addStateMap.put("message", "An error occurred: " + e.getMessage());
			addStateMap.put("status", "error");
		}
		return addStateMap;

	}

	@PostMapping("/state/updateState")
	private @ResponseBody Map<String, Object> updateState(@RequestBody Map<String, String> val) {

		logger.info("EXECUTING METHOD :: updateState");

		Map<String, Object> getStateMap = new HashMap<String, Object>();
		int statecount = 0;
		try {
			String state_code = val.get("state_code");
			String state_name = val.get("state_name");
			String modified_by = val.get("modified_by");
			String state_id = val.get("state_id");
			String id = val.get("id");

			logger.info("EXECUTING METHOD :: updateState :: BEFORE moving to History table");

			staterepo.insertToHistoryTable(state_id, modified_by);

			logger.info("EXECUTING METHOD :: updateState :: BEFORE updating");

			statecount = staterepo.UpdateState(state_code, state_name, modified_by, state_id, id);
			getStateMap.put("action", "UpdateState");
			getStateMap.put("message", (statecount > 0) ? "Success" : "State details not updated!");
			getStateMap.put("status", (statecount > 0) ? "yes" : "no");
		} catch (DataIntegrityViolationException e) { // Handles duplicate entry exception
			getStateMap.put("action", "UpdateState");
			getStateMap.put("message", "Duplicate entry detected: " + e.getMessage());
			getStateMap.put("status", "Duplicate_entry");
		} catch (Exception e) { // Handles general exceptions

			logger.error("ERROR IN THE METHOD :: updateState  -> " + e.getMessage());

			getStateMap.put("action", "UpdateState");
			getStateMap.put("message", "An error occurred: " + e.getMessage());
			getStateMap.put("status", "error");
		}
		return getStateMap;
	}

	@PostMapping("/state/deleteState")
	private @ResponseBody Map<String, Object> deleteStatesById(@RequestParam String user_id,
			@RequestParam String state_id) {

		logger.info("EXECUTING METHOD :: deleteStatesById");

		Map<String, Object> deleteStatemap = new HashMap<String, Object>();
		int statecount = 0;
		try {

			int parsedUserId = Integer.parseInt(user_id);
			int parsedStateId = Integer.parseInt(state_id);

			int moveToHistoryTable = staterepo.moveToHistoryBeforeDelete(parsedUserId, parsedStateId);

			statecount = staterepo.deleteState(state_id);

			deleteStatemap.put("action", "DeleteState");
			deleteStatemap.put("message", (statecount > 0) ? "Success" : "State details not deleted");
			deleteStatemap.put("status", (statecount > 0) ? "yes" : "no");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteStatesById  -> " + e.getMessage());

		}
		return deleteStatemap;
	}

	@GetMapping("/state/getAllStates/{factory_id}")
	private @ResponseBody Map<String, Object> getAllStatesDetails(@PathVariable String factory_id) {
		Map<String, Object> addAllStateMap = new HashMap<String, Object>();

		logger.info("EXECUTING METHOD :: getAllStatesDetails");

		try {
			int stateCount = staterepo.getStateByCount(factory_id);
			
			List<StateInterface> allStatesList = staterepo.getStates(factory_id);
			addAllStateMap.put("action", "AllStatesInfo");
			addAllStateMap.put("message", (allStatesList.size() > 0) ? "Success" : "State details not found!");
			addAllStateMap.put("status", (allStatesList.size() > 0) ? "yes" : "no");
			

			if ((allStatesList != null) && (!allStatesList.isEmpty())) {
				addAllStateMap.put("StateCount", stateCount);
				addAllStateMap.put("StateDetails", allStatesList);
				
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllStatesDetails  -> " + e.getMessage());

		}
		return addAllStateMap;
	}

	@GetMapping("/state/GetStatesById/{state_id}")
	private @ResponseBody Map<String, Object> getStatesById(@PathVariable String state_id) {

		logger.info("EXECUTING METHOD :: getAllStatesDetails");

		Map<String, Object> getStateMapByid = new HashMap<String, Object>();
		try {
			StateInterface statelist = staterepo.getStateById(state_id);

			getStateMapByid.put("action", "StateDetailsById");
			getStateMapByid.put("message", (statelist != null) ? "Success" : "State details not found!");
			getStateMapByid.put("status", (statelist != null) ? "yes" : "no");
			if (statelist != null) {
				ObjectMapper mapper = new ObjectMapper();
				getStateMapByid.putAll(mapper.convertValue(statelist, new TypeReference<Map<String, Object>>() {
				}));
				mapper.clearProblemHandlers();
				mapper = null;
			}
		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getStatesById  -> " + e.getMessage());
		}
		return getStateMapByid;
	}

	@GetMapping("/state/getStatesBsdOnCountry/{country_id}")
	public @ResponseBody Map<String, Object> getStatesBasedOnCountry(@PathVariable String country_id) {

		Map<String, Object> getStatesBasedOncountrymap = new HashMap<String, Object>();
		List<StateInterface> statesBsdOnCountrylist = null;
		try {

			statesBsdOnCountrylist = staterepo.getStateByCountry(country_id);

			getStatesBasedOncountrymap.put("action", "StateDetailsBasedOnCountry");
			getStatesBasedOncountrymap.put("message",
					(statesBsdOnCountrylist.size() > 0) ? "Success" : "State details not found!");
			getStatesBasedOncountrymap.put("status", (statesBsdOnCountrylist.size() > 0) ? "yes" : "no");

			if ((statesBsdOnCountrylist != null) && (!statesBsdOnCountrylist.isEmpty())) {
				getStatesBasedOncountrymap.put("StateDetailsBasedOnCountry", statesBsdOnCountrylist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getStatesBasedOncountrymap;

	}

}
