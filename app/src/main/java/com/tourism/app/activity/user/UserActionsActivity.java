package com.tourism.app.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.poolfriend.CategoryInfoActivity;
import com.tourism.app.activity.user.adapter.UserActionsAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.UserActionsVO;
import com.tourism.app.widget.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/5/10.
 */
public class UserActionsActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener{

    private final int GET_EVENT_LIST_KEY = 10001;

    private PullToRefreshView pull_refresh_view;
    private ListView listView;
    private UserActionsAdapter adapter;
    /**
     * 当前页码
     */
    private int page = 1;
    /**
     * 是否在加载
     */
    private boolean isLoading = false;


    @Override
    public void initLayout() {
        setContentView(R.layout.activity_user_actions);
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void initListener() {
        pull_refresh_view.setOnFooterRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserActionsVO bean = (UserActionsVO) listView.getItemAtPosition(position);
                if (bean != null) {
                    Bundle data = new Bundle();
                    data.putSerializable("url", bean.getLink());
                    showActivity(context, CategoryInfoActivity.class, data);
                }
            }
        });
    }

    @Override
    public void initValue() {
        setNavigationTitle("报名活动");

        adapter = new UserActionsAdapter(context, listView);
        listView.setAdapter(adapter);

        requestEventList(true);
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
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        startHttpRequest(Constants.HTTP_GET, Constants.URL_USER_ACTIONS, parameter, isLoading, GET_EVENT_LIST_KEY);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        // TODO Auto-generated method stub
        super.onCallbackFromThread(resultJson, resultCode);
        try {
            switch (resultCode) {
                case GET_EVENT_LIST_KEY:
                    BaseResponseMessage br1 = new BaseResponseMessage();
                    br1.parseResponse(resultJson, new TypeToken<List<UserActionsVO>>() {
                    });
                    if (br1.isSuccess() && br1.getResultList() != null && br1.getResultList().size() > 0) {
                        adapter.addLast(br1.getResultList());
                        isLoading = false;
                    }
                    pull_refresh_view.onFooterRefreshComplete();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
