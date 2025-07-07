/*
 * package com.suveechi.integration.controller;
 * 
 * import java.time.LocalDateTime;
 * 
 * import java.time.format.DateTimeFormatter; import java.util.HashMap; import
 * java.util.List; import java.util.Map;
 * 
 * import org.apache.logging.log4j.LogManager; import
 * org.apache.logging.log4j.Logger; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.web.bind.annotation.CrossOrigin; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.ResponseBody; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.suveechi.integration.entity.SaleOrderPackingNote; import
 * com.suveechi.integration.interfaces.AllScrapingNoteDetails; import
 * com.suveechi.integration.interfaces.LatestScrapNoteBalanceInterface; import
 * com.suveechi.integration.interfaces.SaleOrderPackingNoteRepository; import
 * com.suveechi.integration.interfaces.SalePackingNoteInterface; import
 * com.suveechi.integration.interfaces.SalePackingNoteItemsInterface;
 * 
 * @RestController
 * 
 * @RequestMapping("/jssl")
 * 
 * @CrossOrigin public class SaleOrderPackingNoteController {
 * 
 * @Autowired private SaleOrderPackingNoteRepository saleOrderPackingNoterepo =
 * null;
 * 
 * Logger logger = LogManager.getLogger(SaleOrderPackingNoteController.class);
 * 
 * @PostMapping("/saleOrderPackingNote/addSaleOrderPackingNote")
 * public @ResponseBody Map<String, Object> addSaleOrderPackingNote(
 * 
 * @RequestBody Map<String, Object> saleOrderPackingNote) {
 * 
 * logger.info("EXECUTING METHOD :: addSaleOrderPackingNote");
 * 
 * Map<String, Object> createScrapPackingNotemap = new HashMap<String,
 * Object>(); Map<String, String> scrapPackingNoteInfo = null; Map<String,
 * String> scrapPackingNoteItems = null;
 * 
 * try {
 * 
 * scrapPackingNoteInfo = (Map<String, String>)
 * saleOrderPackingNote.get("scrap_packing_note_info"); scrapPackingNoteItems =
 * (Map<String, String>) saleOrderPackingNote.get("scrap_packing_note_items");
 * 
 * SaleOrderPackingNote salepackingnote = new SaleOrderPackingNote();
 * salepackingnote.setSale_order_code(scrapPackingNoteInfo.get("sale_order_code"
 * )); salepackingnote.setScp_load(scrapPackingNoteInfo.get("scp_load"));
 * salepackingnote.setTotal_steel_qty(scrapPackingNoteInfo.get("total_steel_qty"
 * )); salepackingnote.setTotal_non_steel_qty(scrapPackingNoteInfo.get(
 * "total_non_steel_qty"));
 * 
 * DateTimeFormatter formatter =
 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); salepackingnote
 * .setSale_order_validity(LocalDateTime.parse(scrapPackingNoteInfo.get(
 * "sale_order_validity")));
 * salepackingnote.setVendor_id(scrapPackingNoteInfo.get("vendor_id"));
 * salepackingnote.setWeighbridge_no(scrapPackingNoteInfo.get("weighbridge_no"))
 * ;
 * salepackingnote.setTransportername(scrapPackingNoteInfo.get("transportername"
 * )); salepackingnote.setVehicleno(scrapPackingNoteInfo.get("vehicle_no"));
 * salepackingnote.setScp_pn_date(scrapPackingNoteInfo.get("scp_pn_date"));
 * 
 * String saleOrderCode = scrapPackingNoteInfo.get("sale_order_code");
 * salepackingnote.setBuyer_ref_no(saleOrderCode);
 * 
 * salepackingnote.setCreated_by(scrapPackingNoteInfo.get("created_by"));
 * salepackingnote.setCreated_date(LocalDateTime.now());
 * 
 * logger.info("EXECUTING METHOD :: BEFORE saving scrap packing note");
 * 
 * saleOrderPackingNoterepo.save(salepackingnote);
 * 
 * logger.info("EXECUTING METHOD :: AFTER saving scrap packing note");
 * 
 * String scrap_type_id = scrapPackingNoteItems.get("scrap_type_id"); String
 * scrapitem_id = scrapPackingNoteItems.get("scrapitem_id"); String uom_id =
 * scrapPackingNoteItems.get("uom_id"); String quantity =
 * scrapPackingNoteItems.get("quantity"); String kg =
 * scrapPackingNoteItems.get("kg"); String unit_price =
 * scrapPackingNoteItems.get("unit_price"); String total =
 * scrapPackingNoteItems.get("total"); String total_ordered =
 * scrapPackingNoteItems.get("total_ordered"); String total_sold =
 * scrapPackingNoteItems.get("total_sold"); String balance =
 * scrapPackingNoteItems.get("balance"); String pn_items_created_by =
 * salepackingnote.getCreated_by(); LocalDateTime pn_items_created_date =
 * salepackingnote.getCreated_date();
 * 
 * int saleOrder_pn_id = salepackingnote.getScp_pn_id(); String sale_order_code
 * = salepackingnote.getSale_order_code(); String scp_load =
 * salepackingnote.getScp_load();
 * 
 * logger.info(
 * "EXECUTING METHOD :: BEFORE saving scrap packing note item details => addScrapPackingNoteItems "
 * );
 * 
 * int saveScrapPackingNoteItems =
 * saleOrderPackingNoterepo.addScrapPackingNoteItems(saleOrder_pn_id,
 * sale_order_code, scp_load, scrap_type_id, scrapitem_id, uom_id, quantity, kg,
 * unit_price, total, total_ordered, total_sold, balance, pn_items_created_by,
 * pn_items_created_date);
 * 
 * logger.info(
 * "EXECUTING METHOD :: AFTER saving scrap packing note item details => addScrapPackingNoteItems "
 * );
 * 
 * } catch (Exception e) {
 * logger.error("ERROR IN THE METHOD :: addSaleOrderPackingNote  -> " +
 * e.getMessage()); }
 * 
 * return saleOrderPackingNote;
 * 
 * }
 * 
 * @GetMapping("/saleOrderPackingNote/getPackingNoteById") public @ResponseBody
 * Map<String, Object> getPackingNoteBasedOnScrapNote(@RequestParam String
 * scp_load) {
 * 
 * logger.info("EXECUTING METHOD :: getPackingNoteBasedOnScrapNote");
 * 
 * Map<String, Object> getScrapPackingnotemap = new HashMap<String, Object>();
 * SalePackingNoteInterface salepackingnoteinterface = null;
 * SalePackingNoteItemsInterface salepackingnoteitemsinterface = null; String
 * pnsale_order_code = null; LatestScrapNoteBalanceInterface scrapnotebalance =
 * null; try {
 * 
 * logger.info(
 * "EXECUTING METHOD :: getPackingNoteBasedOnScrapNote :: getScrapPackingNoteDetailsForParticularSaleOrder "
 * );
 * 
 * salepackingnoteinterface = saleOrderPackingNoterepo
 * .getScrapPackingNoteDetailsForParticularSaleOrder(scp_load);
 * 
 * logger.
 * info("EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before executing getSaleOrderCode "
 * );
 * 
 * pnsale_order_code = saleOrderPackingNoterepo.getSaleOrderCode(scp_load);
 * 
 * logger.
 * info("EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before executing getLastestBalanceInfo "
 * );
 * 
 * scrapnotebalance =
 * saleOrderPackingNoterepo.getLastestBalanceInfo(pnsale_order_code);
 * 
 * logger.info(
 * "EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before executing getScrapPackingNoteItemsDetails "
 * );
 * 
 * salepackingnoteitemsinterface =
 * saleOrderPackingNoterepo.getScrapPackingNoteItemsDetails(scp_load);
 * 
 * getScrapPackingnotemap.put("action", "SaleOrderScrapPackingNoteDetails");
 * getScrapPackingnotemap.put("message", (salepackingnoteinterface != null) ?
 * "Success" : "Sale Order Scrap Packing Note details not found!");
 * getScrapPackingnotemap.put("status", (salepackingnoteinterface != null) ?
 * "yes" : "no"); getScrapPackingnotemap.put("PackingNoteDetails",
 * salepackingnoteinterface);
 * getScrapPackingnotemap.put("PackingNoteItemDetails",
 * salepackingnoteitemsinterface);
 * getScrapPackingnotemap.put("PackingNoteDetails", salepackingnoteinterface);
 * getScrapPackingnotemap.put("BalanceInfo", scrapnotebalance);
 * 
 * } catch (Exception e) {
 * logger.error("ERROR IN THE METHOD :: getPackingNoteBasedOnScrapNote  -> " +
 * e.getMessage()); } return getScrapPackingnotemap;
 * 
 * }
 * 
 * @PostMapping("/saleOrderPackingNote/updatePackingNoteDetails")
 * public @ResponseBody Map<String, Object> updateScrapPackingNote(
 * 
 * @RequestBody Map<String, Object> updateScrapPackingNote) {
 * 
 * logger.info("EXECUTING METHOD :: updateScrapPackingNote");
 * 
 * Map<String, Object> updateScrapPackingNotemap = new HashMap<String,
 * Object>(); Map<String, String> scrapPackingNoteInfo = null; Map<String,
 * String> scrapPackingNoteItems = null;
 * 
 * try { scrapPackingNoteInfo = (Map<String, String>)
 * updateScrapPackingNote.get("scrap_packing_note_info"); scrapPackingNoteItems
 * = (Map<String, String>)
 * updateScrapPackingNote.get("scrap_packing_note_items");
 * 
 * SaleOrderPackingNote updatesalepackingnote = new SaleOrderPackingNote();
 * DateTimeFormatter formatter =
 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 * 
 * updatesalepackingnote.setScp_load(scrapPackingNoteInfo.get("scp_load"));
 * updatesalepackingnote.setVendor_id(scrapPackingNoteInfo.get("vendor_id"));
 * updatesalepackingnote.setWeighbridge_no(scrapPackingNoteInfo.get(
 * "weighbridge_no"));
 * updatesalepackingnote.setTransportername(scrapPackingNoteInfo.get(
 * "transportername"));
 * updatesalepackingnote.setVehicleno(scrapPackingNoteInfo.get("vehicle_no"));
 * updatesalepackingnote.setScp_pn_date(scrapPackingNoteInfo.get("scp_pn_date"))
 * ;
 * 
 * updatesalepackingnote.setCredit_reference(scrapPackingNoteInfo.get(
 * "credit_reference"));
 * updatesalepackingnote.setOther_reference(scrapPackingNoteInfo.get(
 * "other_reference"));
 * updatesalepackingnote.setProject_reference(scrapPackingNoteInfo.get(
 * "project_reference"));
 * updatesalepackingnote.setCentral_excise_tarrif_no(scrapPackingNoteInfo.get(
 * "central_excise_tarrif_no"));
 * updatesalepackingnote.setRemarks(scrapPackingNoteInfo.get("remarks"));
 * updatesalepackingnote.setModified_by(scrapPackingNoteInfo.get("modified_by"))
 * ; updatesalepackingnote.setModified_date(LocalDateTime.now());
 * 
 * String scp_load = updatesalepackingnote.getScp_load();
 * 
 * // SaleOrderPackingNote existingRecord = //
 * saleOrderPackingNoterepo.findByScpLoad(scp_load);
 * 
 * logger.
 * info("EXECUTING METHOD :: updateScrapPackingNote :: findScp_loadIfExists ");
 * 
 * SaleOrderPackingNote existingRecord =
 * saleOrderPackingNoterepo.findScp_loadIfExists(scp_load); if (existingRecord
 * != null) {
 * 
 * existingRecord.setWeighbridge_no(updatesalepackingnote.getWeighbridge_no());
 * existingRecord.setTransportername(updatesalepackingnote.getTransportername())
 * ; existingRecord.setVehicleno(updatesalepackingnote.getVehicleno());
 * existingRecord.setScp_pn_date(updatesalepackingnote.getScp_pn_date());
 * existingRecord.setModified_by(updatesalepackingnote.getModified_by());
 * existingRecord.setModified_date(updatesalepackingnote.getModified_date());
 * updatesalepackingnote.setCredit_reference(updatesalepackingnote.
 * getCredit_reference());
 * updatesalepackingnote.setOther_reference(updatesalepackingnote.
 * getOther_reference());
 * updatesalepackingnote.setProject_reference(updatesalepackingnote.
 * getProject_reference());
 * updatesalepackingnote.setCentral_excise_tarrif_no(updatesalepackingnote.
 * getCentral_excise_tarrif_no());
 * updatesalepackingnote.setRemarks(updatesalepackingnote.getRemarks());
 * 
 * logger.
 * info("EXECUTING METHOD :: updateScrapPackingNote ::BEFORE saving ScrapPackingNoteDetails"
 * );
 * 
 * saleOrderPackingNoterepo.save(existingRecord);
 * 
 * String quantity = scrapPackingNoteItems.get("quantity"); String kg =
 * scrapPackingNoteItems.get("kg"); String unit_price =
 * scrapPackingNoteItems.get("unit_price"); String total =
 * scrapPackingNoteItems.get("total"); String total_ordered =
 * scrapPackingNoteItems.get("total_ordered"); String total_sold =
 * scrapPackingNoteItems.get("total_sold"); String balance =
 * scrapPackingNoteItems.get("balance"); String modified_by =
 * updatesalepackingnote.getModified_by(); LocalDateTime modified_date =
 * updatesalepackingnote.getModified_date();
 * 
 * logger.info(
 * "EXECUTING METHOD :: updateScrapPackingNote ::BEFORE saving ScrapPackingNoteItemDetailsDetails"
 * );
 * 
 * int updatePackingNoteItemsrecord =
 * saleOrderPackingNoterepo.updatePackingNoteItemsInfo(quantity, kg, unit_price,
 * total, total_ordered, total_sold, balance, modified_by, modified_date,
 * scp_load);
 * 
 * updateScrapPackingNotemap.put("message", (updatePackingNoteItemsrecord > 0) ?
 * "Success" : "Scrap Packing Note not updated");
 * updateScrapPackingNotemap.put("status", (updatePackingNoteItemsrecord > 0) ?
 * "yes" : "no"); updateScrapPackingNotemap.put("action",
 * "UpdateScrapPackingNoteDetails");
 * 
 * }
 * 
 * } catch (Exception e) {
 * logger.error("ERROR IN THE METHOD :: updateScrapPackingNote  -> " +
 * e.getMessage()); } return updateScrapPackingNotemap;
 * 
 * }
 * 
 * @PostMapping("/saleOrderPackingNote/verifyPackingNoteDetails")
 * public @ResponseBody Map<String, Object> verifyScrapPackingNote(
 * 
 * @RequestBody Map<String, Object> verifyScrapPackingNote) {
 * 
 * logger.info("EXECUTING METHOD :: verifyScrapPackingNote");
 * 
 * Map<String, Object> verifyScrapPackingNotemap = new HashMap<String,
 * Object>(); Map<String, String> scrapPackingNoteInfo = null; Map<String,
 * String> scrapPackingNoteItems = null;
 * 
 * try { scrapPackingNoteInfo = (Map<String, String>)
 * verifyScrapPackingNote.get("scrap_packing_note_info"); scrapPackingNoteItems
 * = (Map<String, String>)
 * verifyScrapPackingNote.get("scrap_packing_note_items");
 * 
 * SaleOrderPackingNote verifysalepackingnote = new SaleOrderPackingNote();
 * DateTimeFormatter formatter =
 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 * 
 * verifysalepackingnote.setScp_load(scrapPackingNoteInfo.get("scp_load")); //
 * verifysalepackingnote.setVendor_id(scrapPackingNoteInfo.get("vendor_id"));
 * verifysalepackingnote.setWeighbridge_no(scrapPackingNoteInfo.get(
 * "weighbridge_no"));
 * verifysalepackingnote.setTransportername(scrapPackingNoteInfo.get(
 * "transportername"));
 * verifysalepackingnote.setVehicleno(scrapPackingNoteInfo.get("vehicle_no"));
 * verifysalepackingnote.setScp_pn_date(scrapPackingNoteInfo.get("scp_pn_date"))
 * ;
 * verifysalepackingnote.setIs_verified(scrapPackingNoteInfo.get("is_verified"))
 * ;
 * verifysalepackingnote.setVerified_by(scrapPackingNoteInfo.get("verified_by"))
 * ; verifysalepackingnote.setVerified_date(LocalDateTime.now());
 * verifysalepackingnote.setRemarks(scrapPackingNoteInfo.get("remarks"));
 * 
 * String scp_load = verifysalepackingnote.getScp_load();
 * 
 * SaleOrderPackingNote verifyExistingRecord =
 * saleOrderPackingNoterepo.findScp_loadIfExists(scp_load); if
 * (verifyExistingRecord != null) {
 * 
 * verifyExistingRecord.setWeighbridge_no(verifysalepackingnote.
 * getWeighbridge_no());
 * verifyExistingRecord.setTransportername(verifysalepackingnote.
 * getTransportername());
 * verifyExistingRecord.setVehicleno(verifysalepackingnote.getVehicleno());
 * verifyExistingRecord.setScp_pn_date(verifysalepackingnote.getScp_pn_date());
 * verifyExistingRecord.setVerified_by(verifysalepackingnote.getVerified_by());
 * verifyExistingRecord.setVerified_date(verifysalepackingnote.getVerified_date(
 * )); verifyExistingRecord.setRemarks(verifysalepackingnote.getRemarks());
 * 
 * logger.info(
 * "EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before saving/verifying ScrapPackingNoteDetails "
 * );
 * 
 * saleOrderPackingNoterepo.save(verifyExistingRecord);
 * 
 * String quantity = scrapPackingNoteItems.get("quantity"); String kg =
 * scrapPackingNoteItems.get("kg"); String unit_price =
 * scrapPackingNoteItems.get("unit_price"); String total =
 * scrapPackingNoteItems.get("total"); String total_ordered =
 * scrapPackingNoteItems.get("total_ordered"); String total_sold =
 * scrapPackingNoteItems.get("total_sold"); String balance =
 * scrapPackingNoteItems.get("balance"); String modified_by =
 * verifysalepackingnote.getVerified_by(); LocalDateTime modified_date =
 * verifysalepackingnote.getVerified_date();
 * 
 * logger.info(
 * "EXECUTING METHOD :: getPackingNoteBasedOnScrapNote ::before saving/verifying ScrapPackingNoteItemDetails "
 * );
 * 
 * int updatePackingNoteItemsDuringVerificationrecord = saleOrderPackingNoterepo
 * .updatePackingNoteItemsInfo(quantity, kg, unit_price, total, total_ordered,
 * total_sold, balance, modified_by, modified_date, scp_load);
 * 
 * verifyScrapPackingNotemap.put("message",
 * (updatePackingNoteItemsDuringVerificationrecord > 0) ? "Success" :
 * "Scrap Packing Note not verified"); verifyScrapPackingNotemap.put("status",
 * (updatePackingNoteItemsDuringVerificationrecord > 0) ? "yes" : "no");
 * verifyScrapPackingNotemap.put("action", "VerifyScrapPackingNoteDetails");
 * 
 * }
 * 
 * } catch (Exception e) {
 * logger.error("ERROR IN THE METHOD :: verifyScrapPackingNote  -> " +
 * e.getMessage()); } return verifyScrapPackingNotemap;
 * 
 * }
 * 
 * @GetMapping("/saleOrderPackingNote/allScrapPackingNoteDetails")
 * public @ResponseBody Map<String, Object> getAllScrapPackingNoteDetails() {
 * 
 * logger.info("EXECUTING METHOD :: getAllScrapPackingNoteDetails");
 * 
 * Map<String, Object> allScrapPackingNoteDetailsmap = new HashMap<String,
 * Object>(); List<AllScrapingNoteDetails> allScPackingNoteDetails = null;
 * 
 * try {
 * 
 * allScPackingNoteDetails =
 * saleOrderPackingNoterepo.getAllScrapPackingnoteDetails();
 * 
 * allScrapPackingNoteDetailsmap.put("message", ((allScPackingNoteDetails !=
 * null) && (!allScPackingNoteDetails.isEmpty())) ? "Success" :
 * "AllScrapPackingNoteDetails Not Available");
 * allScrapPackingNoteDetailsmap.put("status", ((allScPackingNoteDetails !=
 * null) && (!allScPackingNoteDetails.isEmpty())) ? "yes" : "no");
 * allScrapPackingNoteDetailsmap.put("action", "DistrictInfo");
 * 
 * if ((allScPackingNoteDetails != null) &&
 * (!allScPackingNoteDetails.isEmpty())) {
 * allScrapPackingNoteDetailsmap.put("AllScraPackingNoteDetails",
 * allScPackingNoteDetails); }
 * 
 * } catch (Exception e) {
 * logger.error("ERROR IN THE METHOD :: getAllScrapPackingNoteDetails  -> " +
 * e.getMessage()); } return allScrapPackingNoteDetailsmap;
 * 
 * }
 * 
 * public @ResponseBody Map<String, Object> createInvoiceForScrap() {
 * 
 * Map<String, Object> createScrapInvoicemap = new HashMap<String, Object>();
 * 
 * }
 * 
 * }
 */