package com.suveechi.integration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ADVANCEPACKINGNOTE")
public class AdvancePackingNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pnId; // Assuming the primary key column name is "pn_id"

    private String conId; // Assuming the column name is "con_id"
    private String loadId; // Assuming the column name is "load_id"
    private String createdBy; // Assuming the column name is "created_by"
    private LocalDateTime createdDate; // Assuming the column name is "created_date"

    private int factory_id;
    // Getters and Setters
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

	public int getFactory_id() {
		return factory_id;
	}

	public void setFactory_id(int factory_id) {
		this.factory_id = factory_id;
	}
}

