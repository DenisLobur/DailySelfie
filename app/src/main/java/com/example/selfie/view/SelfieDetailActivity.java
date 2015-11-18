package com.example.selfie.view;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import com.example.selfie.R;
import com.example.selfie.common.GenericActivity;
import com.example.selfie.model.services.DownloadSelfieService;
import com.example.selfie.presenter.SelfiePresenter;
import com.example.selfie.view.ui.SelfieAdapter;


public class SelfieDetailActivity extends GenericActivity<SelfiePresenter.View, SelfiePresenter> implements SelfiePresenter.View {

    public static final String SELFIE_EXTRA = "SELFIE";

    private ImageView detailImage;


    private DownloadResultReceiver mDownloadResultReceiver;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, SelfiePresenter.class, this);

        setContentView(R.layout.activity_detail);

        detailImage = (ImageView) findViewById(R.id.detail_img);

        mDownloadResultReceiver = new DownloadResultReceiver();

        Intent command = getIntent();

        /*Selfie selectedSelfie = (Selfie) command.getSerializableExtra(VIDEO_EXTRA);
        if (selectedSelfie != null) {
            getOps().setSelfie(selectedSelfie);

            Log.i(TAG, "Setting title: " + getOps().getSelfie().getTitle());
            title.setText(getOps().getSelfie().getTitle());
            duration.setText(String.valueOf(getOps().getSelfie().getDuration() / 1000) + " sec");
            contentType.setText(getOps().getSelfie().getContentType());
            rating.setText(String.valueOf(getOps().getSelfie().getStarRating()));

            updateView();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(DownloadSelfieService.ACTION_DOWNLOAD_SERVICE_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadResultReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadResultReceiver);
    }

    /*public void updateView() {
        if (getOps().getSelfie() != null) {
            rating.setText(String.valueOf(getOps().getSelfie().getStarRating()));

            if (!getOps().getSelfie().isFileStored()) {
                watchButton.setText(R.string.download_selfie);
            } else {
                ratingBar.setVisibility(View.VISIBLE);
                watchButton.setText(R.string.watch_selfie);
            }
        }
    }*/

    public void watchSelfie(View view) {
        getOps().watchSelfie();
    }

    @Override
    public void setAdapter(SelfieAdapter selfieAdapter) {
        // no adapter needed
    }

    private class DownloadResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long selfieId = intent.getLongExtra(DownloadSelfieService.SELFIE_ID_EXTRA, 0);

            getOps().updateSelfieForView(selfieId);

            //updateView();
        }
    }
}
