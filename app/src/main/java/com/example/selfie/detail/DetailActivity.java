package com.example.selfie.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.selfie.LifecycleLoggerActivity;
import com.example.selfie.MainActivity;
import com.example.selfie.R;

/**
 * Created by denis on 10/11/15.
 */
public class DetailActivity extends LifecycleLoggerActivity implements DetailPresenter.DetailView {

    private ImageView detailPic;
    private int imageId;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailPic = (ImageView)findViewById(R.id.detail_img);

        Intent intent = getIntent();
        image = intent.getParcelableExtra(MainActivity.IMAGE_BITMAP);
        imageId = intent.getIntExtra(MainActivity.IMAGE_ID, -1);
        detailPic.setImageBitmap(image);
    }

    @Override
    public void setDetailPic(Bitmap bm) {
        detailPic.setImageBitmap(bm);
    }
}
