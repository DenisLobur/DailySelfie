package com.example.selfie.web;

import android.os.AsyncTask;
import android.util.Log;
import com.example.selfie.model.mediator.webdata.Selfie;
import com.example.selfie.utils.Constants;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.List;

/**
 * Created by denis on 10/24/15.
 */
public class PictureApi {

    private static PictureServiceProxy pictureApi;
    private static final String NAME = "admin";
    private static final String PASS = "pass";

    public static void init(String userName, String password) {
        pictureApi = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL + PictureServiceProxy.TOKEN_PATH)
                .setUsername(NAME)
                .setPassword(PASS)
                .setClientId("mobile")
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(PictureServiceProxy.class);

        new GetStringsTask().execute();
    }

    private static FromApi fromApi;

    public static void initApiInterface(FromApi api) {
        fromApi = api;
    }

    public interface FromApi {
        void success();
    }

    private static class GetStringsTask extends AsyncTask<Void, Void, List<String>> {
        private List<Selfie> allStrings;
        @Override
        protected List doInBackground(Void... params) {
            try {
                //pictureApi.sendPictureData(new Selfie("name", new byte[1024]));
                allStrings = (List<Selfie>) pictureApi.getSelfies();

            } catch (Exception e) {
                Log.e("PictureApi", e.getMessage());
            }
            return allStrings;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            fromApi.success();
        }
    }

    private static class SendPictureTask extends AsyncTask<Void, Void, Selfie> {
        private Selfie selfie;
        PictureStatus status;

        public SendPictureTask(Selfie selfie) {
            this.selfie = selfie;
        }

        @Override
        protected Selfie doInBackground(Void... params) {
            try {
                //selfie = pictureApi.sendPictureData(new Selfie("name", "path"));

            } catch (Exception e) {
                Log.e("PictureApi", e.getMessage());
            }
            return selfie;
        }

        @Override
        protected void onPostExecute(Selfie st) {
            super.onPostExecute(st);
            //fromApi.success();
        }
    }

    public static String uploadPicture(Selfie selfie) {
        new SendPictureTask(selfie).execute();
        return null;
    }
}
