package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iVBack;

    TextInputEditText etName, etEmail, etMobile, etCountryCode, etPassword, etConfirmPassword;
    Button btnSignUp;
    AtClass atClass;
    Button btnRetry;
    ProgressDialogHandler progressDialogHandler;
    String Message;
    RadioButton rbMale, rbFemale;

    String Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iVBack = findViewById(R.id.iVBack);
        iVBack.setOnClickListener(this);

        atClass = new AtClass();
        progressDialogHandler = new ProgressDialogHandler(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etCountryCode = findViewById(R.id.etCountryCode);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        atClass.getNoInputTextInputActivityRequest(etCountryCode,this);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);


        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);

        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbMale.setChecked(true);
                    rbFemale.setChecked(false);
                    Gender = getString(R.string.sign_up_gender_male_text);
                    Log.d("Gender", Gender);

                    setMaleSelected();
                    setFemaleNotSelected();
                }
            }
        });

        rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbFemale.setChecked(true);
                    rbMale.setChecked(false);
                    Gender = getString(R.string.sign_up_female_text);
                    Log.d("Gender", Gender);
                    setFemaleSelected();
                    setMaleNotSelected();
                }
            }
        });

    }

    private void setFemaleSelected() {
        rbFemale.setButtonTintList(getResources().getColorStateList(R.color.colorPrimary));
        rbFemale.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setFemaleNotSelected() {
        rbFemale.setButtonTintList(getResources().getColorStateList(R.color.colorBlackThree));
        rbFemale.setTextColor(getResources().getColor(R.color.colorBlackThree));

    }

    private void setMaleSelected() {
        rbMale.setButtonTintList(getResources().getColorStateList(R.color.colorPrimary));
        rbMale.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setMaleNotSelected() {
        rbMale.setButtonTintList(getResources().getColorStateList(R.color.colorBlackThree));
        rbMale.setTextColor(getResources().getColor(R.color.colorBlackThree));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                if (checkName() && checkEmail() && checkMobile() && checkPassword() &&  checkGender()) {
                    if (atClass.isNetworkAvailable(this)) {
                        SignUpUser();
                    } else {
                        Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.iVBack:
                onBackPressed();
                break;



        }
    }


    private boolean checkGender() {
        boolean isValid = false;
        if (Gender != null && !Gender.isEmpty() && !Gender.equals("")) {
            isValid = true;
        } else {
            isValid = false;
            Toast.makeText(this, getString(R.string.sign_up_gender_validation_error_toast_text), Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }


    private boolean checkName() {
        boolean isValid = false;
        if (etName.getText().toString().trim().length() > 0) {
            isValid = true;
            etName.setError(null);
        } else {
            isValid = false;
            etName.setError(getString(R.string.sign_up_name_validation_empty_name_error_message_text));
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
                etEmail.setError(getString(R.string.sign_up_email_validation_invalid_email_error_message_text));

            }
        } else {
            isValid = false;
            etEmail.setError(getString(R.string.sign_up_email_validation_empty_email_error_message_text));
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

    private boolean checkPassword() {
        boolean isValid = false;
        if (etPassword.getText().toString().trim().length() <= 0) {
            etPassword.setError(getString(R.string.sign_up_password_validation_invalid_password_error_message_text));
            isValid = false;
        } else if (etPassword.getText().toString().trim().length() < 8) {
            etPassword.setError(getString(R.string.sign_up_password_validation_invalid_length_password_error_message_text));
            isValid = false;
        } else if (etConfirmPassword.getText().toString().trim().length() <= 0) {
            etConfirmPassword.setError(getString(R.string.sign_up_password_validation_invalid_confirm_password_error_message_text));
            isValid = false;
        } else if (etConfirmPassword.getText().toString().trim().length() < 8) {
            etConfirmPassword.setError(getString(R.string.sign_up_password_validation_invalid_length_confirm_password_error_message_text));
            isValid = false;
        } else {
            if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                isValid = true;
                etPassword.setError(null);
                etConfirmPassword.setError(null);
            } else {
                Toast.makeText(this, getString(R.string.sign_up_password_validation_password_not_matched_error_message_toast_text), Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean checkMobile() {
        boolean isValid = false;
        if (etMobile.getText().toString().trim().length() <= 0) {
            isValid = false;
            etMobile.setError(getString(R.string.sign_up_mobile_validation_empty_mobile_error_message_text));
        } else if (etMobile.getText().toString().trim().length() == 10) {
            isValid = true;
            etMobile.setError(null);
        } else {
            isValid = false;
            etMobile.setError(getString(R.string.sign_up_mobile_validation_invalid_mobile_error_message_text));
        }
        return isValid;
    }

    private void SignUpUser() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.SIGN_UP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);

                if (error instanceof NetworkError) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(SignUpActivity.this);
                params.put(JsonFields.SIGN_UP_REQUEST_PARAM_CUSTOMER_NAME, etName.getText().toString().trim());
                params.put(JsonFields.SIGN_UP_REQUEST_PARAM_CUSTOMER_EMAIL, etEmail.getText().toString().trim());
                params.put(JsonFields.SIGN_UP_REQUEST_PARAM_CUSTOMER_MOBILE, etMobile.getText().toString().trim());
                params.put(JsonFields.SIGN_UP_REQUEST_PARAM_CUSTOMER_PASSWORD, etPassword.getText().toString().trim());
                params.put(JsonFields.SIGN_UP_REQUEST_PARAM_CUSTOMER_GENDER, Gender);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
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

                Intent i = new Intent(SignUpActivity.this, SuccessMessageActivity.class);
                i.putExtra("Message", Message);
                i.putExtra("FromScreen", "SignUpActivity");
                i.putExtra("ToScreen", "LoginActivity");
                startActivity(i);
                finish();
            } /*else if (flag == 2) // Error Flag Email ID Already Registered Display Error Message
            {
                progressDialogHandler.showPopupProgressSpinner(false);

                Intent i = new Intent(SignUpActivity.this, ErrorMessageActivity.class);
                i.putExtra("Message", Message);
                i.putExtra("ScreenName", "SignUpActivity");
                startActivity(i);
                finish();

            }*/ else if (flag == 0) { // Error Flag Passwords Does Not Matched
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }else
            {
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}