package com.av.communication.presenter;

import com.av.communication.Constants;
import com.av.communication.control.AVContextControl;
import com.av.communication.model.MySelfInfo;
import com.av.communication.ui.viewinterface.LoginView;
import com.av.communication.util.LogUtils;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

/**
 * @author 蒋先明
 * @date 2016/9/4
 */
public class LoginPresenter extends BasePresenter {

    private LoginView mView;

    public LoginPresenter(LoginView mView) {
        this.mView = mView;
    }

    public void imLogin(){
        final TIMUser user = mView.getTIMUser();
        TIMManager.getInstance().login(Integer.parseInt(Constants.SDK_APPID), mView.getTIMUser(), mView.getUserSig(), new TIMCallBack() {

            @Override
            public void onError(int i, String s) {
                mView.imLoginError(i, s);
            }

            @Override
            public void onSuccess() {
                LogUtils.e("onSuccess");
                MySelfInfo.getInstance().setIdentifier(user.getIdentifier());
                MySelfInfo.getInstance().setUserSig(mView.getUserSig());

                AVContextControl.getInstance().setAVConfig(Integer.parseInt(Constants.SDK_APPID), Constants.ACCOUNT_TYPE, user.getIdentifier(), mView.getUserSig());
                AVContextControl.getInstance().startContext();

                mView.imLoginSuccess();
            }
        });
    }

}
