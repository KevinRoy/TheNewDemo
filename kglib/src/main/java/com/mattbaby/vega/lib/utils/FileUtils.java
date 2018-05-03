package com.mattbaby.vega.lib.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * file util
 * Created by kevin on 16/8/10.
 */

public class FileUtils {

    public static final String FILECACHE = "appCache";

    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 获取SD卡的根目录
    public static String getSDCardBaseDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    // 获取缓存目录
    public static String getCacheDir() {
        String cachePath;
        if (isSDCardMounted()) {
            File outCacheFile = ContextUtils.appContext.getExternalCacheDir();
            if (outCacheFile != null) {
                cachePath = outCacheFile.getAbsolutePath();
            } else {
                String packageName = DeviceUtils.getPackageName();
                cachePath = FileUtils.getSDCardBaseDir()
                        + File.separator + FILECACHE
                        + File.separator + packageName;
            }
        } else {
            cachePath = ContextUtils.appContext.getCacheDir().getAbsolutePath();
        }
        File file = new File(cachePath);
        if (!file.exists()) file.mkdirs();
        return cachePath;
    }

    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath() {
        File path = null;
        if (sdCardIsAvailable()) {
            path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 文件或者文件夹是否存在.
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除指定文件夹下所有文件, 保留文件夹.
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File exeFile = files[i];
            if (exeFile.isDirectory()) {
                delAllFile(exeFile.getAbsolutePath());
            } else {
                exeFile.delete();
            }
        }
        return flag;
    }


    /**
     * 删除指定文件夹，包括文件夹本身以及子目录以及子文件
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteDirectory(String dirPath) {// 删除目录（文件夹）以及目录下的文件
        boolean flag;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();// 获得传入路径下的所有文件
        for (int i = 0; i < files.length; i++) {// 循环遍历删除文件夹下的所有文件(包括子目录)
            if (files[i].isFile()) {// 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                System.out.println(files[i].getAbsolutePath() + " 删除成功");
                if (!flag)
                    break;// 如果删除失败，则跳出
            } else {// 运用递归，删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;// 如果删除失败，则跳出
            }
        }
        if (!flag)
            return false;
        if (dirFile.delete()) {// 删除当前目录
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {// 路径为文件且不为空则进行删除
            file.delete();// 文件删除
            return true;
        }
        return false;
    }

    /**
     * 文件复制.
     */
    public static boolean copy(String srcFile, String destFile) {
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 复制整个文件夹内.
     *
     * @param oldPath string 原文件路径如：c:/fqf.
     * @param newPath string 复制后路径如：f:/fqf/ff.
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
    }

    /**
     * 重命名文件.
     */
    public static boolean renameFile(String resFilePath, String newFilePath) {
        File resFile = new File(resFilePath);
        File newFile = new File(newFilePath);
        return resFile.renameTo(newFile);
    }

    /**
     * 获取磁盘可用空间.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static long getSDCardAvailaleSize() {
        File path = getRootPath();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /**
     * 获取某个目录可用大小.
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static long getDirSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /**
     * 获取文件或者文件夹大小.
     */
    public static long getFileAllSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] childrens = file.listFiles();
                long size = 0;
                if (childrens.length > 0) {
                    for (File f : childrens) {
                        size += getFileAllSize(f.getPath());
                    }
                }
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }

    /**
     * 创建一个文件.
     */
    public static boolean initFile(String path) {
        boolean result = false;
        try {
            File file = new File(path);
            if (!file.exists()) {
                result = file.createNewFile();
            } else if (file.isDirectory()) {
                file.delete();
                result = file.createNewFile();
            } else if (file.exists()) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建一个文件夹.
     */
    public static boolean initDirectory(String path) {
        boolean result = false;
        File file = new File(path);
        if (!file.exists()) {
            result = file.mkdirs();
        } else if (!file.isDirectory()) {
            file.delete();
            result = file.mkdirs();
        } else if (file.exists()) {
            result = true;
        }
        return result;
    }

    /**
     * 复制文件.
     */
    public static void copyFile(File from, File to) throws IOException {
        if (!from.exists()) {
            throw new IOException("The source file not exist: " + from.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(from);
        try {
            copyFile(fis, to);
        } finally {
            fis.close();
        }
    }

    /**
     * 复制文件.
     */
    public static long copyFile(InputStream from, File to) throws IOException {
        long totalBytes = 0;
        FileOutputStream fos = new FileOutputStream(to, false);
        try {
            byte[] data = new byte[1024];
            int len;
            while ((len = from.read(data)) > -1) {
                fos.write(data, 0, len);
                totalBytes += len;
            }
            fos.flush();
        } finally {
            fos.close();
        }
        return totalBytes;
    }

    /**
     * 保存流到文件.
     */
    public static void saveFile(InputStream inputStream, String filePath) {
        try {
            OutputStream outputStream = new FileOutputStream(new File(filePath), false);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用UTF8保存一个文件.
     */
    public static void saveFileUTF8(String path, String content, Boolean append) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, append);
        Writer out = new OutputStreamWriter(fos, "UTF-8");
        out.write(content);
        out.flush();
        out.close();
        fos.flush();
        fos.close();
    }

    /**
     * 用UTF8读取一个文件.
     */
    public static String getFileUTF8(String path) {
        String result = "";
        InputStream fin = null;
        try {
            fin = new FileInputStream(path);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            fin.close();
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到一个文件Intent.
     */
    public static Intent getFileIntent(String path, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), mimeType);
        return intent;
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     */
    public static void saveImage(Bitmap bm, String fileName) {
        try {
            File myCaptureFile = new File(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把Bitmap高质量压缩存储到手机里面
     *
     * @param bmp
     * @param fileName 路径-包括文件名
     */
    public static void compressBmpToFile(Bitmap bmp, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.PNG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把图片网址转为Bitmap
     */
    public static Bitmap url_bitmap(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            int max = conn.getContentLength();
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            byte[] result = baos.toByteArray();
            return BitmapFactory.decodeByteArray(result, 0, result.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按照比例缩放图片
     *
     * @param path 路径
     * @param w    宽
     * @param h    高
     * @return
     */
    public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    //把图片案比例饿压缩为合适大小--高质量
    public static Bitmap getImageVie(String srcPath, float height, float width) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = height;//这里设置高度为
        float ww = width;//这里设置宽度为
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

//    有时候显示图片的时候 会发现有些图片的位置不正，这时我们就需要调整一下方向：

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     *
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
        int degree = getPicRotate(path);
        if (degree != 0) {
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        return bitmap;
    }

    /**
     * 读取图片文件旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    /**
     * 判断文件是否为图片，仅通过后缀名判断很不全面，如果把一个txt文件后缀改成gif都会被认为是图片，通过java可以判断文件本身是否为图片。
     */
    public static boolean isImage(String path) {
        Drawable drawable = Drawable.createFromPath(path);
        if (drawable == null) {
//            Toast.show("图片格式错误");
            return false;
        } else {
            return true;
        }
    }
}
