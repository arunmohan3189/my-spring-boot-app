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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suveechi.integration.interfaces.CountryInterface;
import com.suveechi.integration.repository.CountryRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class CountryController {

	@Autowired
	private CountryRepository countryrepo;

	Logger logger = LogManager.getLogger(CountryController.class);

	@PostMapping("/country/addCountry")
	public @ResponseBody Map<String, Object> addCountry(@RequestBody Map<String, String> country) {

		logger.info("EXECUTING METHOD :: addCountry");

		Map<String, Object> addCountrymap = new HashMap<String, Object>();

		try {
			String country_code = country.get("country_code");
			String country_name = country.get("country_name");
			String user_id = country.get("user_id");
			String factory_id = country.containsKey("factory_id") ? country.get("factory_id") : "0";

			int addCountryRecord = countryrepo.addCountry(country_code, country_name, user_id, factory_id);

			addCountrymap.put("message", (addCountryRecord > 0) ? "Success" : "Country Not Added");
			addCountrymap.put("status", (addCountryRecord > 0) ? "yes" : "no");
			addCountrymap.put("action", "AddCountry");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: addCountry  -> " + e.getMessage());
		}
		return addCountrymap;

	}

	@PostMapping("/country/updateCountry")
	public @ResponseBody Map<String, Object> updateCountry(@RequestBody Map<String, String> upcountry) {

		logger.info("EXECUTING METHOD :: updateCountry");

		Map<String, Object> updateCountrymap = new HashMap<String, Object>();

		try {

			String country_id = upcountry.get("country_id");
			String country_code = upcountry.get("country_code");
			String country_name = upcountry.get("country_name");
			String user_id = upcountry.get("user_id");

			int moveExistingToHistoryrecord = countryrepo.moveCountryToHistory(country_id, user_id);

			if (moveExistingToHistoryrecord == 0) {
				updateCountrymap.put("message", "Failed to insert existing company into history tale");
				updateCountrymap.put("status", "no");
				updateCountrymap.put("action", "UpdateCountry");

				return updateCountrymap;
			} else {

				int updateCountryrecord = countryrepo.updateCountry(country_code, country_name, user_id, country_id);

				updateCountrymap.put("message", (updateCountryrecord > 0) ? "Success" : "Country Not Updated");
				updateCountrymap.put("status", (updateCountryrecord > 0) ? "yes" : "no");
				updateCountrymap.put("action", "UpdateCountry");

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: updateCountry  -> " + e.getMessage());
		}
		return updateCountrymap;

	}

	@PostMapping("/country/deleteCountry")
	public @ResponseBody Map<String, Object> deleteCountry(@RequestBody Map<String, String> delcountry) {

		logger.info("EXECUTING METHOD :: deleteCountry");

		String country_id = delcountry.get("country_id");
		String user_id = delcountry.get("user_id");

		Map<String, Object> deleteCountrymap = new HashMap<String, Object>();

		try {

			int moveExistingToHistoryBeforeDeleting = countryrepo.moveToHistoryTable(country_id, user_id);
			if (moveExistingToHistoryBeforeDeleting == 0) {
				deleteCountrymap.put("message", "Failed to insert existing Country into history table");
				deleteCountrymap.put("status", "no");
				deleteCountrymap.put("action", "DeleteCountry");

				return deleteCountrymap;
			}

			int deleteCompanyrecord = countryrepo.deleteCountry(country_id, user_id);
			deleteCountrymap.put("message", (deleteCompanyrecord > 0) ? "Success" : "Country Not Deleted");
			deleteCountrymap.put("status", (deleteCompanyrecord > 0) ? "yes" : "no");
			deleteCountrymap.put("action", "DeleteCountry");

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: deleteCountry  -> " + e.getMessage());
		}
		return deleteCountrymap;
	}

	@GetMapping("/country/getAllCountryList/{factory_id}")
	public @ResponseBody Map<String, Object> getAllCountryList(@PathVariable String factory_id) {

		logger.info("EXECUTING METHOD :: getAllCountryList");

		Map<String, Object> allCountrymap = new HashMap<String, Object>();
		List<CountryInterface> allCountryList = null;

		try {
			allCountryList = countryrepo.getAllCountryDetails(factory_id);

			allCountrymap.put("message", ((allCountryList != null) && (!allCountryList.isEmpty())) ? "Success"
					: "Country Details not available");
			allCountrymap.put("status", ((allCountryList != null) && (!allCountryList.isEmpty())) ? "yes" : "no");
			allCountrymap.put("action", "CountryInfo");

			if ((allCountryList != null) && (!allCountryList.isEmpty())) {
				allCountrymap.put("CountryDetails", allCountryList);
			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getAllCountryList  -> " + e.getMessage());
		}
		return allCountrymap;
	}

	@GetMapping("/country/getCountryBasedOnId/{country_id}")
	public @ResponseBody Map<String, Object> getCountryBasedOnId(@PathVariable String country_id) {

		logger.info("EXECUTING METHOD :: getCountryBasedOnId");

		Map<String, Object> countryBasedOnIdmap = new HashMap<String, Object>();
		CountryInterface countryByid = null;
		try {

			countryByid = countryrepo.getCOuntryBasedOnId(country_id);

			countryBasedOnIdmap.put("message", (countryByid != null) ? "Success" : "Country details not available");
			countryBasedOnIdmap.put("status", (countryByid != null) ? "yes" : "no");
			countryBasedOnIdmap.put("action", "CountryBasedOnId");

			if (countryByid != null) {

				ObjectMapper mapper = new ObjectMapper();
				countryBasedOnIdmap.putAll(mapper.convertValue(countryByid, new TypeReference<Map<String, Object>>() {
				}));
				mapper.clearProblemHandlers();
				mapper = null;

			}

		} catch (Exception e) {
			logger.error("ERROR IN THE METHOD :: getCountryBasedOnId  -> " + e.getMessage());
		}
		return countryBasedOnIdmap;
	}

}
