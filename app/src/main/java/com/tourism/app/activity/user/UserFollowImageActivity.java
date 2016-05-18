package com.tourism.app.activity.user;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.vo.UserFollowVO;

/**
 * Created by Jzy on 16/5/11.
 */
public class UserFollowImageActivity extends BaseActivity {

    private ImageView user_avatar_icon_iv;
    private Button guides_add_reply_btn;
    private TextView guides_reply_tv;

    private UserFollowVO userFollowVO;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_user_follow_image);
    }

    @Override
    public void init() {
        userFollowVO = (UserFollowVO) getIntent().getExtras().getSerializable("vo");
    }

    @Override
    public void initView() {
        user_avatar_icon_iv = (ImageView) findViewById(R.id.user_avatar_icon_iv);
        guides_add_reply_btn = (Button) findViewById(R.id.guides_add_reply_btn);
        guides_reply_tv = (TextView) findViewById(R.id.guides_reply_tv);
    }

    @Override
    public void initListener() {
        user_avatar_icon_iv.setOnClickListener(this);
    }

    @Override
    public void initValue() {
        guides_add_reply_btn.setVisibility(View.GONE);

        guides_reply_tv.setText(userFollowVO.getDescription());
        ImageLoader.getInstance().displayImage(userFollowVO.getLitpic(), user_avatar_icon_iv, options, animateFirstListener);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.user_avatar_icon_iv:
                goBack();
                break;
        }
    }
}
