package com.av.communication.control;

import android.content.Intent;

import com.av.communication.Application;
import com.av.communication.Constants;
import com.av.communication.util.LogUtils;
import com.tencent.av.sdk.AVCallback;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVError;
import com.tencent.openqq.IMSdkInt;

/**
 * @author 蒋先明
 * @date 2016/9/3
 */
public class AVContextControl {

    private static AVContextControl instance;
    private AVContext mAVContext = null;
    private AVContext.StartParam mConfig = null;
    private boolean isStartContext = false;
    private boolean mIsInStartContext = false;
    private boolean mIsInStopContext = false;

    public static AVContextControl getInstance() {
        if (instance == null) {
            instance = new AVContextControl();
        }
        return instance;
    }

    /**
     * 启动AVSDK系统的回调接口
     */
    private AVCallback mStartContextCompleteCallback = new AVCallback() {
        public void onComplete(int result, String s) {
            if (result == AVError.AV_OK) {
                isStartContext = true;
            } else {
                mAVContext = null;
            }
            LogUtils.e("AVCallback onComplete: result " + result + ", s: " + s);
        }
    };

    /**
     * 启动AVSDK系统
     * @return 0 代表成功
     */
    public int startContext() {
        int result = AVError.AV_OK;
        if (!hasAVContext()) {
            onAVSDKCreate(true, IMSdkInt.get().getTinyId(), 0);
        } else {
            return AVError.AV_ERR_FAILED;
        }
        return result;
    }

    /**
     * 实际初始化AVSDK
     * @param result
     * @param tinyId
     * @param errorCode
     */
    private void onAVSDKCreate(boolean result, long tinyId, int errorCode) {
        if (result) {
            mAVContext = AVContext.createInstance(Application.application, false);
            int ret = mAVContext.start(mConfig, mStartContextCompleteCallback);
            LogUtils.e("AVContext.start ret: " + ret);
            mIsInStartContext = true;
        } else {
            mStartContextCompleteCallback.onComplete(errorCode,"");
        }
    }

    /**
     *  设置AVSDK参数
     * @param appid
     * @param accountype
     * @param identifier
     * @param usersig
     */
    public void setAVConfig(int appid, String accountype, String identifier, String usersig) {
        mConfig = new AVContext.StartParam();
        mConfig.sdkAppId = appid;
        mConfig.accountType = accountype;
        mConfig.appIdAt3rd = Integer.toString(appid);
        mConfig.identifier = identifier;
    }

    boolean hasAVContext() {
        return mAVContext != null;
    }

    /**
     * 销毁AVSDK
     */
    private void avDestory() {
        mAVContext.destroy();
        mAVContext = null;
        mIsInStopContext = false;
        isStartContext = false;
        Application.application.sendBroadcast(new Intent(Constants.ACTION_CLOSE_CONTEXT_COMPLETE));
    }

    public AVContext getAVContext(){
        return mAVContext;
    }
}
