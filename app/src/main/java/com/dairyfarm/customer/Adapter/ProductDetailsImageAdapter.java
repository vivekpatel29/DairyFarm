package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dairyfarm.customer.Activity.ProductDetailsActivity;
import com.dairyfarm.customer.Activity.ProductDetailsImagesActivity;
import com.dairyfarm.customer.Model.ProductDetailsImage;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

public class ProductDetailsImageAdapter extends RecyclerView.Adapter<ProductDetailsImageAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<ProductDetailsImage> listProductDetailsImage;

    int selectedItem = 0;

    String isFrom;

    public ProductDetailsImageAdapter(ArrayList<ProductDetailsImage> listProductDetailsImage, String isFrom) {
        this.listProductDetailsImage = listProductDetailsImage;
        this.isFrom = isFrom;
    }

    @Override
    public ProductDetailsImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.product_details_images_list_row_item_layout, parent, false);
        mView.setClickable(true);
        return new ProductDetailsImageAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProductDetailsImageAdapter.ViewHolder holder, final int position) {
        final ProductDetailsImage productDetailsImage = listProductDetailsImage.get(position);

        if (selectedItem == position) {
            holder.ivImageBg.setBackgroundResource(R.drawable.product_details_image_selected_bg);
            if (isFrom.equals("1")) {
                ((ProductDetailsActivity) mContext).SetImageToBigImageView(productDetailsImage.getProductImageURL());
            } else {
                ((ProductDetailsImagesActivity) mContext).SetImageToBigImageView(productDetailsImage.getProductImageURL());
            }
        } else {
            holder.ivImageBg.setBackgroundResource(R.drawable.product_details_image_not_selected_bg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = position;
                notifyDataSetChanged();
                //((ProductDetailsImagesActivity)mContext).SetImageToBigImageView(productDetailsImage.getProductImageURL());
            }
        });


        holder.llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(productDetailsImage.getProductImageURL()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_login).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.llProgressLayout.setVisibility(View.GONE);
                holder.iVProductDetailsImage.setImageResource(R.drawable.app_icon_login);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                holder.llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.iVProductDetailsImage);


    }

    @Override
    public int getItemCount() {
        return listProductDetailsImage.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(final int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iVProductDetailsImage;
        ImageView ivImageBg;
        LinearLayout llProgressLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            iVProductDetailsImage = itemView.findViewById(R.id.iVProductDetailsImage);
            ivImageBg = itemView.findViewById(R.id.ivImageBg);
            llProgressLayout = itemView.findViewById(R.id.llProgressLayout);
        }
    }
}
