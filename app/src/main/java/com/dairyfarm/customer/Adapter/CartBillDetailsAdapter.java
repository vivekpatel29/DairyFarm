package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dairyfarm.customer.Model.CartBillDetails;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

public class CartBillDetailsAdapter  extends RecyclerView.Adapter<CartBillDetailsAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<CartBillDetails> listCartBillDetails;

    public CartBillDetailsAdapter(ArrayList<CartBillDetails> listCartBillDetails) {
        this.listCartBillDetails = listCartBillDetails;
    }

    @Override
    public CartBillDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.cart_bill_details_list_row_item_layout, parent, false);
        return new CartBillDetailsAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final CartBillDetailsAdapter.ViewHolder holder, final int position) {
        final CartBillDetails cartBillDetails = listCartBillDetails.get(position);

        if (cartBillDetails.getValue() != null &&
                !cartBillDetails.getValue().isEmpty() &&
                !cartBillDetails.getValue().equals("")) {
            holder.llCartAmountListRow.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvTitle.setText(Html.fromHtml(cartBillDetails.getTitle(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvTitle.setText(Html.fromHtml(cartBillDetails.getTitle()));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvValue.setText(Html.fromHtml(cartBillDetails.getValue(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvValue.setText(Html.fromHtml(cartBillDetails.getValue()));
            }

        } else {
            holder.llCartAmountListRow.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return listCartBillDetails.size();
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
        TextView tvTitle, tvValue;
        LinearLayout llCartAmountListRow;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvValue = (TextView) itemView.findViewById(R.id.tvValue);
            llCartAmountListRow = (LinearLayout) itemView.findViewById(R.id.llCartAmountListRow);
        }
    }
}