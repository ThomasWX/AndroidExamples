package com.job.scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    public static final int MSG_UN_COLOR_START = 0;
    public static final int MSG_UN_COLOR_STOP = 1;

    public static final int MSG_COLOR_START = 2;
    public static final int MSG_COLOR_STOP = 3;

    public static final String MESSENGER_INTENT_KEY = BuildConfig.APPLICATION_ID + ".MESSENGER_INTENT_KEY";
    public static final String WORK_DURATION_KEY = BuildConfig.APPLICATION_ID + ".WORK_DURATION_KEY";

    private EditText mDelayEditText; // 延迟
    private EditText mDeadlineEditText; // 截止
    private EditText mDurationTimeEditText; // 运行时长
    private RadioButton mWifiConnectivityRadioButton; // WIFI 网络联接
    private RadioButton mAnyConnectivityRadioButton; // 任何 网络联接

    private CheckBox mRequiresChargingCheckBox; // 要求充电
    private CheckBox mRequiresIdleCheckBox; // 要求联接电脑

    private ComponentName mServiceComponent;

    private int mJobId = 0;

    // Handler for incoming messages from the service.
    private IncomingMessageHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up UI.
        mDelayEditText = findViewById(R.id.job_scheduler_delay_time);
        mDurationTimeEditText = findViewById(R.id.job_scheduler_duration_time);
        mDeadlineEditText = findViewById(R.id.job_scheduler_deadline_time);
        mWifiConnectivityRadioButton = findViewById(R.id.job_scheduler_checkbox_unmetered);
        mAnyConnectivityRadioButton = findViewById(R.id.job_scheduler_checkbox_any);
        mRequiresChargingCheckBox = findViewById(R.id.job_scheduler_checkbox_charging);
        mRequiresIdleCheckBox = findViewById(R.id.job_scheduler_checkbox_idle);
        mServiceComponent = new ComponentName(this, JobSchedulerService.class);

        mHandler = new IncomingMessageHandler(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Start service and provide it a way to communicate with this class.
        Intent intent = new Intent(this, JobSchedulerService.class);
        Messenger incoming = new Messenger(mHandler);
        intent.putExtra(MESSENGER_INTENT_KEY, incoming);
        startService(intent);
    }


    @Override
    protected void onStop() {

        // A service can be "started" and/or "bound". In this case, it's "started" by this Activity
        // and "bound" to the JobScheduler (also called "Scheduled" by the JobScheduler). This call
        // to stopService() won't prevent scheduled jobs to be processed. However, failing
        // to call stopService() would keep it alive indefinitely.
        stopService(new Intent(this, JobSchedulerService.class));

        super.onStop();
    }


    /**
     * Executed when user clicks on FINISH LAST TASK.
     */
    public void finishJob(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> allPendingJobs = scheduler.getAllPendingJobs();

        if (allPendingJobs.size() > 0) {
            // Finish the last one
            int jobId = allPendingJobs.get(0).getId();
            scheduler.cancel(jobId);

            Toast.makeText(this, String.format(getString(R.string.cancelled_job), jobId), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.no_jobs_to_cancel, Toast.LENGTH_SHORT).show();
        }
    }

    public void scheduleJob(View view) {
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, mServiceComponent);

        String delay = mDelayEditText.getText().toString();
        if (!TextUtils.isEmpty(delay)) {
            builder.setMinimumLatency(Long.valueOf(delay) * 1000);
        }

        String deadline = mDeadlineEditText.getText().toString();
        if (!TextUtils.isEmpty(delay)) {
            builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
        }

        boolean requiresUnmetered = mWifiConnectivityRadioButton.isChecked();
        boolean requiresAnyConnectivity = mAnyConnectivityRadioButton.isChecked();

        if (requiresUnmetered) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        } else if (requiresAnyConnectivity) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        }

        builder.setRequiresDeviceIdle(mRequiresIdleCheckBox.isChecked());
        builder.setRequiresCharging(mRequiresChargingCheckBox.isChecked());


        // Extra, work duration
        PersistableBundle bundle = new PersistableBundle();
        String workDuration = mDurationTimeEditText.getText().toString();
        if (TextUtils.isEmpty(workDuration)) {
            workDuration = "1";
        }
        bundle.putLong(WORK_DURATION_KEY, Long.valueOf(workDuration) * 1000);

        builder.setExtras(bundle);

        // Scheduler job
        Log.d(TAG, "Scheduling job");
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(builder.build());
    }

    /**
     * Executed when user clicks on CANCEL ALL.
     */
    public void cancelAllJobs(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        scheduler.cancelAll();
        Toast.makeText(this, R.string.cancel_all_jobs_button_text, Toast.LENGTH_SHORT).show();
    }


    /**
     * A {@link Handler} allows you to send messages associated with a thread. A {@link Messenger}
     * uses this handler to communicate from {@link JobSchedulerService}. It's also used to make
     * the start and stop views blink for a short period of time.
     */
    private static class IncomingMessageHandler extends Handler {
        // Prevent possible leaks with a weak reference.
        private WeakReference<MainActivity> mActivity;

        public IncomingMessageHandler(MainActivity activity) {
            super(/*default looper*/);
            this.mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            MainActivity activity = mActivity.get();
            if (activity == null) {
                // Activity is no longer available, exit.
                return;
            }

            View showStartView = activity.findViewById(R.id.job_scheduler_onStart);
            View showStopView = activity.findViewById(R.id.job_scheduler_onStop);

            Message m;
            switch (msg.what) {
                /*
                 * Receives callback from the service when a job has landed
                 * on the app. Turns on indicator and sends a message to turn it off after
                 * a second.
                 */

                case MSG_COLOR_START:
                    // Start received, turn on the indicator and show text.
                    showStartView.setBackgroundColor(getColor(R.color.start_received));
                    updateParamsTextView(msg.obj, "started");

                    // Send message to turn it off after a second.
                    m = obtainMessage(MSG_UN_COLOR_START);
                    sendMessageDelayed(m, 1000L);
                    break;

                /*
                 * Receives callback from the service when a job that previously landed on the
                 * app must stop executing. Turns on indicator and sends a message to turn it
                 * off after two seconds.
                 */
                case MSG_COLOR_STOP:
                    // Stop received, turn on the indicator and show text.
                    showStopView.setBackgroundColor(getColor(R.color.stop_received));
                    updateParamsTextView(msg.obj, "stopped");

                    // Send message to turn it off after a second.
                    m = obtainMessage(MSG_UN_COLOR_STOP);
                    sendMessageDelayed(m, 2000L);

                    break;

                case MSG_UN_COLOR_START:
                    showStartView.setBackgroundColor(getColor(R.color.none_received));
                    updateParamsTextView(null, "");

                    break;
                case MSG_UN_COLOR_STOP:
                    showStopView.setBackgroundColor(getColor(R.color.none_received));
                    updateParamsTextView(null, "");
                    break;
            }
        }


        private void updateParamsTextView(@Nullable Object jobId, String action) {
            TextView params = mActivity.get().findViewById(R.id.job_scheduler_task_params);
            if (jobId == null) {
                params.setText("");
                return;
            }
            String jobIdText = String.valueOf(jobId);
            params.setText(String.format("Job ID %s %s", jobIdText, action));
        }

        private int getColor(@ColorRes int color) {
            return mActivity.get().getColor(color);
        }
    }

}