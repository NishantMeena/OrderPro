package com.customer.orderproupdated.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Advantal on 7/13/2017.
 */

public class StateListResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("state")
    @Expose
    private List<StateDetails> state = null;
    private final static long serialVersionUID = -6583242049554611104L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StateDetails> getState() {
        return state;
    }

    public void setState(List<StateDetails> state) {
        this.state = state;
    }
}
