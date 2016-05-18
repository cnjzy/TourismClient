package com.tourism.app.activity.guides;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesLocationSearchAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.StringUtil;
import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.widget.dialog.CustomLoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/7.
 */
public class GuidesLocationSearchActivity extends BaseActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    private ListView listView;
    private GuidesLocationSearchAdapter adapter;
    private EditText navigation_title_et;


    /**
     * 百度地图
     */
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<GuidesLocationVO> dataList = new ArrayList<GuidesLocationVO>();

    /**
     * 游记对象
     */
    private GuidesVO vo;

    private GuidesDao guidesDao;
    private GuidesDayDao dayDao;
    private GuidesLocationDao locationDao;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_location_search);
    }

    @Override
    public void init() {
        loadingDialog = new CustomLoadingDialog(context, "", false);

        vo = (GuidesVO) getIntent().getExtras().get("vo");
        guidesDao = new GuidesDao(context);
        dayDao = new GuidesDayDao(context);
        locationDao = new GuidesLocationDao(context);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.listView);
        navigation_title_et = (EditText) findViewById(R.id.navigation_title_et);
    }

    @Override
    public void initListener() {
        navigation_title_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    String city = Constants.location != null ? Constants.location.getCity() : "北京";
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(city));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final GuidesLocationVO locationVO = (GuidesLocationVO) listView.getItemAtPosition(position);
                DialogUtil.showDateAlert(context, new DialogUtil.OnDateSelectedListener() {
                    @Override
                    public void onSelected(int year, int month, int day) {
                        String dateStr = year + "-" + StringUtil.formatNumber(month) + "-" + StringUtil.formatNumber(day);
                        Map<String, Object> params = new HashMap<String, Object>();
                        GuidesDayVO dayVO = dayDao.getDayByGuidesIdAndDate(vo.getLocal_id(), dateStr);
                        if (dayVO == null) {
                            dayVO = new GuidesDayVO();
                            dayVO.setTrip_date(dateStr);
                            dayVO.setParent_id(vo.getLocal_id());
                            dayVO.setLocal_id(dayDao.add(dayVO));
                        }

                        locationVO.setParent_id(dayVO.getLocal_id());
                        locationDao.add(locationVO);
                        setResult(RESULT_OK);
                        finish();
                    }
                }, "选择日期");
            }
        });
    }

    @Override
    public void initValue() {
        setNavigationTitle("关联游记地点");

        loadingDialog.show();

        if (Constants.location == null) {
            mPoiSearch.searchInCity((new PoiCitySearchOption())
                    .city(Constants.location == null ? "北京" : Constants.location.getCity())
                    .keyword("景点")
                    .pageCapacity(20)
                    .pageNum(1));
        } else {
            mPoiSearch.searchNearby(new PoiNearbySearchOption()
                    .location(new LatLng(Constants.location.getLatitude(), Constants.location.getLongitude()))
                    .keyword("景点")
                    .radius(3000)
                    .pageNum(0)
                    .pageCapacity(20));
        }

        adapter = new GuidesLocationSearchAdapter(context, listView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.navigation_right_btn:
                goBack();
                break;
            case R.id.tab_left_rb:
                if (loadingDialog != null && !loadingDialog.isShowing()){
                    loadingDialog.show();
                }
                if (Constants.location == null) {
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(Constants.location == null ? "北京" : Constants.location.getCity())
                            .keyword("景点")
                            .pageCapacity(20)
                            .pageNum(1));
                } else {
                    mPoiSearch.searchNearby(new PoiNearbySearchOption()
                            .location(new LatLng(Constants.location.getLatitude(), Constants.location.getLongitude()))
                            .keyword("景点")
                            .radius(3000)
                            .pageNum(0)
                            .pageCapacity(20));
                }
                break;
            case R.id.tab_right_rb:
                if (loadingDialog != null && !loadingDialog.isShowing()){
                    loadingDialog.show();
                }
                LogUtil.i("location", Constants.location + "");
                if (Constants.location == null) {
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(Constants.location == null ? "北京" : Constants.location.getCity())
                            .keyword("住宿")
                            .pageCapacity(20)
                            .pageNum(1));
                } else {
                    mPoiSearch.searchNearby(new PoiNearbySearchOption()
                            .location(new LatLng(Constants.location.getLatitude(), Constants.location.getLongitude()))
                            .keyword("住宿")
                            .radius(3000)
                            .pageNum(0)
                            .pageCapacity(20));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    public void onGetPoiResult(PoiResult result) {
        loadingDialog.cancel();
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(context, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            adapter.clear();
            for (int i = 0; i < result.getAllPoi().size(); i++) {
                LogUtil.i("poi", result.getAllPoi().get(i).name);
                GuidesLocationVO vo = new GuidesLocationVO();
                vo.setLocation_name(result.getAllPoi().get(i).name);
                vo.setLocation_name_en(result.getAllPoi().get(i).address);
                dataList.add(vo);
            }
            adapter.addLast(dataList);

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(context, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        loadingDialog.cancel();
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(context, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        loadingDialog.cancel();
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        adapter.clear();
        for (int i = 0; i < res.getAllSuggestions().size(); i++) {
            LogUtil.i("poi", res.getAllSuggestions().get(i).key);
            GuidesLocationVO vo = new GuidesLocationVO();
            vo.setLocation_name(res.getAllSuggestions().get(i).key);
            vo.setLocation_name_en(res.getAllSuggestions().get(i).key);
            dataList.add(vo);
        }
        adapter.addLast(dataList);
//        sugAdapter.clear();
//        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
//            if (info.key != null)
//                sugAdapter.add(info.key);
//        }
//        sugAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromWindow() {
        loadingDialog.cancel();
        super.onDetachedFromWindow();
    }
}
