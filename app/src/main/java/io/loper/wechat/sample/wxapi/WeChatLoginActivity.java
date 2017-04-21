package io.loper.wechat.sample.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.annimon.stream.Optional;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.loper.wechat.sample.BroadcastAction;
import io.loper.wechat.sample.IntentKey;
import io.loper.wechat.sample.WeChat;

/**
 * @author JoongWon Baik
 */
public class WeChatLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver();

        SendAuth.Req req = new SendAuth.Req();
        req.scope = WeChat.AUTH_SCOPE;
        req.state = WeChat.STATE;
        WXAPIFactory.createWXAPI(this, WeChat.APP_ID, false).sendReq(req);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    private void registerReceiver() {
        registerReceiver(receiver, new IntentFilter(BroadcastAction.WE_CHAT_AUTH_RESULT));
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception ignore) {}
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Optional.ofNullable(intent).ifPresent(from -> handleBroadcast(from));
        }
    };

    private void handleBroadcast(@NonNull Intent from) {
        final String action = from.getAction();
        if (BroadcastAction.WE_CHAT_AUTH_RESULT.equalsIgnoreCase(action)) {
            Intent result = new Intent();
            result.putExtra(IntentKey.WE_CHAT_AUTH_CODE, from.getStringExtra(IntentKey.WE_CHAT_AUTH_CODE));
            result.putExtra(IntentKey.WE_CHAT_ERROR_CODE, from.getStringExtra(IntentKey.WE_CHAT_ERROR_CODE));
            setResult(RESULT_OK, result);
            finish();
        }
    }
}
