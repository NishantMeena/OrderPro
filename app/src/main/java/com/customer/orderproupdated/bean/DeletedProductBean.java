package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Advantal on 6/6/2017.
 */

public class DeletedProductBean {

    @SerializedName("deleted_pid")
    @Expose
    private String deletedPid;
    private final static long serialVersionUID = -4273545140489037702L;

    public String getDeletedPid() {
        return deletedPid;
    }

    public void setDeletedPid(String deletedPid) {
        this.deletedPid = deletedPid;
    }
}
