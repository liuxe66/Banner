package com.liuxe.bannerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuxe.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Banner mBanner1,mBanner2,mBanner3,mBanner4;
    private List<String> mImgs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBanner1 = findViewById(R.id.banner1);
        mBanner2 = findViewById(R.id.banner2);
        mBanner3 = findViewById(R.id.banner3);
        mBanner4 = findViewById(R.id.banner4);

        mImgs.add("http://www.yunxiaosheng.com/oss/image/article/picture/031633c93b04492eae6dd8d443e62064.jpg");
        mImgs.add("http://www.yunxiaosheng.com/oss/image/article/picture/db52d42e717a4d6982e2a9653d0eaffd.jpg");
        mImgs.add("http://www.yunxiaosheng.com/oss/image/article/picture/e00018e1a70444c09ac7e2f7bfe4787e.jpg");
        mImgs.add("http://www.yunxiaosheng.com/oss/image/article/picture/031633c93b04492eae6dd8d443e62064.jpg");
        mImgs.add("http://www.yunxiaosheng.com/oss/image/article/picture/db52d42e717a4d6982e2a9653d0eaffd.jpg");
        mImgs.add("http://www.yunxiaosheng.com/oss/image/article/picture/e00018e1a70444c09ac7e2f7bfe4787e.jpg");

        mBanner1.setImgList(mImgs)
                .start();
        mBanner2.setImgList(mImgs)
                .start();
        mBanner3.setImgList(mImgs)
                .start();
        mBanner4.setImgList(mImgs)
                .setAutoPlay(true)
                .setPlayDuration(6000)
                .setShowIndicator(true)
                .setScrollDuration(1200)
                .start();
    }
}
