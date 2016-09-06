package com.av.communication.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.av.communication.R;
import com.av.communication.presenter.MainPresenter;
import com.av.communication.ui.viewinterface.MainView;
import com.av.communication.util.LogUtils;
import com.tencent.av.opengl.ui.GLRootView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    @Bind(R.id.av_video_view)
    GLRootView avView;

    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mPresenter = new MainPresenter(this);
        mPresenter.startEnterRoom();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeSetContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    /**
     * 完成进出房间流程
     * @param success
     */
    @Override
    public void enterRoomComplete(boolean success) {
        LogUtils.e("enterRoomComplete");
        mPresenter.initAvUILayer(this, avView);

        mPresenter.setCameraPreviewChangeCallback();
    }

    @Override
    public void quiteRoomComplete(boolean success) {

    }
}
