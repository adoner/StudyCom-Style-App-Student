package com.sanaltebesir.stb_student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class referance extends AppCompatActivity {

    private String referencekey;
    private String userid;
    private  TextView davetKey;
    //php connections
    JSONParser jsonParser = new JSONParser();
    private static String phpUrl = "http://www.sanaltebesir.com/android/student/referencekey.php";
    private JSONObject json;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referance);
        session = new SessionHandler(getApplicationContext());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userid = session.getUserDetails().userid;
        TextView tw21 = (TextView)findViewById(R.id.textView21);
        davetKey =(TextView) findViewById(R.id.davetkey);
        Button davetbtn =(Button)findViewById(R.id.btn_davet);
        new AsyncTaskRef().execute();

        tw21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nasıl Çalışır
            }
        });

        davetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Davet butonu
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Sanal Tebeşir'le sınava hazırlan, çözemediğin sorulara anında cevap al! Bana özel "+referencekey+" koduyla Sanal Tebeşir'i kullanmaya başlayabilirsin.";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanal Tebeşir");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Paylaş"));
            }
        });
    }

    class AsyncTaskRef extends AsyncTask<String,String,String> {

        protected String doInBackground(String... args){

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userid", userid));

            json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

            return null;
        }

        protected void onPostExecute(String file_url){

            try {

                referencekey = json.getString("referencekey");
                davetKey.setText(referencekey);
                //Toast.makeText(getApplicationContext(), referencekey, Toast.LENGTH_LONG).show();

            }
            catch (JSONException e) {

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
