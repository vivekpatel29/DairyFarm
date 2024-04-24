package com.dairyfarm.customer.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.dairyfarm.customer.R;

public class CustomDividerDecorator extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public CustomDividerDecorator(Context context, String Status) {
        if (Status.equals("1")) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);

        } else if (Status.equals("2")) {
            mDivider = context.getResources().getDrawable(R.drawable.divider_grey);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}