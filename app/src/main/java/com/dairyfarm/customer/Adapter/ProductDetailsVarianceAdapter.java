package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dairyfarm.customer.Activity.ProductDetailsActivity;
import com.dairyfarm.customer.Model.ProductDetailsVariance;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

public class ProductDetailsVarianceAdapter extends RecyclerView.Adapter<ProductDetailsVarianceAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<ProductDetailsVariance> listProductDetailsVariance;

    String atr_grp_id;

    int selectedVariancePosition = -1;

    boolean isFist = false;


    public String getAtr_grp_id() {
        return atr_grp_id;
    }

    public void setAtr_grp_id(String atr_grp_id) {
        this.atr_grp_id = atr_grp_id;
    }

    public ProductDetailsVarianceAdapter(ArrayList<ProductDetailsVariance> listProductDetailsVariance) {
        this.listProductDetailsVariance = listProductDetailsVariance;
    }

    @Override
    public ProductDetailsVarianceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.product_details_variance_row_item_layout, parent, false);
        return new ProductDetailsVarianceAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProductDetailsVarianceAdapter.ViewHolder holder, final int position) {
        final ProductDetailsVariance productDetailsVariance = listProductDetailsVariance.get(position);

        if (!isFist) {
            selectedVariancePosition = 0;
            holder.btnProductAttribute.setBackgroundResource(R.drawable.selected_product_details_variance_bg_shape);
            holder.btnProductAttribute.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            ((ProductDetailsActivity) mContext).setUpProductVariantsDetails(productDetailsVariance.getAttributeID(), productDetailsVariance.getAttributeString(), productDetailsVariance.getAttributeGroupID(), productDetailsVariance.getAddToCart(), productDetailsVariance.getCartString(), productDetailsVariance.getCartID(), productDetailsVariance.getCartQty(), productDetailsVariance.getWishlistID(), productDetailsVariance.getMarketPrice(), productDetailsVariance.getSellingPrice(), productDetailsVariance.getSavingPercentage(), productDetailsVariance.getHasOffer(), productDetailsVariance.getOfferString(), productDetailsVariance.getOfferID(), productDetailsVariance.getIsFeatured(), productDetailsVariance.getFeatureString(), productDetailsVariance.getShareLink(), productDetailsVariance.getListImages());
            isFist = true;
        }

        if (selectedVariancePosition == position) {
            holder.btnProductAttribute.setBackgroundResource(R.drawable.selected_product_details_variance_bg_shape);
            holder.btnProductAttribute.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            ((ProductDetailsActivity) mContext).setUpProductVariantsDetails(productDetailsVariance.getAttributeID(), productDetailsVariance.getAttributeString(), productDetailsVariance.getAttributeGroupID(), productDetailsVariance.getAddToCart(), productDetailsVariance.getCartString(), productDetailsVariance.getCartID(), productDetailsVariance.getCartQty(), productDetailsVariance.getWishlistID(), productDetailsVariance.getMarketPrice(), productDetailsVariance.getSellingPrice(), productDetailsVariance.getSavingPercentage(), productDetailsVariance.getHasOffer(), productDetailsVariance.getOfferString(), productDetailsVariance.getOfferID(), productDetailsVariance.getIsFeatured(), productDetailsVariance.getFeatureString(), productDetailsVariance.getShareLink(), productDetailsVariance.getListImages());
        } else {
            holder.btnProductAttribute.setBackgroundResource(R.drawable.not_selected_product_details_variance_bg_shape);
            holder.btnProductAttribute.setTextColor(mContext.getResources().getColor(R.color.colorBlackThree));
        }

        holder.btnProductAttribute.setText(productDetailsVariance.getAttributeString());

        holder.btnProductAttribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedVariancePosition = position;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                }, 10);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProductDetailsVariance.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView btnProductAttribute;

        public ViewHolder(View itemView) {
            super(itemView);
            btnProductAttribute = itemView.findViewById(R.id.btn_product_attribute);
        }
    }


}

