package com.dairyfarm.customer.Model;

public class SelectOfferCoupon {
    String CouponID, CouponCode, CouponTitle, CouponDescription, CouponMinimumAmount, CouponDiscount, CouponType, CouponUsagePersPerson, CouponEndDate, CouponMaximimAmount;

    public SelectOfferCoupon(String couponID, String couponCode, String couponTitle, String couponDescription, String couponMinimumAmount, String couponDiscount, String couponType, String couponUsagePersPerson, String couponEndDate, String couponMaximimAmount) {
        CouponID = couponID;
        CouponCode = couponCode;
        CouponTitle = couponTitle;
        CouponDescription = couponDescription;
        CouponMinimumAmount = couponMinimumAmount;
        CouponDiscount = couponDiscount;
        CouponType = couponType;
        CouponUsagePersPerson = couponUsagePersPerson;
        CouponEndDate = couponEndDate;
        CouponMaximimAmount = couponMaximimAmount;
    }

    public SelectOfferCoupon() {
    }

    public String getCouponID() {
        return CouponID;
    }

    public void setCouponID(String couponID) {
        CouponID = couponID;
    }

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public String getCouponTitle() {
        return CouponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        CouponTitle = couponTitle;
    }

    public String getCouponDescription() {
        return CouponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        CouponDescription = couponDescription;
    }

    public String getCouponMinimumAmount() {
        return CouponMinimumAmount;
    }

    public void setCouponMinimumAmount(String couponMinimumAmount) {
        CouponMinimumAmount = couponMinimumAmount;
    }

    public String getCouponDiscount() {
        return CouponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        CouponDiscount = couponDiscount;
    }

    public String getCouponType() {
        return CouponType;
    }

    public void setCouponType(String couponType) {
        CouponType = couponType;
    }

    public String getCouponUsagePersPerson() {
        return CouponUsagePersPerson;
    }

    public void setCouponUsagePersPerson(String couponUsagePersPerson) {
        CouponUsagePersPerson = couponUsagePersPerson;
    }

    public String getCouponEndDate() {
        return CouponEndDate;
    }

    public void setCouponEndDate(String couponEndDate) {
        CouponEndDate = couponEndDate;
    }

    public String getCouponMaximimAmount() {
        return CouponMaximimAmount;
    }

    public void setCouponMaximimAmount(String couponMaximimAmount) {
        CouponMaximimAmount = couponMaximimAmount;
    }
}
