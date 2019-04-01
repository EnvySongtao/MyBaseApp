package com.gst.mybaseapp.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.math.BigDecimal;

/**
 * 图片导入框架  gilde支持gif 和普通导入 还支持圆角哦
 * 因为Glide实现简单 所以直接在ImageLoader 实现了 ，正确方式需要再包装的
 */
public class ImageLoader {
    private static final ImageLoader ourInstance = new ImageLoader();

    public static ImageLoader getInstance() {
        return ourInstance;
    }

    private ImageLoader() {
    }


    /**
     *
     * 资源文件导入image 无失败默认图
     * @param context
     * @param resId
     * @param imageView
     */
    public static void loadResouceIntoImageView(Context context, int resId, ImageView imageView){
        try {
            Glide.with(context).load(resId).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 资源文件导入image 有失败默认图
     * @param context
     * @param resId
     * @param imageView
     * @param errorResId
     */
    public static void loadResouceIntoImageViewError(Context context, int resId, ImageView imageView, int errorResId){
        try {
            Glide.with(context).load(resId).error(errorResId).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 网络文件导入image 无失败默认图
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadUrlIntoImageView(Context context,String url, ImageView imageView){
        try {
            if(isUrlGif(url)){
                Glide.with(context).asGif().load(url).into(imageView);
            }else{
                Glide.with(context).load(url).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 网络文件导入image 有失败默认图
     * @param context
     * @param url
     * @param imageView
     * @param errorResId
     */
    public static void loadUrlIntoImageViewError(Context context,String url, ImageView imageView, int errorResId){
        try {
            if(errorResId<=0){
                loadUrlIntoImageView(context,url,imageView);
            }else if(isUrlGif(url)){
                Glide.with(context).load(url).error(errorResId).into(imageView);
            }else{
                Glide.with(context).asGif().load(url).error(errorResId).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 文件导入image 无失败默认图
     * @param context
     * @param fliePath
     * @param imageView
     */
    public static void loadFlieIntoImageView(Context context,String fliePath, ImageView imageView){
        try {
            Glide.with(context).load(new File(fliePath)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 文件导入image 有失败默认图
     * @param context
     * @param fliePath
     * @param imageView
     * @param errorResId
     */
    public static void loadFlieImageViewError(Context context,String fliePath, ImageView imageView, int errorResId){
        try {
            Glide.with(context).load(new File(fliePath)).error(errorResId).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支持将图片转为圆角导入
     * @param context
     * @param uri
     * @param imageView
     */
    public static void loadCircleImageView(Context context,String uri, ImageView imageView){
        if(StringUtils.startWithIngoreCase(uri,"http")){
            Glide.with(context).load(uri)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                    .into(imageView);
        }else{
            Glide.with(context).load(new File(uri))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                    .into(imageView);
        }
    }


    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir=context.getExternalCacheDir()+ ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/"+ InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static boolean isUrlGif(String url){
        if(TextUtils.isEmpty(url)) return false;
        if(url.length()<4) return false;
        String urlLast4=url.substring(url.length()-4);
        if(!".gif".equalsIgnoreCase(urlLast4)) return false;
        return true;
    }
}
