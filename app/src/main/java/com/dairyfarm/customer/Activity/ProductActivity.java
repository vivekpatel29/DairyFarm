package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Html;
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
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dairyfarm.customer.APIHelper.CommonRequestParams;
import com.dairyfarm.customer.APIHelper.JsonFields;
import com.dairyfarm.customer.APIHelper.WebAuthorization;
import com.dairyfarm.customer.APIHelper.WebURL;
import com.dairyfarm.customer.Adapter.ProductsAdapter;
import com.dairyfarm.customer.Model.ProductVariance;
import com.dairyfarm.customer.Model.Products;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.dairyfarm.customer.Utils.ProductsLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivBack;

    ArrayList<Products> listProducts = new ArrayList<>();
    ProductsAdapter productsAdapter = new ProductsAdapter(listProducts);

    RecyclerView rvSelectProduct;
    EditText etSearch;

    LinearLayout llSelectProductMaster, llNoInternetConnection;
    Button btnRetry;

    LinearLayout llSelectProduct, llError, llNoRecordFound;

    TextView tvMessage, tvNoRecordFoundMessage;
    String Message;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    public int currentPageValue = 1;
    public int searchCurrentPageValue = 1;

    CustomerSessionManager customerSessionManager;
    AtClass atClass;

    LinearLayout llProduct;

    ImageView ivVoiceSearch, iVClearSearchResults;
    LinearLayout llProgressLayout;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    SwipeRefreshLayout swipeToRefreshLayout;

    TextView tvToolbarTitle;
    String SubCategoryID, SubCategoryName;

    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    ImageView ivListLayout, ivGridLayout;
    ProductsLayoutManager productsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ivListLayout = findViewById(R.id.ivListLayout);
        ivListLayout.setOnClickListener(this);
        ivGridLayout = findViewById(R.id.ivGridLayout);
        ivGridLayout.setOnClickListener(this);

        productsLayoutManager = new ProductsLayoutManager(this);

        productsAdapter = new ProductsAdapter(listProducts);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        llProgressLayout = findViewById(R.id.llProgressLayout);

        ivVoiceSearch = findViewById(R.id.ivVoiceSearch);
        ivVoiceSearch.setOnClickListener(this);
        iVClearSearchResults = findViewById(R.id.iVClearSearchResults);
        iVClearSearchResults.setOnClickListener(this);

        llProduct = findViewById(R.id.llProduct);

        atClass = new AtClass();
        customerSessionManager = new CustomerSessionManager(this);

        llSelectProductMaster = findViewById(R.id.llSelectProductMaster);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);

        llSelectProduct = findViewById(R.id.llSelectProduct);
        llError = findViewById(R.id.llError);
        llNoRecordFound = findViewById(R.id.llNoRecordsFound);

        tvMessage = findViewById(R.id.tvMessage);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        tvNoRecordFoundMessage = findViewById(R.id.tvNoRecordFoundMessage);

        etSearch = findViewById(R.id.etSearch);

        rvSelectProduct = findViewById(R.id.rvSelectProduct);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_large);
        rvSelectProduct.addItemDecoration(itemDecoration);
        linearLayoutManager = new LinearLayoutManager(this);
        rvSelectProduct.setLayoutManager(linearLayoutManager);
        rvSelectProduct.setAdapter(productsAdapter);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setSelected(true);

        setLayoutManagerAccordingToPreference();

        setTextChangeListeners();
        setUpSwipeToRefreshLayout();

        Intent i = getIntent();
        if (i.hasExtra("SubCategoryID")) {
            SubCategoryID = i.getStringExtra("SubCategoryID");
        } else {
            SubCategoryID = "";
        }

        if (i.hasExtra("SubCategoryName")) {
            SubCategoryName = i.getStringExtra("SubCategoryName");
        } else {
            SubCategoryName = "";
        }


        //setProductsData();
        //setTopicName();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (atClass.isNetworkAvailable(this)) {
            if (atClass.CheckEmptyString(this, SubCategoryID, "")
                    && atClass.CheckEmptyString(this, SubCategoryName, "")) {

                setToolbarTitleAndSearchHint();

                if (etSearch.getText().toString().trim() != null && !etSearch.getText().toString().trim().isEmpty() && !etSearch.getText().toString().trim().equals("")) {
                    searchCurrentPageValue = 1;
                    getProductsData(etSearch.getText().toString().trim(), "3");
                } else {
                    setPagination();
                }
            } else {
                llProduct.setVisibility(View.VISIBLE);
                llSelectProductMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                llSelectProduct.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);
                tvMessage.setText(getString(R.string.common_something_went_wrong));
            }
        } else {
            setNoInternetConnection();
        }
    }

    private void setToolbarTitleAndSearchHint() {
        atClass.setUpTextToView("1", tvToolbarTitle, SubCategoryName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            etSearch.setHint(Html.fromHtml("Search by " + SubCategoryName + " name", Html.FROM_HTML_MODE_COMPACT));
        } else {
            etSearch.setHint(Html.fromHtml("Search by " + SubCategoryName + " name"));
        }
    }

    /*private void setTopicName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTitle.setText(Html.fromHtml(TopicName, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvTitle.setText(Html.fromHtml(TopicName));
        }
    }*/

    private void setPagination() {
        rvSelectProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }


                int firstPosition = getFirstPosition();
                if (firstPosition > 0) {
                    swipeToRefreshLayout.setEnabled(false);
                } else {
                    swipeToRefreshLayout.setEnabled(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (productsLayoutManager.getProductLayout().equals("1")) {
                    currentItems = linearLayoutManager.getChildCount();
                    totalItems = linearLayoutManager.getItemCount();
                    scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                } else {
                    currentItems = gridLayoutManager.getChildCount();
                    totalItems = gridLayoutManager.getItemCount();
                    scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                }

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    if (atClass.isNetworkAvailable(ProductActivity.this)) {
                        currentPageValue++;
                        searchCurrentPageValue++;
                        getProductsData("", "2");
                    } else {
                        setNoInternetConnection();
                    }
                }
            }
        });

        if (atClass.isNetworkAvailable(ProductActivity.this)) {
            getProductsData("", "1");
        } else {
            setNoInternetConnection();
        }
    }

    private int getFirstPosition() {
        int firstPosition = 0;
        if (productsLayoutManager.getProductLayout() != null && !productsLayoutManager.getProductLayout().equals("") && !productsLayoutManager.getProductLayout().isEmpty()) {
            if (productsLayoutManager.getProductLayout().equals("1")) {
                //List Layout
                firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            } else if (productsLayoutManager.getProductLayout().equals("2")) {
                //Grid Layout
                productsLayoutManager.setProductLayout("2");
                firstPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            } else {
                //Set Layout as List
                firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            }
        } else {
            //Set Layout as List
            firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        }

        return firstPosition;
    }


    private void setLayoutManagerAccordingToPreference() {
        if (productsLayoutManager.getProductLayout() != null && !productsLayoutManager.getProductLayout().equals("") && !productsLayoutManager.getProductLayout().isEmpty()) {
            if (productsLayoutManager.getProductLayout().equals("1")) {
                //List Layout
                productsLayoutManager.setProductLayout("1");
                setListLayout();
            } else if (productsLayoutManager.getProductLayout().equals("2")) {
                //Grid Layout
                productsLayoutManager.setProductLayout("2");
                setGridLayout();
            } else {
                //Set Layout as List
                productsLayoutManager.setProductLayout("1");
                setListLayout();
            }
        } else {
            //Set Layout as List
            productsLayoutManager.setProductLayout("1");
            setListLayout();
        }
    }

    private void setGridLayout() {
        ivListLayout.setVisibility(View.VISIBLE);
        ivGridLayout.setVisibility(View.GONE);

        gridLayoutManager = new GridLayoutManager(this, 2);
        rvSelectProduct.setLayoutManager(gridLayoutManager);
        rvSelectProduct.setAdapter(productsAdapter);
    }

    private void setListLayout() {
        ivListLayout.setVisibility(View.GONE);
        ivGridLayout.setVisibility(View.VISIBLE);

        linearLayoutManager = new LinearLayoutManager(this);
        rvSelectProduct.setLayoutManager(linearLayoutManager);
        rvSelectProduct.setAdapter(productsAdapter);
    }

    private void setTextChangeListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!atClass.isNetworkAvailable(ProductActivity.this)) {
                    llProduct.setVisibility(View.GONE);
                    llSelectProductMaster.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    iVClearSearchResults.setVisibility(View.VISIBLE);
                    ivVoiceSearch.setVisibility(View.GONE);
                    if (etSearch.getText().toString().trim() != null && !etSearch.getText().toString().trim().equals("") && !etSearch.getText().toString().trim().equals("")) {
                        searchCurrentPageValue = 1;
                        getProductsData(charSequence.toString(), "3");
                    } else {
                        searchCurrentPageValue = 1;
                        currentPageValue = 1;
                        getProductsData("", "3");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input

            }
        });
    }

    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                RefreshScreen();
            }
        });
    }

    private void RefreshScreen() {
        Intent i = new Intent(ProductActivity.this, ProductActivity.class);
        i.putExtra("SubCategoryID",SubCategoryID);
        i.putExtra("SubCategoryName",SubCategoryName);
        startActivity(i);
        finish();
    }

    private void setNoInternetConnection() {
        llProduct.setVisibility(View.VISIBLE);
        llSelectProductMaster.setVisibility(View.GONE);
        llNoInternetConnection.setVisibility(View.VISIBLE);
    }

    public void getProductsData(final String SearchQuery, String Status) {
        llProgressLayout.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.PRODUCTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response, SearchQuery, Status);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llProgressLayout.setVisibility(View.GONE);
                setNoInternetConnection();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAuthorization.checkAuthentication();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                CommonRequestParams commonRequestParams = new CommonRequestParams();
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ProductActivity.this);
                params.put(JsonFields.PRODUCTS_REQUEST_PARAMS_SUB_CATEGORY_ID, SubCategoryID);
                params.put(JsonFields.PRODUCTS_REQUEST_PARAMS_PRODUCTS_SEARCH_QUERY, SearchQuery);
                if (SearchQuery != null && !SearchQuery.isEmpty() && !SearchQuery.equals("")) {
                    params.put(JsonFields.COMMON_REQUEST_PARAM_CURRENT_PAGE, String.valueOf(searchCurrentPageValue));
                } else {
                    if (Status.equals("3")) {
                        params.put(JsonFields.COMMON_REQUEST_PARAM_CURRENT_PAGE, "1");
                    } else {
                        params.put(JsonFields.COMMON_REQUEST_PARAM_CURRENT_PAGE, String.valueOf(currentPageValue));
                    }
                }
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProductActivity.this);
        requestQueue.add(stringRequest);
    }


    private void parseJSON(String response, String SearchQuery, String Status) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                if (currentPageValue == 1) {
                    listProducts.clear();
                } else if (searchCurrentPageValue == 1 && Status.equals("3")) {
                    listProducts.clear();
                }

                llProgressLayout.setVisibility(View.GONE);
                ArrayList<String> listProductImages = null;
                ArrayList<ProductVariance> listProductVariance = null;
                ArrayList<String> listProductAttributeImages = null;
                JSONArray arrProducts = jsonObject.optJSONArray(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ARRAY);
                if (arrProducts.length() > 0) {
                    for (int i = 0; i < arrProducts.length(); i++) {
                        JSONObject productObject = arrProducts.optJSONObject(i);
                        String product_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ID);
                        String product_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_NAME);
                        String category_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CATEGORY_NAME);
                        String sub_category_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SUB_CATEGORY_NAME);
                        String has_attributes = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_HAS_ATTRIBUTES);
                        String attribute_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_ID);
                        String attribute_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_STRING);
                        String manage_stock = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_MANAGE_STOCK);
                        String stock_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_STOCK_QTY);
                        String image_path = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGE_PATH);
                        String thumb_url = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_THUMB_PATH);
                        String add_to_cart = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ADD_TO_CART);
                        String cart_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_STRING);
                        String cart_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_ID);
                        String cart_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_QTY);
                        String wishlist_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_WISHLIST_ID);
                        String market_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_MARKET_PRICE);
                        String selling_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SELLING_PRICE);
                        String saving_percentage = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SAVING_PERCENTAGE);
                        String saving_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SAVING_PRICE);
                        String product_details = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_DETAILS);
                        String product_is_active = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IS_ACTIVE);
                        String total_cart_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_TOTAL_QTY);
                        String last_inserted_cart = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_LAST_ID);
                        String last_inserted_cart_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_LAST_NAME);
                        String has_offer = productObject.optString(JsonFields.PRODUCTS_RESPONSE_HAS_OFFER);
                        String has_offer_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_OFFER_STRING);
                        String offer_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_OFFER_ID);
                        String is_featured = productObject.optString(JsonFields.PRODUCTS_RESPONSE_IS_FEATURED);
                        String featured_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_FEATURED_STRING);

                        JSONArray arrProductImages = productObject.optJSONArray(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGES_ARRAY);
                        if (arrProductImages.length() > 0) {
                            listProductImages = new ArrayList<>();
                            for (int j = 0; j < arrProductImages.length(); j++) {
                                JSONObject productAttributesObject = arrProductImages.optJSONObject(j);
                                String ProductImage = productAttributesObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGE);
                                listProductImages.add(ProductImage);
                            }
                        }

                        JSONArray arrProductAttributes = productObject.optJSONArray(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ARRAY);
                        if (arrProductAttributes.length() > 0) {
                            listProductVariance = new ArrayList<>();
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
                                String product_attribute_offer_string = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_OFFER_STRING);
                                String product_attribute_offer_id = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_OFFER_ID);
                                String product_attribute_is_featured = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IS_FEATURED);
                                String product_attribute_feature_string = productAttributesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_FEATURE_STRING);

                                JSONArray arrProductAttributeImages = productAttributesObject.optJSONArray(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGES_ARRAY);
                                if (arrProductAttributes.length() > 0) {
                                    listProductAttributeImages = new ArrayList<>();
                                    for (int l = 0; l < arrProductAttributeImages.length(); l++) {
                                        JSONObject productAttributesImagesObject = arrProductAttributeImages.optJSONObject(l);
                                        String ProductAttributeImage = productAttributesImagesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGE);
                                        listProductAttributeImages.add(ProductAttributeImage);
                                    }
                                }
                                listProductVariance.add(new ProductVariance(product_attribute_id, product_attribute_string, product_attribute_group_id, product_attribute_group_name, product_attribute_value_id, product_attribute_value_name, product_attribute_market_price, product_attribute_selling_price, product_attribute_add_to_cart, product_attribute_cart_string, product_attribute_is_active, product_attribute_cart_id, product_attribute_cart_qty, product_attribute_manage_stock, product_attribute_stock_qty, product_attribute_wishlist_id, product_attribute_saving_price, product_attribute_saving_percentage, product_attribute_image_path, product_attribute_thumb_path, product_attribute_has_offer, product_attribute_offer_string, product_attribute_offer_id, product_attribute_is_featured, product_attribute_feature_string, listProductAttributeImages));
                            }
                        }
                        listProducts.add(new Products(product_id, product_name, category_name, sub_category_name, has_attributes, attribute_id, attribute_string, manage_stock, stock_qty, image_path, thumb_url, add_to_cart, cart_string, cart_id, cart_qty, wishlist_id, market_price, selling_price, saving_percentage, saving_price, product_details, product_is_active, total_cart_qty, last_inserted_cart, last_inserted_cart_name, has_offer, has_offer_string, offer_id, is_featured, featured_string, listProductImages, listProductVariance));
                    }
                    productsAdapter.notifyDataSetChanged();

                    llSelectProductMaster.setVisibility(View.VISIBLE);
                    llNoInternetConnection.setVisibility(View.GONE);

                    llProduct.setVisibility(View.VISIBLE);
                    llSelectProduct.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llError.setVisibility(View.GONE);

                } else {
                    llProduct.setVisibility(View.VISIBLE);
                    llSelectProductMaster.setVisibility(View.VISIBLE);
                    llNoInternetConnection.setVisibility(View.GONE);

                    llSelectProduct.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llError.setVisibility(View.GONE);
                    tvMessage.setText(getString(R.string.common_something_went_wrong));
                }
            } else if (flag == 2) {
                llProgressLayout.setVisibility(View.GONE);
                llProduct.setVisibility(View.VISIBLE);
                llSelectProductMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);
            } else if (flag == 3) {
                llProgressLayout.setVisibility(View.GONE);
                customerSessionManager.logout();
                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(ProductActivity.this, LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else if (flag == 4) {
                llProgressLayout.setVisibility(View.GONE);
                llProduct.setVisibility(View.VISIBLE);
                llSelectProductMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                llSelectProduct.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);

            } else {
                llProgressLayout.setVisibility(View.GONE);
                llProduct.setVisibility(View.VISIBLE);
                llSelectProductMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                if (SearchQuery != null && !SearchQuery.isEmpty() && !SearchQuery.equals("")) {
                    llSelectProduct.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.VISIBLE);
                    llError.setVisibility(View.GONE);
                    tvNoRecordFoundMessage.setText(Message);
                } else {
                    llSelectProduct.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llError.setVisibility(View.VISIBLE);
                    tvMessage.setText(Message);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(this)) {
                    RefreshScreen();
                } else {
                    setNoInternetConnection();
                }
                break;

            case R.id.ivVoiceSearch:
                promptSpeechInput();
                break;

            case R.id.ivListLayout:
                productsLayoutManager.setProductLayout("1");
                setListLayout();
                break;

            case R.id.ivGridLayout:
                productsLayoutManager.setProductLayout("2");
                setGridLayout();
                break;

            case R.id.iVClearSearchResults:
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        iVClearSearchResults.setVisibility(View.GONE);
                        ivVoiceSearch.setVisibility(View.VISIBLE);
                    }
                }, 50);
                etSearch.setText("");
                break;

            case R.id.ivViewCart:
                if (customerSessionManager.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, MyCartActivity.class);
                    startActivity(i);
                }
                break;
        }
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.search_speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, getString(R.string.search_speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etSearch.setText(result.get(0));
                }
                break;
            }
        }
    }

    public void getProductsDataFromAdapter(String response, int pos) {
        //getProductsData();
        Log.d("pos", "Val " + pos);
        Log.d("Response", response);

        try {
            JSONObject jsonObject = new JSONObject(response);

            ArrayList<String> listProductImages = null;
            ArrayList<ProductVariance> listProductVariance = null;
            ArrayList<String> listProductAttributeImages = null;

            JSONArray arrProducts = jsonObject.optJSONArray(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ARRAY);
            if (arrProducts.length() > 0) {
                for (int i = 0; i < arrProducts.length(); i++) {
                    JSONObject productObject = arrProducts.optJSONObject(i);
                    String product_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ID);
                    String product_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_NAME);
                    String category_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CATEGORY_NAME);
                    String sub_category_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SUB_CATEGORY_NAME);
                    String has_attributes = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_HAS_ATTRIBUTES);
                    String attribute_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_ID);
                    String attribute_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_STRING);
                    String manage_stock = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_MANAGE_STOCK);
                    String stock_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_STOCK_QTY);
                    String image_path = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGE_PATH);
                    String thumb_url = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_THUMB_PATH);
                    String add_to_cart = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_ADD_TO_CART);
                    String cart_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_STRING);
                    String cart_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_ID);
                    String cart_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_CART_QTY);
                    String wishlist_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_WISHLIST_ID);
                    String market_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_MARKET_PRICE);
                    String selling_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SELLING_PRICE);
                    String saving_percentage = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SAVING_PERCENTAGE);
                    String saving_price = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_SAVING_PRICE);
                    String product_details = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_DETAILS);
                    String product_is_active = productObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IS_ACTIVE);
                    String total_cart_qty = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_TOTAL_QTY);
                    String last_inserted_cart = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_LAST_ID);
                    String last_inserted_cart_name = productObject.optString(JsonFields.PRODUCTS_RESPONSE_CART_LAST_NAME);
                    String has_offer = productObject.optString(JsonFields.PRODUCTS_RESPONSE_HAS_OFFER);
                    String has_offer_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_OFFER_STRING);
                    String offer_id = productObject.optString(JsonFields.PRODUCTS_RESPONSE_OFFER_ID);
                    String is_featured = productObject.optString(JsonFields.PRODUCTS_RESPONSE_IS_FEATURED);
                    String featured_string = productObject.optString(JsonFields.PRODUCTS_RESPONSE_FEATURED_STRING);

                    JSONArray arrProductImages = productObject.optJSONArray(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGES_ARRAY);
                    if (arrProductImages.length() > 0) {
                        listProductImages = new ArrayList<>();
                        for (int j = 0; j < arrProductImages.length(); j++) {
                            JSONObject productAttributesObject = arrProductImages.optJSONObject(j);
                            String ProductImage = productAttributesObject.optString(JsonFields.PRODUCTS_RESPONSE_PRODUCT_IMAGE);
                            listProductImages.add(ProductImage);
                        }
                    }

                    JSONArray arrProductAttributes = productObject.optJSONArray(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ARRAY);
                    if (arrProductAttributes.length() > 0) {
                        listProductVariance = new ArrayList<>();
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
                            String product_attribute_is_featured = productObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IS_FEATURED);
                            String product_attribute_feature_string = productObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_FEATURE_STRING);

                            JSONArray arrProductAttributeImages = productAttributesObject.optJSONArray(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGES_ARRAY);
                            if (arrProductAttributes.length() > 0) {
                                listProductAttributeImages = new ArrayList<>();
                                for (int l = 0; l < arrProductAttributeImages.length(); l++) {
                                    JSONObject productAttributesImagesObject = arrProductAttributeImages.optJSONObject(l);
                                    String ProductAttributeImage = productAttributesImagesObject.optString(JsonFields.PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGE);
                                    listProductAttributeImages.add(ProductAttributeImage);
                                }
                            }
                            listProductVariance.add(new ProductVariance(product_attribute_id, product_attribute_string, product_attribute_group_id, product_attribute_group_name, product_attribute_value_id, product_attribute_value_name, product_attribute_market_price, product_attribute_selling_price, product_attribute_add_to_cart, product_attribute_cart_string, product_attribute_is_active, product_attribute_cart_id, product_attribute_cart_qty, product_attribute_manage_stock, product_attribute_stock_qty, product_attribute_wishlist_id, product_attribute_saving_price, product_attribute_saving_percentage, product_attribute_image_path, product_attribute_thumb_path, product_attribute_has_offer, product_attribute_offer_string, product_attribute_offer_id, product_attribute_is_featured, product_attribute_feature_string, listProductAttributeImages));
                        }
                    }
                    listProducts.set(pos, new Products(product_id, product_name, category_name, sub_category_name, has_attributes, attribute_id, attribute_string, manage_stock, stock_qty, image_path, thumb_url, add_to_cart, cart_string, cart_id, cart_qty, wishlist_id, market_price, selling_price, saving_percentage, saving_price, product_details, product_is_active, total_cart_qty, last_inserted_cart, last_inserted_cart_name, has_offer, has_offer_string, offer_id, is_featured, featured_string, listProductImages, listProductVariance));
                }
                productsAdapter.notifyItemChanged(pos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPageValue() {

    }

}