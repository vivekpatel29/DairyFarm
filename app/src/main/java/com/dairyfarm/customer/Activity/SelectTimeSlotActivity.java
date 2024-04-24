package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dairyfarm.customer.Fragments.TimeSlotTimeFragment;
import com.dairyfarm.customer.Model.TimeSlotDates;
import com.dairyfarm.customer.Model.TimeSlotTime;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.DefaultTimeSlotManager;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SelectTimeSlotActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llSelectDeliveryTimeSlot, llNoRecordFound, llNoInternetConnection;

    TabLayout timeSlotDateTabs;
    ViewPager timeSlotViewPager;

    ProgressDialogHandler progressDialogHandler;
    AtClass atClass;

    String Message;

    ArrayList<TimeSlotDates> listTimeSlotDates = new ArrayList<>();
    ArrayList<TimeSlotTime> listTimeSlotTime = new ArrayList<>();
    ArrayList<TimeSlotTime> listTimeSlotTempTime = new ArrayList<>();

    ViewPagerAdapter timeSlotViewPagerAdapter;

    TextView tvMessage;

    Button btnSelectTimeSlotSelected, btnSelectTimeSlotNotSelected;

    String TimeSlotID;

    Button btnRetry;

    CustomerSessionManager customerSessionManager;

    ImageView ivBack;

    DefaultTimeSlotManager defaultTimeSlotManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time_slot);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        customerSessionManager = new CustomerSessionManager(this);
        defaultTimeSlotManager = new DefaultTimeSlotManager(this);

        Intent i = getIntent();

        if (i.hasExtra("TimeSlotID")) {
            if (i.getStringExtra("TimeSlotID") != null &&
                    !i.getStringExtra("TimeSlotID").equals("") &&
                    !i.getStringExtra("TimeSlotID").isEmpty()) {

                defaultTimeSlotManager.setTimeSlotDefaultID(i.getStringExtra("TimeSlotID"));
            } else {
                defaultTimeSlotManager.RemoveDefaultTimeSlotID();
            }
        } else {
            defaultTimeSlotManager.RemoveDefaultTimeSlotID();
        }


        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        btnSelectTimeSlotSelected = findViewById(R.id.btnSelectTimeSlotSelected);
        btnSelectTimeSlotNotSelected = findViewById(R.id.btnSelectTimeSlotNotSelected);

        btnSelectTimeSlotSelected.setOnClickListener(this);

        tvMessage = findViewById(R.id.tvMessage);

        atClass = new AtClass();

        progressDialogHandler = new ProgressDialogHandler(this);

        llSelectDeliveryTimeSlot = findViewById(R.id.llSelectDeliveryTimeSlot);
        llNoRecordFound = findViewById(R.id.llError);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);

        timeSlotDateTabs = findViewById(R.id.timeSlotDateTabs);
        timeSlotViewPager = findViewById(R.id.timeSlotViewPager);
        timeSlotDateTabs.setupWithViewPager(timeSlotViewPager);


        if (atClass.isNetworkAvailable(this)) {
            getTimeSlotData();
        } else {
            llNoInternetConnection.setVisibility(View.VISIBLE);
            llNoRecordFound.setVisibility(View.GONE);
            llSelectDeliveryTimeSlot.setVisibility(View.GONE);
        }

    }


    private void getTimeSlotData() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DELIVERY_SLOTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(SelectTimeSlotActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            Message = jsonObject.optString(JsonFields.MESSAGE);
            if (flag == 1) {
                listTimeSlotDates.clear();
                progressDialogHandler.showPopupProgressSpinner(false);
                JSONArray arrTimeSlotDates = jsonObject.optJSONArray(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_ARRAY);
                if (arrTimeSlotDates.length() > 0) {
                    for (int i = 0; i < arrTimeSlotDates.length(); i++) {
                        JSONObject timeSlotDatesObject = arrTimeSlotDates.optJSONObject(i);
                        String timeSlotDateID = timeSlotDatesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_ID);
                        String Date = timeSlotDatesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_DATE);
                        String dayName = timeSlotDatesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_DAY_NAME);
                        String monthName = timeSlotDatesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_MONTH_NAME);
                        String monthNo = timeSlotDatesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_MONTH_NO);
                        String Year = timeSlotDatesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_YEAR);
                        String isDefault = timeSlotDatesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_DATES_IS_DEFAULT);

                        JSONArray arrTimeSlotTime = timeSlotDatesObject.optJSONArray(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_ARRAY);
                        if (arrTimeSlotTime != null) {
                            if (arrTimeSlotTime.length() > 0) {
                                listTimeSlotTime = new ArrayList<>();
                                for (int j = 0; j < arrTimeSlotTime.length(); j++) {
                                    JSONObject timeSlotTimesObject = arrTimeSlotTime.optJSONObject(j);
                                    String timeSlotTimeID = timeSlotTimesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_SLOT_TIME_ID);
                                    String timeSlotString = timeSlotTimesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_TIME_SLOT_STRING);
                                    String timeSlotTimeIsDefault = timeSlotTimesObject.optString(JsonFields.TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_IS_DEFAULT);

                                    listTimeSlotTime.add(new TimeSlotTime(timeSlotTimeID, timeSlotString, timeSlotTimeIsDefault));
                                }
                            }
                            listTimeSlotDates.add(new TimeSlotDates(timeSlotDateID, Date, dayName, monthName, monthNo, Year, isDefault, listTimeSlotTime));
                        } else {
                            listTimeSlotTime = null;
                        }
                    }
                    setupViewPager();
                    llSelectDeliveryTimeSlot.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                } else {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.VISIBLE);
                    tvMessage.setText(Message);
                }
            } else if (flag == 3) {
                progressDialogHandler.showPopupProgressSpinner(false);

                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(this, LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                startActivity(i);
                finish();

            } else {
                progressDialogHandler.showPopupProgressSpinner(false);
                llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectTimeSlotSelected:
                if (atClass.isNetworkAvailable(SelectTimeSlotActivity.this)) {
                    if (checkTimeSlotID()) {
                        selectTimeSlot();
                    }
                } else {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(SelectTimeSlotActivity.this)) {
                    getTimeSlotData();
                } else {
                    llSelectDeliveryTimeSlot.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private boolean checkTimeSlotID() {
        boolean isTimeSlotValid = false;

        if (defaultTimeSlotManager.getTimeSlotDefaultID() != null && !defaultTimeSlotManager.getTimeSlotDefaultID().isEmpty() && !defaultTimeSlotManager.getTimeSlotDefaultID().equals("")) {
            isTimeSlotValid = true;
        } else {
            isTimeSlotValid = false;
            Toast.makeText(this, getString(R.string.time_slot_please_select_any_time_slot_error_text), Toast.LENGTH_SHORT).show();
        }
        return isTimeSlotValid;
    }

    private void selectTimeSlot() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SELECT_DELIVERY_TIME_SLOT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseSelectTimeSlotJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(SelectTimeSlotActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(SelectTimeSlotActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(SelectTimeSlotActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(SelectTimeSlotActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectTimeSlotActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(SelectTimeSlotActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.SELECT_TIME_SLOT_REQUEST_PARAMS_TIME_SLOT_ID, defaultTimeSlotManager.getTimeSlotDefaultID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseSelectTimeSlotJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler.showPopupProgressSpinner(false);
                defaultTimeSlotManager.RemoveDefaultTimeSlotID();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 100);
            } else if (flag == 3) {
                progressDialogHandler.showPopupProgressSpinner(false);

                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(this, LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                startActivity(i);
                finish();

            } else {
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setHideButton() {
        Log.d("Hide Button Called", "Yes");
        btnSelectTimeSlotNotSelected.setVisibility(View.VISIBLE);
        btnSelectTimeSlotSelected.setVisibility(View.GONE);
    }

    public void setShowButton() {
        Log.d("Show Button Called", "Yes");
        btnSelectTimeSlotNotSelected.setVisibility(View.GONE);
        btnSelectTimeSlotSelected.setVisibility(View.VISIBLE);
    }


    public void setData(String timeSlotTimeID) {
        TimeSlotID = timeSlotTimeID;
        Log.d("ID Setted", "Yes");
        Log.d("ID Setted", "Yes");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        int mCurrentPosition = -1;

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            fragment = new TimeSlotTimeFragment(SelectTimeSlotActivity.this);
            listTimeSlotTempTime = listTimeSlotDates.get(position).getListTimeSlotTimes();

            Bundle b = new Bundle();
            b.putSerializable("listTimeSlotTime", listTimeSlotTempTime);
            fragment.setArguments(b);
            Fragment finalFragment = null;
            if (fragment != null) {
                finalFragment = fragment;
            }
            return finalFragment;
        }

        @Override
        public int getCount() {
            return listTimeSlotDates.size();
        }
    }


    private void setupViewPager() {
        timeSlotViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        timeSlotViewPager.setAdapter(timeSlotViewPagerAdapter);

        setupTabIcons();
    }

    private void setupTabIcons() {
        for (int i = 0; i < listTimeSlotDates.size(); i++) {
            View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.time_slot_tab_layout, null, false);

            RelativeLayout rlTimeSlotTabs = (RelativeLayout) headerView.findViewById(R.id.rlTimeSlotTabs);

            timeSlotDateTabs.getTabAt(i).setCustomView(rlTimeSlotTabs);

            TextView tvDate = (TextView) timeSlotDateTabs.getTabAt(i).getCustomView().findViewById(R.id.tv_date);
            TextView tvDayName = (TextView) timeSlotDateTabs.getTabAt(i).getCustomView().findViewById(R.id.tv_day_name);
            TextView tvMonthName = (TextView) timeSlotDateTabs.getTabAt(i).getCustomView().findViewById(R.id.tv_month_name);
            TextView tvYear = (TextView) timeSlotDateTabs.getTabAt(i).getCustomView().findViewById(R.id.tv_year);
            TextView tvMonthNo = (TextView) timeSlotDateTabs.getTabAt(i).getCustomView().findViewById(R.id.tv_month_no);

            tvDate.setText(listTimeSlotDates.get(i).getDate());
            tvMonthName.setText(listTimeSlotDates.get(i).getMonthName());
            tvDayName.setText(listTimeSlotDates.get(i).getDayName());
            tvYear.setText(listTimeSlotDates.get(i).getYear());
            tvMonthNo.setText(listTimeSlotDates.get(i).getMonthNo());
        }


        int position = 0;
        for (int j = 0; j < listTimeSlotDates.size(); j++) {
            if (listTimeSlotDates.get(j).getIsDefault().equals("1")) {
                position = j;
                Log.d("Position", String.valueOf(j));
            }
        }


        final int finalPosition = position;

        if (finalPosition == 0) {

            new Handler().postDelayed(() -> {
                        timeSlotViewPager.setCurrentItem(1);
                        timeSlotViewPager.setCurrentItem(finalPosition);
                        Log.d("Called Here", "Yes");
                        /*viewPager.setScrollPosition(position, 0f, true);*/
                    },
                    200);

        } else {

            new Handler().postDelayed(() -> {
                        timeSlotViewPager.setCurrentItem(finalPosition);
                        Log.d("Called Here", "Yes");
                        /*viewPager.setScrollPosition(position, 0f, true);*/
                    },
                    200);
        }


        timeSlotDateTabs.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(timeSlotViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
                        TextView tvDayName = (TextView) tab.getCustomView().findViewById(R.id.tv_day_name);
                        TextView tvMonthName = (TextView) tab.getCustomView().findViewById(R.id.tv_month_name);

                        TextView tvYear = (TextView) tab.getCustomView().findViewById(R.id.tv_year);
                        TextView tvMonthNo = (TextView) tab.getCustomView().findViewById(R.id.tv_month_no);

                        LinearLayout llBackground = (LinearLayout) tab.getCustomView().findViewById(R.id.llBackground);

                        tvDate.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        tvDayName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        tvMonthName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        llBackground.setBackgroundResource(R.drawable.time_slot_selected_tabs_bg);

                        Log.d("Tab Selected", "Yes");
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
                        TextView tvDayName = (TextView) tab.getCustomView().findViewById(R.id.tv_day_name);
                        TextView tvMonthName = (TextView) tab.getCustomView().findViewById(R.id.tv_month_name);
                        LinearLayout llBackground = (LinearLayout) tab.getCustomView().findViewById(R.id.llBackground);

                        tvDate.setTextColor(getResources().getColor(R.color.colorBlackThree));
                        tvDayName.setTextColor(getResources().getColor(R.color.colorBlackThree));
                        tvMonthName.setTextColor(getResources().getColor(R.color.colorBlackThree));
                        llBackground.setBackgroundResource(R.drawable.time_slot_not_selected_tabs_bg);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );


        timeSlotViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //int i = tab.getPosition();
                timeSlotViewPagerAdapter.getItem(i).setUserVisibleHint(false);
                Log.d("Page Selected", "Yes");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}