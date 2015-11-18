package com.example.selfie.model.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.example.selfie.model.mediator.SelfieDataMediator;
import com.example.selfie.model.mediator.webdata.Selfie;


public class DownloadSelfieService extends IntentService {

    public static final String ACTION_DOWNLOAD_SERVICE_RESPONSE =
            "coursera.assignment3client.services.DownloadSelfieService.RESPONSE";

    private static final String SELFIE_EXTRA = "SELFIE_EXTRA";
    public static final String SELFIE_ID_EXTRA = "SELFIE_ID_EXTRA";

    private static final int NOTIFICATION_ID = 2;

    private SelfieDataMediator mSelfieMediator;

    private NotificationManager mNotifyManager;

    private NotificationCompat.Builder mBuilder;

    public DownloadSelfieService() {
        super(DownloadSelfieService.class.getSimpleName());
    }

    public static Intent makeIntent(Context context, Selfie selfie) {
        //return new Intent(context, DownloadSelfieService.class).putExtra(SELFIE_EXTRA, selfie);
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Selfie selfie = (Selfie) intent.getSerializableExtra(SELFIE_EXTRA);

        if (selfie != null) {
            startNotification();

            mSelfieMediator = new SelfieDataMediator();
            //mSelfieMediator.downloadSelfie(getApplicationContext(), selfie);

            finishNotification("Download Completed :)");

            sendBroadcast(selfie.getId());
        } else {
            Log.e(DownloadSelfieService.class.getSimpleName(), "No selfie was specified for download");
        }
    }

    /**
     * Starts the Notification to show the progress of selfie upload.
     */
    private void startNotification() {
        // Gets access to the Android Notification Service.
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the Notification and set a progress indicator for an
        // operation of indeterminate length.
        mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Selfie Download")
                .setContentText("Download in progress")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("Downloading selfie")
                .setProgress(0, 0, true);

        // Build and issue the notification.
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void finishNotification(String status) {
        // When the loop is finished, updates the notification.
        mBuilder.setContentTitle(status)
                // Removes the progress bar.
                .setProgress(0, 0, false)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentText("")
                .setTicker(status);

        // Build the Notification with the given
        // Notification Id.
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendBroadcast(long selfieId) {
        // Use a LocalBroadcastManager to restrict the scope of this
        // Intent to the SelfieDownloadClient application.
        Intent intent = new Intent(ACTION_DOWNLOAD_SERVICE_RESPONSE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(SELFIE_ID_EXTRA, selfieId);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
