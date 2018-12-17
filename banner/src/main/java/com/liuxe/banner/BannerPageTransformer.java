package com.liuxe.banner;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Liuxe on 2018/9/29 0029
 */
public class BannerPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9f;
    @Override
    public void transformPage(View page, float position) {

        //    第一个页面position变化为[0,-1]
        //    第二个页面position变化为[1,0]

        if (position < -1) {
            page.setScaleY(MIN_SCALE);
            page.setScaleX(MIN_SCALE);
        } else if (position <= 1) {
            float scale=Math.max(MIN_SCALE,1-Math.abs(position));
            page.setScaleY(scale);
            page.setScaleX(scale);
        } else {
            page.setScaleY(MIN_SCALE);
            page.setScaleX(MIN_SCALE);
        }
    }
}
