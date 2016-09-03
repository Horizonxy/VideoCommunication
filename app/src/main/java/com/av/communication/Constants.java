package com.av.communication;


import com.tencent.av.sdk.AVRoomMulti;

/**
 * 静态函数
 */
public class Constants {

    public static final String PRIVATE_KEY_PART1 = "MIGEAgEAMBAGByqGSM49AgEGBSuBBAAKBG0wawIBAQQgn0al+WTosgzq6jZgDik1";
    public static final String PRIVATE_KEY_PART2 = "0ZBDwXH6AEHWM5wY5FbPTnuhRANCAAQ4Yvi8mMTfxdgnivr5tpCNe++iVPvPCwpG";
    public static final String PRIVATE_KEY_PART3 = "h/qNjlUU7ahMcpphc81jqcbbsEK1BsVhyriS9ZmYcuOEF2iOy3p5";

    public static final String SDK_APPID = "1400014279";
    public static final String ACCOUNT_TYPE = "7253";
    public static final String APP_VER = "1.0";

    public static final int TIME_OUT = 5000;
    public static final int REQ_PERMISSIONS = 100;

    public static final String CHAT_NUM = "1001";

    public static final int AUDIO_VOICE_CHAT_MODE = 0;

    public static final long HOST_AUTH = AVRoomMulti.AUTH_BITS_DEFAULT;//权限位；TODO：默认值是拥有所有权限。
    public static final String HOST_ROLE = "Host";

    public static final int IS_ALREADY_IN_ROOM = 10025;

    private static final String PACKAGE =  Application.application.getPackageName();

    public static final String ACTION_CLOSE_CONTEXT_COMPLETE = PACKAGE  + ".ACTION_CLOSE_CONTEXT_COMPLETE";

    public static final String ACTION_SURFACE_CREATED = PACKAGE + ".ACTION_SURFACE_CREATED";
    public static final String ACTION_HOST_ENTER = PACKAGE + ".ACTION_HOST_ENTER";
    public static final String ACTION_CAMERA_OPEN_IN_LIVE = PACKAGE  + ".ACTION_CAMERA_OPEN_IN_LIVE";
    public static final String ACTION_CAMERA_CLOSE_IN_LIVE = PACKAGE + ".ACTION_CAMERA_CLOSE_IN_LIVE";
    public static final String ACTION_SWITCH_VIDEO = PACKAGE + ".ACTION_SWITCH_VIDEO";
    public static final String ACTION_HOST_LEAVE = PACKAGE + ".ACTION_HOST_LEAVE";
}
