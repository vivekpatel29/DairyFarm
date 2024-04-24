package com.dairyfarm.customer.Model;

public class SelectAddress {
    String AddressID, AddressName, Address, IsDefault, MobileNumber, isAvailable, AvailableString, AddressType, AddressLatitude, AddressLogitude;

    public SelectAddress(String addressID, String addressName, String address, String isDefault, String mobileNumber, String isAvailable, String availableString, String addressType, String addressLatitude, String addressLogitude) {
        AddressID = addressID;
        AddressName = addressName;
        Address = address;
        IsDefault = isDefault;
        MobileNumber = mobileNumber;
        this.isAvailable = isAvailable;
        AvailableString = availableString;
        AddressType = addressType;
        AddressLatitude = addressLatitude;
        AddressLogitude = addressLogitude;
    }

    public SelectAddress() {
    }

    public String getAddressID() {
        return AddressID;
    }

    public void setAddressID(String addressID) {
        AddressID = addressID;
    }

    public String getAddressName() {
        return AddressName;
    }

    public void setAddressName(String addressName) {
        AddressName = addressName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(String isDefault) {
        IsDefault = isDefault;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getAvailableString() {
        return AvailableString;
    }

    public void setAvailableString(String availableString) {
        AvailableString = availableString;
    }

    public String getAddressType() {
        return AddressType;
    }

    public void setAddressType(String addressType) {
        AddressType = addressType;
    }

    public String getAddressLatitude() {
        return AddressLatitude;
    }

    public void setAddressLatitude(String addressLatitude) {
        AddressLatitude = addressLatitude;
    }

    public String getAddressLogitude() {
        return AddressLogitude;
    }

    public void setAddressLogitude(String addressLogitude) {
        AddressLogitude = addressLogitude;
    }
}

