package com.tourism.app.activity.poolfriend;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.poolfriend.adapter.ReplyAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.ReplyVO;
import com.tourism.app.widget.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/5/11.
 */
public class ReplyActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener{
    private final int GET_EVENT_LIST_KEY = 10001;

    private PullToRefreshView pull_refresh_view;
    private ListView listView;
    private ReplyAdapter adapter;

    private View list_is_empty_ll;
    private TextView list_null_text_tv;

    /**
     * 当前页码
     */
    private int page = 1;
    /**
     * 是否在加载
     */
    private boolean isLoading = false;

    private String aid;
    private String type;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_reply);
    }

    @Override
    public void init() {
        aid = getIntent().getExtras().getString("aid");
        type = getIntent().getExtras().getString("type");
    }

    @Override
    public void initView() {
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        listView = (ListView) findViewById(R.id.listView);
        list_is_empty_ll = findViewById(R.id.list_is_empty_ll);
        list_null_text_tv = (TextView) findViewById(R.id.list_null_text_tv);

    }

    @Override
    public void initListener() {
        pull_refresh_view.setOnFooterRefreshListener(this);
    }

    @Override
    public void initValue() {
        setNavigationTitle("评论列表");
        adapter = new ReplyAdapter(context, listView);
        listView.setAdapter(adapter);

        list_is_empty_ll.setVisibility(View.GONE);
        list_null_text_tv.setText("暂无评论");

        requestEventList(true);
    }

    /**
     * 获取服务信息
     */
    public void requestEventList(boolean isLoading) {
        isLoading = true;
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("aid", aid));
        parameter.add(new RequestParameter("type", type));
        parameter.add(new RequestParameter("page", page));
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        startHttpRequest(Constants.HTTP_GET, Constants.URL_REPLY_LIST, parameter, isLoading, GET_EVENT_LIST_KEY);
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
                    br1.parseResponse(resultJson, new TypeToken<List<ReplyVO>>() {
                    });
                    if (br1.isSuccess() && br1.getResultList() != null && br1.getResultList().size() > 0) {
                        adapter.addLast(br1.getResultList());
                        isLoading = false;
                    }
                    if (adapter.getCount() == 0){
                        list_is_empty_ll.setVisibility(View.VISIBLE);
                    }else{
                        list_is_empty_ll.setVisibility(View.GONE);
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
            page++;
            requestEventList(false);
        } else {
            pull_refresh_view.onFooterRefreshComplete();
        }
    }

    public static void show(Context context, String aid, String type){
        Bundle data = new Bundle();
        data.putString("aid", aid);
        data.putString("type", type);
        BaseActivity.showActivity(context, ReplyActivity.class, data);
    }
}
