package com.example.selfie.presenter;

import android.app.Activity;
import android.util.Log;

/**
 * Created by denis on 10/11/15.
 */
public class MainPresenter extends Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private MainView mainView;

    @Override
    public void onAttachView(PresenterView view) {
        Log.d(TAG, "onAttachView");
        mainView = (MainView)view;
    }

    @Override
    public void onDetachView() {
        Log.d(TAG, "onDetachView");

        mainView = null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public interface MainView extends PresenterView {
        void showProgress(boolean show);
        Activity getContext();
    }
}
