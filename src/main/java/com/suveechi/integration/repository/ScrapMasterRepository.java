package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.SalesOrderTandCInterfaces;
import com.suveechi.integration.interfaces.ScrapItemInterfaces;
import com.suveechi.integration.interfaces.ScrapSalesLocationInterfaces;
import com.suveechi.integration.interfaces.ScrapSalesOrderInterfaces;
import com.suveechi.integration.interfaces.ScrapTypeInterfaces;

import jakarta.transaction.Transactional;

public interface ScrapMasterRepository extends JpaRepository<User, Integer> {

	/* SCRAPTYPE START */
	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESCRAPTYPES_MASTER(scrap_typecode,scrap_typename,created_by,created_date,factory_id)"
			+ "VALUES(:scrap_typecode,:scrap_typename,:created_by,GETDATE(),:factory_id)", nativeQuery = true)
	int addScrapSaleScrapTypes(String scrap_typecode, String scrap_typename, String created_by, String factory_id);

	@Query(value = "SELECT stid,scrap_typecode,scrap_typename,factory_id FROM SCRAPSALESCRAPTYPES_MASTER WHERE factory_id = :factory_id and is_delete=0", nativeQuery = true)
	List<ScrapTypeInterfaces> getAllScrapSaleScrapTypes(String factory_id);

	@Query(value = "SELECT stid,scrap_typecode,scrap_typename FROM SCRAPSALESCRAPTYPES_MASTER WHERE is_delete=0 AND stid=:stid", nativeQuery = true)
	ScrapTypeInterfaces getScrapSaleScrapTypesById(String stid);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAPSALESCRAPTYPES_MASTER SET is_delete=1, modified_by =:modified_by, modified_date = GETDATE() WHERE stid=:stid", nativeQuery = true)
	int deleteScrapSaleScrapTypesById(String stid, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESCRAPTYPES_MASTER SET scrap_typecode=:scrap_typecode,scrap_typename=:scrap_typename,modified_by=:modified_by,modified_date=GETDATE() WHERE stid=:stid", nativeQuery = true)
	int UpdateScrapSaleScrapTypes(String stid, String scrap_typecode, String scrap_typename, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "INSERT into SCRAPSALESCRAPTYPES_MASTER_HISTORY(stid,scrap_typecode,scrap_typename,created_by,created_date,modified_by,modified_date,action,transaction_date)"
			+ " select stid,scrap_typecode,scrap_typename,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from SCRAPSALESCRAPTYPES_MASTER where stid=:stid", nativeQuery = true)
	void insertScrapSalesTypeHistoryRecord(String modified_by, String stid);

	@Modifying
	@Transactional
	@Query(value = "INSERT into SCRAPSALESCRAPTYPES_MASTER_HISTORY(stid,scrap_typecode,scrap_typename,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date)"
			+ " select stid,scrap_typecode,scrap_typename,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE() from SCRAPSALESCRAPTYPES_MASTER where stid=:stid", nativeQuery = true)
	void UpdateScrapSaleScrapTypesHistory(String stid, String modified_by);
	/* SCRAPTYPE END */

	/* SCRAPITEM START */
	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESSCRAPITEMS_MASTER(scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,factory_id)"
			+ "VALUES(:scraptypecode,:scrapitemcode,:scrapitemname,:created_by,GETDATE(),:factory_id)", nativeQuery = true)
	int addScrapSaleScrapItems(String scraptypecode, String scrapitemcode, String scrapitemname, String created_by, String factory_id);

	@Query(value = "SELECT siid,scraptypecode,scrapitemcode,scrapitemname,factory_id FROM SCRAPSALESSCRAPITEMS_MASTER WHERE factory_id = :factory_id and is_delete=0", nativeQuery = true)
	List<ScrapItemInterfaces> getAllScrapSaleScrapItems(String factory_id);

	@Query(value = "SELECT siid,scraptypecode,scrapitemcode,scrapitemname FROM SCRAPSALESSCRAPITEMS_MASTER WHERE is_delete=0 AND siid=:siid", nativeQuery = true)
	ScrapItemInterfaces getScrapItemsById(String siid);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESSCRAPITEMS_MASTER SET is_delete=1, modified_by =:modified_by, modified_date = GETDATE() WHERE siid=:siid", nativeQuery = true)
	int deleteScrapItemsById(String siid, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESSCRAPITEMS_MASTER SET scraptypecode=:scraptypecode,scrapitemcode=:scrapitemcode,scrapitemname=:scrapitemname,modified_by=:modified_by,modified_date=GETDATE() WHERE siid=:siid", nativeQuery = true)
	int UpdateScrapSaleScrapItems(String siid, String scraptypecode, String scrapitemcode, String scrapitemname,
			String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESSCRAPITEMS_MASTER_HISTORY(siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,modified_by,modified_date,action,transaction_date)"
			+ " select siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from SCRAPSALESSCRAPITEMS_MASTER where siid =:siid", nativeQuery = true)
	void insertScrapItemHistory(String siid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALESSCRAPITEMS_MASTER_HISTORY(siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date)"
			+ " select siid,scraptypecode,scrapitemcode,scrapitemname,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE() from SCRAPSALESSCRAPITEMS_MASTER where siid =:siid", nativeQuery = true)
	void updateScrapItemHistory(String siid, String modified_by);
	/* SCRAPITEM END */

	/* SCRAP SALES ORDER START */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SCRAPSALESORDERTYPE_MASTER (saleorder_typecode, saleorder_typename,created_by, created_date,factory_id) VALUES (:saleorder_typecode, :saleorder_typename, :created_by, GETDATE(),:factory_id)", nativeQuery = true)
	int addSalesOrder(String saleorder_typecode, String saleorder_typename, String created_by, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALESORDERTYPE_MASTER  SET saleorder_typecode = :saleorder_typecode, saleorder_typename = :saleorder_typename, modified_by = :modified_by, modified_date=GETDATE() WHERE saleorder_id= :saleorder_id", nativeQuery = true)
	int updateSalesorder(String saleorder_typecode, String saleorder_typename, String modified_by, String saleorder_id);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAPSALESORDERTYPE_MASTER_HISTORY (saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, modified_by, modified_date, action, transaction_date) "
			+ "SELECT saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, :modified_by, GETDATE(), 'UPDATE', GETDATE() "
			+ "FROM SCRAPSALESORDERTYPE_MASTER WHERE saleorder_id = :saleorder_id", nativeQuery = true)
	int insertSalesOrderHistory(String saleorder_id, String modified_by);

	@Transactional
	@Query(value = "SELECT saleorder_id, saleorder_typecode, saleorder_typename FROM SCRAPSALESORDERTYPE_MASTER WHERE saleorder_id = :saleorder_id", nativeQuery = true)

	ScrapSalesOrderInterfaces findSalesOrder(String saleorder_id);

	@Transactional
	@Query(value = "SELECT saleorder_id, saleorder_typecode, saleorder_typename FROM SCRAPSALESORDERTYPE_MASTER WHERE factory_id = :factory_id and is_delete = 0", nativeQuery = true)
	List<ScrapSalesOrderInterfaces> getAllSalesOrders(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SCRAPSALESORDERTYPE_MASTER SET is_delete=1 , modified_by = :modified_by, modified_date = GETDATE() where saleorder_id = :saleorder_id ", nativeQuery = true)
	int insertDeletedRecord(String saleorder_id, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SCRAPSALESORDERTYPE_MASTER_HISTORY (saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, modified_by, modified_date, action, transaction_date,deleted_by,deleted_date) "
			+ "SELECT saleorder_id,saleorder_typecode, saleorder_typename,created_by, created_date, modified_by, modified_date, 'DELETE', GETDATE(),:modified_by,GETDATE() "
			+ "FROM SCRAPSALESORDERTYPE_MASTER WHERE saleorder_id = :saleorder_id", nativeQuery = true)
	int updateSalesOrderHistory(String saleorder_id, String modified_by);

	/* SCRAP SALES ORDER END */

	/* SALES LOCATION START */

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALELOCATION(location_code,location_name,created_by,created_date,factory_id)"
			+ "VALUES(:location_code,:location_name,:created_by,GETDATE(),:factory_id)", nativeQuery = true)
	int addLocationTrack(String location_code, String location_name, String created_by, String factory_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALELOCATION SET is_delete=1 , modified_by=:modified_by, modified_date = GETDATE() WHERE slid=:slid", nativeQuery = true)
	int deleteLocationTrackById(String slid, String modified_by);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SCRAPSALELOCATION SET location_code=:location_code,location_name=:location_name,modified_by=:modified_by,modified_date=GETDATE() WHERE is_delete=0 AND slid=:slid", nativeQuery = true)
	int UpdateLocationTrack(String slid, String location_code, String location_name, String modified_by);

	@Query(value = "SELECT slid,location_code,location_name,created_by FROM SCRAPSALELOCATION WHERE factory_id = :factory_id and is_delete=0", nativeQuery = true)
	List<ScrapSalesLocationInterfaces> getAllLocationTrack(String factory_id);

	@Query(value = "SELECT slid,location_code,location_name FROM SCRAPSALELOCATION WHERE is_delete=0 AND slid=:slid", nativeQuery = true)
	ScrapSalesLocationInterfaces getLocationTrackById(String slid);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALELOCATION_HISTORY(slid,location_code,location_name,created_by,created_date,modified_by,modified_date,action,transaction_date)"
			+ " select slid,location_code,location_name,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from SCRAPSALELOCATION where slid = :slid", nativeQuery = true)
	void insertLocationHistory(String slid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT into SCRAPSALELOCATION_HISTORY(slid,location_code,location_name,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by, deleted_date)"
			+ " select slid,location_code,location_name,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE() from SCRAPSALELOCATION where slid = :slid", nativeQuery = true)
	void updateLocationHistory(String slid, String modified_by);

	/* SALES LOCATION END */

	/* SALES T & C START */

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SaleOrderTandC (saleordercode, slno, description, value, status, created_by, created_date,factory_id) \r\n"
			+ "VALUES (:saleordercode, :slno, :description, :value, :status, :created_by, GETDATE(), :factory_id)", nativeQuery = true)
	int addsalesTncDetailsDetails(String saleordercode, String slno, String description, String value, String status,
			String created_by, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SaleOrderTandC SET saleordercode=:saleordercode,slno=:slno,description=:description,value=:value,status=:status,"
			+ "modified_by=:modified_by,modified_date=GETDATE() WHERE tcid=:tcid ", nativeQuery = true)
	int updateSalesTncDetails(String tcid, String saleordercode, String slno, String description, String value,
			String status, String modified_by);

	@Query(value = "SELECT tcid,saleordercode,slno,description,value,status FROM SaleOrderTandC WHERE tcid=:tcid", nativeQuery = true)
	SalesOrderTandCInterfaces findSalesOrderTnCById(String tcid);

	@Query(value = "SELECT tcid,saleordercode,slno,description,value,status FROM SaleOrderTandC WHERE factory_id = :factory_id and  is_delete=0", nativeQuery = true)
	List<SalesOrderTandCInterfaces> getAllSalesOrderTnc(String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE SaleOrderTandC SET is_delete=1, modified_by =:modified_by, deleted_date = GETDATE() WHERE tcid=:tcid", nativeQuery = true)
	int deleteSalesTncDetails(String tcid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SaleOrderTandC_HISTORY (tcid,sale_ordercode, slno, description, value, status, created_by, created_date, modified_by, modified_date,action, transaction_date ) \r\n"
			+ " select tcid,sale_ordercode, slno, description, value, status, created_by, created_date, :modified_by, GETDATE(),'UPDATE' , GETDATE() from SaleOrderTandC where tcid= :tcid ", nativeQuery = true)
	void insertsalesTandCHistory(String tcid, String modified_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO SaleOrderTandC_HISTORY (tcid,sale_ordercode, slno, description, value, status, created_by, created_date, modified_by, modified_date,action, transaction_date,deleted_by, deleted_date ) \r\n"
			+ " select tcid,sale_ordercode, slno, description, value, status, created_by, created_date, modified_by, modified_date,'DELETE',:modified_by , GETDATE() from SaleOrderTandC where tcid= :tcid", nativeQuery = true)
	void updatesalesTandCHistory(String tcid, String modified_by);

	/* SALES T&C END */
}
