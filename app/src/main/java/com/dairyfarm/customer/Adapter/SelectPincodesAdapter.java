package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dairyfarm.customer.Activity.SelectPincodeActivity;
import com.dairyfarm.customer.Model.SelectPincodes;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SelectPincodesAdapter extends RecyclerView.Adapter<SelectPincodesAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<SelectPincodes> listSelectPincodes;
    String PincodeID;
    String isFrom;

    public SelectPincodesAdapter(ArrayList<SelectPincodes> listSelectPincodes, String PincodeID, String isFrom) {
        this.listSelectPincodes = listSelectPincodes;
        this.PincodeID = PincodeID;
        this.isFrom = isFrom;
    }

    @Override
    public SelectPincodesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.select_pincode_list_row_item_layout, parent, false);
        return new SelectPincodesAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(SelectPincodesAdapter.ViewHolder holder, final int position) {
        final SelectPincodes selectPincodes = listSelectPincodes.get(position);

        holder.tvPincodesName.setSelected(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvPincodesName.setText(Html.fromHtml(selectPincodes.getPincodeTitle(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvPincodesName.setText(Html.fromHtml(selectPincodes.getPincodeTitle()));
        }

        if (PincodeID != null && !PincodeID.isEmpty() && !PincodeID.equals("")) {
            if (PincodeID.equals(selectPincodes.getPincodeID())) {
                holder.tvPincodesName.setTextColor(Color.parseColor("#90b943"));
            } else {
                holder.tvPincodesName.setTextColor(Color.parseColor("#000000"));
            }
        }

        holder.cVPincodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("PincodeID", selectPincodes.getPincodeID());
                intent.putExtra("PincodeName", selectPincodes.getPincodeNumber());
                intent.putExtra("Area", selectPincodes.getArea());
                intent.putExtra("City", selectPincodes.getCity());
                intent.putExtra("State", selectPincodes.getState());
                ((SelectPincodeActivity) mContext).setResult(RESULT_OK, intent);
                ((SelectPincodeActivity) mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSelectPincodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPincodesName;
        CardView cVPincodes;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPincodesName = itemView.findViewById(R.id.tvPincodesName);
            cVPincodes = itemView.findViewById(R.id.cVPincodes);
        }
    }

    public void filterList(ArrayList<SelectPincodes> filterdNames) {
        this.listSelectPincodes = filterdNames;
        notifyDataSetChanged();
    }

    public interface SetOnViewButtonClickListener {
        public void onViewButtonClicked(SelectPincodes selectPincodes, int position);
    }

    public interface SetOnEditButtonClickListener {
        public void onEditButtonClicked(SelectPincodes selectPincodes, int position);
    }
}

