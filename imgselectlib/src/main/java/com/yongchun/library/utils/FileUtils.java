package com.yongchun.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dee on 15/11/20.
 */
public class FileUtils {

    public static final String POSTFIX = ".jpeg";
    public static final String APP_NAME = "Matt";
    public static final String MATT_PATH_IMAGE = "/" + APP_NAME + "/Image/";

    public static File createCameraFile(Context context) {
        return createMediaFile(context, MATT_PATH_IMAGE);
    }

    public static File createCropFile(Context context) {
        return createMediaFile(context, MATT_PATH_IMAGE);
    }

    private static File createMediaFile(Context context, String parentPath) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(
                Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory() : context.getCacheDir();
        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists()) {
            folderDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp;
        File tmpFile = new File(folderDir, fileName + POSTFIX);
        return tmpFile;
    }

    /**
     * 判断文件是否为图片，仅通过后缀名判断很不全面，如果把一个txt文件后缀改成gif都会被认为是图片，
     * <p>
     * 通过安卓Bitmap来判断是否为图片，但是当图片大于3M的时候可能会出现OOM，解决方案：http://blog.csdn.net/shuaihj/article/details/8808409
     */
    public static boolean isImage(String path) {
        //解决方案
        InputStream is = null;
        try {
            //1.加载位图
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //2.为位图设置100K的缓存
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTempStorage = new byte[100 * 1024];
        //3. 设置位图颜色显示优化方式
        //ALPHA_8：每个像素占用1byte内存（8位）
        //ARGB_4444:每个像素占用2byte内存（16位）
        //ARGB_8888:每个像素占用4byte内存（32位）
        //RGB_565:每个像素占用2byte内存（16位）
        //Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
        opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        opts.inPurgeable = true;
        //5.设置位图缩放比例
        //width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
        opts.inSampleSize = 4;
        //6.设置解码位图的尺寸信息
        opts.inInputShareable = true;
        //7.解码位图
        Bitmap drawable = BitmapFactory.decodeStream(is, null, opts);
        //8.显示位图
        //...
        //如果直接使用这个如果文件大于3M可能会OOM
//        Bitmap drawable = BitmapFactory.decodeFile(path);
        if (drawable == null) {
            //图片格式错误
            return false;
        } else {
            return true;
        }
    }

}
