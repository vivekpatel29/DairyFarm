package com.dairyfarm.customer.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dairyfarm.customer.Adapter.TimeSlotAdapter;
import com.dairyfarm.customer.Model.TimeSlotTime;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.DefaultTimeSlotManager;
import com.dairyfarm.customer.Utils.ItemOffsetDecoration;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TimeSlotTimeFragment extends Fragment {
    View view;
    RecyclerView rvTimeSlotTimeRecyclerView;
    Context mContext;
    Context mCtx;
    ProgressDialogHandler progressDialogHandler;
    AtClass atClass;

    ArrayList<TimeSlotTime> listTimeSlotTime = new ArrayList<>();


    LinearLayout llTimeSlotTimes, llTimeSlotNoTimes;
    TextView tvMessage;

    DefaultTimeSlotManager defaultTimeSlotManager;

    String SelectedTimeSlotTimeID;


    @SuppressLint("ValidFragment")
    public TimeSlotTimeFragment(Context context) {
        // Required empty public constructor
        mCtx = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_time_slot_time, container, false);
        mContext = view.getContext();

        defaultTimeSlotManager = new DefaultTimeSlotManager(mContext);

        rvTimeSlotTimeRecyclerView = view.findViewById(R.id.rvTimeSlotTimeRecyclerView);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        rvTimeSlotTimeRecyclerView.addItemDecoration(itemDecoration);


        llTimeSlotTimes = view.findViewById(R.id.llTimeSlotTimes);
        llTimeSlotNoTimes = view.findViewById(R.id.llTimeSlotNoTimes);

        tvMessage = view.findViewById(R.id.tvMessage);
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    UpdateData();
                }
            }, 100);
        }
    }


    private void UpdateData() {
        //progressBarHandler = new ProgressBarHandler(mCtx);
        atClass = new AtClass();

        Bundle b = getArguments();
        if (b != null) {
            listTimeSlotTime = (ArrayList<TimeSlotTime>) b.get("listTimeSlotTime");

            if (listTimeSlotTime != null && !listTimeSlotTime.isEmpty() && listTimeSlotTime.size() > 0) {
                llTimeSlotTimes.setVisibility(View.VISIBLE);
                llTimeSlotNoTimes.setVisibility(View.GONE);

                TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(listTimeSlotTime, defaultTimeSlotManager.getTimeSlotDefaultID());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                rvTimeSlotTimeRecyclerView.setLayoutManager(linearLayoutManager);
                rvTimeSlotTimeRecyclerView.setAdapter(timeSlotAdapter);

            } else {
                llTimeSlotTimes.setVisibility(View.GONE);
                llTimeSlotNoTimes.setVisibility(View.VISIBLE);
                tvMessage.setText(getString(R.string.time_slot_no_time_slot_found_for_the_selected_time_slot_text));
            }
        } else {
            llTimeSlotTimes.setVisibility(View.GONE);
            llTimeSlotNoTimes.setVisibility(View.VISIBLE);
            tvMessage.setText(getString(R.string.time_slot_no_time_slot_found_for_the_selected_time_slot_text));
        }
    }
}