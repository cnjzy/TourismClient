package com.tourism.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tourism.app.util.StringUtil;
import com.tourism.app.util.preference.Preferences;
import com.tourism.app.util.preference.PreferencesUtils;
import com.tourism.app.vo.UserInfoVO;
import com.umeng.socialize.PlatformConfig;

import org.json.simple.BaseJson;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyApp extends Application {
    private static Context context;

    /**
     * 登陆用户
     */
    private UserInfoVO mUserInfo;

    /**
     * 配置文件信息
     */
    public static PreferencesUtils preferencesUtils;

    /**
     * 图片加载参数选项
     */
    public static DisplayImageOptions options;
    
    /**
     *  图片第一次加载动画
     */
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();

        // 初始化加载图片
        initImageLoader(getApplicationContext());

        // 本地化
        preferencesUtils = new PreferencesUtils(getApplicationContext(), Preferences.USER_INFO_FILE);
        mUserInfo = (UserInfoVO) (BaseJson.parser(new TypeToken<UserInfoVO>() {
        }, preferencesUtils.getString(Preferences.PREFERENSE_USER_INFO, "")));
        
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

        // 第三方授权、分享
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx7bd1c85b7b913127", "a1b45b0e3fb1331530c5d0a98b93fba6");
        //新浪微博
        PlatformConfig.setSinaWeibo("1752635441", "699cef5eb788d08d6d11c12bbe0cd8e2");
        // QQ
        PlatformConfig.setQQZone("1105350693", "vj02zRPvC0G7jdz3");
    }

    /**
     * 初始化图片加载
     * 
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.img_default_horizon)
                .showImageForEmptyUri(R.drawable.img_default_horizon).showImageOnFail(R.drawable.img_default_horizon)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedBitmapDisplayer(20))
                .build();

    }

    public DisplayImageOptions getDefaultImageLoaderOptions() {
        return options;
    }

    public UserInfoVO getUserInfo() {
    	if(mUserInfo == null)
    		mUserInfo = (UserInfoVO) (BaseJson.parser(new TypeToken<UserInfoVO>(){}, preferencesUtils.getString(Preferences.PREFERENSE_USER_INFO, "")));
        return mUserInfo;
    }

    public void setUserInfo(UserInfoVO mUserInfo) {
        this.mUserInfo = mUserInfo;
        preferencesUtils.putString(Preferences.PREFERENSE_USER_INFO, BaseJson.toJson(mUserInfo));
    }

    public static void showToast(String msg) {
        Toast toast = Toast.makeText(context, (!StringUtil.isEmpty(msg)) ? msg : "", Toast.LENGTH_SHORT);
        toast.show();
    }
    
    public ImageLoadingListener getAnimateFirstListener() {
        return animateFirstListener;
    }
    
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
    
}
