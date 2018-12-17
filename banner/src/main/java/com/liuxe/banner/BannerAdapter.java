package com.liuxe.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Liuxe on 2018/12/14 0014
 */
public class BannerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mList;
    private int radius;
    private ItemClickListener mItemClickListener;

    public BannerAdapter(Context context, List<String> list,int radius,ItemClickListener
            itemClickListener) {
        mContext = context;
        mList = list;
        this.radius = radius;
        mItemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return mList.size() == 1?1:Integer.MAX_VALUE;
    }

    private int getRealCount(){
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int realPosition;
        if (getRealCount() == 0){
            return null;
        } else {
            realPosition = position%getRealCount();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner,null);
        CardView cardView = view.findViewById(R.id.banner_card);
       //设置圆角角度
        cardView.setRadius(radius);
        ImageView ivBg = view.findViewById(R.id.banner_img);
        Glide.with(mContext).load(mList.get(realPosition)).into(ivBg);
        //view 的点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    mItemClickListener.onItemClick(realPosition);
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
