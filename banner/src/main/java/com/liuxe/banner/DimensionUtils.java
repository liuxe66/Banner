package com.liuxe.banner;

import android.content.Context;

/**
 * Created by Liuxe on 2018/12/14 0014
 */
public class DimensionUtils {
    /**
     * 将px值转换为dp值
     */
    public static int px2dp(Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dp值转换为px值
     */
    public static int dp2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
