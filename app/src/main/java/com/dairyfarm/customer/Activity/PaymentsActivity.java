package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dairyfarm.customer.Adapter.PaymentMethodsAdapter;
import com.dairyfarm.customer.Model.PaymentMethods;
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

public class PaymentsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rVPaymentMethods;
    LinearLayoutManager linearLayoutManager;

    ArrayList<PaymentMethods> listPaymentMethod = new ArrayList<PaymentMethods>();
    PaymentMethodsAdapter paymentMethodsAdapter;

    ImageView ivBack;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    int currentPageValue = 1;

    Button btnSelectPaymentsSelected, btnSelectPaymentsNotSelected;

    String selectedPaymentMethodID;

    String PaymentMethodID;

    LinearLayout llPayments, llNoRecordFound, llNoInternetConnection;
    TextView tvMessage, tvPaymentMethodMessage;
    Button btnRetry;
    String Message;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler1, progressDialogHandler2;
    CustomerSessionManager customerSessionManager;

    SwipeRefreshLayout swipeToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        progressDialogHandler1 = new ProgressDialogHandler(this);
        progressDialogHandler2 = new ProgressDialogHandler(this);
        customerSessionManager = new CustomerSessionManager(this);
        atClass = new AtClass();

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        llPayments = findViewById(R.id.llPayments);
        llNoRecordFound = findViewById(R.id.llError);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);

        tvMessage = findViewById(R.id.tvMessage);
        tvPaymentMethodMessage = findViewById(R.id.tvPaymentMethodMessage);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        btnSelectPaymentsSelected = findViewById(R.id.btnSelectPaymentsSelected);
        btnSelectPaymentsNotSelected = findViewById(R.id.btnSelectPaymentsNotSelected);

        rVPaymentMethods = findViewById(R.id.rVPaymentMethods);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_large);
        rVPaymentMethods.addItemDecoration(itemDecoration);
        linearLayoutManager = new LinearLayoutManager(this);
        rVPaymentMethods.setLayoutManager(linearLayoutManager);
        btnSelectPaymentsSelected.setOnClickListener(this);

        setUpSwipeToRefreshLayout();
    }

    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                Intent i = new Intent(PaymentsActivity.this, PaymentsActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        /*rVPaymentMethods.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (atClass.isNetworkAvailable(PaymentsActivity.this)) {
                        currentPageValue++;
                        getPaymentsData();
                    } else {
                        llPayments.setVisibility(View.GONE);
                        llNoRecordFound.setVisibility(View.GONE);
                        llNoInternetConnection.setVisibility(View.VISIBLE);
                    }
                }
            }
        });*/

        if (atClass.isNetworkAvailable(PaymentsActivity.this)) {
            getPaymentsData();
        } else {
            llPayments.setVisibility(View.GONE);
            llNoRecordFound.setVisibility(View.GONE);
            llNoInternetConnection.setVisibility(View.VISIBLE);
        }
    }

    public void setData(String paymentMethodID, String paymentMethodMessage) {
        selectedPaymentMethodID = paymentMethodID;

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPaymentMethodMessage.setText(Html.fromHtml(paymentMethodMessage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvPaymentMethodMessage.setText(Html.fromHtml(paymentMethodMessage));
        }*/
    }

    public void setShowButton() {
        btnSelectPaymentsSelected.setVisibility(View.VISIBLE);
        btnSelectPaymentsNotSelected.setVisibility(View.GONE);
        //btnDeliverToThisAddressSelected.setBackgroundColor(Color.parseColor("#2f3978"));
    }

    public void setHideButton() {
        btnSelectPaymentsSelected.setVisibility(View.GONE);
        btnSelectPaymentsNotSelected.setVisibility(View.VISIBLE);
        //btnDeliverToThisAddressSelected.setBackgroundColor(Color.parseColor("#2f3978"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectPaymentsSelected:
                if (atClass.isNetworkAvailable(this)) {
                    if (checkPaymentMethodID()) {
                        SelectPaymentMethod();
                    }
                } else {
                    llPayments.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(PaymentsActivity.this)) {
                    Intent i = new Intent(PaymentsActivity.this, PaymentsActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    llPayments.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private boolean checkPaymentMethodID() {
        boolean isValid = false;
        if (selectedPaymentMethodID != null && !selectedPaymentMethodID.isEmpty() && !selectedPaymentMethodID.equals("")) {
            isValid = true;
        } else {
            isValid = false;
            Toast.makeText(this, getString(R.string.payment_method_selection_validation_error_text), Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    private void SelectPaymentMethod() {
        progressDialogHandler2.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SELECT_PAYMENT_METHOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseSelectPaymentMethodJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(PaymentsActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(PaymentsActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(PaymentsActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(PaymentsActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentsActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(PaymentsActivity.this);
                params.put(JsonFields.SELECT_PAYMENT_METHOD_POST_PARAMS_PAYMENT_METHOD_ID, selectedPaymentMethodID);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseSelectPaymentMethodJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                onBackPressed();
            } else if (flag == 3) {
                progressDialogHandler2.showPopupProgressSpinner(false);

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
                progressDialogHandler1.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getPaymentsData() {
        progressDialogHandler1.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PAYMENT_METHOD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llPayments.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    llPayments.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    llPayments.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    llPayments.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    llPayments.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(PaymentsActivity.this);
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
                progressDialogHandler1.showPopupProgressSpinner(false);

                if (currentPageValue == 1) {
                    listPaymentMethod.clear();
                }

                PaymentMethodID = jsonObject.optString(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_SELECTED_PAYMENT_METHOD_ID);

                JSONArray arrPayments = jsonObject.optJSONArray(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_ARRAY);
                if (arrPayments.length() > 0) {
                    for (int i = 0; i < arrPayments.length(); i++) {
                        JSONObject paymentsObject = arrPayments.optJSONObject(i);
                        String payment_method_id = paymentsObject.optString(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_ID);
                        String payment_method_name = paymentsObject.optString(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_NAME);
                        String payment_method_image = paymentsObject.optString(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_IMAGE_URL);
                        String payment_method_message = paymentsObject.optString(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_MESSAGE);
                        String is_default = paymentsObject.optString(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_IS_DEFAULT);
                        String can_proceed = paymentsObject.optString(JsonFields.PAYMENTS_RESPONSE_PAYMENT_METHOD_CAN_PROCEED);

                        listPaymentMethod.add(new PaymentMethods(payment_method_id, payment_method_name, payment_method_image, payment_method_message, is_default, can_proceed));
                    }
                    PaymentMethodsAdapter paymentMethodsAdapter = new PaymentMethodsAdapter(listPaymentMethod, PaymentMethodID);
                    rVPaymentMethods.setAdapter(paymentMethodsAdapter);
                    llPayments.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                }
            } else if (flag == 2) {
                progressDialogHandler1.showPopupProgressSpinner(false);

                llPayments.setVisibility(View.VISIBLE);
                llNoRecordFound.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
            } else if (flag == 3) {
                progressDialogHandler1.showPopupProgressSpinner(false);

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
                progressDialogHandler1.showPopupProgressSpinner(false);

                llPayments.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}