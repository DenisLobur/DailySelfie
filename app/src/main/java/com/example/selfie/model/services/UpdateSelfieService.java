package com.example.selfie.model.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.example.selfie.model.mediator.SelfieDataMediator;
import com.example.selfie.model.mediator.webdata.Selfie;


public class UpdateSelfieService extends IntentService {

    private static final String SELFIE_EXTRA = "SELFIE_EXTRA";
    private static final String SELFIE_RATING_EXTRA = "SELFIE_RATING_EXTRA";

    private SelfieDataMediator mSelfieMediator;

    public UpdateSelfieService() {
        super(UpdateSelfieService.class.getSimpleName());
    }

    public static Intent makeIntent(Context context, Selfie selfie, double rating) {
        //return new Intent(context, UpdateSelfieService.class).putExtra(SELFIE_EXTRA, selfie).putExtra(SELFIE_RATING_EXTRA, rating);
    return  null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Selfie selfie = (Selfie) intent.getSerializableExtra(SELFIE_EXTRA);
        double rating = intent.getDoubleExtra(SELFIE_RATING_EXTRA, 0);

        if (selfie != null) {
            mSelfieMediator = new SelfieDataMediator();
            //mSelfieMediator.rateSelfie(getApplicationContext(), selfie, rating);

            sendBroadcast(selfie.getId());
        } else {
            Log.e(UpdateSelfieService.class.getSimpleName(), "No selfie was specified for download");
        }
    }

    private void sendBroadcast(long selfieId) {
        // Use a LocalBroadcastManager to restrict the scope of this
        // Intent to the SelfieDownloadClient application.
        Intent intent = new Intent(DownloadSelfieService.ACTION_DOWNLOAD_SERVICE_RESPONSE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(DownloadSelfieService.SELFIE_ID_EXTRA, selfieId);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
