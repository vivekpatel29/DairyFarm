package com.dairyfarm.customer.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.GPSTracker;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private static final int CHOOSE_PINCODE_REQUEST = 001;
    ImageView ivBack;

    TextInputEditText etName, etAddress, etPincode, etState, etTownCity, etCountryCode, etMobileNumber;
    TextView tvPincode;
    CheckBox ChkDefaultAddress;

    String AddressID;
    String PincodeID, PincodeName;

    LinearLayout llEditAddress, llNoInternetConnection, llNoRecordFound;
    Button btnRetry;
    TextView tvMessage;
    String Message;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;
    CustomerSessionManager customerSessionManager;

    String AddressName, Address, Locality, City, State, isDefault, MobileNumber, AddressType, AddressLatitude, AddressLongitude;

    Button btnEditAddress;

    RadioButton rbHome, rbOffice, rbOther;

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private TextView resultText;

    String Latitude = "", Longitude = "";
    GPSTracker gpsTracker;
    private static final int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    TextView tvAddressTypeSelectionError;

    private GoogleApiClient mGoogleApiClient;

    AutoCompleteTextView acetLocality;
    ArrayList<String> listPincodeID = new ArrayList<>();
    ArrayList<String> listPincode = new ArrayList<>();
    ArrayList<String> listArea = new ArrayList<>();
    ArrayList<String> listCity = new ArrayList<>();
    ArrayList<String> listState = new ArrayList<>();

    LinearLayout llProgressLayout;

    SwipeRefreshLayout swipeToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        atClass = new AtClass();

        llProgressLayout = findViewById(R.id.llProgressLayout);

        tvAddressTypeSelectionError = findViewById(R.id.tvAddressTypeSelectionError);

        rbHome = findViewById(R.id.rbHome);
        rbOffice = findViewById(R.id.rbOffice);
        rbOther = findViewById(R.id.rbOther);

        progressDialogHandler = new ProgressDialogHandler(this);
        customerSessionManager = new CustomerSessionManager(this);

        llEditAddress = findViewById(R.id.llEditAddress);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);
        llNoRecordFound = findViewById(R.id.llError);

        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

        tvMessage = findViewById(R.id.tvMessage);

        ivBack = findViewById(R.id.ivBack);

        etName = findViewById(R.id.et_name);
        etAddress = findViewById(R.id.et_address);
        acetLocality = findViewById(R.id.acetLocality);
        etPincode = findViewById(R.id.et_pincode);
        etState = findViewById(R.id.et_state);
        etTownCity = findViewById(R.id.et_town_city);
        etCountryCode = findViewById(R.id.et_country_code);
        etMobileNumber = findViewById(R.id.et_mobile_number);

        etCountryCode.setFocusable(false);
        etCountryCode.setFocusableInTouchMode(false);
        etCountryCode.setFocusableInTouchMode(false);

        etPincode.setFocusable(false);
        etPincode.setFocusableInTouchMode(false);
        etPincode.setFocusableInTouchMode(false);

        ChkDefaultAddress = findViewById(R.id.ChkDefaultAddress);

        etPincode.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        btnEditAddress = findViewById(R.id.btnEditAddress);
        btnEditAddress.setOnClickListener(this);

        Intent i = getIntent();

        if (i.hasExtra("AddressID")) {
            AddressID = i.getStringExtra("AddressID");
        }

        isDefault = "0";
        ChkDefaultAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkDefaultAddress.isChecked()) {
                    isDefault = "1";
                } else {
                    isDefault = "0";
                }
            }
        });

        rbHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbHome.setChecked(true);
                    rbOffice.setChecked(false);
                    rbOther.setChecked(false);
                    AddressType = "Home";
                    Log.d("Address Type", AddressType);

                    setHomeSelected();
                    setOfficeNotSelected();
                    setOtherNotSelected();
                }
            }
        });

        rbOffice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbOffice.setChecked(true);
                    rbHome.setChecked(false);
                    rbOther.setChecked(false);
                    AddressType = "Office";
                    Log.d("Address Type", AddressType);

                    setOfficeSelected();
                    setHomeNotSelected();
                    setOtherNotSelected();
                }
            }
        });

        rbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbOther.setChecked(true);
                    rbHome.setChecked(false);
                    rbOffice.setChecked(false);
                    AddressType = "Other";
                    Log.d("Address Type", AddressType);

                    setOtherSelected();
                    setHomeNotSelected();
                    setOfficeNotSelected();
                }
            }
        });

        if (AddressID != null && !AddressID.isEmpty() && !AddressID.equals("")) {
            if (atClass.isNetworkAvailable(this)) {
                getAddressDetails();
                //getAndSetCurrentLatLng();
                configureCameraIdle();

                //Log.e("AddressID",AddressID);
            } else {
                llNoInternetConnection.setVisibility(View.VISIBLE);
                llEditAddress.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.GONE);
            }
        } else {
            llNoRecordFound.setVisibility(View.VISIBLE);
            tvMessage.setText(getString(R.string.common_something_went_wrong));
        }

        setUpSwipeToRefreshLayout();

    }

    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                Intent i = new Intent(EditAddressActivity.this, EditAddressActivity.class);
                i.putExtra("AddressID", AddressID);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpOnTextChangeListenerForLocalityAndAdapter();
        acetLocality.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PincodeID = listPincodeID.get(position);
                PincodeName = listPincode.get(position);
                String Area = listArea.get(position);
                String City = listCity.get(position);
                String State = listState.get(position);

                if (Area != null && !Area.isEmpty() && !Area.equals("")) {
                    acetLocality.setText(Area);
                } else {
                    acetLocality.setText("");
                }

                if (City != null && !City.isEmpty() && !City.equals("")) {
                    etTownCity.setText(City);
                } else {
                    etTownCity.setText("");
                }

                if (State != null && !State.isEmpty() && !State.equals("")) {
                    etState.setText(State);
                } else {
                    etState.setText("");
                }

                if (PincodeName != null && !PincodeName.isEmpty() && !PincodeName.equals("")) {
                    etPincode.setText(PincodeName);
                } else {
                    etPincode.setText("");
                }

                // Do Whatever you want to do ;)
            }
        });
    }

    private void setUpOnTextChangeListenerForLocalityAndAdapter() {
        acetLocality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //retrieve datas
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (acetLocality.getText().toString().trim().length() > 0) {
                    getAreaData(acetLocality.getText().toString().trim());
                }
                Log.d("TAG", "area is " + listArea);
            }
        });
    }

    private void parseAreaJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                llProgressLayout.setVisibility(View.GONE);

                listPincodeID.clear();
                listPincode.clear();
                listArea.clear();
                listCity.clear();
                listState.clear();

                JSONArray arrAreas = jsonObject.optJSONArray(JsonFields.AREA_RESPONSE_AREA_ARRAY);
                if (arrAreas.length() > 0) {
                    for (int i = 0; i < arrAreas.length(); i++) {
                        JSONObject areasObject = arrAreas.optJSONObject(i);
                        String pincode_id = areasObject.optString(JsonFields.AREA_RESPONSE_PINCODE_ID);
                        String pincode = areasObject.optString(JsonFields.AREA_RESPONSE_PINCODE);
                        String area = areasObject.optString(JsonFields.AREA_RESPONSE_AREA);
                        String city = areasObject.optString(JsonFields.AREA_RESPONSE_CITY);
                        String state = areasObject.optString(JsonFields.AREA_RESPONSE_STATE);

                        listPincodeID.add(pincode_id);
                        listPincode.add(pincode);
                        listArea.add(area);
                        listCity.add(city);
                        listState.add(state);
                    }

                    final ArrayAdapter<String> localityAutoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listArea);
                    acetLocality.setAdapter(localityAutoAdapter);
                }

            } else if (flag == 3) {
                llProgressLayout.setVisibility(View.GONE);

                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(EditAddressActivity.this, LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                startActivity(i);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                finish();
            } else {
                llProgressLayout.setVisibility(View.GONE);
                Toast.makeText(EditAddressActivity.this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAreaData(String term) {
        llProgressLayout.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ADDRESS_AREA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseAreaJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llProgressLayout.setVisibility(View.GONE);
                if (error instanceof NetworkError) {
                    Toast.makeText(EditAddressActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(EditAddressActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(EditAddressActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(EditAddressActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditAddressActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(EditAddressActivity.this);
                params.put(JsonFields.AREA_REQUEST_PARAMS_SEARCH_QUERY, term);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditAddressActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);

        if (Latitude != null && !Latitude.isEmpty() && !Latitude.equals("") && !Latitude.equals("null")
                && Longitude != null && !Longitude.isEmpty() && !Longitude.equals("") && !Longitude.equals("null")) {
            double lat = Double.parseDouble(Latitude);
            double lng = Double.parseDouble(Longitude);

            LatLng latLng = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }
    }

    private void configureCameraIdle() {

        if (atClass.isNetworkAvailable(this)) {
            onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng latLng = mMap.getCameraPosition().target;
                    Geocoder geocoder = new Geocoder(EditAddressActivity.this);
                    try {
                        if (atClass.isNetworkAvailable(EditAddressActivity.this)) {
                            List<android.location.Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if (addressList != null && addressList.size() > 0) {
                                String locality = addressList.get(0).getAddressLine(0);
                                String country = addressList.get(0).getCountryName();

                                if (locality != null && !locality.equals("") && !locality.isEmpty()) {
                                    resultText.setText(locality);
                                    Latitude = String.valueOf(latLng.latitude);
                                    Longitude = String.valueOf(latLng.longitude);
                                } else {
                                    Log.d("Locality", "Not Valid");
                                    ShowCanNotDetectLocationDialog();
                                    Latitude = "";
                                    Longitude = "";
                                }
                            } else {
                                ShowCanNotDetectLocationDialog();
                                Latitude = "";
                                Longitude = "";
                                Log.d("Locality", "Not Valid");
                            }
                        } else {
                            ShowCanNotDetectLocationDialog();
                            Latitude = "";
                            Longitude = "";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

        } else {
            ShowCanNotDetectLocationDialog();
            Latitude = "";
            Longitude = "";
        }

    }

    private void ShowCanNotDetectLocationDialog() {
        final Dialog AddressErrorDialog = new Dialog(this);
        AddressErrorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //MyDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        AddressErrorDialog.setContentView(R.layout.adddress_maps_dialog_layout);

        TextView tvTitle = AddressErrorDialog.findViewById(R.id.tvTitle);
        TextView tvMessage = AddressErrorDialog.findViewById(R.id.tvMessage);

        Button btnRetry = AddressErrorDialog.findViewById(R.id.btnRetry);

        tvTitle.setText(getString(R.string.address_maps_error_title_text));
        tvMessage.setText(getString(R.string.address_maps_error_message_text));
        btnRetry.setText(getString(R.string.address_maps_error_retry_button_text));

        btnRetry.setEnabled(true);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressErrorDialog.dismiss();
                //getAndSetCurrentLatLng();
                //configureCameraIdle();
                Intent i = new Intent(EditAddressActivity.this, EditAddressActivity.class);
                i.putExtra("AddressID", AddressID);
                startActivity(i);
                finish();
            }
        });

        AddressErrorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AddressErrorDialog.setCancelable(false);
        AddressErrorDialog.setCanceledOnTouchOutside(false);
        AddressErrorDialog.show();
        AddressErrorDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void setHomeSelected() {
        rbHome.setButtonTintList(getResources().getColorStateList(R.color.colorDark));
        rbHome.setTextColor(getResources().getColor(R.color.colorDark));
    }

    private void setOfficeSelected() {
        rbOffice.setButtonTintList(getResources().getColorStateList(R.color.colorDark));
        rbOffice.setTextColor(getResources().getColor(R.color.colorDark));
    }

    private void setOtherSelected() {
        rbOther.setButtonTintList(getResources().getColorStateList(R.color.colorDark));
        rbOther.setTextColor(getResources().getColor(R.color.colorDark));
    }

    private void setHomeNotSelected() {
        rbHome.setButtonTintList(getResources().getColorStateList(R.color.colorBlackThree));
        rbHome.setTextColor(getResources().getColor(R.color.colorBlackThree));
    }

    private void setOfficeNotSelected() {
        rbOffice.setButtonTintList(getResources().getColorStateList(R.color.colorBlackThree));
        rbOffice.setTextColor(getResources().getColor(R.color.colorBlackThree));
    }

    private void setOtherNotSelected() {
        rbOther.setButtonTintList(getResources().getColorStateList(R.color.colorBlackThree));
        rbOther.setTextColor(getResources().getColor(R.color.colorBlackThree));
    }

    private void getAddressDetails() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ADDRESS_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof NoConnectionError) {
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else if (error instanceof TimeoutError) {
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                } else {
                    llEditAddress.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(EditAddressActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.ADDRESS_DETAILS_REQUEST_PARAMS_ADDRESS_ID, AddressID);
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
                JSONArray arrAddress = jsonObject.optJSONArray(JsonFields.ADDRESS_DETAILS_RESPONSE_ADDRESS_ARRAY);
                if (arrAddress.length() > 0) {
                    for (int i = 0; i < arrAddress.length(); i++) {
                        JSONObject addressObject = arrAddress.optJSONObject(i);
                        AddressID = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_ADDRESS_ID);
                        AddressName = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_NAME);
                        Address = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS);
                        Locality = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_DETAILS);
                        City = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_CITY);
                        State = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_STATE);
                        PincodeID = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_PINCODE_ID);
                        PincodeName = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_PINCODE);
                        isDefault = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_IS_DEFAULT);
                        MobileNumber = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_MOBILE_NUMBER);
                        AddressType = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_TYPE);
                        AddressLatitude = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_LATITUDE);
                        AddressLongitude = addressObject.optString(JsonFields.ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_LONGITUDE);
                        setData();
                    }
                }
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
                llEditAddress.setVisibility(View.GONE);
                llNoInternetConnection.setVisibility(View.GONE);
                llNoRecordFound.setVisibility(View.VISIBLE);
                tvMessage.setText(Message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        if (AddressName != null && !AddressName.isEmpty() && !AddressName.equals("")) {
            etName.setText(AddressName);
        } else {
            etName.setText("");
        }

        if (Address != null && !Address.isEmpty() && !Address.equals("")) {
            etAddress.setText(Address);
        } else {
            etAddress.setText("");
        }

        if (Locality != null && !Locality.isEmpty() && !Locality.equals("")) {
            acetLocality.setText(Locality);
        } else {
            acetLocality.setText("");
        }

        if (City != null && !City.isEmpty() && !City.equals("")) {
            etTownCity.setText(City);
        } else {
            etTownCity.setText("");
        }

        if (State != null && !State.isEmpty() && !State.equals("")) {
            etState.setText(State);
        } else {
            etState.setText("");
        }

        if (MobileNumber != null && !MobileNumber.isEmpty() && !MobileNumber.equals("")) {
            etMobileNumber.setText(MobileNumber);
        } else {
            etMobileNumber.setText("");
        }

        if (PincodeID != null && !PincodeID.isEmpty() && !PincodeID.equals("") &&
                PincodeName != null && !PincodeName.isEmpty() && !PincodeName.equals("")) {
            etPincode.setText(PincodeName);
        } else {
            etPincode.setText("");
        }

        if (isDefault != null && !isDefault.isEmpty() && !isDefault.equals("")) {
            if (isDefault.equals("1")) {
                ChkDefaultAddress.setChecked(true);
            } else {
                ChkDefaultAddress.setChecked(false);
            }
        } else {
            ChkDefaultAddress.setChecked(false);
        }

        if (AddressType != null && !AddressType.isEmpty() && !AddressType.equals("")) {
            if (AddressType.equalsIgnoreCase("Home")) {
                rbHome.setChecked(true);
                rbOffice.setChecked(false);
                rbOther.setChecked(false);
                AddressType = "Home";
                Log.d("Address Type", AddressType);

                setHomeSelected();
                setOfficeNotSelected();
                setOtherNotSelected();
            } else if (AddressType.equalsIgnoreCase("Office")) {
                rbHome.setChecked(false);
                rbOffice.setChecked(true);
                rbOther.setChecked(false);
                AddressType = "Office";
                Log.d("Address Type", AddressType);

                setHomeNotSelected();
                setOfficeSelected();
                setOtherNotSelected();

            } else if (AddressType.equalsIgnoreCase("Other")) {
                rbHome.setChecked(false);
                rbOffice.setChecked(false);
                rbOther.setChecked(true);
                AddressType = "Other";
                Log.d("Address Type", AddressType);

                setHomeNotSelected();
                setOfficeNotSelected();
                setOtherSelected();
            } else {
                rbHome.setChecked(false);
                rbOffice.setChecked(false);
                rbOther.setChecked(false);
                AddressType = "";

                setHomeNotSelected();
                setOfficeNotSelected();
                setOtherNotSelected();

            }
        } else {
            rbHome.setChecked(false);
            rbOffice.setChecked(false);
            rbOther.setChecked(false);
            AddressType = "";

            setHomeNotSelected();
            setOfficeNotSelected();
            setOtherNotSelected();
        }

        Latitude = AddressLatitude;
        Longitude = AddressLongitude;


        getAndSetCurrentLatLng();
        configureCameraIdle();
    }

    private void getAndSetCurrentLatLng() {
        try {
            if (ActivityCompat.checkSelfPermission(EditAddressActivity.this, mPermission)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditAddressActivity.this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (Latitude != null && !Latitude.isEmpty() && !Latitude.equals("")
                && Longitude != null && !Longitude.isEmpty() && !Longitude.equals("")) {
            new MapTask().execute();
        } else {
            if (gpsTracker.canGetLocation()) {
                Log.d("Inside If", "Yes");
                final double latitude = gpsTracker.getLatitude();
                final double longitude = gpsTracker.getLongitude();

                if (latitude != 0.0 && longitude != 0.0) {
                    Latitude = String.valueOf(latitude);
                    Longitude = String.valueOf(longitude);
                    new MapTask().execute();
                } else {
                    //Ahmedabad Location
                    Latitude = "23.0204978";
                    Longitude = "72.4396539";
                    new MapTask().execute();
                }
            } else {
                //Ahmedabad Location
                Log.d("Inside Else", "Yes");
                Latitude = "23.0204978";
                Longitude = "72.4396539";
                new MapTask().execute();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setMap();
    }

    private void setMap() {
        resultText = (TextView) findViewById(R.id.resultText);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public class MapTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isGooglePlayServiceAvailable()) {
                buildGoogleApiClient();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
        }
    }

    public boolean isGooglePlayServiceAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int googlePlayServicesAvailable = googleApiAvailability.isGooglePlayServicesAvailable(EditAddressActivity.this);
        if (googlePlayServicesAvailable != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(googlePlayServicesAvailable)) {
                googleApiAvailability.getErrorDialog(this, googlePlayServicesAvailable, 404).show();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(EditAddressActivity.this).addConnectionCallbacks(this).addConnectionCallbacks(this).addApi(LocationServices.API).addApi(Places.GEO_DATA_API).build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_pincode:
                Intent i2 = new Intent(EditAddressActivity.this, SelectPincodeActivity.class);
                i2.putExtra("PincodeID", PincodeID);
                i2.putExtra("isFrom", "2");
                startActivityForResult(i2, CHOOSE_PINCODE_REQUEST);
                break;

            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.btnEditAddress:
                if (atClass.isNetworkAvailable(this)) {
                    if (checkLatLng() && checkAddressType() && checkAddressID() && checkName() && checkAddress() && checkLocality() && checkPincode() && checkState() && checkTownCity() && checkMobile()) {
                        EditAddress();
                    }
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                }
                break;
            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i = new Intent(this, EditAddressActivity.class);
                    i.putExtra("AddressID", AddressID);
                    startActivity(i);
                    finish();
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                }
                break;
        }
    }

    private boolean checkAddressType() {
        boolean isAddressTypeValid = false;
        if (AddressType != null && !AddressType.isEmpty() && !AddressType.equals("")) {
            isAddressTypeValid = true;
            tvAddressTypeSelectionError.setVisibility(View.GONE);
            etName.setError(null);
        } else {
            isAddressTypeValid = false;
            tvAddressTypeSelectionError.setVisibility(View.VISIBLE);
            tvAddressTypeSelectionError.setText(R.string.edit_address_empty_address_type_validation_error_text);
        }
        return isAddressTypeValid;
    }

    private boolean checkLatLng() {
        boolean isLatLngValid = false;
        if (Latitude != null && !Latitude.isEmpty() && !Latitude.equals("")
                && Longitude != null && !Longitude.isEmpty() && !Longitude.equals("")) {
            isLatLngValid = true;
        } else {
            isLatLngValid = false;
            ShowCanNotDetectLocationDialog();
        }
        return isLatLngValid;
    }


    private boolean checkAddressID() {
        boolean isValid = false;
        if (AddressID != null && !AddressID.isEmpty() && !AddressID.equals("")) {
            isValid = true;
        } else {
            isValid = false;
            Toast.makeText(this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();

        }
        return isValid;
    }

    private boolean checkName() {
        boolean isValid = false;
        if (etName.getText().toString().trim().length() > 0) {
            isValid = true;
            etName.setError(null);
        } else {
            etName.setError(getString(R.string.edit_address_empty_name_validation_error_text));
        }
        return isValid;
    }


    private boolean checkAddress() {
        boolean isValid = false;
        if (etAddress.getText().toString().trim().length() > 0) {
            isValid = true;
            etAddress.setError(null);
        } else {
            etAddress.setError(getString(R.string.edit_address_empty_address_validation_error_text));
        }
        return isValid;
    }

    private boolean checkLocality() {
        boolean isValid = false;
        if (acetLocality.getText().toString().trim().length() > 0) {
            isValid = true;
            acetLocality.setError(null);
        } else {
            acetLocality.setError(getString(R.string.edit_address_empty_locality_validation_error_text));
        }
        return isValid;
    }

    private boolean checkPincode() {
        boolean isValid = false;
        if (PincodeID != null && !PincodeID.isEmpty() && !PincodeID.equals("")) {
            isValid = true;
        } else {
            Toast.makeText(this, getString(R.string.edit_address_pincode_validation_error), Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private boolean checkTownCity() {
        boolean isValid = false;
        if (etTownCity.getText().toString().trim().length() > 0) {
            isValid = true;
            etTownCity.setError(null);
        } else {
            etTownCity.setError(getString(R.string.edit_address_empty_town_or_city_validation_error_text));
        }
        return isValid;
    }

    private boolean checkState() {
        boolean isValid = false;
        if (etState.getText().toString().trim().length() > 0) {
            isValid = true;
            etState.setError(null);
        } else {
            etState.setError(getString(R.string.edit_address_empty_state_validation_error_text));
        }
        return isValid;
    }

    private boolean checkMobile() {
        boolean isValid = false;
        if (etMobileNumber.getText().toString().trim().length() <= 0) {
            isValid = false;
            etMobileNumber.setError(getString(R.string.edit_address_mobile_number_validation_error_text));
        } else if (etMobileNumber.getText().toString().trim().length() == 10) {
            isValid = true;
            etMobileNumber.setError(null);
        } else {
            isValid = false;
            etMobileNumber.setError(getString(R.string.edit_address_mobile_number_not_valid_validation_error_text));
        }
        return isValid;
    }


    private void EditAddress() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.EDIT_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseEditAddressJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                } else if (error instanceof NoConnectionError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                } else if (error instanceof TimeoutError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llEditAddress.setVisibility(View.GONE);
                    llNoRecordFound.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(EditAddressActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_TYPE, AddressType);
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_LATITUDE, Latitude);
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_LONGITUDE, Longitude);
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_ID, AddressID);
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_NAME, etName.getText().toString().trim());
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS, etAddress.getText().toString().trim());
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_LOCALITY, acetLocality.getText().toString().trim());
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_PINCODE_ID, PincodeID);
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_CITY, etTownCity.getText().toString().trim());
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_STATE, etState.getText().toString().trim());
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_MOBILE_NUMBER, etMobileNumber.getText().toString().trim());
                params.put(JsonFields.EDIT_ADDRESS_REQUEST_PARAMS_IS_DEFAULT, isDefault);
                Log.d("Params", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditAddressActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseEditAddressJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler.showPopupProgressSpinner(false);
                //Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditAddressActivity.this, SuccessMessageActivity.class);
                i.putExtra("Message", Message);
                i.putExtra("FromScreenName", "EditAddressActivity");
                i.putExtra("ToScreenName", "MyAddressActivity");
                startActivity(i);
                finish();
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
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PINCODE_REQUEST && resultCode == RESULT_OK) {
            PincodeID = data.getStringExtra("PincodeID");
            PincodeName = data.getStringExtra("PincodeName");
            String Area = data.getStringExtra("Area");
            String City = data.getStringExtra("City");
            String State = data.getStringExtra("State");


            if (PincodeID != null && !PincodeID.isEmpty() && !PincodeID.equals("") &&
                    PincodeName != null && !PincodeName.isEmpty() && !PincodeName.equals("") &&
                    Area != null && !Area.isEmpty() && !Area.equals("") &&
                    City != null && !City.isEmpty() && !City.equals("") &&
                    State != null && !State.isEmpty() && !State.equals("")) {
                //Log.d("selected Address id",PincodeID);
                etPincode.setText(PincodeName);
                acetLocality.setText(Area);
                etTownCity.setText(City);
                etState.setText(State);
            } else {
                Toast.makeText(this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
