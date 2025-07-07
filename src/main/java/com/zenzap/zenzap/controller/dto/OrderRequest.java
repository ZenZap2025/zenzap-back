package com.zenzap.zenzap.controller.dto;

import java.util.List;

public class OrderRequest {
    private String clientName;
    private String email;
    private String address;
    private List<PurchaseRequest> products;

    // Getters y setters

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PurchaseRequest> getProducts() {
        return products;
    }

    public void setProducts(List<PurchaseRequest> products) {
        this.products = products;
    }
}
