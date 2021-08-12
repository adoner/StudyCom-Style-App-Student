package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class reviewQuestion extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/review.php";
    private JSONObject json;
    private ProgressDialog pDialog;
    private RatingBar ratingBar;
    private Button btnSubmit;
    public String rating;
    public String comment;
    public String troubles;
    public String questionid;
    public String [] trouble = {"","Soru Hatalı Çözülmüş","Fotoğraf Kalitesi Düşük","Cevap Çok Karışık","Yazılar Okunmuyor"};
    private Spinner spTrouble;
    private EditText etComment;
    ArrayList<HashMap<String, String>> questionList;
    JSONParser jsonParser = new JSONParser();
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_question);
        session = new SessionHandler(getApplicationContext());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating((float) 0.0);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize((float) 0.5);
        btnSubmit = findViewById(R.id.btnSubmit);
        spTrouble = findViewById(R.id.troubleSpinner);
        etComment = findViewById(R.id.studentComment);
        Intent intent = getIntent();
        questionid = intent.getStringExtra("questionid");
        //Toast.makeText(getApplicationContext(), questionid, Toast.LENGTH_LONG).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,trouble);
        spTrouble.setAdapter(adapter);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                new GetQuestionData().execute();
                //Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class GetQuestionData extends AsyncTask<Void,String,Void> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(reviewQuestion.this);
            pDialog.setMessage("Lütfen bekleyiniz...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected Void doInBackground(Void... voids){

            rating = String.valueOf(ratingBar.getRating());
            comment = etComment.getText().toString().trim();
            troubles = spTrouble.getSelectedItem().toString().trim();

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("questionid", questionid));
                params.add(new BasicNameValuePair("rating", rating));
                params.add(new BasicNameValuePair("comment", comment));
                params.add(new BasicNameValuePair("troubles", troubles));
                json = jsonParser.makeHttpRequest(phpUrl,
                        "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void aVoid){

            super.onPostExecute(aVoid);
            pDialog.dismiss();

            try {

                Intent i = new Intent(getApplicationContext(), questionBox.class);
                startActivity(i);
                finish();

            }catch(NullPointerException e){

                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
