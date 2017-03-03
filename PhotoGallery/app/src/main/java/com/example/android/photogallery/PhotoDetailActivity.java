package com.example.android.photogallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        ImageView mImageView = (ImageView) findViewById(R.id.photo_detail_image_view);

        String url = getIntent().getStringExtra("url");

        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.bill_up_close)
                .into(mImageView);
    }
}
