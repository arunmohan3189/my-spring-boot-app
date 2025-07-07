package com.suveechi.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suveechi.integration.entity.User;
import com.suveechi.integration.interfaces.AdvanceSeriesMasterInterfaces;
import com.suveechi.integration.interfaces.ChallanSeriesMasterInterfaces;
import com.suveechi.integration.interfaces.DebitCreditSeriesMasterInterfaces;
import com.suveechi.integration.interfaces.DeliveryChallanSeriesMasterInterfaces;
import com.suveechi.integration.interfaces.GSTSeriesMasterInterfaces;
import com.suveechi.integration.interfaces.PaymentSeriesMasterInterfaces;
import com.suveechi.integration.interfaces.SelfInvoiceSeriesMasterInterfaces;
import com.suveechi.integration.interfaces.SeriesMasterList;

import jakarta.transaction.Transactional;

public interface SeriesMasterRepository extends JpaRepository<User, Integer>{
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_gst, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertGST(String state_id, String invoice_series, String start_date, String end_date, String user_id, String status, String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_advance, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertAdvance(String state_id, String invoice_series, String start_date, String end_date, String user_id, String status, String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_debitcredit, created_by, created_date,debitcredit_type, status,factory_id ) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :debitcredit_type , :status,:factory_id)", nativeQuery = true)
	int insertDebitCredit(String state_id, String invoice_series, String start_date, String end_date, String user_id,String debitcredit_type, String status, String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_challan , created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertChallan(String state_id, String invoice_series, String start_date, String end_date, String user_id, String status, String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_paymentvoucher, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status,:factory_id)", nativeQuery = true)
	int insertPaymentVoucher(String state_id, String invoice_series, String start_date, String end_date, String user_id, String status, String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_selfinvoice, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertSelfInvoice(String state_id, String invoice_series, String start_date, String end_date, String user_id, String status, String factory_id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SERIES_MASTER (state_id,  invoice_series, start_date, end_date, is_deliverychallan, created_by, created_date, status,factory_id) VALUES (:state_id,  :invoice_series, :start_date, :end_date, 1,:user_id, GETDATE(), :status, :factory_id)", nativeQuery = true)
	int insertDeliveryChallan(String state_id, String invoice_series, String start_date, String end_date, String user_id,  String status, String factory_id);
	
	// UPDATE QUERY
	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_gst = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateGST(String state_id, String invoice_series, String start_date, String end_date, String user_id, String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_advance = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateAdvance(String state_id, String invoice_series, String start_date, String end_date, String user_id, String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_debitcredit = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateDebitCredit(String state_id, String invoice_series, String start_date, String end_date, String user_id, String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_challan = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateChallan(String state_id, String invoice_series, String start_date, String end_date, String user_id, String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_paymentvoucher = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updatePaymentVoucher(String state_id, String invoice_series, String start_date, String end_date, String user_id, String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_selfinvoice = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateSelfInvoice(String state_id, String invoice_series, String start_date, String end_date, String user_id, String series_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET state_id = :state_id, invoice_series = :invoice_series, start_date = :start_date, end_date = :end_date, is_deliverychallan = 1, modified_by =:user_id, modified_date = GETDATE() WHERE series_id = :series_id", nativeQuery = true)
	int updateDeliveryChallan(String state_id, String invoice_series, String start_date, String end_date, String user_id, String series_id);
	
	// DELETE

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() " +
	               "WHERE series_no = :series_no AND is_gst = 1", nativeQuery = true)
	int deleteGSTSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() " +
	               "WHERE series_no = :series_no AND is_advance = 1", nativeQuery = true)
	int deleteAdvanceSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() " +
	               "WHERE series_no = :series_no AND is_debitCredit = 1", nativeQuery = true)
	int deleteDebitCreditSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() " +
	               "WHERE series_no = :series_no AND is_challan = 1", nativeQuery = true)
	int deleteChallanSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() " +
	               "WHERE series_no = :series_no AND is_paymentVoucher = 1", nativeQuery = true)
	int deletePaymentVoucherSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() " +
	               "WHERE series_no = :series_no AND is_selfInvoice = 1", nativeQuery = true)
	int deleteSelfInvoiceSeries(String series_no, String user_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SERIES_MASTER SET is_delete = 1, modified_by = :user_id, modified_date = GETDATE() " +
	               "WHERE series_no = :series_no AND is_deliveryChallan = 1", nativeQuery = true)
	int deleteDeliveryChallanSeries(String series_no, String user_id);



	// SEARCH
	// Search for GST Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_gst = 1", nativeQuery = true)
	GSTSeriesMasterInterfaces searchGSTSeriesById(String series_id);

	// Search for Advance Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_advance = 1", nativeQuery = true)
	AdvanceSeriesMasterInterfaces searchAdvanceSeriesById(String series_id);

	// Search for Debit Credit Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_debitcredit = 1", nativeQuery = true)
	DebitCreditSeriesMasterInterfaces searchDebitCreditSeriesById(String series_id);

	// Search for Challan Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_challan = 1", nativeQuery = true)
	ChallanSeriesMasterInterfaces searchChallanSeriesById(String series_id);

	// Search for Payment Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_paymentvoucher = 1", nativeQuery = true)
	PaymentSeriesMasterInterfaces searchPaymentSeriesById(String series_id);

	// Search for Self Invoice Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_selfinvoice = 1", nativeQuery = true)
	SelfInvoiceSeriesMasterInterfaces searchSelfInvoiceSeriesById(String series_id);

	// Search for Delivery Challan Series Master by ID
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE series_id = :series_id AND is_deliverychallan = 1", nativeQuery = true)
	DeliveryChallanSeriesMasterInterfaces searchDeliveryChallanSeriesById(String series_id);

	// List all GST Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_gst = 1", nativeQuery = true)
	List<GSTSeriesMasterInterfaces> listAllGSTSeries();

	// List all Advance Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_advance = 1", nativeQuery = true)
	List<AdvanceSeriesMasterInterfaces> listAllAdvanceSeries();

	// List all Debit Credit Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_debitcredit = 1", nativeQuery = true)
	List<DebitCreditSeriesMasterInterfaces> listAllDebitCreditSeries();

	// List all Challan Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_challan = 1", nativeQuery = true)
	List<ChallanSeriesMasterInterfaces> listAllChallanSeries();

	// List all Payment Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_paymentvoucher = 1", nativeQuery = true)
	List<PaymentSeriesMasterInterfaces> listAllPaymentSeries();

	// List all Self Invoice Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_selfinvoice = 1", nativeQuery = true)
	List<SelfInvoiceSeriesMasterInterfaces> listAllSelfInvoiceSeries();

	// List all Delivery Challan Series Masters
	@Query(value = "SELECT * FROM SERIES_MASTER WHERE is_deliverychallan = 1", nativeQuery = true)
	List<DeliveryChallanSeriesMasterInterfaces> listAllDeliveryChallanSeries();



	/*
	 * @Query(value
	 * ="Select * from SERIES_MASTER where is_gst = 1 and is_advance =1 and is_debitcredit = 1"
	 * +
	 * " and is_challan=1 and is_paymentVoucher = 1 and is_selfinvoice = 1 and is_deliveryChallan = 1 and is_delete = 0"
	 * , nativeQuery = true) List<SeriesMasterList> valList();
	 */
	
	@Query(value ="select sm.*, concat(ssm.state_code, '- ' , ssm.state_name,'(',ssm.state_id,')') as state_name  from SERIES_MASTER sm inner join STATE_MASTER ssm on ssm.id = sm.state_id where sm.is_delete = 0 and sm.factory_id = :factory_id", nativeQuery = true)
	List<SeriesMasterList> valList(String factory_id);

//	@Query(value ="SELECT [bu_id],s.id as state_id, (s.[state_name] +'( '+ u.state_code +' )' +' - '+ [state_gstno]) as code FROM [GST_MASTER] as u inner join STATE_MASTER as s on s.state_code = u.state_code", nativeQuery = true)
//	List<SeriesMasterList> typeList();
	
	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_gst = 1)", nativeQuery = true)
	List<SeriesMasterList> typeList();
	
	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_gst = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListGST(String factory_id);
	@Query(value ="SELECT * from STATE_MASTER", nativeQuery = true)
	List<SeriesMasterList> typeSateList();

	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_advance = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListADVANCE(String factory_id);

	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_debitCredit = 1  and status = 'Open' and is_delete = 0 and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListDEBITCREDIT(String factory_id);

	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_challan = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListCHALLAN(String factory_id);

	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_paymentVoucher = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListPAYMENT(String factory_id);

	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_selfInvoice = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListSELFINVOICE(String factory_id);

	@Query(value ="select id, concat(state_code, '- ' , state_name,'(',state_id,')') as state_name from STATE_MASTER where is_delete = 0  and id not in (select state_id from SERIES_MASTER where is_deliveryChallan = 1  and status = 'Open' and is_delete = 0  and factory_id = :factory_id)", nativeQuery = true)
	List<SeriesMasterList> typeListDELIVERYCHALAN(String factory_id);

	@Query(value="select count(*) from SERIES_MASTER where state_id = :state_id   and status = 'Closed'", nativeQuery = true)
	int checkStatusClosed(String state_id);
}
