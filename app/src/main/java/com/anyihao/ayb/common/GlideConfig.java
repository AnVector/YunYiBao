package com.anyihao.ayb.common;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * author: Administrator
 * date: 2017/2/14 001417:46.
 * email:looper@126.com
 */

public class GlideConfig implements GlideModule {

    private static final int DISK_SIZE = 1024 * 1024 * 100;
    private static final int MEMORY_SIZE = (int) (Runtime.getRuntime().maxMemory()) / 8;  //
    // 取1/8最大内存作为最大缓存

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
    // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DISK_SIZE));  //内存中
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "cache", DISK_SIZE)); //sd卡中

        // 默认内存和图片池大小
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(MEMORY_SIZE));
        builder.setBitmapPool(new LruBitmapPool(MEMORY_SIZE));

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
