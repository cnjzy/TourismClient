package com.tourism.app.activity.poolfriend;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.user.UserLoginActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.StrategyVO;

import java.util.ArrayList;
import java.util.List;

public class CategoryImageActivity extends BaseActivity {
    private final int REQUEST_PHOTOLIKE_KEY = 10004;

    private ImageButton navigation_like_btn;
    private ImageView user_avatar_icon_iv;
    private TextView guides_reply_tv;

    private StrategyVO vo;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_category_image);
    }

    @Override
    public void init() {
        vo = (StrategyVO) getIntent().getExtras().getSerializable("vo");
    }

    @Override
    public void initView() {
        user_avatar_icon_iv = (ImageView) findViewById(R.id.user_avatar_icon_iv);
        guides_reply_tv = (TextView) findViewById(R.id.guides_reply_tv);
        navigation_like_btn = (ImageButton) findViewById(R.id.navigation_like_btn);
    }

    @Override
    public void initListener() {
        navigation_like_btn.setOnClickListener(this);
    }

    @Override
    public void initValue() {
        setNavigationTitle("旅行胶卷");
        setNavigationRightButton(View.VISIBLE, -1, R.drawable.ico_gd_d);

        if (vo != null){
            if (vo.getPhoto() != null && !TextUtils.isEmpty(vo.getPhoto().getWidth()) && !TextUtils.isEmpty(vo.getPhoto().getHeight())){
                ImageLoader.getInstance().displayImage(vo.getPhoto().getUrl(), user_avatar_icon_iv, options, animateFirstListener);
            }
            guides_reply_tv.setText(vo.getDescription());
            if (vo.getCurrent_user_favorite() == 0){
                navigation_like_btn.setImageResource(R.drawable.icon_xh_d);
            }else{
                navigation_like_btn.setImageResource(R.drawable.icon_xh_e);
            }
        }
    }

    /**
     * 收藏活动
     */
    public void requestLikePhoto(String photoid, String action) {
        if (isUserEmpty()){
            showActivity(context, UserLoginActivity.class);
            return;
        }
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        parameter.add(new RequestParameter("photoid", photoid));
        parameter.add(new RequestParameter("action", action));
        startHttpRequest(Constants.HTTP_POST, Constants.URL_LIKE_PHOTO, parameter, false,
                REQUEST_PHOTOLIKE_KEY);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.navigation_like_btn:
                if(vo.getPhoto() != null) {
                    requestLikePhoto(vo.getPhoto().getId() + "", vo.getCurrent_user_favorite() == 0 ? "on" : "off");
                }
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

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        // TODO Auto-generated method stub
        super.onCallbackFromThread(resultJson, resultCode);
        try {
            switch (resultCode) {
                case REQUEST_PHOTOLIKE_KEY:
                    BaseResponseMessage br1 = new BaseResponseMessage();
                    br1.parseResponse(resultJson, new TypeToken<StrategyVO>() {});
                    if (vo.getCurrent_user_favorite() == 0){
                        vo.setCurrent_user_favorite(1);
                        navigation_like_btn.setImageResource(R.drawable.icon_xh_e);
                    }else{
                        vo.setCurrent_user_favorite(0);
                        navigation_like_btn.setImageResource(R.drawable.icon_xh_d);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
