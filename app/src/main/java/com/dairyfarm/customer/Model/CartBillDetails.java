package com.dairyfarm.customer.Model;

public class CartBillDetails {
    String Title, Value;

    public CartBillDetails(String title, String value) {
        Title = title;
        Value = value;
    }

    public CartBillDetails() {

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
