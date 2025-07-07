package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.suveechi.integration.entity.SaleOrderEntry;
import com.suveechi.integration.interfaces.SaleOrderInterface;
import com.suveechi.integration.interfaces.SaleOrderItemDescLevelInterface;
import com.suveechi.integration.interfaces.SaleOrderItemLevelInterface;
import com.suveechi.integration.interfaces.allSaleOrderEntriesInterface;

@Repository
public interface SaleOrderEntryRepository extends JpaRepository<SaleOrderEntry, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SALE_ORDER_ITEM_LEVEL_ENTRY (soe_id, auction_no, scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, tolerance, created_by, created_date)\r\n"
			+ "VALUES (:soeid, :auction_no, :scrap_type_id, :scrapitem_id, :uom_id, :quantity, :kg, :unit_price, :total, :tolerance, :created_by, GETDATE())", nativeQuery = true)
	int insertIntoSaleOrderItemLevelEntry(int soeid, String auction_no, String scrap_type_id, String scrapitem_id,
			String uom_id, String quantity, String kg, String unit_price, String total, String tolerance,
			String created_by);

	@Transactional
	@Modifying
	@Query(value = "insert into SALE_ORDER_DESCRIPTION_ENTRY (soe_id, sl_no, terms_conditions, created_by, created_date) VALUES (:soeid, :sl_no,  :terms_conditions, :created_by, GETDATE()) ", nativeQuery = true)
	int insertIntoSaleOrderDescriptionLevelEntry(int soeid, int sl_no, String terms_conditions, String created_by);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_ITEM_LEVEL_ENTRY " + "SET scrap_type_id = :scrap_type_id, "
			+ "scrapitem_id = :scrapitem_id, " + "uom_id = :uom_id, " + "quantity = :quantity, " + "kg = :kg, "
			+ "unit_price = :unit_price, " + "total = :total, " + "tolerance = :tolerance, "
			+ "modified_by = :modified_by, " + "modified_date = GETDATE() "
			+ "WHERE soe_id = :soe_id AND auction_no = :auction_no", nativeQuery = true)
	int updateSaleOrderItemLevel(Integer auction_no, Integer scrap_type_id, Integer scrapitem_id, Integer uom_id,
			Integer quantity, Integer kg, Float unit_price, Float total, Integer tolerance, String modified_by,
			String soe_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SALE_ORDER_DESCRIPTION_ENTRY set terms_conditions = :terms_conditions, modified_by = :modified_by WHERE soe_id = :soe_id and sl_no = :sl_no", nativeQuery = true)
	int updateDescriptionLevelSaleOrderEntry(String soe_id, String sl_no, String terms_conditions, String modified_by);

	@Query(value = "SELECT soe_id, sale_order_type_id, location_type_id, sale_order_to_id, sale_order_duration, advance_payment, \r\n"
			+ "               billing_address_id, shipping_address_id, business_unit_id, tax1, tax1_value, tax2, tax2_value, \r\n"
			+ "               tax3, tax3_value, net_amount, total_tax, grand_total from SALE_ORDER_ENTRY where soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderInterface> getAllSaleOrderDetailsBAsedOnId(String soe_id);

	@Query(value = "SELECT soe_id, auction_no, scrap_type_id, scrapitem_id, uom_id, quantity, kg, unit_price, total, tolerance \r\n"
			+ "        FROM SALE_ORDER_ITEM_LEVEL_ENTRY where soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderItemLevelInterface> getSaleOrderItemLevelDetails(String soe_id);

	@Query(value = "SELECT soe_id, sl_no, terms_conditions from SALE_ORDER_DESCRIPTION_ENTRY where soe_id = :soe_id", nativeQuery = true)
	List<SaleOrderItemDescLevelInterface> getSaleOrderDescriptionLevelDetails(String soe_id);

	@Query(value = " select count(*) from SALE_ORDER_ENTRY where soe_id = :id and is_locked = 1", nativeQuery = true)
	int checkisLocked(String id);

	@Query(value = "select soe.sale_order_code, soe.factory_id from SALE_ORDER_ENTRY soe\r\n"
			+ "where soe.factory_id = :factory_id and soe.is_delete=0", nativeQuery = true)
	List<allSaleOrderEntriesInterface> getAllSaleOrderDetailsBasedOnFactory(String factory_id);

	
	@Query(value = "SELECT soe_id from SALE_ORDER_ENTRY where sale_order_code = :sale_order_code",nativeQuery = true)
	String getSoeIdBasedOnSaleOrderCode(String sale_order_code);

}
