package com.customer.orderproupdated.bean;
        import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MerchantDetail  implements Serializable
{
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("contactno")
    @Expose
    private String contactno;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("registration_date")
    @Expose
    private String registrationDate;
    @SerializedName("request_status")
    @Expose
    private String requestStatus;
    private final static long serialVersionUID = -4740982656114009214L;

    private Boolean is_merchant_sleceted=false;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Boolean getIs_merchant_sleceted() {
        return is_merchant_sleceted;
    }

    public void setIs_merchant_sleceted(Boolean is_merchant_sleceted) {
        this.is_merchant_sleceted = is_merchant_sleceted;
    }

}


