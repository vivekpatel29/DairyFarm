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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.dairyfarm.customer.Activity.LoginActivity;
import com.dairyfarm.customer.Activity.LogoutActivity;
import com.dairyfarm.customer.Activity.MyCartActivity;
import com.dairyfarm.customer.Activity.ProductActivity;
import com.dairyfarm.customer.Activity.ProductDetailsActivity;
import com.dairyfarm.customer.Model.ProductVariance;
import com.dairyfarm.customer.Model.Products;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.CustomerSessionManager;
import com.dairyfarm.customer.Utils.ProductsLayoutManager;
import com.dairyfarm.customer.Utils.ProgressDialogHandler;
import com.dairyfarm.customer.Utils.UserConfirmationAlertDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ProductsAdapter  extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<Products> listProduct;

    ProductsLayoutManager productsLayoutManager;

    ArrayList<ProductVariance> listProductVariance = new ArrayList<>();
    ProductVarianceAdapter productVarianceAdapter;

    static TextView tvProductVarianceMarketPrice, tvProductVarianceSellingPrice;

    static LinearLayout llAddToCartSelected, llAddToCartNotSelected, llNotifyMe;

    RecyclerView rvVariance;

    public ProductsAdapter(ArrayList<Products> listProduct) {
        this.listProduct = listProduct;
    }

    AtClass atClass;

    static String ProductAttributeID, AttributeCartID, AttributeCartQty;

    CustomerSessionManager customerSessionManager;

    ProgressDialogHandler progressDialogHandler1, progressDialogHandler2;

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        productsLayoutManager = new ProductsLayoutManager(mContext);
        atClass = new AtClass();
        customerSessionManager = new CustomerSessionManager(mContext);
        progressDialogHandler1 = new ProgressDialogHandler(mContext);
        progressDialogHandler2 = new ProgressDialogHandler(mContext);
        View mView;
        if (productsLayoutManager.getProductLayout() != null && !productsLayoutManager.getProductLayout().isEmpty() && !productsLayoutManager.getProductLayout().equals("")) {
            if (productsLayoutManager.getProductLayout().equals("1")) {
                mView = LayoutInflater.from(mContext).inflate(R.layout.product_list_row_item_layout, parent, false);
            } else if (productsLayoutManager.getProductLayout().equals("2")) {
                mView = LayoutInflater.from(mContext).inflate(R.layout.product_grid_row_item_layout, parent, false);
            } else {
                mView = LayoutInflater.from(mContext).inflate(R.layout.product_list_row_item_layout, parent, false);
            }
        } else {
            mView = LayoutInflater.from(mContext).inflate(R.layout.product_list_row_item_layout, parent, false);
        }
        return new ProductsAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.ViewHolder holder, final int position) {
        final Products products = listProduct.get(position);


        holder.tvProductName.setSelected(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductName.setText(Html.fromHtml(products.getProductName(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductName.setText(Html.fromHtml(products.getProductName()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductSellingPrice.setText(Html.fromHtml(products.getSellingPrice(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductSellingPrice.setText(Html.fromHtml(products.getSellingPrice()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductMarketPrice.setText(Html.fromHtml(products.getMarketPrice(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductMarketPrice.setText(Html.fromHtml(products.getMarketPrice()));
        }
        holder.tvProductMarketPrice.setPaintFlags(holder.tvProductMarketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvProductSavingsInPercentage.setText(Html.fromHtml(products.getSavingPercentage(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvProductSavingsInPercentage.setText(Html.fromHtml(products.getSavingPercentage()));
        }

        holder.llProgressLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(products.getImagePath()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.app_icon_login).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.llProgressLayout.setVisibility(View.GONE);
                holder.iVProductImage.setImageResource(R.drawable.app_icon_login);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //if you want to convert the drawable to ImageView
                holder.llProgressLayout.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.iVProductImage);


        /*if (products.getHasOffer().equals("1")) {
            holder.onOfferShimmerContainer.setVisibility(View.VISIBLE);
        } else {
            holder.onOfferShimmerContainer.setVisibility(View.GONE);
        }*/


        if (products.getHasOffer().equals("1")) {
            holder.onOfferShimmerContainer.setVisibility(View.GONE);
            if (products.getOfferString() != null && !products.getOfferString().equals("") && !products.getOfferString().isEmpty()) {
                holder.tvOfferString.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tvOfferString.setText(Html.fromHtml(products.getOfferString(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tvOfferString.setText(Html.fromHtml(products.getOfferString()));
                }
            } else {
                holder.tvOfferString.setVisibility(View.GONE);
            }
        } else {
            holder.tvOfferString.setVisibility(View.GONE);
            holder.onOfferShimmerContainer.setVisibility(View.GONE);
        }

        if (products.getIsFeatured().equals("1")) {
            if (products.getFeaturedString() != null && !products.getFeaturedString().equals("") && !products.getFeaturedString().isEmpty()) {
                holder.tvFeaturedString.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tvFeaturedString.setText(Html.fromHtml(products.getFeaturedString(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tvFeaturedString.setText(Html.fromHtml(products.getFeaturedString()));
                }
            } else {
                holder.tvFeaturedString.setVisibility(View.GONE);
            }
        } else {
            holder.tvFeaturedString.setVisibility(View.GONE);
        }

        if (products.getAddToCart().equalsIgnoreCase("1")) {
            if (!products.getCartID().equalsIgnoreCase("0")) {
                holder.flAddToCart.setVisibility(View.GONE);
                holder.flCartQty.setVisibility(View.VISIBLE);
                holder.flDontAddToCart.setVisibility(View.GONE);
                holder.llComingSoonLayout.setVisibility(View.GONE);
                Log.d("Called", "Yes");
                Log.d("Product Name", products.getProductName());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tvProductQuantityCount.setText(Html.fromHtml(products.getCartTotalQty(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tvProductQuantityCount.setText(Html.fromHtml(products.getCartTotalQty()));
                }
            } else {
                if (products.getHasAttributes().equalsIgnoreCase("1")) {
                    holder.flAddToCart.setVisibility(View.VISIBLE);
                    holder.tvCustomizable.setVisibility(View.GONE);
                    holder.flDontAddToCart.setVisibility(View.GONE);
                    holder.llComingSoonLayout.setVisibility(View.GONE);
                    holder.flCartQty.setVisibility(View.GONE);
                } else {
                    holder.flAddToCart.setVisibility(View.VISIBLE);
                    holder.tvCustomizable.setVisibility(View.GONE);
                    holder.flDontAddToCart.setVisibility(View.GONE);
                    holder.llComingSoonLayout.setVisibility(View.GONE);
                    holder.flCartQty.setVisibility(View.GONE);
                }
            }
        } else {
            holder.flAddToCart.setVisibility(View.GONE);
            holder.flCartQty.setVisibility(View.GONE);
            holder.flDontAddToCart.setVisibility(View.VISIBLE);
            holder.llComingSoonLayout.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvCartString.setText(Html.fromHtml(products.getCartString(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvCartString.setText(Html.fromHtml(products.getCartString()));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tvComingSoonMessage.setText(Html.fromHtml(products.getCartString(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tvComingSoonMessage.setText(Html.fromHtml(products.getCartString()));
            }
        }

        holder.cVProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductDetailsActivity.class);
                i.putExtra("ProductID", products.getProductID());
                //i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP|i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });

        holder.tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerSessionManager.getCustomerID().equals("0")) {
                    //Customer Not Logged In
                    Intent i = new Intent(mContext, LoginActivity.class);
                    i.putExtra("isFrom", "2");
                    i.putExtra("OfferID", products.getOfferID());
                    i.putExtra("FromScreenName", "ProductAdapter");
                    i.putExtra("ToScreenName", "ProductActivity");
                    mContext.startActivity(i);
                } else {
                    if (products.getHasAttributes().equalsIgnoreCase("1")) {
                        ArrayList<ProductVariance> lstPrVr = products.getListProductVariance();
                        Log.d("List Size", String.valueOf(lstPrVr.size()));
                        if (lstPrVr != null && !lstPrVr.isEmpty() &&
                                products.getProductAttributeID() != null && !products.getProductAttributeID().isEmpty() && !products.getProductAttributeID().equals("")) {
                            OpenProductVarianceBottomSheet(products.getProductID(), products.getProductName(), products.getProductAttributeID(), products.getListProductVariance(), products.getOfferID(), holder, position);
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AddItemToCart(customerSessionManager.getCustomerID(), products.getProductID(), "", "", products.getOfferID(), position);
                    }
                }
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.getHasAttributes().equals("1")) {
                    ArrayList<ProductVariance> lstPrVr = products.getListProductVariance();
                    Log.d("List Size", String.valueOf(lstPrVr.size()));
                    if (lstPrVr != null && !lstPrVr.isEmpty() && !lstPrVr.equals("")) {
                        Log.d("CartID", "Last " + products.getLastCartID());
                        Log.d("CartName", "Last " + products.getLastCartName());
                        OpenChoosingOptionForAddToCartBottomSheet(products.getProductID(), products.getProductName(), products.getProductAttributeID(), products.getLastCartID(), products.getLastCartName(), products.getListProductVariance(), products.getOfferID(), holder, position);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (atClass.isNetworkAvailable(mContext)) {
                        ChangeCart(products.getCartID(), "1", position, products.getOfferID());
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.getHasAttributes().equals("1")) {
                    MultipleCustomizationAlert();
                } else {
                    if (products.getCartQty().equals("1")) {
                        if (atClass.isNetworkAvailable(mContext)) {
                            ShowAlertDeleteAlert(products.getCartID(), position, products.getOfferID());
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (atClass.isNetworkAvailable(mContext)) {
                            ChangeCart(products.getCartID(), "0", position, products.getOfferID());
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void MultipleCustomizationAlert() {
        final UserConfirmationAlertDialog userConfirmationAlertDialog = new UserConfirmationAlertDialog(mContext);
        userConfirmationAlertDialog.setTitle(mContext.getString(R.string.multiple_customization_user_confirmation_title_text));
        //userConfirmationAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        userConfirmationAlertDialog.setMessage(mContext.getString(R.string.multiple_customization_user_confirmation_description_text));
        userConfirmationAlertDialog.setPositiveButton(mContext.getString(R.string.multiple_customization_user_confirmation_positive_button_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userConfirmationAlertDialog.dismiss();
                //Do want you want
                //Do want you want
                if (atClass.isNetworkAvailable(mContext)) {
                    Intent i = new Intent(mContext, MyCartActivity.class);
                    mContext.startActivity(i);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        userConfirmationAlertDialog.setNegativeButton(mContext.getString(R.string.multiple_customization_user_confirmation_negative_button_text), new View.OnClickListener() {
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

    private void ShowAlertDeleteAlert(final String CartID, int pos, String offerID) {
        final UserConfirmationAlertDialog userConfirmationAlertDialog = new UserConfirmationAlertDialog(mContext);
        userConfirmationAlertDialog.setTitle(mContext.getString(R.string.remove_product_from_cart_confirmation_title_text));
        //userConfirmationAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        userConfirmationAlertDialog.setMessage(mContext.getString(R.string.remove_product_from_cart_confirmation_description_text));
        userConfirmationAlertDialog.setPositiveButton(mContext.getString(R.string.remove_product_from_cart_confirmation_positive_button_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do want you want
                userConfirmationAlertDialog.dismiss();
                //Do want you want
                if (atClass.isNetworkAvailable(mContext)) {
                    ChangeCart(CartID, "0", pos, offerID);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        userConfirmationAlertDialog.setNegativeButton(mContext.getString(R.string.remove_product_from_cart_confirmation_negative_button_text), new View.OnClickListener() {
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

    private void OpenChoosingOptionForAddToCartBottomSheet(final String ProductID, final String ProductName, final String ProductAtrID, final String LastCartID, final String LastCartProductName, final ArrayList<ProductVariance> listProAttr, String OfferID, final ViewHolder holder, final int pos) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.product_choose_option_add_to_cart_bottom_sheet_layout, null, false);
        final BottomSheetDialog productAddToCartOptionBottomSheet = new BottomSheetDialog(mContext);
        productAddToCartOptionBottomSheet.setContentView(mView);

        final ImageView ivCloseBottomSheet = mView.findViewById(R.id.ivCloseBottomSheet);

        ivCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAddToCartOptionBottomSheet.dismiss();
            }
        });

        TextView tvAddToCartOptionProductNameTitle = mView.findViewById(R.id.tvAddToCartOptionProductNameTitle);
        TextView tvLastSelection = mView.findViewById(R.id.tvLastSelection);

        tvAddToCartOptionProductNameTitle.setSelected(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvAddToCartOptionProductNameTitle.setText(Html.fromHtml(ProductName, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvAddToCartOptionProductNameTitle.setText(Html.fromHtml(ProductName));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvLastSelection.setText(Html.fromHtml(LastCartProductName, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvLastSelection.setText(Html.fromHtml(LastCartProductName));
        }

        TextView tvIWillChoose = mView.findViewById(R.id.tvIWillChoose);
        TextView tvRepeat = mView.findViewById(R.id.tvRepeat);

        tvIWillChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAddToCartOptionBottomSheet.dismiss();
                OpenProductVarianceBottomSheet(ProductID, ProductName, ProductAtrID, listProAttr, OfferID, holder, pos);
                Log.d("ProductAttributeId", String.valueOf(listProAttr.size()));
            }
        });

        tvRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (atClass.isNetworkAvailable(mContext)) {
                    productAddToCartOptionBottomSheet.dismiss();
                    ChangeCart(LastCartID, "1", pos, OfferID);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        productAddToCartOptionBottomSheet.show();
    }

    public void OpenProductVarianceBottomSheet(final String ProductID, String ProductName, final String ProAttrID, final ArrayList<ProductVariance> listProductAttribute, final String OfferID, final ViewHolder holder, final int pos) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.product_variance_bottom_sheet_layout, null, false);
        final BottomSheetDialog productVarianceBottomSheetDialog = new BottomSheetDialog(mContext);
        productVarianceBottomSheetDialog.setContentView(mView);

        final ImageView ivCloseBottomSheet = mView.findViewById(R.id.ivCloseBottomSheet);


        ivCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productVarianceBottomSheetDialog.dismiss();
            }
        });

        final TextView tvProductName = mView.findViewById(R.id.tvProductName);
        tvProductName.setSelected(true);

        rvVariance = mView.findViewById(R.id.rvVariance);

        llNotifyMe = mView.findViewById(R.id.llNotifyMe);
        llAddToCartSelected = mView.findViewById(R.id.llAddToCartSelected);
        llAddToCartNotSelected = mView.findViewById(R.id.llAddToCartNotSelected);

        llAddToCartSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productVarianceBottomSheetDialog.dismiss();
                if (atClass.isNetworkAvailable(mContext)) {
                    if (AttributeCartQty.equals("0")) {
                        //Add To Cart
                        if (customerSessionManager.getCustomerID() != null && !customerSessionManager.getCustomerID().isEmpty() && !customerSessionManager.getCustomerID().equals("")) {
                            if (customerSessionManager.getCustomerID().equals("0")) {
                                //Customer Not Logged In
                                Intent i = new Intent(mContext, LoginActivity.class);
                                i.putExtra("isFrom", "2");
                                i.putExtra("FromScreenName", "ProductAdapter");
                                i.putExtra("ToScreenName", "ProductActivity");
                                mContext.startActivity(i);
                            } else {
                                if (ProductAttributeID != null && !ProductAttributeID.isEmpty() && !ProductAttributeID.equals("") &&
                                        ProductID != null && !ProductID.isEmpty() && !ProductID.equals(""))
                                    /*&& OfferID != null && !OfferID.isEmpty() && !OfferID.equals("")*/ {
                                    if (atClass.isNetworkAvailable(mContext)) {
                                        Log.d("ProductAttributeID", ProductAttributeID);
                                        AddItemToCart(customerSessionManager.getCustomerID(), ProductID, "1", ProductAttributeID, OfferID, pos);
                                    } else {
                                        Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            //Customer Not Logged In
                            Intent i = new Intent(mContext, LoginActivity.class);
                            i.putExtra("isFrom", "2");
                            i.putExtra("FromScreenName", "ProductAdapter");
                            i.putExtra("ToScreenName", "ProductActivity");
                            mContext.startActivity(i);
                        }
                    } else {
                        //Change Cart
                        if (atClass.isNetworkAvailable(mContext)) {
                            ChangeCart(AttributeCartID, "1", pos, OfferID);
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.common_no_internet), Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvProductVarianceMarketPrice = mView.findViewById(R.id.tvProductVarianceMarketPrice);
        tvProductVarianceSellingPrice = mView.findViewById(R.id.tvProductVarianceSellingPrice);

        SetUpDataOfProductVariance(listProductAttribute, ProAttrID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvProductName.setText(Html.fromHtml(ProductName, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvProductName.setText(Html.fromHtml(ProductName));
        }

        productVarianceBottomSheetDialog.show();
    }

    private void ChangeCart(final String cartID, final String status, int pos, String offerID) {
        progressDialogHandler1.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.CHANGE_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseChangeCartJSON(response, pos);
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
                params.put(JsonFields.CHANGE_CART_REQUEST_PARAMS_CART_ID, cartID);
                params.put(JsonFields.CHANGE_CART_REQUEST_PARAMS_OFFER_ID, offerID);
                params.put(JsonFields.CHANGE_CART_REQUEST_PARAMS_STATUS, status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void parseChangeCartJSON(String response, int pos) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler1.showPopupProgressSpinner(false);
                ((ProductActivity) mContext).setCurrentPageValue();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ProductActivity) mContext).getProductsDataFromAdapter(response, pos);
                    }
                }, 500);
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


    private void AddItemToCart(final String customerID, final String productID, final String hasAttribute, final String productAttributeID, String offerID, int pos) {
        progressDialogHandler2.showPopupProgressSpinner(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.ADD_PRODUCT_TO_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response, pos);
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
                params.put(JsonFields.COMMON_REQUEST_PARAM_CUSTOMER_ID, customerID);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_PRODUCT_ID, productID);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_OFFER_ID, offerID);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_HAS_ATTRIBUTES, hasAttribute);
                params.put(JsonFields.ADD_TO_CART_REQUEST_PARAMS_PRODUCT_ATTRIBUTE_ID, productAttributeID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String response, int pos) {
        Log.d("RESPONSE", response.toString());
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonFields.FLAG);
            String Message = jsonObject.optString(JsonFields.MESSAGE);

            if (flag == 1) {
                progressDialogHandler2.showPopupProgressSpinner(false);
                ((ProductActivity) mContext).setCurrentPageValue();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        ((ProductActivity) mContext).getProductsDataFromAdapter(response, pos);
                    }
                }, 500);
            } else if (flag == 3) {
                progressDialogHandler2.showPopupProgressSpinner(false);
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
                progressDialogHandler2.showPopupProgressSpinner(false);
                Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SetUpDataOfProductVariance(ArrayList<ProductVariance> listPrVar, String ProductAttributeID) {
        listProductVariance = listPrVar;
        //Log.d("ProductVarinace",String.valueOf(listPrVar.size()));
        //Log.d("AttributeID","AI"+ProductAttributeID);
        if (listProductVariance != null && !listProductVariance.isEmpty() && !listProductVariance.equals("") &&
                ProductAttributeID != null && !ProductAttributeID.isEmpty() && !ProductAttributeID.equals("")) {
            productVarianceAdapter = new ProductVarianceAdapter(listProductVariance, ProductAttributeID);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            rvVariance.setLayoutManager(linearLayoutManager);
            rvVariance.setAdapter(productVarianceAdapter);

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.common_something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    public static void setButton(String isProductAvailable) {
        if (isProductAvailable.equalsIgnoreCase("1")) {
            llAddToCartSelected.setVisibility(View.VISIBLE);
            llAddToCartNotSelected.setVisibility(View.GONE);
            llNotifyMe.setVisibility(View.GONE);
        } else {
            llNotifyMe.setVisibility(View.GONE);
            llAddToCartSelected.setVisibility(View.GONE);
            llAddToCartNotSelected.setVisibility(View.VISIBLE);
        }
    }

    public static void setData(String AttributeID, String ProductVarianceSellingPrice, String ProductVarianceMarketPrice, String CartID, String CartQty) {
        ProductAttributeID = AttributeID;
        Log.d("ProductAttributeID", "Value" + ProductAttributeID);
        AttributeCartID = CartID;
        Log.d("ProductAttributeCartID", "Value" + AttributeCartID);
        AttributeCartQty = CartQty;
        Log.d("ProductAttributeCartQty", "Value" + AttributeCartQty);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvProductVarianceMarketPrice.setText(Html.fromHtml(ProductVarianceMarketPrice, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvProductVarianceMarketPrice.setText(Html.fromHtml(ProductVarianceMarketPrice));
        }
        tvProductVarianceMarketPrice.setPaintFlags(tvProductVarianceMarketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvProductVarianceSellingPrice.setText(Html.fromHtml(ProductVarianceSellingPrice, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvProductVarianceSellingPrice.setText(Html.fromHtml(ProductVarianceSellingPrice));
        }
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
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
        TextView tvProductName, tvProductSellingPrice, tvProductMarketPrice;
        ImageView iVProductImage;
        CardView cVProduct;
        FrameLayout flAddToCart, flCartQty, flNotifyMe, flDontAddToCart;
        TextView tvAddToCart;
        TextView tvCustomizable;
        Button btnMinus, btnPlus;
        TextView tvProductQuantityCount, tvProductSavingsInPercentage, tvCartString;
        ShimmerFrameLayout onOfferShimmerContainer;
        LinearLayout llProgressLayout;
        FrameLayout llComingSoonLayout;
        TextView tvComingSoonMessage;
        TextView tvOfferString, tvFeaturedString;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductSellingPrice = (TextView) itemView.findViewById(R.id.tvProductSellingPrice);
            tvProductMarketPrice = (TextView) itemView.findViewById(R.id.tvProductMarketPrice);

            iVProductImage = (ImageView) itemView.findViewById(R.id.iVProductImage);
            cVProduct = (CardView) itemView.findViewById(R.id.cVProduct);
            flAddToCart = (FrameLayout) itemView.findViewById(R.id.flAddToCart);
            flCartQty = (FrameLayout) itemView.findViewById(R.id.flCartQty);
            flDontAddToCart = (FrameLayout) itemView.findViewById(R.id.flDontAddToCart);
            flNotifyMe = (FrameLayout) itemView.findViewById(R.id.flNotifyMe);
            tvAddToCart = (TextView) itemView.findViewById(R.id.tvAddToCart);
            tvCustomizable = (TextView) itemView.findViewById(R.id.tvCustomizable);
            tvCartString = (TextView) itemView.findViewById(R.id.tvCartString);
            btnMinus = (Button) itemView.findViewById(R.id.btnMinus);
            btnPlus = (Button) itemView.findViewById(R.id.btnPlus);
            tvProductQuantityCount = (TextView) itemView.findViewById(R.id.tvProductQuantityCount);
            tvProductSavingsInPercentage = (TextView) itemView.findViewById(R.id.tvProductSavingsInPercentage);
            onOfferShimmerContainer = itemView.findViewById(R.id.onOfferShimmerContainer);
            llProgressLayout = itemView.findViewById(R.id.llProgressLayout);

            llComingSoonLayout = itemView.findViewById(R.id.llComingSoonLayout);
            tvComingSoonMessage = itemView.findViewById(R.id.tvComingSoonMessage);

            tvOfferString = itemView.findViewById(R.id.tvOfferString);
            tvFeaturedString = itemView.findViewById(R.id.tvFeaturedString);


        }
    }
}
