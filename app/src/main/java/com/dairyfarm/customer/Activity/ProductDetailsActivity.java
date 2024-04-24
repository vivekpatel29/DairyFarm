package com.dairyfarm.customer.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dairyfarm.customer.APIHelper.CommonRequestParams;
import com.dairyfarm.customer.APIHelper.JsonFields;
import com.dairyfarm.customer.APIHelper.WebAuthorization;
import com.dairyfarm.customer.APIHelper.WebURL;
import com.dairyfarm.customer.Adapter.ProductDetailsImageAdapter;
import com.dairyfarm.customer.Adapter.ProductDetailsVarianceAdapter;
import com.dairyfarm.customer.Model.ProductDetailsImage;
import com.dairyfarm.customer.Model.ProductDetailsVariance;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvProductName, tvProductDescription;

    TextView tvMarketPrice, tvSellingPrice, tvDiscountInPercentage;
    TextView tvAddToCartSelected, tvAddToCartNotSelected, tvViewCart;
    TextView tvAddToWishList, tvViewWishList;

    RecyclerView rVProductVarianceWiseImages, rvProductDetailsVariance;

    ArrayList<ProductDetailsImage> listProductDetailsWiseVarianceImage = new ArrayList<>();
    ProductDetailsImageAdapter productDetailsImageAdapter = new ProductDetailsImageAdapter(listProductDetailsWiseVarianceImage, "1");
    ProductDetailsImage productDetailsImage;

    ArrayList<ProductDetailsVariance> listProductDetailsVariance = new ArrayList<>();
    ProductDetailsVarianceAdapter productDetailsVarianceAdapter = new ProductDetailsVarianceAdapter(listProductDetailsVariance);
    ProductDetailsVariance productDetailsVariance;

    ImageView ivProductBigImageView;
    LinearLayout llProductVariancePrice;

    String ProductID;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler1, progressDialogHandler2, progressDialogHandler3;

    TextView tvStockText;

    FrameLayout flProductDetails;
    LinearLayout llNoInternetConnection, llNoRecordFound;

    Button btnRetry;
    TextView tvMessage;
    String Message;

    CustomerSessionManager customerSessionManger;

    String product_id, product_name, category_name, sub_category_name, has_attributes, attribute_id, attribute_string, manage_stock, stock_qty, image_path, thumb_url, add_to_cart, cart_string, cart_id, cart_qty, wishlist_id, market_price, selling_price, saving_percentage, saving_price, product_details, product_is_active, total_cart_qty, last_inserted_cart, last_inserted_cart_name, has_offer, offer_string, offer_id, share_link, is_featured, featured_string;

    String mProductID, mProductName, mProductDetails, mHasAttributes, mAttributeID, mAttributeString, mAttributeGroupID, mAddToCart, mCartString, mCartID, mCartQty, mWishlistID, mMarketPrice, mSellingPrice, mSavingPercentage, mSavingPrice, mHasOffer, mOfferString, mOfferID, mShareLink, mIsFeatured, mFeaturedString;

    ArrayList<ProductDetailsImage> mlistProductImage;
    LinearLayout llProductDetailsImages;
    LinearLayout llProductVariance;

    ShimmerFrameLayout onOfferShimmerContainer;
    ImageView ivShare;
    LinearLayout llProgressLayout;

    //OfferIDManager offerIDManager;
    String OfferID;

    SwipeRefreshLayout swipeToRefreshLayout;

    TextView tvOfferString, tvFeaturedString;
    FrameLayout llComingSoonLayout;
    TextView tvComingSoonMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        tvOfferString = findViewById(R.id.tvOfferString);
        tvFeaturedString = findViewById(R.id.tvFeaturedString);
        llComingSoonLayout = findViewById(R.id.llComingSoonLayout);
        tvComingSoonMessage = findViewById(R.id.tvComingSoonMessage);

