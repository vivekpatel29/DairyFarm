package com.dairyfarm.customer.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.LinearLayout;

import com.dairyfarm.customer.R;

//import com.wang.avi.AVLoadingIndicatorView;

public class ProgressDialogHandler {
    Context mContext;
    private Dialog progressDialog = null;

    LinearLayout llProgress;

    public ProgressDialogHandler(Context context) {
        this.mContext = context;
    }

    public void showPopupProgressSpinner(Boolean isShowing) {
        if (isShowing == true) {
            progressDialog = new Dialog(mContext);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.popup_progressbar);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();

            /*avi = (AVLoadingIndicatorView) progressDialog.findViewById(R.id.avi);
            avi.setIndicator("com.kemchhofresh.user.Utils.MyCustomIndicator");
            avi.show();*/

            progressDialog.show();

        } else if (isShowing == false) {
            if ((this.progressDialog != null) && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        }
    }
}
