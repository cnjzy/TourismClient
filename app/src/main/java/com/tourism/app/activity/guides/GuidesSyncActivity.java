package com.tourism.app.activity.guides;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesDeleteDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.json.StrategyJson;
import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesDeleteVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesResultVO;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.vo.UserInfoVO;
import com.tourism.app.widget.view.CustomRoundProgress;

import org.json.simple.BaseJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jzy on 16/5/13.
 */
public class GuidesSyncActivity extends BaseActivity {

    private final int REQUEST_SYNC_DATA_CODE = 10001;
    private final int REQUEST_SYNC_IMAGE_CODE = 10002;

    private CustomRoundProgress sync_progress_rg;
    private TextView sync_content_tv;
    private Button sync_guides_btn;

    /**
     * 上传图片总数
     */
    private int uploadImageCount = 0;
    /**
     * 已经上传数量
     */
    private int alreadyUploadImageCount = 0;

    /**
     * 游记对象
     */
    private GuidesVO vo;
    private List<GuidesDeleteVO> deleteList;
    private List<GuidesNotedVO> notedList = new ArrayList<GuidesNotedVO>();


    private GuidesDao guidesDao;
    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;
    private GuidesNotedDao guidesNotedDao;
    private GuidesDeleteDao guidesDeleteDao;

