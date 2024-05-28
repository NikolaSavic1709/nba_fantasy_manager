package com.ftn.sbnz.model.models.unused;

public class Customer {
    private long id;
    private double accState;

    public Customer(long id, double accState) {
        this.id = id;
        this.accState = accState;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAccState() {
        return accState;
    }

    public void setAccState(double accState) {
        this.accState = accState;
    }
}
