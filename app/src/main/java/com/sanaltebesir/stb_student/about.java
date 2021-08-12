package com.sanaltebesir.stb_student;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tw1 = findViewById(R.id.textView1);
        TextView tw14 = findViewById(R.id.textView14);
        TextView tw15 = findViewById(R.id.textView15);
        TextView tw17 = findViewById(R.id.textView17);
        TextView tw16 = findViewById(R.id.textView16);
        TextView tw23 = findViewById(R.id.textView23);
        TextView tw24 = findViewById(R.id.textView24);

        tw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Nasıl Çalışır
                Intent myIntent = new Intent(getApplicationContext(), howToWork.class);
                startActivity(myIntent);
            }
        });

        tw14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Sıkça Sorulan Sorular
                Intent myIntent = new Intent(getApplicationContext(), faqPage.class);
                startActivity(myIntent);
            }
        });

        tw15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Destek İletişim
                Intent myIntent = new Intent(getApplicationContext(), supportPage.class);
                startActivity(myIntent);
            }
        });

        tw17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Hakkımızda
                Intent myIntent = new Intent(getApplicationContext(), aboutUs.class);
                startActivity(myIntent);
            }
        });

        tw16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Bizi Değerlendirin
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
            }
        });

        tw23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getApplicationContext(), privacyPage.class);
                startActivity(myIntent);

            }
        });

        tw24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getApplicationContext(), termsConditions.class);
                startActivity(myIntent);

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
