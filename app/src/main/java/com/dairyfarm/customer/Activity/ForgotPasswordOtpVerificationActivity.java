package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ForgotPasswordOtpVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iVBack;

    TextView tvEmailID, tvResendOTP, tvTimer;
    LinearLayout llResendOtp, llResendOtpTimer;
    TextInputEditText tieOtp1, tieOtp2, tieOtp3, tieOtp4, tieOtp5, tieOtp6;
    Button btnVerifyOTP;
    String EmailID;
    String Message;

    AtClass atClass;
    ProgressDialogHandler progressDialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp_verification);

        Intent i = getIntent();

        if (i.hasExtra("EmailID")) {
            EmailID = i.getStringExtra("EmailID");
        }

        atClass = new AtClass();
        progressDialogHandler = new ProgressDialogHandler(this);

        iVBack = findViewById(R.id.iVBack);
        iVBack.setOnClickListener(this);

        tvEmailID = findViewById(R.id.tvEmailID);
        tvResendOTP = findViewById(R.id.tvResendOTP);
        tvTimer = findViewById(R.id.tvTimer);
        tvResendOTP.setOnClickListener(this);

        llResendOtp = findViewById(R.id.llResendOtp);
        llResendOtpTimer = findViewById(R.id.llResendOtpTimer);

        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        btnVerifyOTP.setOnClickListener(this);

        tieOtp1 = findViewById(R.id.tie_verify_mobile_number_otp1);
        tieOtp2 = findViewById(R.id.tie_verify_mobile_number_otp2);
        tieOtp3 = findViewById(R.id.tie_verify_mobile_number_otp3);
        tieOtp4 = findViewById(R.id.tie_verify_mobile_number_otp4);
        tieOtp5 = findViewById(R.id.tie_verify_mobile_number_otp5);
        tieOtp6 = findViewById(R.id.tie_verify_mobile_number_otp6);

        tvEmailID.setText(EmailID);
        setupTextChangeListerners();
        SetUpTimer();
    }

    private void SetUpTimer() {
        llResendOtp.setVisibility(View.GONE);
        llResendOtpTimer.setVisibility(View.VISIBLE);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 <= 9) {
                    tvTimer.setText("00:" + "0" + millisUntilFinished / 1000);
                } else {
                    tvTimer.setText("00:" + millisUntilFinished / 1000);
                }
            }

            public void onFinish() {
                Log.d("Timer", "Finished");
                llResendOtp.setVisibility(View.VISIBLE);
                llResendOtpTimer.setVisibility(View.GONE);
            }
        }.start();
    }

    private void setupTextChangeListerners() {
        tieOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 1) {
                    tieOtp2.requestFocus();
                    tieOtp1.setBackgroundResource(R.drawable.selected_edittext_bg);
                } else {
                    tieOtp1.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                }
            }
        });

        tieOtp1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d("TAG", "OnKeyListener, premuto BACKSPACE");
                    tieOtp1.setText("");
                }
                return false;
            }
        });

        tieOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 1) {
                    tieOtp3.requestFocus();
                    tieOtp2.setBackgroundResource(R.drawable.selected_edittext_bg);
                } else {

                    tieOtp2.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                }

            }
        });

        tieOtp2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d("TAG", "OnKeyListener, premuto BACKSPACE");
                    tieOtp2.setText("");
                    tieOtp2.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                    tieOtp1.requestFocus();

                }
                return false;
            }
        });


        tieOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 1) {
                    tieOtp4.requestFocus();
                    tieOtp3.setBackgroundResource(R.drawable.selected_edittext_bg);
                } else {

                    tieOtp3.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                }

            }
        });


        tieOtp3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d("TAG", "OnKeyListener, premuto BACKSPACE");
                    tieOtp3.setText("");
                    tieOtp3.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                    tieOtp2.requestFocus();
                }
                return false;
            }
        });


        tieOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 1) {
                    tieOtp5.requestFocus();
                    tieOtp4.setBackgroundResource(R.drawable.selected_edittext_bg);
                } else {

                    tieOtp4.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                }

            }
        });


        tieOtp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d("TAG", "OnKeyListener, premuto BACKSPACE");
                    tieOtp4.setText("");
                    tieOtp4.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                    tieOtp3.requestFocus();
                }
                return false;
            }
        });


        tieOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tieOtp5.setBackgroundResource(R.drawable.selected_edittext_bg);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 1) {
                    tieOtp6.requestFocus();
                    tieOtp5.setBackgroundResource(R.drawable.selected_edittext_bg);
                } else {
                    tieOtp5.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                }
            }
        });

        tieOtp5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d("TAG", "OnKeyListener, premuto BACKSPACE");
                    tieOtp5.setText("");
                    tieOtp5.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                    tieOtp4.requestFocus();

                }
                return false;
            }
        });

        tieOtp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 1) {
                    tieOtp6.setBackgroundResource(R.drawable.selected_edittext_bg);
                } else {
                    tieOtp6.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                }
            }
        });

        tieOtp6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d("TAG", "OnKeyListener, premuto BACKSPACE");
                    tieOtp6.setText("");
                    tieOtp6.setBackgroundResource(R.drawable.not_selected_edittext_bg);
                    tieOtp5.requestFocus();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVerifyOTP:
                boolean isOTP1Valid = checkOTP(tieOtp1);
                boolean isOTP2Valid = checkOTP(tieOtp2);
                boolean isOTP3Valid = checkOTP(tieOtp3);
                boolean isOTP4Valid = checkOTP(tieOtp4);
                boolean isOTP5Valid = checkOTP(tieOtp5);
                boolean isOTP6Valid = checkOTP(tieOtp6);

                if (isOTP1Valid && isOTP2Valid && isOTP3Valid && isOTP4Valid && isOTP5Valid && isOTP6Valid) {
                    if (atClass.isNetworkAvailable(this)) {
                        verifyOTP();
                    } else {
                        Toast.makeText(this, getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.tvResendOTP:
                ResendOtp();
                break;

            case R.id.iVBack:
                onBackPressed();
                break;


        }
    }

    private void ResendOtp() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.REQUEST_EMAIL_OTP_FOR_FORGOT_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResendJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ForgotPasswordOtpVerificationActivity.this);
                params.put(JsonFields.REQUEST_FORGOT_PASSWORD_EMAIL_OTP_REQUEST_PARAMS_CUSTOMER_EMAIL, EmailID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void parseResendJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                SetUpTimer();
            } else {
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verifyOTP() {
        progressDialogHandler.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.EMAIL_OTP_VERIFICATION_FOR_FORGOT_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordOtpVerificationActivity.this, getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = commonRequestParams.getCommonRequestParams(ForgotPasswordOtpVerificationActivity.this);
                String strOTP = tieOtp1.getText().toString() + tieOtp2.getText().toString() + tieOtp3.getText().toString() + tieOtp4.getText().toString() + tieOtp5.getText().toString() + tieOtp6.getText().toString();
                params.put(JsonFields.VERIFY_FORGOT_PASSWORD_EMAIL_OTP_REQUEST_PARAMS_CUSTOMER_EMAIL, EmailID);
                params.put(JsonFields.VERIFY_FORGOT_PASSWORD_EMAIL_OTP_REQUEST_PARAMS_EMAIL_OTP, strOTP);
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
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, ResetPasswordActivity.class);
                i.putExtra("EmailID", EmailID);
                startActivity(i);
                finish();
            } else {
                ResetInput();
                progressDialogHandler.showPopupProgressSpinner(false);
                Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ResetInput() {
        tieOtp1.setText("");
        tieOtp2.setText("");
        tieOtp3.setText("");
        tieOtp4.setText("");
        tieOtp5.setText("");
        tieOtp6.setText("");

        tieOtp1.setError(null);
        tieOtp2.setError(null);
        tieOtp3.setError(null);
        tieOtp4.setError(null);
        tieOtp5.setError(null);
        tieOtp6.setError(null);

    }

    private boolean checkOTP(TextInputEditText tieOtp) {
        boolean isOTPValid = false;
        if (tieOtp.getText().toString().trim().length() != 1) {
            tieOtp.setBackgroundResource(R.drawable.edittext_error_bg);
        } else {
            tieOtp.setBackgroundResource(R.drawable.selected_edittext_bg);
            isOTPValid = true;
        }
        return isOTPValid;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(this, ForgotPasswordActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
