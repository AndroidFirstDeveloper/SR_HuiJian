package org.suirui.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suirui.srpaas.video.passsdk.manages.AndroidAppUtil;
import com.suirui.srpaas.video.passsdk.manages.Participant;

import java.util.List;

/**
 * 邀请界面
 *
 * @authordingna
 * @date2017-09-19
 **/
public class TestMeetingInvientActivity extends Activity {
    private final String TAG = TestMeetingInvientActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_layout);
        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String subject = bundle.getString(AndroidAppUtil.EXTRA_SUBJECT);
                String confId = bundle.getString(AndroidAppUtil.EXTRA_MEETING_ID);
                String confPwd = bundle.getString(AndroidAppUtil.EXTRA_MEETING_PSW);
                List<Participant> list = (List<Participant>) bundle.getSerializable(AndroidAppUtil.EXTRA_PARTICIPANT_LIST);
                Log.e(TAG, "邀请....subject:" + subject + " confId:" + confId + " confPwd:" + confPwd);
                Log.e(TAG, "onCreate: 会议人员="+new Gson().toJson(list));
                Toast.makeText(this, "subject:" + subject + " confId:" + confId + " confPwd:" + confPwd, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
