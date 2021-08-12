package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class packageHistory extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/packageHistory.php";
    private JSONObject json;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> packageHistoryList;
    JSONParser jsonParser = new JSONParser();
    private SessionHandler session;
    public String userid;
    public ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_history);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionHandler(getApplicationContext());
        userid = session.getUserDetails().userid;
        packageHistoryList = new ArrayList<>();
        lv = findViewById(R.id.lvPackageHistory);
        new GetPackageHistory().execute();

    }

    private class GetPackageHistory extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(packageHistory.this);
            pDialog.setMessage("Lütfen bekleyiniz...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected Void doInBackground(Void... voids){

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("userid", userid));
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("packagehistory");

                for(int i = 0; i<arr.length();i++){

                    String packageid = arr.getJSONObject(i).getString("packageid");
                    String packagename = arr.getJSONObject(i).getString("packagename");
                    String questionlimit = arr.getJSONObject(i).getString("questionlimit");
                    String askedquestion = arr.getJSONObject(i).getString("askedquestion");
                    String kalansoru = arr.getJSONObject(i).getString("kalansoru");
                    String startdate = arr.getJSONObject(i).getString("startdate");
                    String finishdate = arr.getJSONObject(i).getString("finishdate");

                    HashMap<String, String> qList = new HashMap<>();

                    // adding each child node to HashMap key => value
                    qList.put("packageid", packageid);
                    qList.put("packagename", packagename);
                    qList.put("questionlimit", questionlimit);
                    qList.put("askedquestion", askedquestion);
                    qList.put("kalansoru", kalansoru);
                    qList.put("startdate", startdate);
                    qList.put("finishdate", finishdate);

                    // adding contact to contact list
                    packageHistoryList.add(qList);

                }

            }catch (JSONException e) {

                e.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(Void aVoid){

            super.onPostExecute(aVoid);
            pDialog.dismiss();

            try {

                ListAdapter adapter = new SimpleAdapter(
                        packageHistory.this, packageHistoryList,
                        R.layout.packagehistory_listitem, new String[]{"packagename","questionlimit","askedquestion","kalansoru","startdate","finishdate"},
                        new int[]{R.id.packagename, R.id.tvsoruhakki, R.id.tvsorulansoru,R.id.tvkalansoru, R.id.tvbaslangic, R.id.tvbitis});

                lv.setAdapter(adapter);

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
