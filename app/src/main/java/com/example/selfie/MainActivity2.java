package com.example.selfie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.example.selfie.model.mediator.webdata.Selfie;
import com.example.selfie.web.PictureApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by denis on 10/25/15.
 */
public class MainActivity2 extends Activity implements View.OnClickListener{

    //TODO: 1) get Uri of newly created file
    //      2) put this uri to intent extra
    //      3) on activity result get this uri and create file with it
    //      4) send this file to server

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String IMAGE_BITMAP = "image_bitmap";
    public static final String IMAGE_ID = "image_id";

    private ArrayList<Selfie> selfieList = new ArrayList<Selfie>();
    private ListView listView;
    private SelfieListAdapter adapter;
    private String currentPhotoPath;
    private byte [] image;
    private int imageId;
    private Bitmap imageBm;
    private DataBaseHandler db;
    private Toolbar toolbar;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //listView = (ListView)findViewById(R.id.photos_list);
        db = new DataBaseHandler(this);
        //button = (Button)findViewById(R.id.send_to_server);
        button.setOnClickListener(new SendToServerListener());

        /*List<Selfie> allSelfies = db.getAllSelfies();
        for (Selfie sf : allSelfies) {
            selfieList.add(sf);
        }*/

        //adapter = new SelfieListAdapter(this, selfieList);
//        getAllFiles();
        listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if (id == R.id.action_camera) {
//            dispatchTakePictureIntent();
            dispatchTakePictureIntent();
            //return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ioe){
                Log.d("exception", ioe.getMessage());
            }
            if(photoFile != null) {
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                takePictureIntent.putExtra("data", photoFile.getAbsolutePath());
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        String imageFileName = "SELF_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return  image;
    }

    private Selfie selfie;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                bitmap = (Bitmap)data.getExtras().get("data");
                Uri uri = getImageUri(this, bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                int i = 0;
//                selfie = new Selfie(String.valueOf(i++), imageInByte);
                PictureApi.uploadPicture(selfie);
                //db.sendPictureData(selfie);
                selfieList.add(selfie);
                adapter.notifyDataSetChanged();
            }

        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onClick(View v) {

    }

    private class SendToServerListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //if (selfie != null) {

                //PictureApi.uploadPicture(selfie);
            //}
        }
    }
}
