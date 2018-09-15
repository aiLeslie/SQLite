package com.leslie.codebase.litepal.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private GestureDetector mGestureDetector;
    private View childView;
    private RecyclerView touchView;

    public RecyclerItemClickListener(Context context, final RecyclerItemClickListener.OnItemClickListener mListener) {
        mContext = context;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent ev) {
                if (childView != null && mListener != null) {
//                        mListener.onItemClick(childView, touchView.getChildPosition(childView));
                    mListener.onItemClick(childView, touchView.getChildLayoutPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent ev) {
                if (childView != null && mListener != null) {
//                        mListener.onLongClick(childView, touchView.getChildPosition(childView));
                    mListener.onLongClick(childView, touchView.getChildLayoutPosition(childView));
                }
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, final int position);

        void onLongClick(View view, final int posotion);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        childView = rv.findChildViewUnder(e.getXPrecision(), e.getYPrecision());
        touchView = rv;
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Toast.makeText(mContext, "onTouchEvent: ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
