package io.loper.wechat.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.annimon.stream.Optional;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.loper.wechat.sample.wxapi.WeChatLoginActivity;

/**
 * @author JoongWon Baik
 */
public class MainActivity extends AppCompatActivity {
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        Optional.ofNullable(unbinder).ifPresent(Unbinder::unbind);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ActivityReqCode.WE_CHAT_LOGIN) {
            Optional.ofNullable(data).ifPresent(intent -> {
                final String code = intent.getStringExtra(IntentKey.WE_CHAT_AUTH_CODE);
                Toast.makeText(this, code, Toast.LENGTH_LONG).show();
            });
        }
    }

    @OnClick(R.id.we_chat_login_btn)
    public void onClickWeChatLogin() {
        Intent intent = new Intent(this, WeChatLoginActivity.class);
        startActivityForResult(intent, ActivityReqCode.WE_CHAT_LOGIN);
    }
}
