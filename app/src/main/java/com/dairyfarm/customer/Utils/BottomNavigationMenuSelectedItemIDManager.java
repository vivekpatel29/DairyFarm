package com.dairyfarm.customer.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class BottomNavigationMenuSelectedItemIDManager {
    private static final String WEB_SERVICES_SECRET_PREF = "dairy_farm__bottom_navigation_menu_selected_item_id_pef";
    private final SharedPreferences.Editor editor;
    Context mContext;
    SharedPreferences preferences;
    public static final String BOTTOM_NAVIGATION_MENU_SELECTED_ITEM_ID = "bottom_navigation_menu_selected_item_id";
    AtClass atClass;

    public BottomNavigationMenuSelectedItemIDManager(Context mContext) {
        this.mContext = mContext;
        atClass = new AtClass();
        preferences = mContext.getSharedPreferences(WEB_SERVICES_SECRET_PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setBottomNavigationMenuSelectedItemID(String bottomNavigationMenuSelectedItemID) {
        editor.putString(BOTTOM_NAVIGATION_MENU_SELECTED_ITEM_ID, bottomNavigationMenuSelectedItemID);
        editor.apply();
    }

    public String getBottomNavigationMenuSelectedItemID() {
        return preferences.getString(BOTTOM_NAVIGATION_MENU_SELECTED_ITEM_ID, "");
    }

    public void RemoveSession() {
        editor.remove(BOTTOM_NAVIGATION_MENU_SELECTED_ITEM_ID);
        editor.commit();
    }
}