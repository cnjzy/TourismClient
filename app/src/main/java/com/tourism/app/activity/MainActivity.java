package com.tourism.app.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.brand.BrandFragment;
import com.tourism.app.activity.news.NewsFragment;
import com.tourism.app.activity.poolfriend.PoolFriendFragment;
import com.tourism.app.activity.user.UserHomeActivity;
import com.tourism.app.activity.user.UserLoginActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.BaseFragment;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.DeviceUtil;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.DownloadManagerUtils;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.PopupWindowsUtils;
import com.tourism.app.vo.VersionVO;
import com.tourism.app.widget.view.NavigationView;
import com.tourism.app.widget.view.NavigationView.OnTitleChangeListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final int REQUEST_VERSION = 10001;

    private ViewPager viewPager;
    private TabFragmentPagerAdapter tabAdapter;

    private NavigationView navigationView;
    public String[] tabTitle = {"拼友", "品牌", "服务"}; // 标题
    public NavigationView.OnTitleChangeListener onTitleChangeListener = new OnTitleChangeListener() {
        public void change(int i) {
            viewPager.setCurrentItem(i, true);
        }
    };

    private PoolFriendFragment poolFriendFragment;
    private BrandFragment brandFragment;
    private NewsFragment newsFragment;

    /**
     * 更多按钮
     */
    private View more_btn;

    // 定位相关
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void init() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(Constants.LOCATION_SCAN_SPAN);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void initView() {
        navigationView = (NavigationView) findViewById(R.id.page_title);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        more_btn = findViewById(R.id.more_btn);
    }

    @Override
    public void initListener() {
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                navigationView.setSelectPosition(position);
                BaseFragment bf = (BaseFragment) tabAdapter.getItem(position);
                tabAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                navigationView.setLinePoint(arg0, arg2 / tabTitle.length);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void initValue() {
        viewPager.setOffscreenPageLimit(3);
        tabAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        navigationView.setTab(tabTitle, 0);
        navigationView.setOnTitleChangeListener(onTitleChangeListener);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                requestVersion();
            }
        }, 3000);

        initBaiduPush();
    }

    private void initBaiduPush(){

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                DeviceUtil.getChannelName(context, "api_key"));
    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment ft = null;
            switch (arg0) {
                case 0:
                    if (poolFriendFragment == null) {
                        poolFriendFragment = new PoolFriendFragment();
                    }
                    ft = poolFriendFragment;
                    break;
                case 1:
                    if (brandFragment == null) {
                        brandFragment = new BrandFragment();
                    }
                    ft = brandFragment;
                    break;
                case 2:
                    if (newsFragment == null) {
                        newsFragment = new NewsFragment();
                    }
                    ft = newsFragment;
                    break;

            }
            return ft;
        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }

    }

    static int i = 1;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.serach_btn:
                showActivity(context, SearchActivity.class);
                break;
            case R.id.user_btn:
                if (isUserEmpty()) {
                    showActivityForResult(context, UserLoginActivity.class, 99, null);
                } else {
                    showActivity(context, UserHomeActivity.class);
                }
                break;
            case R.id.more_btn:
                PopupWindowsUtils.getHomeMenu(context, more_btn);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK) {
            if (isUserEmpty()) {
                showActivityForResult(context, UserLoginActivity.class, 99, null);
            } else {
                showActivity(context, UserHomeActivity.class);
            }
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || TextUtils.isEmpty(location.getCity())) {
                return;
            }

            LogUtil.i("MyLocationListenner", "city:" + location.getCity());

            Constants.location = location;
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 获取服务信息
     */
    public void requestVersion() {
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("system", 1));
        startHttpRequest(Constants.HTTP_GET, Constants.URL_VERSION, parameter, false, REQUEST_VERSION);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        super.onCallbackFromThread(resultJson, resultCode);
        try{
            switch (resultCode){
                case REQUEST_VERSION:
                    BaseResponseMessage brm = new BaseResponseMessage();
                    brm.parseResponse(resultJson, new TypeToken<VersionVO>(){});
                    if (brm.isSuccess()){
                        final VersionVO vo = (VersionVO) brm.getResult();
                        if (!vo.getVersion().equals(DeviceUtil.getVersionName(context))){
                            DialogUtil.showTipDialog(context, "升级提示", vo.getContent(), new DialogUtil.OnCallbackListener() {
                                public void onClick(int whichButton, Object o) {
                                    DownloadManagerUtils.downloadApk(context, vo.getUrl(), getString(R.string.app_name), vo.getContent());
                                }
                            });
                        }
                    }
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
