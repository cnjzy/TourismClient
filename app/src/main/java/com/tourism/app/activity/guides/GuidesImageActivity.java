package com.tourism.app.activity.guides;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.util.StringUtil;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;

public class GuidesImageActivity extends BaseActivity {
    private ImageView user_avatar_icon_iv;
    private TextView guides_reply_tv;
    private Button guides_add_reply_btn;

    private GuidesVO vo;
    private GuidesNotedVO notedVO;
    private GuidesNotedDao notedDao;
    private GuidesDao guidesDao;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_image);
    }

    @Override
    public void init() {
        notedDao = new GuidesNotedDao(context);
        guidesDao = new GuidesDao(context);
        vo = (GuidesVO) getIntent().getExtras().getSerializable("vo");
        notedVO = (GuidesNotedVO) getIntent().getExtras().getSerializable("notedVO");
        notedVO = notedDao.getById(notedVO.getLocal_id());
    }

    @Override
    public void initView() {
        user_avatar_icon_iv = (ImageView) findViewById(R.id.user_avatar_icon_iv);
        guides_reply_tv = (TextView) findViewById(R.id.guides_reply_tv);
        guides_add_reply_btn = (Button) findViewById(R.id.guides_add_reply_btn);
    }

    @Override
    public void initListener() {
        guides_reply_tv.setOnClickListener(this);
        guides_add_reply_btn.setOnClickListener(this);
    }

    @Override
    public void initValue() {
        setNavigationTitle("旅行胶卷");
        setNavigationRightButton(View.VISIBLE, -1, R.drawable.ico_gd_d);

        LoadLocalImageUtil.getInstance().displayFromSDCard(notedVO.getPath(), user_avatar_icon_iv, null);
        if (!TextUtils.isEmpty(notedVO.getDescription())) {
            guides_reply_tv.setText(notedVO.getDescription());
            guides_add_reply_btn.setVisibility(View.GONE);
            guides_reply_tv.setVisibility(View.VISIBLE);
        } else {
            guides_add_reply_btn.setVisibility(View.VISIBLE);
            guides_reply_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_left_btn:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.guides_add_reply_btn:
                DialogUtil.showReplyDialog(this, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        if (o != null && !TextUtils.isEmpty(o.toString())) {
                            guides_reply_tv.setText(o.toString());
                            guides_add_reply_btn.setVisibility(View.GONE);
                            guides_reply_tv.setVisibility(View.VISIBLE);
                            notedVO.setDescription(o.toString());
                        } else {
                            guides_add_reply_btn.setVisibility(View.VISIBLE);
                            guides_reply_tv.setVisibility(View.GONE);
                            notedVO.setDescription("");
                        }
                        notedDao.update(notedVO);
                    }
                });
                break;
            case R.id.guides_reply_tv:
                DialogUtil.showReplyDialog(this, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        if (o != null && !TextUtils.isEmpty(o.toString())) {
                            guides_reply_tv.setText(o.toString());
                            guides_add_reply_btn.setVisibility(View.GONE);
                            guides_reply_tv.setVisibility(View.VISIBLE);
                            notedVO.setDescription(o.toString());
                        } else {
                            guides_add_reply_btn.setVisibility(View.VISIBLE);
                            guides_reply_tv.setVisibility(View.GONE);
                            notedVO.setDescription("");
                        }
                        notedDao.update(notedVO);
                    }
                });
                break;
            case R.id.navigation_right_btn:
                DialogUtil.showGuidesImageDialog(context, new DialogUtil.OnCallbackListener() {
                    @Override
                    public void onClick(int whichButton, Object o) {
                        switch (whichButton) {
                            case 0:
                                DialogUtil.showDateAlert(context, new DialogUtil.OnDateSelectedListener() {
                                    @Override
                                    public void onSelected(int year, int month, int day) {
                                        String dateStr = year + "-" + StringUtil.formatNumber(month) + "-" + StringUtil.formatNumber(day);
                                        // 删除之前的
                                        notedDao.delete(notedVO);

                                        // 新加节点
                                        notedDao.addNotedByDate(dateStr, vo, notedVO);
                                    }
                                }, "选择日期").show();
                                break;
                            case 1:
                                notedDao.delete(notedVO);
                                // 新加节点
                                notedDao.addNotedByDate(notedVO.getDate(), vo, notedVO);
                                break;
                            case 2:
                                notedDao.delete(notedVO);
                                vo.setPhotos_count(vo.getPhotos_count() - 1);
                                guidesDao.update(vo);
                                break;
                        }
                    }
                });
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
