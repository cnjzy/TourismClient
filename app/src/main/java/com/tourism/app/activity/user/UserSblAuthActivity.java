package com.tourism.app.activity.user;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.UserInfoVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzy on 16/6/5.
 */
public class UserSblAuthActivity extends BaseActivity {
    private final int REQUEST_GET_USER_INFO_CODE = 10001;

    private EditText login_user_et;
    private EditText login_password_et;
    private Button login_submit_btn;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(login_user_et.getText()) || TextUtils.isEmpty(login_password_et.getText())){
                login_submit_btn.setOnClickListener(null);
                login_submit_btn.setBackgroundResource(R.drawable.btn_gray_round);
            }else{
                login_submit_btn.setOnClickListener(UserSblAuthActivity.this);
                login_submit_btn.setBackgroundResource(R.drawable.btn_blue_round);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_user_sbl_auth);
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        login_user_et = (EditText) findViewById(R.id.login_user_et);
        login_password_et = (EditText) findViewById(R.id.login_password_et);
        login_submit_btn = (Button) findViewById(R.id.login_submit_btn);
    }

    @Override
    public void initListener() {
        login_submit_btn.setOnClickListener(this);

        login_user_et.addTextChangedListener(textWatcher);
        login_password_et.addTextChangedListener(textWatcher);
    }

    @Override
    public void initValue() {
        setNavigationTitle("斯巴鲁VIP认证");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_submit_btn:
                String userName = login_user_et.getText().toString();
                String code = login_password_et.getText().toString();
                requestUserInfo(userName, code);
                break;
        }
    }

    /**
     * 获取用户信息
     */
    public void requestUserInfo(String userName, String code) {
        List<RequestParameter> parameter = new ArrayList<RequestParameter>();
        parameter.add(new RequestParameter("token", getUserInfo().getToken()));
        parameter.add(new RequestParameter("userName", userName));
        parameter.add(new RequestParameter("code", code));
        startHttpRequest(Constants.HTTP_POST, Constants.URL_SET_USER_VIP,
                parameter, true, REQUEST_GET_USER_INFO_CODE);
    }

    @Override
    public void onCallbackFromThread(String resultJson, int resultCode) {
        super.onCallbackFromThread(resultJson, resultCode);
        try{
            switch (resultCode){
                case REQUEST_GET_USER_INFO_CODE:
                    BaseResponseMessage brm = new BaseResponseMessage();
                    brm.parseResponse(resultJson, new TypeToken<UserInfoVO>() {
                    });
                    if (brm.isSuccess()){
                        showToast("提交申请成功");
                        finish();
                    }
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
