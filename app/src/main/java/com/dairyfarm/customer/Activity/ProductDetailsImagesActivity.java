package com.dairyfarm.customer.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dairyfarm.customer.Adapter.ProductDetailsImageAdapter;
import com.dairyfarm.customer.Model.ProductDetailsImage;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ProductDetailsImagesActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iVBack;
    PhotoView ivProductBigImageView;
    RecyclerView rVProductVarianceWiseImages;

    ArrayList<ProductDetailsImage> listProductDetailsWiseVarianceImage = new ArrayList<>();
    ProductDetailsImage productDetailsImage;

    FrameLayout flProductDetailsVarianceWiseImages;
    LinearLayout llNoRecordFound, llNoInternetConnection;
    TextView tvMessage;

    LinearLayout llProgressLayout;

    SwipeRefreshLayout swipeToRefreshLayout;

    Button btnRetry;

    AtClass atClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_images);

        atClass = new AtClass();
        llProgressLayout = findViewById(R.id.llProgressLayout);

        ivProductBigImageView = findViewById(R.id.ivProductBigImageView);
        iVBack = findViewById(R.id.iVBack);
        iVBack.setOnClickListener(this);

        flProductDetailsVarianceWiseImages = findViewById(R.id.flProductDetailsVarianceWiseImages);
        llNoRecordFound = findViewById(R.id.llError);
        llNoInternetConnection = findViewById(R.id.llNoInternetConnection);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        tvMessage = findViewById(R.id.tvMessage);

        rVProductVarianceWiseImages = findViewById(R.id.rVProductVarianceWiseImages);
        ItemOffsetDecoration itemDecoration1 = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rVProductVarianceWiseImages.addItemDecoration(itemDecoration1);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rVProductVarianceWiseImages.setLayoutManager(linearLayoutManager1);

        ivProductBigImageView.setOnClickListener(this);

        if (atClass.isNetworkAvailable(this)) {
            if (getIntent().hasExtra("arrayListProductVarianceWiseDetails")) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    listProductDetailsWiseVarianceImage = (ArrayList<ProductDetailsImage>) bundle.getSerializable("arrayListProductVarianceWiseDetails");
                    if (listProductDetailsWiseVarianceImage != null && !listProductDetailsWiseVarianceImage.isEmpty() && !listProductDetailsWiseVarianceImage.equals("")) {
                        SetData();
                    } else {
                        llNoRecordFound.setVisibility(View.VISIBLE);
                        llNoInternetConnection.setVisibility(View.GONE);
                        tvMessage.setText(getString(R.string.product_image_details_no_image_for_product_text));
                        flProductDetailsVarianceWiseImages.setVisibility(View.GONE);
                    }
                } else {
                    llNoRecordFound.setVisibility(View.VISIBLE);
                    llNoInternetConnection.setVisibility(View.GONE);
                    tvMessage.setText(getString(R.string.product_image_details_no_image_for_product_text));
                    flProductDetailsVarianceWiseImages.setVisibility(View.GONE);
                }
            }
        } else {
            llNoRecordFound.setVisibility(View.GONE);
            llNoInternetConnection.setVisibility(View.VISIBLE);
            flProductDetailsVarianceWiseImages.setVisibility(View.GONE);
        }

        setUpSwipeToRefreshLayout();
    }


    private void setUpSwipeToRefreshLayout() {
        swipeToRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeToRefreshLayout.setColorSchemeResources(R.color.gradientStartColor, R.color.gradientEndColor);

        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefreshLayout.setRefreshing(false);
                Intent i = new Intent(ProductDetailsImagesActivity.this, ProductDetailsImagesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("arrayListProductVarianceWiseDetails", listProductDetailsWiseVarianceImage);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

    }

    private void SetData() {
        llNoRecordFound.setVisibility(View.GONE);
        flProductDetailsVarianceWiseImages.setVisibility(View.VISIBLE);

        ProductDetailsImageAdapter productDetailsImageAdapter = new ProductDetailsImageAdapter(listProductDetailsWiseVarianceImage, "0");
        rVProductVarianceWiseImages.setAdapter(productDetailsImageAdapter);
        rVProductVarianceWiseImages.setSelected(true);
    }

    public void SetImageToBigImageView(String productImageURL) {
        llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(this).load(productImageURL).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_transparent).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                llProgressLayout.setVisibility(View.GONE);
                ivProductBigImageView.setImageResource(R.drawable.app_icon_transparent);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(ivProductBigImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProductBigImageView:
                Log.d("Clicked", "Yes");
                if (rVProductVarianceWiseImages.getVisibility() == View.GONE) {
                    rVProductVarianceWiseImages.setVisibility(View.VISIBLE);
                } else {
                    rVProductVarianceWiseImages.setVisibility(View.GONE);
                }
                break;

            case R.id.iVBack:
                onBackPressed();
                break;

            case R.id.btnRetry:
                if (atClass.isNetworkAvailable(this)) {
                    Intent i = new Intent(ProductDetailsImagesActivity.this, ProductDetailsImagesActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("arrayListProductVarianceWiseDetails", listProductDetailsWiseVarianceImage);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                } else {
                    llNoRecordFound.setVisibility(View.GONE);
                    llNoInternetConnection.setVisibility(View.VISIBLE);
                    flProductDetailsVarianceWiseImages.setVisibility(View.GONE);
                }
                break;
        }
    }
}

