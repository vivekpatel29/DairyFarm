package com.dairyfarm.customer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.dairyfarm.customer.Adapter.HomeRecyclerViewSliderAdapter;
import com.dairyfarm.customer.Model.Category;
import com.dairyfarm.customer.Model.HomeSlider;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.BottomNavigationMenuSelectedItemIDManager;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_ONE_STRING;

public class BottomNavigationMenuOneFragment extends Fragment implements View.OnClickListener {

    View view;
    Context context;


    LinearLayout llHome, llHomeContentOne, llHomeContentTwo, llHomeContentThree, llError, llNoInternetConnection;
    Button btnRetry;
    TextView tvMessage;

    EditText etSearch;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;
    CustomerSessionManager customerSessionManager;
    SwipeRefreshLayout swipeToRefreshLayout;
    BottomNavigationMenuSelectedItemIDManager bottomNavigationMenuSelectedItemIDManager;

    String HomeContentAvailable, HomeContentOneAvailable, HomeContentTwoAvailable, HomeContentThreeAvailable;
    String HomeContentOneTitle, HomeContentTwoTitle, HomeContentThreeTitle;

    ArrayList<Category> listCategory = new ArrayList<>();
    CategoryAdapter categoryAdapter = new CategoryAdapter(listCategory, "1");
    Category category;

    ArrayList<HomeSlider> listRecyclerViewSlider = new ArrayList<>();
    HomeRecyclerViewSliderAdapter homeRecyclerViewSliderAdapter = new HomeRecyclerViewSliderAdapter(listRecyclerViewSlider);
    HomeSlider homeSlider;

    RecyclerView rVSlider;
    RecyclerView rvCategory;

    TextView tvDeliveryTime, tvCategoryTitle;


    public BottomNavigationMenuOneFragment() {
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
        view = inflater.inflate(R.layout.fragment_bottom_navigation_menu_one, container, false);

        atClass = new AtClass();
        progressDialogHandler = new ProgressDialogHandler(getActivity());
        bottomNavigationMenuSelectedItemIDManager = new BottomNavigationMenuSelectedItemIDManager(getActivity());
        customerSessionManager = new CustomerSessionManager(getActivity());

        tvDeliveryTime = view.findViewById(R.id.tvDeliveryTime);
        tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);

        llHome = view.findViewById(R.id.llHome);
        llHomeContentOne = view.findViewById(R.id.llHomeContentOne);
        llHomeContentTwo = view.findViewById(R.id.llHomeContentTwo);
        llHomeContentThree = view.findViewById(R.id.llHomeContentThree);
        llError = view.findViewById(R.id.llError);
        llNoInternetConnection = view.findViewById(R.id.llNoInternetConnection);

        btnRetry = view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        tvMessage = view.findViewById(R.id.tvMessage);

        etSearch = view.findViewById(R.id.etSearch);
        atClass.getNoInputTextInputActivityRequest(etSearch, ((HomeActivity) context));

        rVSlider = view.findViewById(R.id.rVSlider);
        ItemOffsetDecoration itemDecoration1 = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        rVSlider.addItemDecoration(itemDecoration1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rVSlider.setLayoutManager(linearLayoutManager1);
        rVSlider.setAdapter(homeRecyclerViewSliderAdapter);

        rvCategory = view.findViewById(R.id.rvCategory);
        ItemOffsetDecoration itemDecoration2 = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        rvCategory.addItemDecoration(itemDecoration2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvCategory.setLayoutManager(gridLayoutManager);
        rvCategory.setAdapter(categoryAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpSwipeToRefreshLayout();
        if (atClass.isNetworkAvailable(getActivity())) {
            getDashboardData();
        } else {
            setNoInternetConnection();
        }
    }

    private void getDashboardData() {
        progressDialogHandler.showPopupProgressSpinner(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
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
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler.showPopupProgressSpinner(false);

                listCategory.clear();
                listRecyclerViewSlider.clear();

                HomeContentAvailable = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_AVAILABLE);
                HomeContentOneAvailable = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_ONE_AVAILABLE);
                HomeContentTwoAvailable = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_TWO_AVAILABLE);
                HomeContentThreeAvailable = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_THREE_AVAILABLE);


