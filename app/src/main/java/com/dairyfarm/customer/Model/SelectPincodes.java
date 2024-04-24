package com.dairyfarm.customer.Model;

public class SelectPincodes {

    String PincodeID, PincodeNumber, PincodeTitle, Area, City, State;

    public SelectPincodes(String pincodeID, String pincodeNumber, String pincodeTitle, String area, String city, String state) {
        PincodeID = pincodeID;
        PincodeNumber = pincodeNumber;
        PincodeTitle = pincodeTitle;
        Area = area;
        City = city;
        State = state;
    }

    public SelectPincodes() {

    }

    public String getPincodeID() {
        return PincodeID;
    }

    public void setPincodeID(String pincodeID) {
        PincodeID = pincodeID;
    }

    public String getPincodeNumber() {
        return PincodeNumber;
    }

    public void setPincodeNumber(String pincodeNumber) {
        PincodeNumber = pincodeNumber;
    }

    public String getPincodeTitle() {
        return PincodeTitle;
    }

    public void setPincodeTitle(String pincodeTitle) {
        PincodeTitle = pincodeTitle;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}


