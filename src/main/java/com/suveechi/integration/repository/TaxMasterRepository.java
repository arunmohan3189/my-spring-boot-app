package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.TaxMasterInterface;

import jakarta.transaction.Transactional;

public interface TaxMasterRepository extends JpaRepository<User, Integer>{
	@Transactional
	@Modifying
	@Query(value="INSERT into TAX_MASTER(tax_name,tax_per,startdate,enddate,created_by,created_date)"
	+"VALUES(:tax_name,:tax_per,:startdate,:enddate,:created_by,GETDATE())",nativeQuery = true)
	int addTaxMasters(String tax_name,String tax_per,String startdate,String enddate,String created_by);
	
	@Query(value="SELECT * FROM TAX_MASTER WHERE is_delete=0 ",nativeQuery = true)
	List<TaxMasterInterface> getAllTaxMasters();
	
	@Query(value="SELECT tax_id,tax_name,tax_per,startdate,enddate FROM TAX_MASTER WHERE is_delete=0 AND tax_id=:tax_id",nativeQuery = true)
	TaxMasterInterface getTaxMastersById(String tax_id);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE TAX_MASTER SET is_delete=1 , modified_by =:modified_by, modified_date = GETDATE() WHERE tax_id=:tax_id", nativeQuery=true)
	int deleteTaxMastersById(String tax_id, String modified_by);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE TAX_MASTER SET tax_name=:tax_name,tax_per=:tax_per,startdate=:startdate,enddate=:enddate,modified_by=:modified_by,modified_date=GETDATE() WHERE tax_id=:tax_id", nativeQuery=true)
	int UpdateTaxMasters(String tax_name,String tax_per,String startdate,String enddate, String modified_by, String tax_id);

	
	@Modifying
	@Transactional
	@Query(value="INSERT INTO TAX_MASTER_HISTORY (tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,modified_by,modified_date,action,transaction_date) select tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE() from TAX_MASTER WHERE tax_id=:tax_id", nativeQuery=true)
	int insertTaxMasterHistory( String modified_by, String tax_id);
	
	@Modifying
	@Transactional
	@Query(value="INSERT INTO TAX_MASTER_HISTORY (tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,modified_by,modified_date,action,transaction_date,deletd_by,deleted_date) select tax_id,tax_name,tax_per,startdate,enddate,created_by,created_date,modified_by,modified_date,'UPDATE',GETDATE(),:modified_by,GETDATE() from TAX_MASTER WHERE tax_id=:tax_id", nativeQuery=true)
	int updateTaxMasterHistory( String modified_by, String tax_id);
}
