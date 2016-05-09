package com.tourism.app.util.image;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.tourism.app.vo.BaseVO;

import java.lang.ref.SoftReference;

/**
 * Created by Jzy on 16/5/9.
 */
public class ImageFolder extends BaseVO {
    private String folderId;
    private long fileId;
    private String name;
    private int index;
    private String finaName;
    private String path;
    private int count;
    private String dateStr;
    private SoftReference<Bitmap> bmp;
    private boolean isSelected = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBmp(Context context) {
        if (bmp == null) {
            ContentResolver cr = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, fileId, MediaStore.Images.Thumbnails.MICRO_KIND, options);// 获取指定图片缩略片
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

    /**
     * @return the folderId
     */
    public String getFolderId() {
        return folderId;
    }

    /**
     * @param folderId the folderId to set
     */
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /**
     * @return the fileId
     */
    public long getFileId() {
        return fileId;
    }

    /**
     * @param fileId the fileId to set
     */
    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the finaName
     */
    public String getFinaName() {
        return finaName;
    }

    /**
     * @param finaName the finaName to set
     */
    public void setFinaName(String finaName) {
        this.finaName = finaName;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /* (non-Javadoc)
            * @see java.lang.Object#toString()
            */
    @Override
    public String toString() {
        return "ImageFolder [folderId=" + folderId + ", fileId=" + fileId + ", name=" + name + ", index=" + index + ", finaName=" + finaName + ", path=" + path + ", count="
                + count + ", bmp=" + bmp + "]";
    }

}
