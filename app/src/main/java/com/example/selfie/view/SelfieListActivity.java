package com.example.selfie.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.selfie.R;
import com.example.selfie.SelfieListAdapter;
import com.example.selfie.common.GenericActivity;
import com.example.selfie.common.Utils;
import com.example.selfie.detail.DetailActivity;
import com.example.selfie.model.mediator.webdata.Selfie;
import com.example.selfie.model.services.UploadSelfieService;
import com.example.selfie.presenter.SelfiePresenter;
import com.example.selfie.utils.SelfieStorageUtils;
import com.example.selfie.view.ui.SelfieAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SelfieListActivity extends GenericActivity<SelfiePresenter.View, SelfiePresenter> implements SelfiePresenter.View, AdapterView.OnItemClickListener {

    private final int REQUEST_SELFIE_CAPTURE = 99;
    public static final String IMAGE_BITMAP = "image_bitmap";
    public static final String IMAGE_ID = "image_id";

    private UploadResultReceiver mUploadResultReceiver;
    private Button mUploadSelfieButton;
    private ListView mSelfiesList;
    private Uri mSelfieUri;
    private SelfieListAdapter adapter;
    private ArrayList<Selfie> pictureList = new ArrayList<>();
    private Selfie selfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mUploadResultReceiver = new UploadResultReceiver();
        mSelfiesList = (ListView) findViewById(R.id.selfie_list);
        mUploadSelfieButton = (Button) findViewById(R.id.send_to_server);
        mUploadSelfieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        adapter = new SelfieListAdapter(this, pictureList);
        mSelfiesList.setAdapter(adapter);
        mSelfiesList.setOnItemClickListener(this);
        super.onCreate(savedInstanceState, SelfiePresenter.class, this);
//        getOps(). getSelfieList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(UploadSelfieService.ACTION_UPLOAD_SERVICE_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mUploadResultReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUploadResultReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        byte [] image = pictureList.get(position).getPictureBlob();
//        imageId = pictureList.get(position).getID();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap imageBm = BitmapFactory.decodeStream(imageStream);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(IMAGE_BITMAP, imageBm);
//        intent.putExtra(IMAGE_ID, imageId);
        startActivity(intent);
    }

    private class UploadResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //getOps().getSelfieList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selfieUri = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELFIE_CAPTURE) {
                //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                selfieUri = mSelfieUri;
                createPictureThumb(data);
                //getOps().uploadSelfie(selfieUri);//NPE in getData();
            }

            if (selfieUri != null){
                Utils.showToast(this, "Uploading selfie");
                //getOps().uploadSelfie(selfieUri);
            }
        }

        if (selfieUri == null) {
            Utils.showToast(this, "Could not get selfie to upload");
        }
    }

    private void createPictureThumb(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap yourImage = extras.getParcelable("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
            selfie = new Selfie("xHGGGVHghghghgGHG", imageInByte);
            //db.sendPictureData(selfie);
            pictureList.add(selfie);
            Uri tempUri = getImageUri(this, yourImage);
            adapter = new SelfieListAdapter(this, pictureList);
            mSelfiesList.setAdapter(adapter);
            adapter.swap(pictureList);
            //adapter.notifyDataSetChanged();
            //Intent i = new Intent(MainActivity.this, DetailActivity.class);
            //startActivity(i);
            //finish();
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", "Description");
        return Uri.parse(path);
    }

    @Override
    public void setAdapter(SelfieAdapter selfieAdapter) {
        mSelfiesList.setAdapter(selfieAdapter);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selfie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            //dispatchTakePictureIntent();
            mSelfieUri = SelfieStorageUtils.getSelfieUri(getApplicationContext());
            //dispatchCameraIntent(mSelfieUri);
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchCameraIntent(Uri selfieUri) {
        Intent takeSelfieIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, selfieUri);
        if (takeSelfieIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeSelfieIntent, REQUEST_SELFIE_CAPTURE);
        }
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
                startActivityForResult(takePictureIntent, REQUEST_SELFIE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        String imageFileName = "SELF_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        //currentPhotoPath = image.getAbsolutePath();
        return  image;
    }


}
