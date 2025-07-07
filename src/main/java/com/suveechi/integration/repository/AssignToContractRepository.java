package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.AssignToContract;
import com.suveechi.integration.interfaces.BankMasterInterface;
import com.suveechi.integration.interfaces.BusinessUnitInterface;
import com.suveechi.integration.interfaces.ContractG2Interfaces;
import com.suveechi.integration.interfaces.ContractListFromContractInterfaces;
import com.suveechi.integration.interfaces.InvoiceConsigneeAddressInterface;
import com.suveechi.integration.interfaces.LoadsG2List;
import com.suveechi.integration.interfaces.MileStoneAssignedContractListInterfaces;
import com.suveechi.integration.interfaces.ServiceCodeMasterInterface;
import com.suveechi.integration.interfaces.ShipmentDeliveryConditionInterfaces;
import com.suveechi.integration.interfaces.TaxMasterInterface;
import com.suveechi.integration.interfaces.WorkOrderMasterInterface;

import jakarta.transaction.Transactional;

public interface AssignToContractRepository extends JpaRepository<User, Integer>{

	@Transactional
	@Modifying
	@Query(value ="INSERT INTO CONTRACT_MASTER (Contract_id,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,work_id,bank_details_id,regd_office_id,it_pan_no,\r\n"
			+ "	Export,bid,H_SType,ServiceCode,SType,SCode,HType,HCode,Taxinx,ARENo,LotNo,ContainterNo,EPCGNo,CalculationType,[Percent],xptxt,rvscharg,tax,ntax,\r\n"
			+ "	advfrightrec,created_by, created_date) VALUES (:Contract_id,:invoice_to_id,:consignee_id,:shipment_mode_id,:delivery_condition_id,:product_desc_id,:work_id,:bank_details_id,:regd_office_id,:it_pan_no,\r\n"
			+ ":Export,:bid,:H_SType,:ServiceCode,:SType,:SCode,:HType,:HCode,:Taxinx,:ARENo,:LotNo,:ContainterNo,:EPCGNo,:CalculationType,:Percent,:xptxt,:rvscharg,:tax,:ntax,\r\n"
			+ "	:advfrightrec,:created_by,GETDATE())", nativeQuery = true)
	int createContract(int Contract_id, int invoice_to_id, int consignee_id, int shipment_mode_id,
			int delivery_condition_id, int product_desc_id, int work_id, int bank_details_id, int regd_office_id,
			int it_pan_no, String Export, int bid, int H_SType, String ServiceCode, String SType, String SCode,
			String HType, String HCode, int Taxinx, String ARENo, String LotNo, String ContainterNo, String EPCGNo,
			String CalculationType, String Percent, String xptxt, int rvscharg, int tax, int ntax, int advfrightrec,
			String created_by);

	@Transactional
	@Modifying
	@Query(value="UPDATE CONTRACT_MASTER SET Contract_id = :Contract_id,invoice_to_id = :invoice_to_id,consignee_id = :consignee_id,shipment_mode_id = :shipment_mode_id,delivery_condition_id = :delivery_condition_id,product_desc_id = :product_desc_id,work_id = :work_id,bank_details_id = :bank_details_id,regd_office_id = :regd_office_id,it_pan_no =:it_pan_no,\r\n"
			+ "	Export = :Export,bid = :bid,H_SType = :H_SType,ServiceCode = :ServiceCode,SType = :SType,SCode = :SCode,HType = :HType,HCode = :HCode,Taxinx = :Taxinx,ARENo = :ARENo,LotNo = :LotNo,ContainterNo = :ContainterNo,EPCGNo = :EPCGNo,CalculationType = :CalculationType,[Percent] = :Percent,xptxt = :xptxt,rvscharg = :rvscharg,tax = :tax,ntax =:ntax,\r\n"
			+ "	advfrightrec = :advfrightrec,modified_by = :modified_by,modified_date = GETDATE() where con_slno = :con_slno", nativeQuery = true)
	int updateContract(int Contract_id, int invoice_to_id, int consignee_id, int shipment_mode_id,
			int delivery_condition_id, int product_desc_id, int work_id, int bank_details_id, int regd_office_id,
			int it_pan_no, String Export, int bid, int H_SType, String ServiceCode, String SType, String SCode,
			String HType, String HCode, int Taxinx, String ARENo, String LotNo, String ContainterNo, String EPCGNo,
			String CalculationType, String Percent, String xptxt, int rvscharg, int tax, int ntax, int advfrightrec,
			String modified_by, int con_slno);

