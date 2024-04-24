package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dairyfarm.customer.Activity.SelectAddressActivity;
import com.dairyfarm.customer.Model.SelectAddress;
import com.dairyfarm.customer.R;

import java.util.ArrayList;

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<SelectAddress> listSelectAddress;
    int selectedPosition = -1;

    boolean isFist = false;

    String AddressID;
    /*ProgressBarHandler progressBarHandler;*/

    /*ProgressDialogHandler progressDialogHandler;*/

    public SelectAddressAdapter(ArrayList<SelectAddress> listSelectAddress, String AddressID) {
        this.listSelectAddress = listSelectAddress;
        this.AddressID = AddressID;
    }

    @Override
    public SelectAddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.select_address_list_row_item_layout, parent, false);
        /*progressDialogHandler = new ProgressDialogHandler(mContext);*/
//        progressBarHandler = new ProgressBarHandler(mContext);
        return new SelectAddressAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final SelectAddressAdapter.ViewHolder holder, final int position) {
        final SelectAddress selectAddress = listSelectAddress.get(position);

        //int isDefault = Integer.parseInt(listSelectAddress.get(position).getIsDefault());

        if (!isFist) {
            if (AddressID != null && !AddressID.equals("") && !AddressID.isEmpty()) {
                if (AddressID.equals(selectAddress.getAddressID())) {
                    //Log.d("AddressID", "AdaterAdd" + AddressID);
                    selectedPosition = position;
                    holder.rbSelected.setVisibility(View.VISIBLE);
                    holder.rbNotSelected.setVisibility(View.GONE);

                    if (selectAddress.getIsAvailable().equals("1")) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((SelectAddressActivity) mContext).setShowButton();
                            }

                        }, 100); // 100ms delay
                        ((SelectAddressActivity) mContext).setData(selectAddress.getAddressID());
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                ((SelectAddressActivity) mContext).setHideButton();
                            }

                        }, 100); // 100ms delay
                        ((SelectAddressActivity) mContext).setData(selectAddress.getAddressID());
                    }
                    isFist = true;
                }
            }
        }

        if (selectAddress.getIsAvailable().equals("1")) {
            holder.tvDeliveryNotAvailable.setVisibility(View.GONE);
        } else {
            holder.tvDeliveryNotAvailable.setVisibility(View.VISIBLE);
            holder.tvDeliveryNotAvailable.setText(selectAddress.getAvailableString());
        }

        int isDefaultAddress = Integer.parseInt(selectAddress.getIsDefault());
        if (isDefaultAddress == 1) {
            holder.tvDefaultAddressTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvDefaultAddressTitle.setVisibility(View.INVISIBLE);
        }

        if (selectAddress.getAddressName() != null && !selectAddress.getAddressName().isEmpty() && !selectAddress.getAddressName().equals("")) {
            holder.tvAddressName.setText(selectAddress.getAddressName());
            holder.tvAddressName.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddressName.setVisibility(View.GONE);
        }

        if (selectAddress.getAddress() != null && !selectAddress.getAddress().isEmpty() && !selectAddress.getAddress().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvAddress.setText(Html.fromHtml(selectAddress.getAddress(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvAddress.setText(Html.fromHtml(selectAddress.getAddress()));
            }
            holder.tvAddress.setVisibility(View.VISIBLE);

        } else {
            holder.tvAddress.setVisibility(View.GONE);
        }

        if (selectAddress.getMobileNumber() != null && !selectAddress.getMobileNumber().isEmpty() && !selectAddress.getMobileNumber().equals("")) {
            holder.tvMobileNumber.setText(selectAddress.getMobileNumber());
            holder.tvMobileNumber.setVisibility(View.VISIBLE);
        } else {
            holder.tvMobileNumber.setVisibility(View.GONE);
        }

        if (selectAddress.getAddressType() != null && !selectAddress.getAddressType().isEmpty() && !selectAddress.getAddressType().equals("")) {
            if (selectAddress.getAddressType().equalsIgnoreCase("Home")) {
                holder.tvAddressTypeHome.setVisibility(View.VISIBLE);
                holder.tvAddressTypeOffice.setVisibility(View.GONE);
                holder.tvAddressTypeOther.setVisibility(View.GONE);
            } else if (selectAddress.getAddressType().equalsIgnoreCase("Office")) {
                holder.tvAddressTypeHome.setVisibility(View.GONE);
                holder.tvAddressTypeOffice.setVisibility(View.VISIBLE);
                holder.tvAddressTypeOther.setVisibility(View.GONE);
            } else if (selectAddress.getAddressType().equalsIgnoreCase("Other")) {
                holder.tvAddressTypeHome.setVisibility(View.GONE);
                holder.tvAddressTypeOffice.setVisibility(View.GONE);
                holder.tvAddressTypeOther.setVisibility(View.VISIBLE);
            } else {
                holder.tvAddressTypeHome.setVisibility(View.GONE);
                holder.tvAddressTypeOffice.setVisibility(View.GONE);
                holder.tvAddressTypeOther.setVisibility(View.GONE);
            }
        } else {
            holder.tvAddressTypeHome.setVisibility(View.GONE);
            holder.tvAddressTypeOffice.setVisibility(View.GONE);
            holder.tvAddressTypeOther.setVisibility(View.GONE);
        }


        if (selectedPosition == position) {
            holder.rbSelected.setVisibility(View.VISIBLE);
            holder.rbNotSelected.setVisibility(View.GONE);
            if (selectAddress.getIsAvailable().equals("1")) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ((SelectAddressActivity) mContext).setShowButton();
                    }

                }, 100); // 100ms delay
                ((SelectAddressActivity) mContext).setData(selectAddress.getAddressID());

            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        ((SelectAddressActivity) mContext).setHideButton();
                    }

                }, 100); // 100ms delay
                ((SelectAddressActivity) mContext).setData(selectAddress.getAddressID());
            }
        } else {
            holder.rbSelected.setVisibility(View.GONE);
            holder.rbNotSelected.setVisibility(View.VISIBLE);
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
    }

    @Override
    public int getItemCount() {
        return listSelectAddress.size();
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
        RadioButton rbSelectAddress;
        TextView tvDefaultAddressTitle, tvAddressName, tvAddress, tvMobileNumber, tvDeliveryNotAvailable;
        ImageView rbSelected, rbNotSelected;
        TextView tvAddressTypeHome, tvAddressTypeOffice, tvAddressTypeOther;


        public ViewHolder(View itemView) {
            super(itemView);
            rbSelectAddress = itemView.findViewById(R.id.rbSelectAddress);
            tvDefaultAddressTitle = itemView.findViewById(R.id.tvDefaultAddressTitle);
            rbSelected = itemView.findViewById(R.id.rbSelected);
            rbNotSelected = itemView.findViewById(R.id.rbNotSelected);
            tvAddressName = itemView.findViewById(R.id.tvAddressName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
            tvDeliveryNotAvailable = itemView.findViewById(R.id.tvDeliveryNotAvailable);

            tvAddressTypeHome = itemView.findViewById(R.id.tvAddressTypeHome);
            tvAddressTypeOffice = itemView.findViewById(R.id.tvAddressTypeOffice);
            tvAddressTypeOther = itemView.findViewById(R.id.tvAddressTypeOther);


        }
    }

}