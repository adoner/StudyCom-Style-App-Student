package com.sanaltebesir.stb_student;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class usageStatistics extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/statistics.php";
    private JSONObject json;
    private ProgressDialog pDialog;
    private SessionHandler session;
    public String userid;
    public TextView packagename;
    public TextView questionlimit;
    public TextView startdate;
    public TextView finishdate;
    public TextView total;
    public TextView matematik;
    public TextView fizik;
    public TextView geometri;
    public TextView kimya;
    public TextView biyoloji;
    public TextView fenbilgisi;
    public TextView turkce;
    public TextView tarih;
    public TextView cografya;
    public TextView sosyal;
    public TextView felsefe;
    public TextView ingilizce;
    public TextView almanca;
    public TextView fransizca;
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> customerList;
    ArrayList<HashMap<String, String>> lectureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_statistics);
        session = new SessionHandler(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        packagename = findViewById(R.id.packagename);
        questionlimit = findViewById(R.id.questionlimit);
        startdate = findViewById(R.id.startdate);
        finishdate = findViewById(R.id.finishdate);
        total = findViewById(R.id.total);
        matematik = findViewById(R.id.matematik);
        geometri = findViewById(R.id.geometri);
        fizik = findViewById(R.id.fizik);
        kimya = findViewById(R.id.kimya);
        biyoloji = findViewById(R.id.biyoloji);
        fenbilgisi = findViewById(R.id.fenbilgisi);
        turkce = findViewById(R.id.turkce);
        tarih = findViewById(R.id.tarih);
        cografya = findViewById(R.id.cografya);
        sosyal = findViewById(R.id.sosyal);
        felsefe = findViewById(R.id.felsefe);
        ingilizce = findViewById(R.id.ingilizce);
        almanca = findViewById(R.id.almanca);
        fransizca = findViewById(R.id.fransizca);
        userid = session.getUserDetails().userid;
        customerList = new ArrayList<>();
        lectureList = new ArrayList<>();
        new usageStatistics.GetContacts().execute();
        new usageStatistics.GetUsage().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(usageStatistics.this);
            pDialog.setMessage("Lütfen bekleyiniz...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl,
                        "POST", params);
                // check log cat for response
                Log.d("Create Response", json.toString());

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("students");

                for(int i = 0; i<arr.length();i++){

                    String packagename = arr.getJSONObject(i).getString("packagename");
                    String questionlimit = arr.getJSONObject(i).getString("questionlimit");
                    String askedquestion = arr.getJSONObject(i).getString("askedquestion");
                    String startdate = arr.getJSONObject(i).getString("startdate");
                    String finishdate = arr.getJSONObject(i).getString("finishdate");

                    HashMap<String, String> qList = new HashMap<>();

                    // adding each child node to HashMap key => value
                    qList.put("packagename", packagename);
                    qList.put("questionlimit", questionlimit);
                    qList.put("askedquestion", askedquestion);
                    qList.put("startdate", startdate);
                    qList.put("finishdate", finishdate);

                    // adding contact to contact list
                    customerList.add(qList);

                }

            }catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (pDialog.isShowing())
                pDialog.dismiss();

            try{

                packagename.setText(customerList.get(0).get("packagename"));
                questionlimit.setText(customerList.get(0).get("questionlimit"));
                startdate.setText(customerList.get(0).get("startdate"));
                finishdate.setText(customerList.get(0).get("finishdate"));

            }catch(NullPointerException e){

                e.printStackTrace();

            }
        }
    }

    private class GetUsage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl,
                        "POST", params);
                // check log cat for response
                Log.d("Create Response", json.toString());

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("lectures");

                for(int i = 0; i<arr.length();i++){

                    String total = arr.getJSONObject(i).getString("total");
                    String matematik = arr.getJSONObject(i).getString("matematik");
                    String geometri = arr.getJSONObject(i).getString("geometri");
                    String fizik = arr.getJSONObject(i).getString("fizik");
                    String kimya = arr.getJSONObject(i).getString("kimya");
                    String biyoloji = arr.getJSONObject(i).getString("biyoloji");
                    String fenbilgisi = arr.getJSONObject(i).getString("fenbilgisi");
                    String turkce = arr.getJSONObject(i).getString("turkce");
                    String tarih = arr.getJSONObject(i).getString("tarih");
                    String cografya = arr.getJSONObject(i).getString("cografya");
                    String felsefe = arr.getJSONObject(i).getString("felsefe");
                    String sosyal = arr.getJSONObject(i).getString("sosyal");
                    String ingilizce = arr.getJSONObject(i).getString("ingilizce");
                    String almanca = arr.getJSONObject(i).getString("almanca");
                    String fransizca = arr.getJSONObject(i).getString("fransizca");

                    HashMap<String, String> qList = new HashMap<>();

                    // adding each child node to HashMap key => value
                    qList.put("total", total);
                    qList.put("matematik", matematik);
                    qList.put("geometri", geometri);
                    qList.put("fizik", fizik);
                    qList.put("kimya", kimya);
                    qList.put("biyoloji", biyoloji);
                    qList.put("fenbilgisi", fenbilgisi);
                    qList.put("turkce", turkce);
                    qList.put("tarih", tarih);
                    qList.put("cografya", cografya);
                    qList.put("felsefe", felsefe);
                    qList.put("sosyal", sosyal);
                    qList.put("ingilizce", ingilizce);
                    qList.put("almanca", almanca);
                    qList.put("fransizca", fransizca);

                    // adding contact to contact list
                    lectureList.add(qList);

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

                total.setText(lectureList.get(0).get("total"));
                matematik.setText(lectureList.get(0).get("matematik"));
                geometri.setText(lectureList.get(0).get("geometri"));
                fizik.setText(lectureList.get(0).get("fizik"));
                kimya.setText(lectureList.get(0).get("kimya"));
                biyoloji.setText(lectureList.get(0).get("biyoloji"));
                fenbilgisi.setText(lectureList.get(0).get("fenbilgisi"));
                turkce.setText(lectureList.get(0).get("turkce"));
                tarih.setText(lectureList.get(0).get("tarih"));
                cografya.setText(lectureList.get(0).get("cografya"));
                felsefe.setText(lectureList.get(0).get("felsefe"));
                sosyal.setText(lectureList.get(0).get("sosyal"));
                ingilizce.setText(lectureList.get(0).get("ingilizce"));
                almanca.setText(lectureList.get(0).get("almanca"));
                fransizca.setText(lectureList.get(0).get("fransizca"));

            }catch(NullPointerException e){

                e.printStackTrace();

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
