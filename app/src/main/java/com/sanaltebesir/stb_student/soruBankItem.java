package com.sanaltebesir.stb_student;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class soruBankItem extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/soruBank.php";
    private JSONObject json;
    private ProgressDialog pDialog;
    public String lecture;
    public ListView lv;
    public List<String> subjectList;
    public ArrayAdapter<String> listAdapter;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soru_bank_item);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv = findViewById(R.id.lw_soruBankItem);
        Intent myIntent = getIntent();
        lecture = myIntent.getStringExtra("lecture");
        getSupportActionBar().setTitle(lecture);
        new GetSubjectList().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), soruBankView.class);
                myIntent.putExtra("subject", listAdapter.getItem(position));
                startActivity(myIntent);
            }
        });

    }

    class GetSubjectList extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(soruBankItem.this);
            pDialog.setMessage("Lütfen bekleyiniz...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected Void doInBackground(Void... voids){

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("lecture", lecture));
                json = jsonParser.makeHttpRequest(phpUrl, "POST", params);
                // check log cat for response
                //Log.d("Create Response", json.toString());

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("questionbankdata");
                subjectList = new ArrayList<>();

                for(int i = 0; i<arr.length();i++){

                    String subject = arr.getJSONObject(i).getString("subject");
                    subjectList.add(i, subject);

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

                listAdapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, subjectList);
                lv.setAdapter(listAdapter);

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