	@Transactional
	@Modifying
	@Query(value="update CONTRACT_MASTER SET is_delete = 1, modified_by = :modified_by, modified_date = GETDATE() where con_slno =:con_slno", nativeQuery = true)
	int updateContractDelete(String modified_by, String con_slno);

	@Query(value="select work_id,workorder_no,factory_id from WORKORDER_MASTER where is_delete = 0 and factory_id =:factory_id", nativeQuery = true)
	List<WorkOrderMasterInterface> getWorkOrderList(String factory_id);

	@Query(value="select account_id,bank_name,factory_id from BANK_MASTER where is_delete = 0 and factory_id =:factory_id", nativeQuery = true)
	List<BankMasterInterface> getBankNameList(String factory_id);

	@Query(value="select servicecode_id,service_type,service_code,factory_id from SERVICECODE_MASTER where is_delete = 0 and factory_id =:factory_id", nativeQuery = true)
	List<ServiceCodeMasterInterface> getServiceCode(String factory_id);

	@Query(value="select business_unit_id, business_unit_name,factory_id from business_units where is_delete = 0 and factory_id =:factory_id", nativeQuery = true)
	List<BusinessUnitInterface> getBusinessUnitList(String factory_id);

	@Query(value="select id,address, is_invoice,is_consignee,factory_id from INVOICE_CONSIGNEE_ADDRESS_MASTER where is_delete = 0 and factory_id =:factory_id", nativeQuery = true)
	List<InvoiceConsigneeAddressInterface> getInvoiceConsinee(String factory_id);

	@Query(value="select si_id, shipment_mode,delivery_condition,factory_id from SHIPMENT_DELIVERY_CONDITION where is_delete = 0 and factory_id =:factory_id", nativeQuery = true)
	List<ShipmentDeliveryConditionInterfaces> getShipmentDeliveryCondition(String factory_id);

	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER_HISTORY (con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,modified_by,modified_date,action,transaction_date, contract_name, milestone_id) "
			+ " select con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,:modified_by,GETDATE(),'UPDATE',GETDATE(),contract_name, milestone_id from CONTRACT_MASTER where con_slno = :con_slno ",nativeQuery = true)
	void insertContracrtHistory(int con_slno,String modified_by);
	
	

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER_HISTORY (con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,modified_by,modified_date,action,transaction_date,deleted_by,deleted_date,contract_name, milestone_id) "
			+ " select con_slno,contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,modified_by,modified_date,'DELETE',GETDATE(),:modified_by,GETDATE(),contract_name, milestone_id from CONTRACT_MASTER where con_slno = :con_slno ",nativeQuery = true)
	void updateContracrHistory(String con_slno,String modified_by);

	@Query(value = "select  tax_id, tax_name, tax_per from TAX_MASTER where is_delete = 0", nativeQuery = true)
	List<TaxMasterInterface> getTaxMaster();

	@Query(value = "select cm.*,bu.business_unit_name,bu.bu_code from CONTRACT_MASTER cm\r\n"
			+ " inner join business_units bu on bu.business_unit_id = cm.bid \r\n"
			+ " where cm.is_delete = 0 and cm.factory_id =  :factory_id",nativeQuery = true)
	List<AssignToContract> getContractList(String factory_id);

	@Query(value="select contract_id, descr,cname from Contracts", nativeQuery = true)
	List<ContractG2Interfaces> getContractListG2();
	
	@Query(value="SELECT contract_id, descr FROM Contracts WHERE contract_id NOT IN (SELECT contract_id FROM CONTRACT_MASTER)", nativeQuery = true)
	List<ContractG2Interfaces> getContractListUnique();
	
	@Query(value = "select distinct cm.*,\r\n"
			+ "icam.address as invoice_to_id_value, icam1.address as consignee_id_value,\r\n"
			+ "shd.shipment_mode as shipment_mode_id_value, shdd.delivery_condition as delivery_condition_id_value,\r\n"
			+ "bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value, om.org_name as reg_office_id_value,\r\n"
			+ "sm.service_code as s_code_value, sms.service_code as h_code_value,\r\n"
			+ "tm.tax_id,tm.tax_name,tm.tax_per,apni.tax_total as taxable_amount, apni.net_total as nontaxable_amount, apni.total from CONTRACT_MASTER cm\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = cm.consignee_id\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 on icam1.id = cm.invoice_to_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shd on shd.si_id = cm.shipment_mode_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd on  shdd.si_id = cm.delivery_condition_id\r\n"
			+ "inner join WORKORDER_MASTER wm on wm.work_id = cm.work_id\r\n"
			+ "inner join BANK_MASTER bm on bm.account_id = cm.bank_details_id\r\n"
			+ "inner join SERVICECODE_MASTER sms on sms.servicecode_id = cm.h_code\r\n"
			+ "inner join SERVICECODE_MASTER sm on sm.servicecode_id = cm.s_code\r\n"
			+ "inner join organization_master om on om.org_id = cm.regd_office_id\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id\r\n"
			+ "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id\r\n"
			+ "left join ADVANCEPACKINGNOTE apn on apn.con_id = cm.contract_id\r\n"
			+ "left join ADVANCEPACKINGNOTE_ITEM apni on apni.pn_id = apn.pn_id\r\n"
			+ " where cm.con_slno = :con_slno and cm.factory_id = :factory_id" ,nativeQuery = true)
	List<AssignToContract> searchContractById(String con_slno, String factory_id);

