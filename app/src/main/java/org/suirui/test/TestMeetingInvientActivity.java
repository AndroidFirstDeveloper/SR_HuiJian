package org.suirui.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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
    private TextView invite_layout_param_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_layout);
        invite_layout_param_tv = findViewById(R.id.invite_layout_param_tv);
        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String subject = bundle.getString(AndroidAppUtil.EXTRA_SUBJECT);
                String confId = bundle.getString(AndroidAppUtil.EXTRA_MEETING_ID);
                String confPwd = bundle.getString(AndroidAppUtil.EXTRA_MEETING_PSW);
                List<Participant> list = (List<Participant>) bundle.getSerializable(AndroidAppUtil.EXTRA_PARTICIPANT_LIST);
                Log.e(TAG, "邀请....subject:" + subject + " confId:" + confId + " confPwd:" + confPwd);
                Log.e(TAG, "onCreate: 会议人员=" + new Gson().toJson(list));
                Toast.makeText(this, "subject:" + subject + " confId:" + confId + " confPwd:" + confPwd, Toast.LENGTH_SHORT).show();
                showData(subject, confId, confPwd, list);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showData(String subject, String confId, String confPwd, List<Participant> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("会议主题：").append(subject).append("\n")
                .append("会议id：").append(confId).append("\n")
                .append("会议密码：").append(confPwd).append("\n");
        sb.append("参会人员：");
        String divider = "";
        for (Participant participant : list) {
            sb.append(divider).append(participant.getNikeName());
            divider = "、";
        }
        invite_layout_param_tv.setText(sb.toString());
    }
}
