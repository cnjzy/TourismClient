package com.tourism.app.activity.guides;

import android.view.View;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.PhotoManager;
import com.tourism.app.vo.GuidesVO;

public class GuidesAddInfoActivity extends BaseActivity {

    private PhotoManager photoMgr;

    private GuidesVO vo;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_add_info);
    }

    @Override
    public void init() {
        vo = (GuidesVO) getIntent().getExtras().getSerializable("vo");
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initValue() {
        photoMgr = new PhotoManager(context);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.navigation_show_btn:
                showActivity(context, GuidesDetailActivity.class);
                break;
            case R.id.navigation_right_btn:
                DialogUtil.showGuidesListDialog(context, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        switch (whichButton){
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                        }
                    }
                });
                break;
            case R.id.foot_bar_noted_btn:
                showActivity(context, GuidesAddNotedActivity.class);
                break;
            case R.id.foot_bar_camera_btn:
                DialogUtil.showGuidesCameraDialog(context, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        if (whichButton == 0) {
                            photoMgr.getPhotoByCamera(context, "img.png");
                        } else {
                            showActivity(context, GuidesGalleryActivity.class);
                        }
                    }
                });
                break;
            case R.id.foot_bar_location_btn:
                showActivity(context, GuidesLocationSearchActivity.class);
                break;
        }
    }
}
