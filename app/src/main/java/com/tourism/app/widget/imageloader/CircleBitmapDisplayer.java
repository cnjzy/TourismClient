/** 
 * Project Name:TourismAppClient 
 * File Name:CircleBitmapDisplayer.java 
 * Package Name:com.tourism.app.widget.imageloader 
 * Date:2016年4月27日下午4:44:55 
 * Copyright (c) 2016, jzy0106@aliyun.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.widget.imageloader;  

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/** 
 * ClassName:CircleBitmapDisplayer 
 * Date:     2016年4月27日 下午4:44:55
 * @author   Jzy 
 * @version   
 * @see       
 */
public class CircleBitmapDisplayer implements BitmapDisplayer {

    protected  final int margin ;

    public CircleBitmapDisplayer() {
        this(0);
    }

    public CircleBitmapDisplayer(int margin) {
        this.margin = margin;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap, margin));
    }


}
  