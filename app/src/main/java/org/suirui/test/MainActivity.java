package org.suirui.test;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.suirui.srpaas.video.passsdk.manages.ConferenceInfo;
import com.suirui.srpaas.video.passsdk.manages.HuiJianLoginEvent;
import com.suirui.srpaas.video.passsdk.manages.HuiJianSDKEvent;
import com.suirui.srpaas.video.passsdk.manages.LoginListener;
import com.suirui.srpaas.video.passsdk.manages.MeetingListener;
import com.suirui.srpaas.video.passsdk.manages.OnGetMeetingInfoListener;
import com.suirui.srpaas.video.passsdk.manages.OnGetParticipantInfoListener;
import com.suirui.srpaas.video.passsdk.manages.Participant;
import com.suirui.srpaas.video.passsdk.manages.TermInfo;
import com.suirui.srpaas.video.passsdk.manages.plug.VideoConfiguration;
import com.suirui.srpaas.video.passsdk.manages.plug.VideoPlugManage;
import com.suirui.srpaas.video.third.HuiJianSdk;
import com.suirui.srpaas.video.util.StringUtil;

import org.suirui.srpaas.entry.SRError;
import org.suirui.srpaas.sdk.SRPaas;

import java.util.List;


public class MainActivity extends Activity implements LoginListener, MeetingListener {
    public static final String appId = "beb7da4ced7c42a085c3c99697f9aa42";
    public static final String secretKey = "beb7da4ced7c42a085c3c99697f9aa42";
    //        public static String doMain = "http://lab.suirui.com";//需要更换自己的测试环境
    public static String doMain = "http://47.93.195.90";//需要更换自己的测试环境
    private final String TAG = MainActivity.class.getName();
    EditText meet_id, meet_pwd;
    private String confId = "";
    private String nickName = "";
    private String confPwd = "";
    private String uid = "";
    private String token = "";

