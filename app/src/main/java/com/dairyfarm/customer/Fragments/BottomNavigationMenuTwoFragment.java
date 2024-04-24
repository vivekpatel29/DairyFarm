package com.dairyfarm.customer.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dairyfarm.customer.Activity.HomeActivity;
import com.dairyfarm.customer.Activity.LogoutActivity;
import com.dairyfarm.customer.Adapter.CategoryAdapter;
import com.dairyfarm.customer.Model.Category;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.BottomNavigationMenuSelectedItemIDManager;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_TWO_STRING;

public class BottomNavigationMenuTwoFragment extends Fragment implements View.OnClickListener {
    View view;
    Context mContext;

    TextView tvCategoryTitle;

    LinearLayoutManager linearLayoutManager;

    ArrayList<Category> listCategory = new ArrayList<>();
    CategoryAdapter categoryAdapter = new CategoryAdapter(listCategory, "0");

    RecyclerView rvSelectCategory;
    EditText etSearch;

    LinearLayout llSelectCategoryMaster, llNoInternetConnection;
    Button btnRetry;

    LinearLayout llSelectCategory, llError, llNoRecordFound;

    TextView tvMessage, tvNoRecordFoundMessage;
    String Message;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    public int currentPageValue = 1;
    public int searchCurrentPageValue = 1;

    CustomerSessionManager customerSessionManager;
    AtClass atClass;

    LinearLayout llCategory;

    ImageView ivCategoryVoiceSearch, iVCategoryClearSearchResults;
    LinearLayout llProgressLayout;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    BottomNavigationMenuSelectedItemIDManager bottomNavigationMenuSelectedItemIDManager;
    SwipeRefreshLayout swipeToRefreshLayout;


