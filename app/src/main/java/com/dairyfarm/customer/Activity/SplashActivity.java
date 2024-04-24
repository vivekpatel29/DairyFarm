package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivLogo;
    LinearLayout llNoInternetConnectionSplash;
    TextView tvVersion, tvRetry;

    AtClass atClass;
    CustomerSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        atClass = new AtClass();

        tvVersion = findViewById(R.id.tvVersion);

        tvRetry = findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(this);

        llNoInternetConnectionSplash = findViewById(R.id.llNoInternetConnectionSplash);

        atClass = new AtClass();
        userSessionManager = new CustomerSessionManager(this);

        ivLogo = findViewById(R.id.ivLogo);

        AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
        animation.setDuration(2500);
        animation.setStartOffset(200);
        animation.setFillAfter(true);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        ivLogo.startAnimation(animation);
        tvVersion.setText(getString(R.string.splash_version_text) + " " + atClass.getAppVersionName());

        CheckInternet();
    }

    private void CheckInternet() {
        if (atClass.isNetworkAvailable(this)) {
            llNoInternetConnectionSplash.setVisibility(View.GONE);
            if (userSessionManager.getLoginStatus()) {
                HandleHomeScreenNavigation();
            } else {
                HandleWelcomeScreenNavigation();
            }
        } else {
            llNoInternetConnectionSplash.setVisibility(View.VISIBLE);
        }
    }

    private void HandleHomeScreenNavigation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

    private void HandleWelcomeScreenNavigation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRetry:
                if (atClass.isNetworkAvailable(this)) {
                    llNoInternetConnectionSplash.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(800);

                                if (userSessionManager.getLoginStatus()) {
                                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 800);
                } else {
                    llNoInternetConnectionSplash.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}