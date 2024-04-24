package com.dairyfarm.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dairyfarm.customer.APIHelper.CommonRequestParams;
import com.dairyfarm.customer.APIHelper.JsonFields;
import com.dairyfarm.customer.APIHelper.WebAuthorization;
import com.dairyfarm.customer.APIHelper.WebURL;
import com.dairyfarm.customer.Activity.LogoutActivity;
import com.dairyfarm.customer.Activity.MyCartActivity;
import com.dairyfarm.customer.Activity.ProductActivity;
import com.dairyfarm.customer.Model.ConfirmOder;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.dairyfarm.customer.Utils.UserConfirmationAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<ConfirmOder> listConfirmOrder;
    int confirmOrderCount;

    ProgressDialogHandler progressDialogHandler1, progressDialogHandler2;
    CustomerSessionManager customerSessionManager;
    AtClass atClass;

    public ConfirmOrderAdapter(ArrayList<ConfirmOder> listConfirmOrder) {
        this.listConfirmOrder = listConfirmOrder;
    }

    @Override
    public ConfirmOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.confirm_order_list_row_item_layout, parent, false);
        progressDialogHandler1 = new ProgressDialogHandler(mContext);
        progressDialogHandler2 = new ProgressDialogHandler(mContext);
        customerSessionManager = new CustomerSessionManager(mContext);
        atClass = new AtClass();
        return new ConfirmOrderAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ConfirmOrderAdapter.ViewHolder holder, final int position) {
        final ConfirmOder confirmOrder = listConfirmOrder.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductTitle.setText(Html.fromHtml(confirmOrder.getProductTitle(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductTitle.setText(Html.fromHtml(confirmOrder.getProductTitle()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductSellingPrice.setText(Html.fromHtml(confirmOrder.getTotalSellingPrice(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductSellingPrice.setText(Html.fromHtml(confirmOrder.getTotalSellingPrice()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductMarketPrice.setText(Html.fromHtml(confirmOrder.getTotalMarketPrice(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductMarketPrice.setText(Html.fromHtml(confirmOrder.getTotalMarketPrice()));
        }

        holder.tvProductMarketPrice.setPaintFlags(holder.tvProductMarketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductSavingsInPercentage.setText(Html.fromHtml(confirmOrder.getTotalSavingPercentage(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductSavingsInPercentage.setText(Html.fromHtml(confirmOrder.getTotalSavingPercentage()));
        }

        holder.llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(confirmOrder.getProductImagePath()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_login).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.llProgressLayout.setVisibility(View.GONE);
                holder.ivProductImage.setImageResource(R.drawable.app_icon_login);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                holder.llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.ivProductImage);


        holder.tvconfirmOrderQuantityCount.setText(confirmOrder.getProductQty());
        //holder.tvPrice.setText(confirmOrder.getTotalProductPrice());

        holder.iVRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRemoveFromCartConfirmationAlert(confirmOrder.getCartID());
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeCart(confirmOrder.getCartID(), "1", confirmOrder.getOfferID());
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmOrder.getProductQty().equals("1")) {
                    ShowRemoveFromCartConfirmationAlert(confirmOrder.getCartID());
                } else {
                    ChangeCart(confirmOrder.getCartID(), "0", confirmOrder.getOfferID());
                }
            }
        });
    }

    private void ShowRemoveFromCartConfirmationAlert(final String cartID) {
        final UserConfirmationAlertDialog userConfirmationAlertDialog = new UserConfirmationAlertDialog(mContext);
        userConfirmationAlertDialog.setTitle(mContext.getString(R.string.cart_item_remove_confirmation_title_text));
        //userConfirmationAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        userConfirmationAlertDialog.setMessage(mContext.getString(R.string.cart_item_remove_confirmation_description_text));
        userConfirmationAlertDialog.setPositiveButton(mContext.getString(R.string.cart_item_remove_confirmation_positive_button_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userConfirmationAlertDialog.dismiss();
                //Do want you want
                if (atClass.isNetworkAvailable(mContext)) {
                    RemoveItemFromCart(cartID);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

        userConfirmationAlertDialog.setNegativeButton(mContext.getString(R.string.cart_item_remove_confirmation_negative_button_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userConfirmationAlertDialog.dismiss();

            }
        });
        userConfirmationAlertDialog.setCancelable(false);
        userConfirmationAlertDialog.setCanceledOnTouchOutside(false);
        userConfirmationAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userConfirmationAlertDialog.show();
        userConfirmationAlertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void RemoveItemFromCart(final String CartID) {
        progressDialogHandler1.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.REMOVE_CART_ITEM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseRemoveFromCartJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAuthorization.checkAuthentication();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                CommonRequestParams commonRequestParams = new CommonRequestParams();
                Map<String, String> params = commonRequestParams.getCommonRequestParams(mContext);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.REMOVE_CART_REQUEST_PARAMS_CART_ID, CartID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void parseRemoveFromCartJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MyCartActivity) mContext).getCartDetails();
                    }
                }, 100);
            } else if (flag == 3) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                String LogoutTitle = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_TITLE);
                String LogoutMessage = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_MESSAGE);
                String LogoutIcon = jsonObject.optString(JsonFields.COMMON_LOGOUT_RESPONSE_ICON);

                Intent i = new Intent(mContext, LogoutActivity.class);
                i.putExtra("Title", LogoutTitle);
                i.putExtra("Message", LogoutMessage);
                i.putExtra("ImageURL", LogoutIcon);
                mContext.startActivity(i);
                ((ProductActivity) mContext).finish();
            } else {
                progressDialogHandler1.showPopupProgressSpinner(false);
                Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ChangeCart(final String cartID, final String status, final String offerID) {
        progressDialogHandler2.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CHANGE_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseChangeCartJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                if (error instanceof NetworkError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_network_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_no_connection_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_server_error), Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_timeout_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_volley_error), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAuthorization.checkAuthentication();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                CommonRequestParams commonRequestParams = new CommonRequestParams();
                Map<String, String> params = commonRequestParams.getCommonRequestParams(mContext);
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerSessionManager.getCustomerID());
                params.put(JsonFields.CHANGE_CART_REQUEST_PARAMS_OFFER_ID, offerID);
                params.put(JsonFields.CHANGE_CART_REQUEST_PARAMS_CART_ID, cartID);
                params.put(JsonFields.CHANGE_CART_REQUEST_PARAMS_STATUS, status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void parseChangeCartJSON(String response) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MyCartActivity) mContext).getCartDetails();
                    }
                }, 100);
            } else {
                progressDialogHandler2.showPopupProgressSpinner(false);
                Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listConfirmOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductTitle, tvProductMarketPrice, tvProductSellingPrice, tvProductSavingsInPercentage, tvconfirmOrderQuantityCount, tvPrice;
        ImageView ivProductImage;
        Button btnMinus, btnPlus;
        ImageView iVRemoveFromCart;
        LinearLayout llProgressLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle);
            tvProductMarketPrice = itemView.findViewById(R.id.tvProductMarketPrice);
            tvProductSellingPrice = itemView.findViewById(R.id.tvProductSellingPrice);
            tvProductSavingsInPercentage = itemView.findViewById(R.id.tvProductSavingsInPercentage);

            tvconfirmOrderQuantityCount = itemView.findViewById(R.id.tvconfirmOrderQuantityCount);
            //tvPrice =itemView.findViewById(R.id.tvPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            iVRemoveFromCart = itemView.findViewById(R.id.iVRemoveFromCart);
            llProgressLayout = itemView.findViewById(R.id.llProgressLayout);
        }
    }
}

