package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class supportPage extends AppCompatActivity {

    public EditText etAdSoyad;
    public EditText etEposta;
    public EditText etMesaj;
    public Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_page);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etAdSoyad = findViewById(R.id.etAdSoyad);
        etEposta = findViewById(R.id.etEposta);
        etMesaj = findViewById(R.id.etMesaj);
        sendButton = findViewById(R.id.sendButton);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
