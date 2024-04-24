package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
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
import com.dairyfarm.customer.Adapter.SubCategoriesAdapter;
import com.dairyfarm.customer.Model.SubCategories;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class SubCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;

    LinearLayoutManager linearLayoutManager;
    ArrayList<SubCategories> listSubCategories = new ArrayList<>();
    SubCategoriesAdapter subCategoriesAdapter = new SubCategoriesAdapter(listSubCategories);

    RecyclerView rvSelectSubCategory;
    EditText etSearch;

    LinearLayout llSelectSubCategoryMaster, llNoInternetConnection;
    Button btnRetry;

    LinearLayout llSelectSubCategory, llError, llNoRecordFound;

    TextView tvMessage, tvNoRecordFoundMessage;
    String Message;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    public int currentPageValue = 1;
    public int searchCurrentPageValue = 1;

    CustomerSessionManager customerSessionManager;
    AtClass atClass;

    LinearLayout llSubCategory;

    ImageView ivVoiceSearch, iVClearSearchResults;
    LinearLayout llProgressLayout;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    SwipeRefreshLayout swipeToRefreshLayout;

    TextView tvToolbarTitle;
    String CategoryID, CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        subCategoriesAdapter = new SubCategoriesAdapter(listSubCategories);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        llProgressLayout = findViewById(R.id.llProgressLayout);

        ivVoiceSearch = findViewById(R.id.ivVoiceSearch);
        ivVoiceSearch.setOnClickListener(this);
        iVClearSearchResults = findViewById(R.id.iVClearSearchResults);
        iVClearSearchResults.setOnClickListener(this);

        llSubCategory = findViewById(R.id.llSubCategory);

        atClass = new AtClass();
        customerSessionManager = new CustomerSessionManager(this);

        llSelectSubCategoryMaster = findViewById(R.id.llSelectSubCategoryMaster);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);

        llSelectSubCategory = findViewById(R.id.llSelectSubCategory);
        llError = findViewById(R.id.llError);
        llNoRecordFound = findViewById(R.id.llNoRecordsFound);

        tvMessage = findViewById(R.id.tvMessage);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        tvNoRecordFoundMessage = findViewById(R.id.tvNoRecordFoundMessage);

        etSearch = findViewById(R.id.etSearch);

        rvSelectSubCategory = findViewById(R.id.rvSelectSubCategory);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_large);
        rvSelectSubCategory.addItemDecoration(itemDecoration);
        linearLayoutManager = new LinearLayoutManager(this);
        rvSelectSubCategory.setLayoutManager(linearLayoutManager);
        rvSelectSubCategory.setAdapter(subCategoriesAdapter);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setSelected(true);

        setTextChangeListeners();
        setUpSwipeToRefreshLayout();

        Intent i = getIntent();
        if (i.hasExtra("CategoryID")) {
            CategoryID = i.getStringExtra("CategoryID");
        } else {
            CategoryID = "";
        }

        if (i.hasExtra("CategoryName")) {
            CategoryName = i.getStringExtra("CategoryName");
        } else {
            CategoryName = "";
        }

        if (atClass.isNetworkAvailable(this)) {
            if (atClass.CheckEmptyString(this, CategoryID, "")
                    && atClass.CheckEmptyString(this, CategoryName, "")) {

                setToolbarTitleAndSearchHint();

                if (etSearch.getText().toString().trim() != null && !etSearch.getText().toString().trim().isEmpty() && !etSearch.getText().toString().trim().equals("")) {
                    searchCurrentPageValue = 1;
                    getSubCategorysData(etSearch.getText().toString().trim(), "3");
                } else {
                    setPagination();
                }
            } else {
                llSubCategory.setVisibility(View.VISIBLE);
                llSelectSubCategoryMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                llSelectSubCategory.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);
                tvMessage.setText(getString(R.string.common_something_went_wrong));
            }
        } else {
            setNoInternetConnection();
        }

        //setSubCategorysData();
        //setTopicName();
    }

    private void setToolbarTitleAndSearchHint() {
        atClass.setUpTextToView("1",tvToolbarTitle,CategoryName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            etSearch.setHint(Html.fromHtml("Search by "+CategoryName+ " name", Html.FROM_HTML_MODE_COMPACT));
        } else {
            etSearch.setHint(Html.fromHtml("Search by "+CategoryName+ " name"));
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
        rvSelectSubCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }

                int firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstPosition > 0) {
                    swipeToRefreshLayout.setEnabled(false);
                } else {
                    swipeToRefreshLayout.setEnabled(true);
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
                    if (atClass.isNetworkAvailable(SubCategoryActivity.this)) {
                        currentPageValue++;
                        searchCurrentPageValue++;
                        getSubCategorysData("", "2");
                    } else {
                        setNoInternetConnection();
                    }
                }
            }
        });

        if (atClass.isNetworkAvailable(SubCategoryActivity.this)) {
            getSubCategorysData("", "1");
        } else {
            setNoInternetConnection();
        }
    }

    private void setTextChangeListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!atClass.isNetworkAvailable(SubCategoryActivity.this)) {
                    llSubCategory.setVisibility(View.GONE);
                    llSelectSubCategoryMaster.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    iVClearSearchResults.setVisibility(View.VISIBLE);
                    ivVoiceSearch.setVisibility(View.GONE);
                    if (etSearch.getText().toString().trim() != null && !etSearch.getText().toString().trim().equals("") && !etSearch.getText().toString().trim().equals("")) {
                        searchCurrentPageValue = 1;
                        getSubCategorysData(charSequence.toString(), "3");
                    } else {
                        searchCurrentPageValue = 1;
                        currentPageValue = 1;
                        getSubCategorysData("", "3");
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
        Intent i = new Intent(SubCategoryActivity.this, SubCategoryActivity.class);
        i.putExtra("CategoryID",CategoryID);
        i.putExtra("CategoryName",CategoryName);
        startActivity(i);
        finish();
    }

    private void setNoInternetConnection() {
        llSubCategory.setVisibility(View.VISIBLE);
        llSelectSubCategoryMaster.setVisibility(View.GONE);
        llNoInternetConnection.setVisibility(View.VISIBLE);
    }

    public void getSubCategorysData(final String SearchQuery, String Status) {
        llProgressLayout.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SUB_CATEGORIES_URL, new Response.Listener<String>() {
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(SubCategoryActivity.this);
                params.put(JsonFields.SUB_CATEGORY_REQUEST_PARAMS_CATEGORY_ID, CategoryID);
                params.put(JsonFields.SUB_CATEGORY_REQUEST_PARAMS_SUB_CATEGORY_SEARCH_QUERY, SearchQuery);
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
        RequestQueue requestQueue = Volley.newRequestQueue(SubCategoryActivity.this);
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
                    listSubCategories.clear();
                } else if (searchCurrentPageValue == 1 && Status.equals("3")) {
                    listSubCategories.clear();
                }

                llProgressLayout.setVisibility(View.GONE);
                JSONArray arrSubCategories = jsonObject.optJSONArray(JsonFields.SUB_CATEGORY_RESPONSE_SUB_CATEGORY_ARRAY);
                if (arrSubCategories.length() > 0) {
                    for (int i = 0; i < arrSubCategories.length(); i++) {
                        JSONObject SubCategorysObject = arrSubCategories.optJSONObject(i);
                        String SubCategoryID = SubCategorysObject.optString(JsonFields.SUB_CATEGORY_RESPONSE_SUB_CATEGORY_ID);
                        String SubCategoryName = SubCategorysObject.optString(JsonFields.SUB_CATEGORY_RESPONSE_SUB_CATEGORY_NAME);
                        String SubCategoryImage = SubCategorysObject.optString(JsonFields.SUB_CATEGORY_RESPONSE_SUB_CATEGORY_IMAGE);


                        listSubCategories.add(new SubCategories(SubCategoryID, SubCategoryName, SubCategoryImage));
                    }
                    subCategoriesAdapter.notifyDataSetChanged();

                    llSelectSubCategoryMaster.setVisibility(View.VISIBLE);
                    llNoInternetConnection.setVisibility(View.GONE);

                    llSubCategory.setVisibility(View.VISIBLE);
                    llSelectSubCategory.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llError.setVisibility(View.GONE);

                } else {
                    llSubCategory.setVisibility(View.VISIBLE);
                    llSelectSubCategoryMaster.setVisibility(View.VISIBLE);
                    llNoInternetConnection.setVisibility(View.GONE);

                    llSelectSubCategory.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llError.setVisibility(View.GONE);
                    tvMessage.setText(getString(R.string.common_something_went_wrong));
                }
            } else if (flag == 2) {
                llProgressLayout.setVisibility(View.GONE);
                llSubCategory.setVisibility(View.VISIBLE);
                llSelectSubCategoryMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);
            } else if (flag == 3) {
                llProgressLayout.setVisibility(View.GONE);
                customerSessionManager.logout();
                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(SubCategoryActivity.this, LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else if (flag == 4) {
                llProgressLayout.setVisibility(View.GONE);
                llSubCategory.setVisibility(View.VISIBLE);
                llSelectSubCategoryMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                llSelectSubCategory.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);

            } else {
                llProgressLayout.setVisibility(View.GONE);
                llSubCategory.setVisibility(View.VISIBLE);
                llSelectSubCategoryMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                if (SearchQuery != null && !SearchQuery.isEmpty() && !SearchQuery.equals("")) {
                    llSelectSubCategory.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.VISIBLE);
                    llError.setVisibility(View.GONE);
                    tvNoRecordFoundMessage.setText(Message);
                } else {
                    llSelectSubCategory.setVisibility(View.GONE);
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

}
