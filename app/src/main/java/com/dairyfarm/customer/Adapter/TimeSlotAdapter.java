package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dairyfarm.customer.Activity.SelectTimeSlotActivity;
import com.dairyfarm.customer.Model.TimeSlotTime;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.DefaultTimeSlotManager;

import java.util.ArrayList;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<TimeSlotTime> listTimeSlotTime;

    int selectedPosition = -1;

    boolean isFist = false;

    String TimeSlotTimeID;

    DefaultTimeSlotManager defaultTimeSlotManager;

    public TimeSlotAdapter(ArrayList<TimeSlotTime> listTimeSlotTime, String TimeSlotID) {
        this.listTimeSlotTime = listTimeSlotTime;
        this.TimeSlotTimeID = TimeSlotID;
    }

    @Override
    public TimeSlotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.time_slot_list_row_item_layout, parent, false);
        defaultTimeSlotManager = new DefaultTimeSlotManager(mContext);
        return new TimeSlotAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final TimeSlotAdapter.ViewHolder holder, final int position) {
        final TimeSlotTime timeSlotTime = listTimeSlotTime.get(position);

        if (!isFist) {
            if (TimeSlotTimeID != null && !TimeSlotTimeID.equals("") && !TimeSlotTimeID.isEmpty()) {
                if (TimeSlotTimeID.equals(timeSlotTime.getTimeSlotTimeID())) {
                    selectedPosition = position;
                    holder.rbSelected.setVisibility(View.VISIBLE);
                    holder.rbNotSelected.setVisibility(View.GONE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((SelectTimeSlotActivity) mContext).setShowButton();
                        }
                    }, 100); // 100ms delay
                    ((SelectTimeSlotActivity) mContext).setData(timeSlotTime.getTimeSlotTimeID());
                    defaultTimeSlotManager.setTimeSlotDefaultID(timeSlotTime.getTimeSlotTimeID());
                    isFist = true;
                    //defaultTimeSlotManager.setTimeSlotIsSettled();
                }
            } else {
                if (timeSlotTime.getIsDefault().equals("1")) {
                    selectedPosition = position;
                    holder.rbSelected.setVisibility(View.VISIBLE);
                    holder.rbNotSelected.setVisibility(View.GONE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((SelectTimeSlotActivity) mContext).setShowButton();
                        }
                    }, 100); // 100ms delay
                    ((SelectTimeSlotActivity) mContext).setData(timeSlotTime.getTimeSlotTimeID());
                    defaultTimeSlotManager.setTimeSlotDefaultID(timeSlotTime.getTimeSlotTimeID());
                    isFist = true;
                    //defaultTimeSlotManager.setTimeSlotIsSettled();
                }
            }
        }

        if (selectedPosition == position) {
            holder.rbSelected.setVisibility(View.VISIBLE);
            holder.rbNotSelected.setVisibility(View.GONE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((SelectTimeSlotActivity) mContext).setShowButton();
                }

            }, 100); // 100ms delay
            ((SelectTimeSlotActivity) mContext).setData(timeSlotTime.getTimeSlotTimeID());
            defaultTimeSlotManager.setTimeSlotDefaultID(timeSlotTime.getTimeSlotTimeID());

        } else {
            holder.rbSelected.setVisibility(View.GONE);
            holder.rbNotSelected.setVisibility(View.VISIBLE);
        }

        holder.tvTitle.setText(timeSlotTime.getTimeSlot());

        holder.cvTimeSlotTime.setOnClickListener(new View.OnClickListener() {
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
        return listTimeSlotTime.size();
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
        TextView tvTitle;
        CardView cvTimeSlotTime;
        ImageView rbSelected, rbNotSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            cvTimeSlotTime = (CardView) itemView.findViewById(R.id.cvTimeSlotTime);
            rbSelected = (ImageView) itemView.findViewById(R.id.rbSelected);
            rbNotSelected = (ImageView) itemView.findViewById(R.id.rbNotSelected);
        }
    }
}


