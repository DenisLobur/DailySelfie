package com.example.selfie.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import com.example.selfie.model.mediator.webdata.Selfie;

import java.io.File;

/**
 * SelfieMediaStoreUtils contains helper methods to access
 * SelfieMediaStore to get metadata of Selfie.  It works with all Uri
 * schemes.
 */
public class SelfieMediaStoreUtils {
    /**
     * Content Uri scheme for Downloads Provider.
     */
    public static final String DOWNLOADS_PROVIDER_PATH =
        "content://downloads/public_downloads";   
       
    /**
     * Gets the Selfie from MediaMetadataRetriever
     * by a given path of the selfie file.
     * 
     * @param context
     * @param filePath of selfie
     * @return Selfie having the given filePath
     */
    public static Selfie getSelfie(Context context,
                                 String filePath) {
        // Get the MediaMetadataRetriever for retrieving
        // meta data from an input media file.
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        
        //Set the dataSource to the selfie file path.
        retriever.setDataSource(filePath);
        
        // Get the selfie selfie file name.
        final String title =
            new File(filePath).getName();
        
        //Get the duration of the Selfie.
        /*final long duration =
            Long.parseLong
            (retriever.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION));*/
        
        // Get the MimeType of the Selfie.
        final String contentType =
            retriever.extractMetadata
            (MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
       
        // Create a new Selfie containing the meta-data.
        return new Selfie("", title, "", 1000l, 1l, 3f);
    }
    
    /** 
     * Get a Selfie file path from a Uri. This will get the the path
     * for Storage Access Framework Documents, as well as the _data
     * field for the MediaStore and other file-based ContentProviders.
     * 
     * @param context The context. 
     * @param uri The Uri to query. 
     * 
     * return selfieFilePath
     */ 
    public static String getPath(final Context context,
                                 final Uri uri) {
        // Check if the version of current device is greater 
        // than API 19 (KitKat).
        final boolean isKitKat =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
     
        // DocumentProvider.
        if (isKitKat
            && DocumentsContract.isDocumentUri(context,
                uri)) {
            // ExternalStorageProvider 
            if (isExternalStorageDocument(uri)) {
                final String docId =
                    DocumentsContract.getDocumentId(uri);
                final String[] split =
                    docId.split(":");
                final String type =
                    split[0];
     
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() 
                        + "/" 
                        + split[1];
                } 
            } 
            // DownloadsProvider 
            else if (isDownloadsDocument(uri)) {
                final String id =
                    DocumentsContract.getDocumentId(uri);
                final Uri contentUri =
                    ContentUris.withAppendedId
                    (Uri.parse(DOWNLOADS_PROVIDER_PATH),
                            Long.valueOf(id));
     
                return getSelfieDataColumn(context,
                        contentUri,
                        null,
                        null);
            } 
            // MediaProvider 
            else if (isMediaDocument(uri)) {
                final String docId =
                    DocumentsContract.getDocumentId(uri);
                final String[] split =
                    docId.split(":");
                
                final Uri contentUri = null;
                    //MediaStore.Selfie.Media.EXTERNAL_CONTENT_URI;
               
                final String selection = "_id = ?";
                final String[] selectionArgs = new String[] {
                    split[1]
                }; 
     
                // Get the FilePath from Selfie MediStore
                // for given Uri, selection, selectionArgs.
                return getSelfieDataColumn(context,
                        contentUri,
                        selection,
                        selectionArgs);
            } 
        } 
        // MediaStore (and general) .
        else if ("content".equalsIgnoreCase(uri.getScheme())) 
            return getSelfieDataColumn(context,
                    uri,
                    null,
                    null);
        // File 
        else if ("file".equalsIgnoreCase(uri.getScheme())) 
            return uri.getPath();
     
        return null; 
    } 
     
    /** 
     * Get the value of the data column for this Uri. This is useful
     * for MediaStore Uris, and other file-based ContentProviders.
     * 
     * @param context The context. 
     * @param uri The Uri to query. 
     * @param selection (Optional) Filter used in the query. 
     * @param selectionArgs (Optional) Selection arguments used in the query. 
     * @return The value of the _data column, which is typically a file path.
     */ 
    private static String getSelfieDataColumn(Context context,
                                             Uri uri,
                                             String selection,
                                             String[] selectionArgs) {
        // Projection used to query Android Selfie Content Provider.
        final String[] projection = {
            MediaStore.Images.Media.DATA
        }; 
     
        //Query and get a cursor to Android Selfie
        // Content Provider.
        try (Cursor cursor =
                 context.getContentResolver().query(uri,
                                                    projection,
                                                    selection,
                                                    selectionArgs,
                                                    null)) {
                // If selfie is present, get the file path of the selfie.
                if (cursor != null 
                    && cursor.moveToFirst()) 
                    return cursor.getString(cursor.getColumnIndexOrThrow
                                            (MediaStore.Images.Media.DATA));
            } 

        // No selfie present. returns null.
        return null; 
    } 
    
    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is ExternalStorageProvider. 
     */ 
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents"
            .equals(uri.getAuthority());
    } 
     
    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is DownloadsProvider. 
     */ 
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents"
            .equals(uri.getAuthority());
    } 
     
    /** 
     * @param uri The Uri to check. 
     * @return Whether the Uri authority is MediaProvider. 
     */ 
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents"
            .equals(uri.getAuthority());
    } 
}
