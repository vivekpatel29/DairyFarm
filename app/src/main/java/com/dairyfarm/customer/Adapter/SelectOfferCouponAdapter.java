package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dairyfarm.customer.Activity.SelectOfferCouponApplyPromoCodeActivity;
import com.dairyfarm.customer.Model.SelectOfferCoupon;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SelectOfferCouponAdapter extends RecyclerView.Adapter<SelectOfferCouponAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<SelectOfferCoupon> listSelectOfferCoupon;
    AtClass atClass;

    public SelectOfferCouponAdapter(ArrayList<SelectOfferCoupon> listSelectOfferCoupon) {
        this.listSelectOfferCoupon = listSelectOfferCoupon;
    }

    @Override
    public SelectOfferCouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        atClass = new AtClass();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.select_offer_coupon_list_row_item_layout, parent, false);
        return new SelectOfferCouponAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final SelectOfferCouponAdapter.ViewHolder holder, final int position) {
        final SelectOfferCoupon selectOfferCoupon = listSelectOfferCoupon.get(position);

        holder.tvOfferCouponCode.setText(selectOfferCoupon.getCouponCode());
        holder.tvOfferTitle.setText(selectOfferCoupon.getCouponTitle());
        holder.tvOfferDescription.setText(selectOfferCoupon.getCouponDescription());

        if (selectOfferCoupon.getCouponMinimumAmount() != null
                && !selectOfferCoupon.getCouponMinimumAmount().isEmpty()
                && !selectOfferCoupon.getCouponMinimumAmount().equals("")) {
            holder.llCouponMinimumAmount.setVisibility(View.VISIBLE);
            holder.tvCouponMinimumAmount.setText(selectOfferCoupon.getCouponMinimumAmount());
        } else {
            holder.llCouponMinimumAmount.setVisibility(View.GONE);
        }

        if (selectOfferCoupon.getCouponDiscount() != null
                && !selectOfferCoupon.getCouponDiscount().isEmpty()
                && !selectOfferCoupon.getCouponDiscount().equals("")) {
            holder.llDiscountAmount.setVisibility(View.VISIBLE);
            holder.tvDiscountAmount.setText(selectOfferCoupon.getCouponDiscount());
        } else {
            holder.llDiscountAmount.setVisibility(View.GONE);
        }


        if (selectOfferCoupon.getCouponType() != null
                && !selectOfferCoupon.getCouponType().isEmpty()
                && !selectOfferCoupon.getCouponType().equals("")) {
            holder.llCouponType.setVisibility(View.VISIBLE);
            holder.tvCouponType.setText(selectOfferCoupon.getCouponType());
        } else {
            holder.llCouponType.setVisibility(View.GONE);
        }


        if (selectOfferCoupon.getCouponUsagePersPerson() != null
                && !selectOfferCoupon.getCouponUsagePersPerson().isEmpty()
                && !selectOfferCoupon.getCouponUsagePersPerson().equals("")) {
            holder.llUsagePerUser.setVisibility(View.VISIBLE);
            holder.tvUsagePerUser.setText(selectOfferCoupon.getCouponUsagePersPerson());
        } else {
            holder.llUsagePerUser.setVisibility(View.GONE);
        }

        if (selectOfferCoupon.getCouponEndDate() != null
                && !selectOfferCoupon.getCouponEndDate().isEmpty()
                && !selectOfferCoupon.getCouponEndDate().equals("")) {
            holder.llCouponEndDate.setVisibility(View.VISIBLE);
            holder.tvCouponEndDate.setText(selectOfferCoupon.getCouponEndDate());
        } else {
            holder.llCouponEndDate.setVisibility(View.GONE);
        }

        holder.llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.llMore.setVisibility(View.GONE);
                holder.llCouponTermsConditions.setVisibility(View.VISIBLE);

            }
        });

        holder.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (atClass.isNetworkAvailable(mContext)) {
                    Intent intent = new Intent();
                    intent.putExtra("CouponStatus", "1");
                    intent.putExtra("CouponID", selectOfferCoupon.getCouponID());
                    ((SelectOfferCouponApplyPromoCodeActivity) mContext).setResult(RESULT_OK, intent);
                    ((SelectOfferCouponApplyPromoCodeActivity) mContext).finish();
                } else {
                    Toast.makeText(mContext, "Please Check Your Internet Connection & Try Again Later!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSelectOfferCoupon.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOfferCouponCode, tvOfferTitle, tvOfferDescription;
        LinearLayout llMore, llCouponTermsConditions;
        LinearLayout llCouponMinimumAmount, llDiscountAmount, llCouponType, llUsagePerUser, llCouponEndDate;
        TextView tvCouponMinimumAmount, tvDiscountAmount, tvCouponType, tvUsagePerUser, tvCouponEndDate;
        TextView tvApply;


        public ViewHolder(View itemView) {
            super(itemView);

            tvOfferCouponCode = itemView.findViewById(R.id.tvOfferCouponCode);
            tvOfferTitle = itemView.findViewById(R.id.tvOfferTitle);
            tvOfferDescription = itemView.findViewById(R.id.tvOfferDescription);

            llMore = itemView.findViewById(R.id.llMore);
            llCouponTermsConditions = itemView.findViewById(R.id.llCouponTermsConditions);
            llCouponMinimumAmount = itemView.findViewById(R.id.llCouponMinimumAmount);
            llDiscountAmount = itemView.findViewById(R.id.llDiscountAmount);
            llCouponType = itemView.findViewById(R.id.llCouponType);
            llUsagePerUser = itemView.findViewById(R.id.llUsagePerUser);
            llCouponEndDate = itemView.findViewById(R.id.llCouponEndDate);

            tvCouponMinimumAmount = itemView.findViewById(R.id.tvCouponMinimumAmount);
            tvDiscountAmount = itemView.findViewById(R.id.tvDiscountAmount);
            tvCouponType = itemView.findViewById(R.id.tvCouponType);
            tvUsagePerUser = itemView.findViewById(R.id.tvUsagePerUser);
            tvCouponEndDate = itemView.findViewById(R.id.tvCouponEndDate);
            tvApply = itemView.findViewById(R.id.tvApply);
        }
    }
}

