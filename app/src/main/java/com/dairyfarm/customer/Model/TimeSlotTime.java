package com.dairyfarm.customer.Model;

import java.io.Serializable;

public class TimeSlotTime implements Serializable {

    String TimeSlotTimeID, TimeSlot, isDefault;

    public TimeSlotTime(String timeSlotTimeID, String timeSlot, String isDefault) {
        TimeSlotTimeID = timeSlotTimeID;
        TimeSlot = timeSlot;
        this.isDefault = isDefault;
    }

    public TimeSlotTime() {
    }

    public String getTimeSlotTimeID() {
        return TimeSlotTimeID;
    }

    public void setTimeSlotTimeID(String timeSlotTimeID) {
        TimeSlotTimeID = timeSlotTimeID;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}