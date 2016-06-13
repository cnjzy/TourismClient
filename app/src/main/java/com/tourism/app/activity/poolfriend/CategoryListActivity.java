/** 
 * Project Name:TourismAppClient 
 * File Name:EventCategoryListActivity.java 
 * Package Name:com.tourism.app.activity.poolfriend 
 * Date:2016年4月27日上午11:42:31 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.poolfriend;  

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.SearchActivity;
import com.tourism.app.activity.poolfriend.adapter.EventListAdapter;
import com.tourism.app.activity.poolfriend.adapter.EventStrategyListAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.CategoryVO.Category;
import com.tourism.app.vo.EventVO;
import com.tourism.app.vo.StrategyVO;
import com.tourism.app.widget.view.PullToRefreshView;
import com.tourism.app.widget.view.PullToRefreshView.OnFooterRefreshListener;

import java.util.ArrayList;
import java.util.List;

/** 
 * ClassName:EventCategoryListActivity 
 * Date:     2016年4月27日 上午11:42:31
 * @author   Jzy 
 * @version   
 * @see       
 */
public class CategoryListActivity extends BaseActivity implements OnFooterRefreshListener{
	private final int GET_EVENT_LIST_KEY = 10001;
	private final int GET_STRATEGY_LIST_KEY = 10002;
	
	private ImageView category_banner_iv;
	private PullToRefreshView pull_refresh_view;
	private ListView listView;
	
	/**
	 * 活动列表
	 */
	private EventListAdapter eventListAdapter;
	/**
	 * 攻略列表
	 */
	private EventStrategyListAdapter strategyListAdapter;
	
	/**
     * 当前页码
     */
    private int page = 1;
    /**
     * 是否在加载
     */
    private boolean isLoading = false;
    /**
     * 分类对象
     */
	private Category categoryVO;
	
	/**
	 * 1活动
	 * 2攻略
	 */
	private int currentType = 1;
	
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_event_category);
	}

	@Override
	public void init() {
		categoryVO = (Category) getIntent().getExtras().getSerializable("vo");
	}

	@Override
	public void initView() {
		category_banner_iv = (ImageView) findViewById(R.id.category_banner_iv);
		pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		listView = (ListView) findViewById(R.id.listView);
	}

	@Override
	public void initListener() {
		pull_refresh_view.setOnFooterRefreshListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


				Bundle data = new Bundle();
				if (currentType == 1) {
					StrategyVO bean = (StrategyVO) listView.getItemAtPosition(position);
					if (bean != null) {
						data.putSerializable("vo", bean);
						showActivity(context, StrategyInfoActivity.class, data);
					}
				}else{
					EventVO bean = (EventVO) listView.getItemAtPosition(position);
					if (bean != null) {
						data.putSerializable("vo", bean);
						showActivity(context, CategoryInfoActivity.class, data);
					}
				}
			}
		});
	}

	@Override
	public void initValue() {
		ImageLoader.getInstance().displayImage(categoryVO.getBanner(), category_banner_iv, options, animateFirstListener);

		strategyListAdapter = new EventStrategyListAdapter(context, listView);
		listView.setAdapter(strategyListAdapter);
		requestEventList(categoryVO.getLink_strategy(), true);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		page = 0;
//        isLoading = false;
//        adapter.clear();
//        getContactsList(true);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.navigation_left_btn:
			break;
		case R.id.navigation_right_btn:
			showActivity(context, SearchActivity.class);
			break;
		case R.id.tab_left_rb:
			currentType = 1;
			strategyListAdapter = new EventStrategyListAdapter(context, listView);
			listView.setAdapter(strategyListAdapter);
			page = 1;
			isLoading = false;
			requestEventList(categoryVO.getLink_strategy(), true);
			break;
		case R.id.tab_right_rb:
			currentType = 2;
			eventListAdapter = new EventListAdapter(context, listView);
			listView.setAdapter(eventListAdapter);
			page = 1;
			isLoading = false;
			requestEventList(categoryVO.getLink_activity(), true);
			break;
		}
	}

	@Override
    public void onFooterRefresh(PullToRefreshView view) {
        if(!isLoading){
            page ++;
            if(currentType == 1){
            	requestEventList(categoryVO.getLink_strategy(), false);
            }else{
            	requestEventList(categoryVO.getLink_activity(), false);
            }
        }else{
            pull_refresh_view.onFooterRefreshComplete();
        }
    }
	
	/**
	 * 获取服务信息
	 */
	public void requestEventList(String url, boolean isLoading) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("page", page));
		startHttpRequest(Constants.HTTP_GET, url, parameter, isLoading, GET_EVENT_LIST_KEY);
	}

	@Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        // TODO Auto-generated method stub
        super.onCallbackFromThread(resultJson, resultCode);
        try{
            switch(resultCode){
            case GET_EVENT_LIST_KEY:
            	BaseResponseMessage br1 = new BaseResponseMessage();

				if(currentType == 1){
					br1.parseResponse(resultJson, new TypeToken<List<StrategyVO>>(){});
					if(br1.isSuccess() && br1.getResultList() != null && br1.getResultList().size() > 0){
						strategyListAdapter.addLast(br1.getResultList());
						isLoading = false;
					}
				}else{
					br1.parseResponse(resultJson, new TypeToken<List<EventVO>>(){});
					if(br1.isSuccess() && br1.getResultList() != null && br1.getResultList().size() > 0){
						eventListAdapter.addLast(br1.getResultList());
						isLoading = false;
					}
				}


                pull_refresh_view.onFooterRefreshComplete();
                break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
  