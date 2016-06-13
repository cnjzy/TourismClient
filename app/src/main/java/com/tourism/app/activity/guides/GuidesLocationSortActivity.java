package com.tourism.app.activity.guides;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesLocationSortAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jzy on 16/5/13.
 */
public class GuidesLocationSortActivity extends BaseActivity {
    private DragSortListView listView;
    private DragSortController mController;
    private GuidesLocationSortAdapter adapter;

    //监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                    if (from != to) {
                        GuidesLocationVO vo = (GuidesLocationVO) adapter.getItem(from);//得到listview的适配器
                        adapter.removeItem(from);//在适配器中”原位置“的数据。
                        adapter.insertItem(vo, to);//在目标位置中插入被拖动的控件。
                    }
                }
            };


    private GuidesVO vo;

    private GuidesDao guidesDao;
    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;
    private GuidesNotedDao guidesNotedDao;

    private List<GuidesLocationVO> locationList = new ArrayList<GuidesLocationVO>();

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_location_sort);
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
        listView = (DragSortListView) findViewById(R.id.listView);
    }

    @Override
    public void initListener() {
        //得到滑动listview并且设置监听器。
        listView.setDropListener(onDrop);
    }

    @Override
    public void initValue() {
        setNavigationTitle("地理位置排序");

        initData();

        mController = buildController(listView);
        listView.setFloatViewManager(mController);
        listView.setOnTouchListener(mController);
        listView.setDragEnabled(true);

        adapter = new GuidesLocationSortAdapter(context, listView);
        adapter.addLast(locationList, false);
        listView.setAdapter(adapter);
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
            GuidesLocationVO dayVO = new GuidesLocationVO();
            dayVO.setIsDate(true);
            dayVO.setDateStr(vo.getTrip_days().get(i).getTrip_date());
            dayVO.setLocal_id(vo.getTrip_days().get(i).getLocal_id());
            locationList.add(dayVO);

            params.put(key, vo.getTrip_days().get(i).getLocal_id());
            locationList.addAll(guidesLocationDao.getByParams(params));
        }

        for (int i = 0; i < locationList.size(); i++){
            if (!locationList.get(i).isDate() && TextUtils.isEmpty(locationList.get(i).getLocation_name())){
                locationList.remove(i);
                i--;
            }
        }
    }

    private void setData(){
        locationList = adapter.getDataList();
        // 设置排序
        int parent_id = 0;
        int rank = 0;
        for (int i = 0 ; i < locationList.size(); i++){
            if (locationList.get(i).isDate()){
                parent_id = locationList.get(i).getLocal_id();
                rank = 0;
            }else{
                locationList.get(i).setParent_id(parent_id);
                locationList.get(i).setRank(rank);
                guidesLocationDao.update(locationList.get(i));
                rank++;
            }
        }
        setResult(RESULT_OK);
        finish();
    }



    public DragSortController getController() {
        return mController;
    }
    /**
     * Called in onCreateView. Override this to provide a custom
     * DragSortController.
     */
    public DragSortController buildController(DragSortListView dslv) {
        // defaults are
        //   dragStartMode = onDown
        //   removeMode = flingRight
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
//        controller.setRemoveMode(removeMode);
        return controller;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigation_left_btn:
                setData();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            setData();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
