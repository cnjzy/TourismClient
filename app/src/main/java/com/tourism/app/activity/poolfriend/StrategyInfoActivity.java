package com.tourism.app.activity.poolfriend;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.poolfriend.adapter.StrategyLeftBarAdapter;
import com.tourism.app.activity.poolfriend.adapter.StrategyListAdapter;
import com.tourism.app.activity.user.UserLoginActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.ShareUtils;
import com.tourism.app.vo.StrategyVO;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/5/10.
 */
public class StrategyInfoActivity extends BaseActivity {
    private final int GET_EVENT_INFO_KEY = 10001;
    private final int REQUEST_SIGNED_UP_KEY = 10002;
    private final int REQUEST_MAKE_KEY = 10003;
    private final int REQUEST_PHOTOLIKE_KEY = 10004;
    private final int REQUEST_COLLECTION = 10005;


    private ImageButton navigation_collection_btn;
    /**
     * 基层View
     */
    private RelativeLayout rootView;
    private View headerView;

    /**
     * 左边栏
     */
    private Animation rightInAnimation;
    private Animation leftOutAnimation;
    private View event_strategy_left_bar_ll;
    private RelativeLayout.LayoutParams leftBarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private ListView left_bar_listView;
    private StrategyLeftBarAdapter leftBarAdapter;

    private ImageView event_banner_iv;
    private ImageView user_icon_iv;
    private TextView user_name_tv;
    private TextView user_notepad_tv;
    private ListView listView;
    private StrategyListAdapter listAdapter;

    /**
     * 活动对象
     */
    private StrategyVO eventVO;
    private int id;
    private String url;

    /**
     * 游记对象
     */
    private StrategyVO strategyVO;
    /**
     * 数据对象列表
     */
    private List<StrategyVO> dataList = new ArrayList<StrategyVO>();
    /**
     * 左边栏数据列表
     */
    private List<StrategyVO> dataLeftBarList = new ArrayList<StrategyVO>();

    /**
     * 圆形图片适配器
     */
    public DisplayImageOptions circleOptions;

