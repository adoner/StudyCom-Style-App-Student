package com.sanaltebesir.stb_student;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/statistics.php";
    private static String phpUrl2 = "http://www.sanaltebesir.com/android/student/packageStatistics.php";
    private static String phpUrl3 = "http://www.sanaltebesir.com/android/student/checkPackageStatus.php";
    private static final String KEY_STATUS = "status";
    private static final String KEY_USERID = "userid";
    private static final String KEY_MESSAGE = "message";
    private static final String TAG = "MyNotificationToken";
    private JSONObject json;
    JSONParser jsonParser = new JSONParser();
    private TextView toplamsoru;
    private TextView kalansoru;
    private TextView sorulansoru;
    private int bakiye;
    public String userid;
    public Button camButton;
    public Button aboutUs;
    public Button messageBtn;
    public String currentDate;
    public TextView twMessage;
    ArrayList<HashMap<String, String>> unviewedList;
    ArrayList<HashMap<String, String>> packageStat;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionHandler(getApplicationContext());
        getSupportActionBar().hide();
        Button sorubank = findViewById(R.id.srbnk_btn);
        Button c = findViewById(R.id.srkts_btn);
        Button d = findViewById(R.id.hsbm_btn);
        Button e = findViewById(R.id.ozelders_btn);
        camButton = findViewById(R.id.cam_btn);
        aboutUs = findViewById(R.id.about_btn);
        messageBtn = findViewById(R.id.message_btn);
        twMessage = findViewById(R.id.twMessage);
        Button button_profil = findViewById(R.id.button_profil);
        Button button_market = findViewById(R.id.button_market);
        Button button_referance = findViewById(R.id.button_referance);
        CardView crdw = findViewById(R.id.crdvw1);
        ImageView logoview = findViewById(R.id.logoImgview);
        toplamsoru = findViewById(R.id.toplamsoru);
        sorulansoru = findViewById(R.id.sorulansoru);
        kalansoru = findViewById(R.id.kalansoru);
        userid = session.getUserDetails().userid;
        unviewedList = new ArrayList<>();
        packageStat = new ArrayList<>();
        currentDate = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
        //session.logoutUser();
        //Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_LONG).show();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;

                }
                // Get new Instance ID token
                String token = task.getResult().getToken();
                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
            }
        });

        new GetPackStat().execute();
        new GetMessage().execute();

        logoview.bringToFront();
        camButton.bringToFront();
        twMessage.setTranslationZ(10);

        sorubank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openSoruBank();
            }
        });

        d.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                openHsbm();
            }
        });

        c.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openQuestionBox();
            }
        });

        e.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openOzelDers();
            }
        });

        camButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPackageCam();
            }
        });

        button_profil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openProfil();
            }
        });

        button_market.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openMarket();
            }
        });

        button_referance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openReferance();
            }
        });

        aboutUs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openAbout();
            }
        });

        messageBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openMessage();
            }
        });
    }

    private class GetPackStat extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl2, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr3 = json.getJSONArray("packagestat");

                for(int i = 0; i<arr3.length();i++){

                    String packageid = arr3.getJSONObject(i).getString("packageid");
                    String questionlimit = arr3.getJSONObject(i).getString("questionlimit");
                    String askedquestion = arr3.getJSONObject(i).getString("askedquestion");
                    String soruhakki = arr3.getJSONObject(i).getString("soruhakki");

                    HashMap<String, String> qList3 = new HashMap<>();
                    // adding each child node to HashMap key => value
                    qList3.put("packageid", packageid);
                    qList3.put("questionlimit", questionlimit);
                    qList3.put("askedquestion", askedquestion);
                    qList3.put("soruhakki", soruhakki);

                    // adding contact to contact list
                    packageStat.add(qList3);

                }

            }catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            toplamsoru.setText(packageStat.get(0).get("soruhakki"));
            sorulansoru.setText(packageStat.get(0).get("askedquestion"));
            kalansoru.setText(Integer.toString(Integer.parseInt(packageStat.get(0).get("soruhakki"))-Integer.parseInt(packageStat.get(0).get("askedquestion"))));

        }
    }

    private void checkPackageFileUpload() {

        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERID, userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, phpUrl3, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getInt(KEY_STATUS) == 0) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Sanal Tebeşir");
                                builder.setMessage("Soru hakkınız kalmamıştır veya paket süreniz dolmuştur lütfen paketlerimizden satın alınız..!");
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

                                bakiye = Integer.parseInt(packageStat.get(0).get("soruhakki"))-Integer.parseInt(packageStat.get(0).get("askedquestion"));

                                if(bakiye > 0) {

                                    Intent intent = new Intent(getApplicationContext(), qFileUpload.class);
                                    startActivity(intent);

                                }

                                if(bakiye == 0) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Sanal Tebeşir");
                                    builder.setMessage("Soru hakkınız kalmamıştır veya paket süreniz dolmuştur lütfen paketlerimizden satın alınız..!");
                                    builder.setNegativeButton("Daha Sonra", null);
                                    builder.setPositiveButton("Paketleri İncele", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Intent intent = new Intent(getApplicationContext(), packageList.class);
                                            startActivity(intent);

                                        }
                                    });
                                    builder.show();

                                }

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

    private void checkPackageCam() {

        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERID, userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, phpUrl3, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getInt(KEY_STATUS) == 0) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Sanal Tebeşir");
                                builder.setMessage("Soru hakkınız kalmamıştır veya paket süreniz dolmuştur lütfen paketlerimizden satın alınız..!");
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

                                bakiye = Integer.parseInt(packageStat.get(0).get("soruhakki"))-Integer.parseInt(packageStat.get(0).get("askedquestion"));

                                if(bakiye > 0) {

                                    Intent intent = new Intent(getApplicationContext(), questionPhotoUpload.class);
                                    startActivity(intent);

                                }

                                if(bakiye == 0) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Sanal Tebeşir");
                                    builder.setMessage("Soru hakkınız kalmamıştır veya paket süreniz dolmuştur lütfen paketlerimizden satın alınız..!");
                                    builder.setNegativeButton("Daha Sonra", null);
                                    builder.setPositiveButton("Paketleri İncele", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Intent intent = new Intent(getApplicationContext(), packageList.class);
                                            startActivity(intent);

                                        }
                                    });
                                    builder.show();

                                }

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

    private class GetMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);
                // check log cat for response
                //Log.d("Create Response", json.toString());

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr4 = json.getJSONArray("messages");

                for(int i = 0; i<arr4.length();i++){

                    String unviewed = arr4.getJSONObject(i).getString("unviewed");

                    HashMap<String, String> qList4 = new HashMap<>();
                    // adding each child node to HashMap key => value
                    qList4.put("unviewed", unviewed);

                    // adding contact to contact list
                    unviewedList.add(qList4);

                }

            }catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try{
                //Message
                twMessage.setText(unviewedList.get(0).get("unviewed"));

            }catch(NullPointerException e){

                e.printStackTrace();

            }
        }
    }

    public void openHsbm(){

        Intent intent = new Intent(this, myAccount.class);
        startActivity(intent);
    }

    public void openQuestionBox(){

        Intent intent = new Intent(this, questionBox.class);
        startActivity(intent);
    }

    public void openOzelDers(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Sanal Tebeşir");
        builder.setMessage("Özel Ders özelliği çok yakın zamanda hizmetinizde olacak!.");
        builder.setNegativeButton("Tamam", null);
        /*builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });*/
        builder.show();

    }

    public void openProfil(){

        Intent intent = new Intent(this, myProfile.class);
        startActivity(intent);

    }

    public void openSoruBank(){

        Intent intent = new Intent(this, soruBank.class);
        startActivity(intent);

    }

    public void openAbout(){

        Intent intent = new Intent(this, about.class);
        startActivity(intent);

    }
    public void openMarket(){

        Intent intent = new Intent(this, packageList.class);
        startActivity(intent);

    }

    public void openMessage(){

        Intent intent = new Intent(this, myMessage.class);
        startActivity(intent);

    }
    public void openReferance(){

        Intent intent = new Intent(this, referance.class);
        startActivity(intent);

    }
}
