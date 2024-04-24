package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dairyfarm.customer.Model.ProductNotAvailableToPlaceOrder;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

public class ProductNotAvailableToPlaceOrderAdapter extends RecyclerView.Adapter<ProductNotAvailableToPlaceOrderAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<ProductNotAvailableToPlaceOrder> listProductNotAvailableToPlaceOrder;

    public ProductNotAvailableToPlaceOrderAdapter(ArrayList<ProductNotAvailableToPlaceOrder> listProductNotAvailableToPlaceOrder) {
        this.listProductNotAvailableToPlaceOrder = listProductNotAvailableToPlaceOrder;
    }

    @Override
    public ProductNotAvailableToPlaceOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.product_not_available_to_place_order_list_row_item_layout, parent, false);
        return new ProductNotAvailableToPlaceOrderAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProductNotAvailableToPlaceOrderAdapter.ViewHolder holder, final int position) {
        final ProductNotAvailableToPlaceOrder productNotAvailableToPlaceOrder = listProductNotAvailableToPlaceOrder.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductName.setText(Html.fromHtml(productNotAvailableToPlaceOrder.getProductName(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductName.setText(Html.fromHtml(productNotAvailableToPlaceOrder.getProductName()));
        }

        if (productNotAvailableToPlaceOrder.getAvailableString() != null && !productNotAvailableToPlaceOrder.getAvailableString().equals("") && !productNotAvailableToPlaceOrder.getAvailableString().isEmpty()) {
            holder.llComingSoonLayout.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvComingSoonMessage.setText(Html.fromHtml(productNotAvailableToPlaceOrder.getAvailableString(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvComingSoonMessage.setText(Html.fromHtml(productNotAvailableToPlaceOrder.getAvailableString()));
            }
        } else {
            holder.llComingSoonLayout.setVisibility(View.GONE);
        }

        holder.llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(productNotAvailableToPlaceOrder.getImageURL()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_transparent).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.llProgressLayout.setVisibility(View.GONE);
                holder.iVProductImage.setImageResource(R.drawable.app_icon_transparent);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                holder.llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.iVProductImage);


    }


    @Override
    public int getItemCount() {
        return listProductNotAvailableToPlaceOrder.size();
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
        ImageView iVProductImage;
        TextView tvProductName;
        LinearLayout llProgressLayout;
        FrameLayout llComingSoonLayout;
        TextView tvComingSoonMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            iVProductImage = itemView.findViewById(R.id.iVProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            llProgressLayout = itemView.findViewById(R.id.llProgressLayout);
            llComingSoonLayout = itemView.findViewById(R.id.llComingSoonLayout);
            tvComingSoonMessage = itemView.findViewById(R.id.tvComingSoonMessage);
        }
    }
}
