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

import com.suveechi.integration.interfaces.SalesOrderTandCInterfaces;
import com.suveechi.integration.interfaces.ScrapItemInterfaces;
import com.suveechi.integration.interfaces.ScrapSalesLocationInterfaces;
import com.suveechi.integration.interfaces.ScrapSalesOrderInterfaces;
import com.suveechi.integration.interfaces.ScrapTypeInterfaces;
import com.suveechi.integration.repository.ScrapMasterRepository;

@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class ScrapMasterController {

	@Autowired
	public ScrapMasterRepository scrapMasterRepository;

	/* SCRAPTYPE START */
	@PostMapping("/scraptypes/add")
	public @ResponseBody Map<String, Object> createScrapTypes(@RequestBody Map<String, String> val) {
		Map<String, Object> addScrapSaleScrapTypesMap = new HashMap<String, Object>();
		try {
			String scrap_typecode = val.get("scrap_typecode");
			String scrap_typename = val.get("scrap_typename");
			String created_by = val.get("created_by");
			String factory_id = val.get("factory_id");
			int scraptypescount = scrapMasterRepository.addScrapSaleScrapTypes(scrap_typecode, scrap_typename,
					created_by,factory_id);
			addScrapSaleScrapTypesMap.put("action", "AddScrapSaleScrapTypes");
			addScrapSaleScrapTypesMap.put("message",(scraptypescount > 0) ? "Success" : "Failure");
			addScrapSaleScrapTypesMap.put("status", (scraptypescount > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addScrapSaleScrapTypesMap;
	}

	@PostMapping("/scraptypes/update")
	public @ResponseBody Map<String, Object> UpdateScrapSaleScrapTypes(@RequestBody Map<String, String> val) {
		Map<String, Object> addScrapSaleScrapTypesMap = new HashMap<String, Object>();
		try {
			String stid = val.get("stid");
			String scrap_typecode = val.get("scrap_typecode");
			String scrap_typename = val.get("scrap_typename");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.insertScrapSalesTypeHistoryRecord(stid, modified_by);
			int scraptypescount = scrapMasterRepository.UpdateScrapSaleScrapTypes(stid, scrap_typecode, scrap_typename,
					modified_by);
			addScrapSaleScrapTypesMap.put("action", "UpdateScrapSaleScrapTypes");
			addScrapSaleScrapTypesMap.put("message",(scraptypescount > 0) ? "Success" : "Failure");
			addScrapSaleScrapTypesMap.put("status", (scraptypescount > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addScrapSaleScrapTypesMap;
	}

	@GetMapping("/scraptypes/list/{factory_id}")
	public @ResponseBody Map<String, Object> listSaleScrapTypes(@PathVariable String factory_id) {
		Map<String, Object> getAllScrapSaleScrapTypesMap = new HashMap<String, Object>();
		List<ScrapTypeInterfaces> getAllScrapSaleScraplists = null;
		try {
			getAllScrapSaleScraplists = scrapMasterRepository.getAllScrapSaleScrapTypes(factory_id);
			getAllScrapSaleScrapTypesMap.put("action", "GetAllScrapSaleScrapTypes");
			getAllScrapSaleScrapTypesMap.put("message",(getAllScrapSaleScraplists != null) ? "Success" : "Failure");
			getAllScrapSaleScrapTypesMap.put("status", (getAllScrapSaleScraplists != null) ? "yes" : "no");
			getAllScrapSaleScrapTypesMap.put("Lists", getAllScrapSaleScraplists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAllScrapSaleScrapTypesMap;
	}

	@GetMapping("/scraptypes/search/{id}")
	public @ResponseBody Map<String, Object> searchScrapTypesByid(@PathVariable String id) {
		Map<String, Object> getScrapSaleScrapTypesMap = new HashMap<String, Object>();
		ScrapTypeInterfaces getScrapSaleScrap = null;
		try {
			getScrapSaleScrap = scrapMasterRepository.getScrapSaleScrapTypesById(id);
			getScrapSaleScrapTypesMap.put("action", "GetScrapSaleScrapTypesById");
			getScrapSaleScrapTypesMap.put("message",(getScrapSaleScrap != null) ? "Success" : "Failure");
			getScrapSaleScrapTypesMap.put("status", (getScrapSaleScrap != null) ? "yes" : "no");
			getScrapSaleScrapTypesMap.put("ScrapSaleScrapType", getScrapSaleScrap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getScrapSaleScrapTypesMap;
	}

	@PostMapping("/scraptypes/delete")
	public @ResponseBody Map<String, Object> deleteScrapSaleScrapTypesByid(@RequestBody Map<String, String> val) {
		Map<String, Object> deleteScrapSaleScrapTypesMap = new HashMap<String, Object>();
		try {
			String stid = val.get("stid");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.UpdateScrapSaleScrapTypesHistory(stid, modified_by);
			int scraptypescount = scrapMasterRepository.deleteScrapSaleScrapTypesById(stid, modified_by);
			deleteScrapSaleScrapTypesMap.put("action", "DeleteScrapSaleScrapTypesById");
			deleteScrapSaleScrapTypesMap.put("message",	(scraptypescount > 0) ? "Success" : "Failure");
			deleteScrapSaleScrapTypesMap.put("status", (scraptypescount > 0) ? "yes" : "no");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleteScrapSaleScrapTypesMap;
	}

	/* SCRAPTYPE END */

	/* SCRAPITEM START */

	@PostMapping("/scrapsalescrapitems/add")
	public @ResponseBody Map<String, Object> createScrapSaleScrapItems(@RequestBody Map<String, String> val) {
		Map<String, Object> addscrapitemcount = new HashMap<String, Object>();
		int scrapitemcount = 0;
		try {
			String scraptypecode = val.get("scraptypecode");
			String scrapitemcode = val.get("scrapitemcode");
			String scrapitemname = val.get("scrapitemname");
			String created_by = val.get("created_by");
			String factory_id = val.get("factory_id");
			scrapitemcount = scrapMasterRepository.addScrapSaleScrapItems(scraptypecode, scrapitemcode, scrapitemname,
					created_by,factory_id);
			addscrapitemcount.put("action", "Addscrapitemcount");
			addscrapitemcount.put("message", (scrapitemcount > 0) ? "Success" : "Failure");
			addscrapitemcount.put("status", (scrapitemcount > 0) ? "yes" : "no");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return addscrapitemcount;
	}

	@PostMapping("/scrapsalescrapitems/update")
	public @ResponseBody Map<String, Object> UpdateScrapSaleScrapItems(@RequestBody Map<String, String> val) {
		Map<String, Object> updatescrapsalescrapitemsMap = new HashMap<String, Object>();
		try {
			String siid = val.get("siid");
			String scraptypecode = val.get("scraptypecode");
			String scrapitemcode = val.get("scrapitemcode");
			String scrapitemname = val.get("scrapitemname");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.insertScrapItemHistory(siid, modified_by);
			int scrapitemcount = scrapMasterRepository.UpdateScrapSaleScrapItems(siid, scraptypecode, scrapitemcode,
					scrapitemname, modified_by);
			updatescrapsalescrapitemsMap.put("action", "UpdateScrapSaleScrapItems");
			updatescrapsalescrapitemsMap.put("message",(scrapitemcount > 0) ? "Success" : "Failure");
			updatescrapsalescrapitemsMap.put("status", (scrapitemcount > 0) ? "yes" : "no");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return updatescrapsalescrapitemsMap;
	}

	@PostMapping("/scrapsalescrapitems/delete")
	private @ResponseBody Map<String, Object> deleteScrapItemsById(@RequestBody Map<String, String> val) {
		Map<String, Object> getScrapSaleScrapItemsMap = new HashMap<String, Object>();

		try {
			String siid = val.get("siid");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.updateScrapItemHistory(siid, modified_by);
			int taxcount = scrapMasterRepository.deleteScrapItemsById(siid, modified_by);
			getScrapSaleScrapItemsMap.put("action", "DeleteScrapItemsById");
			getScrapSaleScrapItemsMap.put("message", (taxcount > 0) ? "Success" : "Failure");
			getScrapSaleScrapItemsMap.put("status", (taxcount > 0) ? "yes" : "no");

		} catch (Exception e) {

		}
		return getScrapSaleScrapItemsMap;
	}

	@GetMapping("scrapsalescrapitems/list/{factory_id}")
	public @ResponseBody Map<String, Object> listScrapSaleScrapItems(@PathVariable String factory_id) {
		Map<String, Object> getAllScrapSaleScrapItemsMap = new HashMap<String, Object>();
		List<ScrapItemInterfaces> getAllScrapSaleScrapItemsLists = null;
		try {
			getAllScrapSaleScrapItemsLists = scrapMasterRepository.getAllScrapSaleScrapItems(factory_id);
			getAllScrapSaleScrapItemsMap.put("action", "GetAllScrapSaleScrapItems");
			getAllScrapSaleScrapItemsMap.put("message",(getAllScrapSaleScrapItemsLists != null) ? "Success" : "Failure");
			getAllScrapSaleScrapItemsMap.put("status", (getAllScrapSaleScrapItemsLists != null) ? "yes" : "no");
			getAllScrapSaleScrapItemsMap.put("ListScrapSaleScrapItems", getAllScrapSaleScrapItemsLists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAllScrapSaleScrapItemsMap;
	}

	@GetMapping("/scrapsalesrapitems/search/{id}")
	private @ResponseBody Map<String, Object> getScrapItemsById(@PathVariable String id) {
		Map<String, Object> getScrapItemsByIdMap = new HashMap<String, Object>();
		try {
			ScrapItemInterfaces getScrapItemsByIdlists = scrapMasterRepository.getScrapItemsById(id);
			getScrapItemsByIdMap.put("action", "GetScrapItemsById");
			getScrapItemsByIdMap.put("message", (getScrapItemsByIdlists != null) ? "Success" : "Failure");
			getScrapItemsByIdMap.put("status", (getScrapItemsByIdlists != null) ? "yes" : "no");
			getScrapItemsByIdMap.put("scrapsalescrapitems", getScrapItemsByIdlists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getScrapItemsByIdMap;
	}
	/* SCRAPITEM END */

	/* SALSE ORDER ITEM START */

	@PostMapping("/salesordertype/add")
	private @ResponseBody Map<String, Object> createbusinessUnit(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String saleorder_typecode = val.get("saleorder_typecode");
			String saleorder_typename = val.get("saleorder_typename");
			String created_by = val.get("created_by");
			String factory_id = val.get("factory_id");
			int unitCount = scrapMasterRepository.addSalesOrder(saleorder_typecode, saleorder_typename, created_by,factory_id);
			response.put("message", (unitCount > 0) ? "Success" : "Failure");
			response.put("status", (unitCount > 0) ? "yes" : "no");
			response.put("action", "Add salesOrderDetails");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/salesordertype/update")
	public @ResponseBody Map<String, Object> updateSalesOrder(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<>();
		try {
			String saleorder_id = val.get("saleorder_id");
			String saleorder_typecode = val.get("saleorder_typecode");
			String saleorder_typename = val.get("saleorder_typename");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.insertSalesOrderHistory(saleorder_id, modified_by);
			int count = scrapMasterRepository.updateSalesorder(saleorder_typecode, saleorder_typename, modified_by,
					saleorder_id);
			response.put("message", (count > 0) ? "Success" : "Failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "UPDATE_Record_In_SalesMAster");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/salesordertype/delete")
	public @ResponseBody Map<String, Object> deleteSalesorder(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<>();
		try {
			String saleorder_id = val.get("saleorder_id");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.updateSalesOrderHistory(saleorder_id, modified_by);
			int count = scrapMasterRepository.insertDeletedRecord(saleorder_id, modified_by);
			response.put("message", (count > 0) ? "Success" : "Failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_SalesMASTER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/salesordertype/search/{saleorder_id}")
	public @ResponseBody Map<String, Object> findSalesOrderById(@PathVariable String saleorder_id) {
		Map<String, Object> response = new HashMap<>();
		ScrapSalesOrderInterfaces salesOrderInterface = null;
		try {
			salesOrderInterface = scrapMasterRepository.findSalesOrder(saleorder_id);
			response.put("message", (salesOrderInterface != null) ? "Success" : "Failure");
			response.put("status", (salesOrderInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesMASTER");
			response.put("SalesOrderDetails", salesOrderInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/salesordertype/list/{factory_id}")
	public @ResponseBody Map<String, Object> findAllSalesOrder(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<>();
		List<ScrapSalesOrderInterfaces> listSalesOrder = null;
		try {
			listSalesOrder = scrapMasterRepository.getAllSalesOrders(factory_id);
			response.put("message", (listSalesOrder != null) ? "Success" : "Failure");
			response.put("status", (listSalesOrder != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesMASTER");
			response.put("SalesOrderDetails", listSalesOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/* SALES ORDER ITEM END */
	/* SALES LOCATION START */

	@PostMapping("/scrapsalelocation/add")
	public @ResponseBody Map<String, Object> createLocationTrack(@RequestBody Map<String, String> val) {
		Map<String, Object> addlocationcount = new HashMap<String, Object>();
		int locationcount = 0;
		try {
			String location_code = val.get("location_code");
			String location_name = val.get("location_name");
			String created_by = val.get("created_by");
			String factory_id = val.get("factory_id");
			locationcount = scrapMasterRepository.addLocationTrack(location_code, location_name, created_by,factory_id);
			addlocationcount.put("action", "Addlocationcount");
			addlocationcount.put("message", (locationcount > 0) ? "Success" : "Failure");
			addlocationcount.put("status", (locationcount > 0) ? "yes" : "no");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return addlocationcount;
	}

	@GetMapping("scrapsalelocation/list/{factory_id}")
	public @ResponseBody Map<String, Object> getAllLocationTrack(@PathVariable String factory_id) {
		Map<String, Object> getAllLocationTrackMap = new HashMap<String, Object>();
		List<ScrapSalesLocationInterfaces> getAllLocationTrackLists = null;
		try {
			getAllLocationTrackLists = scrapMasterRepository.getAllLocationTrack(factory_id);
			getAllLocationTrackMap.put("action", "GetAll");
			getAllLocationTrackMap.put("message", (getAllLocationTrackLists.size() > 0) ? "Success" : "Failure");
			getAllLocationTrackMap.put("status", (getAllLocationTrackLists.size() > 0) ? "yes" : "no");
			getAllLocationTrackMap.put("ListLocationTrack", getAllLocationTrackLists);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return getAllLocationTrackMap;
	}

	@GetMapping("/scrapsalelocation/search/{slid}")
	private @ResponseBody Map<String, Object> getLocationTrackById(@PathVariable String slid) {
		Map<String, Object> getLocationTrackByIdMap = new HashMap<String, Object>();
		ScrapSalesLocationInterfaces getLocationTrackByIdLists = null;
		try {
			getLocationTrackByIdLists = scrapMasterRepository.getLocationTrackById(slid);
			getLocationTrackByIdMap.put("action", "GetLocationTrackById");
			getLocationTrackByIdMap.put("message",
					(getLocationTrackByIdLists != null) ? "Success" : "Failure");
			getLocationTrackByIdMap.put("status", (getLocationTrackByIdLists != null) ? "yes" : "no");
			getLocationTrackByIdMap.put("locationtrack", getLocationTrackByIdLists);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return getLocationTrackByIdMap;

	}

	@PostMapping("/scrapsalelocation/delete")
	private @ResponseBody Map<String, Object> deleteLocationTrackById(@RequestBody Map<String, String> map) {
		Map<String, Object> getLocationTrackMap = new HashMap<String, Object>();

		try {
			String slid = map.get("slid");
			String modified_by = map.get("modified_by");
			scrapMasterRepository.updateLocationHistory(slid, modified_by);
			int locationcount = scrapMasterRepository.deleteLocationTrackById(slid, modified_by);
			getLocationTrackMap.put("action", "DeleteLocationTrackById");
			getLocationTrackMap.put("message", (locationcount > 0) ? "Success" : "Failure");
			getLocationTrackMap.put("status", (locationcount > 0) ? "yes" : "no");

		} catch (Exception e) {

		}
		return getLocationTrackMap;
	}

	@PostMapping("/scrapsalelocation/update")
	public @ResponseBody Map<String, Object> UpdateLocationTrack(@RequestBody Map<String, String> val) {
		Map<String, Object> updatelocationtrackMap = new HashMap<String, Object>();
		try {
			String slid = val.get("slid");
			String location_code = val.get("location_code");
			String location_name = val.get("location_name");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.insertLocationHistory(slid, modified_by);
			int locationcount = scrapMasterRepository.UpdateLocationTrack(slid, location_code, location_name,
					modified_by);
			updatelocationtrackMap.put("action", "UpdateLocationTrack");
			updatelocationtrackMap.put("message",(locationcount > 0) ? "Success" : "Failure");
			updatelocationtrackMap.put("status", (locationcount > 0) ? "yes" : "no");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return updatelocationtrackMap;
	}
	/* SALES LOCATION END */

	/* SALES T&C START */

	@PostMapping("/salesordertandc/add")
	private @ResponseBody Map<String, Object> createSalesTnC(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			String sale_ordercode = val.get("sale_ordercode");
			String slno = val.get("slno");
			String description = val.get("description");
			String value = val.get("value");
			String status = val.get("status");
			String created_by = val.get("created_by");
			String factory_id = val.get("factory_id");
			int count = scrapMasterRepository.addsalesTncDetailsDetails(sale_ordercode, slno, description, value,
					status, created_by,factory_id);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Insert_Record_In_salesordertandc");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return response;

	}

	@PostMapping("/salesordertandc/update")
	private @ResponseBody Map<String, Object> updateSalesTnC(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String tcid = val.get("tcid");
			String sale_ordercode = val.get("sale_ordercode");
			String slno = val.get("slno");
			String description = val.get("description");
			String value = val.get("value");
			String status = val.get("status");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.insertsalesTandCHistory(tcid, modified_by);
			int count = scrapMasterRepository.updateSalesTncDetails(tcid, sale_ordercode, slno, description, value,
					status, modified_by);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "Update_Record_In_salesordertandc");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return response;

	}

	@GetMapping("/salesordertandc/search/{tcid}")
	public @ResponseBody Map<String, Object> findSalesOrderTnCById(@PathVariable String tcid) {
		Map<String, Object> response = new HashMap<>();
		SalesOrderTandCInterfaces salesOrderTandCInterface = null;
		try {
			salesOrderTandCInterface = scrapMasterRepository.findSalesOrderTnCById(tcid);
			response.put("message", (salesOrderTandCInterface != null) ? "success" : "failure");
			response.put("status", (salesOrderTandCInterface != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesTnCMASTER");
			response.put("SalesTnCDetails", salesOrderTandCInterface);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("action", "findSalesOrderTnCById");
			response.put("message", "An error occurred while retrieving the business unit.");
			response.put("status", "error");
		}

		return response;
	}

	@GetMapping("/salesordertandc/list/{factory_id}")
	public @ResponseBody Map<String, Object> findAllSalesTandCOrder(@PathVariable String factory_id) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<SalesOrderTandCInterfaces> listSalesOrder = scrapMasterRepository.getAllSalesOrderTnc(factory_id);
			response.put("message", (listSalesOrder != null) ? "success" : "failure");
			response.put("status", (listSalesOrder != null) ? "yes" : "no");
			response.put("action", "Search_Record_In_SalesMASTER");
			response.put("SalesTnCDetailsList", listSalesOrder);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("action", "findAllSalesOrders");
			response.put("message", "An error occurred while retrieving the business unit.");
			response.put("status", "error");
		}
		return response;
	}

	@PostMapping("/salesordertandc/delete")
	public @ResponseBody Map<String, Object> deleteSalesTandCorder(@RequestBody Map<String, String> val) {
		Map<String, Object> response = new HashMap<>();
		try {
			String tcid = val.get("tcid");
			String modified_by = val.get("modified_by");
			scrapMasterRepository.updatesalesTandCHistory(tcid, modified_by);
			int count  = scrapMasterRepository.deleteSalesTncDetails(tcid, modified_by);
			response.put("message", (count > 0) ? "success" : "failure");
			response.put("status", (count > 0) ? "yes" : "no");
			response.put("action", "DELETE_Record_In_SalesOrderTnCMaster");

		} catch (Exception e) {
			e.printStackTrace();

		}

		return response;
	}

	/* SALES T&C END */
}
