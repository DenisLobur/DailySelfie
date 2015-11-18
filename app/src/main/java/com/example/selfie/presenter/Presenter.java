package com.example.selfie.presenter;

/**
 * Created by denis on 10/11/15.
 */
public abstract class Presenter {

    public abstract void onAttachView(PresenterView view);

    public abstract void onDetachView();

    public abstract void initialize();

    public abstract void resume();

    public abstract void pause();
}
