package com.example.selfie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.selfie.detail.DetailActivity;
import com.example.selfie.model.mediator.webdata.Selfie;
import com.example.selfie.presenter.MainPresenter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends LifecycleLoggerActivity implements MainPresenter.MainView, AdapterView.OnItemClickListener {

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
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.photos_list);
        db = new DataBaseHandler(this);
        button = (Button)findViewById(R.id.send_to_server);
        button.setOnClickListener(new SendToServerListener());

        /*List<Selfie> allSelfies = db.getAllSelfies();
        for (Selfie sf : allSelfies) {
            selfieList.add(sf);
        }*/

        //adapter = new SelfieListAdapter(this, selfieList);
//        getAllFiles();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private class SendToServerListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (selfie != null) {

                //PictureApi.uploadPicture("1", )
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selfie_list, menu);
        return true;
    }

    private boolean edit;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
//            dispatchTakePictureIntent();
            takeSelfie();
            return true;
        } else if (id == R.id.action_edit) {
            adapter.setVisible(edit);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (edit) {
            adapter.setVisible(false);
            edit = false;
        } else {
            super.onBackPressed();
        }
    }

    private void takeSelfie() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        /*cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 0);
        cameraIntent.putExtra("aspectY", 0);
        cameraIntent.putExtra("outputX", 200);
        cameraIntent.putExtra("outputY", 150);*/
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ioe){
                Log.d(TAG, ioe.getMessage());
            }
            if(photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                takePictureIntent.putExtra("data", photoFile.getAbsolutePath());
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat ("yyMMdd_HHmmss").format(new Date());
        String imageFileName = "SELF_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return  image;
    }

    private String getSelfieTime() {
        return new SimpleDateFormat ("yyMMdd_HH:mm:ss").format(new Date());
    }

    private Selfie selfie;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Uri pictureUri = data.getData();
            if (extras != null) {
                Bitmap yourImage = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
//                selfie = new Selfie(getSelfieTime(), imageInByte);
                //db.sendPictureData(selfie);
                selfieList.add(selfie);
                adapter.notifyDataSetChanged();
                //Intent i = new Intent(MainActivity.this, DetailActivity.class);
                //startActivity(i);
                //finish();
            }
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Bitmap imageBitmap =
            //Uri tempUri = getImageUri(this, imageBitmap);
            //File finalFile = new File(getRealPathFromURI(tempUri));
            //listFragment.getAdapter().addItem(new Selfie(mCurrentPhotoPath, mCurrentPhotoPath));
            //getAllFiles();
            //new Selfie(imageBitmap.toString(), imageBitmap.get);
            //mImageView.setImageBitmap(imageBitmap);

//            adapter.addItem(new Selfie(currentPhotoPath, currentPhotoPath));
        }

    }

    private void getAllFiles(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File [] allFiles = dir.listFiles();
        for(File file : allFiles) {
            if (file.getName().contains("SELF")){
                Log.d(TAG, "FOUND: " + file.getName());
                //listFragment.getAdapter().addItem(new Selfie(file.getName(), file.getAbsolutePath()));
                //adapter.addItem(new Selfie(file.getName(), file.getAbsolutePath()));
            }
        }
    }


    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", "Description");
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void showProgress(boolean show) {
        View loadingView = findViewById(R.id.progress_layout);
        if (loadingView != null) {
            int visibility = show ? View.VISIBLE : View.GONE;
            loadingView.setVisibility(visibility);
        }
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "position:" + position + " id: " + id);
        //image = selfieList.get(position).getImage();
        //imageId = selfieList.get(position).getID();

        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        imageBm = BitmapFactory.decodeStream(imageStream);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(IMAGE_BITMAP, imageBm);
        intent.putExtra(IMAGE_ID, imageId);
        startActivity(intent);
    }
}
