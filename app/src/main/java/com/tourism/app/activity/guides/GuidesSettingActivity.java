package com.tourism.app.activity.guides;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;

/**
 * Created by Jzy on 16/5/7.
 */
public class GuidesSettingActivity extends BaseActivity {
    private View item_icon_ll;
    private ImageView item_icon_iv;
    private View item_name_ll;
    private TextView item_name_tv;
    private View item_open_ll;
    private TextView item_open_tv;
    private View item_category_ll;
    private TextView item_category_tv;


    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_setting);
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        item_icon_ll = findViewById(R.id.item_icon_ll);
        item_icon_iv = (ImageView) findViewById(R.id.item_icon_iv);
        item_name_ll = findViewById(R.id.item_name_ll);
        item_name_tv = (TextView) findViewById(R.id.item_name_tv);
        item_open_ll = findViewById(R.id.item_open_ll);
        item_open_tv = (TextView) findViewById(R.id.item_open_tv);
        item_category_ll = findViewById(R.id.item_category_ll);
        item_category_tv = (TextView) findViewById(R.id.item_category_tv);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initValue() {

    }
}
