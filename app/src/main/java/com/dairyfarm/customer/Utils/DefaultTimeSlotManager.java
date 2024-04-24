package com.dairyfarm.customer.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DefaultTimeSlotManager {
    private static final String DEFAULT_TIME_SLOT_PREF = "dairy_farm_default_timeslot_pref";
    private static final String KEY_IS_TIME_SLOT_TIME_ID = "DEFAULT_TIME_SLOT_ID";
    private final SharedPreferences.Editor editor;
    Context mContext;
    SharedPreferences preferences;

    public DefaultTimeSlotManager(Context mContext) {
        this.mContext = mContext;
        preferences = mContext.getSharedPreferences(DEFAULT_TIME_SLOT_PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setTimeSlotDefaultID(String DefaultTimeSlotID) {
        editor.putString(KEY_IS_TIME_SLOT_TIME_ID, DefaultTimeSlotID).apply();
    }

    public String getTimeSlotDefaultID() {
        return preferences.getString(KEY_IS_TIME_SLOT_TIME_ID, "");
    }

    public void RemoveDefaultTimeSlotID() {
        editor.remove(KEY_IS_TIME_SLOT_TIME_ID).commit();
    }
}