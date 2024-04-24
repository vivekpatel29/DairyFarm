package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvSignUp;
    TextView tvSkipLogin;

    TextInputEditText etEmail, etPassword;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;
    CustomerSessionManager customerSessionManager;

    TextView tvForgotPassword;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        atClass = new AtClass();
        progressDialogHandler = new ProgressDialogHandler(this);
        customerSessionManager = new CustomerSessionManager(this);

        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(this);

        tvSkipLogin = findViewById(R.id.tvSkipLogin);
        tvSkipLogin.setOnClickListener(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        //atClass.getNoInputTextInputActivityRequest();

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (atClass.isNetworkAvailable(this)) {
                    if (checkEmail() && checkPassword()) {
                        LoginUser();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvForgotPassword:
                if (atClass.isNetworkAvailable(this)) {
                    Intent forgotPasswordIntent = new Intent(this,ForgotPasswordActivity.class);
                    startActivity(forgotPasswordIntent);
                } else {
                    Toast.makeText(this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvSignUp:
                if (atClass.isNetworkAvailable(this)) {
                    Intent signUpIntent= new Intent(this,SignUpActivity.class);
                    startActivity(signUpIntent);
                } else {
                    Toast.makeText(this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;




        }
    }


    private boolean checkPassword() {
        boolean isValid = false;

        if (etPassword.getText().toString().trim().length() <= 0) {
            isValid = false;
            etPassword.setError(getString(R.string.login_password_validation_empty_password_error_message_text));
        } else if (etPassword.getText().toString().trim().length() < 8) {
            isValid = false;
            etPassword.setError(getString(R.string.login_password_validation_empty_password_error_message_text));
        } else {
            isValid = true;
            etPassword.setError(null);
        }
        return isValid;
    }

    private boolean checkEmail() {
        boolean isValid = false;
        if (etEmail.getText().toString().trim().length() > 0) {
            if (validateEmailAddress(etEmail.getText().toString().trim())) {
                isValid = true;
                etEmail.setError(null);
            } else {
                isValid = false;
                etEmail.setError(getString(R.string.login_email_validation_invalid_email_error_message_text));

            }
        } else {
            isValid = false;
            etEmail.setError(getString(R.string.login_email_validation_empty_email_error_message_text));
        }
        return isValid;
    }

    private boolean validateEmailAddress(String emailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }


    private void LoginUser() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(LoginActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(LoginActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(LoginActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(LoginActivity.this);
                params.put(JsonFields.LOGIN_REQUEST_PARAM_CUSTOMER_EMAIL, etEmail.getText().toString().trim());
                params.put(JsonFields.LOGIN_REQUEST_PARAM_CUSTOMER_PASSWORD, etPassword.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);
            String CustomerID = jsonObject.optString(JsonFields.LOGIN_RESPONSE_CUSTOMER_ID);
            String CustomerName = jsonObject.optString(JsonFields.LOGIN_RESPONSE_CUSTOMER_NAME);
            String CustomerEmail = jsonObject.optString(JsonFields.LOGIN_RESPONSE_CUSTOMER_EMAIL);
            String CustomerMobile = jsonObject.optString(JsonFields.LOGIN_RESPONSE_CUSTOMER_MOBILE);
            String CustomerProfilePictureURL = jsonObject.optString(JsonFields.LOGIN_RESPONSE_CUSTOMER_PROFILE_PICTURE_URL);

            if (flag == 1) {  // Success Flag Now Proceed to Success and then Home Activity
                progressDialogHandler.showPopupProgressSpinner(false);

                if (atClass.CheckEmptyString(this, CustomerID, "") &&
                        atClass.CheckEmptyString(this, CustomerName, "") &&
                        atClass.CheckEmptyString(this, CustomerEmail, "") &&
                        atClass.CheckEmptyString(this, CustomerMobile, "")) {
                    customerSessionManager.setCustomerDetails(CustomerID, CustomerName, CustomerEmail, CustomerMobile, CustomerProfilePictureURL);
                    customerSessionManager.setLoginStatus();

                    Intent i = new Intent(LoginActivity.this, SuccessMessageActivity.class);
                    i.putExtra("Message", Message);
                    i.putExtra("FromScreenName", "LoginActivity");
                    i.putExtra("ToScreenName", "HomeActivity");
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            } else {
                RestInput();
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void RestInput() {
        etEmail.setText("");
        etPassword.setText("");
        etEmail.setError(null);
        etPassword.setError(null);
    }
}