package com.example.selfie.presenter;

import android.util.Log;
import com.example.selfie.common.ConfigurableOps;
import com.example.selfie.common.ContextView;
import com.example.selfie.common.GenericAsyncTask;
import com.example.selfie.common.GenericAsyncTaskOps;
import com.example.selfie.model.mediator.SelfieDataMediator;

import java.lang.ref.WeakReference;


public class LoginPresenter implements GenericAsyncTaskOps<Void, Void, Boolean>, ConfigurableOps<LoginPresenter.View> {

    private static final String TAG = LoginPresenter.class.getSimpleName();
    private WeakReference<View> mSelfieView;
    private GenericAsyncTask<Void, Void, Boolean, LoginPresenter> mAsyncTask;
    private SelfieDataMediator mSelfieMediator;

    @Override
    public void onConfiguration(LoginPresenter.View view, boolean firstTimeIn) {
        final String time = firstTimeIn ? "first time" : "second+ time";

        Log.d(TAG, "onConfiguration() called the " + time + " with view = " + view);
        mSelfieView = new WeakReference<>(view);
        if (firstTimeIn) {
            mSelfieMediator = new SelfieDataMediator();
        }
    }

    public void login(String username, String password) {
        SelfieDataMediator.init(username, password);

        mAsyncTask = new GenericAsyncTask<>(this);
        mAsyncTask.execute();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public Boolean doInBackground(Void... params) {
        return mSelfieMediator.isLoggedIn();
    }

    @Override
    public void onPostExecute(Boolean loggedIn) {
        if (loggedIn) {
            mSelfieView.get().success();
        } else {
            mSelfieView.get().error(new Exception("Login failed"));
            mSelfieView.get().finish();
        }
    }

    public interface View extends ContextView {
        void finish();
        void error(Exception e);
        void success();
    }
}
