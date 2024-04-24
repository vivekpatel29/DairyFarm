package com.dairyfarm.customer.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ProductsLayoutManager {
    private static final String PRODUCTS_LAYOUT_PREF = "dairy_farm_products_layout_pref";
    private final SharedPreferences.Editor editor;
    Context mContext;
    SharedPreferences preferences;
    public static final String PRODUCT_LAYOUT = "product_layout";

    public ProductsLayoutManager(Context mContext) {
        this.mContext = mContext;
        preferences = mContext.getSharedPreferences(PRODUCTS_LAYOUT_PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setProductLayout(String productLayout) {
        editor.putString(PRODUCT_LAYOUT, productLayout);
        editor.apply();
    }

    public String getProductLayout() {
        return preferences.getString(PRODUCT_LAYOUT, "");
    }

    public void RemoveProductLayout() {
        editor.remove(PRODUCT_LAYOUT);
    }
}
