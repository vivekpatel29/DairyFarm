package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dairyfarm.customer.R;

public class OrderPlacedSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView done;

    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    Button btnShopMore;
    TextView tvViewOrder;
    TextView tvMessage;
    String Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed_success);

        done = findViewById(R.id.done);
        //konfettiView = findViewById(R.id.viewKonfetti);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCheckAnimation();
            }
        }, 500);

        tvViewOrder = findViewById(R.id.tvViewOrder);
        btnShopMore = findViewById(R.id.btnShopMore);
        tvMessage = findViewById(R.id.tvMessage);

        tvViewOrder.setOnClickListener(this);
        btnShopMore.setOnClickListener(this);

        Intent i = getIntent();
        if (i.hasExtra("Message")) {
            Message = i.getStringExtra("Message");
            tvMessage.setText(Message);
        } else {
            Message = "";
        }
    }

    private void setCheckAnimation() {
        Drawable drawable = done.getDrawable();
        if (drawable instanceof AnimatedVectorDrawableCompat) {
            avd = (AnimatedVectorDrawableCompat) drawable;
            avd.start();
        } else if (drawable instanceof AnimatedVectorDrawable) {
            avd2 = (AnimatedVectorDrawable) drawable;
            avd2.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvViewOrder:
//                Intent viewOrderIntent = new Intent(this, MyOrderActivity.class);
//                viewOrderIntent.setFlags(viewOrderIntent.FLAG_ACTIVITY_CLEAR_TOP | viewOrderIntent.FLAG_ACTIVITY_CLEAR_TASK | viewOrderIntent.FLAG_ACTIVITY_NEW_TASK);
//                viewOrderIntent.putExtra("status", "1");
//                startActivity(viewOrderIntent);
                break;

            case R.id.btnShopMore:
                Intent shopMoreIntent = new Intent(this, HomeActivity.class);
                shopMoreIntent.setFlags(shopMoreIntent.FLAG_ACTIVITY_CLEAR_TOP | shopMoreIntent.FLAG_ACTIVITY_CLEAR_TASK | shopMoreIntent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(shopMoreIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}