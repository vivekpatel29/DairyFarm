package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.dairyfarm.customer.R;

public class SuccessMessageActivity extends AppCompatActivity {

    ImageView iVSuccessLogo;
    TextView tvSuccessMessage;
    String SuccessMessage;
    String FromScreenName, ToScreenName;
    String ProductID;

    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_message);

        iVSuccessLogo = findViewById(R.id.iVSuccessLogo);
        tvSuccessMessage = findViewById(R.id.tvSuccessMessage);

        Intent i = getIntent();

        if (i.hasExtra("Message")) {
            SuccessMessage = i.getStringExtra("Message");
        }

        if (i.hasExtra("FromScreenName")) {
            FromScreenName = i.getStringExtra("FromScreenName");
        }

        if (i.hasExtra("ToScreenName")) {
            ToScreenName = i.getStringExtra("ToScreenName");
        }

        if (i.hasExtra("ProductID")) {
            ProductID = i.getStringExtra("ProductID");
        }



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCheckAnimation();
            }
        }, 500);

        tvSuccessMessage.setText(SuccessMessage);

        if (FromScreenName != null && !FromScreenName.isEmpty() && !FromScreenName.equals("")
                && ToScreenName != null && !ToScreenName.isEmpty() && !ToScreenName.equals("")) {
            NavigateFromToScreen();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        //Intent intent = new Intent(SuccessMessageActivity.this,SelectAddressActivity.class);
                        //startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 1500);
        }
    }


    private void setCheckAnimation() {
        Drawable drawable = iVSuccessLogo.getDrawable();
        if(drawable instanceof AnimatedVectorDrawableCompat)
        {
            avd = (AnimatedVectorDrawableCompat)drawable;
            avd.start();
        }else if(drawable instanceof AnimatedVectorDrawable)
        {
            avd2 = (AnimatedVectorDrawable)drawable;
            avd2.start();
        }
    }
    private void NavigateFromToScreen() {
        if (FromScreenName.equals("LoginActivity") && ToScreenName.equals("HomeActivity")) {
            HandleClearAllScreenWithoutDataPassingNavigation(this, HomeActivity.class);
        } else if (FromScreenName.equals("SignUpActivity") && ToScreenName.equals("LoginActivity")) {
            HandleClearAllScreenWithoutDataPassingNavigation(this, LoginActivity.class);
        } else if (FromScreenName.equals("ResetPasswordActivity") && ToScreenName.equals("LoginActivity")) {
            HandleClearAllScreenWithoutDataPassingNavigation(this, LoginActivity.class);
        } else if (FromScreenName.equals("ProductDetailsActivity") && ToScreenName.equals("ProductDetailsActivity")) {
            HandleScreenNavigationWithDataPassing(this, ProductDetailsActivity.class,"ProductID",ProductID);
        } else if (FromScreenName.equals("AddAddressActivity") && ToScreenName.equals("MyCartActivity")) {
            HandleScreenNavigationWithoutDataPassing(this, MyCartActivity.class);
        } else if (FromScreenName.equals("AddAddressActivity") && ToScreenName.equals("SelectAddressActivity")) {
            HandleScreenNavigationWithoutDataPassing(this, SelectAddressActivity.class);
        } else if (FromScreenName.equals("AddAddressActivity") && ToScreenName.equals("MyAddressActivity")) {
            HandleScreenNavigationWithoutDataPassing(this, MyAddressActivity.class);
        } else if (FromScreenName.equals("EditAddressActivity") && ToScreenName.equals("MyAddressActivity")) {
            HandleScreenNavigationWithoutDataPassing(this, MyAddressActivity.class);
        }



    }

    private void HandleClearAllScreenWithoutDataPassingNavigation(final AppCompatActivity ActivityFrom, final Class<? extends AppCompatActivity> ActivityTo) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(ActivityFrom, ActivityTo);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);

    }

    private void HandleClearAllScreenWithDataPassingNavigation(final AppCompatActivity ActivityFrom, final Class<? extends AppCompatActivity> ActivityTo, final String Key, final String Value) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(ActivityFrom, ActivityTo);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Key, Value);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);

    }


    private void HandleScreenNavigationWithDataPassing(final AppCompatActivity ActivityFrom, final Class<? extends AppCompatActivity> ActivityTo, final String Key, final String Value) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(ActivityFrom, ActivityTo);
                    intent.putExtra(Key, Value);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);

    }


    private void HandleScreenNavigationWithThreeDataPassing(final AppCompatActivity ActivityFrom, final Class<? extends AppCompatActivity> ActivityTo, final String Key1, final String Value1,final String Key2, final String Value2,final String Key3, final String Value3) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(ActivityFrom, ActivityTo);
                    intent.putExtra(Key1, Value1);
                    intent.putExtra(Key2, Value2);
                    intent.putExtra(Key3, Value3);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
    }


    private void HandleScreenNavigationWithDataPassing(final AppCompatActivity ActivityFrom, final Class<? extends AppCompatActivity>ActivityTo, final String Key1, final String Value1,final String Key2, final String Value2) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(ActivityFrom, ActivityTo);
                    intent.putExtra(Key1, Value1);
                    intent.putExtra(Key2, Value2);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
    }

    private void HandleScreenNavigationWithoutDataPassing(final AppCompatActivity ActivityFrom, final Class<? extends AppCompatActivity> ActivityTo) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(ActivityFrom, ActivityTo);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
    }


    private void HandleScreenNavigationAfterFinishing() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1500);

    }
}