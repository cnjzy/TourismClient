package com.tourism.app.activity.guides;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesCategoryAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.GuidesCategoryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/5/16.
 */
public class GuidesCategoryActivity extends BaseActivity {
    private final int REQUEST_GET_COLLECTION_CODE = 10001;


    private ListView listView;
    private GuidesCategoryAdapter adapter;
    private List<GuidesCategoryVO> dataList;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_category);
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GuidesCategoryVO vo = (GuidesCategoryVO) listView.getItemAtPosition(position);
                if (vo != null){
                    Intent intent = new Intent();
                    intent.putExtra("vo", vo);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void initValue() {
        setNavigationTitle("分类列表");

        requestCategoryList();
    }

    /**
     * 获取游记收藏列表信息
     */
    public void requestCategoryList() {
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        startHttpRequest(Constants.HTTP_GET, Constants.URL_GUIDES_CATEGORY,
                parameter, true, REQUEST_GET_COLLECTION_CODE);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        super.onCallbackFromThread(resultJson, resultCode);
        try {
            BaseResponseMessage brm = new BaseResponseMessage();
            switch (resultCode) {
                case REQUEST_GET_COLLECTION_CODE:
                    brm.parseResponse(resultJson, new TypeToken<List<GuidesCategoryVO>>() {
                    });
                    if (brm.isSuccess()) {
                        dataList  = (List<GuidesCategoryVO>) brm.getResultList();
                        adapter = new GuidesCategoryAdapter(context, listView);
                        adapter.addLast(dataList, false);
                        listView.setAdapter(adapter);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
