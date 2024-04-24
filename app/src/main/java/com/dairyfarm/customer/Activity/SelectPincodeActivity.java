package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.dairyfarm.customer.Adapter.SelectPincodesAdapter;
import com.dairyfarm.customer.Model.SelectPincodes;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SelectPincodeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    RecyclerView rvSelectPincodes;

    private ArrayList<SelectPincodes> listSelectPincodes = new ArrayList<>();
    SelectPincodesAdapter selectPincodesAdapter;
    SelectPincodes selectPincodes;

    String PincodeID;

    LinearLayoutManager linearLayoutManager;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    int currentPageValue = 1;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;
    CustomerSessionManager customerSessionManager;

    LinearLayout llSelectPincode, llNoRecordFound, llNoInternetConnection;
    Button btnRetry;
    TextView tvMessage;
    String Message;

    String isFrom;


    // 1 isFrom : From Add Address
    // 2 isFrom : From Edit Address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pincode);

        llSelectPincode = findViewById(R.id.llSelectPincode);
        llNoRecordFound = findViewById(R.id.llError);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);

        btnRetry = findViewById(R.id.btnRetry);
        tvMessage = findViewById(R.id.tvMessage);

        atClass = new AtClass();
        progressDialogHandler = new ProgressDialogHandler(this);
        customerSessionManager = new CustomerSessionManager(this);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        Intent i = getIntent();

        if (i.hasExtra("PincodeID")) {
            PincodeID = i.getStringExtra("PincodeID");
            isFrom = i.getStringExtra("isFrom");
        }

        selectPincodesAdapter = new SelectPincodesAdapter(listSelectPincodes, PincodeID, isFrom);
        rvSelectPincodes = findViewById(R.id.rvSelectPincodes);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_large);
        rvSelectPincodes.addItemDecoration(itemDecoration);
        linearLayoutManager = new LinearLayoutManager(this);
        rvSelectPincodes.setLayoutManager(linearLayoutManager);
        rvSelectPincodes.setAdapter(selectPincodesAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        rvSelectPincodes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    if (atClass.isNetworkAvailable(SelectPincodeActivity.this)) {
                        currentPageValue++;
                        getPincodesData();
                    } else {
                        llSelectPincode.setVisibility(View.GONE);
                        llNoRecordFound.setVisibility(View.GONE);
                        llNoInternetConnection.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        if (atClass.isNetworkAvailable(SelectPincodeActivity.this)) {
            getPincodesData();
        } else {
            llSelectPincode.setVisibility(View.GONE);
            llNoRecordFound.setVisibility(View.GONE);
            llNoInternetConnection.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(SelectPincodeActivity.this)) {
                    getPincodesData();
                } else {
                    llSelectPincode.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);

                }
                break;
        }
    }

    private void getPincodesData() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PINCODES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llSelectPincode.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    llSelectPincode.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    llSelectPincode.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    llSelectPincode.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    llSelectPincode.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(SelectPincodeActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.COMMON_REQUEST_PARAM_CURRENT_PAGE, String.valueOf(currentPageValue));
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

                if (currentPageValue == 1) {
                    listSelectPincodes.clear();
                }

                progressDialogHandler.showPopupProgressSpinner(false);
                JSONArray arrPincodes = jsonObject.optJSONArray(JsonFields.PINCODE_RESPONSE_PINCODE_ARRAY);
                if (arrPincodes.length() > 0) {
                    for (int i = 0; i < arrPincodes.length(); i++) {
                        JSONObject pincodesObject = arrPincodes.optJSONObject(i);
                        String pincode_id = pincodesObject.optString(JsonFields.PINCODE_RESPONSE_PINCODE_ID);
                        String pincode_title = pincodesObject.optString(JsonFields.PINCODE_RESPONSE_PINCODE_TITLE);
                        String pincode = pincodesObject.optString(JsonFields.PINCODE_RESPONSE_PINCODE);
                        String area = pincodesObject.optString(JsonFields.PINCODE_RESPONSE_AREA);
                        String city = pincodesObject.optString(JsonFields.PINCODE_RESPONSE_CITY);
                        String state = pincodesObject.optString(JsonFields.PINCODE_RESPONSE_STATE);

                        listSelectPincodes.add(new SelectPincodes(pincode_id, pincode, pincode_title, area, city, state));
                    }

                    selectPincodesAdapter.notifyDataSetChanged();
                    llSelectPincode.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                }
            } else if (flag == 2) {
                progressDialogHandler.showPopupProgressSpinner(false);
                llSelectPincode.setVisibility(View.VISIBLE);
                llNoRecordFound.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
            } else {
                progressDialogHandler.showPopupProgressSpinner(false);
                llSelectPincode.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}