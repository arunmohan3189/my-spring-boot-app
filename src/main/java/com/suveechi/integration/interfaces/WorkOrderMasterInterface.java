package com.suveechi.integration.interfaces;

public interface WorkOrderMasterInterface {
	String getWork_id();
	String getWorkorder_no();
	String getFactory_id();

}
/*
work_id INT,
code INT,
workorder_no VARCHAR(MAX),
docket_no VARCHAR(MAX),
wo_date DATETIME,
dock_date DATETIME,
*/