    /**
     * 喜欢图片
     */
    private StrategyVO photo;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_event_strategy_info);
    }

    @Override
    public void init() {
        eventVO = (StrategyVO) getIntent().getExtras().getSerializable("vo");
        id = getIntent().getExtras().getInt("id");
        url = getIntent().getExtras().getString("url");

        rightInAnimation = AnimationUtils.loadAnimation(context, R.anim.left_bar_right_out);
        rightInAnimation.setFillAfter(true);

        leftOutAnimation = AnimationUtils.loadAnimation(context, R.anim.left_bar_right_in);
        leftOutAnimation.setFillAfter(true);
        leftOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rootView.removeView(event_strategy_left_bar_ll);
                event_strategy_left_bar_ll.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        headerView = getLayoutInflater().inflate(R.layout.view_event_strategy_info_header, null);
        event_strategy_left_bar_ll = getLayoutInflater().inflate(R.layout.view_event_strategy_left_bar, null);
        left_bar_listView = (ListView) event_strategy_left_bar_ll.findViewById(R.id.left_bar_listView);

        navigation_collection_btn = (ImageButton) headerView.findViewById(R.id.navigation_collection_btn);
        event_banner_iv = (ImageView) headerView.findViewById(R.id.event_banner_iv);
        user_icon_iv = (ImageView) headerView.findViewById(R.id.user_icon_iv);
        user_name_tv = (TextView) headerView.findViewById(R.id.user_name_tv);
        user_notepad_tv = (TextView) headerView.findViewById(R.id.user_notepad_tv);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void initListener() {
        left_bar_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if(position == left_bar_listView.getCount()-1){
                    listView.smoothScrollToPosition(left_bar_listView.getCount() - 1);
                } else {
                    StrategyVO vo = (StrategyVO) left_bar_listView.getItemAtPosition(position);
                    listView.smoothScrollToPositionFromTop(vo.getIndex() + 1, 0);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StrategyVO vo = (StrategyVO) listView.getItemAtPosition(position);
                if(vo != null){
                    Bundle data = new Bundle();
                    data.putSerializable("vo", vo);
                    showActivity(context, CategoryImageActivity.class, data);
                }
            }
        });
    }

    @Override
    public void initValue() {
        if(eventVO != null && !TextUtils.isEmpty(eventVO.getLink())) {
            requestEventInfo(eventVO.getLink());
        } else if(!TextUtils.isEmpty(url)){
            requestEventInfo(url);
        } else {
            String url = Constants.URL_RIP_DETAILS + "?id=" + eventVO.getId() + "&token" + getUserInfo().getToken();
            requestEventInfo(url);
        }
    }

    public void setViewsValue(){
        listView.addHeaderView(headerView);

        leftBarAdapter = new StrategyLeftBarAdapter(context, left_bar_listView);
        leftBarAdapter.addLast(dataLeftBarList, false);
        left_bar_listView.setAdapter(leftBarAdapter);

        listAdapter = new StrategyListAdapter(context, listView);
        listAdapter.setStrategyVO(strategyVO);
        listAdapter.addLast(dataList, false);
        listView.setAdapter(listAdapter);

        ImageLoader.getInstance().displayImage(strategyVO.getLitpic(), event_banner_iv, options, animateFirstListener);
        ImageLoader.getInstance().displayImage(strategyVO.getUser_avatar(), user_icon_iv, circleOptions, animateFirstListener);
        user_name_tv.setText(strategyVO.getUser_nickname());
        user_notepad_tv.setText(strategyVO.getStart_date() + "/" + strategyVO.getDays_count() + "天/" + strategyVO.getPhotos_count() + "图");

        if (strategyVO.getCurrent_user_favorite() == 1){
            navigation_collection_btn.setImageResource(R.drawable.icon_xh_e);
        }else{
            navigation_collection_btn.setImageResource(R.drawable.icon_xh_d);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.show_left_bar_btn:
                rootView.addView(event_strategy_left_bar_ll, leftBarParams);
                event_strategy_left_bar_ll.setVisibility(View.VISIBLE);
                event_strategy_left_bar_ll.startAnimation(rightInAnimation);
                event_strategy_left_bar_ll.setOnClickListener(this);
                break;
            case R.id.event_strategy_left_bar_ll:
                event_strategy_left_bar_ll.startAnimation(leftOutAnimation);
                event_strategy_left_bar_ll.setOnClickListener(null);
                break;
            case R.id.navigation_reply_btn:
                if (isUserEmpty()){
                    showActivity(context, UserLoginActivity.class);
                    return;
                }
                ReplyActivity.show(context, strategyVO.getId() + "", "trip");
                break;
            case R.id.navigation_collection_btn:
                requestCollection(eventVO.getCurrent_user_favorite() == 1 ? "off" : "on");
                eventVO.setCurrent_user_favorite(eventVO.getCurrent_user_favorite() == 1 ? 0 : 1);
                if (eventVO.getCurrent_user_favorite() == 1){
                    navigation_collection_btn.setImageResource(R.drawable.icon_xh_e);
                }else{
                    navigation_collection_btn.setImageResource(R.drawable.icon_xh_d);
                }
                break;
            case R.id.navigation_share_btn:
                ShareUtils.getInstance().share(context, eventVO.getName());
                break;
        }
    }

    /**
     * 获取服务信息
     */
    public void requestEventInfo(String url) {
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        if (getUserInfo() != null)
            parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        startHttpRequest(Constants.HTTP_GET, url, parameter, true,
                GET_EVENT_INFO_KEY);
    }

    /**
     * 收藏活动
     */
    public void requestLikePhoto(String photoid, String action) {
        if (isUserEmpty()){
            showActivity(context, UserLoginActivity.class);
            return;
        }
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        parameter.add(new RequestParameter("photoid", photoid));
        parameter.add(new RequestParameter("action", action));
        startHttpRequest(Constants.HTTP_POST, Constants.URL_LIKE_PHOTO, parameter, false,
                REQUEST_PHOTOLIKE_KEY);
    }

    /**
     * 收藏活动
     */
    public void requestCollection(String action) {
        if (isUserEmpty()){
            showActivity(context, UserLoginActivity.class);
            return;
        }
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        parameter.add(new RequestParameter("aid", eventVO.getId()));
        parameter.add(new RequestParameter("action", action));
        parameter.add(new RequestParameter("type", "trip"));
        startHttpRequest(Constants.HTTP_POST, Constants.URL_COLLECTION, parameter, true,
                REQUEST_COLLECTION);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        // TODO Auto-generated method stub
        super.onCallbackFromThread(resultJson, resultCode);
        try {
            switch (resultCode) {
                case GET_EVENT_INFO_KEY:
                    BaseResponseMessage br1 = new BaseResponseMessage();
                    br1.parseResponse(resultJson, new TypeToken<StrategyVO>() {});
                    if (br1.isSuccess()) {
                        strategyVO = (StrategyVO) br1.getResult();

                        for (int i = 0; i < strategyVO.getTrip_days().size(); i++){
                            StrategyVO dayVO = strategyVO.getTrip_days().get(i);
                            StrategyVO childDayVO = new StrategyVO();
                            childDayVO.setTrip_date(dayVO.getTrip_date());
                            childDayVO.setId(dayVO.getId());
                            childDayVO.setDay(dayVO.getDay());
                            childDayVO.setIsDate(true);
                            dataList.add(childDayVO);

                            for (int j = 0 ; j < dayVO.getLocations().size(); j++){
                                StrategyVO locationVO = dayVO.getLocations().get(j);
                                for (int m = 0; m < locationVO.getNotes().size(); m++){
                                    StrategyVO notedVO = locationVO.getNotes().get(m);
                                    notedVO.setLocation_name(locationVO.getLocation_name());
                                    dataList.add(notedVO);
                                }
                            }
                        }

                        for (int i = 0 ; i < dataList.size(); i++){
                            if (i == 0 || dataList.get(i).isDate() ||
                                    (!TextUtils.isEmpty(dataList.get(i).getLocation_name())
                                            && !dataList.get(i).getLocation_name().equals(dataLeftBarList.get(dataLeftBarList.size()-1).getLocation_name()))
                                    ){
                                dataList.get(i).setIndex(i);
                                dataLeftBarList.add(dataList.get(i));
                            }

                        }

                        setViewsValue();
                    }
                    break;
                case REQUEST_PHOTOLIKE_KEY:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
