package com.tourism.app.activity.guides;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesSettingCoverAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.util.DateUtils;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jzy on 16/5/7.
 */
public class GuidesSettingCoverActivity extends BaseActivity{
    private ImageView guides_vover_iv;
    private TextView guides_img_city_tv;
    private TextView guides_img_date_tv;
    private GridView gridView;
    private GuidesSettingCoverAdapter adapter;

    private GuidesVO vo;


    private GuidesDao guidesDao;
    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;
    private GuidesNotedDao guidesNotedDao;

    private List<GuidesNotedVO> dataList = new ArrayList<GuidesNotedVO>();

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_setting_cover);
    }

    @Override
    public void init() {
        vo = (GuidesVO) getIntent().getExtras().getSerializable("vo");
        guidesDao = new GuidesDao(context);
        guidesDayDao = new GuidesDayDao(context);
        guidesLocationDao = new GuidesLocationDao(context);
        guidesNotedDao = new GuidesNotedDao(context);
        vo = guidesDao.getById(vo.getLocal_id());
    }

    @Override
    public void initView() {
        guides_vover_iv = (ImageView) findViewById(R.id.guides_vover_iv);
        guides_img_city_tv = (TextView) findViewById(R.id.guides_img_city_tv);
        guides_img_date_tv = (TextView) findViewById(R.id.guides_img_date_tv);
        gridView = (GridView) findViewById(R.id.gridView);
    }

    @Override
    public void initListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GuidesNotedVO notedVO = (GuidesNotedVO) gridView.getItemAtPosition(position);
                if (notedVO != null){
                    LoadLocalImageUtil.getInstance().displayFromSDCard(notedVO.getPath(), guides_vover_iv, null);
                    vo.setFront_cover_photo_id(notedVO.getLocal_id());
                    vo.setPhotoVO(notedVO);
                    guidesDao.update(vo);
                    gridView.invalidateViews();
                }
            }
        });
    }

    @Override
    public void initValue() {
        setNavigationTitle("设置游记封面");

        initData();

        guides_img_city_tv.setText(vo.getName());
        if (!TextUtils.isEmpty(vo.getStart_date()))
            guides_img_date_tv.setText(vo.getStart_date() + " / " + DateUtils.getIntervalDays(vo.getStart_date(), vo.getEnd_date(), DateUtils.parsePatterns[1]) + "天 / " + vo.getPhotos_count() + "张");
        else
            guides_img_date_tv.setText("");

        if (vo.getPhotoVO() != null)
            LoadLocalImageUtil.getInstance().displayFromSDCard(vo.getPhotoVO().getPath(), guides_vover_iv, options);

        adapter = new GuidesSettingCoverAdapter(context, gridView);
        adapter.addLast(dataList, false);
        adapter.setGuidesVO(vo);
        gridView.setAdapter(adapter);

    }

    /**
     * 初始化数据
     */
    private void initData(){
        vo = guidesDao.getById(vo.getLocal_id());
        vo.setPhotoVO(guidesNotedDao.getById(vo.getFront_cover_photo_id()));

        String key = "parent_id";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(key, vo.getLocal_id());
        vo.setTrip_days(guidesDayDao.getByParams(params));
        for (int i = 0; i < vo.getTrip_days().size();i ++){
            params.put(key, vo.getTrip_days().get(i).getLocal_id());
            vo.getTrip_days().get(i).setLocations(guidesLocationDao.getByParams(params));
        }

        for (int i = 0; i < vo.getTrip_days().size(); i++){
            for (int j = 0; j < vo.getTrip_days().get(i).getLocations().size(); j++){
                params.put(key, vo.getTrip_days().get(i).getLocations().get(j).getLocal_id());
                params.put("type", "pic");
                dataList.addAll(guidesNotedDao.getByParams(params));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigation_left_btn:
                finish();
                setResult(RESULT_OK);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            setResult(RESULT_OK);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
