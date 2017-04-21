package io.loper.wechat.sample.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.loper.wechat.sample.BroadcastAction;
import io.loper.wechat.sample.IntentKey;
import io.loper.wechat.sample.WeChat;

/**
 * @author JoongWon Baik
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle any communication from WeChat and then terminate activity. This class must be an activity
        // or the communication will not be received from WeChat.
        WXAPIFactory.createWXAPI(this, WeChat.APP_ID, false).handleIntent(getIntent(), this);
    }

    /**
     * Called when WeChat is initiating a request to your application. This is not used for
     * authentication.
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {
    }

    /**
     * Called when WeChat is responding to a request this app initiated. Invoked by WeChat after
     * authorization has been given by the user.
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK :
                Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
                if (baseResp instanceof SendAuth.Resp) {
                    sendAuthResult(((SendAuth.Resp)baseResp));
                }
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL :
                Toast.makeText(this, "User canceled the request", Toast.LENGTH_LONG).show();
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED :
                Toast.makeText(this, "User denied the request", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void sendAuthResult(SendAuth.Resp resp) {
        Intent intent = new Intent();
        intent.setAction(BroadcastAction.WE_CHAT_AUTH_RESULT);
        intent.putExtra(IntentKey.WE_CHAT_AUTH_CODE, resp.code);
        intent.putExtra(IntentKey.WE_CHAT_ERROR_CODE, resp.errCode);
        sendBroadcast(intent);

        finish();
    }
}
