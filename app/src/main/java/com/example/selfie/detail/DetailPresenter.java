package com.example.selfie.detail;

import android.graphics.Bitmap;
import com.example.selfie.presenter.Presenter;
import com.example.selfie.presenter.PresenterView;

/**
 * Created by denis on 10/11/15.
 */
public class DetailPresenter extends Presenter {
    @Override
    public void onAttachView(PresenterView view) {

    }

    @Override
    public void onDetachView() {

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

    public interface DetailView extends PresenterView {
        void setDetailPic(Bitmap bm);
    }
}
