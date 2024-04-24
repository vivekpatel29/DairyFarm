package com.dairyfarm.customer.Model;

public class PaymentMethods {

    String PaymentMethodID, PaymentMethodName, PaymentMethodImage, PaymentMethodMessage, isDefault, CanProceed;

    public PaymentMethods(String paymentMethodID, String paymentMethodName, String paymentMethodImage, String paymentMethodMessage, String isDefault, String canProceed) {
        PaymentMethodID = paymentMethodID;
        PaymentMethodName = paymentMethodName;
        PaymentMethodImage = paymentMethodImage;
        PaymentMethodMessage = paymentMethodMessage;
        this.isDefault = isDefault;
        CanProceed = canProceed;
    }

    public PaymentMethods() {
    }

    public String getPaymentMethodID() {
        return PaymentMethodID;
    }

    public void setPaymentMethodID(String paymentMethodID) {
        PaymentMethodID = paymentMethodID;
    }

    public String getPaymentMethodName() {
        return PaymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        PaymentMethodName = paymentMethodName;
    }

    public String getPaymentMethodImage() {
        return PaymentMethodImage;
    }

    public void setPaymentMethodImage(String paymentMethodImage) {
        PaymentMethodImage = paymentMethodImage;
    }

    public String getPaymentMethodMessage() {
        return PaymentMethodMessage;
    }

    public void setPaymentMethodMessage(String paymentMethodMessage) {
        PaymentMethodMessage = paymentMethodMessage;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getCanProceed() {
        return CanProceed;
    }

    public void setCanProceed(String canProceed) {
        CanProceed = canProceed;
    }
}

