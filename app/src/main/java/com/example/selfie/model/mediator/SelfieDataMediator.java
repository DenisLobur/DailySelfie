package com.example.selfie.model.mediator;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.example.selfie.model.mediator.webdata.Selfie;
import com.example.selfie.model.mediator.webdata.SelfieServiceProxy;
import com.example.selfie.model.provider.SelfieProviderContract;
import com.example.selfie.utils.Constants;
import com.example.selfie.view.LoginActivity;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.ArrayList;
import java.util.Collection;

public class SelfieDataMediator {

    private static String TAG = SelfieDataMediator.class.getSimpleName();
    public static final String STATUS_UPLOAD_SUCCESSFUL = "Upload succeeded";
    public static final String STATUS_UPLOAD_ERROR_FILE_TOO_LARGE = "Upload failed: File too big";
    public static final String STATUS_UPLOAD_ERROR = "Upload failed";
    public static final String STATUS_UPLOAD_ERROR_FORBIDDEN = "Upload not permitted";
    private static SelfieServiceProxy mSelfieServiceProxy;

    public SelfieDataMediator() {

    }

    public static void init(String user, String password) {
        mSelfieServiceProxy = new SecuredRestBuilder()
                .setLoginEndpoint(Constants.SERVER_URL + SelfieServiceProxy.TOKEN_PATH)
                .setUsername(user)
                .setPassword(password)
                .setClientId("mobile")
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(Constants.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(SelfieServiceProxy.class);
    }

    public boolean isLoggedIn() {
        try {
            return mSelfieServiceProxy.isPicLoggedIn();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
    }

    public ArrayList<Selfie> getPictureList() {
        if (mSelfieServiceProxy != null)
            try {
                Collection<Selfie> pictures = mSelfieServiceProxy.getPictureList();
                ArrayList<Selfie> picList = new ArrayList<>();
                picList.addAll(pictures);
                return picList;
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
                return new ArrayList<>();
            }

        return null;
    }

    public static synchronized SelfieServiceProxy getSelfieServiceProxy(Context context) {
        if (mSelfieServiceProxy != null) {
            return mSelfieServiceProxy;
        } else {
            Log.e(TAG, "User not logged in");
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            return null;
        }
    }

    public static synchronized void clearSelfieServiceProxy() {
        mSelfieServiceProxy = null;
    }

    /**
     * Uploads the Selfie having the given Id.  This Id is the Id of
     * Selfie in Android Selfie Content Provider.
     *
     * @param selfieUri Id of the Selfie to be uploaded.
     * @return A String indicating the status of the selfie upload operation.
     */
    public String uploadSelfie(Context context, Uri selfieUri) {
        if (getSelfieServiceProxy(context) == null) {
            return null;
        }

        long localId = ContentUris.parseId(selfieUri);
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentValues updateValues = new ContentValues();
        updateValues.put(MediaStore.Images.ImageColumns.DESCRIPTION, "title");
        String selection = "" + MediaStore.Images.ImageColumns._ID + " = '" + localId + "'";
        context.getContentResolver().update(uri, updateValues, selection, null);

        //query MediaStore
        String[] queryProjection = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.SIZE,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        Cursor c = context.getContentResolver().query(uri, null,
                null, null, null);

        if (c != null && c.moveToFirst()) {
            //retrieve data from MediaStore
            int index = c.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String location = c.getString(index);
            index = c.getColumnIndex(MediaStore.Images.ImageColumns.SIZE);
            String size = c.getString(index);
            index = c.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE);
            String contentType = c.getString(index);
            c.close();

            Selfie selfie = new Selfie("", "name", "", 2000l, 1l, 11f);
            selfie.setContentType(contentType);
            selfie.setLocation(location);
            selfie.setStarRating(10f);
            selfie.setLikes(100l);
            try {
                Selfie cloudId = mSelfieServiceProxy.addPicture(selfie);
            }catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }

        //~~~~~~~~~~

        /*// Get the path of selfie file from selfieUri.
        String filePath = SelfieMediaStoreUtils.getPath(context,
                selfieUri);

        // Get the Selfie from Android Selfie Content Provider having
        // the given filePath.
        Selfie androidSelfie = SelfieMediaStoreUtils.getSelfie(context, filePath);

        // Check if any such Selfie exists in Android Selfie Content
        // Provider.
        if (androidSelfie != null) {
            // Add the metadata of the Selfie to the Selfie Service and
            // get the resulting Selfie that contains additional
            // meta-data (e.g., Id and ContentType) generated by the
            // Selfie Service.
            Selfie receivedSelfie = null;
            try {
                //receivedSelfie = mSelfieServiceProxy.sendPictureData(androidSelfie);
            } catch (RetrofitError e) {
                Log.e(TAG, e.getMessage());
                return STATUS_UPLOAD_ERROR_FORBIDDEN;
            }

            // Check if the Selfie Service returned any Selfie metadata.
            if (receivedSelfie != null) {
                // Prepare to Upload the Selfie data.

                // Create an instance of the file to upload.
                File selfieFile = new File(filePath);

                // Check if the file size is less than the size of the
                // selfie that can be uploaded to the Selfie Service.
                if (selfieFile.length() < Constants.MAX_SIZE_MEGA_BYTE) {
                    // Upload the Selfie data to the Selfie Service and get the
                    // status of the uploaded selfie data.
                   *//* SelfieStatus status =
                            mSelfieServiceProxy.setSelfieData
                                    (receivedSelfie.getId(),
                                            new TypedFile(receivedSelfie.getContentType(),
                                                    selfieFile));*//*

                    // Check if the Status of the Selfie is ready or not.
                    //if (status.getState() == SelfieStatus.SelfieState.READY)
                    // Selfie successfully uploaded.
                    return STATUS_UPLOAD_SUCCESSFUL;
                } else
                    // Selfie can't be uploaded due to large selfie size.
                    return STATUS_UPLOAD_ERROR_FILE_TOO_LARGE;
            }
        }*/

        // Error occurred while uploading the selfie.
        return "";
    }

    /**
     * Get the List of Selfies from Selfie Service.
     *
     * @return the List of Selfies from Server or null if there is
     * failure in getting the Selfies.
     */
    /*public List<Selfie> getSelfieList(Context context) {
        if (getSelfieServiceProxy(context) == null) {
            return null;
        }
        ArrayList<Selfie> list = (ArrayList<Selfie>)mSelfieServiceProxy.getSelfieList();

        return list;
    }
*/
    /*public void downloadSelfie(Context context, Selfie selfie) {
        if (getSelfieServiceProxy(context) == null) {
            return;
        }

        Response response = mSelfieServiceProxy.getData(selfie.getId());

        if (response.getStatus() == HttpURLConnection.HTTP_FORBIDDEN) {
            Log.e(TAG, "User not allowed to perform download operation");
            return;
        }

        // save in disk the downloaded selfie
        File storedFile = SelfieStorageUtils.storeSelfieInExternalDirectory(context, response, "selfie" + selfie.getId() + ".mpg");

        if (storedFile != null) {
            selfie.setLocalUrl(storedFile.getPath());

            // persist in local db
            saveSelfieInLocalStorage(context, selfie);
        }
    }*/
    private void saveSelfieInLocalStorage(Context context, Selfie selfie) {
        Selfie selfieStored = getSelfieFromLocalStorage(context, selfie.getId());

        if (selfieStored == null) {
            ContentValues values = new ContentValues();
            values.put(SelfieProviderContract.SelfieTable._ID, selfie.getId());
            values.put(SelfieProviderContract.SelfieTable.COLUMN_TITLE, selfie.getTitle());
            values.put(SelfieProviderContract.SelfieTable.COLUMN_CONTENT_TYPE, selfie.getContentType());
            values.put(SelfieProviderContract.SelfieTable.COLUMN_DURATION, selfie.getDuration());
            values.put(SelfieProviderContract.SelfieTable.COLUMN_STAR_RATING, selfie.getStarRating());

            context.getContentResolver().insert(SelfieProviderContract.SelfieTable.CONTENT_URI, values);
        } else {
            ContentValues values = new ContentValues();

            String selection = SelfieProviderContract.SelfieTable._ID + "=?";
            String[] args = new String[]{String.valueOf(selfie.getId())};

            context.getContentResolver().update(SelfieProviderContract.SelfieTable.CONTENT_URI, values, selection, args);
        }
    }

    public Selfie getSelfieFromLocalStorage(Context context, long selfieId) {
        String selection = SelfieProviderContract.SelfieTable._ID + "=?";
        String[] args = new String[]{String.valueOf(selfieId)};

        ContentResolver contentResolver = context.getContentResolver();
        try (Cursor cursor = contentResolver.query(SelfieProviderContract.SelfieTable.CONTENT_URI, null, selection, args, null)) {
            if (cursor.moveToFirst()) {
                Selfie localSelfie = new Selfie();
                long id = cursor.getLong(cursor.getColumnIndex(SelfieProviderContract.SelfieTable._ID));
                String title = cursor.getString(cursor.getColumnIndex(SelfieProviderContract.SelfieTable.COLUMN_TITLE));
                int duration = cursor.getInt(cursor.getColumnIndex(SelfieProviderContract.SelfieTable.COLUMN_DURATION));
                String contentType = cursor.getString(cursor.getColumnIndex(SelfieProviderContract.SelfieTable.COLUMN_CONTENT_TYPE));
                float starRating = cursor.getFloat(cursor.getColumnIndex(SelfieProviderContract.SelfieTable.COLUMN_STAR_RATING));

                localSelfie.setId(id);
                localSelfie.setTitle(title);
                localSelfie.setDuration(duration);
                localSelfie.setContentType(contentType);
                localSelfie.setStarRating(starRating);

                return localSelfie;
            }
        }

        return null;
    }

    /*public void rateSelfie(Context context, Selfie selfie, double rating) {
        if (getSelfieServiceProxy(context) == null) {
            return;
        }

        Log.i(SelfieDataMediator.class.getSimpleName(), "Sending rating for selfie: " + rating);

        AverageSelfieRating selfieUpdated = null;
        try {
            selfieUpdated = mSelfieServiceProxy.rateSelfie(selfie.getId(), rating);
        }catch (RetrofitError e){
            Log.e(TAG, e.getMessage());
            return;
        }

        selfieUpdated = mSelfieServiceProxy.getSelfieRating(selfie.getId());

        ContentValues values = new ContentValues();
        values.put(SelfieProviderContract.SelfieTable.COLUMN_STAR_RATING, selfieUpdated.getRating());

        String selection = SelfieProviderContract.SelfieTable._ID + "=?";
        String[] args = new String[]{String.valueOf(selfieUpdated.getSelfieId())};

        context.getContentResolver().update(SelfieProviderContract.SelfieTable.CONTENT_URI, values, selection, args);
    }*/
}
