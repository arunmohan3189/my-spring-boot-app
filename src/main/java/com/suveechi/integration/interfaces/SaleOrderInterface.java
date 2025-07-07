package com.suveechi.integration.interfaces;

public interface SaleOrderInterface {

	String getSoe_id();

	String getSale_order_code();

	String getSale_order_type_id();

	String getLocation_type_id();

	String getSale_order_to_id();

	String getAuction_date();

	String getSale_order_duration();

	String getAdvance_payment();

	String getBilling_address_id();

	String getShipping_address_id();

	String getBusiness_unit_id();

	String getTax1();

	String getTax1_value();

	String getTax2();

	String getTax2_value();

	String getTax3();

	String getTax3_value();

	String getNet_amount();

	String getTotal_tax();

	String getGrand_total();

	String getCreated_by();

	String getCreated_date();

	String getModified_by();

	String getModified_date();

	String getIs_delete();

	String getFactory_id();

}
