package com.suveechi.integration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="QSCHALLAN_PACKINGNOTE_MASTER")
public class QSChallanPacking {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pn_id")
    private int pnId; // Assuming the primary key column name is "pn_id"
    @Column(name = "con_id")
    private String conId; // Assuming the column name is "con_id"
    @Column(name = "load_id")
    private String loadId; // Assuming the column name is "load_id"
    @Column(name = "lot_no")
    private String lotNo;
	@Column(name = "created_by")
    private String createdBy; // Assuming the column name is "created_by"
    @Column(name = "filepath")
    private String filepath;
    @Column(name = "freight")
    private String freight;
    @Column(name = "milestone_id")
    private int milestone_id;
    @Column(name = "taxexemstatus")
    private String taxexemstatus;
    @Column(name = "taxexemamount")
    private String taxexemamount;
    @Column(name = "created_date")
    private LocalDateTime created_date;
    public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public int getPnId() {
		return pnId;
	}
	public void setPnId(int pnId) {
		this.pnId = pnId;
	}
	public String getConId() {
		return conId;
	}
	public void setConId(String conId) {
		this.conId = conId;
	}
	public String getLoadId() {
		return loadId;
	}
	public void setLoadId(String loadId) {
		this.loadId = loadId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public int getMilestone_id() {
		return milestone_id;
	}
	public void setMilestone_id(int milestone_id) {
		this.milestone_id = milestone_id;
	}
	public String getTaxexemstatus() {
		return taxexemstatus;
	}
	public void setTaxexemstatus(String taxexemstatus) {
		this.taxexemstatus = taxexemstatus;
	}
	public String getTaxexemamount() {
		return taxexemamount;
	}
	public void setTaxexemamount(String taxexemamount) {
		this.taxexemamount = taxexemamount;
	}
	public LocalDateTime getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}
    
    
}
