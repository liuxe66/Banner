package com.liuxe.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Liuxe on 2018/12/13 0013
 */
public class Banner extends RelativeLayout {
    //轮播图片左边距
    private int bannerLeftMargin = 0;
    //轮播图片右边距
    private int bannerRightMargin = 0;
    //轮播图片圆角角度
    private int bannerRadius = 0;
    //轮播图之间的距离
    private int bannerMargin = 0;
    //是否显示指示器
    private boolean isShowIndicator;
    //点击回调监听
    private ItemClickListener mItemClickListener;
    //指示器view
    private IndicatorView mIndicatorView;
    //指示器位置
    private int indicator_position = BannerConfig.INDICATOR_CENTER;
    //当前页面索引
    private int curPageIndex;
    //是否定时播放
    private boolean isAutoPlay;
    //自动播放时间间隔 单位毫秒 6000
    private int playDuration = 6000;
    //滑动切换动画时间 单位毫秒 6000
    private int scrollDuration = 1200;
    //是否启用viewpager滑动动画
    private boolean isUsePageTransformer;
    //布局加载器
    private LayoutInflater mInflater;
    private LinearLayout mLlBanner;
    private ViewPager mVpBanner;
    private BannerAdapter mBannerAdapter;
    private List<String> mImgList = new ArrayList<>();
    private Context mContext;
   private Handler mHandler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case 200:
                   mVpBanner.setCurrentItem(mVpBanner.getCurrentItem() + 1);
                   mHandler.sendEmptyMessageDelayed(200, playDuration);
                   break;
           }
       }
   };

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        //获取属性
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.Banner);
        bannerLeftMargin = ta.getDimensionPixelSize(R.styleable.Banner_bannerLeftMargin,0);
        bannerRightMargin = ta.getDimensionPixelSize(R.styleable.Banner_bannerRightMargin,0);
        bannerRadius = ta.getDimensionPixelSize(R.styleable.Banner_bannerRadius,0);
        bannerMargin = ta.getDimensionPixelSize(R.styleable.Banner_bannerMargin,10);
        isAutoPlay = ta.getBoolean(R.styleable.Banner_isAutoPlay,false);
        isShowIndicator = ta.getBoolean(R.styleable.Banner_isShowIndicator,false);
        isUsePageTransformer = ta.getBoolean(R.styleable.Banner_isUsePageTransformer,false);
        playDuration = ta.getInteger(R.styleable.Banner_playDuration,6000);
        scrollDuration = ta.getInteger(R.styleable.Banner_scrollDuration,1200);
        //初始设置
        init();
    }

    private void init(){
        View banner = mInflater.inflate(R.layout.view_banner,null);
        mLlBanner = banner.findViewById(R.id.banner_ll);
        mVpBanner = banner.findViewById(R.id.banner_vp);
    }

    /**
     * 当view配置好之后 执行（必须在最后执行的方法）
     */
    public void start(){
        this.removeAllViews();
        //先移除当前布局，在添加
        mLlBanner.removeViewAt(0);
        //viewpager的左右边距
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) mVpBanner.getLayoutParams();
        params1.leftMargin=bannerLeftMargin;
        params1.rightMargin=bannerRightMargin;

        //设置之后 viewpager可以扩展到所在的父布局上
        mLlBanner.setClipChildren(false);
        mVpBanner.setClipChildren(false);
        //设置viewpager 滑动动画时长
        ViewPagerScroller scroller = new ViewPagerScroller(mContext);
        scroller.setScrollDuration(scrollDuration);//时间越长，速度越慢。
        scroller.initViewPagerScroll(mVpBanner);

        //实例化adapter 传入上下文，图片源，圆角角度，点击监听事件
        mBannerAdapter = new BannerAdapter(mContext,mImgList,bannerRadius,mItemClickListener);
        mVpBanner.setAdapter(mBannerAdapter);
        //添加页面切换动画
        if (isUsePageTransformer){
            BannerPageTransformer bannerPageTransformer = new BannerPageTransformer();
            mVpBanner.setPageTransformer(true,bannerPageTransformer);
        }
        //viewpager的一些设置
        mVpBanner.setPageMargin(bannerMargin);
        mVpBanner.setCurrentItem(getStartSelectItem());
        mVpBanner.setPageMargin(bannerMargin);//图片之间的边距
        mVpBanner.setOffscreenPageLimit(mImgList.size());
        //添加到父布局
        mLlBanner.addView(mVpBanner);
        if (isAutoPlay){
            mHandler.sendEmptyMessageDelayed(200,playDuration);
        }
        //将mLlBanner 的onTouch 事件交给mVpBanner 执行
        mLlBanner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mVpBanner.onTouchEvent(event);
            }
        });
        //添加viewpager滑动监听
        addScrollListener();
        //将view添加到布局
        this.addView(mLlBanner);
        //是否显示指示器
        if (isShowIndicator){
            mIndicatorView = new IndicatorView(mContext);
            mIndicatorView.setPosSize(mImgList.size());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams
                    .WRAP_CONTENT,DimensionUtils.dp2px(mContext,12));

            //指示器位置
            switch (indicator_position) {
                case BannerConfig.INDICATOR_LEFT: //左 距左边框16dp
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.leftMargin = DimensionUtils.dp2px(mContext,16);
                    break;
                case BannerConfig.INDICATOR_RIGHT: //右  距右边框16dp
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.rightMargin = DimensionUtils.dp2px(mContext,16);
                    break;
                case BannerConfig.INDICATOR_CENTER: //水平居中
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    break;
            }
            //底部 距离底部10dp
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.bottomMargin = DimensionUtils.dp2px(mContext,10);
            //指示器添加到布局
            mIndicatorView.setLayoutParams(params);
            this.addView(mIndicatorView);
        }
    }

    /**
     * 主要处理 有指示器时 滑动事件
     */
    private void addScrollListener() {
        mVpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (isShowIndicator){
                    int leftMargin;
                    leftMargin = (int) (DimensionUtils.dp2px(mContext,3) + (position % getRealCount()
                            + positionOffset) * DimensionUtils.dp2px(mContext,8));
                    if (curPageIndex == position) {
                        //向右滑动
                        if (position % getRealCount() == mImgList.size() - 1) {
                            if (positionOffset > 0.9) {
                                leftMargin = (int) (DimensionUtils.dp2px(mContext,3));
                            } else {
                                leftMargin = (int) (DimensionUtils.dp2px(mContext,3) + (position %
                                        getRealCount()
                                ) * DimensionUtils.dp2px(mContext,8));
                            }
                        }

                    } else {
                        //向左滑动
                        if ((position % getRealCount()) == mImgList.size() - 1) {
                            if (positionOffset < 0.1) {
                                leftMargin = (int) (DimensionUtils.dp2px(mContext,3) + (position %
                                        getRealCount()
                                ) * DimensionUtils.dp2px(mContext,8));
                            } else {
                                leftMargin = (int) (DimensionUtils.dp2px(mContext,3));
                            }
                        }
                    }

                    mIndicatorView.setPosCurLeftMargin(leftMargin);
                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //判定状态
                switch (state){
                    case 0: //什么都没做
                        mHandler.removeMessages(200);
                        mHandler.sendEmptyMessageDelayed(200, 5000);
                        break;
                    case 1: //开始滑动
                        curPageIndex = mVpBanner.getCurrentItem();
                        mHandler.removeMessages(200);
                        break;
                    case 2: //结束滑动
                        mHandler.removeMessages(200);
                        break;
                }
            }
        });
    }

    public void startPlay(){
        if (isAutoPlay){
            mHandler.sendEmptyMessageDelayed(200,playDuration);
        }

    }

    public void stopPlay(){
        if (isAutoPlay){
            mHandler.removeMessages(200);
        }

    }

    public Banner setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
        return this;
    }

    public Banner setPlayDuration(int playDuration) {
        this.playDuration = playDuration;
        return this;
    }

    public Banner setScrollDuration(int scrollDuration) {
        this.scrollDuration = scrollDuration;
        return this;
    }

    public Banner setIndicator_position(int indicator_position) {
        this.indicator_position = indicator_position;
        return this;
    }

    //条目点击回调方法
    public Banner setOnItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
        return this;
    }

    public Banner setShowIndicator(boolean showIndicator) {
        isShowIndicator = showIndicator;
        return this;
    }

    public Banner setImgList(List<String> imgList) {
        mImgList = imgList;
        return this;
    }

    public Banner setBannerLeftMargin(int bannerLeftMargin) {
        this.bannerLeftMargin = DimensionUtils.dp2px(mContext,bannerLeftMargin);
        return this;
    }

    public Banner setBannerRightMargin(int bannerRightMargin) {
        this.bannerRightMargin = DimensionUtils.dp2px(mContext,bannerRightMargin);
        return this;
    }

    public Banner setBannerRadius(int bannerRadius) {
        this.bannerRadius = DimensionUtils.dp2px(mContext,bannerRadius);
        return this;
    }

    public Banner setBannerMargin(int bannerMargin) {
        this.bannerMargin = DimensionUtils.dp2px(mContext,bannerMargin);
        return this;
    }

    /**
     * 获取开始位置item
     * @return
     */
    private int getStartSelectItem() {
        // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
        // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示

        int currentItem = Integer.MAX_VALUE / 2;
        if (currentItem % getRealCount() == 0) {
            return currentItem;
        }
        // 直到找到从0开始的位置
        while (currentItem % getRealCount() != 0) {
            currentItem++;
        }
        return currentItem;
    }
    /**
     * 获取真实的Count
     *
     * @return
     */
    private int getRealCount() {
        return mImgList == null ? 0 : mImgList.size();
    }
}
