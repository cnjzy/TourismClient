package com.tourism.app.activity.guides;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.util.DateUtils;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.PhotoManager;
import com.tourism.app.util.json.StrategyJson;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;

import java.io.File;
import java.util.HashMap;

public class GuidesEditActivity extends BaseActivity {


    private PhotoManager photoMgr;
    private Bitmap bmp;

    private GuidesVO vo;

    private GuidesDao guidesDao;
    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;
    private GuidesNotedDao guidesNotedDao;


    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_add_info);
    }

    @Override
    public void init() {
        vo = (GuidesVO) getIntent().getExtras().getSerializable("vo");
        guidesDao = new GuidesDao(context);
        guidesDayDao = new GuidesDayDao(context);
        guidesLocationDao = new GuidesLocationDao(context);
        guidesNotedDao = new GuidesNotedDao(context);
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
        setNavigationTitle("编辑游记");

        photoMgr = new PhotoManager(context);

        initData();
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
                vo.getTrip_days().get(i).getLocations().get(j).setNotes(guidesNotedDao.getByParams(params));
            }
        }

        // 删除无用信息
        for (int i = 0; i < vo.getTrip_days().size(); i++){
            // 如果每天节点里面地理位置为空，那么删除
            if (vo.getTrip_days().get(i).getLocations().size() == 0){
                guidesDayDao.deleteById(vo.getTrip_days().get(i).getLocal_id());
                vo.getTrip_days().remove(i);
                i--;
            }
        }

        System.out.println("=====" + StrategyJson.getGuiesJson(vo));
    }

    @Override
    public void onClick(View v) {
        final Bundle data = new Bundle();
        data.putSerializable("vo", vo);
        super.onClick(v);
        switch (v.getId()) {
            case R.id.navigation_show_btn:
                showActivityForResult(context, GuidesDetailActivity.class, 89, data);
                break;
            case R.id.navigation_right_btn:
                DialogUtil.showGuidesListDialog(context, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        Bundle data = new Bundle();
                        data.putSerializable("vo", vo);
                        switch (whichButton) {
                            case 0:
                                showActivity(context, GuidesLocationSortActivity.class, data);
                                break;
                            case 1:
                                showActivity(context, GuidesSettingActivity.class, data);
                                break;
                            case 2:
                                showActivity(context, GuidesSyncActivity.class, data);
                                break;
                            case 3:
                                guidesDao.delete(vo);
                                setResult(RESULT_OK);
                                finish();
                                break;
                        }
                    }
                });
                break;
            case R.id.foot_bar_noted_btn:
                showActivityForResult(context, GuidesAddNotedActivity.class, 90, data);
                break;
            case R.id.foot_bar_camera_btn:
                DialogUtil.showGuidesCameraDialog(context, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        if (whichButton == 0) {
                            photoMgr.getPhotoByCamera(context, "img.png");
                        } else {
                            showActivityForResult(context, GuidesGalleryActivity.class, 91, data);
                        }
                    }
                });
                break;
            case R.id.foot_bar_location_btn:
                showActivityForResult(context, GuidesLocationSearchActivity.class, 92, data);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        releaseBmp();
        bmp = photoMgr.onActivityResult(context, requestCode, resultCode, data);
        if(bmp != null){
            File file = new File(photoMgr.getFilePath());
            String imageDate = DateUtils.getDateByLong(file.lastModified(), DateUtils.parsePatterns[1]);

            GuidesNotedVO guidesNotedVO = new GuidesNotedVO();
            guidesNotedVO.setIs_image_upload(false);
            guidesNotedVO.setPath(photoMgr.getFilePath());
            guidesNotedVO.setDate(imageDate);
            guidesNotedVO.setType("pic");
            guidesNotedDao.addNoted(guidesNotedVO, vo);
        }

        if(resultCode == RESULT_OK){
            initData();
        }
    }


    /**
     * 释放bitmap
     */
    private void releaseBmp(){
        if(bmp != null && !bmp.isRecycled()){
            bmp.recycle();
            bmp = null;
        }
    }

    @Override
    protected void onDestroy() {
        releaseBmp();
        super.onDestroy();
    }
}
