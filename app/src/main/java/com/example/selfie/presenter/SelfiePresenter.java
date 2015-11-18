package com.example.selfie.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.example.selfie.common.*;
import com.example.selfie.model.mediator.SelfieDataMediator;
import com.example.selfie.model.mediator.webdata.Selfie;
import com.example.selfie.model.services.UpdateSelfieService;
import com.example.selfie.model.services.UploadSelfieService;
import com.example.selfie.view.ui.SelfieAdapter;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SelfiePresenter implements GenericAsyncTaskOps<Void, Void, List<Selfie>>, ConfigurableOps<SelfiePresenter.View> {

    private static final String TAG = SelfiePresenter.class.getSimpleName();
    private WeakReference<View> mSelfieView;
    private GenericAsyncTask<Void, Void, List<Selfie>, SelfiePresenter> mAsyncTask;
    SelfieDataMediator mSelfieMediator;
    private SelfieAdapter mAdapter;
    private Selfie selfie;

    public void onConfiguration(View view, boolean firstTimeIn) {
        final String time = firstTimeIn ? "first time" : "second+ time";

        Log.d(TAG, "onConfiguration() called the " + time + " with view = " + view);

        mSelfieView = new WeakReference<>(view);

        if (firstTimeIn) {
            mSelfieMediator = new SelfieDataMediator();
            mAdapter = new SelfieAdapter(mSelfieView.get().getApplicationContext());
        }

        mSelfieView.get().setAdapter(mAdapter);
    }

    /**
     * Start a service that Uploads the Selfie having given Id.
     *
     * @param selfieUri
     */
    public void uploadSelfie(Uri selfieUri) {
        // Sends an Intent command to the UploadSelfieService.
        mSelfieView.get().getApplicationContext().startService
                (UploadSelfieService.makeIntent(mSelfieView.get().getApplicationContext(), selfieUri));
    }

    public Selfie getSelfie() {
        return selfie;
    }

    public void setSelfie(Selfie selfie) {
        this.selfie = mSelfieMediator.getSelfieFromLocalStorage(mSelfieView.get().getApplicationContext(), selfie.getId());

        if (this.selfie == null) {
            this.selfie = selfie;
        }
    }

    public void watchSelfie() {
        if (getSelfie() == null) {
            Utils.showToast(mSelfieView.get().getActivityContext(), "No selfie selected");
            return;
        }

//        if (getSelfie().isFileStored()) {
//            Intent playSelfie = new Intent(Intent.ACTION_VIEW);
//            playSelfie.setDataAndType(Uri.parse(getSelfie().getLocalUrl()), "selfie/*");
//
//            mSelfieView.get().getActivityContext().startActivity(playSelfie);
//        } else {
//            mSelfieView.get().getApplicationContext().startService(
//                    DownloadSelfieService.makeIntent(mSelfieView.get().getApplicationContext(), getSelfie()));
//
//            Utils.showToast(mSelfieView.get().getActivityContext(), "Downloading selfie");
//        }
    }

    public void updateSelfieForView(long selfieId) {
        Selfie selfie = mSelfieMediator.getSelfieFromLocalStorage(mSelfieView.get().getApplicationContext(), selfieId);

        if (selfie != null) {
            this.selfie = selfie;
        }
    }

    /**
     * Gets the SelfieList from Server by executing the AsyncTask to
     * expand the acronym without blocking the caller.
     */
    public void getSelfieList() {
        mAsyncTask = new GenericAsyncTask<>(this);
        mAsyncTask.execute();
    }

    @Override
    public void onPreExecute() {

    }

    /**
     * Retrieve the List of Selfies by help of SelfieDataMediator via a
     * synchronous two-way method call, which runs in a background
     * thread to avoid blocking the UI thread.
     */
    @Override
    public List<Selfie> doInBackground(Void... params) {
        ArrayList<Selfie> pictureList = mSelfieMediator.getPictureList();
        if(pictureList == null) {
            return null;
        }
        return pictureList;
    }

    /**
     * Display the results in the UI Thread.
     */
    @Override
    public void onPostExecute(List<Selfie> selfies) {
        displaySelfieList(selfies);
    }

    /**
     * Display the Selfies in ListView.
     *
     * @param selfies
     */
    public void displaySelfieList(List<Selfie> selfies) {
        if (selfies != null) {
            // Update the adapter with the List of Selfies.
            mAdapter.setSelfies(selfies);

            Utils.showToast(mSelfieView.get().getActivityContext(),
                    "Selfies available from the Selfie Service");
        } else {
            Utils.showToast(mSelfieView.get().getActivityContext(),
                    "Please connect to the Selfie Service");

            // Close down the Activity.
            mSelfieView.get().finish();
        }
    }

    public void updateRating(Selfie selfie, double rating){
        // Sends an Intent command to the UploadSelfieService.
        mSelfieView.get().getApplicationContext().startService
                (UpdateSelfieService.makeIntent(mSelfieView.get().getApplicationContext(), selfie, rating));
    }



    public interface View extends ContextView {
        void finish();
        void setAdapter(SelfieAdapter selfieAdapter);
    }

}
