package com.av.communication.control;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.av.communication.Constants;
import com.av.communication.R;
import com.av.communication.util.LogUtils;
import com.tencent.av.opengl.GraphicRendererMgr;
import com.tencent.av.opengl.gesturedetectors.MoveGestureDetector;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.opengl.ui.GLViewGroup;
import com.tencent.av.utils.QLog;

/**
 * Created by Administrator on 2016/9/6.
 */
public class AVUIControl extends GLViewGroup {

    static final String TAG = "VideoLayerUI";

    Context mContext = null;
    GraphicRendererMgr mGraphicRenderMgr = null;
    View mRootView = null;
    private AVContextControl qavsdk;

    GLRootView mGlRootView = null;
    GLVideoView mGlVideoView[] = null;

    private SurfaceView mSurfaceView = null;

    public AVUIControl(Context context, View rootView) {
        mContext = context;
        mRootView = rootView;
        mGraphicRenderMgr = GraphicRendererMgr.getInstance();
        qavsdk = AVContextControl.getInstance();
        initQQGlView();
        initCameraPreview();
        initVideoParam();
        //id_view.clear();
    }

    void initQQGlView() {
        if (QLog.isColorLevel()) {
            QLog.d(TAG, QLog.CLR, "initQQGlView");
        }
        mGlRootView = (GLRootView) mRootView.findViewById(R.id.av_video_view);
        mGlVideoView = new GLVideoView[Constants.VIDEO_VIEW_MAX];
        // for (int i = 0; i < mGlVideoView.length; i++) {
        // mGlVideoView[i] = new GLVideoView(mVideoController, mContext.getApplicationContext());
        // mGlVideoView[i].setVisibility(GLView.INVISIBLE);
        // addView(mGlVideoView[i]);
        // }
        mGlVideoView[0] = new GLVideoView(mContext.getApplicationContext(), mGraphicRenderMgr);
        mGlVideoView[0].setVisibility(GLView.INVISIBLE);
        addView(mGlVideoView[0]);
        for (int i = Constants.VIDEO_VIEW_MAX - 1; i >= 1; i--) {
            mGlVideoView[i] = new GLVideoView(mContext.getApplicationContext(), mGraphicRenderMgr);
            mGlVideoView[i].setVisibility(GLView.INVISIBLE);
            addView(mGlVideoView[i]);
        }
        mGlRootView.setContentPane(this);
        // set bitmap ,reuse the backgroud BitmapDrawable,mlzhong
        // setBackground(UITools.getBitmapFromResourceId(mContext, R.drawable.qav_gaudio_bg));

//        mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleGestureListener());
//        mGestureDetector = new GestureDetector(mContext, new GestureListener());
//        mMoveDetector = new MoveGestureDetector(mContext, new MoveListener());
//        mTouchListener = new TouchListener();
//        setOnTouchListener(mTouchListener);
    }

    void initCameraPreview() {

//		SurfaceView localVideo = (SurfaceView) mRootView.findViewById(R.id.av_video_surfaceView);
//		SurfaceHolder holder = localVideo.getHolder();
//		holder.addCallback(mSurfaceHolderListener);
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 3.0以下必须在初始化时调用，否则不能启动预览
//		localVideo.setZOrderMediaOverlay(true);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = 1;
        layoutParams.height = 1;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // layoutParams.flags |= LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.windowAnimations = 0;// android.R.style.Animation_Toast;
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //layoutParams.setTitle("Toast");
        try {
            mSurfaceView = new SurfaceView(mContext);
            SurfaceHolder holder = mSurfaceView.getHolder();
            holder.addCallback(mSurfaceHolderListener);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 3.0以下必须在初始化时调用，否则不能启动预览
            mSurfaceView.setZOrderMediaOverlay(true);
            windowManager.addView(mSurfaceView, layoutParams);
        } catch (IllegalStateException e) {
            windowManager.updateViewLayout(mSurfaceView, layoutParams);
            if (QLog.isColorLevel()) {
                QLog.d(TAG, QLog.CLR, "add camera surface view fail: IllegalStateException." + e);
            }
        } catch (Exception e) {
            if (QLog.isColorLevel()) {
                QLog.d(TAG, QLog.CLR, "add camera surface view fail." + e);
            }
        }
        LogUtils.i("initCameraPreview");
    }

    private SurfaceHolder.Callback mSurfaceHolderListener = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //mCameraSurfaceCreated = true;
            //if (qavsdk.getAvRoomMulti() != null) {
                qavsdk.getAVContext().setRenderMgrAndHolder(mGraphicRenderMgr, holder);
           // }
            mContext.sendBroadcast(new Intent(Constants.ACTION_SURFACE_CREATED));
            LogUtils.e(" surfaceCreated");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (holder.getSurface() == null) {
                return;
            }
            holder.setFixedSize(width, height);
           // SxbLog.e(TAG, "memoryLeak surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //SxbLog.e(TAG, "memoryLeak surfaceDestroyed");
        }
    };

    private void initVideoParam() {
        if (null != qavsdk) {
            //isSupportMultiVideo = true;
        }

        if (QLog.isColorLevel()) {
            //QLog.d(TAG, QLog.CLR, "isSupportMultiVideo: " + isSupportMultiVideo);
        }
    }

}