                if (atClass.CheckEmptyString(getActivity(), HomeContentAvailable, "")) {
                    if (HomeContentAvailable.equals("1")) {
                        setHome();
                        if (atClass.CheckEmptyString(getActivity(), HomeContentOneAvailable, "")) {
                            if (HomeContentOneAvailable.equals("1")) {
                                HomeContentOneTitle = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_ONE_TITLE);

                                listRecyclerViewSlider.clear();
                                JSONArray arrSlider = jsonObject.optJSONArray(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_ONE_SLIDER_ARRAY);
                                if (arrSlider.length() > 0) {
                                    for (int i = 0; i < arrSlider.length(); i++) {
                                        JSONObject sliderObject = arrSlider.optJSONObject(i);
                                        String slider_id = sliderObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_ONE_SLIDER_ID);
                                        //String slider_type = sliderObject.optString(JsonFields.DASHBOARD_SLIDER_RESPONSE_SLIDER_TYPE);
                                        String image_url = sliderObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_ONE_SLIDER_IMAGE);

                                        listRecyclerViewSlider.add(new HomeSlider(slider_id, "", "", "Image", "", image_url));
                                        //listViewPagerSlider.add(new HomeSlider(slider_id, "", "", slider_type, "Image", image_url));
                                    }

                                    homeRecyclerViewSliderAdapter.notifyDataSetChanged();

                                    if (listRecyclerViewSlider != null && !listRecyclerViewSlider.isEmpty() && listRecyclerViewSlider.size()>0) {
                                        llHomeContentOne.setVisibility(View.VISIBLE);
                                    } else {
                                        llHomeContentOne.setVisibility(View.GONE);

                                    }
                                }
                            } else {
                                llHomeContentOne.setVisibility(View.GONE);
                            }
                        } else {
                            llHomeContentOne.setVisibility(View.GONE);
                        }


                        if (atClass.CheckEmptyString(getActivity(), HomeContentTwoAvailable, "")) {
                            if (HomeContentTwoAvailable.equals("1")) {
                                HomeContentTwoTitle = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_TWO_TITLE);

                                String HomeContentTwoContent = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_TWO_CONTENT);
                                if (atClass.CheckEmptyString(getActivity(), HomeContentTwoContent, "")) {
                                    llHomeContentTwo.setVisibility(View.VISIBLE);
                                    atClass.setUpTextToView("0", tvDeliveryTime, HomeContentTwoContent);
                                } else {
                                    llHomeContentTwo.setVisibility(View.GONE);
                                }
                            } else {
                                llHomeContentTwo.setVisibility(View.GONE);
                            }
                        } else {
                            llHomeContentTwo.setVisibility(View.GONE);
                        }


                        if (atClass.CheckEmptyString(getActivity(), HomeContentThreeAvailable, "")) {
                            if (HomeContentThreeAvailable.equals("1")) {
                                HomeContentThreeTitle = jsonObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_THREE_TITLE);

                                if (atClass.CheckEmptyString(getActivity(), HomeContentThreeTitle, "")) {
                                    atClass.setUpTextToView("0", tvCategoryTitle, HomeContentThreeTitle);
                                } else {
                                    tvCategoryTitle.setVisibility(View.INVISIBLE);
                                }

                                listCategory.clear();
                                JSONArray arrCategories = jsonObject.optJSONArray(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_ARRAY);
                                if (arrCategories.length() > 0) {
                                    for (int j = 0; j < arrCategories.length(); j++) {
                                        JSONObject cartProductObject = arrCategories.optJSONObject(j);
                                        String category_id = cartProductObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_ID);
                                        String category_name = cartProductObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_NAME);
                                        String category_image_url = cartProductObject.optString(JsonFields.DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_IMAGE);
                                        //String is_publish = cartProductObject.optString(JsonFields.DASHBOARD_CATEGORY_RESPONSE_CATEGORY_IS_PUBLISH);
                                        //String is_publish_string = cartProductObject.optString(JsonFields.DASHBOARD_CATEGORY_RESPONSE_CATEGORY_IS_PUBLISH_STRING);

                                        listCategory.add(new Category(category_id, category_name, category_image_url));
                                    }
                                    categoryAdapter.notifyDataSetChanged();


                                    if (listCategory != null && !listCategory.isEmpty() && listCategory.size()>0) {
                                        llHomeContentThree.setVisibility(View.VISIBLE);
                                    } else {
                                        llHomeContentThree.setVisibility(View.GONE);
                                    }
                                }

                            } else {
                                llHomeContentThree.setVisibility(View.GONE);
                            }
                        } else {
                            llHomeContentThree.setVisibility(View.GONE);
                        }
                    } else {
                        setError("");
                    }
                } else {
                    setError("");
                }
            } else if (flag == 3) {
                progressDialogHandler.showPopupProgressSpinner(false);
                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(getActivity(), LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                startActivity(i);
                getActivity().finish();
            } else {
                progressDialogHandler.showPopupProgressSpinner(false);
                setError(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void RefreshScreen() {
        Intent i = new Intent(getActivity(), HomeActivity.class);
        bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_ONE_STRING);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivViewCart:
                if (customerSessionManager.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    /*Intent i = new Intent(mContext, IntroductionSliderLoginActivity.class);
                    i.putExtra("isFrom", "2");
                    i.putExtra("FromScreenName", "HomeFragment");
                    i.putExtra("ToScreenName", "HomeActivity");
                    startActivity(i);*/
                } else {
                    /*Intent i1 = new Intent(getActivity(), MyCartActivity.class);
                    //i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);*/
                }
                break;

            case R.id.ivViewWishList:
                if (customerSessionManager.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    /*Intent i = new Intent(mContext, IntroductionSliderLoginActivity.class);
                    i.putExtra("isFrom", "2");
                    i.putExtra("FromScreenName", "HomeFragment");
                    i.putExtra("ToScreenName", "HomeActivity");
                    startActivity(i);*/
                } else {
                    /*Intent i2 = new Intent(mContext, MyWishlistActivity.class);
                    //i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i2);*/
                }
                break;

            case R.id.etSearch:
                /*Intent i3 = new Intent(mContext, SearchProductsActivity.class);
                //i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i3);*/
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(getActivity())) {
                    RefreshScreen();
                } else {
                    setNoInternetConnection();
                }
                break;
        }
    }

    private void setHome() {
        llHome.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
        llNoInternetConnection.setVisibility(View.GONE);
    }

    private void setError(String Message) {
        llHome.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        llNoInternetConnection.setVisibility(View.GONE);

        if (atClass.CheckEmptyString(getActivity(), Message, "")) {
            atClass.setUpTextToView("0", tvMessage, Message);
        } else {
            atClass.setUpTextToView("0", tvMessage, getString(R.string.common_something_went_wrong));
        }
    }

    private void setNoInternetConnection() {
        llHome.setVisibility(View.GONE);
        llError.setVisibility(View.GONE);
        llNoInternetConnection.setVisibility(View.VISIBLE);
    }
}