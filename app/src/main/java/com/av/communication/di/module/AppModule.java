package com.av.communication.di.module;

import com.av.communication.Application;
import com.av.communication.Constants;
import com.tencent.TIMManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSLoginHelper;

/**
 * @author 蒋先明
 * @date 2016/9/3
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application = application;
        TIMManager.getInstance().disableBeaconReport();
        TIMManager.getInstance().init(application);
    }

    @Singleton
    @Provides
    public TLSLoginHelper provideTLSLoginHelper(){
        TLSLoginHelper tlsLoginHelper = TLSLoginHelper.getInstance().init(application, Integer.parseInt(Constants.SDK_APPID), Integer.parseInt(Constants.ACCOUNT_TYPE), Constants.APP_VER);
        tlsLoginHelper.setTimeOut(Constants.TIME_OUT);
        return tlsLoginHelper;
    }

    @Singleton
    @Provides
    public TLSAccountHelper provideTLSAccountHelper(){
        TLSAccountHelper tlsAccountHelper = TLSAccountHelper.getInstance().init(application, Integer.parseInt(Constants.SDK_APPID), Integer.parseInt(Constants.ACCOUNT_TYPE), Constants.APP_VER);
        tlsAccountHelper.setTimeOut(Constants.TIME_OUT);
        return tlsAccountHelper;
    }
}
