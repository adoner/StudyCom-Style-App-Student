package com.sanaltebesir.stb_student;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class uploadServerData extends AppCompatActivity {

    String[] lecture = {"Matematik", "Geometri", "Fizik", "Kimya", "Biyoloji","Fen Bilgisi",
            "Türkçe","Tarih","Coğrafya","Sosyal Bilgiler","Felsefe ve Din Kültürü","İngilizce","Almanca","Fransızca"};
    public ProgressBar progressBar;
    private ProgressDialog pDialog;
    private static final String KEY_EMPTY = "";
    public static String phpUrl = "http://www.sanaltebesir.com/android/student/UpdateServerData.php";
    public JSONObject json;
    JSONParser jsonParser = new JSONParser();
    private SessionHandler session;
    public int progressStatus = 0;
    public Handler hdlr = new Handler();
    public Boolean progress = true;
    public TextView prgTxt;
    public String notification;
    public String userid;
    public String imagename;
    private Spinner spin;
    public EditText edtxt;
    public Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_server_data);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionHandler(getApplicationContext());
        userid = session.getUserDetails().userid;

        Intent intent = getIntent();
        imagename = intent.getStringExtra("imagename");
        submitBtn = findViewById(R.id.submit_btn);
        edtxt = findViewById(R.id.upload_edtxt);
        spin = findViewById(R.id.lectures_spinner);
        progressBar = findViewById(R.id.prgBar);
        prgTxt = findViewById(R.id.prgrs_txtvw);

        submitBtn.setEnabled(false);
        edtxt.setEnabled(false);
        spin.setEnabled(false);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,lecture);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setPrompt("İlgili Dersi Seçiniz..");
        spin.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_row_nothing_selected, this));

        SharedPreferences preferences=getSharedPreferences("UploadProgress",MODE_PRIVATE);
        progress = preferences.getBoolean("progress", false);
        notification = preferences.getString("notification", "");
        new GetProgress().execute();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

    }

    private class GetProgress extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void...voids) {

            try {

                new Thread(new Runnable() {

                    public void run() {

                        while (progressStatus < 100) {

                                progressStatus += 1;

                            // Update the progress bar and display the current value in text view
                            hdlr.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                }
                            });
                            try {

                                // Sleep for 100 milliseconds to show the progress slowly.
                                Thread.sleep(100);

                            } catch (InterruptedException e) {

                                e.printStackTrace();

                            }
                        }
                    }
                }).start();

            }catch(RuntimeException e){

                e.printStackTrace();
            }

            Boolean percent = true;
            return percent;
        }

        protected void onPostExecute(Boolean percent){
            super.onPostExecute(percent);

            try {

                if (percent) {
                    prgTxt.setText(notification);
                    progressBar.setVisibility(View.GONE);
                    submitBtn.setEnabled(true);
                    edtxt.setEnabled(true);
                    spin.setEnabled(true);
                }

            }catch(RuntimeException e){

                e.printStackTrace();

            }
        }
    }

   private class UpdateServerData extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(uploadServerData.this);
            pDialog.setMessage("Lütfen bekleyiniz...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void...voids) {

            try{

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("lecturename", spin.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("notes", edtxt.getText().toString()));
                params.add(new BasicNameValuePair("imagename", imagename));
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }
            return null;
        }

       @Override
       protected void onPostExecute(String s) {

           super.onPostExecute(s);
           // Dismiss the progress dialog
           if (pDialog.isShowing())
               pDialog.dismiss();
               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(intent);

       }
    }

    public void submitData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(uploadServerData.this);
        builder.setTitle("Soru Gönderimini Onayla");
        builder.setMessage("Sorun eğitmen ağımızdaki eğitmenlere ulaştırılacak, hesabından 1 soru kredisi düşülecek.");
        builder.setNegativeButton("İptal", null);
        builder.setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new UpdateServerData().execute();

            }
        });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
