package com.dairyfarm.customer.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dairyfarm.customer.APIHelper.CommonRequestParams;
import com.dairyfarm.customer.APIHelper.JsonFields;
import com.dairyfarm.customer.APIHelper.WebAuthorization;
import com.dairyfarm.customer.APIHelper.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CustomerSessionManager {
    private static final String CUSTOMER_PREF = "dairy_farm_customer_pref";
    private static final String KEY_IS_LOGIN = "IS_LOGIN";
    private final SharedPreferences.Editor editor;
    Context mContext;
    SharedPreferences preferences;
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_EMAIL = "customer_email";
    public static final String CUSTOMER_MOBILE = "customer_mobile";
    public static final String CUSTOMER_IMAGE_URL = "customer_image_url";


    public static final String CUSTOMER_PICTURE_URI = "customer_picture_uri";

    AtClass atClass;

    public CustomerSessionManager(Context mContext) {
        this.mContext = mContext;
        atClass = new AtClass();
        preferences = mContext.getSharedPreferences(CUSTOMER_PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLoginStatus() {
        editor.putBoolean(KEY_IS_LOGIN, true).commit();
    }

    public boolean getLoginStatus() {
        return preferences.getBoolean(KEY_IS_LOGIN, false);
    }

    public void logout() {
        LogoutDealer();
    }

    public void setCustomerDetails(String customer_id, String customer_name, String customer_email, String customer_mobile, String customer_image_url) {
        editor.putString(CUSTOMER_ID, customer_id);
        editor.putString(CUSTOMER_NAME, customer_name);
        editor.putString(CUSTOMER_EMAIL, customer_email);
        editor.putString(CUSTOMER_MOBILE, customer_mobile);
        editor.putString(CUSTOMER_IMAGE_URL, customer_image_url);
        editor.apply();
    }

    public void setPhotoURI(String PhotoURI) {
        editor.putString(CUSTOMER_PICTURE_URI, PhotoURI);
        editor.apply();
    }

    public String getPhotoURI() {
        return preferences.getString(CUSTOMER_PICTURE_URI, "");
    }

    public String getCustomerID() {
        return preferences.getString(CUSTOMER_ID, "");
    }

    public String getCustomerName() {
        return preferences.getString(CUSTOMER_NAME, "");
    }

    public String getCustomerEmail() {
        return preferences.getString(CUSTOMER_EMAIL, "");
    }

    public String getCustomerMobile() {
        return preferences.getString(CUSTOMER_MOBILE, "");
    }

    public String getCustomerImageURL() {
        return preferences.getString(CUSTOMER_IMAGE_URL, "");
    }

    public void setCustomerImageURL(String imageURL) {
        editor.putString(CUSTOMER_IMAGE_URL, imageURL);
        editor.apply();
    }

    private void LogoutDealer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.LOGOUT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseLogoutJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    RemoveSession();
                } else if (error instanceof NoConnectionError) {
                    RemoveSession();
                } else if (error instanceof ServerError) {
                    RemoveSession();
                } else if (error instanceof TimeoutError) {
                    RemoveSession();
                } else {
                    RemoveSession();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAuthorization.checkAuthentication();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                CommonRequestParams commonRequestParams = new CommonRequestParams();
                Map<String, String> params = commonRequestParams.getCommonRequestParams(mContext);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void parseLogoutJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                RemoveSession();
            } else {
                RemoveSession();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void RemoveSession() {
        editor.remove(CUSTOMER_ID);
        editor.remove(CUSTOMER_NAME);
        editor.remove(CUSTOMER_EMAIL);
        editor.remove(CUSTOMER_MOBILE);
        editor.remove(CUSTOMER_IMAGE_URL);
        editor.remove(KEY_IS_LOGIN);
        editor.commit();
    }
}
