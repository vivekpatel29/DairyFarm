package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iVBack;
    TextInputEditText etEmail;
    Button btnSubmit;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        atClass = new AtClass();
        progressDialogHandler = new ProgressDialogHandler(this);

        iVBack = findViewById(R.id.iVBack);
        iVBack.setOnClickListener(this);

        etEmail = findViewById(R.id.etEmail);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (atClass.isNetworkAvailable(this)) {
                    if (checkEmail()) {
                        CheckForgotPasswordMailAndSendOtp();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iVBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    private boolean checkEmail() {
        boolean isValid = false;
        if (etEmail.getText().toString().trim().length() > 0) {
            if (validateEmailAddress(etEmail.getText().toString().trim())) {
                isValid = true;
                etEmail.setError(null);
            } else {
                isValid = false;
                etEmail.setError(getString(R.string.forgot_password_email_validation_invalid_email_error_message_text));

            }
        } else {
            isValid = false;
            etEmail.setError(getString(R.string.forgot_password_email_validation_empty_email_error_message_text));
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


    private void CheckForgotPasswordMailAndSendOtp() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.REQUEST_EMAIL_OTP_FOR_FORGOT_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ForgotPasswordActivity.this);
                params.put(JsonFields.REQUEST_FORGOT_PASSWORD_EMAIL_OTP_REQUEST_PARAMS_CUSTOMER_EMAIL, etEmail.getText().toString().trim());
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
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler.showPopupProgressSpinner(false);
                Intent i = new Intent(this,ForgotPasswordOtpVerificationActivity.class);
                i.putExtra("EmailID",etEmail.getText().toString().trim());
                startActivity(i);
                finish();
            } else {
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                ResetInput();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ResetInput() {
        etEmail.setText("");
        etEmail.setError(null);
    }

}