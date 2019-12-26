package com.customer.orderproupdated.bean;

        import java.io.Serializable;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class RegistrationResponse implements Serializable
{


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    private final static long serialVersionUID = -222412719715258333L;

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


}

