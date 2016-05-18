package com.tourism.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.adapter.SearchListAdapter;
import com.tourism.app.activity.poolfriend.StrategyInfoActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.StrategyVO;
import com.tourism.app.widget.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener {
    private final int GET_EVENT_LIST_KEY = 10001;
    private final int GET_STRATEGY_LIST_KEY = 10002;

    private EditText navigation_title_et;
    private ListView listView;
    private SearchListAdapter adapter;


    private ImageView category_banner_iv;
    private PullToRefreshView pull_refresh_view;

    /**
     * 当前页码
     */
    private int page = 1;
    /**
     * 是否在加载
     */
    private boolean isLoading = false;

    /**
     * 关键字
     */
    private String keyword = "";

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        navigation_title_et = (EditText) findViewById(R.id.navigation_title_et);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void initListener() {
        pull_refresh_view.setOnFooterRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StrategyVO bean = (StrategyVO) listView.getItemAtPosition(position);
                if (bean != null) {
                    Bundle data = new Bundle();
                    data.putSerializable("vo", bean);
                    showActivity(context, StrategyInfoActivity.class, data);
                }
            }
        });

        navigation_title_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    keyword = navigation_title_et.getText().toString();
                    if (TextUtils.isEmpty(keyword) && event.getAction() == KeyEvent.ACTION_DOWN) {
                        showToast("搜索关键字不能为空");
                    } else {
                        adapter.clear();
                        page = 1;
                        requestEventList(true);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void initValue() {
        adapter = new SearchListAdapter(context, listView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.navigation_right_btn:
                goBack();
                break;
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (!isLoading) {
            page++;
            requestEventList(false);
        } else {
            pull_refresh_view.onFooterRefreshComplete();
        }
    }

    /**
     * 获取服务信息
     */
    public void requestEventList(boolean isLoading) {
        isLoading = true;
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("page", page));
        parameter.add(new RequestParameter("keyword", keyword));
        startHttpRequest(Constants.HTTP_GET, Constants.URL_SEARCH_, parameter, isLoading, GET_EVENT_LIST_KEY);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        // TODO Auto-generated method stub
        super.onCallbackFromThread(resultJson, resultCode);
        System.out.println("resultJson:" + resultJson);
        try {
            switch (resultCode) {
                case GET_EVENT_LIST_KEY:
                    BaseResponseMessage br1 = new BaseResponseMessage();
                    br1.parseResponse(resultJson, new TypeToken<List<StrategyVO>>() {
                    });
                    if (br1.isSuccess() && br1.getResultList() != null && br1.getResultList().size() > 0) {
                        adapter.addLast(br1.getResultList());
                        isLoading = false;
                    } else {
                        if (br1.isSuccess())
                            showToast("暂无结果");
                    }
                    pull_refresh_view.onFooterRefreshComplete();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
