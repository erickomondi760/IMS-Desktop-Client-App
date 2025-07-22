package com.quickrest.entities;

import java.io.Serializable;
import java.math.BigInteger;

public class QuickUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;
    private String userName;
    private String password;

    public QuickUsers() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof QuickUsers)
            return true;

        QuickUsers users = (QuickUsers) obj;

        if((this.id == null && users.id != null) ||(this.id != null && !this.id.equals(users)))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 234;
        return hash += this.id != null? this.id.hashCode():0;
    }
}