    private EditText activity_main_username;
    private EditText activity_main_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HuiJianSDKEvent.getInstance().addMeetingListener(this);
        boolean isMeeting = HuiJianSdk.getInstance().onBackMeeting(this);//已经在会议中的，直接返回到会议中
        Log.e(TAG, "MainActivity....onCreate()..isMeeting:" + isMeeting);
        if (!isMeeting) {
            setContentView(R.layout.activity_main);
            activity_main_username = (EditText) findViewById(R.id.activity_main_username);
            activity_main_username.setText("222");
            activity_main_username.setSelection(3);
            activity_main_password = (EditText) findViewById(R.id.activity_main_password);
            activity_main_password.setText("123456");
            activity_main_password.setSelection(6);
            meet_id = (EditText) findViewById(R.id.meet_id);
            meet_pwd = (EditText) findViewById(R.id.meet_pwd);
            HuiJianLoginEvent.getInstance().addLoginListener(this);
        }
        initVideoPlugin(90, 2);
    }


    private void initVideoPlugin(int cameraRotation, int sdkCameraRotation) {
        //配置修改后台通知栏的logo和appName
        VideoConfiguration videoConfiguration = new VideoConfiguration.VideoConfigBuilder(this)
                .notifyIcon(R.drawable.ic_con)//消息通知图标
                .notifAppName("会见")
                .setInvite(true)//是否打开邀请功能
                .setSRLog(true)//是否打开log
                .setSRMessageAlert(true)//是否打开终端加入/离开，以及结束会议的相关提示
                .setSREndMeetingDialog(true)//是否打开结束/离开会议的二次确认框
                .setPlatformType(false)//true是TV,默认手机
                .setUsbCamera(false)//设置usb摄像头 目前不支持，需要aar文件
                .setSRChat(true)//是否有聊天
                .setSROnlive(true)//是有有直播
                .setSRRecord(true)//是否有录制
                .setSelectType(SRPaas.VideoType.SR_CFG_VIDEO_SIZE_720P.getValue(), SRPaas.VideoType.SR_CFG_VIDEO_SIZE_180P.getValue())//选流大小
//                .setCameraRotation(cameraRotation, sdkCameraRotation)
                .build();
        VideoPlugManage.getManager().init(videoConfiguration);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 登录(第三方自己调用resful接口，这里只是我们做的演示)
     *
     * @param view
     */
    public void onLogin(View view) {
        String password = activity_main_password.getText().toString();
        String name = activity_main_username.getText().toString();
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
            HuiJianSdk.getInstance().onLogin(this, name, password, appId, secretKey, doMain, "");
        } else {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 开始会议
     *
     * @param view
     */
    public void onStartMeeting(View view) {
        /*HuiJianSdk.getInstance().startMeeting(this,
                "beb7da4ced7c42a085c3c99697f9aa42",
                "3949ba59abbe56e057f20f883eadce",
                "http://10.1.36.202",
                "70901006",
                "DA055F1562BA9C1C0404CB0EF7487FB87B0CDE90EFD6AB94179F1E865EA8DEF5",
                "崔振岭",
                "",
                "",
                true);*/
        /**
         * 开始会议
         * appId （用户申请的）
         * secretKey（用户申请的）
         * doMain 域名 （如：http://lab.ihuijian.com）
         * uid  账号
         * nickName 昵称
         * confPwd 会议密码
         * isAudioMeet false:视频会议，true:音频会议
         */
        confId = meet_id.getText().toString();
        confPwd = meet_pwd.getText().toString();
        if (!StringUtil.isEmpty(token)) {
            HuiJianSdk.getInstance().startMeeting(this, appId, secretKey, doMain, uid, token, nickName, confId, confPwd, true);
        } else {
            Toast.makeText(MainActivity.this, "请先登录...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加入会议  3681@pri1(私网：10.10.26.179:8089)    516380@pub(公网：40.125.172.209:8089)
     *
     * @param view
     */
    public void onJoinMeeting(View view) {
        if (StringUtil.isEmpty(token)) {
            Toast.makeText(MainActivity.this, "请先登录...", Toast.LENGTH_SHORT).show();
            return;
        }
        confId = meet_id.getText().toString();
        if (TextUtils.isEmpty(confId)) {
            Toast.makeText(MainActivity.this, "请输入会议号", Toast.LENGTH_LONG).show();
            return;
        }
        /**
         * 加入会议
         * pass_url_root （默认：/api/v1）
         * appId （用户申请的）
         * secretKeyID（用户申请的）
         * DO_MAIN 域名 （如：http://lab.ihuijian.com）
         * CONNECT_JOIN_URL 链接入会的url （默认：/SRhuijian.html?id=）
         * uid  账号
         * nickName 昵称
         * meetingnumber 会议号
         * pwd 会议密码
         */
        confPwd = meet_pwd.getText().toString();
        HuiJianSdk.getInstance().joinMeeting(this, appId, secretKey, doMain, uid, token, nickName, confId, confPwd);
    }

    @Override
    public void onLoginState(boolean isState, String uid, String nikeName, String token) {
        //true 为登录成功
        if (isState) {
            this.uid = uid;
            this.nickName = nikeName;
            this.token = token;
            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, "MainActivity...登录的状态....isState:" + isState + " token:" + token + " uid:" + uid + " nikeName:" + nikeName);
    }

    /**
     * confId 会议号 3.
     * *termInfo 参会人信息(uid,tername) 4.
     * *srError 离开信息(getCur_error:离开状态码；getDetail_reason:离开信息；get Brief_reason:离开信息)
     */
    @Override
    public void onTermLeaveCallBack(String confId, TermInfo termInfo, SRError srError) {
        if (termInfo != null) {
            Log.e(TAG, "MainActivity...onTermLeaveCallBack....app..confId:" + confId + " Uid:" + termInfo.getUid() + " Tername:" + termInfo.getTername());
        }
    }

    /**
     * confId 会议号 3. termInfo 参会人信息(uid,tername)
     */
    @Override
    public void onNewTermJoinCallBack(String confId, TermInfo termInfo) {
        if (termInfo != null) {
            Log.e(TAG, "MainActivity...onNewTermJoinCallBack..app..confId:" + confId + " Uid:" + termInfo.getUid() + " Tername:" + termInfo.getTername());
        }
    }

    @Override
    public void onMeetingState(String confId, int code, String message) {
        Log.e(TAG, "MainActivity...onMeetingState....confId:" + confId + " code:" + code + " message:" + message);

        //当前的会议信息
        HuiJianSdk.getInstance().getMeetingInfo(this, new OnGetMeetingInfoListener() {
            @Override
            public void onFailed(int errorCode, String errorMsg) {
                Log.e(TAG, "MainActivity....getMeetingInfo...onFailed...errorCode:" + errorCode + "   errorMsg:" + errorMsg);
            }

            @Override
            public void onSuccess(ConferenceInfo conferenceInfo) {
                if (conferenceInfo != null) {
                    Log.e(TAG, "MainActivity...conferenceInfo....getNikeName:" + conferenceInfo.getNikeName()
                            + " getConfId:" + conferenceInfo.getConfId() + " getConfPwd:" + conferenceInfo.getConfPwd() + " getStartTime:"
                            + conferenceInfo.getStartTime() + " getEndTime:" + conferenceInfo.getEndTime()
                            + " getConfSubject:" + conferenceInfo.getConfSubject());
                }
            }
        });

        //获取参会人列表
        HuiJianSdk.getInstance().getParticipantInfoList(this, new OnGetParticipantInfoListener() {
            @Override
            public void onFailed(int errorCode, String errorMsg) {
                Log.e(TAG, "MainActivity....getParticipantInfoList...onFailed...errorCode:" + errorCode + "   errorMsg:" + errorMsg);
            }

            @Override
            public void onSuccess(List<Participant> list) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Participant participant = list.get(i);
                        if (participant != null) {
                            Log.e(TAG, "MainActivity...participant....getNikeName:" + participant.getNikeName() + " getUid:" + participant.getUid());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCameraRotation(int cameraId, int screenRotation) {
        Log.e(TAG, "MainActivity....onCameraRotation...cameraId:" + cameraId + "   screenRotation:" + screenRotation);
        if (screenRotation == 0) {//横屏
            initVideoPlugin(0, 1);
        } else if (screenRotation == 1) {//竖屏
            initVideoPlugin(90, 2);
        } else if (screenRotation == 9) {//竖屏
            initVideoPlugin(270, 0);
        } else if (screenRotation == 8) {//横屏
            initVideoPlugin(180, 3);
        } else {//竖屏
            initVideoPlugin(90, 2);
        }
    }
}
