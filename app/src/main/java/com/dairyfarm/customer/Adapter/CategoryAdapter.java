package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dairyfarm.customer.Activity.SubCategoryActivity;
import com.dairyfarm.customer.Model.Category;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<Category> listCategory;
    String isFromDashboard;
    AtClass atClass;

    public CategoryAdapter(ArrayList<Category> listCategory, String isFromDashboard) {
        this.listCategory = listCategory;
        this.isFromDashboard = isFromDashboard;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        atClass = new AtClass();
        View mView;
        if (isFromDashboard != null && !isFromDashboard.isEmpty() && !isFromDashboard.equals("")) {
            if (isFromDashboard.equals("1")) {
                mView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_row_item_layout, parent, false);
            } else if (isFromDashboard.equals("0")) {
                mView = LayoutInflater.from(mContext).inflate(R.layout.category_list_row_item_layout, parent, false);
            } else {
                mView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_row_item_layout, parent, false);
            }
        } else {
            mView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_row_item_layout, parent, false);
        }
        return new CategoryAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {
        final Category category = listCategory.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvCategoryName.setText(Html.fromHtml(category.getCategoryName(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvCategoryName.setText(Html.fromHtml(category.getCategoryName()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (atClass.isNetworkAvailable(mContext)) {
                    if (atClass.CheckEmptyString(mContext, category.getCategoryID(), "")
                            && atClass.CheckEmptyString(mContext, category.getCategoryName(), "")) {
                        Intent intent = new Intent(mContext, SubCategoryActivity.class);
                        intent.putExtra("CategoryID", category.getCategoryID());
                        intent.putExtra("CategoryName", category.getCategoryName());
                        //intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP|intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(category.getCategoryImage()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_login).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.llProgressLayout.setVisibility(View.GONE);
                holder.iVCategoryImage.setImageResource(R.drawable.app_icon_login);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                holder.llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.iVCategoryImage);

        /*if (category.getIsPublish() != null && !category.getIsPublish().equals("") && !category.getIsPublish().isEmpty()) {
            if (category.getIsPublish().equals("1")) {
                //Available
                holder.tvAvailableString.setVisibility(View.GONE);

            } else if (category.getIsPublish().equals("0")) {
                //Not Available
                if (category.getIsPublishString() != null && !category.getIsPublishString().equals("") && !category.getIsPublishString().isEmpty()) {
                    holder.tvAvailableString.setVisibility(View.GONE);
                    holder.llComingSoonLayout.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        holder.tvAvailableString.setText(Html.fromHtml(category.getIsPublishString(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        holder.tvAvailableString.setText(Html.fromHtml(category.getIsPublishString()));
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        holder.tvComingSoonMessage.setText(Html.fromHtml(category.getIsPublishString(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        holder.tvComingSoonMessage.setText(Html.fromHtml(category.getIsPublishString()));
                    }
                } else {
                    holder.tvAvailableString.setVisibility(View.GONE);
                    holder.llComingSoonLayout.setVisibility(View.GONE);
                }
            } else {
                holder.tvAvailableString.setVisibility(View.GONE);
                holder.llComingSoonLayout.setVisibility(View.GONE);
            }
        } else {
            holder.tvAvailableString.setVisibility(View.GONE);
            holder.llComingSoonLayout.setVisibility(View.GONE);
        }*/
        holder.tvAvailableString.setSelected(true);
        //holder.tvCategoryName.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
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
        ImageView iVCategoryImage;
        TextView tvCategoryName, tvAvailableString;
        LinearLayout llProgressLayout;

        FrameLayout llComingSoonLayout;
        TextView tvComingSoonMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            iVCategoryImage = itemView.findViewById(R.id.iVCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvAvailableString = itemView.findViewById(R.id.tvAvailableString);
            llProgressLayout = itemView.findViewById(R.id.llProgressLayout);


            llComingSoonLayout = itemView.findViewById(R.id.llComingSoonLayout);
            tvComingSoonMessage = itemView.findViewById(R.id.tvComingSoonMessage);

        }
    }
}
