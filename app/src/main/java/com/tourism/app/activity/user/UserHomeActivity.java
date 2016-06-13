package com.tourism.app.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.guides.GuidesAddActivity;
import com.tourism.app.activity.guides.GuidesEditActivity;
import com.tourism.app.activity.poolfriend.CategoryInfoActivity;
import com.tourism.app.activity.poolfriend.StrategyInfoActivity;
import com.tourism.app.activity.user.adapter.UserCollectionAdapter;
import com.tourism.app.activity.user.adapter.UserFollowAdapter;
import com.tourism.app.activity.user.adapter.UserStrategyAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.vo.UserCollectionVO;
import com.tourism.app.vo.UserFollowVO;
import com.tourism.app.vo.UserInfoVO;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;
import com.tourism.app.widget.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.huewu.pla.lib.internal.PLA_AdapterView;
import me.maxwin.view.XListView;

public class UserHomeActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener {

    private final int REQUEST_GET_USER_INFO_CODE = 10001;
    private final int REQUEST_GET_STRATEGY_CODE = 10002;
    private final int REQUEST_GET_COLLECTION_CODE = 10003;
    private final int REQUEST_GET_FOLLOW_CODE = 10004;

    private ImageView user_icon_iv;
    private TextView user_name_tv;
    private ImageView user_gender_iv;
    private TextView user_notepad_tv;
    private ImageView user_down_arrow_iv;
    private Button add_guides_btn;
    private ImageView user_vip_iv;

    private PullToRefreshView pull_refresh_view;
    private ListView listView;
    private XListView xlistView;
    private UserStrategyAdapter strategyAdapter;
    private UserCollectionAdapter collectionAdapter;
    private UserFollowAdapter followAdapter;


    /**
     * 空布局
     */
    private View list_is_empty_ll;
    private TextView list_is_empty_content_tv;

    /**
     * 屏幕宽度
     */
    private int screenWidth = 0;

    /**
     * 圆形图片加载器
     */
    private DisplayImageOptions circleOptions;

    /**
     * 用户对象
     */
    private UserInfoVO vo;

    /**
     * 当前类型
     * 1 攻略列表
     * 2 我的收藏
     * 3 喜欢
     */
    private int currentType = 0;

    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 是否在加载
     */
    private boolean isLoading = false;

    /**
     * 数据库操作对象
     */
    private GuidesDao guidesDao;
    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;
    private GuidesNotedDao guidesNotedDao;

