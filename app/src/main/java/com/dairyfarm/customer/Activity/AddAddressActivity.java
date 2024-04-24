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
import android.location.Address;
import android.location.Geocoder;
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

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    ImageView ivBack;

    private static final int CHOOSE_PINCODE_REQUEST = 001;

    TextInputEditText etName, etAddress, etPincode, etState, etTownCity, etCountryCode, etMobileNumber;
    CheckBox ChkDefaultAddress;

    LinearLayout llAddAddress, llNoInternetConnection;
    Button btnRetry;

    CustomerSessionManager customerSessionManger;
    ProgressDialogHandler progressDialogHandler;
    AtClass atClass;

    Button btnAddAddress;

    String PincodeID, PincodeName;


    String Message;

    String isDefault;

    String isFrom;

    RadioButton rbHome, rbOffice, rbOther;

    String AddressType = "";

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private TextView resultText;

    String Latitude = "", Longitude = "";
    GPSTracker gpsTracker;
    private static final int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    //1. No Address Add Address
    //2. Select Address Add Address
    //3. Add Address

    String HasAlreadyHomeAddressAdded, HasAlreadyOfficeAddressAdded;

    TextView tvAddressTypeSelectionError;

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
        setContentView(R.layout.activity_add_address);

        llProgressLayout = findViewById(R.id.llProgressLayout);

        tvAddressTypeSelectionError = findViewById(R.id.tvAddressTypeSelectionError);
        tvAddressTypeSelectionError.setVisibility(View.GONE);

        gpsTracker = new GPSTracker(this);

        rbHome = findViewById(R.id.rbHome);
        rbOffice = findViewById(R.id.rbOffice);
        rbOther = findViewById(R.id.rbOther);

        customerSessionManger = new CustomerSessionManager(this);
        progressDialogHandler = new ProgressDialogHandler(this);
        atClass = new AtClass();

        llAddAddress = findViewById(R.id.llAddAddress);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);

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

        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(this);

        ivBack = findViewById(R.id.ivBack);
        etPincode.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        Intent i = getIntent();

        if (i.hasExtra("isFrom")) {
            isFrom = i.getStringExtra("isFrom");
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
                    tvAddressTypeSelectionError.setVisibility(View.GONE);
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
                    tvAddressTypeSelectionError.setVisibility(View.GONE);
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
                    tvAddressTypeSelectionError.setVisibility(View.GONE);
                }
            }
        });


        resultText = (TextView) findViewById(R.id.resultText);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getAndSetCurrentLatLng();
        configureCameraIdle();
        setUpSwipeToRefreshLayout();
    }


    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                Intent i = new Intent(AddAddressActivity.this, AddAddressActivity.class);
                i.putExtra("isFrom", isFrom);
                startActivity(i);
                finish();
            }
        });
    }

    private void configureCameraIdle() {
        Log.d("Called", "Yes");
        if (atClass.isNetworkAvailable(AddAddressActivity.this)) {
            onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng latLng = mMap.getCameraPosition().target;
                    Geocoder geocoder = new Geocoder(AddAddressActivity.this);
                    try {
                        if (atClass.isNetworkAvailable(AddAddressActivity.this)) {
                            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
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
                Intent i = new Intent(AddAddressActivity.this, AddAddressActivity.class);
                i.putExtra("isFrom", isFrom);
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

                Intent i = new Intent(AddAddressActivity.this, LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                startActivity(i);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                finish();
            } else {
                llProgressLayout.setVisibility(View.GONE);

                Toast.makeText(AddAddressActivity.this, Message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddAddressActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(AddAddressActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(AddAddressActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(AddAddressActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddAddressActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(AddAddressActivity.this);
                params.put(JsonFields.AREA_REQUEST_PARAMS_SEARCH_QUERY, term);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManger.getCustomerID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddressActivity.this);
        requestQueue.add(stringRequest);
    }


    private void getAndSetCurrentLatLng() {
        try {
            if (ActivityCompat.checkSelfPermission(AddAddressActivity.this, mPermission)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddAddressActivity.this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gpsTracker.canGetLocation()) {
            Log.d("Inside If", "Yes");
            final double latitude = gpsTracker.getLatitude();
            final double longitude = gpsTracker.getLongitude();

            if (latitude != 0.0 && longitude != 0.0) {
                Latitude = String.valueOf(latitude);
                Longitude = String.valueOf(longitude);
            } else {
                //Ahmedabad Location
                Latitude = "23.0204978";
                Longitude = "72.4396539";
            }
        } else {
            //Ahmedabad Location
            Log.d("Inside Else", "Yes");
            Latitude = "23.0204978";
            Longitude = "72.4396539";
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.et_pincode:
                if (atClass.isNetworkAvailable(AddAddressActivity.this)) {
                    Intent i1 = new Intent(AddAddressActivity.this, SelectPincodeActivity.class);
                    i1.putExtra("PincodeID", PincodeID);
                    i1.putExtra("isFrom", "1");
                    startActivityForResult(i1, CHOOSE_PINCODE_REQUEST);
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
                }
                break;

            case R.id.btnAddAddress:
                if (atClass.isNetworkAvailable(AddAddressActivity.this)) {
                    if (checkLatLng() && checkAddressType() && checkName() && checkAddress() && checkLocality() && checkPincode() && checkState() && checkTownCity() && checkMobile()) {
                        AddNewAddress();
                        Log.d("called", "Yes");
                    }
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
                }
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(AddAddressActivity.this)) {
                    Intent i = new Intent(this, AddAddressActivity.class);
                    i.putExtra("isFrom", isFrom);
                    startActivity(i);
                    finish();
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
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
            tvAddressTypeSelectionError.setText(R.string.add_address_empty_address_type_validation_error_text);
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

    private boolean checkName() {
        boolean isValid = false;
        if (etName.getText().toString().trim().length() > 0) {
            isValid = true;
            etName.setError(null);
        } else {
            etName.setError(getString(R.string.add_address_empty_name_validation_error_text));
        }
        return isValid;
    }


    private boolean checkAddress() {
        boolean isValid = false;
        if (etAddress.getText().toString().trim().length() > 0) {
            isValid = true;
            etAddress.setError(null);
        } else {
            etAddress.setError(getString(R.string.add_address_empty_address_validation_error_text));
        }
        return isValid;
    }

    private boolean checkLocality() {
        boolean isValid = false;
        if (acetLocality.getText().toString().trim().length() > 0) {
            isValid = true;
            acetLocality.setError(null);
        } else {
            acetLocality.setError(getString(R.string.add_address_empty_locality_validation_error_text));
        }
        return isValid;
    }

    private boolean checkPincode() {
        boolean isValid = false;
        if (PincodeID != null && !PincodeID.isEmpty() && !PincodeID.equals("")) {
            isValid = true;
        } else {
            Toast.makeText(this, getString(R.string.add_address_pincode_validation_error), Toast.LENGTH_SHORT).show();
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
            etTownCity.setError(getString(R.string.add_address_empty_town_or_city_validation_error_text));
        }
        return isValid;
    }

    private boolean checkState() {
        boolean isValid = false;
        if (etState.getText().toString().trim().length() > 0) {
            isValid = true;
            etState.setError(null);
        } else {
            etState.setError(getString(R.string.add_address_empty_state_validation_error_text));
        }
        return isValid;
    }

    private boolean checkMobile() {
        boolean isValid = false;
        if (etMobileNumber.getText().toString().trim().length() <= 0) {
            isValid = false;
            etMobileNumber.setError(getString(R.string.add_address_mobile_number_validation_error_text));
        } else if (etMobileNumber.getText().toString().trim().length() == 10) {
            isValid = true;
            etMobileNumber.setError(null);
        } else {
            isValid = false;
            etMobileNumber.setError(getString(R.string.add_address_mobile_number_not_valid_validation_error_text));
        }
        return isValid;
    }


    private void AddNewAddress() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ADD_ADDRESS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
                } else if (error instanceof NoConnectionError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
                } else if (error instanceof TimeoutError) {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
                } else {
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    llAddAddress.setVisibility(View.GONE);
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(AddAddressActivity.this);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManger.getCustomerID());
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_TYPE, AddressType);
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_LATITUDE, Latitude);
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_LONGITUDE, Longitude);
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_NAME, etName.getText().toString().trim());
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_ADDRESS, etAddress.getText().toString().trim());
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_LOCALITY, acetLocality.getText().toString().trim());
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_PINCODE_ID, PincodeID);
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_CITY, etTownCity.getText().toString().trim());
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_STATE, etState.getText().toString().trim());
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_MOBILE_NUMBER, etMobileNumber.getText().toString().trim());
                params.put(JsonFields.ADD_ADDRESS_REQUEST_PARAMS_IS_DEFAULT, isDefault);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddressActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {  // Success Flag Now Proceed to the Otp Verification Activity
                progressDialogHandler.showPopupProgressSpinner(false);
                //Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                if (isFrom.equals("1")) {
                    Intent i = new Intent(AddAddressActivity.this, SuccessMessageActivity.class);
                    i.putExtra("Message", Message);
                    i.putExtra("FromScreenName", "AddAddressActivity");
                    i.putExtra("ToScreenName", "MyCartActivity");
                    startActivity(i);
                    finish();
                } else if (isFrom.equals("2")) {
                    Intent i = new Intent(AddAddressActivity.this, SuccessMessageActivity.class);
                    i.putExtra("Message", Message);
                    i.putExtra("FromScreenName", "AddAddressActivity");
                    i.putExtra("ToScreenName", "SelectAddressActivity");
                    startActivity(i);
                    finish();
                } else if (isFrom.equals("3")) {
                    Intent i = new Intent(AddAddressActivity.this, SuccessMessageActivity.class);
                    i.putExtra("Message", Message);
                    i.putExtra("FromScreenName", "AddAddressActivity");
                    i.putExtra("ToScreenName", "MyAddressActivity");
                    startActivity(i);
                    finish();
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
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
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