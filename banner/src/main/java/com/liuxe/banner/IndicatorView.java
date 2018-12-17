package com.liuxe.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Liuxe on 2018/12/14 0014
 */
public class IndicatorView extends FrameLayout {
    private int posSize;
    private int posCur;
    private Context mContext;
    private LayoutInflater mInflater;

    private RelativeLayout mRlIndicator;
    private LinearLayout mllPointdefault;
    private ImageView mIvPointSelect;
    private View mView;

    public IndicatorView(Context context) {
        this(context,null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        init();
    }

    private void init() {
        mView = mInflater.inflate(R.layout.view_indicator,null);
        mRlIndicator = mView.findViewById(R.id.rl_indicator);
        mllPointdefault = mView.findViewById(R.id.ll_point_default);
        mIvPointSelect = mView.findViewById(R.id.iv_point_cur);
    }

    public void setPosSize(int posSize){
        this.posSize = posSize;
        for (int i = 0; i < posSize; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackground(mContext.getDrawable(R.drawable.shape_point_default));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DimensionUtils
                    .dp2px(mContext,4), DimensionUtils.dp2px(mContext,4));
            if (i == 0) {
                layoutParams.setMargins(DimensionUtils
                        .dp2px(mContext,4), 0, DimensionUtils
                        .dp2px(mContext,4), 0);
            } else {
                layoutParams.setMargins(0, 0, DimensionUtils
                        .dp2px(mContext,4), 0);
            }
            imageView.setLayoutParams(layoutParams);
            mllPointdefault.addView(imageView, i);
        }
        this.addView(mView);
    }

    public void setPosCurLeftMargin(int leftMargin){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvPointSelect.getLayoutParams();
        params.leftMargin = leftMargin;
        mIvPointSelect.setLayoutParams(params);
    }
}
