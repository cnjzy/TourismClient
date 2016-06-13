package com.tourism.app.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.tourism.app.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Jzy on 16/6/5.
 */
public class ShareUtils {

    private static ShareUtils share;
    private Activity act;

    public static synchronized ShareUtils getInstance(){
        if (share == null){
            share = new ShareUtils();
        }
        return share;

//        Config.OpenEditor = false;
    }

    public void share(Activity act, String content){
        this.act = act;
        Bitmap bitmap = BitmapFactory.decodeResource(act.getResources(), R.drawable.ic_launcher);
        UMImage image = new UMImage(act, bitmap);
        //UMImage image = new UMImage(ShareActivity.this,bitmap);
        //UMusic music = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//        UMusic music = new UMusic("http://y.qq.com/#type=song&mid=002I7CmS01UAIH&tpl=yqq_song_detail");
//        music.setTitle("This is music title");
//        music.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");
//        music.setDescription(content);

//        UMVideo video = new UMVideo("http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");
//        video.setThumb("http://www.adiumxtras.com/images/thumbs/dango_menu_bar_icon_set_11_19047_6240_thumb.png");
        String url = "http://www.yuanxingjia.cn/index_m.html";

        new ShareAction(act).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .withTitle(content)
                .withText(content)
                .withMedia(image)
                .withTargetUrl(url)
                .setCallback(umShareListener)
                .open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(act, "收藏成功啦", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(act, "分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(act,"分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(act,"分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