    private List<GuidesVO> guidesList;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_user);
    }

    @Override
    public void init() {
        guidesDao = new GuidesDao(context);
        guidesDayDao = new GuidesDayDao(context);
        guidesLocationDao = new GuidesLocationDao(context);
        guidesNotedDao = new GuidesNotedDao(context);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        circleOptions = new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.img_default_horizon)
                .showImageForEmptyUri(R.drawable.img_default_horizon)
                .showImageOnFail(R.drawable.img_default_horizon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
    }

    @Override
    public void initView() {
        user_icon_iv = (ImageView) findViewById(R.id.user_icon_iv);
        user_name_tv = (TextView) findViewById(R.id.user_name_tv);
        user_gender_iv = (ImageView) findViewById(R.id.user_gender_iv);
        user_notepad_tv = (TextView) findViewById(R.id.user_notepad_tv);
        user_down_arrow_iv = (ImageView) findViewById(R.id.user_down_arrow_iv);
        add_guides_btn = (Button) findViewById(R.id.add_guides_btn);
        user_vip_iv = (ImageView) findViewById(R.id.user_vip_iv);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        listView = (ListView) findViewById(R.id.listView);
        xlistView = (XListView) findViewById(R.id.xlistView);

        list_is_empty_ll = findViewById(R.id.list_is_empty_ll);
        list_is_empty_content_tv = (TextView) findViewById(R.id.list_is_empty_content_tv);
    }

    @Override
    public void initListener() {
        user_icon_iv.setOnClickListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentType == 0) {
                    GuidesVO vo = (GuidesVO) listView.getItemAtPosition(position);
                    if (vo != null) {
                        Bundle data = new Bundle();
                        data.putSerializable("vo", vo);
                        showActivityForResult(context, GuidesEditActivity.class, 101, data);
                    }
                } else if (currentType == 1) {
                    UserCollectionVO vo = (UserCollectionVO) listView.getItemAtPosition(position);
                    if (vo != null) {
                        Bundle data = new Bundle();
                        data.putString("url", vo.getLink());
                        if (vo.getType().equals("trip")) {
                            showActivity(context, StrategyInfoActivity.class, data);
                        } else {
                            showActivity(context, CategoryInfoActivity.class, data);
                        }
                    }
                }
            }
        });

            xlistView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(PLA_AdapterView<?> parent, View view, int position,
                                        long id) {
                    UserFollowVO vo = (UserFollowVO) xlistView.getItemAtPosition(position);
                    if (vo != null) {
                        Bundle data = new Bundle();
                        data.putSerializable("vo", vo);
                        showActivity(context, UserFollowImageActivity.class, data);
                    }
                }
            });
        }

        @Override
        public void initValue () {
            setNavigationTitle("我的");
            setNavigationRightButton(View.VISIBLE, 0, R.drawable.ico_gd_d);
            setDownArrowPoint(0);

            xlistView.setPullLoadEnable(false);
            xlistView.setPullRefreshEnable(false);

            initData();

            if (guidesList == null || guidesList.size() == 0) {
                list_is_empty_ll.setVisibility(View.VISIBLE);
                list_is_empty_content_tv.setText("您还没有攻略");
            } else {
                list_is_empty_ll.setVisibility(View.GONE);
                strategyAdapter = new UserStrategyAdapter(context, listView);
                strategyAdapter.addLast(guidesList, false);
                listView.setAdapter(strategyAdapter);
            }
        }

        @Override
        protected void onStart () {
            super.onStart();
            requestUserInfo();
            initData();
            setViewsValue();
        }

        /**
         * 初始化数据
         */

    private void initData() {

        guidesList = guidesDao.getByParams(null);

        for (int n = 0; guidesList != null && n < guidesList.size(); n++) {
            GuidesVO guidesVO = guidesList.get(n);
            String key = "parent_id";
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put(key, guidesVO.getLocal_id());
            guidesVO.setTrip_days(guidesDayDao.getByParams(params));
            for (int i = 0; i < guidesVO.getTrip_days().size(); i++) {
                params.put(key, guidesVO.getTrip_days().get(i).getLocal_id());
                guidesVO.getTrip_days().get(i).setLocations(guidesLocationDao.getByParams(params));
            }

            for (int i = 0; i < guidesVO.getTrip_days().size(); i++) {
                for (int j = 0; j < guidesVO.getTrip_days().get(i).getLocations().size(); j++) {
                    params.put(key, guidesVO.getTrip_days().get(i).getLocations().get(j).getLocal_id());
                    guidesVO.getTrip_days().get(i).getLocations().get(j).setNotes(guidesNotedDao.getByParams(params));
                }
            }

            guidesVO.setPhotoVO(guidesNotedDao.getById(guidesVO.getFront_cover_photo_id()));
        }

        if (currentType == 0){
            if (guidesList == null || guidesList.size() == 0) {
                list_is_empty_ll.setVisibility(View.VISIBLE);
                list_is_empty_content_tv.setText("您还没有攻略");
            } else {
                list_is_empty_ll.setVisibility(View.GONE);
                strategyAdapter = new UserStrategyAdapter(context, listView);
                strategyAdapter.addLast(guidesList, false);
                listView.setAdapter(strategyAdapter);
            }
        }

    }

    private void setViewsValue() {
        if(vo != null) {
            ImageLoader.getInstance().displayImage(vo.getAvatar(), user_icon_iv, circleOptions, animateFirstListener);
            user_name_tv.setText(TextUtils.isEmpty(vo.getNickname()) ? "游客" : vo.getNickname());
            if (vo.getSex() == 0 || vo.getSex() == 3) {
                user_gender_iv.setVisibility(View.INVISIBLE);
            } else {
                user_gender_iv.setBackgroundResource(vo.getSex() == 1 ? R.drawable.ico_nan : R.drawable.ico_nv);
            }

            if (vo.getVip_status() == 2) {
                user_vip_iv.setBackgroundResource(R.drawable.vip_review);
            } else {
                user_vip_iv.setBackgroundResource(R.drawable.vip_review_n);
            }

            user_notepad_tv.setText(vo.getTrips_count() + "篇游记");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.user_gl_rb:
                setDownArrowPoint(0);
                xlistView.setVisibility(View.GONE);
                pull_refresh_view.setVisibility(View.VISIBLE);

                initData();

                if (guidesList == null || guidesList.size() == 0) {
                    list_is_empty_ll.setVisibility(View.VISIBLE);
                    list_is_empty_content_tv.setText("您还没有攻略");
                } else {
                    list_is_empty_ll.setVisibility(View.GONE);
                    strategyAdapter = new UserStrategyAdapter(context, listView);
                    strategyAdapter.addLast(guidesList, false);
                    listView.setAdapter(strategyAdapter);
                }
                break;
            case R.id.user_sc_rb:
                page = 0;
                isLoading = false;
                setDownArrowPoint(1);
                requestCollectionList(true);
                break;
            case R.id.user_xh_rb:
                page = 0;
                isLoading = false;
                setDownArrowPoint(2);
                requestFollowList(true);
                break;
            case R.id.user_gd_rb:
                showActivity(context, UserSblAuthActivity.class);
                break;
            case R.id.user_icon_iv:
                showActivity(context, UserInfoActivity.class);
                break;
            case R.id.navigation_right_btn:
                DialogUtil.showUserMoreDialog(context, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        if (whichButton == 0) {
                            showActivity(context, UserActionsActivity.class);
                        }
                    }
                });
                break;
            case R.id.add_guides_btn:
                showActivity(context, GuidesAddActivity.class);
                break;
        }
    }

    /**
     * 设置下箭头坐标
     *
     * @param i
     */
    private void setDownArrowPoint(int i) {
        currentType = i;
        int itemWidth = screenWidth / 4;
        int marginLeft = itemWidth * i
                + (itemWidth - user_down_arrow_iv.getWidth()) / 2;
        LinearLayout.LayoutParams arrowParams = (LinearLayout.LayoutParams) user_down_arrow_iv
                .getLayoutParams();
        arrowParams.leftMargin = marginLeft;
        user_down_arrow_iv.setLayoutParams(arrowParams);
        if (i == 0){
            add_guides_btn.setVisibility(View.VISIBLE);
        }else{
            add_guides_btn.setVisibility(View.GONE);
        }
    }

    /**
     * 获取用户信息
     */
    public void requestUserInfo() {
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));

        startHttpRequest(Constants.HTTP_GET, Constants.URL_GET_USER_INFO,
                parameter, true, REQUEST_GET_USER_INFO_CODE);
    }

    /**
     * 获取游记收藏列表信息
     */
    public void requestCollectionList(boolean isLoading) {
        page++;
        isLoading = true;
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        parameter.add(new RequestParameter("page", page));
        startHttpRequest(Constants.HTTP_GET, Constants.URL_USER_COLLECTION,
                parameter, isLoading, REQUEST_GET_COLLECTION_CODE);
    }

    /**
     * 获取游记喜欢列表信息
     */
    public void requestFollowList(boolean isLoading) {
        page++;
        isLoading = true;
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        parameter.add(new RequestParameter("page", page));
        startHttpRequest(Constants.HTTP_GET, Constants.URL_USER_FOLLOW,
                parameter, isLoading, REQUEST_GET_FOLLOW_CODE);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        super.onCallbackFromThread(resultJson, resultCode);
        try {
            BaseResponseMessage brm = new BaseResponseMessage();
            switch (resultCode) {
                case REQUEST_GET_USER_INFO_CODE:
                    brm.parseResponse(resultJson, new TypeToken<UserInfoVO>() {
                    });
                    if (brm.isSuccess()) {
                        vo = (UserInfoVO) brm.getResult();
                        vo.setToken(getUserInfo().getToken());
                        setUserInfo(vo);
                        setViewsValue();
                    } else if(brm.getCode() == 300){
                        showActivity(context, UserLoginActivity.class);
                    }
                    break;
                case REQUEST_GET_COLLECTION_CODE:
                    if (page == 1) {
                        xlistView.setVisibility(View.GONE);
                        pull_refresh_view.setVisibility(View.VISIBLE);
                        collectionAdapter = new UserCollectionAdapter(context, listView);
                        listView.setAdapter(collectionAdapter);
                    }
                    brm = new BaseResponseMessage();
                    brm.parseResponse(resultJson, new TypeToken<List<UserCollectionVO>>() {
                    });
                    if (brm.isSuccess() && brm.getResultList() != null && brm.getResultList().size() > 0) {
                        list_is_empty_ll.setVisibility(View.GONE);
                        collectionAdapter.addLast(brm.getResultList());
                        isLoading = false;
                    } else {
                        if (collectionAdapter.getCount() == 0) {
                            list_is_empty_ll.setVisibility(View.VISIBLE);
                            list_is_empty_content_tv.setText("您还没有收藏");
                        }
                    }
                    pull_refresh_view.onFooterRefreshComplete();
                    break;
                case REQUEST_GET_FOLLOW_CODE:
                    if (page == 1) {
                        followAdapter = new UserFollowAdapter(context, listView);
                        xlistView.setAdapter(followAdapter);
                        xlistView.setVisibility(View.VISIBLE);
                        pull_refresh_view.setVisibility(View.GONE);
                    }
                    brm = new BaseResponseMessage();
                    brm.parseResponse(resultJson, new TypeToken<List<UserFollowVO>>() {
                    });
                    if (brm.isSuccess() && brm.getResultList() != null && brm.getResultList().size() > 0) {
                        list_is_empty_ll.setVisibility(View.GONE);
                        followAdapter.addLast(brm.getResultList());
                        isLoading = false;
                    } else {
                        if (followAdapter.getCount() == 0) {
                            list_is_empty_ll.setVisibility(View.VISIBLE);
                            list_is_empty_content_tv.setText("您还没有喜欢的图片");
                        }
                    }
                    pull_refresh_view.onFooterRefreshComplete();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (!isLoading) {
            if (currentType == 0) {

            } else if (currentType == 1) {
                requestCollectionList(false);
            } else if (currentType == 2) {
                requestFollowList(false);
            }
        } else {
            pull_refresh_view.onFooterRefreshComplete();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 99:
                // 登录
                break;
            case 101:
                // 游记返回
                break;
            default:
                finish();
                break;
        }
    }
}
