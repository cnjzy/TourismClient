package com.tourism.app.activity.guides;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;

/**
 * Created by Jzy on 16/5/7.
 */
public class GuidesSettingCoverActivity extends BaseActivity{
    private ImageView guides_vover_iv;
    private TextView guides_img_city_tv;
    private TextView guides_img_date_tv;
    private GridView gridView;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_setting_cover);
    }

    @Override
    public void init() {

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

    }

    @Override
    public void initValue() {

    }
}
