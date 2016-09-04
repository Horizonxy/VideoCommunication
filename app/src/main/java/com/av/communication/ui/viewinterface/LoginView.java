package com.av.communication.ui.viewinterface;

import com.tencent.TIMUser;

/**
 * @author 蒋先明
 * @date 2016/9/4
 */
public interface LoginView {

    void imLoginSuccess();
    void imLoginError(int i, String s);
    TIMUser getTIMUser();
    String getUserSig();
}
