package com.tourism.app.activity.guides;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.vo.GuidesCategoryVO;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.widget.view.SwitchButton;

/**
 * Created by Jzy on 16/5/7.
 */
public class GuidesSettingActivity extends BaseActivity {
    private View item_icon_ll;
    private ImageView item_icon_iv;
    private View item_name_ll;
    private TextView item_name_tv;
    private View item_open_ll;
    private SwitchButton item_open_tv;
    private View item_category_ll;
    private TextView item_category_tv;

    private GuidesVO guidesVO;

    public DisplayImageOptions bgOptions;

    private GuidesDao guidesDao;
    private GuidesNotedDao guidesNotedDao;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_setting);
    }

    @Override
    public void init() {
        guidesDao = new GuidesDao(context);
        guidesNotedDao = new GuidesNotedDao(context);

        bgOptions = new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.img_default_horizon)
                .showImageForEmptyUri(R.drawable.img_default_horizon)
                .showImageOnFail(R.drawable.img_default_horizon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();
    }

    @Override
    public void initView() {
        item_icon_ll = findViewById(R.id.item_icon_ll);
        item_icon_iv = (ImageView) findViewById(R.id.item_icon_iv);
        item_name_ll = findViewById(R.id.item_name_ll);
        item_name_tv = (TextView) findViewById(R.id.item_name_tv);
        item_open_ll = findViewById(R.id.item_open_ll);
        item_open_tv = (SwitchButton) findViewById(R.id.item_switch_sb);
        item_category_ll = findViewById(R.id.item_category_ll);
        item_category_tv = (TextView) findViewById(R.id.item_category_tv);
    }

    @Override
    public void initListener() {
        item_icon_ll.setOnClickListener(this);
        item_name_ll.setOnClickListener(this);
        item_open_ll.setOnClickListener(this);
        item_category_ll.setOnClickListener(this);

        item_open_tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    guidesVO.setPrivacy(1);
                }else{
                    guidesVO.setPrivacy(0);
                }
                guidesDao.update(guidesVO);
            }
        });
    }

    @Override
    public void initValue() {
        setNavigationTitle("设置游记");

        guidesVO = (GuidesVO) getIntent().getExtras().getSerializable("vo");
        guidesVO = guidesDao.getById(guidesVO.getLocal_id());
        guidesVO.setPhotoVO(guidesNotedDao.getById(guidesVO.getFront_cover_photo_id()));

        if (guidesVO.getPhotoVO() != null)
            LoadLocalImageUtil.getInstance().displayFromSDCard(guidesVO.getPhotoVO().getPath(), item_icon_iv, bgOptions);
        item_name_tv.setText(guidesVO.getName());
        item_open_tv.setChecked(guidesVO.getPrivacy() == 0 ? false : true);
        item_category_tv.setText(TextUtils.isEmpty(guidesVO.getFriend_category_name()) ? "未设置" : guidesVO.getFriend_category_name());
    }

    @Override
    public void onResume() {
        initValue();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle data = new Bundle();
        data.putSerializable("vo", guidesVO);
        switch (v.getId()){
            case R.id.item_icon_ll:
                showActivityForResult(context, GuidesSettingCoverActivity.class, 98, data);
                break;
            case R.id.item_name_ll:
                DialogUtil.showReplyDialog(context, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        guidesVO.setName(o.toString());
                        guidesDao.update(guidesVO);
                        item_name_tv.setText(o.toString());
                    }
                }, "请输入标题");
                break;
            case R.id.item_open_ll:
                break;
            case R.id.item_category_ll:
                showActivityForResult(context, GuidesCategoryActivity.class, 99, null);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK){
            GuidesCategoryVO categoryVO = (GuidesCategoryVO) data.getSerializableExtra("vo");
            guidesVO.setFriend_category_id(categoryVO.getId());
            guidesVO.setFriend_category_name(categoryVO.getName());
            guidesDao.update(guidesVO);
            item_category_tv.setText(categoryVO.getName());
        }
    }
}
