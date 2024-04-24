package com.example.dairyfarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {


    ArrayList<Integer> image;
    ArrayList<String>  product;
    Context context;
    public DataAdapter(ArrayList<Integer> image, ArrayList<String> product, Context context) {
        this.image = image;
        this.product = product;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.tvproduct.setText(product.get(position));
      holder.igproduct.setImageResource(image.get(position));
    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public  class ViewHolder extends  RecyclerView.ViewHolder{
       ImageView  igproduct;
       TextView tvproduct;
        public ViewHolder( View itemView) {
            super(itemView);
            igproduct= itemView.findViewById(R.id.igproduct);
            tvproduct= itemView.findViewById(R.id.tvproduct);
        }
    }

}
