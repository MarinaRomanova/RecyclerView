package com.example.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookDetailActivity extends AppCompatActivity {
    TextView textViewTitle;
    ImageView imageView;
    TextView textViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        textViewTitle = findViewById(R.id.title_detail);
        imageView = findViewById(R.id.bookImage_detail);
        textViewDescription = findViewById(R.id.subTitle_detail);

        textViewTitle.setText(getIntent().getStringExtra("title"));
        Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
                .into(imageView);
        textViewDescription.setText(getIntent().getStringExtra("description"));
    }
}
