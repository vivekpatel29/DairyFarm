package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dairyfarm.customer.Activity.PaymentsActivity;
import com.dairyfarm.customer.Model.PaymentMethods;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<PaymentMethods> listPaymentMethods;
    int selectedPosition = -1;

    boolean isFist = false;
    String PaymentMethodID;

    public PaymentMethodsAdapter(ArrayList<PaymentMethods> listPaymentMethods, String PaymentMethodID) {
        this.listPaymentMethods = listPaymentMethods;
        this.PaymentMethodID = PaymentMethodID;
    }

    @Override
    public PaymentMethodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.payment_methods_list_row_item_layout, parent, false);
        return new PaymentMethodsAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final PaymentMethodsAdapter.ViewHolder holder, final int position) {
        final PaymentMethods paymentMethods = listPaymentMethods.get(position);

        if (!isFist) {
            if (PaymentMethodID != null && !PaymentMethodID.equals("") && !PaymentMethodID.isEmpty()) {
                if (PaymentMethodID.equals(paymentMethods.getPaymentMethodID())) {
                    if (paymentMethods.getCanProceed().equals("1")) {
                        selectedPosition = position;
                        holder.rbSelected.setVisibility(View.VISIBLE);
                        holder.rbNotSelected.setVisibility(View.GONE);
                        holder.rbSelectedNotSelected.setVisibility(View.GONE);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((PaymentsActivity) mContext).setShowButton();
                            }

                        }, 100); // 100ms delay
                        ((PaymentsActivity) mContext).setData(paymentMethods.getPaymentMethodID(), paymentMethods.getPaymentMethodMessage());
                    } else {
                        selectedPosition = position;
                        holder.rbSelected.setVisibility(View.GONE);
                        holder.rbNotSelected.setVisibility(View.GONE);
                        holder.rbSelectedNotSelected.setVisibility(View.VISIBLE);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((PaymentsActivity) mContext).setHideButton();
                            }
                        }, 100); // 100ms delay
                        ((PaymentsActivity) mContext).setData(paymentMethods.getPaymentMethodID(), paymentMethods.getPaymentMethodMessage());
                    }
                    isFist = true;
                }
            }
        }

        if (selectedPosition == position) {
            if (paymentMethods.getCanProceed().equals("1")) {
                holder.rbSelected.setVisibility(View.VISIBLE);
                holder.rbNotSelected.setVisibility(View.GONE);
                holder.rbSelectedNotSelected.setVisibility(View.GONE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((PaymentsActivity) mContext).setShowButton();
                    }
                }, 100); // 100ms delay
                ((PaymentsActivity) mContext).setData(paymentMethods.getPaymentMethodID(), paymentMethods.getPaymentMethodMessage());
            } else {
                holder.rbSelected.setVisibility(View.GONE);
                holder.rbNotSelected.setVisibility(View.GONE);
                holder.rbSelectedNotSelected.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((PaymentsActivity) mContext).setHideButton();
                    }

                }, 100); // 100ms delay
                ((PaymentsActivity) mContext).setData(paymentMethods.getPaymentMethodID(), paymentMethods.getPaymentMethodMessage());
            }

        } else {
            holder.rbSelected.setVisibility(View.GONE);
            holder.rbNotSelected.setVisibility(View.VISIBLE);
            holder.rbSelectedNotSelected.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }

                }, 10); // 100ms delay
            }
        });

        holder.tvPaymentMethodName.setText(paymentMethods.getPaymentMethodName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvPaymentMethodName.setText(Html.fromHtml(paymentMethods.getPaymentMethodName(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvPaymentMethodName.setText(Html.fromHtml(paymentMethods.getPaymentMethodName()));
        }


        if (paymentMethods.getPaymentMethodMessage() != null && !paymentMethods.getPaymentMethodMessage().isEmpty() && !paymentMethods.getPaymentMethodMessage().equals("")) {
            holder.tvPaymentMethodName.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvPaymentMethodMessage.setText(Html.fromHtml(paymentMethods.getPaymentMethodMessage(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvPaymentMethodMessage.setText(Html.fromHtml(paymentMethods.getPaymentMethodMessage()));
            }
        } else {
            holder.tvPaymentMethodMessage.setVisibility(View.GONE);
        }


        holder.llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(paymentMethods.getPaymentMethodImage()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_transparent).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.llProgressLayout.setVisibility(View.GONE);
                holder.iVPaymentMethodsImage.setImageResource(R.drawable.app_icon_transparent);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                holder.llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.iVPaymentMethodsImage);

    }


    @Override
    public int getItemCount() {
        return listPaymentMethods.size();
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
        TextView tvPaymentMethodName, tvPaymentMethodMessage;
        ImageView iVPaymentMethodsImage, rbNotSelected, rbSelected, rbSelectedNotSelected;
        LinearLayout llProgressLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            iVPaymentMethodsImage = itemView.findViewById(R.id.iVPaymentMethodsImage);
            tvPaymentMethodName = itemView.findViewById(R.id.tvPaymentMethodName);
            tvPaymentMethodMessage = itemView.findViewById(R.id.tvPaymentMethodMessage);


            rbNotSelected = itemView.findViewById(R.id.rbNotSelected);
            rbSelected = itemView.findViewById(R.id.rbSelected);
            rbSelectedNotSelected = itemView.findViewById(R.id.rbSelectedNotSelected);


            llProgressLayout = itemView.findViewById(R.id.llProgressLayout);
        }
    }
}