package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.dairyfarm.customer.Model.HomeSlider;
import com.dairyfarm.customer.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class HomeRecyclerViewSliderAdapter extends RecyclerView.Adapter<HomeRecyclerViewSliderAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<HomeSlider> listSlider;
    RecyclerView rVSliderOfferProduct;
    static BottomSheetDialog sliderOfferProductBottomSheetDialog;

   /* ArrayList<SliderOfferProduct> listSliderOfferProduct = new ArrayList<>();
    SliderOfferProductAdapter sliderOfferProductAdapter = new SliderOfferProductAdapter(listSliderOfferProduct);*/

    public HomeRecyclerViewSliderAdapter(ArrayList<HomeSlider> listSlider) {
        this.listSlider = listSlider;
    }

    public static void CloseSliderOfferProductsBottomSheet() {
        sliderOfferProductBottomSheetDialog.dismiss();
    }

    @Override
    public HomeRecyclerViewSliderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.home_slider_recyclerview_list_row_item_layout, parent, false);
        return new HomeRecyclerViewSliderAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final HomeRecyclerViewSliderAdapter.ViewHolder holder, final int position) {
        final HomeSlider slider = listSlider.get(position);

//        holder.tvCategoryName.setText(slider.getSliderDescription());
        holder.llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(slider.getImageURL()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_transparent).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.llProgressLayout.setVisibility(View.GONE);
                holder.iVSliderImage.setImageResource(R.drawable.app_icon_login);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                holder.llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.iVSliderImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slider.getSliderType().equalsIgnoreCase("video")) {
                    /*Intent mIntent = new Intent(mContext, ExoPlayerActivity.class);
                    mIntent.putExtra(KEY_VIDEO_URI, slider.getVideoURL());
                    mContext.startActivity(mIntent);*/
                } else if (slider.getSliderType().equalsIgnoreCase("image")) {

                } else if (slider.getSliderType().equalsIgnoreCase("Offer Slider")) {
                   /* offerIDManager.setOfferID(slider.getOfferID());
                    Intent mIntent = new Intent(mContext, ProductActivity.class);
                    mContext.startActivity(mIntent);*/
                } else {
                    //ShowSliderOfferBottomSheetDialog();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listSlider.size();
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
        ImageView iVSliderImage;
        TextView tvSliderTitle;
        LinearLayout llProgressLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            iVSliderImage = itemView.findViewById(R.id.iVSliderImage);
            tvSliderTitle = itemView.findViewById(R.id.tvSliderTitle);
            llProgressLayout = itemView.findViewById(R.id.llProgressLayout);
        }
    }


    private void ShowSliderOfferBottomSheetDialog() {
/*        setSliderProductOfferData();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.slider_offer_bottom_sheet_layout, null, false);
        sliderOfferProductBottomSheetDialog = new BottomSheetDialog(mContext);
        sliderOfferProductBottomSheetDialog.setContentView(mView);
        ImageView ivCloseBottomSheet = mView.findViewById(R.id.ivCloseBottomSheet);
        rVSliderOfferProduct = mView.findViewById(R.id.rVSliderOfferProduct);
*//*        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(mContext.getResources().getDrawable(R.drawable.line_divider));
        rVSliderOfferProduct.addItemDecoration(dividerItemDecoration);*//*
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rVSliderOfferProduct.setLayoutManager(linearLayoutManager);
        rVSliderOfferProduct.setAdapter(sliderOfferProductAdapter);

        ivCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderOfferProductBottomSheetDialog.dismiss();
            }
        });

        sliderOfferProductBottomSheetDialog.show();*/
    }

    private void setSliderProductOfferData() {
        /*listSliderOfferProduct.clear();
        listSliderOfferProduct.add(new SliderOfferProduct("1", "Potato", "₹15", "₹13", "Potatoes should be included in the diet of those having mouth ulcers. As they are easy to digest, they are good for patients. Consumption of potatoes helps to maintain the blood glucose level and keeps the brain alert and active. People who are diagnosed with kidney stones or heart disorders can include potatoes in their diet as they are light on the stomach.", "", "1 Kg", "10% Off"));
        sliderOfferProductAdapter.notifyDataSetChanged();*/
    }
}

