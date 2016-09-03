package com.av.communication;

import com.av.communication.di.component.DaggerAppComponent;
import com.av.communication.di.module.AppModule;

import javax.inject.Inject;

import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSLoginHelper;


/**
 * 全局Application
 */
public class Application extends android.app.Application {

    public static Application application;

    @Inject
    TLSLoginHelper mLoginHelper;
    @Inject
    TLSAccountHelper mAccountHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        DaggerAppComponent.builder().appModule(new AppModule(this)).build().inject(this);
    }

    public TLSLoginHelper getTLSLoginHelper(){
        return mLoginHelper;
    }

    public TLSAccountHelper getTLSAccountHelper(){
        return mAccountHelper;
    }
}
