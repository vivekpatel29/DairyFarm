package com.example.dairyfarm;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.dairyfarm.R.layout.fragment_category;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(fragment_category, container, false);
        ArrayList image,product;
        RecyclerView rvproduct;

        rvproduct=view.findViewById(R.id.rvproduct);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        rvproduct.setLayoutManager(linearLayoutManager);

       image= new ArrayList<>(Arrays.asList(R.drawable.cowmilk,R.drawable.kidsmilk,R.drawable.standardmilk,R.drawable.skimmedmilk,R.drawable.doubletonedmilk,R.drawable.tonedmilk,R.drawable.fullcreammilk,R.drawable.atwomilk));
       product= new ArrayList<>(Arrays.asList("Cow Milk"," Kids Milk"," Standard Milk","Skimmed Milk","Double Toned Milk","Toned Milk","Full Cream Milk","A2 Premium Milk"));

       DataAdapter dataAdapter= new DataAdapter(image,product,getActivity());
       rvproduct.setAdapter(dataAdapter);

        return view;
    }
}