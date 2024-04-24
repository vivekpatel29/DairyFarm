package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dairyfarm.customer.R;

public class LogoutActivity extends AppCompatActivity {
    TextView tvLogoutTitle,tvLogoutMessage;
    ImageView iVLogoutIcon;
    String Title,Message,ImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        tvLogoutTitle = findViewById(R.id.tvLogoutTitle);
        tvLogoutMessage = findViewById(R.id.tvLogoutMessage);
        iVLogoutIcon = findViewById(R.id.iVLogoutIcon);

        Intent i = getIntent();



        if(i.hasExtra("ImageURL"))
        {
            ImageURL = i.getStringExtra("ImageURL");
            ImageURL = "";
            Glide.with(this).load(ImageURL).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).error(R.drawable.inactive_user).into(iVLogoutIcon);
        }

        if(i.hasExtra("Title"))
        {
            Title = i.getStringExtra("Title");
            tvLogoutTitle.setText(Title);

            Title = "Oops!";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvLogoutTitle.setText(Html.fromHtml(Title, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvLogoutTitle.setText(Html.fromHtml(Title));
            }
        }

        if(i.hasExtra("Message"))
        {
            Message = i.getStringExtra("Message");
            Message = "You are disabled by administrator from accessing this app please contact administrator for assistance.";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvLogoutMessage.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvLogoutMessage.setText(Html.fromHtml(Message));
            }

            tvLogoutMessage.setText(Message);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Intent intent = new Intent(LogoutActivity.this,SplashActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
    }
}