    /**
     * 是否正在上传
     */
    private boolean isUpload = true;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_sync);
    }

    @Override
    public void init() {
        vo = (GuidesVO) getIntent().getExtras().getSerializable("vo");

        guidesDao = new GuidesDao(context);
        guidesDayDao = new GuidesDayDao(context);
        guidesLocationDao = new GuidesLocationDao(context);
        guidesNotedDao = new GuidesNotedDao(context);
        guidesDeleteDao = new GuidesDeleteDao(context);
    }

    @Override
    public void initView() {
        sync_progress_rg = (CustomRoundProgress) findViewById(R.id.sync_progress_rg);
        sync_content_tv = (TextView) findViewById(R.id.sync_content_tv);
        sync_guides_btn = (Button) findViewById(R.id.sync_guides_btn);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initValue() {
        setNavigationTitle("同步游记");

        sync_progress_rg.setMaxProgress(vo.getPhotos_count());

        DialogUtil.showGuidesSyncDialog(context, new DialogUtil.OnCallbackListener() {
            public void onClick(int whichButton, Object o) {
                switch (whichButton){
                    case 0:
                        initData();
                        if (vo.is_upload() == 0) {
                            requesSyncData();
                        } else if (vo.is_upload() == 1) {
                            if (notedList.size() > 0)
                                requesSyncImage(notedList.get(0));
                        }
                        break;
                    case 1:
                        initData();
                        if (vo.is_upload() == 0) {
                            requesSyncData();
                        } else if (vo.is_upload() == 1) {
                            if (notedList.size() > 0)
                                requesSyncImage(notedList.get(0));
                        }
                        break;
                    case 2:
                        finish();
                        break;
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        vo = guidesDao.getById(vo.getLocal_id());
        vo.setPhotoVO(guidesNotedDao.getById(vo.getFront_cover_photo_id()));

        String key = "parent_id";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(key, vo.getLocal_id());
        vo.setTrip_days(guidesDayDao.getByParams(params));
        for (int i = 0; i < vo.getTrip_days().size(); i++) {
            params.put(key, vo.getTrip_days().get(i).getLocal_id());
            vo.getTrip_days().get(i).setLocations(guidesLocationDao.getByParams(params));
        }

        notedList.clear();
        for (int i = 0; i < vo.getTrip_days().size(); i++) {
            for (int j = 0; j < vo.getTrip_days().get(i).getLocations().size(); j++) {
                params.put(key, vo.getTrip_days().get(i).getLocations().get(j).getLocal_id());
                vo.getTrip_days().get(i).getLocations().get(j).setNotes(guidesNotedDao.getByParams(params));
                notedList.addAll(vo.getTrip_days().get(i).getLocations().get(j).getNotes());

                // 设置排序，文字排在最上面
                for (int m = vo.getTrip_days().get(i).getLocations().get(j).getNotes().size()-1; m >= 0; m--){
                    if (vo.getTrip_days().get(i).getLocations().get(j).getNotes().get(m).getType().equals("txt")){
                        GuidesNotedVO notedVO = vo.getTrip_days().get(i).getLocations().get(j).getNotes().get(m);
                        vo.getTrip_days().get(i).getLocations().get(j).getNotes().remove(m);
                        vo.getTrip_days().get(i).getLocations().get(j).getNotes().add(0, notedVO);
                    }
                }
            }
        }

        // 获取所有图片数量
        for (int i = 0; i < notedList.size(); i++) {
            if (notedList.get(i).getType().equals("txt") || notedList.get(i).is_image_upload()) {
                notedList.remove(i);
                i--;
            }
        }

        // 获取已经删除的数据
        deleteList = guidesDeleteDao.getByParams(null);

        System.out.println("=====" + StrategyJson.getGuiesJson(vo));
        System.out.println("=====" + StrategyJson.getDeleteJson(deleteList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sync_guides_btn:
                if (isUpload) {
                    cancelRequest();
                    sync_guides_btn.setText("开始同步游记");
                } else {
                    initValue();
                    sync_guides_btn.setText("停止同步游记");
                }
                isUpload = !isUpload;
                break;
            case R.id.navigation_left_btn:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    /**
     * 同步数据
     */
    public void requesSyncData() {
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        parameter.add(new RequestParameter("maps", StrategyJson.getGuiesJson(vo).toString()));
        parameter.add(new RequestParameter("delete", StrategyJson.getDeleteJson(deleteList).toString()));
        startHttpRequest(Constants.HTTP_POST, Constants.URL_SYNC_GUIDES_DATA, parameter, false, REQUEST_SYNC_DATA_CODE);
    }

    /**
     * 同步图片
     */
    public void requesSyncImage(GuidesNotedVO notedVO) {
        LogUtil.i("上传图片", notedVO.toString());
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("Filedata", notedVO.getPath(), true));
        parameter.add(new RequestParameter("note_id", notedVO.getServer_id()));
        parameter.add(new RequestParameter("front_cover", notedVO.getLocal_id() == vo.getFront_cover_photo_id() ? 1 : 0));
        startHttpRequest(Constants.HTTP_FILE, Constants.URL_SYNC_GUIDES_IMAGE, parameter, false, REQUEST_SYNC_IMAGE_CODE);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        super.onCallbackFromThread(resultJson, resultCode);
        try {
            BaseResponseMessage brm = new BaseResponseMessage();
            switch (resultCode) {
                case REQUEST_SYNC_DATA_CODE:
                    brm.parseResponse(resultJson, new TypeToken<UserInfoVO>() {});
                    if (brm.isSuccess()) {

                        JSONParser parser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) parser.parse(resultJson);
                        if (jsonObject.containsKey("sync_mappings")){
                            String mapStr = jsonObject.get("sync_mappings").toString();
                            List<GuidesResultVO> resultList = (List<GuidesResultVO>) BaseJson.parser(new TypeToken<List<GuidesResultVO>>() {}, mapStr);

                            for (int i = 0; resultList != null && i < resultList.size(); i++){
                                GuidesResultVO resultVO = resultList.get(i);
                                if (resultVO.getType().equals("trip")){
                                    //游记
                                    GuidesVO guidesVO = guidesDao.getById(resultVO.getLocal_id());
                                    guidesVO.setServer_id(resultVO.getServer_id());
                                    guidesVO.setIs_upload(1);
                                    vo = guidesVO;
                                    guidesDao.update(guidesVO);
                                }else if (resultVO.getType().equals("trip_days")){
                                    // 游记天
                                    GuidesDayVO guidesDayVO = guidesDayDao.getById(resultVO.getLocal_id());
                                    guidesDayVO.setServer_id(resultVO.getServer_id());
                                    guidesDayDao.update(guidesDayVO);
                                }else if (resultVO.getType().equals("locations")){
                                    // 游记地点
                                    GuidesLocationVO guidesLocationVO = guidesLocationDao.getById(resultVO.getLocal_id());
                                    guidesLocationVO.setServer_id(resultVO.getServer_id());
                                    guidesLocationDao.update(guidesLocationVO);
                                }else if (resultVO.getType().equals("notes")){
                                    // 游记节点
                                    GuidesNotedVO guidesNotedVO = guidesNotedDao.getById(resultVO.getLocal_id());
                                    guidesNotedVO.setServer_id(resultVO.getServer_id());
                                    guidesNotedDao.update(guidesNotedVO);
                                }
                            }
                        }
                        // 修改上传同步信息
//                        vo.setIs_upload(1);
//                        guidesDao.update(vo);

                        // 重新读取数据
                        initData();

                        // 开始同步图片
                        if (notedList.size() > 0)
                            requesSyncImage(notedList.get(0));
                        else{
                            // 修改上传同步信息
                            vo.setIs_upload(2);
                            guidesDao.update(vo);
                        }
                    }
                    break;
                case REQUEST_SYNC_IMAGE_CODE:
                    brm.parseResponse(resultJson, new TypeToken<UserInfoVO>() {});
                    if (brm.isSuccess()) {
                        //开始同步下一个
                        // 设置已经上传数量
                        vo.setAlready_photos_count(vo.getAlready_photos_count() + 1);
                        // 更新已上传数量
                        guidesDao.update(vo);
                        // 设置progress
                        sync_progress_rg.setProgress(vo.getAlready_photos_count());
                        // 设置当前图片图片上传状态
                        notedList.get(0).setIs_image_upload(true);
                        // 更新当前图片上传状态
                        guidesNotedDao.update(notedList.get(0));
                        // 从队列里删除已上传图片
                        notedList.remove(0);
                        // 如果有未上传图片，继续上传
                        if (notedList.size() > 0)
                            requesSyncImage(notedList.get(0));
                        else{
                            // 修改上传同步信息
                            vo.setIs_upload(2);
                            guidesDao.update(vo);
                            sync_progress_rg.setProgress(vo.getPhotos_count());
                            sync_content_tv.setText("上传完成");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
