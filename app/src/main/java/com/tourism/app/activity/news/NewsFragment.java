package com.tourism.app.activity.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.news.adapter.NewsBannerAdapter;
import com.tourism.app.activity.news.adapter.NewsListAdapter;
import com.tourism.app.activity.web.WebViewActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.BaseFragment;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.DeviceUtil;
import com.tourism.app.vo.NewsVO;
import com.tourism.app.vo.WeatherVO;
import com.tourism.app.widget.view.ChildViewPager;
import com.tourism.app.widget.view.PointWidget;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment{
	private final int REQUEST_GET_NEWS_CODE = 10001;
	private final int REQUEST_GET_WEATHER_CODE = 10002;
	
	private ChildViewPager view_pager;
	private PointWidget view_pager_ponit;
	private TextView news_name_tv;
	private NewsBannerAdapter newsBannerAdapter;
	private RelativeLayout.LayoutParams bannerParams;
	
	private ListView list_view;
	private NewsListAdapter listAdapter;

	
	// 天气
	private TextView weather_city_tv;
	private TextView weather_wendu_tv;
	private TextView weather_kongqi_tv;
	private TextView weather_xianxing_tv;
	
	private NewsVO vo;
	
	@Override
	public void init() {
		bannerParams = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, DeviceUtil.dip2px(context, 180));
	}

	@Override
	public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_news, container, false);
	}

	@Override
	public void initView() {
		view_pager = (ChildViewPager) findViewById(R.id.view_pager);
		view_pager_ponit = (PointWidget) findViewById(R.id.view_pager_ponit);
		news_name_tv = (TextView) findViewById(R.id.news_name_tv);
		list_view = (ListView) findViewById(R.id.list_view);
		
		weather_city_tv = (TextView) findViewById(R.id.weather_city_tv);
		weather_wendu_tv = (TextView) findViewById(R.id.weather_wendu_tv);
		weather_kongqi_tv = (TextView) findViewById(R.id.weather_kongqi_tv);
		weather_xianxing_tv = (TextView) findViewById(R.id.weather_xianxing_tv);
	}

	@Override
	public void initListener() {
		news_name_tv.setOnClickListener(this);
		list_view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NewsVO vo = (NewsVO) list_view.getItemAtPosition(position);
				if(vo != null){
					WebViewActivity.show(context, vo.getLink(), getString(R.string.news_info_title));
				}
			}
		});
	}

	@Override
	public void initValue() {
		requestNews();
		requestWeather();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.news_name_tv:
			if(vo != null){
				WebViewActivity.show(context, vo.getStick().getLink(), getString(R.string.news_info_title));
			}
			break;
		}
	}
	
	/**
	 * 获取服务信息
	 */
	public void requestNews() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequest(Constants.HTTP_GET, Constants.URL_NEWS_LIST, parameter, false, REQUEST_GET_NEWS_CODE);
	}


	/**
	 * 获取天气信息
	 */
	public void requestWeather() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("city", Constants.location == null ? "北京" : Constants.location.getCity()));
		startHttpRequest(Constants.HTTP_GET, Constants.URL_WEATHER_LIST, parameter, false, REQUEST_GET_WEATHER_CODE);
	}
	
	@Override
	public synchronized void onCallbackFromThread(String resultJson, int resultCode) {
		try{
			BaseResponseMessage brm = new BaseResponseMessage();
			switch(resultCode){
			case REQUEST_GET_NEWS_CODE:
				brm.parseResponse(resultJson, new TypeToken<NewsVO>(){});
				if(brm.isSuccess()){
					vo = (NewsVO) brm.getResult();
					if(vo != null){
						// 设置头条新闻
						news_name_tv.setText(getString(R.string.news_top1, vo.getStick().getTitle()));
						news_name_tv.setOnClickListener(this);
						
						// 设置banner
//						view_pager.setLayoutParams(bannerParams);
						newsBannerAdapter = new NewsBannerAdapter(context, vo.getBanner(), view_pager, view_pager_ponit);
						view_pager.setAdapter(newsBannerAdapter);
						view_pager.setCurrentItem(100 + (100%vo.getBanner().size()), false);
						
						// 设置新闻列表
						listAdapter = new NewsListAdapter((BaseActivity) context, list_view);
						listAdapter.addLast(vo.getList(), false);
						list_view.setAdapter(listAdapter);
					}
				}
				break;
			case REQUEST_GET_WEATHER_CODE:
				brm.parseResponse(resultJson, new TypeToken<WeatherVO>(){});
				if(brm.isSuccess()){
					WeatherVO wvo = (WeatherVO) brm.getResult();
					weather_city_tv.setText(wvo.getWeather().getData().getCity());
					weather_wendu_tv.setText(wvo.getWeather().getData().getCond() + " " + wvo.getWeather().getData().getTmp() + "°");
					weather_kongqi_tv.setText("空气质量：" + wvo.getWeather().getData().getQlty() + "    PM2.5：" + wvo.getWeather().getData().getPm25());
					String number = "";
					for (int i = 0; i < wvo.getVehiclelimit().getData().getNumber().size(); i++) {
						number += wvo.getVehiclelimit().getData().getNumber().get(i);
						if(i != wvo.getVehiclelimit().getData().getNumber().size()-1){
							number += "和";
						}
					}
					weather_xianxing_tv.setText("今日尾号限行：" + number);
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
