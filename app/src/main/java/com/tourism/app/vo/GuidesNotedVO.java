package com.tourism.app.vo;

/**
 * Created by Jzy on 16/5/9.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.ref.SoftReference;

/**
 * 游记内容节点
 */
@DatabaseTable(tableName = "guides_noteds")
public class GuidesNotedVO extends BaseDBVO {
    @DatabaseField
    private int rank;
    @DatabaseField
    private String type;
    @DatabaseField
    private String description = "";

    /**
     * Image
     */
    @DatabaseField
    private String path;
    @DatabaseField(defaultValue = "0")
    private boolean is_image_upload = false;
    @DatabaseField
    private long fileId;
    @DatabaseField
    private String date;

    public GuidesNotedVO(){}

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean is_image_upload() {
        return is_image_upload;
    }

    public void setIs_image_upload(boolean is_image_upload) {
        this.is_image_upload = is_image_upload;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 图片
     */
    private SoftReference<Bitmap> bmp;
    /**
     * 不失真改变Bitmap
     */
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public Bitmap getBmp(Context context) {
        if (bmp == null) {
            ContentResolver cr = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, fileId, MediaStore.Images.Thumbnails.MICRO_KIND, options);// 获取指定图片缩略片

            if (bitmap == null) {
                options.inJustDecodeBounds = true;
                Bitmap tmp = BitmapFactory.decodeFile(path, options);
                if (tmp != null) {
                    tmp.recycle();
                    tmp = null;
                }

                final double beY = options.outHeight * 1.0 / 300;
                final double beX = options.outWidth * 1.0 / 300;
                options.inSampleSize = (int) (beY < beX ? beX : beY);
                if (options.inSampleSize <= 1) {
                    options.inSampleSize = 1;
                }

                // NOTE: out of memory error
                while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                    options.inSampleSize++;
                }

                int newHeight = 300;
                int newWidth = 300;
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }

                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(path, options);
            }

            bmp = new SoftReference<Bitmap>(bitmap);
        }

        return bmp.get();
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = new SoftReference<Bitmap>(bmp);
    }

    public void recyleBitmap() {
        this.bmp = null;
    }

    @Override
    public String toString() {
        return "GuidesNotedVO{" +
                "rank=" + rank +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", path='" + path + '\'' +
                ", is_image_upload=" + is_image_upload +
                ", fileId=" + fileId +
                ", date='" + date + '\'' +
                ", bmp=" + bmp +
                '}';
    }
}
