package com.example.selfie.common;

/**
 * The base interface that an operations ("Ops") class can implement
 * so that it can be notified automatically by the GenericAsyncTask
 * framework during the AsyncTask processing.
 */
public interface GenericAsyncTaskOps<Params, Progress, Result> {
    void onPreExecute();
    @SuppressWarnings("unchecked")
    Result doInBackground(Params... params);
    void onPostExecute(Result result);
}

