package com.av.communication.presenter;

import android.content.Context;

import com.av.communication.Constants;
import com.av.communication.control.AVContextControl;
import com.av.communication.control.AVUIControl;
import com.av.communication.ui.viewinterface.MainView;
import com.av.communication.util.LogUtils;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMValueCallBack;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVVideoCtrl;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MainPresenter extends BasePresenter {

    private static boolean isInRoom;
    private MainView mView;

    public MainPresenter(MainView mView) {
        this.mView = mView;
    }

    public void startEnterRoom(){
        createIMChatRoom();
    }

    private void createIMChatRoom() {
        final ArrayList<String> list = new ArrayList<String>();
        String roomName = "ChatRoom-"+Constants.ROOM_NUM;
        TIMGroupManager.getInstance().createGroup("ChatRoom", list, roomName, Constants.ROOM_NUM, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e("createIMChatRoom onError: "+i+" "+s);
                //已在房间中,重复进入房间
                if (i == Constants.IS_ALREADY_IN_ROOM) {
                    isInRoom = true;
                    createAVRoom(Integer.parseInt(Constants.ROOM_NUM));
                    return;
                }
                quiteRoom();
            }

            @Override
            public void onSuccess(String s) {
                LogUtils.e("createIMChatRoom onSuccess: "+s);
                createAVRoom(Integer.parseInt(Constants.ROOM_NUM));
            }
        });
    }

    private void createAVRoom(int roomNum) {
        AVContext avContext = AVContextControl.getInstance().getAVContext();
        byte[] authBuffer = null;//权限位加密串；TODO：请业务侧填上自己的加密串

        AVRoomMulti.EnterParam.Builder enterRoomParam = new AVRoomMulti.EnterParam.Builder(roomNum);
        enterRoomParam.auth(Constants.HOST_AUTH, authBuffer).avControlRole(Constants.HOST_ROLE).autoCreateRoom(true).isEnableMic(true).isEnableSpeaker(true);//；TODO：主播权限 所有权限
        enterRoomParam.audioCategory(Constants.AUDIO_VOICE_CHAT_MODE).videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);
        if (avContext != null) {
            // create room
            int ret = avContext.enterRoom(mEventListener, enterRoomParam.build());
            LogUtils.i("createAVRoom " + ret);
        }
    }

    /**
     * 房间回调
     */
    private AVRoomMulti.EventListener mEventListener = new AVRoomMulti.EventListener() {

        @Override
        public void onEnterRoomComplete(int result) {
            LogUtils.e("mEventListener: "+result);
            if (result == 0) {
                isInRoom = true;
                initAudioService();
                mView.enterRoomComplete(true);
            } else {
                quiteRoom();
            }
        }

        @Override
        public void onExitRoomComplete() {
            isInRoom = false;
            quiteRoom();
            uninitAudioService();
            mView.quiteRoomComplete(true);
        }

        @Override
        public void onRoomDisconnect(int i) {
            isInRoom = false;
            quiteRoom();
            uninitAudioService();
            mView.quiteRoomComplete(true);
        }

        //房间成员变化回调
        @Override
        public void onEndpointsUpdateInfo(int i, String[] strings) {
        }

        @Override
        public void onPrivilegeDiffNotify(int i) {
        }

        @Override
        public void onSemiAutoRecvCameraVideo(String[] strings) {
        }

        @Override
        public void onCameraSettingNotify(int i, int i1, int i2) {
        }

        @Override
        public void onRoomEvent(int i, int i1, Object o) {
        }
    };

    private void quiteRoom() {
        //quiteIMChatRoom();
        quiteAVRoom();
    }

    private void quiteAVRoom() {
        if (isInRoom == true) {
            AVContext avContext = AVContextControl.getInstance().getAVContext();
            int result = avContext.exitRoom();
        } else {
            quiteIMChatRoom();
            uninitAudioService();
        }
    }

    private void quiteIMChatRoom() {
        TIMGroupManager.getInstance().deleteGroup(Constants.ROOM_NUM, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                isInRoom = false;
            }
        });
        TIMManager.getInstance().deleteConversation(TIMConversationType.Group, Constants.ROOM_NUM);
    }

    private void initAudioService() {
        if ((AVContextControl.getInstance().getAVContext() != null) && (AVContextControl.getInstance().getAVContext().getAudioCtrl() != null)) {
            AVContextControl.getInstance().getAVContext().getAudioCtrl().startTRAEService();
        }
    }

    private void uninitAudioService() {
        if ((AVContextControl.getInstance().getAVContext() != null) && (AVContextControl.getInstance().getAVContext().getAudioCtrl() != null)) {
            AVContextControl.getInstance().getAVContext().getAudioCtrl().startTRAEService();
        }
    }

    public void initAvUILayer(Context context, GLRootView avView) {
        new AVUIControl(context, avView);
    }

    public void setCameraPreviewChangeCallback() {
        AVVideoCtrl avVideoCtrl = AVContextControl.getInstance().getAVContext().getVideoCtrl();
        if (avVideoCtrl != null)
            avVideoCtrl.setCameraPreviewChangeCallback(mCameraPreviewChangeCallback);
    }

    private AVVideoCtrl.CameraPreviewChangeCallback mCameraPreviewChangeCallback = new AVVideoCtrl.CameraPreviewChangeCallback() {
        @Override
        public void onCameraPreviewChangeCallback(int cameraId) {
            LogUtils.d("mCameraPreviewChangeCallback.onCameraPreviewChangeCallback cameraId = " + cameraId);
            //AVContextControl.getInstance().setMirror(FRONT_CAMERA == cameraId);
        }
    };
}
