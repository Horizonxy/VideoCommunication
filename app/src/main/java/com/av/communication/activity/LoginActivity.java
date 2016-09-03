package com.av.communication.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.av.communication.Constants;
import com.av.communication.R;
import com.av.communication.control.AVContextControl;
import com.av.communication.model.MySelfInfo;
import com.av.communication.util.LogUtils;
import com.av.communication.util.PermissionUtils;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.av.sdk.AVCallback;
import com.tencent.av.sdk.AVError;

public class LoginActivity extends BaseActivity implements AVCallback {

    private static final String USERSIG = "eJxFkN1Og0AQRt*F2xqZXbpFTLxAQs2mWmiBxHBDCLvQifLjspRW47uL2Mbbc*bLfDNfRvwc3eZdhyLLdWYpYdwbYNzMWJ46VDLLSy3VhAljjAJc7VGqHttmEhQII9QC*JcoZKOxxDl4aBV*TpN-qsdqYi9*4nHvMNo8qfemZXnFQqz3abRIeMzDzTEsVsMYvKdbJxoYrGoXfdcMg3RXRR-*bj2qE5BNDbn56nBoXbUdEqcc2CPGQfV07h*uy8RbNl-323859SNLajsXqbGWM7epA8S*oxeeF0U7NDrT507O7-j*AQSKV58_";
    private static final String IDENTIFIER = "horizon";

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,  Manifest.permission.WAKE_LOCK, Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("登录");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PermissionUtils.checkPermissions(this, PERMISSIONS)) {
            PermissionUtils.requestPermissions(this, PERMISSIONS);
        } else {
            imLogin(IDENTIFIER, USERSIG);
        }
    }

    private void imLogin(final String identifier, final String userSig) {
        TIMUser user = new TIMUser();
        user.setAccountType(Constants.ACCOUNT_TYPE);
        user.setAppIdAt3rd(Constants.SDK_APPID);
        user.setIdentifier(identifier);

        TIMManager.getInstance().login(Integer.parseInt(Constants.SDK_APPID), user, userSig, new TIMCallBack() {

            @Override
            public void onError(int i, String s) {
                LogUtils.e("onError: " + i + " " + s);
            }

            @Override
            public void onSuccess() {
                LogUtils.e("onSuccess");
                MySelfInfo.getInstance().setIdentifier(identifier);
                MySelfInfo.getInstance().setUserSig(userSig);

                AVContextControl.getInstance().setAVConfig(Integer.parseInt(Constants.SDK_APPID), Constants.ACCOUNT_TYPE, identifier, userSig);
                AVContextControl.getInstance().startContext();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onComplete(int i, String s) {
        LogUtils.e("onComplete: " + (i == AVError.AV_OK));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQ_PERMISSIONS) {
            if (!PermissionUtils.verifyPermissions(grantResults)) {
                finish();
                System.exit(0);
            } else {
                imLogin(IDENTIFIER, USERSIG);
            }
        }
    }
}

