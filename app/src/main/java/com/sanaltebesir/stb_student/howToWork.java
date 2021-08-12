package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

public class howToWork extends AppCompatActivity {

    public VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_work);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("http://www.sanaltebesir.com/android/videos/howtoworkstudent.mp4");
        //videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
