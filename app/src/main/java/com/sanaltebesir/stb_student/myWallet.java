package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class myWallet extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/myWallet.php";
    private JSONObject json;
    private ProgressDialog pDialog;
    private SessionHandler session;
    public String userid;
    public TextView twPaketadi;
    public TextView twSoruhakki;
    public TextView twSorulansoru;
    public TextView twKalansoru;
    public TextView twUstpaket;
    public TextView twPaketgecmis;
    public TextView tvEarning;
    public TextView tvInvite;
    public TextView tvOrderhistory;
    public ListView cardListview;
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> packageData;
    ArrayList<HashMap<String, String>> earningData;
    ArrayList<HashMap<String, String>> cardsData;
    public List<String> cardList;
    public ArrayAdapter<String> cardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionHandler(getApplicationContext());
        twUstpaket = findViewById(R.id.tw_ustpaket);
        twPaketgecmis = findViewById(R.id.twPaketgecmis);
        twKalansoru = findViewById(R.id.tw_kalansoru);
        twSorulansoru = findViewById(R.id.tw_sorulansoru);
        twSoruhakki = findViewById(R.id.tw_soruhakki);
        twPaketadi = findViewById(R.id.tw_paketadi);
        tvEarning = findViewById(R.id.tvEarning);
        tvInvite = findViewById(R.id.tvInvite);
        tvOrderhistory = findViewById(R.id.tvOrderHistory);
        cardListview = findViewById(R.id.cardListview);
        userid = session.getUserDetails().userid;
        packageData = new ArrayList<>();
        earningData = new ArrayList<>();
        cardsData = new ArrayList<>();
        new GetPackageData().execute();
        new GetUserEarning().execute();
        new GetCards().execute();

        twUstpaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), packageList.class);
                intent.putExtra("packagename", packageData.get(0).get("packagename"));
                startActivity(intent);
            }
        });

        twPaketgecmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), packageHistory.class);
                startActivity(intent);
            }
        });

        tvInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), referance.class);
                startActivity(intent);
            }
        });

        tvOrderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
    }

    private class GetPackageData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(myWallet.this);
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
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("package");

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
                    packageData.add(qList);

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

                twPaketadi.setText(packageData.get(0).get("packagename"));
                twSoruhakki.setText(packageData.get(0).get("questionlimit"));
                twKalansoru.setText(Integer.toString(Integer.parseInt(packageData.get(0).get("questionlimit"))
                        -Integer.parseInt(packageData.get(0).get("askedquestion"))));
                twSorulansoru.setText(packageData.get(0).get("askedquestion"));

            }catch(NullPointerException e){

                e.printStackTrace();

            }
        }
    }

    private class GetUserEarning extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("userearning");

                for(int i = 0; i<arr.length();i++){

                    String earnings = arr.getJSONObject(i).getString("earnings");
                    HashMap<String, String> qList = new HashMap<>();
                    // adding each child node to HashMap key => value
                    qList.put("earnings", earnings);
                    // adding contact to contact list
                    earningData.add(qList);

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

                if(Integer.parseInt(earningData.get(0).get("earnings"))>0){

                    tvEarning.setText(earningData.get(0).get("earnings")+" TL Hediye Para Mevcut");

                }else{

                    tvEarning.setText("Hiç hediye paranız yok.");
                }

            }catch(NullPointerException e){

                e.printStackTrace();

            }
        }
    }

    private class GetCards extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("cards");

                cardList = new ArrayList<>();

                for(int i = 0; i<arr.length();i++){

                    //String fullname = arr.getJSONObject(i).getString("fullname");
                    String cardno = arr.getJSONObject(i).getString("cardno");
                    //String validity = arr.getJSONObject(i).getString("validity");
                    //String cvv = arr.getJSONObject(i).getString("cvv");

                    // adding contact to contact list
                    //cardList.add(i, fullname);
                    cardList.add(i, cardno);
                    //cardList.add(i, validity);
                    //cardList.add(i, cvv);

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

                cardListAdapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, cardList);
                cardListview.setAdapter(cardListAdapter);

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