//        offerIDManager = new OfferIDManager(this);
//        OfferID = offerIDManager.getOfferID();

        atClass = new AtClass();
        customerSessionManger = new CustomerSessionManager(this);
        progressDialogHandler1 = new ProgressDialogHandler(this);
        progressDialogHandler2 = new ProgressDialogHandler(this);
        progressDialogHandler3 = new ProgressDialogHandler(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initialize();
        setUpSwipeToRefreshLayout();
    }

    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                Intent i = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                i.putExtra("ProductID", ProductID);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        if (i.hasExtra("ProductID")) {
            ProductID = i.getStringExtra("ProductID");
            if (atClass.isNetworkAvailable(this)) {
                getProductDetails();
                //Log.d("Product ID in Details",ProductID);
            } else {

            }
        } else {
            ProductID = "";
        }
    }

    private void getProductDetails() {
        progressDialogHandler1.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PRODUCTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    flProductDetails.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    flProductDetails.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    flProductDetails.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    flProductDetails.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    flProductDetails.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ProductDetailsActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManger.getCustomerID());
                params.put(JsonFields.PRODUCT_DETAILS_REQUEST_PARAMS_PRODUCT_ID, ProductID);
                if (OfferID != null && !OfferID.isEmpty() && !OfferID.equals("")) {
                    params.put(JsonFields.PRODUCT_DETAILS_REQUEST_PARAMS_OFFER_ID, OfferID);
                }
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
                JSONArray arrProducts = jsonObject.optJSONArray(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ARRAY);

                if (arrProducts.length() > 0) {
                    for (int i = 0; i < arrProducts.length(); i++) {
                        JSONObject productObject = arrProducts.optJSONObject(i);
                        product_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ID);
                        product_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_NAME);
                        category_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CATEGORY_NAME);
                        sub_category_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SUB_CATEGORY_NAME);
                        has_attributes = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_HAS_ATTRIBUTES);
                        attribute_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_ID);
                        attribute_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_STRING);
                        manage_stock = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_MANAGE_STOCK);
                        stock_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_STOCK_QTY);
                        image_path = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGE_PATH);
                        thumb_url = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_THUMB_PATH);
                        add_to_cart = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ADD_TO_CART);
                        cart_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_STRING);
                        cart_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_ID);
                        cart_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_QTY);
                        wishlist_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_WISHLIST_ID);
                        market_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_MARKET_PRICE);
                        selling_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SELLING_PRICE);
                        saving_percentage = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SAVING_PERCENTAGE);
                        saving_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SAVING_PRICE);
                        product_details = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_DETAILS);
                        product_is_active = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IS_ACTIVE);
                        total_cart_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_TOTAL_QTY);
                        last_inserted_cart = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_LAST_ID);
                        last_inserted_cart_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_LAST_NAME);
                        has_offer = productObject.optString(JsonFields.PRODUCTS_RESPONSE_HAS_OFFER);
                        offer_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_OFFER_STRING);
                        offer_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_OFFER_ID);
                        is_featured = productObject.optString(JsonFields.PRODUCTS_RESPONSE_IS_FEATURED);
                        featured_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_FEATURED_STRING);
                        share_link = productObject.optString(JsonFields.PRODUCTS_RESPONSE_FEATURED_STRING);


                        if (has_attributes.equals("1")) {
                            JSONArray arrProductAttributes = productObject.optJSONArray(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ARRAY);
                            listProductDetailsVariance.clear();

                            if (arrProductAttributes.length() > 0) {
                                for (int k = 0; k < arrProductAttributes.length(); k++) {
                                    JSONObject productAttributesObject = arrProductAttributes.optJSONObject(k);
                                    String product_attribute_id = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ID);
                                    String product_attribute_string = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_STRING);
                                    String product_attribute_group_id = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_GROUP_ID);
                                    String product_attribute_group_name = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_GROUP_NAME);
                                    String product_attribute_value_id = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_VAL_ID);
                                    String product_attribute_value_name = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_VAL_NAME);
                                    String product_attribute_market_price = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_MARKET_PRICE);
                                    String product_attribute_selling_price = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SELLING_PRICE);
                                    String product_attribute_add_to_cart = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ADD_TO_CART);
                                    String product_attribute_cart_string = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_CART_STRING);
                                    String product_attribute_is_active = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IS_ACTIVE);
                                    String product_attribute_cart_id = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_CART_ID);
                                    String product_attribute_cart_qty = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_CART_QTY);
                                    String product_attribute_manage_stock = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_MANAGE_STOCK);
                                    String product_attribute_stock_qty = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_STOCK_QTY);
                                    String product_attribute_wishlist_id = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_WISHLIST_ID);
                                    String product_attribute_saving_price = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SAVING_PRICE);
                                    String product_attribute_saving_percentage = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SAVING_PERCENTAGE);
                                    String product_attribute_image_path = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGE_PATH);
                                    String product_attribute_thumb_path = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_THUMB_PATH);
                                    String product_attribute_has_offer = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_HAS_OFFER);
                                    String product_attribute_offer_string = productObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_OFFER_STRING);
                                    String product_attribute_offer_id = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_OFFER_ID);
                                    String product_attribute_is_featured = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IS_FEATURED);
                                    String product_attribute_feature_string = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_FEATURE_STRING);
                                    String product_attribute_share_link = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SHARE_LINK);


                                    is_featured = productObject.optString(JsonFields.PRODUCTS_RESPONSE_IS_FEATURED);
                                    featured_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_FEATURED_STRING);

                                    JSONArray arrProductAttributeImages = productAttributesObject.optJSONArray(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGES_ARRAY);
                                    if (arrProductAttributes.length() > 0) {
                                        listProductDetailsWiseVarianceImage = new ArrayList<>();
                                        for (int l = 0; l < arrProductAttributeImages.length(); l++) {
                                            JSONObject productAttributesImagesObject = arrProductAttributeImages.optJSONObject(l);
                                            String ProductAttributeImage = productAttributesImagesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGE);
                                            listProductDetailsWiseVarianceImage.add(new ProductDetailsImage(ProductAttributeImage));
                                        }
                                    }

                                    listProductDetailsVariance.add(new ProductDetailsVariance(product_attribute_id, product_attribute_string, product_attribute_group_id, product_attribute_group_name, product_attribute_value_id, product_attribute_value_name, product_attribute_market_price, product_attribute_selling_price, product_attribute_add_to_cart, product_attribute_cart_string, product_attribute_is_active, product_attribute_cart_id, product_attribute_cart_qty, product_attribute_manage_stock, product_attribute_stock_qty, product_attribute_wishlist_id, product_attribute_saving_price, product_attribute_saving_percentage, product_attribute_image_path, product_attribute_thumb_path, product_attribute_has_offer, product_attribute_offer_string, product_attribute_offer_id, product_attribute_is_featured, product_attribute_feature_string, product_attribute_share_link, listProductDetailsWiseVarianceImage));
                                }
                                productDetailsVarianceAdapter.notifyDataSetChanged();
                            }

                        } else {
                            JSONArray arrProductImages = productObject.optJSONArray(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGES_ARRAY);
                            if (arrProductImages.length() > 0) {
                                listProductDetailsWiseVarianceImage.clear();
                                for (int j = 0; j < arrProductImages.length(); j++) {
                                    JSONObject productAttributesObject = arrProductImages.optJSONObject(j);
                                    String ProductImage = productAttributesObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGE);
                                    listProductDetailsWiseVarianceImage.add(new ProductDetailsImage(ProductImage));
                                }
                            }

                        }

                    }
                    setProductDetails();
                    flProductDetails.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                } else {
                    flProductDetails.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.VISIBLE);
                    tvMessage.setText(Message);
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
                flProductDetails.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setProductDetails() {
        if (has_attributes.equals("1")) {
            // Product Has Atributes
        } else {
            // Product Not Having Attributes
            mProductID = product_id;
            mProductName = product_name;
            mProductDetails = product_details;
            mHasAttributes = has_attributes;
            mAttributeID = attribute_id;
            mAttributeString = attribute_string;
            mAttributeGroupID = attribute_string;
            mAddToCart = add_to_cart;
            mCartString = cart_string;
            mCartID = cart_id;
            mCartQty = cart_qty;
            mWishlistID = wishlist_id;
            mMarketPrice = market_price;
            mSellingPrice = selling_price;
            mSavingPercentage = saving_percentage;
            mlistProductImage = listProductDetailsWiseVarianceImage;
            mHasOffer = has_offer;
            mOfferString = offer_string;
            mOfferID = offer_id;
            mShareLink = share_link;
            mIsFeatured = is_featured;
            Log.d("Is Featured", "Val " + is_featured);
            mFeaturedString = featured_string;
            Log.d("Featured String", "Val " + featured_string);
            setData();
        }
    }

    private void initialize() {
        onOfferShimmerContainer = findViewById(R.id.onOfferShimmerContainer);
        llProductDetailsImages = findViewById(R.id.llProductDetailsImages);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);
        llNoRecordFound = findViewById(R.id.llError);
        flProductDetails = findViewById(R.id.flProductDetails);
        llProductVariance = findViewById(R.id.llProductVariance);

        btnRetry = findViewById(R.id.btnRetry);
        tvMessage = findViewById(R.id.tvMessage);

        btnRetry.setOnClickListener(this);

        ivBack = findViewById(R.id.ivBack);
        ivShare = findViewById(R.id.ivShare);
        ivShare.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvProductName = findViewById(R.id.tvProductName);
        tvMarketPrice = findViewById(R.id.tvMarketPrice);
        tvSellingPrice = findViewById(R.id.tvSellingPrice);
        tvDiscountInPercentage = findViewById(R.id.tvDiscountInPercentage);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvAddToCartSelected = findViewById(R.id.tvAddToCartSelected);
        tvAddToCartNotSelected = findViewById(R.id.tvAddToCartNotSelected);
        tvViewCart = findViewById(R.id.tvViewCart);

        tvAddToWishList = findViewById(R.id.tvAddToWishList);
        tvViewWishList = findViewById(R.id.tvViewWishList);

        llProductVariancePrice = findViewById(R.id.llProductVariancePrice);
        tvStockText = findViewById(R.id.tvStockText);

        ivProductBigImageView = findViewById(R.id.ivProductBigImageView);
        llProgressLayout = findViewById(R.id.llProgressLayout);

        rVProductVarianceWiseImages = findViewById(R.id.rVProductVarianceWiseImages);
        ItemOffsetDecoration itemDecoration1 = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rVProductVarianceWiseImages.addItemDecoration(itemDecoration1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rVProductVarianceWiseImages.setLayoutManager(linearLayoutManager1);
        rVProductVarianceWiseImages.setAdapter(productDetailsImageAdapter);

        rvProductDetailsVariance = findViewById(R.id.rvProductDetailsVariance);
        ItemOffsetDecoration itemDecoration2 = new ItemOffsetDecoration(this, R.dimen.item_offset_less);
        rvProductDetailsVariance.addItemDecoration(itemDecoration2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvProductDetailsVariance.setLayoutManager(linearLayoutManager2);
        rvProductDetailsVariance.setAdapter(productDetailsVarianceAdapter);

        tvAddToCartSelected.setOnClickListener(this);
        tvViewCart.setOnClickListener(this);

        tvAddToWishList.setOnClickListener(this);
        tvViewWishList.setOnClickListener(this);
    }

    private void setVarianceWiseImagesSlider(ArrayList<ProductDetailsImage> listProductDetailsPictures) {
        //mlistProductImage = listProductDetailsPictures;
        listProductDetailsWiseVarianceImage = mlistProductImage;
        Log.d("List Image Size", String.valueOf(listProductDetailsWiseVarianceImage.size()));
        ProductDetailsImageAdapter productDetailsImageAdapter = new ProductDetailsImageAdapter(listProductDetailsWiseVarianceImage, "1");
        rVProductVarianceWiseImages.setAdapter(productDetailsImageAdapter);
        rVProductVarianceWiseImages.setSelected(true);

//        productDetailsImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvAddToCartSelected:
                if (customerSessionManger.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    Intent i = new Intent(this, LoginActivity.class);
                    i.putExtra("isFrom", "2");
                    i.putExtra("ProductID", ProductID);
                    i.putExtra("FromScreenName", "ProductDetailsActivity");
                    i.putExtra("ToScreenName", "ProductDetailsActivity");
                    startActivity(i);
                } else {
                    if (mHasAttributes.equals("1")) {
                        if (atClass.isNetworkAvailable(this)) {
                            AddItemToCart(customerSessionManger.getCustomerID(), mProductID, mHasAttributes, mAttributeID, mOfferID);
                        } else {
                            Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (atClass.isNetworkAvailable(this)) {
                            AddItemToCart(customerSessionManger.getCustomerID(), mProductID, "", "", mOfferID);
                        } else {
                            Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.tvViewCart:
                if (customerSessionManger.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    Intent i = new Intent(this, LoginActivity.class);
                    i.putExtra("isFrom", "2");
                    i.putExtra("ProductID", ProductID);
                    i.putExtra("FromScreenName", "ProductDetailsActivity");
                    i.putExtra("ToScreenName", "ProductDetailsActivity");
                    startActivity(i);
                } else {
                    Intent i1 = new Intent(this, MyCartActivity.class);
                    startActivity(i1);
                }
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i2 = new Intent(this, ProductDetailsActivity.class);
                    i2.putExtra("ProductID", ProductID);
                    startActivity(i2);
                    finish();
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    flProductDetails.setVisibility(View.GONE);
                }
                break;

            case R.id.tvAddToWishList:
                if (customerSessionManger.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    Intent i = new Intent(this, LoginActivity.class);
                    i.putExtra("isFrom", "2");
                    i.putExtra("ProductID", ProductID);
                    i.putExtra("FromScreenName", "ProductDetailsActivity");
                    i.putExtra("ToScreenName", "ProductDetailsActivity");
                    startActivity(i);
                } else {
                    if (mHasAttributes.equals("1")) {
                        if (atClass.isNetworkAvailable(this)) {
                            AddItemToWishlist(customerSessionManger.getCustomerID(), mProductID, mHasAttributes, mAttributeID, "1", mOfferID);
                        } else {
                            Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (atClass.isNetworkAvailable(this)) {
                            AddItemToWishlist(customerSessionManger.getCustomerID(), mProductID, "", "", "1", mOfferID);
                        } else {
                            Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

            case R.id.tvViewWishList:
                if (customerSessionManger.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    Intent i = new Intent(this, LoginActivity.class);
                    i.putExtra("isFrom", "2");
                    i.putExtra("ProductID", ProductID);
                    i.putExtra("FromScreenName", "ProductDetailsActivity");
                    i.putExtra("ToScreenName", "ProductDetailsActivity");
                    startActivity(i);
                } else {
                    Intent i3 = new Intent(this, MyWishlistActivity.class);
                    startActivity(i3);
                }
                break;
            case R.id.ivProductBigImageView:
                Intent i4 = new Intent(this, ProductDetailsImagesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("arrayListProductVarianceWiseDetails", listProductDetailsWiseVarianceImage);
                i4.putExtras(bundle);
                startActivity(i4);
                break;

            case R.id.ivShare:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, mProductName);
                    String shareMessage = getString(R.string.product_details_share_product_link_take_look_at_text) + " " + mProductName + " " + getString(R.string.product_details_share_product_link_on_text) + " " + getString(R.string.app_name) + getString(R.string.product_details_share_product_double_new_line_text);
                    shareMessage = shareMessage + mShareLink + getString(R.string.product_details_share_product_double_new_line_text);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.product_details_choose_option_to_share_text)));
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.product_details_something_went_wrong_while_sharing_text), Toast.LENGTH_SHORT).show();
                    //e.toString();
                }

                break;
        }
    }

    private void AddItemToWishlist(final String customerID, final String mProductID, final String mHasAttributes, final String mAttributeID, final String mStatus, final String mOfferID) {
        progressDialogHandler2.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CHANGE_WISHLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseAddToWishlistJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ProductDetailsActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerID);
                params.put(JsonFields.CHANGE_WISHLIST_REQUEST_PARAM_OFFER_ID, mOfferID);
                params.put(JsonFields.CHANGE_WISHLIST_REQUEST_PARAM_WISHLIST_ID, mWishlistID);
                params.put(JsonFields.CHANGE_WISHLIST_REQUEST_PARAM_PRODUCT_ID, mProductID);
                params.put(JsonFields.CHANGE_WISHLIST_REQUEST_PARAM_HAS_ATTRIBUTES, mHasAttributes);
                params.put(JsonFields.CHANGE_WISHLIST_REQUEST_PARAM_PRODUCT_ATTRIBUTE_ID, mAttributeID);
                params.put(JsonFields.CHANGE_WISHLIST_REQUEST_PARAM_STATUS, mStatus);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseAddToWishlistJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler2.showPopupProgressSpinner(false);

                Intent i = new Intent(this, SuccessMessageActivity.class);
                i.putExtra("Message", Message);
                i.putExtra("FromScreenName", "ProductDetailsActivity");
                i.putExtra("ToScreenName", "ProductDetailsActivity");
                i.putExtra("ProductID", ProductID);
                startActivity(i);
                finish();
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
                Toast.makeText(ProductDetailsActivity.this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void AddItemToCart(final String customerID, final String productID, final String hasAttribute, final String productAttributeID, final String productAttributeOfferID) {
        progressDialogHandler3.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ADD_PRODUCT_TO_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseAddToCartJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler3.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ProductDetailsActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerID);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_PRODUCT_ID, productID);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_OFFER_ID, productAttributeOfferID);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_HAS_ATTRIBUTES, hasAttribute);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_PRODUCT_ATTRIBUTE_ID, productAttributeID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseAddToCartJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler3.showPopupProgressSpinner(false);

                Intent i = new Intent(this, SuccessMessageActivity.class);
                i.putExtra("Message", Message);
                i.putExtra("FromScreenName", "ProductDetailsActivity");
                i.putExtra("ToScreenName", "ProductDetailsActivity");
                i.putExtra("ProductID", ProductID);
                startActivity(i);
                finish();

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
                progressDialogHandler3.showPopupProgressSpinner(false);

                Toast.makeText(ProductDetailsActivity.this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void SetImageToBigImageView(String URL) {
        llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(this).load(URL).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_login).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                llProgressLayout.setVisibility(View.GONE);
                ivProductBigImageView.setImageResource(R.drawable.app_icon_login);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(ivProductBigImageView);
        ivProductBigImageView.setOnClickListener(this);
    }

    public void setUpProductVariantsDetails(String AttrID, String AttrString, String AttrGrpID, String AddToCart, String CartString, String CartID, String CartQty, String WishlistID, String MarketPrice, String SellingPrice, String SavingsPercentage, String HasOffer, String OfferString, String OfferID, String IsFeatured, String FeaturedString, String ShareLink, ArrayList<ProductDetailsImage> listProductDetailsWiseVariancePicture) {
        mProductID = product_id;
        mProductName = product_name;
        mProductDetails = product_details;
        mHasAttributes = has_attributes;
        mAttributeID = AttrID;
        mAttributeString = AttrString;
        mAttributeGroupID = AttrGrpID;
        mAddToCart = AddToCart;
        mCartString = CartString;
        mCartID = CartID;
        mCartQty = CartQty;
        mWishlistID = WishlistID;
        mMarketPrice = MarketPrice;
        mSellingPrice = SellingPrice;
        mSavingPercentage = SavingsPercentage;
        mHasOffer = HasOffer;
        mOfferString = OfferString;
        mOfferID = OfferID;
        mIsFeatured = IsFeatured;
        mFeaturedString = FeaturedString;
        mShareLink = ShareLink;
        mlistProductImage = listProductDetailsWiseVariancePicture;

        //Log.d("MarketPrice", MarketPrice);
        //Log.d("SellingPrice", SellingPrice);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                setData();
            }
        }, 100);
    }

    private void setData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvProductName.setText(Html.fromHtml(mProductName, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvProductName.setText(Html.fromHtml(mProductName));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvProductDescription.setText(Html.fromHtml(mProductDetails, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvProductDescription.setText(Html.fromHtml(mProductDetails));
        }

        tvSellingPrice.setVisibility(View.GONE);
        tvSellingPrice.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvSellingPrice.setText(Html.fromHtml(mSellingPrice, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvSellingPrice.setText(Html.fromHtml(mSellingPrice));
        }

        tvMarketPrice.setVisibility(View.GONE);
        tvMarketPrice.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvMarketPrice.setText(Html.fromHtml(mMarketPrice, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvMarketPrice.setText(Html.fromHtml(mMarketPrice));
        }
        tvMarketPrice.setPaintFlags(tvMarketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tvDiscountInPercentage.setVisibility(View.GONE);
        tvDiscountInPercentage.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvDiscountInPercentage.setText(Html.fromHtml(mSavingPercentage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvDiscountInPercentage.setText(Html.fromHtml(mSavingPercentage));
        }

        if (mShareLink != null && !mShareLink.equals("") && !mShareLink.isEmpty()) {
            ivShare.setVisibility(View.VISIBLE);
            ivShare.setEnabled(true);
        } else {
            ivShare.setVisibility(View.INVISIBLE);
            ivShare.setEnabled(false);
        }

        if (mHasOffer.equals("1")) {
            onOfferShimmerContainer.setVisibility(View.VISIBLE);
        } else {
            onOfferShimmerContainer.setVisibility(View.GONE);
        }

        if (mHasOffer.equals("1")) {
            onOfferShimmerContainer.setVisibility(View.GONE);
            if (mOfferString != null && !mOfferString.equals("") && !mOfferString.isEmpty()) {
                tvOfferString.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvOfferString.setText(Html.fromHtml(mOfferString, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvOfferString.setText(Html.fromHtml(mOfferString));
                }
            } else {
                tvOfferString.setVisibility(View.GONE);
                onOfferShimmerContainer.setVisibility(View.GONE);
            }
        } else {
            tvOfferString.setVisibility(View.GONE);
            onOfferShimmerContainer.setVisibility(View.GONE);
        }


        if (mIsFeatured.equals("1")) {
            if (mFeaturedString != null && !mFeaturedString.equals("") && !mFeaturedString.isEmpty()) {
                tvFeaturedString.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvFeaturedString.setText(Html.fromHtml(mFeaturedString, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvFeaturedString.setText(Html.fromHtml(mFeaturedString));
                }
            } else {
                tvFeaturedString.setVisibility(View.GONE);
            }
        } else {
            tvFeaturedString.setVisibility(View.GONE);
        }


        if (mHasAttributes.equals("1")) {
            llProductVariance.setVisibility(View.VISIBLE);
            if (mlistProductImage != null && !mlistProductImage.isEmpty() && !mlistProductImage.equals("")) {

                Log.d("List Size Of Image", String.valueOf(mlistProductImage.size()));
                setVarianceWiseImagesSlider(mlistProductImage);

            } else {
                //Product images not added
                SetImageToBigImageView("");
                rVProductVarianceWiseImages.setVisibility(View.GONE);
            }

            if (mAddToCart.equals("0")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvStockText.setText(Html.fromHtml(mCartString, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvStockText.setText(Html.fromHtml(mCartString));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvComingSoonMessage.setText(Html.fromHtml(mCartString, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvComingSoonMessage.setText(Html.fromHtml(mCartString));
                }

                llComingSoonLayout.setVisibility(View.VISIBLE);

                tvStockText.setVisibility(View.GONE);

                tvAddToCartNotSelected.setVisibility(View.VISIBLE);
                tvAddToCartSelected.setVisibility(View.GONE);
                tvViewCart.setVisibility(View.GONE);
                //tvAddToCartSelected, tvAddToCartNotSelected, tvViewCart
            } else {
                tvStockText.setVisibility(View.GONE);
                llComingSoonLayout.setVisibility(View.GONE);

                if (mCartQty.equals("0")) {
                    tvAddToCartNotSelected.setVisibility(View.GONE);
                    tvAddToCartSelected.setVisibility(View.VISIBLE);
                    tvViewCart.setVisibility(View.GONE);
                } else {
                    tvAddToCartNotSelected.setVisibility(View.GONE);
                    tvAddToCartSelected.setVisibility(View.GONE);
                    tvViewCart.setVisibility(View.VISIBLE);
                }
            }


            if (mWishlistID.equals("0")) {
                tvAddToWishList.setVisibility(View.VISIBLE);
                tvViewWishList.setVisibility(View.GONE);
            } else {
                tvAddToWishList.setVisibility(View.GONE);
                tvViewWishList.setVisibility(View.VISIBLE);
            }
        } else {
            llProductVariance.setVisibility(View.GONE);

            if (mlistProductImage != null && !mlistProductImage.isEmpty() && !mlistProductImage.equals("")) {
                Log.d("List Size Of Image", String.valueOf(mlistProductImage.size()));
                setVarianceWiseImagesSlider(mlistProductImage);

            } else {
                //Product images not added
                SetImageToBigImageView("");
                rVProductVarianceWiseImages.setVisibility(View.GONE);
            }

            if (mAddToCart.equals("0")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvComingSoonMessage.setText(Html.fromHtml(mCartString, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvComingSoonMessage.setText(Html.fromHtml(mCartString));
                }
                llComingSoonLayout.setVisibility(View.VISIBLE);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvStockText.setText(Html.fromHtml(mCartString, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvStockText.setText(Html.fromHtml(mCartString));
                }
                tvStockText.setVisibility(View.GONE);

                tvAddToCartNotSelected.setVisibility(View.VISIBLE);
                tvAddToCartSelected.setVisibility(View.GONE);
                tvViewCart.setVisibility(View.GONE);

            } else {
                if (mCartQty.equals("0")) {
                    tvAddToCartNotSelected.setVisibility(View.GONE);
                    tvAddToCartSelected.setVisibility(View.VISIBLE);
                    tvViewCart.setVisibility(View.GONE);
                } else {
                    tvAddToCartNotSelected.setVisibility(View.GONE);
                    tvAddToCartSelected.setVisibility(View.GONE);
                    tvViewCart.setVisibility(View.VISIBLE);
                }
            }

            if (mWishlistID.equals("0")) {
                tvAddToWishList.setVisibility(View.VISIBLE);
                tvViewWishList.setVisibility(View.GONE);
            } else {
                tvAddToWishList.setVisibility(View.GONE);
                tvViewWishList.setVisibility(View.VISIBLE);
            }
        }
    }

    public Bitmap getBitmapFromURL(String urL) {
        try {
            URL url = new URL(urL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmapFrmUrl = BitmapFactory.decodeStream(input);
            return bitmapFrmUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
