package com.sanaltebesir.stb_student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class viewSolvedQuestions extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static String phpUrl = "http://www.sanaltebesir.com/android/student/getImageLinks.php";
    private JSONObject json;
    ArrayList<HashMap<String, String>> imageLink;
    JSONParser jsonParser = new JSONParser();
    public String questionid;
    public Button reviewBtn;
    public TextView tvStatus;
    public ImageView ivStatus;
    public String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_solved_questions);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reviewBtn = findViewById(R.id.review);
        tvStatus = findViewById(R.id.tvStatus);
        ivStatus = findViewById(R.id.ivStatus);
        imageLink = new ArrayList<>();
        Intent myIntent = getIntent();
        questionid = myIntent.getStringExtra("questionid");
        reviewBtn.setVisibility(View.GONE);
        tvStatus.bringToFront();
        ivStatus.bringToFront();
        //Toast.makeText(getApplicationContext(), questionid, Toast.LENGTH_LONG).show();
        new GetImageLink().execute();

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do it

                Intent myIntent = new Intent(getApplicationContext(), reviewQuestion.class);
                myIntent.putExtra("questionid", questionid);
                startActivity(myIntent);
                finish();
            }
        });
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(viewSolvedQuestions.this,urls));

        /*CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);*/

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        //indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        /*swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*/

        // Pager listener over indicator
       /* indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });*/

    }

    class GetImageLink extends AsyncTask<Void,Void,Void> {

        protected Void doInBackground(Void... voids){

            try{

                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("questionid", questionid));
                json = jsonParser.makeHttpRequest(phpUrl,
                        "POST", params);

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("imagelinks");

                for(int i = 0; i<arr.length();i++){

                    String questionlink = arr.getJSONObject(i).getString("questionlink");
                    String answerlink = arr.getJSONObject(i).getString("answerlink");
                    String review = arr.getJSONObject(i).getString("review");
                    String solved = arr.getJSONObject(i).getString("solved");
                    String refused = arr.getJSONObject(i).getString("refused");
                    String refusingnote = arr.getJSONObject(i).getString("refusingnote");
                    String assigned = arr.getJSONObject(i).getString("assigned");
                    String accepted = arr.getJSONObject(i).getString("accepted");

                    HashMap<String, String> qList = new HashMap<>();

                    // adding each child node to HashMap key => value
                    qList.put("questionlink", questionlink);
                    qList.put("answerlink", answerlink);
                    qList.put("review", review);
                    qList.put("solved", solved);
                    qList.put("refused", refused);
                    qList.put("refusingnote", refusingnote);
                    qList.put("assigned", assigned);
                    qList.put("accepted", accepted);

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

            Map<String, Integer> imagemap = new HashMap<>();
            imagemap.put("hourglass", R.drawable.hourglass);
            imagemap.put("tick", R.drawable.tick);
            imagemap.put("logo", R.drawable.questions);
            imagemap.put("cross", R.drawable.cross);
            imagemap.put("writing", R.drawable.writing);

            urls = new String[] {
                    "http://www.sanaltebesir.com/uploads/questions/"+imageLink.get(0).get("questionlink"),
                    "http://www.sanaltebesir.com/uploads/answers/"+imageLink.get(0).get("answerlink")
            };

            init();
            if(Integer.parseInt(imageLink.get(0).get("assigned"))==0) {
                ivStatus.setImageResource(imagemap.get("hourglass"));
                tvStatus.setText("Eğitmen Bekleniyor...");
            }
            else if(Integer.parseInt(imageLink.get(0).get("assigned"))==1 && Integer.parseInt(imageLink.get(0).get("accepted"))==1 && Integer.parseInt(imageLink.get(0).get("solved"))==0) {
                ivStatus.setImageResource(imagemap.get("writing"));
                tvStatus.setText("Soru Çözülüyor...");
            }
            else if(Integer.parseInt(imageLink.get(0).get("assigned"))==1 &&Integer.parseInt(imageLink.get(0).get("accepted"))==0 && Integer.parseInt(imageLink.get(0).get("refused"))==1) {
                ivStatus.setImageResource(imagemap.get("cross"));
                tvStatus.setText("Soru \""+imageLink.get(0).get("refusingnote")+"\" nedeniyle reddedildi.\n Hesabına 1 soru kredisi yüklendi.");
            }else{

                ivStatus.setVisibility(View.INVISIBLE);
                tvStatus.setVisibility(View.INVISIBLE);

            }
            if(Integer.parseInt(imageLink.get(0).get("review"))==0 && Integer.parseInt(imageLink.get(0).get("solved"))==1){

                reviewBtn.setVisibility(View.VISIBLE);
                reviewBtn.bringToFront();

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
