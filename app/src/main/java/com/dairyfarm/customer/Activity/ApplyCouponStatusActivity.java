package com.dairyfarm.customer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dairyfarm.customer.R;

public class ApplyCouponStatusActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iVBackCouponCodeAppliedSuccess, iVBackCouponCodeAppliedFail;
    LinearLayout llCouponCodeAppliedSuccess, llCouponCodeAppliedFail;
    TextView tvCouponCodeAppliedSuccess, tvCouponCodeAppliedFail;
    Button btnCouponCodeAppliedSuccessOkGotIt, btnCouponCodeAppliedFailOkGotIt;

    String flag, Message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon_status);

        iVBackCouponCodeAppliedSuccess = findViewById(R.id.iVBackCouponCodeAppliedSuccess);
        iVBackCouponCodeAppliedFail = findViewById(R.id.iVBackCouponCodeAppliedFail);

        iVBackCouponCodeAppliedSuccess.setOnClickListener(this);
        iVBackCouponCodeAppliedFail.setOnClickListener(this);

        llCouponCodeAppliedSuccess = findViewById(R.id.llCouponCodeAppliedSuccess);
        llCouponCodeAppliedFail = findViewById(R.id.llCouponCodeAppliedFail);

        tvCouponCodeAppliedSuccess = findViewById(R.id.tvCouponCodeAppliedSuccess);
        tvCouponCodeAppliedFail = findViewById(R.id.tvCouponCodeAppliedFail);

        btnCouponCodeAppliedSuccessOkGotIt = findViewById(R.id.btnCouponCodeAppliedSuccessOkGotIt);
        btnCouponCodeAppliedFailOkGotIt = findViewById(R.id.btnCouponCodeAppliedFailOkGotIt);

        btnCouponCodeAppliedSuccessOkGotIt.setOnClickListener(this);
        btnCouponCodeAppliedFailOkGotIt.setOnClickListener(this);

        Intent i = getIntent();

        if (i.hasExtra("flag")) {
            flag = i.getStringExtra("flag");
        } else {
            flag = "";
        }

        if (i.hasExtra("Message")) {
            Message = i.getStringExtra("Message");
        } else {
            Message = "";
        }

        setData();

    }

    private void setData() {
        if (flag.equals("1")) {
            llCouponCodeAppliedSuccess.setVisibility(View.VISIBLE);
            llCouponCodeAppliedFail.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvCouponCodeAppliedSuccess.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvCouponCodeAppliedSuccess.setText(Html.fromHtml(Message));
            }

        } else {
            llCouponCodeAppliedSuccess.setVisibility(View.GONE);
            llCouponCodeAppliedFail.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvCouponCodeAppliedFail.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvCouponCodeAppliedFail.setText(Html.fromHtml(Message));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iVBackCouponCodeAppliedSuccess:
            case R.id.btnCouponCodeAppliedSuccessOkGotIt:
            case R.id.iVBackCouponCodeAppliedFail:
            case R.id.btnCouponCodeAppliedFailOkGotIt:
                onBackPressed();
                break;
        }
    }
}
