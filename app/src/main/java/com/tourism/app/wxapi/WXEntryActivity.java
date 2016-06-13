package com.tourism.app.wxapi;


import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tourism.app.common.Constants;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    @Override
    public void onReq(BaseReq arg0) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) {
                    String code = ((SendAuth.Resp) resp).code;
                    Constants.WEICHAT_CODE = code;
                }else{
                    Toast.makeText(this, "分享成功啦", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            default:
                break;
        }

    }
}
