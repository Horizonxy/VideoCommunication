package com.av.communication.di.component;

import com.av.communication.Application;
import com.av.communication.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author 蒋先明
 * @date 2016/9/3
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Application inject(Application application);
}
