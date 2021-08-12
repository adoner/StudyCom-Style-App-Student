package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class soruBankView extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/getSoruBankLinks.php";
    private static String phpUrl2 = "http://www.sanaltebesir.com/android/student/checkPackageStatus.php";
    private static final String KEY_STATUS = "status";
    private static final String KEY_USERID = "userid";
    private static final String KEY_MESSAGE = "message";
    private JSONObject json;
    ArrayList<HashMap<String, String>> imageLink;
    ArrayList<HashMap<String, String>> userCheck;
    JSONParser jsonParser = new JSONParser();
    public String[] urls;
    public ImageView ivSoruBank;
    public Button btnNext;
    public Button btnPrev;
    public Button btnSolution;
    public String subject;
    public int iter = 0;
    public String userid;
    public String currentDate;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soru_bank_view);
        session = new SessionHandler(getApplicationContext());
        imageLink = new ArrayList<>();
        userCheck = new ArrayList<>();
        ivSoruBank = findViewById(R.id.ivSoruBankView);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnSolution = findViewById(R.id.btnSolution);
        btnPrev.setEnabled(false);
        userid = session.getUserDetails().userid;
        Intent myIntent = getIntent();
        subject = myIntent.getStringExtra("subject");
        currentDate = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());

        new GetImageLink().execute();
        new CheckUserPackageStatus().execute();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    iter = iter + 1;
                    if (iter < imageLink.size()) {

                        Picasso.get().load("http://www.sanaltebesir.com/uploads/questions/"
                                +imageLink.get(iter).get("questionlink")).into(ivSoruBank);

                        if (iter == imageLink.size() - 1) {
                            btnNext.setEnabled(false);
                        }
                        if (iter > 0) {
                            btnPrev.setEnabled(true);
                        }
                    }
                }catch (IndexOutOfBoundsException e){

                    e.printStackTrace();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    iter = iter - 1;
                    if (iter >= 0) {

                        Picasso.get().load("http://www.sanaltebesir.com/uploads/questions/"
                                + imageLink.get(iter).get("questionlink")).into(ivSoruBank);

                        if (iter == 0) {
                            btnPrev.setEnabled(false);
                        }
                        if (iter < imageLink.size() - 1) {
                            btnNext.setEnabled(true);
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        });

        btnSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPackage();

                /*if(Integer.parseInt(userCheck.get(0).get("status"))==1){

                    Intent intent = new Intent(getApplicationContext(), solutionView.class);
                    intent.putExtra("questionid", imageLink.get(iter).get("questionid"));
                    startActivity(intent);
                }

                if(Integer.parseInt(userCheck.get(0).get("status"))==0){

                    AlertDialog.Builder builder = new AlertDialog.Builder(soruBankView.this);
                    builder.setTitle("Sanal Tebeşir");
                    builder.setMessage("Geçerli bir paketiniz yoktur, lütfen paketlerimizden satın alınız..!");
                    builder.setNegativeButton("Daha Sonra", null);
                    builder.setPositiveButton("Paketleri İncele", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                     Intent intent = new Intent(getApplicationContext(), packageList.class);
                     startActivity(intent);

                        }
                    });
                    builder.show();
                }*/
            }
        });
    }

    private void checkPackage() {

        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERID, userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, phpUrl2, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(soruBankView.this);
                                builder.setTitle("Sanal Tebeşir");
                                builder.setMessage("Geçerli bir paketiniz yoktur, lütfen paketlerimizden satın alınız..!");
                                builder.setNegativeButton("Daha Sonra", null);
                                builder.setPositiveButton("Paketleri İncele", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent intent = new Intent(getApplicationContext(), packageList.class);
                                        startActivity(intent);

                                    }
                                });
                                builder.show();

                            }else if(response.getInt(KEY_STATUS) == 1){

                                Intent intent = new Intent(getApplicationContext(), solutionView.class);
                                intent.putExtra("questionid", imageLink.get(iter).get("questionid"));
                                startActivity(intent);

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    class GetImageLink extends AsyncTask<Void,Void,Void> {

        protected Void doInBackground(Void... voids){

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("subject", subject));
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("sbQuestions");

                for(int i = 0; i<arr.length();i++){

                    String questionlink = arr.getJSONObject(i).getString("questionlink");
                    String questionid = arr.getJSONObject(i).getString("questionid");

                    HashMap<String, String> qList = new HashMap<>();

                    // adding each child node to HashMap key => value
                    qList.put("questionlink", questionlink);
                    qList.put("questionid", questionid);

                    // adding contact to contact list
                    imageLink.add(qList);

                }

            }catch (JSONException e) {

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

            Picasso.get().load("http://www.sanaltebesir.com/uploads/questions/" + imageLink.get(0).get("questionlink")).into(ivSoruBank);

        }
    }

    class CheckUserPackageStatus extends AsyncTask<Void,Void,Void> {

        protected Void doInBackground(Void... voids){

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl2, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("contacts");

                for(int i = 0; i<arr.length();i++){

                    String status = arr.getJSONObject(i).getString("status");

                    HashMap<String, String> qList = new HashMap<>();
                    // adding each child node to HashMap key => value
                    qList.put("status", status);

                    // adding contact to contact list
                    userCheck.add(qList);

                }

            }catch (JSONException e) {

                e.printStackTrace();

            }
            return null;
        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

        }
    }
}
