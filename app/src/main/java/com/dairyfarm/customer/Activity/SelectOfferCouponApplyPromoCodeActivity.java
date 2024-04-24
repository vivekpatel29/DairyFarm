package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
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
import com.dairyfarm.customer.Adapter.SelectOfferCouponAdapter;
import com.dairyfarm.customer.Model.SelectOfferCoupon;
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

public class SelectOfferCouponApplyPromoCodeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    RecyclerView rvOfferCoupon;
    ArrayList<SelectOfferCoupon> listSelectOfferCoupon = new ArrayList<>();
    SelectOfferCouponAdapter selectOfferCouponAdapter = new SelectOfferCouponAdapter(listSelectOfferCoupon);
    SelectOfferCoupon selectOfferCoupon;

    LinearLayout llApplyCoupons, llNoInternetConnection, llNoRecordFound;
    Button btnRetry;
    TextView tvMessage;
    String Message;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    public int currentPageValue = 1;

    CustomerSessionManager customerSessionManager;
    LinearLayoutManager linearLayoutManager;

    EditText etCouponCode;
    TextView tvApplyEnabled, tvApplyDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_offer_coupon_apply_promo_code);

        etCouponCode = findViewById(R.id.etCouponCode);
        tvApplyEnabled = findViewById(R.id.tvApplyEnabled);
        tvApplyDisabled = findViewById(R.id.tvApplyDisabled);
        tvApplyEnabled.setOnClickListener(this);

        customerSessionManager = new CustomerSessionManager(this);
        progressDialogHandler = new ProgressDialogHandler(this);
        atClass = new AtClass();

        llApplyCoupons = findViewById(R.id.llApplyCoupons);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);
        llNoRecordFound = findViewById(R.id.llError);

        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        tvMessage = findViewById(R.id.tvMessage);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        rvOfferCoupon = findViewById(R.id.rvOfferCoupon);


        rvOfferCoupon = findViewById(R.id.rvOfferCoupon);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvOfferCoupon.addItemDecoration(itemDecoration);
        linearLayoutManager = new LinearLayoutManager(this);
        rvOfferCoupon.setLayoutManager(linearLayoutManager);
        rvOfferCoupon.setAdapter(selectOfferCouponAdapter);

        setCouponCodeTextChangeListeners();
    }

    private void setCouponCodeTextChangeListeners() {

        etCouponCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (count > 0) {
                    tvApplyEnabled.setVisibility(View.VISIBLE);
                    tvApplyDisabled.setVisibility(View.GONE);
                } else {
                    tvApplyEnabled.setVisibility(View.GONE);
                    tvApplyDisabled.setVisibility(View.VISIBLE);

                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        rvOfferCoupon.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (atClass.isNetworkAvailable(SelectOfferCouponApplyPromoCodeActivity.this)) {
                        currentPageValue++;
                        getOfferCouponsData();
                    } else {
                        llApplyCoupons.setVisibility(View.GONE);
                        llNoRecordFound.setVisibility(View.GONE);
                        llNoInternetConnection.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        if (atClass.isNetworkAvailable(SelectOfferCouponApplyPromoCodeActivity.this)) {
            getOfferCouponsData();
        } else {
            llApplyCoupons.setVisibility(View.GONE);
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

            case R.id.tvApplyEnabled:
                if (atClass.isNetworkAvailable(this)) {
                    Intent intent = new Intent();
                    intent.putExtra("CouponStatus", "2");
                    intent.putExtra("CouponCode", etCouponCode.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(this)) {
                    getOfferCouponsData();
                } else {
                    llApplyCoupons.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setCurrentPageValue() {
        currentPageValue = 1;
    }

    private void getOfferCouponsData() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.OFFER_COUPONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llApplyCoupons.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    llApplyCoupons.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    llApplyCoupons.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    llApplyCoupons.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    llApplyCoupons.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(SelectOfferCouponApplyPromoCodeActivity.this);
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
                    listSelectOfferCoupon.clear();
                }
                progressDialogHandler.showPopupProgressSpinner(false);
                JSONArray arrOfferCoupon = jsonObject.optJSONArray(JsonFields.OFFER_COUPON_RESPONSE_COUPON_ARRAY);
                if (arrOfferCoupon.length() > 0) {
                    for (int i = 0; i < arrOfferCoupon.length(); i++) {
                        JSONObject offerObject = arrOfferCoupon.optJSONObject(i);
                        String coupon_id = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_ID);
                        String coupon_code = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_CODE);
                        String coupon_title = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_TITLE);
                        String coupon_description = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_DESCRIPTION);
                        String coupon_minimum_amount = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_MIN_AMOUNT);
                        String coupon_discount = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_DISCOUNT);
                        String coupon_type = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_TYPE);
                        String coupon_usage_per_person = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_USAGE_PER_PERSON);
                        String coupon_end_date = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_END_DATE);
                        String max_discount_amt = offerObject.optString(JsonFields.OFFER_COUPON_RESPONSE_COUPON_MAX_DISCOUNT_AMOUNT);

                        listSelectOfferCoupon.add(new SelectOfferCoupon(coupon_id, coupon_code, coupon_title, coupon_description, coupon_minimum_amount, coupon_discount, coupon_type, coupon_usage_per_person, coupon_end_date, max_discount_amt));
                    }
                    selectOfferCouponAdapter.notifyDataSetChanged();
                    llApplyCoupons.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                }
            } else if (flag == 2) {
                progressDialogHandler.showPopupProgressSpinner(false);
                llApplyCoupons.setVisibility(View.VISIBLE);
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

                llApplyCoupons.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}