package com.quickrest.entities;

import java.io.Serializable;
import java.math.BigInteger;

public class Groups implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigInteger id;
    private String groupName;
    private String userName;

    public Groups() {

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        return hash += id != null? this.id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Groups)
            return true;

        Groups groups = (Groups) obj;

        if((this.id == null && groups.id != null) || (this.id != null && !groups.id.equals(this.id)))
            return false;

        return true;
    }
}
