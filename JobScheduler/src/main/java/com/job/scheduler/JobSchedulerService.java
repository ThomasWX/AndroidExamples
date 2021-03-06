package com.job.scheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.job.scheduler.MainActivity.MESSENGER_INTENT_KEY;
import static com.job.scheduler.MainActivity.MSG_COLOR_START;
import static com.job.scheduler.MainActivity.MSG_COLOR_STOP;
import static com.job.scheduler.MainActivity.WORK_DURATION_KEY;

/**
 * 已对比完整性
 * Created by wb on 18-2-2.
 * https://github.com/googlesamples/android-JobScheduler/blob/master/Application/src/main/java/com/example/android/jobscheduler/service/MyJobService.java
 */

/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method. It runs jobs for a specific amount of time
 * and finishes them. It keeps the activity updated with changes via a Messenger.
 */
public class JobSchedulerService extends JobService {
    private static final String TAG = JobSchedulerService.class.getSimpleName();
    private Messenger mActivityMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service onDestroy");
    }

    /**
     * When the app's MainActivity (即JobSchedulerActivity) is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mActivityMessenger = intent.getParcelableExtra(MESSENGER_INTENT_KEY);
        return START_NOT_STICKY;
    }

    /**
     * When the app's JobSchedulerActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */


    @Override
    public boolean onStartJob(final JobParameters params) {
        // The work that this service "does" is simply wait for a certain duration and finish
        // the job (on another thread).
        sendMessage(MSG_COLOR_START, params.getJobId());
        long duration = params.getExtras().getLong(WORK_DURATION_KEY);

        // Uses a handler to delay the execution of jobFinished().
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                sendMessage(MSG_COLOR_STOP, params.getJobId());
                jobFinished(params, false);
            }
        }, duration);
        Log.i(TAG, "on start job: " + params.getJobId());

        // Return true as there's more work to be done with this job.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Stop tracking these job parameters, as we've 'finished' executing.

        sendMessage(MSG_COLOR_STOP, params.getJobId());
        Log.i(TAG, "on stop job: " + params.getJobId());

        // Return false to drop the job.
        return false;
    }


    private void sendMessage(int messageId, @Nullable Object params) {
        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.
        if (mActivityMessenger == null) {
            Log.d(TAG, "Service is bound , not started. There's no callback to send a message to.");
            return;
        }
        Message m = Message.obtain();
        m.what = messageId;
        m.obj = params;
        try {
            mActivityMessenger.send(m);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }
}