//	@Query(value = "WITH RankedData AS (\r\n"
//			+ "    SELECT \r\n"
//			+ "        cm.con_slno,\r\n"
//			+ "        cm.bid,\r\n"
//			+ "        cm.contract_id,\r\n"
//			+ "        cm.invoice_type_calculation,\r\n"
//			+ "        cm.percentage_value,\r\n"
//			+ "        cm.invoice_to_id,\r\n"
//			+ "        cm.consignee_id,\r\n"
//			+ "        cm.shipment_mode_id,\r\n"
//			+ "        cm.delivery_condition_id,\r\n"
//			+ "        cm.product_desc_id,\r\n"
//			+ "        cm.bank_details_id,\r\n"
//			+ "        cm.work_id,\r\n"
//			+ "        cm.regd_office_id,\r\n"
//			+ "        cm.s_code,\r\n"
//			+ "        cm.h_code,\r\n"
//			+ "        cm.export,\r\n"
//			+ "        cm.tax_ex_inc,\r\n"
//			+ "        cm.taxable,\r\n"
//			+ "        cm.non_taxable,\r\n"
//			+ "        cm.tax_payable,\r\n"
//			+ "        cm.freight_advance_recovery,\r\n"
//			+ "        cm.area_no,\r\n"
//			+ "        cm.lot_no,\r\n"
//			+ "        cm.containter_no,\r\n"
//			+ "        cm.epcgno,\r\n"
//			+ "        cm.export_title_text,\r\n"
//			+ "        cm.created_by,\r\n"
//			+ "        cm.created_date,\r\n"
//			+ "        cm.modified_by,\r\n"
//			+ "        cm.modified_date,\r\n"
//			+ "        cm.is_delete,\r\n"
//			+ "        cm.contract_name,\r\n"
//			+ "        cm.milestone_id,\r\n"
//			+ "        icam.address AS invoice_to_id_value,\r\n"
//			+ "        icam1.address AS consignee_id_value,\r\n"
//			+ "        shd.shipment_mode AS shipment_mode_id_value,\r\n"
//			+ "        shdd.delivery_condition AS delivery_condition_id_value,\r\n"
//			+ "        bm.bank_name AS bank_details_id_value,\r\n"
//			+ "        wm.workorder_no AS work_id_value,\r\n"
//			+ "        om.org_name AS reg_office_id_value,\r\n"
//			+ "        sm.service_code AS s_code_value,\r\n"
//			+ "        sms.service_code AS h_code_value,\r\n"
//			+ "        mm.milestone_name AS milestone_name_value,\r\n"
//			+ "        tm.tax_id,\r\n"
//			+ "        tm.tax_name,\r\n"
//			+ "        tm.tax_per,\r\n"
//			+ "        ROW_NUMBER() OVER (\r\n"
//			+ "            PARTITION BY tm.tax_id, tm.tax_name, tm.tax_per\r\n"
//			+ "            ORDER BY cm.contract_id DESC\r\n"
//			+ "        ) AS row_num\r\n"
//			+ "    FROM CONTRACT_MASTER cm\r\n"
//			+ "    INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam ON icam.id = cm.consignee_id\r\n"
//			+ "    INNER JOIN INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 ON icam1.id = cm.invoice_to_id\r\n"
//			+ "    INNER JOIN SHIPMENT_DELIVERY_CONDITION shd ON shd.si_id = cm.shipment_mode_id \r\n"
//			+ "    INNER JOIN SHIPMENT_DELIVERY_CONDITION shdd ON shdd.si_id = cm.delivery_condition_id\r\n"
//			+ "    INNER JOIN WORKORDER_MASTER wm ON wm.work_id = cm.work_id\r\n"
//			+ "    INNER JOIN BANK_MASTER bm ON bm.account_id = cm.bank_details_id\r\n"
//			+ "    INNER JOIN SERVICECODE_MASTER sms ON sms.servicecode_id = cm.h_code\r\n"
//			+ "    INNER JOIN SERVICECODE_MASTER sm ON sm.servicecode_id = cm.s_code\r\n"
//			+ "    INNER JOIN organization_master om ON om.org_id = cm.regd_office_id\r\n"
//			+ "    INNER JOIN CONTRACT_ASSIGN_TAX cat ON cat.contract_id = cm.contract_id\r\n"
//			+ "    INNER JOIN MILESTONE_MASTER mm ON mm.milestone_id = cm.milestone_id\r\n"
//			+ "    INNER JOIN TAX_MASTER tm ON tm.tax_id = cat.tax_id\r\n"
//			+ "    WHERE cm.contract_id = :con_slno\r\n"
//			+ ")\r\n"
//			+ "SELECT *\r\n"
//			+ "FROM RankedData\r\n"
//			+ "WHERE row_num = (select count(*) from CONTRACT_ASSIGN_TAX  where contract_id = :con_slno);\r\n" ,nativeQuery = true)
//	List<AssignToContract> searchContractById(String con_slno);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_ASSIGN_TAX(contract_id,tax_id,factory_id,created_by,created_date) values (:contract_id,:tax_id,:factory_id,:created_by,GETDATE())", nativeQuery = true)
	void insertTaxContract(int contract_id, int tax_id, String factory_id, String created_by);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_MASTER (contract_id,bid,invoice_type_calculation,percentage_value,invoice_to_id,consignee_id,shipment_mode_id,delivery_condition_id,product_desc_id,bank_details_id,work_id,regd_office_id,s_code,h_code,export,tax_ex_inc,taxable,non_taxable,tax_payable,freight_advance_recovery,area_no,lot_no,containter_no,epcgno,export_title_text,created_by,created_date,contract_name,factory_id) VALUES "
			+ " (:contract_id,:bid,:invoice_type_calculation,:percentage_value,:invoice_to_id,:consignee_id,:shipment_mode_id,:delivery_condition_id,:product_desc_id,:bank_details_id,:work_id,:regd_office_id,:s_code,:h_code,:export,:tax_ex_inc,:taxable,:non_taxable,:tax_payable,:freight_advance_recovery,:area_no,:lot_no,:containter_no,:epcgno,:export_title_text,:created_by,GETDATE(),:contract_name,:factory_id )",nativeQuery = true)
	int createContractInfo(int contract_id, int bid, String invoice_type_calculation, String percentage_value,
			int invoice_to_id, int consignee_id, int shipment_mode_id, int delivery_condition_id, String product_desc_id,
			int bank_details_id, int work_id, int regd_office_id, int s_code, int h_code, String export,
			String tax_ex_inc, String taxable, String non_taxable, String tax_payable, String freight_advance_recovery,
			String area_no, String lot_no, String containter_no, String epcgno, String export_title_text,
			String created_by,String contract_name, String factory_id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE CONTRACT_MASTER SET contract_id = :contract_id  ,bid = :bid  ,invoice_type_calculation = :invoice_type_calculation  ,percentage_value = :percentage_value  ,invoice_to_id = :invoice_to_id  ,consignee_id = :consignee_id  ,shipment_mode_id = :shipment_mode_id  ,delivery_condition_id = :delivery_condition_id  ,product_desc_id = :product_desc_id  ,bank_details_id = :bank_details_id  ,work_id = :work_id  ,regd_office_id = :regd_office_id  ,s_code = :s_code  ,h_code = :h_code  ,export = :export  ,tax_ex_inc = :tax_ex_inc  ,taxable = :taxable  ,non_taxable = :non_taxable  ,tax_payable = :tax_payable  ,freight_advance_recovery = :freight_advance_recovery  ,area_no = :area_no  ,lot_no = :lot_no  ,containter_no = :containter_no  ,epcgno = :epcgno  ,export_title_text = :export_title_text  ,modified_by = :modified_by  , modified_date = GETDATE(), contract_name = :contract_name where con_slno = :con_slno", nativeQuery = true)
	int updateContractInfo(int contract_id, int bid, String invoice_type_calculation, String percentage_value,
			int invoice_to_id, int consignee_id, int shipment_mode_id, int delivery_condition_id, String product_desc_id,
			int bank_details_id, int work_id, int regd_office_id, int s_code, int h_code, String export,
			String tax_ex_inc, String taxable, String non_taxable, String tax_payable, String freight_advance_recovery,
			String area_no, String lot_no, String containter_no, String epcgno, String export_title_text,
			String modified_by,String contract_name, int con_slno);

	@Transactional
	@Modifying
	@Query(value = "delete from CONTRACT_ASSIGN_TAX where id = :id", nativeQuery = true)
	void deleteTaxContract(int id);

	@Transactional
	@Modifying
	@Query(value = "delete from CONTRACT_ASSIGN_TAX where tax_id = :id and contract_id = :contract_id", nativeQuery = true)
	void deleteTaxContract(int id, int contract_id);
	
	@Query(value = "select tload_id,loadno from Tra_Loads where contract_id = :contract_id", nativeQuery = true)
	List<LoadsG2List> getTraLoadsG2(int contract_id);

//	@Query(value = "select DISTINCT cm.*,\r\n"
//			+ "icam.address as invoice_to_id_value, icam1.address as consignee_id_value,\r\n"
//			+ "shd.shipment_mode as shipment_mode_id_value, shdd.delivery_condition as delivery_condition_id_value,\r\n"
//			+ "bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value, om.org_name as reg_office_id_value,\r\n"
//			+ "sm.service_code as s_code_value, sms.service_code as h_code_value,\r\n"
//			+ "tm.tax_id,tm.tax_name,tm.tax_per from CONTRACT_MASTER cm\r\n"
//			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = cm.consignee_id\r\n"
//			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 on icam1.id = cm.invoice_to_id\r\n"
//			+ "inner join SHIPMENT_DELIVERY_CONDITION shd on shd.si_id = cm.shipment_mode_id \r\n"
//			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd on  shdd.si_id = cm.delivery_condition_id\r\n"
//			+ "inner join WORKORDER_MASTER wm on wm.work_id = cm.work_id\r\n"
//			+ "inner join BANK_MASTER bm on bm.account_id = cm.bank_details_id\r\n"
//			+ "inner join SERVICECODE_MASTER sms on sms.servicecode_id = cm.h_code\r\n"
//			+ "inner join SERVICECODE_MASTER sm on sm.servicecode_id = cm.s_code\r\n"
//			+ "inner join organization_master om on om.org_id = cm.regd_office_id\r\n"
//			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id\r\n"
//			+ "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id where cm.contract_id = :contract_id" ,nativeQuery = true)
//	List<AssignToContract> searchContract(String contract_id);
	
	@Query(value = "select distinct cm.*,\r\n"
			+ "icam.address as invoice_to_id_value, icam1.address as consignee_id_value,\r\n"
			+ "shd.shipment_mode as shipment_mode_id_value, shdd.delivery_condition as delivery_condition_id_value,\r\n"
			+ "bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value, om.org_name as reg_office_id_value,\r\n"
			+ "sm.service_code as s_code_value, sms.service_code as h_code_value,\r\n"
			+ "tm.tax_id,tm.tax_name,tm.tax_per,apni.tax_total as taxable_amount, apni.net_total as nontaxable_amount, apni.total from CONTRACT_MASTER cm\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = cm.consignee_id\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 on icam1.id = cm.invoice_to_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shd on shd.si_id = cm.shipment_mode_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd on  shdd.si_id = cm.delivery_condition_id\r\n"
			+ "inner join WORKORDER_MASTER wm on wm.work_id = cm.work_id\r\n"
			+ "inner join BANK_MASTER bm on bm.account_id = cm.bank_details_id\r\n"
			+ "inner join SERVICECODE_MASTER sms on sms.servicecode_id = cm.h_code\r\n"
			+ "inner join SERVICECODE_MASTER sm on sm.servicecode_id = cm.s_code\r\n"
			+ "inner join organization_master om on om.org_id = cm.regd_office_id\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id\r\n"
			+ "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id\r\n"
			+ "left join ADVANCEPACKINGNOTE apn on apn.con_id = cm.contract_id\r\n"
			+ "left join ADVANCEPACKINGNOTE_ITEM apni on apni.pn_id = apn.pn_id\r\n"
			+ " where cm.contract_id = :contract_id and cm.factory_id = :factory_id" ,nativeQuery = true)
	List<AssignToContract> searchContract(String contract_id, String factory_id);
	
	@Query(value = "select distinct cm.*,\r\n"
			+ "icam.address as invoice_to_id_value, icam1.address as consignee_id_value,\r\n"
			+ "shd.shipment_mode as shipment_mode_id_value, shdd.delivery_condition as delivery_condition_id_value,\r\n"
			+ "bm.bank_name as bank_details_id_value,wm.workorder_no as work_id_value, om.org_name as reg_office_id_value,\r\n"
			+ "sm.service_code as s_code_value, sms.service_code as h_code_value,\r\n"
			+ "tm.tax_id,tm.tax_name,tm.tax_per,apni.tax_total as taxable_amount, apni.net_total as nontaxable_amount, apni.total,\r\n"
			+ "icam.address AS consigneeAddress,icam.district AS consigneeDistrict,icam.pin_no AS consigneePinNo,\r\n"
			+ "icam.pan_no AS consigneePanNo,icam.gst_no AS consigneeGstNo,smm.state_name AS consigneeStateName,\r\n"
			+ "smm.state_code AS consigneeStateCode,cmm.country_name AS consigneeCountryName,\r\n"
			+ "icam1.address AS invoiceAddress,icam1.district AS invoiceDistrict,icam1.pin_no AS invoicePinNo,\r\n"
			+ "icam1.pan_no AS invoicePanNo,icam1.gst_no AS invoiceGstNo,smm1.state_name AS invoiceStateName,\r\n"
			+ "smm1.state_code AS invoiceStateCode,cmm1.country_name AS invoiceCountryName,\r\n"
			+ " bu.business_unit_name as businessUnitName, bu.gst_number as businessgstNumber,\r\n"
			+ "bu.location as businessLocation,bu.bu_code as businessBuCode, sbu.state_code businessStateCode,\r\n"
			+ "sbu.state_name businessStateName, sbu.state_id as businessStateId from CONTRACT_MASTER cm\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam on icam.id = cm.consignee_id\r\n"
			+ "inner join INVOICE_CONSIGNEE_ADDRESS_MASTER icam1 on icam1.id = cm.invoice_to_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shd on shd.si_id = cm.shipment_mode_id\r\n"
			+ "inner join SHIPMENT_DELIVERY_CONDITION shdd on  shdd.si_id = cm.delivery_condition_id\r\n"
			+ "inner join WORKORDER_MASTER wm on wm.work_id = cm.work_id\r\n"
			+ "inner join BANK_MASTER bm on bm.account_id = cm.bank_details_id\r\n"
			+ "inner join SERVICECODE_MASTER sms on sms.servicecode_id = cm.h_code\r\n"
			+ "inner join SERVICECODE_MASTER sm on sm.servicecode_id = cm.s_code\r\n"
			+ "inner join organization_master om on om.org_id = cm.regd_office_id\r\n"
			+ "inner join CONTRACT_ASSIGN_TAX cat on cat.contract_id = cm.contract_id\r\n"
			+ "inner join TAX_MASTER tm on tm.tax_id = cat.tax_id\r\n"
			+ "left join BUSINESS_UNITS bu on bu.business_unit_id = cm.bid\r\n"
			+ "left join ADVANCEPACKINGNOTE apn on apn.pn_id = (SELECT MAX(pn_id) FROM ADVANCEPACKINGNOTE WHERE con_id = cm.contract_id )"
			+ "LEFT JOIN ADVANCEPACKINGNOTE_ITEM apni ON apni.pn_id = apn.pn_id\r\n"
			+ "left join STATE_MASTER smm on smm.id = icam.state_id\r\n"
			+ "left join COUNTRY_MASTER cmm on cmm.id = icam.country_id\r\n"
			+ "left join STATE_MASTER smm1 on smm1.id = icam1.state_id\r\n"
			+ "left join COUNTRY_MASTER cmm1 on cmm1.id = icam1.country_id\r\n"
			+ "left join STATE_MASTER sbu on sbu.id = bu.state_id\r\n"
			+ " where cm.contract_id = :contract_id" ,nativeQuery = true)
	List<AssignToContract> searchContractinInvoice(String contract_id);

//	@Query(value =  "select cm.contract_id,cm.contract_name,cm.percentage_value,mm.milestone_code,mm.milestone_name,cm.milestone_id from CONTRACT_MASTER cm\r\n"
//			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = cm.milestone_id where cm.is_delete = 0", nativeQuery = true)
//	List<ContractListFromContractInterfaces> getContractListFromContract();

	@Query(value =  "select cm.contract_id,cm.contract_name from CONTRACT_MASTER cm\r\n"
			+ " where cm.is_delete = 0  and factory_id =:factory_id", nativeQuery = true) 
	List<ContractListFromContractInterfaces> getContractListFromContract(String factory_id);
	
	@Transactional
	@Modifying
	@Query(value="delete from CONTRACT_ASSIGN_TAX where contract_id = :contract_id", nativeQuery =  true)
	void deleteContractIdInAssignToContract(int contract_id);

	@Transactional
	@Modifying
	@Query(value="INSERT INTO MILESTONE_ASSGIN_CONTRACT_MASTER (milestone_id,contract_id,created_by,created_date,factory_id) VALUES (:milestone_id,:contract_id,:created_by,GETDATE(),:factory_id)", nativeQuery =  true)
	int assignMileStoneToContract(String milestone_id, String contract_id, String created_by, String factory_id);

	@Query(value = "select  mm.milestone_id, CONCAT(mm.milestone_code, ' - ', mm.milestone_name) AS milestone_name, ma.factory_id from MILESTONE_ASSGIN_CONTRACT_MASTER ma\r\n"
			+ "inner join MILESTONE_MASTER mm on mm.milestone_id = ma.milestone_id\r\n"
			+ " where  ma.contract_id = :contract_id and ma.factory_id = :factory_id", nativeQuery = true)
	List<MileStoneAssignedContractListInterfaces> listContractMaster(int contract_id, int factory_id);

	@Query(value = "select count(*) from MILESTONE_ASSGIN_CONTRACT_MASTER where milestone_id = :milestone_id and contract_id = :contract_id and factory_id =:factory_id" , nativeQuery =  true)
	int checkDuplicateMilestone(String milestone_id, String contract_id, String factory_id);

	@Transactional
	@Modifying
	@Query(value="UPDATE CONTRACT_MASTER SET is_locked = :locked where contract_id = :contact_id and factory_id = :factory_id", nativeQuery = true)
	void updateISReleaseInContractMaster(long contact_id, String locked, String factory_id);

	@Query(value = "select count(*) from CONTRACT_MASTER where con_slno = :con_slno and is_locked = 1", nativeQuery = true)
	int checkContractIdisLocked(int con_slno);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO CONTRACT_ASSIGN_TAX_HISTORY (id,contract_id,tax_id,factory_id,created_by,created_date,modified_by,modified_date,action,is_deleted) select id,contract_id,tax_id,factory_id,created_by,created_date,:modified_by,GETDATE(),'DELETE',1 from CONTRACT_ASSIGN_TAX where  tax_id = :tax_id and contract_id = :contract_id", nativeQuery = true)
	void moveToHistoryTaxContract(int tax_id, int contract_id, String modified_by);
	
	
	
	

}