    public BottomNavigationMenuTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bottom_navigation_menu_two, container, false);
        mContext = view.getContext();

        tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);

        llProgressLayout = view.findViewById(R.id.llProgressLayout);

        ivCategoryVoiceSearch = view.findViewById(R.id.ivCategoryVoiceSearch);
        ivCategoryVoiceSearch.setOnClickListener(this);
        iVCategoryClearSearchResults = view.findViewById(R.id.iVCategoryClearSearchResults);
        iVCategoryClearSearchResults.setOnClickListener(this);

        llCategory = view.findViewById(R.id.llCategory);

        atClass = new AtClass();
        customerSessionManager = new CustomerSessionManager(getActivity());
        bottomNavigationMenuSelectedItemIDManager = new BottomNavigationMenuSelectedItemIDManager(getActivity());

        categoryAdapter = new CategoryAdapter(listCategory, "0");

        llSelectCategoryMaster = view.findViewById(R.id.llSelectCategoryMaster);
        llNoInternetConnection = view.findViewById(R.id.llNoInternetConnection);

        llSelectCategory = view.findViewById(R.id.llSelectCategory);
        llError = view.findViewById(R.id.llError);
        llNoRecordFound = view.findViewById(R.id.llNoRecordsFound);

        tvMessage = view.findViewById(R.id.tvMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        tvNoRecordFoundMessage = view.findViewById(R.id.tvNoRecordFoundMessage);

        etSearch = view.findViewById(R.id.etCategorySearch);


        rvSelectCategory = view.findViewById(R.id.rvSelectCategory);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset_large);
        rvSelectCategory.addItemDecoration(itemDecoration);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvSelectCategory.setLayoutManager(linearLayoutManager);
        rvSelectCategory.setAdapter(categoryAdapter);

        setTextChangeListeners();
        //setBlogsData();
        setUpSwipeToRefreshLayout();

        if (atClass.isNetworkAvailable(getActivity())) {
            if (etSearch.getText().toString().trim() != null && !etSearch.getText().toString().trim().isEmpty() && !etSearch.getText().toString().trim().equals("")) {
                searchCurrentPageValue = 1;
                getCategoriesData(etSearch.getText().toString().trim(), "3");
            } else {
                setPagination();
            }
        } else {
            setNoInternetConnection();
        }
        return view;
    }

    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = view.findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);


        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                RefreshScreen();

            }
        });

    }

    private void setNoInternetConnection() {
        llNoInternetConnection.setVisibility(View.VISIBLE);
        llSelectCategoryMaster.setVisibility(View.GONE);
        llCategory.setVisibility(View.GONE);
    }

    private void setPagination() {
        rvSelectCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (atClass.isNetworkAvailable(getActivity())) {
                        currentPageValue++;
                        searchCurrentPageValue++;
                        getCategoriesData("", "2");
                    } else {
                        setNoInternetConnection();
                    }
                }
            }
        });

        if (atClass.isNetworkAvailable(getActivity())) {
            getCategoriesData("", "1");
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
                if (!atClass.isNetworkAvailable(getActivity())) {
                    setNoInternetConnection();
                } else {
                    iVCategoryClearSearchResults.setVisibility(View.VISIBLE);
                    ivCategoryVoiceSearch.setVisibility(View.GONE);
                    if (etSearch.getText().toString().trim() != null && !etSearch.getText().toString().trim().equals("") && !etSearch.getText().toString().trim().equals("")) {
                        searchCurrentPageValue = 1;
                        getCategoriesData(charSequence.toString(), "3");
                    } else {
                        searchCurrentPageValue = 1;
                        currentPageValue = 1;
                        getCategoriesData("", "3");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input

            }
        });
    }


    /*private void setBlogsData() {
        listBlogs.add(new Blogs("1","20+ Awesome Open-Source Android Apps To Boost Your Development Skills","","https://miro.medium.com/max/3240/1*sWR1wIxNOIbnnWMXaqUDZg.jpeg","0","0","10.2K","4K","Created On Jan 21, 2017",""));
        listBlogs.add(new Blogs("2","25 new Android libraries which you definitely want to try at the beginning of 2020","","https://miro.medium.com/max/3240/1*3oNgt5C2JC5FucjUm2SPxg.jpeg","0","1","19.5K","8K","Created On Feb 14, 2017",""));
        listBlogs.add(new Blogs("3","Learning Android Development in 2020 [Beginner’s Edition]","","https://miro.medium.com/max/2560/1*MEKuIso-FD65MzVhcpanKQ.png","0","0","14.8K","3K","Created On Jan 5, 2018",""));
        listBlogs.add(new Blogs("4","30+ Bite-Sized Pro Tips to Become a Better Android Developer","","https://miro.medium.com/max/3240/1*4VewQGeBOjKnYDBDeJbVLw.jpeg","0","1","8.3K","500","Created On Aug 2, 2019",""));
        listBlogs.add(new Blogs("5","50+ Ultimate Resources to Master Android Development","","https://miro.medium.com/max/3240/1*TYH3QG6gAriV0KFxGAP-rg.jpeg","0","0","4.2K","100","Created On Aug 18, 2018",""));
        blogsAdapter.notifyDataSetChanged();

        llSelectBlogMaster.setVisibility(View.VISIBLE);
        llNoInternetConnection.setVisibility(View.GONE);

        llBlog.setVisibility(View.VISIBLE);
        llSelectBlog.setVisibility(View.VISIBLE);
        llNoRecordFound.setVisibility(View.GONE);
        llError.setVisibility(View.GONE);
    }*/

    public void getCategoriesData(final String SearchQuery, String Status) {
        llProgressLayout.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CATEGORIES_URL, new Response.Listener<String>() {
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(getActivity());
                params.put(JsonFields.CATEGORY_REQUEST_PARAMS_CATEGORY_SEARCH_QUERY, SearchQuery);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                    listCategory.clear();
                } else if (searchCurrentPageValue == 1 && Status.equals("3")) {
                    listCategory.clear();
                }

                llProgressLayout.setVisibility(View.GONE);
                listCategory.clear();
                JSONArray arrCategories = jsonObject.optJSONArray(JsonFields.CATEGORY_RESPONSE_CATEGORY_ARRAY);
                if (arrCategories.length() > 0) {
                    for (int j = 0; j < arrCategories.length(); j++) {
                        JSONObject cartProductObject = arrCategories.optJSONObject(j);
                        String category_id = cartProductObject.optString(JsonFields.CATEGORY_RESPONSE_CATEGORY_ID);
                        String category_name = cartProductObject.optString(JsonFields.CATEGORY_RESPONSE_CATEGORY_NAME);
                        String category_image_url = cartProductObject.optString(JsonFields.CATEGORY_RESPONSE_CATEGORY_IMAGE);
                        //String is_publish = cartProductObject.optString(JsonFields.DASHBOARD_CATEGORY_RESPONSE_CATEGORY_IS_PUBLISH);
                        //String is_publish_string = cartProductObject.optString(JsonFields.DASHBOARD_CATEGORY_RESPONSE_CATEGORY_IS_PUBLISH_STRING);

                        listCategory.add(new Category(category_id, category_name, category_image_url));
                    }

                    categoryAdapter.notifyDataSetChanged();

                    llSelectCategoryMaster.setVisibility(View.VISIBLE);
                    llNoInternetConnection.setVisibility(View.GONE);

                    llCategory.setVisibility(View.VISIBLE);
                    llSelectCategory.setVisibility(View.VISIBLE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llError.setVisibility(View.GONE);
                } else {
                    llCategory.setVisibility(View.VISIBLE);
                    llSelectCategoryMaster.setVisibility(View.VISIBLE);
                    llNoInternetConnection.setVisibility(View.GONE);

                    llSelectCategory.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llError.setVisibility(View.GONE);
                    tvMessage.setText(getString(R.string.common_something_went_wrong));
                }
            } else if (flag == 2) {
                llProgressLayout.setVisibility(View.GONE);
                llCategory.setVisibility(View.VISIBLE);
                llSelectCategoryMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);
            } else if (flag == 3) {
                llProgressLayout.setVisibility(View.GONE);
                customerSessionManager.logout();
                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(getActivity(), LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                getActivity().finish();
            } else if (flag == 4) {
                llProgressLayout.setVisibility(View.GONE);
                llCategory.setVisibility(View.VISIBLE);
                llSelectCategoryMaster.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                llSelectCategory.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);

            } else {
                llProgressLayout.setVisibility(View.GONE);
                llCategory.setVisibility(View.VISIBLE);
                llSelectCategory.setVisibility(View.VISIBLE);
                llNoInternetConnection.setVisibility(View.GONE);

                if (SearchQuery != null && !SearchQuery.isEmpty() && !SearchQuery.equals("")) {
                    llSelectCategory.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.VISIBLE);
                    llError.setVisibility(View.GONE);
                    tvNoRecordFoundMessage.setText(Message);
                } else {
                    llSelectCategory.setVisibility(View.GONE);
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

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(getActivity())) {
                    RefreshScreen();
                } else {
                    setNoInternetConnection();
                }
                break;

            case R.id.ivCategoryVoiceSearch:
                promptSpeechInput();
                break;

            case R.id.iVCategoryClearSearchResults:
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        iVCategoryClearSearchResults.setVisibility(View.GONE);
                        ivCategoryVoiceSearch.setVisibility(View.VISIBLE);
                    }
                }, 50);
                etSearch.setText("");
                break;
        }
    }

    private void RefreshScreen() {
        Intent i = new Intent(getActivity(), HomeActivity.class);
        bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_TWO_STRING);
        startActivity(i);
        getActivity().finish();
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
            Toast.makeText(mContext, getString(R.string.search_speech_not_supported), Toast.LENGTH_SHORT).show();
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
