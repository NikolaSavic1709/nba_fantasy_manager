package com.ftn.sbnz.model.events.unused;

import java.io.Serializable;
import java.util.Date;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Timestamp("executionTime")
@Expires("2h30m")
public class TransactionEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private Date executionTime;
    private Long customerId;
    private Double totalAmount;
    private boolean duplicate;

    public TransactionEvent() {
        super();
    }
    
    public TransactionEvent(Long customerId, Double totalAmount) {
        super();
        this.executionTime = new Date();
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.duplicate=false;
    }


    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}

