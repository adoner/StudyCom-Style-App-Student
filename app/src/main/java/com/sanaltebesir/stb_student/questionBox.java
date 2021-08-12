package com.sanaltebesir.stb_student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpConnection;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class questionBox extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/questionBox.php";
    private JSONObject json;
    private ProgressDialog pDialog;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    public ImageView qimage;
    public ListView lv;
    public String userid;
    public TextView twNotes;
    public ImageView iwDurum;
    public RelativeLayout rlInf;
    ArrayList<HashMap<String, String>> questionList;
    JSONParser jsonParser = new JSONParser();
    private SessionHandler session;
    CustomQboxAdapter customQboxAdapter;
    ArrayList itemsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_box);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionHandler(getApplicationContext());
        mySwipeRefreshLayout = this.findViewById(R.id.swipeContainer);
        lv = findViewById(R.id.listview_qbox);
        userid = session.getUserDetails().userid;
        questionList = new ArrayList<>();
        new GetQuestionData().execute();

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(false);
                        Intent i = new Intent(questionBox.this, questionBox.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                }
        );

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(view.getContext(), viewSolvedQuestions.class);
                myIntent.putExtra("questionid", questionList.get(position).get("questionid"));
                startActivity(myIntent);

            }
        });

    }

     private class GetQuestionData extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                pDialog = new ProgressDialog(questionBox.this);
                pDialog.setMessage("Lütfen bekleyiniz...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

            }

            protected Void doInBackground(Void... voids) {

                try {

                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("userid", userid));
                    json = jsonParser.makeHttpRequest(phpUrl, "POST", params);

                } catch (Exception e) {

                    e.printStackTrace();

                }

                try {

                    JSONArray arr = json.getJSONArray("questiondata");

                    for (int i = 0; i < arr.length(); i++) {

                        String questionid = arr.getJSONObject(i).getString("questionid");
                        String questionName = arr.getJSONObject(i).getString("questionName");
                        String answeringDate = arr.getJSONObject(i).getString("answeringDate");
                        String askingDate = arr.getJSONObject(i).getString("askingDate");
                        String lecturename = arr.getJSONObject(i).getString("lecturename");
                        String notes = arr.getJSONObject(i).getString("notes");
                        String assigned = arr.getJSONObject(i).getString("assigned");
                        String accepted = arr.getJSONObject(i).getString("accepted");
                        String solved = arr.getJSONObject(i).getString("solved");
                        String refused = arr.getJSONObject(i).getString("refused");
                        String refusingnote = arr.getJSONObject(i).getString("refusingnote");

                        HashMap<String, String> qList = new HashMap<>();

                        // adding each child node to HashMap key => value
                        qList.put("questionid", questionid);
                        qList.put("questionName", questionName);
                        qList.put("answeringDate", answeringDate);
                        qList.put("askingDate", askingDate);
                        qList.put("lecturename", lecturename);
                        qList.put("notes", notes);
                        qList.put("assigned", assigned);
                        qList.put("accepted", accepted);
                        qList.put("solved", solved);
                        qList.put("refused", refused);
                        qList.put("refusingnote", refusingnote);

                        // adding contact to contact list
                        questionList.add(qList);

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
                return null;

            }

            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
                pDialog.dismiss();

                Map<String, Integer> imagemap = new HashMap<>();
                imagemap.put("hourglass", R.drawable.hourglass);
                imagemap.put("tick", R.drawable.tick);
                imagemap.put("logo", R.drawable.questions);
                imagemap.put("cross", R.drawable.cross);
                imagemap.put("writing", R.drawable.writing);

                try {

                    for (int i = 0; i < questionList.size(); i++) {


                        DataModel dataModel = new DataModel();
                        dataModel.setAskingDate(questionList.get(i).get("askingDate"));
                        dataModel.setTitle(questionList.get(i).get("lecturename"));
                        dataModel.setMessageDate(questionList.get(i).get("notes"));
                        dataModel.setImageid(imagemap.get("logo"));

                        if(Integer.parseInt(questionList.get(i).get("assigned"))==1){
                            dataModel.setViewed("Eğitmen Bekleniyor...");
                            dataModel.setTvsolved("");
                            dataModel.setImageid2(imagemap.get("hourglass"));
                        }
                        if(Integer.parseInt(questionList.get(i).get("assigned"))==1 && Integer.parseInt(questionList.get(i).get("accepted"))==1){
                            dataModel.setViewed("Soru Çözülüyor...");
                            dataModel.setTvsolved("");
                            dataModel.setImageid2(imagemap.get("writing"));
                        }
                        if(Integer.parseInt(questionList.get(i).get("solved"))==1){
                            dataModel.setViewed(questionList.get(i).get("answeringDate"));
                            dataModel.setTvsolved("tarihinde çözüldü");
                            dataModel.setImageid2(imagemap.get("tick"));
                        }
                        if(Integer.parseInt(questionList.get(i).get("assigned"))==1 && Integer.parseInt(questionList.get(i).get("accepted"))==0 && Integer.parseInt(questionList.get(i).get("refused"))==1){
                            dataModel.setViewed("Soru Reddedildi");
                            dataModel.setTvsolved("");
                            dataModel.setImageid2(imagemap.get("cross"));
                        }

                        itemsArray.add(dataModel);
                    }

                    customQboxAdapter = new CustomQboxAdapter(questionBox.this, itemsArray);
                    lv.setAdapter(customQboxAdapter);

                } catch (NullPointerException e) {

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