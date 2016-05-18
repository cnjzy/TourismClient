package com.tourism.app.util.image;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;

import com.tourism.app.util.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/9.
 */
public class ImageUtils {
    /**
     * 获取SD卡图片文件夹目录
     */
    public static List<ImageFolder> getAllSDImageFolder(Context context) {
        List<ImageFolder> ifList = new ArrayList<ImageFolder>();

        String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA, // 图片绝对路径
                "count(" + MediaStore.Images.Media._ID + ")"// 统计当前文件夹下共有多少张图片
        };
        String selection = " 0==0) group by bucket_display_name --(";

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "");

        if (null != cursor) {
            while (cursor.moveToNext()) {
                ImageFolder ifoler = new ImageFolder();
                String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                int count = cursor.getInt(5);// 该文件夹下一共有多少张图片
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, fileId, MediaStore.Images.Thumbnails.MICRO_KIND, options);// 获取指定图片缩略片

                ifoler.setFolderId(folderId);
                ifoler.setName(folder);
                ifoler.setFinaName(finaName);
                ifoler.setPath(path);
                ifoler.setCount(count);
// ifoler.setBmp(bitmap);
                ifList.add(ifoler);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }

//            for (int i = 0; i < ifList.size(); i++) {
//                System.err.println(ifList.get(i).toString());
//                getAllImageByFolder(context, ifList.get(i));
//            }
        }
        return ifList;
    }


    public static List<ImageFolder> getAllImageByFolder(Context context, ImageFolder imageFolder) {
        List<ImageFolder> imageList = new ArrayList<ImageFolder>();

        String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
                MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
                MediaStore.Images.Media.DATA, // 图片绝对路径
// "count(" + MediaStore.Images.Media._ID + ")"//
// 统计当前文件夹下共有多少张图片
        };

        String selection = " " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = '" + imageFolder.getName() + "' ) --(";

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "");

        if (null != cursor) {
            while (cursor.moveToNext()) {
                ImageFolder ifoler = new ImageFolder();
                String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, fileId, MediaStore.Images.Thumbnails.MICRO_KIND, options);// 获取指定图片缩略片

                ifoler.setFolderId(folderId);
                ifoler.setFileId(fileId);
                ifoler.setName(folder);
                ifoler.setFinaName(finaName);
                ifoler.setPath(path);
// ifoler.setBmp(bitmap);
                imageList.add(ifoler);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }

//            for (int i = 0; i < imageList.size(); i++) {
//                System.err.println(imageList.get(i).toString());
//            }
        }

        return  imageList;
    }

    /**
     * 按照时间给图片排序
     * @return
     */
    public static Map<String, List<ImageFolder>> sortImageByDate(List<ImageFolder> list){
        try{
            Map<String, List<ImageFolder>> map = new HashMap<String, List<ImageFolder>>();
            for (int i = 0 ; list != null && i < list.size() ; i ++){
                File file = new File(list.get(i).getPath());
                String dateStr = DateUtils.getDateByLong(file.lastModified(), DateUtils.parsePatterns[1]);
                list.get(i).setDateStr(dateStr);
                if(map.get(dateStr) != null){
                    map.get(dateStr).add(list.get(i));
                }else{
                    List<ImageFolder> flist = new ArrayList<ImageFolder>();
                    flist.add(list.get(i));
                    map.put(dateStr, flist);
                }
            }
            return map;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * SD卡地址
     */
    public static final String BASE_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Tourism" + File.separator + "images";

//    /**
//     * 拷贝所有文件
//     */
//    public static void copyAllFile(GuidesVO vo){
//        try {
//            for (int i = 0 ; i < vo.getTrip_days().size() ; i++){
//                GuidesDayVO dayVO = vo.getTrip_days().get(i);
//                for (int j = 0; j < dayVO.getLocations().size(); j++){
//                    GuidesLocationVO locationVO = dayVO.getLocations().get(j);
//                    for (int m = 0; m < locationVO.get)
//                }
//            }
//
//            File tempFile = new File(TEMP_PATH);
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
//            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//            bos.flush();
//            bos.close();
//            bm.recycle();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
