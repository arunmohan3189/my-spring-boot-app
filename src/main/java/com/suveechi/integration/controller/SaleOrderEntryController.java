package com.suveechi.integration.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.suveechi.integration.entity.SaleOrderEntry;
import com.suveechi.integration.interfaces.SaleOrderInterface;
import com.suveechi.integration.interfaces.SaleOrderItemDescLevelInterface;
import com.suveechi.integration.interfaces.SaleOrderItemLevelInterface;
import com.suveechi.integration.interfaces.allSaleOrderEntriesInterface;
import com.suveechi.integration.repository.SaleOrderEntryRepository;


@CrossOrigin
@RestController
@RequestMapping("/jssl")
public class SaleOrderEntryController {

	@Autowired
	private SaleOrderEntryRepository saleOrderEntryRepository;

	@PostMapping("/saleOrderEntry/addSaleOrderEntry")
	public @ResponseBody Map<String, Object> createSaleOrder(@RequestBody Map<String, Object> saleOrderEntry) {

		Map<String, Object> createSaleOrdermap = new HashMap<String, Object>();

		Map<String, String> val = null;
		List<Map<String, String>> obj = null;
		List<Map<String, String>> obj1 = null;

		String created_by = null;
		int descriptionLevelSaleOrderEntry = 0;

		try {

			val = (Map<String, String>) saleOrderEntry.get("val");
			obj = (List<Map<String, String>>) saleOrderEntry.get("obj");
			obj1 = (List<Map<String, String>>) saleOrderEntry.get("obj1");

			if ((val != null && !val.isEmpty()) && (obj != null && !obj.isEmpty())
					&& (obj1 != null && !obj1.isEmpty())) {

				SaleOrderEntry saleEntry = new SaleOrderEntry();
				saleEntry.setSale_order_type_id(Integer.parseInt(val.get("sale_order_type_id")));
				saleEntry.setSale_order_code(val.get("sale_order_code"));
				saleEntry.setLocation_type_id(Integer.parseInt(val.get("location_type_id")));
				saleEntry.setSale_order_to_id(Integer.parseInt(val.get("sale_order_to_id")));
				saleEntry.setAdvance_payment(Integer.parseInt(val.get("advance_payment")));
				saleEntry.setBilling_address_id(Integer.parseInt(val.get("billing_address_id")));
				saleEntry.setShipping_address_id(Integer.parseInt(val.get("shipping_address_id")));
				saleEntry.setBusiness_unit_id(Integer.parseInt(val.get("business_unit_id")));
				saleEntry.setTax1(Float.parseFloat(val.get("tax1")));
				saleEntry.setTax1_value(Float.parseFloat(val.get("tax1_value")));
				saleEntry.setTax2(Float.parseFloat(val.get("tax2")));
				saleEntry.setTax2_value(Float.parseFloat(val.get("tax2_value")));
				saleEntry.setTax3(Float.parseFloat(val.get("tax3")));
				saleEntry.setTax3_value(Float.parseFloat(val.get("tax3_value")));
				saleEntry.setNet_amount(Float.parseFloat(val.get("net_amount")));
				saleEntry.setTotal_tax(Float.parseFloat(val.get("total_tax")));
				saleEntry.setGrand_total(Float.parseFloat(val.get("grand_total")));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				saleEntry.setAuction_date(LocalDateTime.parse(val.get("auction_date"), formatter));
				saleEntry.setSale_order_duration(LocalDateTime.parse(val.get("sale_order_duration"), formatter));

				saleEntry.setCreated_by(val.get("created_by"));
				saleEntry.setFactory_id(val.get("factory_id"));
				saleEntry.setCreated_date(LocalDateTime.now());

				created_by = val.get("created_by");
				// Save entity to the database
				saleOrderEntryRepository.save(saleEntry);

				int soeid = saleEntry.getSoe_id();
				for (Map<String, String> item : obj) {

					String auction_no = item.get("auction_no");
					String scrap_type_id = item.get("scrap_type_id");
					String scrapitem_id = item.get("scrapitem_id");
					String uom_id = item.get("uom_id");
					String quantity = item.get("quantity");
					String kg = item.get("kg");
					String unit_price = item.get("unit_price");
					String total = item.get("total");
					String tolerance = item.get("tolerance");

					int itemLevelInsert = saleOrderEntryRepository.insertIntoSaleOrderItemLevelEntry(soeid, auction_no,
							scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, tolerance,
							created_by);
				}

				int sl_no = 1;
				for (Map<String, String> desc : obj1) {

					String terms_conditions = desc.get("terms_conditions");

					descriptionLevelSaleOrderEntry = saleOrderEntryRepository
							.insertIntoSaleOrderDescriptionLevelEntry(soeid, sl_no, terms_conditions, created_by);

					sl_no++;
				}
			}

			createSaleOrdermap.put("message", (descriptionLevelSaleOrderEntry > 0) ? "Success" : "SaleOrderNotCreated");
			createSaleOrdermap.put("status", (descriptionLevelSaleOrderEntry > 0) ? "yes" : "no");
			createSaleOrdermap.put("action", "CreateSaleOrderEntry");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return createSaleOrdermap;

	}

	@PostMapping("/saleOrderEntry/updateSaleOrderEntry")
	public @ResponseBody Map<String, Object> updateSaleOrder(@RequestBody Map<String, Object> saleOrder) {

		Map<String, Object> updateSaleOrderEntrymap = new HashMap<String, Object>();

		Map<String, String> saleOrderlevel = null;
		List<Map<String, String>> saleItemlevel = null;
		List<Map<String, String>> saleDesclevel = null;
		int updateSaleOrderItemLevelEntry = 0;
		int updateDescriptionLevelSaleOrderEntry = 0;

		try {

			saleOrderlevel = (Map<String, String>) saleOrder.get("orderlevel");
			saleItemlevel = (List<Map<String, String>>) saleOrder.get("itemlevel");
			saleDesclevel = (List<Map<String, String>>) saleOrder.get("desclevel");
			String modified_by = null;

			if ((saleOrderlevel != null && !saleOrderlevel.isEmpty())
					&& (saleItemlevel != null && !saleItemlevel.isEmpty())
					&& (saleDesclevel != null && !saleDesclevel.isEmpty())) {

				// Set Sale Order Entry details
				SaleOrderEntry existingSaleOrderEntry = new SaleOrderEntry();
				existingSaleOrderEntry.setSoe_id(parseInteger(saleOrderlevel.get("soe_id")));
				/*
				 * int value =
				 * saleOrderEntryRepository.checkisLocked(saleOrderlevel.get("soe_id"));
				 * if(value > 0) { updateSaleOrderEntrymap.put("message",
				 * "Invoice Already Generated Not Able to Update"); return
				 * updateSaleOrderEntrymap; }
				 */
				existingSaleOrderEntry.setSale_order_type_id(parseInteger(saleOrderlevel.get("sale_order_type_id")));
				existingSaleOrderEntry.setLocation_type_id(parseInteger(saleOrderlevel.get("location_type_id")));
				existingSaleOrderEntry.setSale_order_to_id(parseInteger(saleOrderlevel.get("sale_order_to_id")));
				existingSaleOrderEntry.setAdvance_payment(parseInteger(saleOrderlevel.get("advance_payment")));
				existingSaleOrderEntry.setBilling_address_id(parseInteger(saleOrderlevel.get("billing_address_id")));
				existingSaleOrderEntry.setShipping_address_id(parseInteger(saleOrderlevel.get("shipping_address_id")));
				existingSaleOrderEntry.setBusiness_unit_id(parseInteger(saleOrderlevel.get("business_unit_id")));
				existingSaleOrderEntry.setTax1(parseFloat(saleOrderlevel.get("tax1")));
				existingSaleOrderEntry.setTax1_value(parseFloat(saleOrderlevel.get("tax1_value")));
				existingSaleOrderEntry.setTax2(parseFloat(saleOrderlevel.get("tax2")));
				existingSaleOrderEntry.setTax2_value(parseFloat(saleOrderlevel.get("tax2_value")));
				existingSaleOrderEntry.setTax3(parseFloat(saleOrderlevel.get("tax3")));
				existingSaleOrderEntry.setTax3_value(parseFloat(saleOrderlevel.get("tax3_value")));
				existingSaleOrderEntry.setNet_amount(parseFloat(saleOrderlevel.get("net_amount")));
				existingSaleOrderEntry.setTotal_tax(parseFloat(saleOrderlevel.get("total_tax")));
				existingSaleOrderEntry.setGrand_total(parseFloat(saleOrderlevel.get("grand_total")));
				existingSaleOrderEntry.setSale_order_code(saleOrderlevel.get("sale_order_code"));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				existingSaleOrderEntry
						.setAuction_date(LocalDateTime.parse(saleOrderlevel.get("auction_date"), formatter));
				existingSaleOrderEntry.setSale_order_duration(
						LocalDateTime.parse(saleOrderlevel.get("sale_order_duration"), formatter));
				existingSaleOrderEntry.setSoe_id(parseInteger(saleOrderlevel.get("soe_id")));
				existingSaleOrderEntry.setModified_by(saleOrderlevel.get("modified_by"));

				// Save Sale Order Entry
				saleOrderEntryRepository.save(existingSaleOrderEntry);

				modified_by = saleOrderlevel.get("modified_by");

				// Update Sale Order Item Level
				for (Map<String, String> itemsmap : saleItemlevel) {
					updateSaleOrderItemLevelEntry = saleOrderEntryRepository.updateSaleOrderItemLevel(
							parseInteger(itemsmap.get("auction_no")), parseInteger(itemsmap.get("scrap_type_id")),
							parseInteger(itemsmap.get("scrapitem_id")), parseInteger(itemsmap.get("uom_id")),
							parseInteger(itemsmap.get("quantity")), parseInteger(itemsmap.get("kg")),
							parseFloat(itemsmap.get("unit_price")), parseFloat(itemsmap.get("total")),
							parseInteger(itemsmap.get("tolerance")), modified_by, saleOrderlevel.get("soe_id"));
				}

				// Update Description Level Sale Order Entry
				for (Map<String, String> descmap : saleDesclevel) {
					String terms_conditions = descmap.get("terms_conditions");
					String sl_no = descmap.get("sl_no");
					System.out.println(">>>>>" + terms_conditions);

					// Check if the term condition exists for the given soe_id
					updateDescriptionLevelSaleOrderEntry = saleOrderEntryRepository
							.updateDescriptionLevelSaleOrderEntry(saleOrderlevel.get("soe_id"), sl_no, terms_conditions,
									modified_by);
				}

				updateSaleOrderEntrymap.put("message",
						(updateDescriptionLevelSaleOrderEntry > 0) ? "Success" : "SaleOrderNotUpdated");
				updateSaleOrderEntrymap.put("status", (updateDescriptionLevelSaleOrderEntry > 0) ? "yes" : "no");
				updateSaleOrderEntrymap.put("action", "UpdateSaleOrderEntry");

			}

		} catch (Exception e) {
			e.printStackTrace();
			updateSaleOrderEntrymap.put("message", "Error: " + e.getMessage());
			updateSaleOrderEntrymap.put("status", "no");
		}

		return updateSaleOrderEntrymap;
	}

	/*
	 * @GetMapping("/saleOrderEntry/getSaleOrderBasedOnId") public @ResponseBody
	 * Map<String, Object> getSaleOrderDetailsBasedOnId(@RequestParam String soe_id)
	 * {
	 * 
	 * Map<String, Object> saleOrderByIdmap = new HashMap<String, Object>();
	 * List<SaleOrderInterface> saleOrderlist = new ArrayList<SaleOrderInterface>();
	 * List<SaleOrderItemLevelInterface> saleOrderItemLevellist = new
	 * ArrayList<SaleOrderItemLevelInterface>();
	 * List<SaleOrderItemDescLevelInterface> saleOrderDescLevellist = new
	 * ArrayList<SaleOrderItemDescLevelInterface>();
	 * 
	 * try { saleOrderlist =
	 * saleOrderEntryRepository.getAllSaleOrderDetailsBAsedOnId(soe_id);
	 * saleOrderItemLevellist =
	 * saleOrderEntryRepository.getSaleOrderItemLevelDetails(soe_id);
	 * saleOrderDescLevellist =
	 * saleOrderEntryRepository.getSaleOrderDescriptionLevelDetails(soe_id);
	 * 
	 * saleOrderByIdmap.put("orderLevel", saleOrderlist);
	 * saleOrderByIdmap.put("orderItemLevel", saleOrderItemLevellist);
	 * saleOrderByIdmap.put("orderDescriptionLevel", saleOrderDescLevellist);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return saleOrderByIdmap;
	 * 
	 * }
	 */

	@GetMapping("/saleOrderEntry/getSaleOrderBasedOnId")
	public @ResponseBody Map<String, Object> getSaleOrderDetailsBasedOnId(@RequestParam String sale_order_code) {

		Map<String, Object> saleOrderByIdmap = new HashMap<String, Object>();
		List<SaleOrderInterface> saleOrderlist = new ArrayList<SaleOrderInterface>();
		List<SaleOrderItemLevelInterface> saleOrderItemLevellist = new ArrayList<SaleOrderItemLevelInterface>();
		List<SaleOrderItemDescLevelInterface> saleOrderDescLevellist = new ArrayList<SaleOrderItemDescLevelInterface>();

		try {

			String soe_id = saleOrderEntryRepository.getSoeIdBasedOnSaleOrderCode(sale_order_code);

			saleOrderlist = saleOrderEntryRepository.getAllSaleOrderDetailsBAsedOnId(soe_id);
			saleOrderItemLevellist = saleOrderEntryRepository.getSaleOrderItemLevelDetails(soe_id);
			saleOrderDescLevellist = saleOrderEntryRepository.getSaleOrderDescriptionLevelDetails(soe_id);

			saleOrderByIdmap.put("orderLevel", saleOrderlist);
			saleOrderByIdmap.put("orderItemLevel", saleOrderItemLevellist);
			saleOrderByIdmap.put("orderDescriptionLevel", saleOrderDescLevellist);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleOrderByIdmap;

	}

	@GetMapping("/saleOrderEntry/getSaleOrderBasedOnFactoryId/{factory_id}")
	public @ResponseBody Map<String, Object> getAllSaleOrderEntries(@PathVariable String factory_id) {

		Map<String, Object> getAllSaleOrderEntriesMap = new HashMap<String, Object>();
		List<allSaleOrderEntriesInterface> allSaleOrderList = null;

		try {
			allSaleOrderList = saleOrderEntryRepository.getAllSaleOrderDetailsBasedOnFactory(factory_id);

			getAllSaleOrderEntriesMap.put("action", "AllSaleOrderDetails");
			getAllSaleOrderEntriesMap.put("message",
					(allSaleOrderList.size() > 0) ? "Success" : "Sale Order details not found!");
			getAllSaleOrderEntriesMap.put("status", (allSaleOrderList.size() > 0) ? "yes" : "no");

			if ((allSaleOrderList != null) && (!allSaleOrderList.isEmpty())) {
				getAllSaleOrderEntriesMap.put("SaleOrderDetails", allSaleOrderList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAllSaleOrderEntriesMap;

	}

	@PostMapping("/saleOrderEntry/updateSaleOrder")
	public @ResponseBody Map<String, Object> updateSaleOrderByFlush(@RequestBody Map<String, Object> saleOrder) {

		Map<String, Object> updateSaleOrderWIthFlushmap = new HashMap<String, Object>();

		Map<String, String> saleOrderlevel = null;
		List<Map<String, String>> saleItemlevel = null;
		List<Map<String, String>> saleDesclevel = null;
		String modified_by = null;
		try {
			saleOrderlevel = (Map<String, String>) saleOrder.get("orderlevel");
			saleItemlevel = (List<Map<String, String>>) saleOrder.get("saleitemlevel");
			saleDesclevel = (List<Map<String, String>>) saleOrder.get("desclevel");

			if ((saleOrderlevel != null & !saleOrderlevel.isEmpty())
					&& (saleItemlevel != null & !saleItemlevel.isEmpty())
					&& (saleDesclevel != null && !saleDesclevel.isEmpty())) {

				SaleOrderEntry existingSaleOrderEntry = new SaleOrderEntry();
				existingSaleOrderEntry.setSale_order_type_id(parseInteger(saleOrderlevel.get("sale_order_type_id")));
				existingSaleOrderEntry.setLocation_type_id(parseInteger(saleOrderlevel.get("location_type_id")));
				existingSaleOrderEntry.setSale_order_to_id(parseInteger(saleOrderlevel.get("sale_order_to_id")));
				existingSaleOrderEntry.setAdvance_payment(parseInteger(saleOrderlevel.get("advance_payment")));
				existingSaleOrderEntry.setBilling_address_id(parseInteger(saleOrderlevel.get("billing_address_id")));
				existingSaleOrderEntry.setShipping_address_id(parseInteger(saleOrderlevel.get("shipping_address_id")));
				existingSaleOrderEntry.setBusiness_unit_id(parseInteger(saleOrderlevel.get("business_unit_id")));
				existingSaleOrderEntry.setTax1(parseFloat(saleOrderlevel.get("tax1")));
				existingSaleOrderEntry.setTax1_value(parseFloat(saleOrderlevel.get("tax1_value")));
				existingSaleOrderEntry.setTax2(parseFloat(saleOrderlevel.get("tax2")));
				existingSaleOrderEntry.setTax2_value(parseFloat(saleOrderlevel.get("tax2_value")));
				existingSaleOrderEntry.setTax3(parseFloat(saleOrderlevel.get("tax3")));
				existingSaleOrderEntry.setTax3_value(parseFloat(saleOrderlevel.get("tax3_value")));
				existingSaleOrderEntry.setNet_amount(parseFloat(saleOrderlevel.get("net_amount")));
				existingSaleOrderEntry.setTotal_tax(parseFloat(saleOrderlevel.get("total_tax")));
				existingSaleOrderEntry.setGrand_total(parseFloat(saleOrderlevel.get("grand_total")));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				existingSaleOrderEntry
						.setAuction_date(LocalDateTime.parse(saleOrderlevel.get("auction_date"), formatter));
				existingSaleOrderEntry.setSale_order_duration(
						LocalDateTime.parse(saleOrderlevel.get("sale_order_duration"), formatter));
				existingSaleOrderEntry.setSoe_id(parseInteger(saleOrderlevel.get("soe_id")));
				existingSaleOrderEntry.setModified_by(saleOrderlevel.get("modified_by"));

				saleOrderEntryRepository.save(existingSaleOrderEntry);

				modified_by = saleOrderlevel.get("modified_by");

				for (Map<String, String> map : saleItemlevel) {

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return updateSaleOrderWIthFlushmap;

	}

	// Helper Methods to safely parse values
	private Integer parseInteger(String value) {
		try {
			return value != null ? Integer.parseInt(value) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Float parseFloat(String value) {
		try {
			return value != null ? Float.parseFloat(value) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
