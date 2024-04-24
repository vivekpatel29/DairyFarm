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
import com.dairyfarm.customer.Adapter.SelectAddressAdapter;
import com.dairyfarm.customer.Model.SelectAddress;
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

public class SelectAddressActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivBack;
    RecyclerView rvAddress;

    private ArrayList<SelectAddress> listSelectAddress = new ArrayList<>();
    SelectAddress selectAddress;

    LinearLayoutManager linearLayoutManager;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    int currentPageValue = 1;

    Button btnDeliverToThisAddressSelected, btnDeliverToThisAddressNotSelected;

    String selectedAddressID;

    String AddressID;

    LinearLayout llSelectAddress, llNoRecordFound, llNoInternetConnection;
    TextView tvMessage;
    Button btnRetry;
    String Message;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;
    CustomerSessionManager customerSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        progressDialogHandler = new ProgressDialogHandler(this);
        customerSessionManager = new CustomerSessionManager(this);
        atClass = new AtClass();

        Intent i = getIntent();

        if (i.hasExtra("AddressID")) {
            AddressID = i.getStringExtra("AddressID");
            Log.d("AddressID", "Add" + AddressID);
        }

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        llSelectAddress = findViewById(R.id.llSelectAddress);
        llNoRecordFound = findViewById(R.id.llError);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);

        tvMessage = findViewById(R.id.tvMessage);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        btnDeliverToThisAddressSelected = findViewById(R.id.btnDeliverToThisAddressSelected);
        btnDeliverToThisAddressNotSelected = findViewById(R.id.btnDeliverToThisAddressNotSelected);

        rvAddress = findViewById(R.id.rvAddress);

        linearLayoutManager = new LinearLayoutManager(this);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvAddress.addItemDecoration(itemDecoration);
        rvAddress.setLayoutManager(linearLayoutManager);

        btnDeliverToThisAddressSelected.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        rvAddress.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (atClass.isNetworkAvailable(SelectAddressActivity.this)) {
                        currentPageValue++;
                        getAddressData();
                    } else {
                        llSelectAddress.setVisibility(View.GONE);
                        llNoRecordFound.setVisibility(View.GONE);
                        llNoInternetConnection.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        if (atClass.isNetworkAvailable(SelectAddressActivity.this)) {
            getAddressData();
        } else {
            llSelectAddress.setVisibility(View.GONE);
            llNoRecordFound.setVisibility(View.GONE);
            llNoInternetConnection.setVisibility(View.VISIBLE);
        }
    }

    public void setData(String addressID) {
        selectedAddressID = addressID;
    }

    public void setShowButton() {
        btnDeliverToThisAddressSelected.setVisibility(View.VISIBLE);
        btnDeliverToThisAddressNotSelected.setVisibility(View.GONE);
        //btnDeliverToThisAddressSelected.setBackgroundColor(Color.parseColor("#2f3978"));
    }

    public void setHideButton() {
        btnDeliverToThisAddressSelected.setVisibility(View.GONE);
        btnDeliverToThisAddressNotSelected.setVisibility(View.VISIBLE);
        //btnDeliverToThisAddressSelected.setBackgroundColor(Color.parseColor("#2f3978"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDeliverToThisAddressSelected:
                Intent intent = new Intent(this, MyCartActivity.class);
                intent.putExtra("AddressID", selectedAddressID);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(SelectAddressActivity.this)) {
                    getAddressData();
                } else {
                    llSelectAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }


    private void getAddressData() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llSelectAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    llSelectAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    llSelectAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    llSelectAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    llSelectAddress.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(SelectAddressActivity.this);
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
                progressDialogHandler.showPopupProgressSpinner(false);

                if (currentPageValue == 1) {
                    listSelectAddress.clear();
                }

                JSONArray arrAddress = jsonObject.optJSONArray(JsonFields.ADDRESS_RESPONSE_ADDRESS_ARRAY);
                if (arrAddress.length() > 0) {
                    for (int i = 0; i < arrAddress.length(); i++) {
                        JSONObject addressObject = arrAddress.optJSONObject(i);
                        String address_id = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_ID);
                        String address_name = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_NAME);
                        String address = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_LINE);
                        String is_default = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_IS_DEFAULT);
                        String mobile_number = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_MOBILE_NUMBER);
                        String is_available = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_IS_AVAILABLE);
                        String available_string = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_AVAILABLE_STRING);
                        String address_type = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_TYPE);
                        String address_latitude = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_LATITUDE);
                        String address_longitude = addressObject.optString(JsonFields.ADDRESS_RESPONSE_ADDRESS_LONGITUDE);

                        listSelectAddress.add(new SelectAddress(address_id, address_name, address, is_default, mobile_number, is_available, available_string, address_type, address_latitude, address_longitude));
                    }
                    SelectAddressAdapter selectAddressAdapter = new SelectAddressAdapter(listSelectAddress, AddressID);
                    rvAddress.setAdapter(selectAddressAdapter);
                    llSelectAddress.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                }
            } else if (flag == 2) {
                progressDialogHandler.showPopupProgressSpinner(false);

                llSelectAddress.setVisibility(View.VISIBLE);
                llNoRecordFound.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
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

                llSelectAddress.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
