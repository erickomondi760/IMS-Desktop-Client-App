package com.quickrest.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ClientTrader implements Serializable {
    private String identity;
    private String terms;
    private String address;
    private String phoneNumber1;
    private String phoneNumber2;
    private String email1;
    private String email2;
    private String userName;
    private Date dateRegistered;
    private String service;
    private long id;
    private List<ClientTraderOutlet> clientTraderOutlets;

    public ClientTrader() {
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<ClientTraderOutlet> getClientTraderOutlets() {
        return clientTraderOutlets;
    }

    public void setClientTraderOutlets(List<ClientTraderOutlet> clientTraderOutlets) {
        this.clientTraderOutlets = clientTraderOutlets;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
