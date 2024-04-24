package com.dairyfarm.customer.APIHelper;

import android.content.Context;

import com.dairyfarm.customer.Utils.AtClass;

import java.util.HashMap;

public class CommonRequestParams {

    AtClass atClass;

    public CommonRequestParams() {
        atClass = new AtClass();
    }

    public HashMap<String, String> getCommonRequestParams(Context context) {
        HashMap<String, String> params = new HashMap<>();
        params.put(JsonFields.COMMON_REQUEST_PARAM_DEVICE_TYPE, atClass.getDeviceType());
        params.put(JsonFields.COMMON_REQUEST_PARAM_DEVICE_ID, atClass.getDeviceID(context));
        params.put(JsonFields.COMMON_REQUEST_PARAM_DEVICE_TOKEN, atClass.getRefreshedToken());
        params.put(JsonFields.COMMON_REQUEST_PARAM_DEVICE_OS_DETAILS, atClass.getOsInfo(context));
        params.put(JsonFields.COMMON_REQUEST_PARAM_DEVICE_IP_ADDRESS, atClass.getRefreshedIpAddress(context));
        params.put(JsonFields.COMMON_REQUEST_PARAM_DEVICE_MAC_ADDRESS, atClass.getRefreshedMacAddress(context));
        params.put(JsonFields.COMMON_REQUEST_PARAM_DEVICE_MODEL_DETAILS, atClass.getDeviceManufacturerModel());
        params.put(JsonFields.COMMON_REQUEST_PARAM_APP_VERSION_DETAILS, atClass.getAppVersionNumberAndVersionCode());
        return params;
    }

}
