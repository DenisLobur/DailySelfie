package com.example.selfie.common;

import android.os.AsyncTask;

public class GenericAsyncTask<Params,
        Progress,
        Result,
        Ops extends GenericAsyncTaskOps<Params, Progress, Result>>
        extends AsyncTask<Params, Progress, Result> {

    protected final String TAG = getClass().getSimpleName();

    protected Ops mOps;

    public GenericAsyncTask(Ops ops) {
        mOps = ops;
    }

    @SuppressWarnings("unchecked")
    protected Result doInBackground(Params... params) {
        return mOps.doInBackground(params);
    }

    protected void onPostExecute(Result result) {
        mOps.onPostExecute(result);
    }
}
