/** 
 * Project Name:TourismAppClient 
 * File Name:BrandInfoActivity.java 
 * Package Name:com.tourism.app.activity.brand 
 * Date:2016年4月6日下午1:55:33 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.brand;  

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.brand.adapter.BrandInfoListAdapter;
import com.tourism.app.activity.web.WebViewActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.vo.BrandInfoVO;
import com.tourism.app.vo.NewsVO;

/** 
 * ClassName:BrandInfoActivity 
 * Date:     2016年4月6日 下午1:55:33
 * @author   Jzy 
 * @version   
 * @see       
 */
public class BrandInfoActivity extends BaseActivity{
	private final int REQUEST_GET_BRAND_INFO_CODE = 10001;
	
	private SlidingMenu menu;
	
	/**
	 * ListView
	 */
	private ListView list_view;
	private BrandInfoListAdapter listAdapter;
	/**
	 * bannerView
	 */
	private View headerView;
	private ImageView brand_header_icon_1_iv;
	private ImageView brand_header_icon_2_iv;
	
	private View footerView;
	private TextView qr_title_tv;
	private ImageView qr_icon_iv;
	
	private BrandInfoVO brandInfoVO;
	private NewsVO newsVO;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_brand_info);
		initSlidingMenu();
	}

	@Override
	public void init() {
		newsVO = (NewsVO) getIntent().getExtras().getSerializable("vo");
	}

	@Override
	public void initView() {
		list_view = (ListView) findViewById(R.id.list_view);
		headerView = getLayoutInflater().inflate(R.layout.view_brand_info_header, null);
		brand_header_icon_1_iv = (ImageView) headerView.findViewById(R.id.brand_header_icon_1_iv);
		brand_header_icon_2_iv = (ImageView) headerView.findViewById(R.id.brand_header_icon_2_iv);
		
		footerView = getLayoutInflater().inflate(R.layout.view_list_footer_qr_code, null);
		qr_title_tv = (TextView) footerView.findViewById(R.id.qr_title_tv);
		qr_icon_iv = (ImageView) footerView.findViewById(R.id.qr_icon_iv);
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initValue() {
		if(newsVO != null){
			requestBrandInfo();
		}else{
			showToast(R.string.toast_vo_is_empty);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.more_btn:
			menu.showMenu();
			break;
		case R.id.user_btn:
			menu.showSecondaryMenu();
			break;
		case R.id.back_btn:
			goBack();
			break;
		case R.id.header_service_phone_btn:
			if(brandInfoVO != null){
				DialogUtil.showCallPhone(context, brandInfoVO.getHotline());
			}
			break;
		case R.id.header_near_distributor_btn:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getDealers(), getString(R.string.brand_near_distributor));
			}
			break;
		case R.id.header_show_info_btn:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getIndex_url(), getString(R.string.brand_info_home));
			}
			break;
		case R.id.header_make_test_drive_btn:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getTestdrive(), getString(R.string.brand_make_test_drive));
			}
			break;
		case R.id.home_tv:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getIndex_url(), getString(R.string.brand_info_home));
			}
			break;
		case R.id.tmail_tv:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getTmall_url(), getString(R.string.brand_info_tmail));
			}
			break;
		case R.id.pc_tv:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getHome_url(), getString(R.string.brand_info_pc));
			}
			break;
		case R.id.weixin_tv:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getWechat(), getString(R.string.brand_info_weixin));
			}
			break;
		case R.id.service_phone_btn:
			if(brandInfoVO != null){
				DialogUtil.showCallPhone(context, brandInfoVO.getHotline());
			}
			break;
		case R.id.near_distributor_btn:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getDealers(), getString(R.string.brand_near_distributor));
			}
			break;
		case R.id.make_maintenance_btn:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getMaintain(), getString(R.string.brand_make_maintenance));
			}
			break;
		case R.id.make_test_drive_btn:
			if(brandInfoVO != null){
				WebViewActivity.show(context, brandInfoVO.getTestdrive(), getString(R.string.brand_make_test_drive));
			}
			break;
		case R.id.online_advice_ll:
			Bundle data = new Bundle();
			data.putSerializable("vo", brandInfoVO);
			showActivity(context, BrandFeedbackActivity.class, data);
			break;
		}
	}
	
	private void initSlidingMenu(){
		// configure the SlidingMenu  
        menu = new SlidingMenu(this);  
        menu.setMode(SlidingMenu.LEFT_RIGHT);  
        // 设置触摸屏幕的模式  
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  
        menu.setShadowWidthRes(R.dimen.shadow_width);  
        menu.setShadowDrawable(R.drawable.shadow);  
        menu.setSecondaryShadowDrawable(R.drawable.shadowright);
  
        // 设置滑动菜单视图的宽度  
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);  
        // 设置渐入渐出效果的值  
        menu.setFadeDegree(0.35f);  
        /** 
         * SLIDING_WINDOW will include the Title/ActionBar in the content 
         * section of the SlidingMenu, while SLIDING_CONTENT does not. 
         */  
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  
        //为侧滑菜单设置布局  
        menu.setMenu(R.layout.menu_brand_service);  
        menu.setSecondaryMenu(R.layout.menu_brand_user);
	}
	
	/**
	 * 获取品牌信息
	 */
	public void requestBrandInfo() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequest(Constants.HTTP_GET, newsVO.getLink(), parameter, true, REQUEST_GET_BRAND_INFO_CODE);
	}
	
	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);
		try{
			BaseResponseMessage brm = new BaseResponseMessage();
			switch(resultCode){
			case REQUEST_GET_BRAND_INFO_CODE:
				brm.parseResponse(resultJson, new TypeToken<BrandInfoVO>(){});
				if(brm.isSuccess()){
					brandInfoVO = (BrandInfoVO) brm.getResult();
					if(brandInfoVO != null){
						setNavigationTitle(brandInfoVO.getTitle());
						
						list_view.addHeaderView(headerView);
						ImageLoader.getInstance().displayImage(brandInfoVO.getAd_pic(), brand_header_icon_1_iv, options, animateFirstListener);
						ImageLoader.getInstance().displayImage(brandInfoVO.getBrand_pic(), brand_header_icon_2_iv, options, animateFirstListener);
						
						list_view.addFooterView(footerView);
						qr_title_tv.setText("关注" + brandInfoVO.getTitle() + "官方微信账号");
						ImageLoader.getInstance().displayImage(brandInfoVO.getBarcode_pic(), qr_icon_iv, options, animateFirstListener);
						
						listAdapter = new BrandInfoListAdapter(context, list_view);
						listAdapter.addLast(brandInfoVO.getProduct_pic(), false);
						listAdapter.setVo(brandInfoVO);
						list_view.setAdapter(listAdapter);
						
					}
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
  