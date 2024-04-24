package com.dairyfarm.customer.Utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dairyfarm.customer.APIHelper.JsonFields;
import com.dairyfarm.customer.APIHelper.WebAuthorization;
import com.dairyfarm.customer.APIHelper.WebURL;
import com.dairyfarm.customer.BuildConfig;
import com.dairyfarm.customer.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class AtClass extends Application {
    String IPAddress, MacAddress;
    private static AtClass mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        RefreshIPAddress(mInstance);
        RefreshMacAddress(mInstance);
        GetDeviceOS(mInstance);

//        FirebaseApp.initializeApp(this);
//        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//
//        // set in-app defaults
//        Map<String, Object> remoteConfigDefaults = new HashMap();
//        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
//        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, String.valueOf(BuildConfig.VERSION_NAME));
//        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
//                "https://play.google.com/store/apps/details?id=" + mInstance.getPackageManager());
//
//        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults);
//
//        firebaseRemoteConfig.fetch(10) // fetch every 10 seconds
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("TAG", "remote config is fetched.");
//                            firebaseRemoteConfig.fetchAndActivate();
//                        }
//                    }
//                });
    }

    private void RefreshMacAddress(Context context) {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    MacAddress = "00:00:00:00:00:00";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

                MacAddress = res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
            MacAddress = "00:00:00:00:00:00";
        }


        setRefreshedMacAddress(context, MacAddress);
    }

    public void setRefreshedMacAddress(Context context, String macAddress) {
        Log.d("MacAddress", "val" + macAddress);
        SharedPreferences prefs = context.getSharedPreferences("MAC_ADDRESS_PREF", Context.MODE_PRIVATE);
        prefs.edit().putString("MAC_ADDRESS", macAddress).apply();
    }


    public String getRefreshedMacAddress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MAC_ADDRESS_PREF", Context.MODE_PRIVATE);
        String refreshedMacAddress = prefs.getString("MAC_ADDRESS", "00:00:00:00:00:00");
        return refreshedMacAddress;
    }


    private void GetDeviceOS(Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ANDROID_OS_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(context, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRefreshedOSName(context, "");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAuthorization.checkAuthentication();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(JsonFields.GET_DEVICE_OS_NAME_FROM_SDK_REQUEST_PARAMS_SDK_NUMBER, getAndroidSdkVersionNumber());
                Log.d("params", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void parseJSON(Context context, String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            if (flag == 1) {
                String OSName = jsonObject.optString(JsonFields.GET_DEVICE_OS_NAME_FROM_SDK_RESPONSE_OS_NAME_FROM_SDK_NUMBER);
                if (OSName != null && !OSName.isEmpty() && !OSName.equals("")) {
                    setRefreshedOSName(context, OSName);
                } else {
                    setRefreshedOSName(context, "");
                }
            } else {
                setRefreshedOSName(context, "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setRefreshedOSName(Context context, String deviceOSName) {
        SharedPreferences prefs = context.getSharedPreferences("DEVICE_OS_NAME_PREF", Context.MODE_PRIVATE);
        prefs.edit().putString("DEVICE_OS_NAME", deviceOSName).apply();
    }


    public String getRefreshedOSName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("DEVICE_OS_NAME_PREF", Context.MODE_PRIVATE);
        String refreshedDeviceOSName = prefs.getString("DEVICE_OS_NAME", "");
        return refreshedDeviceOSName;
    }

    public String getRefreshedToken() {
//        String refreshedDeviceToken = FirebaseInstanceId.getInstance().getToken();
//        return refreshedDeviceToken;

        String refreshedDeviceToken = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        return refreshedDeviceToken;
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public String RefreshIPAddress(final Context context) {
        String stringUrl = "https://ipinfo.io/ip";
        //String stringUrl = "http://whatismyip.akamai.com/";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        //String url ="http://www.google.com";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stringUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e("IP", "GET IP : " + response);
                        IPAddress = response;
                        setRefreshedIpAddress(context, IPAddress);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                IPAddress = "0.0.0.0";
                setRefreshedIpAddress(context, IPAddress);

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return IPAddress;
    }

    public void setRefreshedIpAddress(Context context, String ipAddress) {
        SharedPreferences prefs = context.getSharedPreferences("IP_ADDRESS_PREF", Context.MODE_PRIVATE);
        prefs.edit().putString("IP_ADDRESS", ipAddress).apply();
    }


    public String getRefreshedIpAddress(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("IP_ADDRESS_PREF", Context.MODE_PRIVATE);
        String refreshedIpAddress = prefs.getString("IP_ADDRESS", "0.0.0.0");
        return refreshedIpAddress;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        Log.d("", "Out of memory");
        super.onLowMemory();
    }

    public static synchronized AtClass getInstance() {
        return mInstance;
    }

    private String getAndroidSdkVersionNumber() {
        int sdkVersion = Build.VERSION.SDK_INT;
        return String.valueOf(sdkVersion);
    }

    public String getAppVersionName() {
        String versionRelease = BuildConfig.VERSION_NAME;
        return String.valueOf(versionRelease);
    }

    public String getAppName(Context context) {
        String AppName = context.getString(R.string.app_name);
        return AppName;
    }

    private String getAppVersionNumber() {
        String versionCodeRelease = String.valueOf(BuildConfig.VERSION_CODE);
        return String.valueOf(versionCodeRelease);
    }


    public String getAppVersionNumberAndVersionCode() {
        String versionNameRelease = String.valueOf(BuildConfig.VERSION_NAME);
        String versionCodeRelease = String.valueOf(BuildConfig.VERSION_CODE);
        return versionNameRelease + "|" + versionCodeRelease;
    }

    public String getAndroidVersionName(Context context) {
        int sdkVersion = Build.VERSION.SDK_INT;
        String release = Build.VERSION.RELEASE;

        String OsName = getRefreshedOSName(context);

        if (OsName != null && !OsName.isEmpty() && !OsName.equals("")) {
        } else {
            OsName = "Unknown Os";
        }
        return OsName + "(" + release + ")";
    }

    private String getDeviceManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }

    private String getDeviceModel() {
        String model = Build.MODEL;
        return model;
    }

    public String getDeviceManufacturerModel() {
        String manufacturer = getDeviceManufacturer();
        String model = getDeviceModel();
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + "|" + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getOrderFrom() {
        return "2";
    }

    public String getDeviceType() {
        return "Android";
    }

    private String getDeviceDetailsInfo() {
        String DeviceDetailsInfo = getDeviceManufacturerModel();
        return DeviceDetailsInfo;
    }

    public String getOsInfo(Context context) {
        String AndroidVersionName = getAndroidVersionName(context);
        //String AndroidVersionName = getRefreshedOSName(context);
        String AndroidSdkVersion = getAndroidSdkVersionNumber();

        String AndroidOsInfo;

        if (AndroidVersionName != null && !AndroidVersionName.isEmpty() && !AndroidVersionName.equals("")) {
            AndroidOsInfo = AndroidVersionName;
        } else {
            AndroidOsInfo = "Unknown Os Version Name";
        }

        /*if(AndroidVersionNumber!= null && !AndroidVersionNumber.isEmpty() &&!AndroidVersionNumber.equals(""))
        {
            AndroidOsInfo = AndroidOsInfo+"|"+AndroidVersionNumber;
        }else
        {
            AndroidOsInfo = AndroidOsInfo+"|"+"Unknown Os Version Number";
        }*/

        if (AndroidSdkVersion != null && !AndroidSdkVersion.isEmpty() && !AndroidSdkVersion.equals("")) {
            AndroidOsInfo = AndroidOsInfo + "|" + AndroidSdkVersion;
        } else {
            AndroidOsInfo = AndroidOsInfo + "|" + "Unknown Os SDK Number";
        }
        return AndroidOsInfo;
    }

    public String getDeviceID(Context context) {
        String DeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return DeviceID;
    }


    public void disablefor1sec(final View v) {
        try {
            v.setEnabled(false);
            v.setAlpha((float) 0.5);
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        v.setEnabled(true);
                        v.setAlpha((float) 1.0);
                    } catch (Exception e) {
                        Log.d("disablefor1sec", " Exception while un hiding the view : " + e.getMessage());
                    }
                }
            }, 1000);
        } catch (Exception e) {
            Log.d("disablefor1sec", " Exception while hiding the view : " + e.getMessage());
        }
    }

    public interface LogOutListener {
        void doLogout(boolean isForground);
    }

    static Timer longTimer;
    static final int LOGOUT_TIME = 900000; // delay in milliseconds i.e. 15 min = 900000 ms or use timeout argument

    public static synchronized void startLogoutTimer(final Context context, final LogOutListener logOutListener) {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
        if (longTimer == null) {

            longTimer = new Timer();

            longTimer.schedule(new TimerTask() {

                public void run() {
                    cancel();

                    longTimer = null;

                    try {
                        boolean foreGround = new ForegroundCheckTask().execute(context).get();

                        if (foreGround) {
                            logOutListener.doLogout(foreGround);
                        } else {
                            logOutListener.doLogout(foreGround);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }, LOGOUT_TIME);
        }
    }

    public static synchronized void stopLogoutTimer() {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
    }

    static class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean AppInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

//    public void showCustomToast(AppCompatActivity appCompatActivity,String ToastMessage) {
//        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
//        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) appCompatActivity.findViewById(R.id.custom_toast_container));
//        TextView tv = (TextView) layout.findViewById(R.id.tv_custom_toast_text);
//        tv.setText(ToastMessage);
//        Toast toast = new Toast(appCompatActivity);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
//    }


    public View getNoInputTextInputActivityRequest(View view, AppCompatActivity appCompatActivity) {
        if (view instanceof TextInputEditText) {
            TextInputEditText textInputEditText = (TextInputEditText) view;

            textInputEditText.setFocusableInTouchMode(false);
            textInputEditText.setFocusable(false);
            textInputEditText.setInputType(InputType.TYPE_NULL);
        } else if(view instanceof EditText)
        {
            EditText editText = (EditText) view;

            editText.setFocusableInTouchMode(false);
            editText.setFocusable(false);
            editText.setInputType(InputType.TYPE_NULL);
        }
        return view;
    }

    public void setUpTextToView(String isMarquee, View view, String Text) {
        if (isMarquee.equals("1")) {
            setMarqueeTextToViewMethodsAndProperties(view);
        }
        if (Text != null && !Text.isEmpty() && !Text.equals("")) {
            setHtmlTextToView(view, Text);
            // do what you want with imageView
        }
    }

    private void setMarqueeTextToViewMethodsAndProperties(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;

            textView.setHorizontallyScrolling(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine();
            textView.setSelected(true);
        } else if (view instanceof EditText) {
            EditText editText = (EditText) view;

            editText.setHorizontallyScrolling(true);
            editText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            editText.setSingleLine();
            editText.setSelected(true);
        } else if (view instanceof TextInputEditText) {
            TextInputEditText textInputEditText = (TextInputEditText) view;

            textInputEditText.setHorizontallyScrolling(true);
            textInputEditText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textInputEditText.setSingleLine();
            textInputEditText.setSelected(true);
        } else if (view instanceof Button) {
            Button button = (Button) view;

            button.setHorizontallyScrolling(true);
            button.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            button.setSingleLine();
            button.setSelected(true);
        }

    }

    public void setHtmlTextToView(View view, String Text) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(Text, Html.FROM_HTML_MODE_COMPACT));
            } else {
                textView.setText(Html.fromHtml(Text));
            }
        } else if (view instanceof EditText) {
            EditText editText = (EditText) view;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                editText.setText(Html.fromHtml(Text, Html.FROM_HTML_MODE_COMPACT));
            } else {
                editText.setText(Html.fromHtml(Text));
            }
        } else if (view instanceof TextInputEditText) {
            TextInputEditText textInputEditText = (TextInputEditText) view;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textInputEditText.setText(Html.fromHtml(Text, Html.FROM_HTML_MODE_COMPACT));
            } else {
                textInputEditText.setText(Html.fromHtml(Text));
            }
        } else if (view instanceof Button) {
            Button button = (Button) view;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                button.setText(Html.fromHtml(Text, Html.FROM_HTML_MODE_COMPACT));
            } else {
                button.setText(Html.fromHtml(Text));
            }
        }

    }


    public boolean CheckEmptyString(Context context, String text, String message) {
        boolean isValid = false;
        if (text != null && !text.equals("") && !text.isEmpty()) {
            isValid = true;
        } else {
            isValid = false;
            if (message != null && !message.isEmpty() && !message.equals("")) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
        return isValid;
    }


    public int getStatusBarHeight(Context context) {
        int height;
        Resources myResources = context.getResources();
        int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = context.getResources().getDimensionPixelSize(idStatusBarHeight);
            //Toast.makeText(this, "Status Bar Height = " + height, Toast.LENGTH_LONG).show();
        } else {
            height = 0;
            //Toast.makeText(this, "Resources NOT found", Toast.LENGTH_LONG).show();
        }
        return height;
    }

    public int getActionBarHeight(Context context) {
        int actionBarHeight;
        int actualActionBarHeight;
        int[] abSzAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            abSzAttr = new int[]{android.R.attr.actionBarSize};
        } else {
            abSzAttr = new int[]{R.attr.actionBarSize};
        }
        TypedArray a = context.obtainStyledAttributes(abSzAttr);
        actualActionBarHeight = a.getDimensionPixelSize(0, -1);

        if (actualActionBarHeight > 0) {
            actionBarHeight = actualActionBarHeight;
        } else {
            actionBarHeight = 0;
        }

        return actionBarHeight;
    }


  /*  public String getMillisToHMS(Context context, long milliSeconds) {
        String HMS = "";
        if (Integer.parseInt(String.valueOf(milliSeconds)) >= 60) {
            int hours = (int) ((milliSeconds / (1000 * 60 * 60)) % 24);
            int minutes = (int) ((milliSeconds / (1000 * 60)) % 60);
            int seconds = (int) (milliSeconds / 1000) % 60;

            if (seconds > 0) {
                if (minutes > 0) {
                    if (hours > 0) {
                        String Hrs;
                        String Mins;
                        String Secs;
                        if (hours == 1) {
                            Hrs = hours + " " + context.getString(R.string.common_timer_hr_text) + " ";

                            if (minutes == 1) {
                                Mins = minutes + " " + context.getString(R.string.common_timer_min_text) + " ";

                                if (seconds == 1) {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_sec_text);
                                } else {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_secs_text);
                                }
                            } else {
                                Mins = minutes + " " + context.getString(R.string.common_timer_mins_text) + " ";

                                if (seconds == 1) {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_sec_text);
                                } else {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_secs_text);
                                }
                            }

                        } else {
                            Hrs = hours + " " + context.getString(R.string.common_timer_hrs_text) + " ";

                            if (minutes == 1) {
                                Mins = minutes + " " + context.getString(R.string.common_timer_min_text) + " ";

                                if (seconds == 1) {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_sec_text);
                                } else {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_secs_text);
                                }
                            } else {
                                Mins = minutes + " " + context.getString(R.string.common_timer_mins_text) + " ";

                                if (seconds == 1) {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_sec_text);
                                } else {
                                    Secs = seconds + " " + context.getString(R.string.common_timer_secs_text);
                                }
                            }
                        }
                        HMS = Hrs + Mins + Secs;

                    } else {
                        String Mins;
                        String Secs;
                        if (minutes == 1) {
                            Mins = minutes + " " + context.getString(R.string.common_timer_min_text)+" ";

                            if (seconds == 1) {
                                Secs = seconds + " " + context.getString(R.string.common_timer_sec_text);
                            } else {
                                Secs = seconds + " " + context.getString(R.string.common_timer_secs_text);
                            }
                        } else {
                            Mins = minutes + " " + context.getString(R.string.common_timer_mins_text)+" ";

                            if (seconds == 1) {
                                Secs = seconds + " " + context.getString(R.string.common_timer_sec_text);
                            } else {
                                Secs = seconds + " " + context.getString(R.string.common_timer_secs_text);
                            }
                        }

                        HMS = Mins + Secs;
                    }
                } else {
                    if (seconds == 1) {
                        HMS = seconds + " " + context.getString(R.string.common_timer_sec_text);
                    } else {
                        HMS = seconds + " " + context.getString(R.string.common_timer_secs_text);
                    }
                }
            } else {
                HMS = "0 " + context.getString(R.string.common_timer_sec_text);
            }
        } else {
            HMS = "0 " + context.getString(R.string.common_timer_sec_text);
        }

        return HMS;
    }


    public boolean CheckBioMetricLock(Context context) {
        boolean isValid = false;
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                isValid = true;
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                isValid = false;
                Toast.makeText(context, context.getString(R.string.common_biometrics_no_hardware_error), Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                isValid = false;
                Toast.makeText(context, context.getString(R.string.common_biometrics_hm_unavailable), Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                isValid = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    context.startActivity(intent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Intent intent = new Intent(Settings.ACTION_FINGERPRINT_ENROLL);
                    context.startActivity(intent);
                }

                Toast.makeText(context, context.getString(R.string.common_biometrics_not_enrolled), Toast.LENGTH_SHORT).show();
                break;
        }
        return isValid;
    }*/

}
