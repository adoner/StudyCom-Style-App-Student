package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

public class soruBank extends AppCompatActivity {

    private ListView lvSorubank;
    public  String[] lectures = {"Matematik","Geometri","Fizik","Kimya","Biyoloji","Fen Bilgisi",
        "Türkçe","Tarih","Coğrafya","Felsefe ve Din Bilgisi","Sosyal Bilgiler","İngilizce","Almanca","Fransızca"};
    public ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soru_bank);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvSorubank = findViewById(R.id.lvSorubank);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lectures);
        lvSorubank.setAdapter(listAdapter);

        lvSorubank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(view.getContext(), soruBankItem.class);
                myIntent.putExtra("lecture", listAdapter.getItem(position));
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
