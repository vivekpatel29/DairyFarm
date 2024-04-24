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

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iVBack;
    TextInputEditText etNewPassword, etConfirmPassword;
    Button btnSubmit;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;

    String EmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent i = getIntent();

        if (i.hasExtra("EmailID")) {
            EmailID = i.getStringExtra("EmailID");
        } else {
            EmailID = "";
        }

        iVBack = findViewById(R.id.iVBack);
        iVBack.setOnClickListener(this);

        atClass = new AtClass();
        progressDialogHandler = new ProgressDialogHandler(this);

        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (atClass.isNetworkAvailable(this)) {
                    if (checkPasswords()) {
                        ResetPassword();
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


    private boolean checkPasswords() {
        boolean isValid = false;
        if (etNewPassword.getText().toString().trim().length() <= 0) {
            etNewPassword.setError(getString(R.string.reset_password_validation_invalid_password_error_message_text));
            isValid = false;
        } else if (etNewPassword.getText().toString().trim().length() < 8) {
            etNewPassword.setError(getString(R.string.reset_password_validation_invalid_length_password_error_message_text));
            isValid = false;
        } else if (etConfirmPassword.getText().toString().trim().length() <= 0) {
            etConfirmPassword.setError(getString(R.string.reset_password_validation_invalid_confirm_password_error_message_text));
            isValid = false;
        } else if (etConfirmPassword.getText().toString().trim().length() < 8) {
            etConfirmPassword.setError(getString(R.string.reset_password_validation_invalid_length_confirm_password_error_message_text));
            isValid = false;
        } else {
            if (etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                isValid = true;
                etNewPassword.setError(null);
                etConfirmPassword.setError(null);
            } else {
                Toast.makeText(this, getString(R.string.reset_password_validation_password_not_matched_error_message_toast_text), Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }
        return isValid;
    }

    private void ResetPassword() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.RESET_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseEditProfileJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ResetPasswordActivity.this);
                params.put(JsonFields.RESET_PASSWORD_REQUEST_PARAMS_CUSTOMER_EMAIL, EmailID);
                params.put(JsonFields.RESET_PASSWORD_REQUEST_PARAMS_NEW_PASSWORD, etNewPassword.getText().toString().trim());
                params.put(JsonFields.RESET_PASSWORD_REQUEST_PARAMS_NEW_CONFIRM_PASSWORD, etConfirmPassword.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseEditProfileJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler.showPopupProgressSpinner(false);

                Intent i = new Intent(ResetPasswordActivity.this, SuccessMessageActivity.class);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Message", Message);
                i.putExtra("FromScreenName", "ResetPasswordActivity");
                i.putExtra("ToScreenName", "LoginActivity");
                startActivity(i);
                finish();
            } else {
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                ResetInputs();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ResetInputs() {
        etNewPassword.setText("");
        etConfirmPassword.setText("");
        etNewPassword.setError(null);
        etConfirmPassword.setError(null);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(this, ForgotPasswordActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
