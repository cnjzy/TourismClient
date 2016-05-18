package com.tourism.app.activity.guides;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesDetailListAdapter;
import com.tourism.app.activity.poolfriend.adapter.StrategyLeftBarAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.DateUtils;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.vo.StrategyVO;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/5/10.
 */
public class GuidesDetailActivity extends BaseActivity {
    private final int GET_EVENT_INFO_KEY = 10001;
    private final int REQUEST_SIGNED_UP_KEY = 10002;
    private final int REQUEST_MAKE_KEY = 10003;
    private final int REQUEST_PHOTOLIKE_KEY = 10004;

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
    private GuidesDetailListAdapter listAdapter;

    /**
     * 活动对象
     */
    private GuidesVO eventVO;

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


    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_detail);
    }

    @Override
    public void init() {
        eventVO = (GuidesVO) getIntent().getExtras().getSerializable("vo");

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
        headerView = getLayoutInflater().inflate(R.layout.view_guides_detail_header, null);
        event_strategy_left_bar_ll = getLayoutInflater().inflate(R.layout.view_event_strategy_left_bar, null);
        left_bar_listView = (ListView) event_strategy_left_bar_ll.findViewById(R.id.left_bar_listView);

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
                    listView.smoothScrollToPositionFromTop(vo.getIndex(), -300);
                }
            }
        });
    }

    @Override
    public void initValue() {

        for (int i = 0; i < eventVO.getTrip_days().size(); i++){
            GuidesDayVO dayVO = eventVO.getTrip_days().get(i);
            StrategyVO childDayVO = new StrategyVO();
            childDayVO.setTrip_date(dayVO.getTrip_date());
            childDayVO.setId(dayVO.getLocal_id());
            childDayVO.setDay(DateUtils.getIntervalDays(eventVO.getStart_date(), dayVO.getTrip_date(), DateUtils.parsePatterns[1]) + "");
            childDayVO.setIsDate(true);
            dataList.add(childDayVO);

            for (int j = 0 ; j < dayVO.getLocations().size(); j++){
                GuidesLocationVO locationVO = dayVO.getLocations().get(j);
                for (int m = 0; m < locationVO.getNotes().size(); m++){
                    GuidesNotedVO notedVO = locationVO.getNotes().get(m);
                    StrategyVO childNotedVO = new StrategyVO();
                    childNotedVO.setId(notedVO.getLocal_id());
                    childNotedVO.setType(notedVO.getType());
                    childNotedVO.setDescription(notedVO.getDescription());
                    childNotedVO.setUrl(notedVO.getPath());

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap tmp = BitmapFactory.decodeFile(notedVO.getPath(), options);
                    if (tmp != null) {
                        tmp.recycle();
                        tmp = null;
                    }
                    childNotedVO.setWidth(options.outWidth);
                    childNotedVO.setHeight(options.outHeight);
                    childNotedVO.setLocation_name(locationVO.getLocation_name());
                    dataList.add(childNotedVO);
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

    public void setViewsValue(){
        listView.addHeaderView(headerView);

        leftBarAdapter = new StrategyLeftBarAdapter(context, left_bar_listView);
        leftBarAdapter.addLast(dataLeftBarList, false);
        left_bar_listView.setAdapter(leftBarAdapter);

        listAdapter = new GuidesDetailListAdapter(context, listView);
        listAdapter.addLast(dataList, false);
        listView.setAdapter(listAdapter);

        if (eventVO.getPhotoVO() != null){
            LoadLocalImageUtil.getInstance().displayFromSDCard(eventVO.getPhotoVO().getPath(), event_banner_iv, options);
        }

        ImageLoader.getInstance().displayImage(getUserInfo().getAvatar(), user_icon_iv, circleOptions, animateFirstListener);
        user_name_tv.setText(getUserInfo().getNickname());
        user_notepad_tv.setText(eventVO.getStart_date() + "/" + DateUtils.getIntervalDays(eventVO.getStart_date(), eventVO.getEnd_date(), DateUtils.parsePatterns[1]) + "天/" + eventVO.getPhotos_count() + "图");
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
                break;
        }
    }
}
