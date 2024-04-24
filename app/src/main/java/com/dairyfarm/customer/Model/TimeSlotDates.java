package com.dairyfarm.customer.Model;

import java.util.ArrayList;

public class TimeSlotDates {
    String TimeSlotDateID, Date, DayName, MonthName, MonthNo, Year, isDefault;

    ArrayList<TimeSlotTime> listTimeSlotTimes;

    public TimeSlotDates(String timeSlotDateID, String date, String dayName, String monthName, String monthNo, String year, String isDefault, ArrayList<TimeSlotTime> listTimeSlotTimes) {
        TimeSlotDateID = timeSlotDateID;
        Date = date;
        DayName = dayName;
        MonthName = monthName;
        MonthNo = monthNo;
        Year = year;
        this.isDefault = isDefault;
        this.listTimeSlotTimes = listTimeSlotTimes;
    }

    public TimeSlotDates() {

    }

    public String getTimeSlotDateID() {
        return TimeSlotDateID;
    }

    public void setTimeSlotDateID(String timeSlotDateID) {
        TimeSlotDateID = timeSlotDateID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDayName() {
        return DayName;
    }

    public void setDayName(String dayName) {
        DayName = dayName;
    }

    public String getMonthName() {
        return MonthName;
    }

    public void setMonthName(String monthName) {
        MonthName = monthName;
    }

    public String getMonthNo() {
        return MonthNo;
    }

    public void setMonthNo(String monthNo) {
        MonthNo = monthNo;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public ArrayList<TimeSlotTime> getListTimeSlotTimes() {
        return listTimeSlotTimes;
    }

    public void setListTimeSlotTimes(ArrayList<TimeSlotTime> listTimeSlotTimes) {
        this.listTimeSlotTimes = listTimeSlotTimes;
    }
}

