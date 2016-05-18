package com.tourism.app.activity.guides;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.StringUtil;
import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.widget.view.UnderlineEditText;

/**
 * Created by Jzy on 16/5/7.
 */
public class GuidesAddNotedActivity extends BaseActivity {

    private UnderlineEditText noted_et;

    /**
     * 实体类
     */
    private GuidesVO vo;
    private GuidesDayVO guidesDayVO;
    private GuidesLocationVO locationVO;
    private GuidesNotedVO notedVO;

    /**
     * 数据库操作类
     */
    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;
    private GuidesNotedDao guidesNotedDao;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_add_noted);
    }

    @Override
    public void init() {
        notedVO = (GuidesNotedVO) getIntent().getExtras().getSerializable("notedVO");
        vo = (GuidesVO) getIntent().getExtras().getSerializable("vo");

        guidesDayDao = new GuidesDayDao(context);
        guidesLocationDao = new GuidesLocationDao(context);
        guidesNotedDao = new GuidesNotedDao(context);
    }

    @Override
    public void initView() {
        noted_et = (UnderlineEditText) findViewById(R.id.noted_et);
    }

    @Override
    public void initListener() {
        noted_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(notedVO == null) {
                    if (!TextUtils.isEmpty(s)) {
                        setNavigationRightButton(View.VISIBLE, -1, R.drawable.ico_ok);
                    } else {
                        setNavigationRightButton(View.INVISIBLE, -1, R.drawable.ico_ok);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initValue() {
        setNavigationTitle("旅行随记");
        if (notedVO != null){
            noted_et.setText(notedVO.getDescription());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.navigation_right_btn:
                if (notedVO != null){
                    DialogUtil.showGuidesNotedDialog(context, new DialogUtil.OnCallbackListener() {
                        @Override
                        public void onClick(int whichButton, Object o) {
                            switch (whichButton){
                                case 0:
                                    DialogUtil.showDateAlert(context, new DialogUtil.OnDateSelectedListener() {
                                        @Override
                                        public void onSelected(int year, int month, int day) {
                                            String dateStr = year + "-" + StringUtil.formatNumber(month) + "-" + StringUtil.formatNumber(day);

                                            // 删除之前的
                                            guidesNotedDao.delete(notedVO);

                                            // 新加节点
                                            notedVO.setParent_id(locationVO.getLocal_id());
                                            guidesNotedDao.addNotedByDate(dateStr, vo, notedVO);
                                        }
                                    }, "选择日期").show();
                                    break;
                                case 1:
                                    guidesNotedDao.delete(notedVO);
                                    setResult(RESULT_OK);
                                    finish();
                                    break;
                            }
                        }
                    });
                } else {
                    DialogUtil.showDateAlert(context, new DialogUtil.OnDateSelectedListener() {
                        @Override
                        public void onSelected(int year, int month, int day) {
                            String dateStr = year + "-" + StringUtil.formatNumber(month) + "-" + StringUtil.formatNumber(day);
                            GuidesNotedVO notedVO = new GuidesNotedVO();
                            notedVO.setType("txt");
                            notedVO.setDescription(noted_et.getText().toString());
                            notedVO.setDate(dateStr);
                            notedVO.setRank(0);
                            guidesNotedDao.addNotedByDate(dateStr, vo, notedVO);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, "选择日期").show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {

        if (notedVO != null) {
            notedVO.setDescription(noted_et.getText().toString());
            guidesNotedDao.update(notedVO);
        }
        super.onDestroy();
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
