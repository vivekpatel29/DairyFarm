package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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
import com.dairyfarm.customer.Adapter.ProductNotAvailableToPlaceOrderAdapter;
import com.dairyfarm.customer.Model.ProductNotAvailableToPlaceOrder;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class CartErrorsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iVBackClosedForDelivery, iVBackClosedForDeliveryAtLocation, ivBackInvalidCartItems, iVBackInvalidCoupon, iVBackInvalidTimeSlot, iVBackInvalidOrderAmount, iVBackInvalidPaymentMethod;
    LinearLayout llClosedForDelivery, llClosedForDeliveryAtLocation, llInvalidCartItems, llInvalidCoupon, llInvalidTimeSlot, llInvalidOrderAmount, llInvalidPaymentMethod;
    TextView tvClosedForDeliveryMessage, tvClosedForDeliveryAtLocationMessage, tvInvalidCouponMessage, tvInvalidTimeSlotMessage, tvInvalidOrderAmountMessage, tvInvalidPaymentMethodMessage, tvRemoveItemMessage;
    Button btnGoBackToCartClosedForDelivery, btnGoBackToCartClosedForDeliveryAtLocation, btnRemoveInvalidCartItems, btnRemoveInvalidCoupon, btnRemoveInvalidTimeSlot, btnGoBackToCartInvalidOrderAmount, btnGoBackToCartInvalidPaymentMethod;

    RecyclerView rVProductsNotAvailable;
    ArrayList<ProductNotAvailableToPlaceOrder> listProductNotAvailableToPlaceOrder = new ArrayList<>();
    ProductNotAvailableToPlaceOrderAdapter productNotAvailableToPlaceOrderAdapter = new ProductNotAvailableToPlaceOrderAdapter(listProductNotAvailableToPlaceOrder);

    AtClass atClass;
    LinearLayoutManager linearLayoutManager;

    String flag = "";
    String ClosedForDeliveryMessage = "";
    String WeAreClosedForDeliveryAtLocation = "";
    String InvalidCouponMessage = "";
    String InvalidTimeSlotMessage = "";
    String InvalidOrderAmountMessage = "";
    String InvalidPaymentMethodMessage = "";
    String InvalidProductsMessage = "";

    String TimeSlotID = "";

    ProgressDialogHandler progressDialogHandler1, progressDialogHandler2, progressDialogHandler3;
    CustomerSessionManager customerSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_errors);

        progressDialogHandler1 = new ProgressDialogHandler(this);
        progressDialogHandler2 = new ProgressDialogHandler(this);
        progressDialogHandler3 = new ProgressDialogHandler(this);

        customerSessionManager = new CustomerSessionManager(this);

        iVBackClosedForDelivery = findViewById(R.id.iVBackClosedForDelivery);
        iVBackClosedForDeliveryAtLocation = findViewById(R.id.iVBackClosedForDeliveryAtLocation);
        ivBackInvalidCartItems = findViewById(R.id.ivBackInvalidCartItems);
        iVBackInvalidCoupon = findViewById(R.id.iVBackInvalidCoupon);
        iVBackInvalidTimeSlot = findViewById(R.id.iVBackInvalidTimeSlot);
        iVBackInvalidOrderAmount = findViewById(R.id.iVBackInvalidOrderAmount);
        iVBackInvalidPaymentMethod = findViewById(R.id.iVBackInvalidPaymentMethod);


        iVBackClosedForDelivery.setOnClickListener(this);
        iVBackClosedForDeliveryAtLocation.setOnClickListener(this);
        ivBackInvalidCartItems.setOnClickListener(this);
        iVBackInvalidCoupon.setOnClickListener(this);
        iVBackInvalidTimeSlot.setOnClickListener(this);
        iVBackInvalidOrderAmount.setOnClickListener(this);
        iVBackInvalidPaymentMethod.setOnClickListener(this);


        llClosedForDelivery = findViewById(R.id.llClosedForDelivery);
        llClosedForDeliveryAtLocation = findViewById(R.id.llClosedForDeliveryAtLocation);
        llInvalidCartItems = findViewById(R.id.llInvalidCartItems);
        llInvalidCoupon = findViewById(R.id.llInvalidCoupon);
        llInvalidTimeSlot = findViewById(R.id.llInvalidTimeSlot);
        llInvalidOrderAmount = findViewById(R.id.llInvalidOrderAmount);
        llInvalidPaymentMethod = findViewById(R.id.llInvalidPaymentMethod);

        tvClosedForDeliveryMessage = findViewById(R.id.tvClosedForDeliveryMessage);
        tvClosedForDeliveryAtLocationMessage = findViewById(R.id.tvClosedForDeliveryAtLocationMessage);
        tvInvalidCouponMessage = findViewById(R.id.tvInvalidCouponMessage);
        tvInvalidTimeSlotMessage = findViewById(R.id.tvInvalidTimeSlotMessage);
        tvInvalidOrderAmountMessage = findViewById(R.id.tvInvalidOrderAmountMessage);
        tvInvalidPaymentMethodMessage = findViewById(R.id.tvInvalidPaymentMethodMessage);
        tvRemoveItemMessage = findViewById(R.id.tvRemoveItemMessage);

        btnGoBackToCartClosedForDelivery = findViewById(R.id.btnGoBackToCartClosedForDelivery);
        btnGoBackToCartClosedForDeliveryAtLocation = findViewById(R.id.btnGoBackToCartClosedForDeliveryAtLocation);
        btnRemoveInvalidCartItems = findViewById(R.id.btnRemoveInvalidCartItems);
        btnRemoveInvalidCoupon = findViewById(R.id.btnRemoveInvalidCoupon);
        btnRemoveInvalidTimeSlot = findViewById(R.id.btnRemoveInvalidTimeSlot);
        btnGoBackToCartInvalidOrderAmount = findViewById(R.id.btnGoBackToCartInvalidOrderAmount);
        btnGoBackToCartInvalidPaymentMethod = findViewById(R.id.btnGoBackToCartInvalidPaymentMethod);


        btnGoBackToCartClosedForDelivery.setOnClickListener(this);
        btnGoBackToCartClosedForDeliveryAtLocation.setOnClickListener(this);
        btnRemoveInvalidCartItems.setOnClickListener(this);
        btnRemoveInvalidCoupon.setOnClickListener(this);
        btnRemoveInvalidTimeSlot.setOnClickListener(this);
        btnGoBackToCartInvalidOrderAmount.setOnClickListener(this);
        btnGoBackToCartInvalidPaymentMethod.setOnClickListener(this);


        atClass = new AtClass();

        rVProductsNotAvailable = findViewById(R.id.rVProductsNotAvailable);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rVProductsNotAvailable.addItemDecoration(itemDecoration);
        linearLayoutManager = new LinearLayoutManager(this);
        rVProductsNotAvailable.setLayoutManager(linearLayoutManager);
        rVProductsNotAvailable.setAdapter(productNotAvailableToPlaceOrderAdapter);

        btnRemoveInvalidCartItems.setOnClickListener(this);

        Intent i = getIntent();

        if (i.hasExtra("flag")) {
            flag = i.getStringExtra("flag");

            if (flag.equals("2")) {
                //Closed For Delivery
                ClosedForDeliveryMessage = i.getStringExtra("Message");
                SetUpClosedForDeliveryLayoutAndData();
            } else if (flag.equals("4")) {
                //We are closed for certain location
                WeAreClosedForDeliveryAtLocation = i.getStringExtra("Message");
                SetUpClosedForDeliveryAtLocationLayoutAndData();

            } else if (flag.equals("5")) {
                //Some products not available or Invalid Items
                if (i.hasExtra("alProductNotAvailableToPlaceOrder")) {
                    Bundle bundle = getIntent().getExtras();
                    if (bundle != null) {
                        listProductNotAvailableToPlaceOrder = (ArrayList<ProductNotAvailableToPlaceOrder>) bundle.getSerializable("alProductNotAvailableToPlaceOrder");
                        InvalidProductsMessage = bundle.getString("Message");

                        if (listProductNotAvailableToPlaceOrder != null && !listProductNotAvailableToPlaceOrder.isEmpty() && !listProductNotAvailableToPlaceOrder.equals("")) {
                            SetUpInvalidCartItemsLayoutAndData();
                        }
                    }
                }

            } else if (flag.equals("6")) {
                //Coupon Code Not Valid
                InvalidCouponMessage = i.getStringExtra("Message");
                SetUpInvalidCouponLayoutAndData();

            } else if (flag.equals("7")) {
                //Time Slot Not Valid
                InvalidTimeSlotMessage = i.getStringExtra("Message");
                TimeSlotID = i.getStringExtra("TimeSlotID");
                SetUpInvalidTimeSlotLayoutAndData();

            } else if (flag.equals("8")) {
                //Order Amount Not Valid
                InvalidOrderAmountMessage = i.getStringExtra("Message");
                SetUpInvalidOrderAmountLayoutAndData();
            } else if (flag.equals("9")) {
                //Order Amount Not Valid
                InvalidPaymentMethodMessage = i.getStringExtra("Message");
                SetUpInvalidPaymentMethodLayoutAndData();
            }


        }

    }

    private void SetUpInvalidPaymentMethodLayoutAndData() {
        llClosedForDelivery.setVisibility(View.GONE);
        llClosedForDeliveryAtLocation.setVisibility(View.GONE);
        llInvalidCartItems.setVisibility(View.GONE);
        llInvalidCoupon.setVisibility(View.GONE);
        llInvalidTimeSlot.setVisibility(View.GONE);
        llInvalidOrderAmount.setVisibility(View.GONE);
        llInvalidPaymentMethod.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvInvalidPaymentMethodMessage.setText(Html.fromHtml(InvalidPaymentMethodMessage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvInvalidPaymentMethodMessage.setText(Html.fromHtml(InvalidPaymentMethodMessage));
        }
    }


    private void SetUpInvalidOrderAmountLayoutAndData() {
        llClosedForDelivery.setVisibility(View.GONE);
        llClosedForDeliveryAtLocation.setVisibility(View.GONE);
        llInvalidCartItems.setVisibility(View.GONE);
        llInvalidCoupon.setVisibility(View.GONE);
        llInvalidTimeSlot.setVisibility(View.GONE);
        llInvalidOrderAmount.setVisibility(View.VISIBLE);
        llInvalidPaymentMethod.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvInvalidOrderAmountMessage.setText(Html.fromHtml(InvalidOrderAmountMessage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvInvalidOrderAmountMessage.setText(Html.fromHtml(InvalidOrderAmountMessage));
        }
    }

    private void SetUpInvalidTimeSlotLayoutAndData() {
        llClosedForDelivery.setVisibility(View.GONE);
        llClosedForDeliveryAtLocation.setVisibility(View.GONE);
        llInvalidCartItems.setVisibility(View.GONE);
        llInvalidCoupon.setVisibility(View.GONE);
        llInvalidTimeSlot.setVisibility(View.VISIBLE);
        llInvalidOrderAmount.setVisibility(View.GONE);
        llInvalidPaymentMethod.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvInvalidTimeSlotMessage.setText(Html.fromHtml(InvalidTimeSlotMessage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvInvalidTimeSlotMessage.setText(Html.fromHtml(InvalidTimeSlotMessage));
        }
    }

    private void SetUpInvalidCouponLayoutAndData() {
        llClosedForDelivery.setVisibility(View.GONE);
        llClosedForDeliveryAtLocation.setVisibility(View.GONE);
        llInvalidCartItems.setVisibility(View.GONE);
        llInvalidCoupon.setVisibility(View.VISIBLE);
        llInvalidTimeSlot.setVisibility(View.GONE);
        llInvalidOrderAmount.setVisibility(View.GONE);
        llInvalidPaymentMethod.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvInvalidCouponMessage.setText(Html.fromHtml(InvalidCouponMessage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvInvalidCouponMessage.setText(Html.fromHtml(InvalidCouponMessage));
        }
    }

    private void SetUpInvalidCartItemsLayoutAndData() {
        llClosedForDelivery.setVisibility(View.GONE);
        llClosedForDeliveryAtLocation.setVisibility(View.GONE);
        llInvalidCartItems.setVisibility(View.VISIBLE);
        llInvalidCoupon.setVisibility(View.GONE);
        llInvalidTimeSlot.setVisibility(View.GONE);
        llInvalidOrderAmount.setVisibility(View.GONE);
        llInvalidPaymentMethod.setVisibility(View.GONE);

        ProductNotAvailableToPlaceOrderAdapter productDetailsImageAdapter = new ProductNotAvailableToPlaceOrderAdapter(listProductNotAvailableToPlaceOrder);
        rVProductsNotAvailable.setAdapter(productDetailsImageAdapter);
        rVProductsNotAvailable.setSelected(true);


        if (InvalidProductsMessage != null && !InvalidProductsMessage.isEmpty() && !InvalidProductsMessage.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvRemoveItemMessage.setText(Html.fromHtml(InvalidProductsMessage, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvRemoveItemMessage.setText(Html.fromHtml(InvalidProductsMessage));
            }
        } else {
            tvRemoveItemMessage.setText("");
        }


        tvRemoveItemMessage = findViewById(R.id.tvRemoveItemMessage);
    }

    private void SetUpClosedForDeliveryAtLocationLayoutAndData() {
        llClosedForDelivery.setVisibility(View.GONE);
        llClosedForDeliveryAtLocation.setVisibility(View.VISIBLE);
        llInvalidCartItems.setVisibility(View.GONE);
        llInvalidCoupon.setVisibility(View.GONE);
        llInvalidTimeSlot.setVisibility(View.GONE);
        llInvalidOrderAmount.setVisibility(View.GONE);
        llInvalidPaymentMethod.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvClosedForDeliveryAtLocationMessage.setText(Html.fromHtml(WeAreClosedForDeliveryAtLocation, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvClosedForDeliveryAtLocationMessage.setText(Html.fromHtml(WeAreClosedForDeliveryAtLocation));
        }
    }

    private void SetUpClosedForDeliveryLayoutAndData() {
        llClosedForDelivery.setVisibility(View.VISIBLE);
        llClosedForDeliveryAtLocation.setVisibility(View.GONE);
        llInvalidCartItems.setVisibility(View.GONE);
        llInvalidCoupon.setVisibility(View.GONE);
        llInvalidTimeSlot.setVisibility(View.GONE);
        llInvalidOrderAmount.setVisibility(View.GONE);
        llInvalidPaymentMethod.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvClosedForDeliveryMessage.setText(Html.fromHtml(ClosedForDeliveryMessage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvClosedForDeliveryMessage.setText(Html.fromHtml(ClosedForDeliveryMessage));
        }
    }

    private void ClearCart() {
        progressDialogHandler1.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CLEAR_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseChangeCartJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(CartErrorsActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CartErrorsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseChangeCartJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                onBackPressed();
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
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRemoveInvalidCartItems:
                if (atClass.isNetworkAvailable(this)) {
                    ClearCart();
                } else {
                    Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnRemoveInvalidCoupon:
                if (atClass.isNetworkAvailable(this)) {
                    RemoveInvalidCoupon();
                } else {
                    Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnRemoveInvalidTimeSlot:
                if (atClass.isNetworkAvailable(this)) {
                    RemoveInvalidTimeSlot();
                } else {
                    Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iVBackClosedForDelivery:
            case R.id.iVBackClosedForDeliveryAtLocation:
            case R.id.ivBackInvalidCartItems:
            case R.id.iVBackInvalidCoupon:
            case R.id.iVBackInvalidTimeSlot:
            case R.id.iVBackInvalidOrderAmount:
            case R.id.iVBackInvalidPaymentMethod:
            case R.id.btnGoBackToCartClosedForDelivery:
            case R.id.btnGoBackToCartClosedForDeliveryAtLocation:
            case R.id.btnGoBackToCartInvalidOrderAmount:
            case R.id.btnGoBackToCartInvalidPaymentMethod:
                onBackPressed();
                break;
        }
    }

    private void RemoveInvalidTimeSlot() {
        progressDialogHandler2.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.REMOVE_DELIVERY_TIME_SLOT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseRemoveTimeSlotJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(CartErrorsActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseRemoveTimeSlotJSON(String response) {
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
                progressDialogHandler2.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void RemoveInvalidCoupon() {
        progressDialogHandler3.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.REMOVE_COUPON_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseRemoveCouponCodeJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler3.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartErrorsActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(CartErrorsActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseRemoveCouponCodeJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler3.showPopupProgressSpinner(false);
                onBackPressed();
            } else if (flag == 3) {
                progressDialogHandler3.showPopupProgressSpinner(false);

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
                progressDialogHandler3.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}