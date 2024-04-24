package com.dairyfarm.customer.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.dairyfarm.customer.Adapter.CartBillDetailsAdapter;
import com.dairyfarm.customer.Adapter.ConfirmOrderAdapter;
import com.dairyfarm.customer.Model.CartBillDetails;
import com.dairyfarm.customer.Model.ConfirmOder;
import com.dairyfarm.customer.Model.ProductNotAvailableToPlaceOrder;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.BottomNavigationMenuSelectedItemIDManager;
import com.dairyfarm.customer.Utils.CustomDividerDecorator;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_THREE_STRING;

public class MyCartActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvConfirmOrder;
    ArrayList<ConfirmOder> listConfirmOrder = new ArrayList<>();
    ConfirmOrderAdapter confirmOrderAdapter = new ConfirmOrderAdapter(listConfirmOrder);
    ConfirmOder confirmOder;
    ImageView ivBack;

    LinearLayout llConfirmOrder, llEmptyCart, llNoInternetConnection;
    TextView tvMessage;
    Button btnRetry;
    String Status;

    ProgressDialogHandler progressDialogHandler1, progressDialogHandler2, progressDialogHandler3, progressDialogHandler4, progressDialogHandler5, progressDialogHandler6, progressDialogHandler7, progressDialogHandler8, progressDialogHandler9, progressDialogHandler10;
    AtClass atClass;
    CustomerSessionManager customerSessionManager;

    String Message;
    String SubTotal, TotalSavings, DeliveryCharges, GrandTotal, TotalQty, CanProceed, HasAddress, AddressID, AddressName, Address, AddressMobileNumber, OfferString, IsCouponApplied, IsPaymentMethodSelected, CouponCode, PaymentMethod, PaymentMethodID, AddressType, AddressLatitude, AddressLongitude, isOrderPaymentOnline, SubmitButtonText, HasWalletPaymentAvailable, WalletAmount, WalletDescription, WalletIsDefault;

    TextView tvOfferText;

    Button btnProceedToPayNotSelected, btnProceedToPaySelected, btnShopMore;

    TextView tvAddressName, tvAddress, tvMobileNumber;
    LinearLayout llNoAddressAddAddress, llDeliveryAddress;
    Button btnAddAddress, btnSelectAddress;
    Button btnNoAddressAddAddress;

    LinearLayout llTimeSlotNotSelected, llTimeSlotSelected;
    TextView tvTimeSlotTitle, tvTimeSlotMessage;

    LinearLayout llPaymentMethodSelected, llPaymentMethodNotSelected;
    TextView tvPaymentMethod;

    ImageView iVRemoveTimeSlot;

    private static final int CHOOSE_ADDRESS_REQUEST = 001;
    private static final int CHOOSE_COUPON_REQUEST = 002;

    String HasTimeSlot, TimeSlotID, TimeSlotTitle, TimeSlotMessage, TotalItemQtyText;

    RecyclerView rvCartBillDetails;

    ArrayList<CartBillDetails> listCartBillDetails = new ArrayList<>();
    CartBillDetailsAdapter cartBillDetailsAdapter = new CartBillDetailsAdapter(listCartBillDetails);

    LinearLayout llCartBillDetails;

    LinearLayout llDeliveryTimeSlot, llDeliveryAddressDetails;

    TextView tvTotalItemQtyText;

    LinearLayout llCouponCodeNotApplied, llAppliedCouponCode;
    TextView tvCouponCode;
    ImageView iVRemoveCouponCode;

    LinearLayout llCoupons, llPaymentMethod;

    TextView tvAddressTypeHome, tvAddressTypeOffice, tvAddressTypeOther;

    TextView tvClearCart;

    //OfferIDManager offerIDManager;

    SwipeRefreshLayout swipeToRefreshLayout;

    LinearLayout llHasWalletPaymentAvailable, llDebitWalletAmount;
    TextView tvWalletAmount, tvWalletDescription;
    ImageView iVUseWalletAmountSelected, iVUseWalletAmountNotSelected;
    String UseWalletAmount;

    BottomNavigationMenuSelectedItemIDManager bottomNavigationMenuSelectedItemIDManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        bottomNavigationMenuSelectedItemIDManager = new BottomNavigationMenuSelectedItemIDManager(this);

        llHasWalletPaymentAvailable = findViewById(R.id.llHasWalletPaymentAvailable);
        llDebitWalletAmount = findViewById(R.id.llDebitWalletAmount);
        llDebitWalletAmount.setOnClickListener(this);
        tvWalletAmount = findViewById(R.id.tvWalletAmount);
        tvWalletDescription = findViewById(R.id.tvWalletDescription);
        iVUseWalletAmountSelected = findViewById(R.id.iVUseWalletAmountSelected);
        iVUseWalletAmountNotSelected = findViewById(R.id.iVUseWalletAmountNotSelected);

        //offerIDManager = new OfferIDManager(this);

        tvAddressTypeHome = findViewById(R.id.tvAddressTypeHome);
        tvAddressTypeOffice = findViewById(R.id.tvAddressTypeOffice);
        tvAddressTypeOther = findViewById(R.id.tvAddressTypeOther);

        tvClearCart = findViewById(R.id.tvClearCart);
        tvClearCart.setOnClickListener(this);

        Intent i = getIntent();

        if (i.hasExtra("status")) {
            Status = i.getStringExtra("status");
        } else {
            Status = "";
        }

        llPaymentMethod = findViewById(R.id.llPaymentMethod);

        llCoupons = findViewById(R.id.llCoupons);
        llCouponCodeNotApplied = findViewById(R.id.llCouponCodeNotApplied);
        llCouponCodeNotApplied.setOnClickListener(this);
        llAppliedCouponCode = findViewById(R.id.llAppliedCouponCode);
        tvCouponCode = findViewById(R.id.tvCouponCode);
        iVRemoveCouponCode = findViewById(R.id.iVRemoveCouponCode);
        iVRemoveCouponCode.setOnClickListener(this);

        tvTotalItemQtyText = findViewById(R.id.tvTotalItemQtyText);
        llDeliveryAddressDetails = findViewById(R.id.llDeliveryAddressDetails);
        llDeliveryTimeSlot = findViewById(R.id.llDeliveryTimeSlot);

        rvCartBillDetails = findViewById(R.id.rvCartBillDetails);
        llCartBillDetails = findViewById(R.id.llCartBillDetails);

        atClass = new AtClass();
        progressDialogHandler1 = new ProgressDialogHandler(this);
        progressDialogHandler2 = new ProgressDialogHandler(this);
        progressDialogHandler3 = new ProgressDialogHandler(this);
        progressDialogHandler4 = new ProgressDialogHandler(this);
        progressDialogHandler5 = new ProgressDialogHandler(this);
        progressDialogHandler6 = new ProgressDialogHandler(this);
        progressDialogHandler7 = new ProgressDialogHandler(this);
        progressDialogHandler8 = new ProgressDialogHandler(this);
        progressDialogHandler9 = new ProgressDialogHandler(this);
        progressDialogHandler10 = new ProgressDialogHandler(this);
        customerSessionManager = new CustomerSessionManager(this);

        llTimeSlotNotSelected = findViewById(R.id.llTimeSlotNotSelected);
        llTimeSlotSelected = findViewById(R.id.llTimeSlotSelected);

        llPaymentMethodSelected = findViewById(R.id.llPaymentMethodSelected);
        llPaymentMethodNotSelected = findViewById(R.id.llPaymentMethodNotSelected);

        llPaymentMethodSelected.setOnClickListener(this);
        llPaymentMethodNotSelected.setOnClickListener(this);

        tvTimeSlotTitle = findViewById(R.id.tvTimeSlotTitle);
        tvTimeSlotMessage = findViewById(R.id.tvTimeSlotMessage);

        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);

        iVRemoveTimeSlot = findViewById(R.id.iVRemoveTimeSlot);
        iVRemoveTimeSlot.setOnClickListener(this);

        llTimeSlotNotSelected.setOnClickListener(this);


        tvOfferText = findViewById(R.id.tvOfferText);

        llNoAddressAddAddress = findViewById(R.id.llNoAddressAddAddress);
        llDeliveryAddress = findViewById(R.id.llDeliveryAddress);

        tvAddressName = findViewById(R.id.tvAddressName);
        tvAddress = findViewById(R.id.tvAddress);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);

        llConfirmOrder = findViewById(R.id.llConfirmOrder);
        llEmptyCart = findViewById(R.id.llEmptyCart);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        tvMessage = findViewById(R.id.tvMessage);

        btnShopMore = findViewById(R.id.btnShopMore);
        btnShopMore.setOnClickListener(this);

        llConfirmOrder = findViewById(R.id.llConfirmOrder);
        llEmptyCart = findViewById(R.id.llEmptyCart);

        btnProceedToPayNotSelected = findViewById(R.id.btnProceedToPayNotSelected);
        btnProceedToPaySelected = findViewById(R.id.btnProceedToPaySelected);

        btnProceedToPayNotSelected.setOnClickListener(this);
        btnProceedToPaySelected.setOnClickListener(this);

        ivBack = findViewById(R.id.ivBack);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnNoAddressAddAddress = findViewById(R.id.btnNoAddressAddAddress);
        btnSelectAddress = findViewById(R.id.btnSelectAddress);
        btnAddAddress.setOnClickListener(this);
        btnSelectAddress.setOnClickListener(this);
        btnNoAddressAddAddress.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        rvConfirmOrder = findViewById(R.id.rvConfirmOrder);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvConfirmOrder.addItemDecoration(itemDecoration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvConfirmOrder.setLayoutManager(linearLayoutManager);
        rvConfirmOrder.setAdapter(confirmOrderAdapter);

        rvCartBillDetails = findViewById(R.id.rvCartBillDetails);
        rvCartBillDetails.setNestedScrollingEnabled(false);
        rvCartBillDetails.addItemDecoration(new CustomDividerDecorator(this, "2"));
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        rvCartBillDetails.setLayoutManager(linearLayoutManager2);
        rvCartBillDetails.setAdapter(cartBillDetailsAdapter);

        setUpSwipeToRefreshLayout();
    }

    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                Intent i = new Intent(MyCartActivity.this, MyCartActivity.class);
                i.putExtra("status", Status);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartDetails();
    }

    public void getCartDetails() {
        progressDialogHandler1.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llConfirmOrder.setVisibility(View.GONE);
                    llEmptyCart.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    llConfirmOrder.setVisibility(View.GONE);
                    llEmptyCart.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    llConfirmOrder.setVisibility(View.GONE);
                    llEmptyCart.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    llConfirmOrder.setVisibility(View.GONE);
                    llEmptyCart.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    llConfirmOrder.setVisibility(View.GONE);
                    llEmptyCart.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
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
                progressDialogHandler1.showPopupProgressSpinner(false);

                SubTotal = jsonObject.optString(JsonFields.CART_RESPONSE_SUB_TOTAL);
                TotalSavings = jsonObject.optString(JsonFields.CART_RESPONSE_TOTAL_SAVINGS);
                DeliveryCharges = jsonObject.optString(JsonFields.CART_RESPONSE_DELIVERY_CHARGE);
                GrandTotal = jsonObject.optString(JsonFields.CART_RESPONSE_GRAND_TOTAL);
                TotalQty = jsonObject.optString(JsonFields.CART_RESPONSE_TOTAL_QTY);
                CanProceed = jsonObject.optString(JsonFields.CART_RESPONSE_CAN_PROCEED);
                HasAddress = jsonObject.optString(JsonFields.CART_RESPONSE_HAS_ADDRESS);
                HasTimeSlot = jsonObject.optString(JsonFields.CART_RESPONSE_HAS_TIME_SLOT);
                TotalItemQtyText = jsonObject.optString(JsonFields.CART_RESPONSE_TOTAL_ITEM_QTY_TEXT);
                IsCouponApplied = jsonObject.optString(JsonFields.CART_RESPONSE_IS_COUPON_APPLIED);
                OfferString = jsonObject.optString(JsonFields.CART_RESPONSE_COUPON_STRING);
                CouponCode = jsonObject.optString(JsonFields.CART_RESPONSE_COUPON_CODE);
                PaymentMethod = jsonObject.optString(JsonFields.CART_RESPONSE_PAYMENT_METHOD);
                PaymentMethodID = jsonObject.optString(JsonFields.CART_RESPONSE_PAYMENT_METHOD_ID);
                IsPaymentMethodSelected = jsonObject.optString(JsonFields.CART_RESPONSE_IS_PAYMENT_METHOD_SELECTED);
                SubmitButtonText = jsonObject.optString(JsonFields.CART_RESPONSE_SUBMIT_BUTTON_TEXT);
                isOrderPaymentOnline = jsonObject.optString(JsonFields.CART_RESPONSE_IS_ORDER_PAYMENT_ONLINE);

                HasWalletPaymentAvailable = jsonObject.optString(JsonFields.CART_RESPONSE_HAS_WALLET_PAYMENT_AVAILABLE);
                WalletAmount = jsonObject.optString(JsonFields.CART_RESPONSE_WALLET_AMOUNT);
                WalletDescription = jsonObject.optString(JsonFields.CART_RESPONSE_WALLET_DESCRIPTION);
                WalletIsDefault = jsonObject.optString(JsonFields.CART_RESPONSE_WALLET_IS_DEFAULT);

                if (HasAddress.equals("1")) {
                    JSONArray arrAddress = jsonObject.optJSONArray(JsonFields.CART_RESPONSE_ADDRESS_ARRAY);
                    for (int i = 0; i < arrAddress.length(); i++) {
                        JSONObject addressObject = arrAddress.optJSONObject(i);
                        AddressID = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_ID);
                        AddressName = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_NAME);
                        Address = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_LINE);
                        AddressMobileNumber = addressObject.optString(JsonFields.CART_RESPONSE_MOBILE_NUMBER);
                        AddressType = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_TYPE);
                        AddressLatitude = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_LATITUDE);
                        AddressLongitude = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_LONGITUDE);
                    }
                }

                if (HasTimeSlot.equals("1")) {
                    JSONArray arrTimeSlot = jsonObject.optJSONArray(JsonFields.CART_RESPONSE_TIME_SLOT_ARRAY);
                    for (int j = 0; j < arrTimeSlot.length(); j++) {
                        JSONObject timeSlotObject = arrTimeSlot.optJSONObject(j);
                        TimeSlotID = timeSlotObject.optString(JsonFields.CART_RESPONSE_TIME_SLOT_ID);
                        TimeSlotTitle = timeSlotObject.optString(JsonFields.CART_RESPONSE_TIME_SLOT_TITLE);
                        TimeSlotMessage = timeSlotObject.optString(JsonFields.CART_RESPONSE_TIME_SLOT_MESSAGE);
                    }
                }

                listConfirmOrder.clear();
                JSONArray arrCart = jsonObject.optJSONArray(JsonFields.CART_RESPONSE_CART_ARRAY);
                if (arrCart.length() > 0) {
                    for (int k = 0; k < arrCart.length(); k++) {
                        JSONObject cartObject = arrCart.optJSONObject(k);
                        String cart_id = cartObject.optString(JsonFields.CART_RESPONSE_CART_ID);
                        String pro_title = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_TITLE);
                        String pro_name = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_NAME);
                        String atr_name = cartObject.optString(JsonFields.CART_RESPONSE_ATTRIBUTE_NAME);
                        String market_price = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_MARKET_PRICE);
                        String selling_price = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_SELLING_PRICE);
                        String saving_price = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_SAVING_PRICE);
                        String saving_per = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_SAVING_PERCENTAGE);
                        String pro_qty = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_QTY);
                        String has_offer = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_HAS_OFFER);
                        String total = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_TOTAL);
                        String img_path = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_IMAGE_PATH);
                        String thumb_path = cartObject.optString(JsonFields.CART_RESPONSE_PRODUCT_THUMB_PATH);
                        String total_market_price = cartObject.optString(JsonFields.CART_RESPONSE_TOTAL_MARKET_PRICE);
                        String total_selling_price = cartObject.optString(JsonFields.CART_RESPONSE_TOTAL_SELLING_PRICE);
                        String total_saving_percentage = cartObject.optString(JsonFields.CART_RESPONSE_TOTAL_SAVING_PERCENTAGE);
                        String offer_id = cartObject.optString(JsonFields.CART_RESPONSE_TOTAL_OFFER_ID);

                        listConfirmOrder.add(new ConfirmOder(cart_id, pro_title, pro_name, atr_name, market_price, selling_price, saving_price, saving_per, pro_qty, has_offer, total, img_path, thumb_path, total_market_price, total_selling_price, total_saving_percentage, offer_id));
                    }

                    JSONArray arrCartBillDetails = jsonObject.optJSONArray(JsonFields.CART_RESPONSE_CART_BILL_DETAILS_ARRAY);
                    listCartBillDetails.clear();
                    for (int l = 0; l < arrCartBillDetails.length(); l++) {
                        JSONObject addressObject = arrCartBillDetails.optJSONObject(l);
                        String Title = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_BILL_DETAILS_TITLE);
                        String Value = addressObject.optString(JsonFields.CART_RESPONSE_ADDRESS_BILL_DETAILS_VALUE);

                        if (Value != null && !Value.isEmpty() && !Value.equals("")) {
                            listCartBillDetails.add(new CartBillDetails(Title, Value));
                        }
                    }
                    setData();

                    confirmOrderAdapter.notifyDataSetChanged();
                    cartBillDetailsAdapter.notifyDataSetChanged();

                    llConfirmOrder.setVisibility(View.VISIBLE);
                    llEmptyCart.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                    tvClearCart.setVisibility(View.VISIBLE);
                }
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
                llConfirmOrder.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llEmptyCart.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
                tvClearCart.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        //CanProceed,HasAddress;
        if (OfferString != null && !OfferString.isEmpty() && !OfferString.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvOfferText.setText(Html.fromHtml(OfferString, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvOfferText.setText(Html.fromHtml(OfferString));
            }
            tvOfferText.setVisibility(View.VISIBLE);
        } else {
            tvOfferText.setVisibility(View.GONE);
        }

        if (TotalItemQtyText != null && !TotalItemQtyText.isEmpty() && !TotalItemQtyText.equals("")) {
            tvTotalItemQtyText.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvTotalItemQtyText.setText(Html.fromHtml(TotalItemQtyText, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvTotalItemQtyText.setText(Html.fromHtml(TotalItemQtyText));
            }
        } else {
            tvTotalItemQtyText.setVisibility(View.GONE);
        }


        llCoupons.setVisibility(View.VISIBLE);
        if (IsCouponApplied != null && !IsCouponApplied.isEmpty() && !IsCouponApplied.equals("")) {
            if (IsCouponApplied.equals("1")) {

                if (CouponCode != null && !CouponCode.isEmpty() && !CouponCode.equals("")) {
                    llCouponCodeNotApplied.setVisibility(View.GONE);
                    llAppliedCouponCode.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvCouponCode.setText(Html.fromHtml(CouponCode, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvCouponCode.setText(Html.fromHtml(CouponCode));
                    }
                } else {
                    llCouponCodeNotApplied.setVisibility(View.VISIBLE);
                    llAppliedCouponCode.setVisibility(View.GONE);
                }


            } else if (IsCouponApplied.equals("0")) {
                llCouponCodeNotApplied.setVisibility(View.VISIBLE);
                llAppliedCouponCode.setVisibility(View.GONE);
            } else {
                llCouponCodeNotApplied.setVisibility(View.VISIBLE);
                llAppliedCouponCode.setVisibility(View.GONE);
            }
        } else {
            llCouponCodeNotApplied.setVisibility(View.VISIBLE);
            llAppliedCouponCode.setVisibility(View.GONE);
        }


        llPaymentMethod.setVisibility(View.VISIBLE);
        if (IsPaymentMethodSelected != null && !IsPaymentMethodSelected.isEmpty() && !IsPaymentMethodSelected.equals("")) {
            if (IsPaymentMethodSelected.equals("1")) {
                if (PaymentMethod != null && !PaymentMethod.isEmpty() && !PaymentMethod.equals("")) {
                    llPaymentMethodNotSelected.setVisibility(View.GONE);
                    llPaymentMethodSelected.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvPaymentMethod.setText(Html.fromHtml(PaymentMethod, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvPaymentMethod.setText(Html.fromHtml(PaymentMethod));
                    }
                } else {
                    llPaymentMethodNotSelected.setVisibility(View.VISIBLE);
                    llPaymentMethodSelected.setVisibility(View.GONE);
                }
            } else if (IsPaymentMethodSelected.equals("0")) {
                llPaymentMethodNotSelected.setVisibility(View.VISIBLE);
                llPaymentMethodSelected.setVisibility(View.GONE);
            } else {
                llPaymentMethodNotSelected.setVisibility(View.VISIBLE);
                llPaymentMethodSelected.setVisibility(View.GONE);
            }
        } else {
            llPaymentMethodNotSelected.setVisibility(View.VISIBLE);
            llPaymentMethodSelected.setVisibility(View.GONE);
        }

        if (listCartBillDetails != null && !listCartBillDetails.isEmpty() && listCartBillDetails.size() > 0) {
            llCartBillDetails.setVisibility(View.VISIBLE);
        } else {
            llCartBillDetails.setVisibility(View.GONE);
        }

        if (SubmitButtonText != null && !SubmitButtonText.isEmpty() && !SubmitButtonText.equals("")) {
            btnProceedToPaySelected.setText("");
            btnProceedToPayNotSelected.setText("");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                btnProceedToPayNotSelected.setText(Html.fromHtml(SubmitButtonText, Html.FROM_HTML_MODE_COMPACT));
            } else {
                btnProceedToPayNotSelected.setText(Html.fromHtml(SubmitButtonText));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                btnProceedToPaySelected.setText(Html.fromHtml(SubmitButtonText, Html.FROM_HTML_MODE_COMPACT));
            } else {
                btnProceedToPaySelected.setText(Html.fromHtml(SubmitButtonText));
            }
        }

        if (CanProceed != null && !CanProceed.isEmpty() && !CanProceed.equals("")) {
            if (CanProceed.equals("1")) {
                btnProceedToPayNotSelected.setVisibility(View.GONE);
                btnProceedToPaySelected.setVisibility(View.VISIBLE);
            } else {
                btnProceedToPayNotSelected.setVisibility(View.VISIBLE);
                btnProceedToPaySelected.setVisibility(View.GONE);
            }
        } else {
            btnProceedToPayNotSelected.setVisibility(View.VISIBLE);
            btnProceedToPaySelected.setVisibility(View.GONE);
        }

        llDeliveryTimeSlot.setVisibility(View.VISIBLE);
        if (HasTimeSlot != null && !HasTimeSlot.isEmpty() && !HasTimeSlot.equals("")) {
            if (HasTimeSlot.equals("1")) {
                llTimeSlotSelected.setVisibility(View.VISIBLE);
                llTimeSlotNotSelected.setVisibility(View.GONE);

                if (TimeSlotTitle != null && !TimeSlotTitle.isEmpty() && !TimeSlotTitle.equals("")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvTimeSlotTitle.setText(Html.fromHtml(TimeSlotTitle, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvTimeSlotTitle.setText(Html.fromHtml(TimeSlotTitle));
                    }
                    tvTimeSlotTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTimeSlotTitle.setVisibility(View.GONE);
                }


                if (TimeSlotMessage != null && !TimeSlotMessage.isEmpty() && !TimeSlotMessage.equals("")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvTimeSlotMessage.setText(Html.fromHtml(TimeSlotMessage, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvTimeSlotMessage.setText(Html.fromHtml(TimeSlotMessage));
                    }
                    tvTimeSlotMessage.setVisibility(View.VISIBLE);
                } else {
                    tvTimeSlotMessage.setVisibility(View.GONE);
                }

            } else {
                llTimeSlotSelected.setVisibility(View.GONE);
                llTimeSlotNotSelected.setVisibility(View.VISIBLE);
            }
        } else {
            llTimeSlotNotSelected.setVisibility(View.VISIBLE);
            llTimeSlotSelected.setVisibility(View.GONE);
        }


        llDeliveryAddressDetails.setVisibility(View.VISIBLE);
        if (HasAddress != null && !HasAddress.isEmpty() && !HasAddress.equals("")) {
            if (HasAddress.equals("1")) {
                llNoAddressAddAddress.setVisibility(View.GONE);
                llDeliveryAddress.setVisibility(View.VISIBLE);

                if (AddressName != null && !AddressName.isEmpty() && !AddressName.equals("")) {
                    tvAddressName.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvAddressName.setText(Html.fromHtml(AddressName, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvAddressName.setText(Html.fromHtml(AddressName));
                    }
                } else {
                    tvAddressName.setVisibility(View.GONE);
                }

                if (Address != null && !Address.isEmpty() && !Address.equals("")) {
                    tvAddress.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvAddress.setText(Html.fromHtml(Address, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvAddress.setText(Html.fromHtml(Address));
                    }

                } else {
                    tvAddress.setVisibility(View.GONE);
                }

                if (AddressMobileNumber != null && !AddressMobileNumber.isEmpty() && !AddressMobileNumber.equals("")) {
                    tvMobileNumber.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvMobileNumber.setText(Html.fromHtml(AddressMobileNumber, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvMobileNumber.setText(Html.fromHtml(AddressMobileNumber));
                    }
                } else {
                    tvMobileNumber.setVisibility(View.GONE);
                }

                if (AddressType != null && !AddressType.isEmpty() && !AddressType.equals("")) {
                    if (AddressType != null && !AddressType.isEmpty() && !AddressType.equals("")) {
                        if (AddressType.equalsIgnoreCase("Home")) {
                            tvAddressTypeHome.setVisibility(View.VISIBLE);
                            tvAddressTypeOffice.setVisibility(View.GONE);
                            tvAddressTypeOther.setVisibility(View.GONE);
                        } else if (AddressType.equalsIgnoreCase("Office")) {
                            tvAddressTypeHome.setVisibility(View.GONE);
                            tvAddressTypeOffice.setVisibility(View.VISIBLE);
                            tvAddressTypeOther.setVisibility(View.GONE);
                        } else if (AddressType.equalsIgnoreCase("Other")) {
                            tvAddressTypeHome.setVisibility(View.GONE);
                            tvAddressTypeOffice.setVisibility(View.GONE);
                            tvAddressTypeOther.setVisibility(View.VISIBLE);
                        } else {
                            tvAddressTypeHome.setVisibility(View.GONE);
                            tvAddressTypeOffice.setVisibility(View.GONE);
                            tvAddressTypeOther.setVisibility(View.GONE);
                        }
                    } else {
                        tvAddressTypeHome.setVisibility(View.GONE);
                        tvAddressTypeOffice.setVisibility(View.GONE);
                        tvAddressTypeOther.setVisibility(View.GONE);
                    }
                } else {
                    tvAddressTypeHome.setVisibility(View.GONE);
                    tvAddressTypeOffice.setVisibility(View.GONE);
                    tvAddressTypeOther.setVisibility(View.GONE);
                }
            } else {
                llNoAddressAddAddress.setVisibility(View.VISIBLE);
                llDeliveryAddress.setVisibility(View.GONE);
            }
        } else {
            llNoAddressAddAddress.setVisibility(View.VISIBLE);
            llDeliveryAddress.setVisibility(View.GONE);
        }

        if (HasWalletPaymentAvailable != null && !HasWalletPaymentAvailable.isEmpty() && !HasWalletPaymentAvailable.equals("")) {
            if (HasWalletPaymentAvailable.equals("1")) {
                llHasWalletPaymentAvailable.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvWalletAmount.setText(Html.fromHtml(WalletAmount, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvWalletAmount.setText(Html.fromHtml(WalletAmount));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvWalletDescription.setText(Html.fromHtml(WalletDescription, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvWalletDescription.setText(Html.fromHtml(WalletDescription));
                }


                if (WalletIsDefault != null && !WalletIsDefault.isEmpty() && !WalletIsDefault.equals("")) {
                    if (WalletIsDefault.equals("1")) {
                        iVUseWalletAmountSelected.setVisibility(View.VISIBLE);
                        iVUseWalletAmountNotSelected.setVisibility(View.GONE);
                    } else if (WalletIsDefault.equals("0")) {
                        iVUseWalletAmountSelected.setVisibility(View.GONE);
                        iVUseWalletAmountNotSelected.setVisibility(View.VISIBLE);
                    } else {
                        iVUseWalletAmountSelected.setVisibility(View.GONE);
                        iVUseWalletAmountNotSelected.setVisibility(View.GONE);
                    }
                } else {
                    iVUseWalletAmountSelected.setVisibility(View.GONE);
                    iVUseWalletAmountNotSelected.setVisibility(View.GONE);
                }
            } else if (HasWalletPaymentAvailable.equals("0")) {
                llHasWalletPaymentAvailable.setVisibility(View.GONE);
            } else {
                llHasWalletPaymentAvailable.setVisibility(View.GONE);
            }
        } else {
            llHasWalletPaymentAvailable.setVisibility(View.GONE);
        }
    }

    private void ChangeAddress(final String AddressID) {
        progressDialogHandler2.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SELECT_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseChangeAddressJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.CHANGE_CART_REQUEST_PARAMS_ADDRESS_ID, AddressID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseChangeAddressJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler2.showPopupProgressSpinner(false);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        //getCartDetails();
                    }
                }, 100);
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

    private void CheckCart() {
        progressDialogHandler3.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CHECK_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseCheckCartJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler3.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseCheckCartJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);
            progressDialogHandler3.showPopupProgressSpinner(false);
            if (flag == 1) {
                //Reconfirm Address place order
                //ConfirmOrderDeliveryAddress();

                if (atClass.isNetworkAvailable(this)) {
                    if (isOrderPaymentOnline != null && !isOrderPaymentOnline.isEmpty() && !isOrderPaymentOnline.equals("")) {
                        if (isOrderPaymentOnline.equals("1")) {
                            ProcessOrder();
                        } else {
                            PlaceOrder();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else if (flag == 2) {
                //We are closed for delivery
                Intent i2 = new Intent(this, CartErrorsActivity.class);
                i2.putExtra("flag", String.valueOf(flag));
                i2.putExtra("Message", Message);
                startActivity(i2);
            } else if (flag == 3) {
                //Logout
                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i3 = new Intent(this, LogoutActivity.class);
                i3.putExtra("Title", LogoutTitle);
                i3.putExtra("Message", LogoutMessage);
                i3.putExtra("ImageURL", LogoutIcon);
                startActivity(i3);
            } else if (flag == 4) {
                //We are closed for certain location
                Intent i4 = new Intent(this, CartErrorsActivity.class);
                i4.putExtra("flag", String.valueOf(flag));
                i4.putExtra("Message", Message);
                startActivity(i4);
            } else if (flag == 5) {
                //Some products not available
                ArrayList<ProductNotAvailableToPlaceOrder> listProductNotAvailableToPlaceOrder = new ArrayList<>();

                JSONArray arrProductsNotAvailable = jsonObject.optJSONArray(JsonFields.PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_ARRAY);
                if (arrProductsNotAvailable.length() > 0) {
                    for (int i = 0; i < arrProductsNotAvailable.length(); i++) {
                        JSONObject productNotAvailableObject = arrProductsNotAvailable.optJSONObject(i);
                        String cart_id = productNotAvailableObject.optString(JsonFields.PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_CART_ID);
                        String product_name = productNotAvailableObject.optString(JsonFields.PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_NAME);
                        String product_id = productNotAvailableObject.optString(JsonFields.PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_ID);
                        String product_atr_id = productNotAvailableObject.optString(JsonFields.PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_ATR);
                        String img_path = productNotAvailableObject.optString(JsonFields.PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_IMAGE_PATH);
                        String available_string = productNotAvailableObject.optString(JsonFields.PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_AVAILABLE_STRING);

                        listProductNotAvailableToPlaceOrder.add(new ProductNotAvailableToPlaceOrder(cart_id, product_id, product_atr_id, product_name, img_path, available_string));
                    }

                    Intent i5 = new Intent(this, CartErrorsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("alProductNotAvailableToPlaceOrder", listProductNotAvailableToPlaceOrder);
                    bundle.putString("flag", String.valueOf(flag));
                    bundle.putString("Message", Message);
                    i5.putExtras(bundle);
                    i5.putExtra("flag", String.valueOf(flag));
                    startActivity(i5);
                }
            } else if (flag == 6) {
                //Coupon Code Not Valid
                Intent i6 = new Intent(this, CartErrorsActivity.class);
                i6.putExtra("Message", Message);
                i6.putExtra("flag", String.valueOf(flag));
                startActivity(i6);
            } else if (flag == 7) {
                //Time Slot Not Valid
                String TimeSlotID = jsonObject.optString(JsonFields.TIME_SLOT_NOT_VALID_TIME_SLOT_ID);

                Intent i7 = new Intent(this, CartErrorsActivity.class);
                i7.putExtra("Message", Message);
                i7.putExtra("TimeSlotID", TimeSlotID);
                i7.putExtra("flag", String.valueOf(flag));
                startActivity(i7);
            } else if (flag == 8) {
                //Order Amount Not Valid
                Intent i8 = new Intent(this, CartErrorsActivity.class);
                i8.putExtra("Message", Message);
                i8.putExtra("flag", String.valueOf(flag));
                startActivity(i8);
            } else if (flag == 9) {
                //Payment Method Not Valid
                Intent i9 = new Intent(this, CartErrorsActivity.class);
                i9.putExtra("Message", Message);
                i9.putExtra("flag", String.valueOf(flag));
                startActivity(i9);
            } else {
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ProcessOrder() {
        progressDialogHandler9.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROCESS_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseProcessOrderJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler9.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_PAYMENT_METHOD_ID, PaymentMethodID);
                //params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_PAYMENT_METHOD, "cod");
                params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_ADDRESS_ID, AddressID);
                params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_TIME_SLOT_ID, TimeSlotID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseProcessOrderJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler9.showPopupProgressSpinner(false);
                String AccessCode = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_ACCESS_CODE);
                String MerchantID = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_MERCHANT_ID);
                String OrderID = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_ORDER_ID);
                String RedirectURL = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_REDIRECT_URL);
                String CancelURL = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_CANCEL_URL);
                String RSAURL = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_RSA_KEY_URL);
                String TransactionURL = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_TRANSACTION_URL);
                String PaymentDetailsID = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_PAYMENT_DETAILS_ID);
                String Currency = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_PAYMENT_CURRENCY);
                String Amount = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_PAYMENT_AMOUNT);
                String BillingCountry = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_BILLING_COUNTRY);
                String BillingTelephone = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_BILLING_TELEPHONE);
                String BillingEmail = jsonObject.optString(JsonFields.ORDER_PAYMENT_PROCESS_BILLING_EMAIL);
                //String ApiPaymentSuccessURL = jsonObject.optString(AvenuesParams.API_SUCCESS_PAYMENT_URL);
                //String ApiPaymentFailURL = jsonObject.optString(AvenuesParams.API_CANCEL_PAYMENT_URL);

                if (atClass.isNetworkAvailable(this)) {
//                    Intent intent = new Intent(MyCartActivity.this, CCAvenuePaymentActivity.class);
//                    intent.putExtra(AvenuesParams.IS_FROM, "1");
//                    intent.putExtra(AvenuesParams.ACCESS_CODE, AccessCode);
//                    intent.putExtra(AvenuesParams.MERCHANT_ID, MerchantID);
//                    intent.putExtra(AvenuesParams.ORDER_ID, OrderID);
//                    intent.putExtra(AvenuesParams.CURRENCY, Currency);
//                    intent.putExtra(AvenuesParams.AMOUNT, Amount);
//                    intent.putExtra(AvenuesParams.REDIRECT_URL, RedirectURL);
//                    intent.putExtra(AvenuesParams.CANCEL_URL, CancelURL);
//                    intent.putExtra(AvenuesParams.RSA_KEY_URL, RSAURL);
//                    intent.putExtra(AvenuesParams.TRANSACTION_URL, TransactionURL);
//                    intent.putExtra(AvenuesParams.PAYMENT_DETAILS_ID, PaymentDetailsID);
//                    intent.putExtra(AvenuesParams.BILLING_COUNTRY, BillingCountry);
//                    intent.putExtra(AvenuesParams.BILLING_TELEPHONE, BillingTelephone);
//                    intent.putExtra(AvenuesParams.BILLING_EMAIL, BillingEmail);
//                    intent.putExtra(AvenuesParams.API_SUCCESS_PAYMENT_URL, ApiPaymentSuccessURL);
//                    intent.putExtra(AvenuesParams.API_CANCEL_PAYMENT_URL, ApiPaymentFailURL);
//                    startActivity(intent);

                    //intent.putExtra(AvenuesParams.AMOUNT, "1");
                    //intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else if (flag == 3) {
                progressDialogHandler9.showPopupProgressSpinner(false);

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
                progressDialogHandler9.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void PlaceOrder() {
        progressDialogHandler4.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PROCESS_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseOrderPlaceJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler4.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_PAYMENT_METHOD_ID, PaymentMethodID);
                //params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_PAYMENT_METHOD, "cod");
                params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_ADDRESS_ID, AddressID);
                params.put(JsonFields.PLACE_ORDER_REQUEST_PARAMS_TIME_SLOT_ID, TimeSlotID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseOrderPlaceJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler4.showPopupProgressSpinner(false);
                //offerIDManager.RemoveOfferID();
                Intent i = new Intent(this, OrderPlacedSuccessActivity.class);
                i.putExtra("Message", Message);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else if (flag == 3) {
                progressDialogHandler4.showPopupProgressSpinner(false);

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
                progressDialogHandler4.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llDebitWalletAmount:
                if (atClass.isNetworkAvailable(this)) {
                    ChangeWalletPaymentSelectionStatus();
                } else {
                    llConfirmOrder.setVisibility(View.GONE);
                    llEmptyCart.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.btnAddAddress:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i1 = new Intent(this, AddAddressActivity.class);
                    i1.putExtra("isFrom", "2");
                    startActivity(i1);
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnSelectAddress:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i2 = new Intent(this, SelectAddressActivity.class);
                    i2.putExtra("AddressID", AddressID);
                    startActivityForResult(i2, CHOOSE_ADDRESS_REQUEST);
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnNoAddressAddAddress:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i3 = new Intent(this, AddAddressActivity.class);
                    i3.putExtra("isFrom", "1");
                    startActivity(i3);
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.llPaymentMethodNotSelected:
            case R.id.llPaymentMethodSelected:
                Intent i3 = new Intent(this, PaymentsActivity.class);
                //i3.setFlags(i3.FLAG_ACTIVITY_CLEAR_TOP|i3.FLAG_ACTIVITY_CLEAR_TASK|i3.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i3);
                break;

            case R.id.btnProceedToPaySelected:
                if (atClass.isNetworkAvailable(this)) {
                    CheckCart();
                } else {
                    llConfirmOrder.setVisibility(View.GONE);
                    llEmptyCart.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                }
/*              Intent i4 = new Intent(this,SelectPaymentMethodActivity.class);
                startActivity(i4);
*/
                break;

            case R.id.btnShopMore:
                Intent i5 = new Intent(this, HomeActivity.class);
                i5.setFlags(i5.FLAG_ACTIVITY_CLEAR_TOP | i5.FLAG_ACTIVITY_CLEAR_TASK | i5.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i5);
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i6 = new Intent(this, MyCartActivity.class);
                    i6.putExtra("status", Status);
                    startActivity(i6);
                    finish();
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEmptyCart.setVisibility(View.GONE);
                    llConfirmOrder.setVisibility(View.GONE);
                }
                break;

            case R.id.llTimeSlotNotSelected:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i7 = new Intent(this, SelectTimeSlotActivity.class);
                    //i7.setFlags(i7.FLAG_ACTIVITY_CLEAR_TOP|i7.FLAG_ACTIVITY_CLEAR_TASK|i7.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i7);
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iVRemoveTimeSlot:
                if (atClass.isNetworkAvailable(this)) {
                    if (checkTimeSlotID()) {
                        RemoveTimeSlot();
                    }
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iVRemoveCouponCode:
                if (atClass.isNetworkAvailable(this)) {
                    RemoveCouponCode();
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.llCouponCodeNotApplied:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i8 = new Intent(this, SelectOfferCouponApplyPromoCodeActivity.class);
                    startActivityForResult(i8, CHOOSE_COUPON_REQUEST);
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvClearCart:
                if (atClass.isNetworkAvailable(this)) {
                    ClearAllCartItems();
                } else {
                    Toast.makeText(atClass, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ChangeWalletPaymentSelectionStatus() {
        progressDialogHandler10.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CHANGE_WALLET_STATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseChangeWalletSelectionJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler10.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseChangeWalletSelectionJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler10.showPopupProgressSpinner(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCartDetails();
                    }
                }, 100);
            } else if (flag == 3) {
                progressDialogHandler10.showPopupProgressSpinner(false);

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
                progressDialogHandler10.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ClearAllCartItems() {
        progressDialogHandler8.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CLEAR_ALL_CART_ITEMS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseChangeCartJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler8.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyCartActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseChangeCartJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler8.showPopupProgressSpinner(false);
                Intent i = new Intent(MyCartActivity.this, SuccessMessageActivity.class);
                i.putExtra("Message", Message);
                i.putExtra("FromScreenName", "MyCartActivity");
                i.putExtra("ToScreenName", "MyCartActivity");
                startActivity(i);
                finish();
            } else if (flag == 3) {
                progressDialogHandler8.showPopupProgressSpinner(false);

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
                progressDialogHandler8.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void RemoveTimeSlot() {
        progressDialogHandler5.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.REMOVE_DELIVERY_TIME_SLOT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseRemoveTimeSlotJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler5.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
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
                progressDialogHandler5.showPopupProgressSpinner(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCartDetails();
                    }
                }, 100);
            } else if (flag == 3) {
                progressDialogHandler5.showPopupProgressSpinner(false);

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
                progressDialogHandler5.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkTimeSlotID() {
        boolean isTimeSlotValid = false;

        if (TimeSlotID != null && !TimeSlotID.equals("") && !TimeSlotID.isEmpty()) {
            isTimeSlotValid = true;
        } else {
            isTimeSlotValid = false;
            Toast.makeText(MyCartActivity.this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
        }
        return isTimeSlotValid;
    }


    private void RemoveCouponCode() {
        progressDialogHandler6.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.REMOVE_COUPON_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseRemoveCouponCodeJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler6.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
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
                progressDialogHandler6.showPopupProgressSpinner(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCartDetails();
                    }
                }, 100);
            } else if (flag == 3) {
                progressDialogHandler6.showPopupProgressSpinner(false);

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
                progressDialogHandler6.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ApplyCouponCode(String selectedCouponStatus, String selectedCouponCode, String selectedCouponID) {
        progressDialogHandler7.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.APPLY_COUPON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseApplyCouponJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler7.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCartActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(MyCartActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.APPLY_COUPON_REQUEST_PARAMS_COUPON_ID, selectedCouponID);
                params.put(JsonFields.APPLY_COUPON_REQUEST_PARAMS_COUPON_CODE, selectedCouponCode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseApplyCouponJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);

            String Message = jsonObject.optString(JsonFields.APPLY_COUPON_RESPONSE_MESSAGE);

            if (flag == 1) {
                progressDialogHandler7.showPopupProgressSpinner(false);

                Intent i = new Intent(this, ApplyCouponStatusActivity.class);
                i.putExtra("flag", String.valueOf(flag));
                i.putExtra("Message", Message);
                startActivity(i);

            } else if (flag == 3) {
                progressDialogHandler7.showPopupProgressSpinner(false);

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
                progressDialogHandler7.showPopupProgressSpinner(false);
                Intent i = new Intent(this, ApplyCouponStatusActivity.class);
                i.putExtra("flag", String.valueOf(flag));
                i.putExtra("Message", Message);
                startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (Status != null && !Status.isEmpty() && !Status.equals("")) {
            if (Status.equals("1")) {
                Intent i = new Intent(this, HomeActivity.class);
                bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_THREE_STRING);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CHOOSE_COUPON_REQUEST && resultCode == RESULT_OK) {
                String CouponStatus = data.getStringExtra("CouponStatus");
                String CouponCodeID = data.getStringExtra("CouponID");
                String CouponCode = data.getStringExtra("CouponCode");

                if (CouponStatus != null && !CouponStatus.isEmpty() && !CouponStatus.isEmpty() && !CouponStatus.equals("")) {
                    if (CouponStatus.equals("1")) {
                        //Coupon ID
                        ApplyCouponCode(CouponStatus, CouponCode, CouponCodeID);
                    } else {
                        //Coupon Code
                        ApplyCouponCode(CouponStatus, CouponCode, CouponCodeID);
                    }
                } else {
                    CouponStatus = "";
                    CouponCodeID = "";
                    CouponCode = "";
                    Toast.makeText(this, getString(R.string.cart_apply_coupon_something_went_wrong_text), Toast.LENGTH_SHORT).show();
                }


            } else if (requestCode == CHOOSE_ADDRESS_REQUEST && resultCode == RESULT_OK) {
                String AddressID = data.getStringExtra("AddressID");
                if (AddressID != null && !AddressID.isEmpty() && !AddressID.isEmpty() && !AddressID.equals("")) {
                    ChangeAddress(AddressID);
                } else {
                    AddressID = "";
                    Toast.makeText(this, getString(R.string.cart_select_address_something_went_wrong_text), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(MyCartActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}