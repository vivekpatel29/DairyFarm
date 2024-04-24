package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dairyfarm.customer.Model.ProductVariance;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

public class ProductVarianceAdapter extends RecyclerView.Adapter<ProductVarianceAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<ProductVariance> listSelectVariance;
    int selectedPosition = -1;

    boolean isFist = false;

    String ProductVarianceID;

    /*ProgressBarHandler progressBarHandler;*/
    /*ProgressDialogHandler progressDialogHandler;*/

    public ProductVarianceAdapter(ArrayList<ProductVariance> listSelectVariance, String ProductVarianceID) {
        this.listSelectVariance = listSelectVariance;
        this.ProductVarianceID = ProductVarianceID;
    }

    @Override
    public ProductVarianceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.select_product_variance_list_row_item_layout, parent, false);
        return new ProductVarianceAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProductVarianceAdapter.ViewHolder holder, final int position) {
        final ProductVariance productVariance = listSelectVariance.get(position);

        if (!isFist) {
            if (ProductVarianceID != null && !ProductVarianceID.equals("") && !ProductVarianceID.isEmpty()) {
                if (ProductVarianceID.equals(productVariance.getAttributeID())) {
                    Log.d("ProductVarianceID", "Value" + ProductVarianceID);
                    selectedPosition = position;
                    holder.rbSelected.setVisibility(View.VISIBLE);
                    holder.rbNotSelected.setVisibility(View.GONE);
                    holder.tvProductVarianceName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    holder.tvProductVarianceMarketPrice.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    holder.tvProductVarianceSellingPrice.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

                    if (productVariance.getAddToCart().equalsIgnoreCase("1")) {
                        ProductsAdapter.setButton("1");
                        ProductsAdapter.setData(productVariance.getAttributeID(), productVariance.getSellingPrice(), productVariance.getMarketPrice(), productVariance.getCartID(), productVariance.getCartQty());
                    } else {
                        ProductsAdapter.setButton("0");
                    }

                    isFist = true;
                }
            }
        }

        if (productVariance.getAttributeString() != null && !productVariance.getAttributeString().isEmpty() && !productVariance.getAttributeString().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvProductVarianceName.setText(Html.fromHtml(productVariance.getAttributeString(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvProductVarianceName.setText(Html.fromHtml(productVariance.getAttributeString()));
            }
            holder.tvProductVarianceName.setVisibility(View.VISIBLE);
        } else {
            holder.tvProductVarianceName.setVisibility(View.GONE);
        }

        if (productVariance.getMarketPrice() != null && !productVariance.getMarketPrice().isEmpty() && !productVariance.getMarketPrice().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvProductVarianceMarketPrice.setText(Html.fromHtml(productVariance.getMarketPrice(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvProductVarianceMarketPrice.setText(Html.fromHtml(productVariance.getMarketPrice()));
            }
            holder.tvProductVarianceMarketPrice.setVisibility(View.VISIBLE);
            holder.tvProductVarianceMarketPrice.setPaintFlags(holder.tvProductVarianceMarketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvProductVarianceMarketPrice.setVisibility(View.GONE);
        }

        if (productVariance.getSellingPrice() != null && !productVariance.getSellingPrice().isEmpty() && !productVariance.getSellingPrice().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvProductVarianceSellingPrice.setText(Html.fromHtml(productVariance.getSellingPrice(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvProductVarianceSellingPrice.setText(Html.fromHtml(productVariance.getSellingPrice()));
            }
            holder.tvProductVarianceSellingPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvProductVarianceSellingPrice.setVisibility(View.GONE);
        }

        if (selectedPosition == position) {
            holder.rbSelected.setVisibility(View.VISIBLE);
            holder.rbNotSelected.setVisibility(View.GONE);

            holder.tvProductVarianceName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.tvProductVarianceMarketPrice.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.tvProductVarianceSellingPrice.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

            if (productVariance.getAddToCart().equalsIgnoreCase("1")) {
                ProductsAdapter.setButton("1");
                ProductsAdapter.setData(productVariance.getAttributeID(), productVariance.getSellingPrice(), productVariance.getMarketPrice(), productVariance.getCartID(), productVariance.getCartQty());
            } else {
                ProductsAdapter.setButton("0");
            }
            /*holder.ll*/
            //((SelectAddressActivity) mContext).setButton();
            //((SelectAddressActivity) mContext).setData(selectAddress.getAddressID(), selectAddress.getUserID(), selectAddress.getAddressOne(), selectAddress.getAddressTwo(), selectAddress.getArea(), selectAddress.getCity(), selectAddress.getState(), selectAddress.getCountry(), selectAddress.getZipcode(),selectAddress.getMobileNumber());
        } else {
            holder.rbSelected.setVisibility(View.GONE);
            holder.rbNotSelected.setVisibility(View.VISIBLE);

            holder.tvProductVarianceName.setTextColor(Color.parseColor("#333333"));
            holder.tvProductVarianceMarketPrice.setTextColor(Color.parseColor("#333333"));
            holder.tvProductVarianceSellingPrice.setTextColor(Color.parseColor("#000000"));

        }

        holder.llProductVariance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // change image
                        notifyDataSetChanged();
                    }

                }, 10); // 100ms delay
            }
        });
    }

    private boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public int getItemCount() {
        return listSelectVariance.size();
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
        TextView tvProductVarianceName, tvProductVarianceSellingPrice, tvProductVarianceMarketPrice;
        ImageView rbSelected, rbNotSelected;
        LinearLayout llProductVariance;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductVarianceName = itemView.findViewById(R.id.tvProductVarianceName);
            tvProductVarianceSellingPrice = itemView.findViewById(R.id.tvProductVarianceSellingPrice);
            tvProductVarianceMarketPrice = itemView.findViewById(R.id.tvProductVarianceMarketPrice);
            rbSelected = itemView.findViewById(R.id.rbSelected);
            rbNotSelected = itemView.findViewById(R.id.rbNotSelected);
            llProductVariance = itemView.findViewById(R.id.llProductVariance);
        }
    }
}
