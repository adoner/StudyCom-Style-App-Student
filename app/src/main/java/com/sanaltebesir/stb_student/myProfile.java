package com.sanaltebesir.stb_student;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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

public class myProfile extends AppCompatActivity {

    private static String phpUrl = "http://www.sanaltebesir.com/android/student/profile.php";
    private static String phpUrl2 = "http://www.sanaltebesir.com/android/student/setSwitchStatus.php";
    private static String phpUrl3 = "http://www.sanaltebesir.com/android/student/editProfile.php";
    public String [] cities = {"Adana","Adıyaman","Afyonkarahisar","Ağrı","Amasya","Ankara","Antalya","Artvin","Aydın","Balıkesir","Bilecik","Bingöl","Bitlis","Bolu","Burdur","Bursa","Çanakkale",
                                "Çankırı","Çorum","Denizli","Diyarbakır","Edirne","Elazığ","Erzincan","Erzurum","Eskişehir","Gaziantep","Giresun","Gümüşhane","Hakkari","Hatay","Isparta","Mersin",
                                "İstanbul","İzmir","Kars","Kastamonu","Kayseri","Kırklareli","Kırşehir","Kocaeli","Konya","Kütahya","Malatya","Manisa","Kahramanmaraş","Mardin","Muğla","Muş","Nevşehir",
                                "Niğde","Ordu","Rize","Sakarya","Samsun","Siirt","Sinop","Sivas","Tekirdağ","Tokat","Trabzon","Tunceli","Şanlıurfa","Uşak","Van","Yozgat","Zonguldak","Aksaray","Bayburt",
                                "Karaman","Kırıkkale","Batman","Şırnak","Bartın","Ardahan","Iğdır","Yalova","Karabük","Kilis","Osmaniye","Düzce"
                                };
    public String [] sinif = {"5.Sınıf","6.Sınıf","7.Sınıf","8.Sınıf","9.Sınıf","10.Sınıf","11.Sınıf","12.Sınıf","Mezun"};
    private static final String KEY_EMPTY = "";
    private JSONObject json;
    private ProgressDialog pDialog;
    public EditText etName;
    public EditText etEmail;
    public TextView etPhone;
    public Button editProfile;
    public Switch swSoruBildirim;
    public Switch swKampanyaBildirim;
    public Switch swGenelBildirim;
    public String switch1Status;
    public String switch2Status;
    public String switch3Status;
    public Spinner spCity;
    public Spinner spSinif;
    public int selectionPosition;
    public String userid;
    ArrayList<HashMap<String, String>> contactList;
    JSONParser jsonParser = new JSONParser();
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.tel);
        editProfile = findViewById(R.id.submitButton);
        swSoruBildirim = findViewById(R.id.switch1);
        swKampanyaBildirim = findViewById(R.id.switch2);
        swGenelBildirim = findViewById(R.id.switch3);
        spCity = findViewById(R.id.spCity);
        spSinif = findViewById(R.id.spSinif);
        session = new SessionHandler(getApplicationContext());
        contactList = new ArrayList<>();
        userid = session.getUserDetails().userid;
        new GetContacts().execute();



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do it
                if(validateInputs()){
                    new EditProfile().execute();
                }
            }
        });

        swSoruBildirim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Do what you want
                    switch1Status = "1";
                }else{
                    switch1Status = "0";
                }

                new SetSwitchStatus().execute();
            }
        });

        swKampanyaBildirim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Do what you want
                    switch2Status = "1";
                }else{
                    switch2Status = "0";
                }

                new SetSwitchStatus().execute();
            }
        });

        swGenelBildirim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Do what you want
                    switch3Status = "1";
                }else{
                    switch3Status = "0";
                }

                new SetSwitchStatus().execute();
            }
        });

    }

    private void setSpinner () {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,cities);
        spCity.setAdapter(adapter);
        selectionPosition= adapter.getPosition(contactList.get(0).get("city"));
        spCity.setSelection(selectionPosition);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,sinif);
        spSinif.setAdapter(adapter2);
        selectionPosition= adapter2.getPosition(contactList.get(0).get("sinif"));
        spSinif.setSelection(selectionPosition);

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(myProfile.this);
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
               // Log.d("Create Response", json.toString());

            }catch(Exception e){

                e.printStackTrace();

            }

            try {

                JSONArray arr = json.getJSONArray("contacts");

                for(int i = 0; i<arr.length();i++){

                    String name = arr.getJSONObject(i).getString("name");
                    String email = arr.getJSONObject(i).getString("email");
                    String tel = arr.getJSONObject(i).getString("tel");
                    String sinif = arr.getJSONObject(i).getString("sinif");
                    String city = arr.getJSONObject(i).getString("city");
                    String notfSolution = arr.getJSONObject(i).getString("notfSolution");
                    String notfCampaign = arr.getJSONObject(i).getString("notfCampaign");
                    String notfGeneral = arr.getJSONObject(i).getString("notfGeneral");

                    HashMap<String, String> qList = new HashMap<>();

                    // adding each child node to HashMap key => value
                    qList.put("name", name);
                    qList.put("email", email);
                    qList.put("tel", tel);
                    qList.put("sinif", sinif);
                    qList.put("city", city);
                    qList.put("notfSolution", notfSolution);
                    qList.put("notfCampaign", notfCampaign);
                    qList.put("notfGeneral", notfGeneral);

                    // adding contact to contact list
                    contactList.add(qList);

                }

            }catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            try {

                setSpinner ();
                etName.setText(contactList.get(0).get("name"));
                etEmail.setText(contactList.get(0).get("email"));
                etPhone.setText(contactList.get(0).get("tel"));

                switch(contactList.get(0).get("notfSolution")){
                    case "1":
                        swSoruBildirim.setChecked(true);
                        break;
                    case "0":
                        swSoruBildirim.setChecked(false);
                        break;
                }

                switch(contactList.get(0).get("notfCampaign")){
                    case "1":
                        swKampanyaBildirim.setChecked(true);
                        break;
                    case "0":
                        swKampanyaBildirim.setChecked(false);
                        break;
                }

                switch(contactList.get(0).get("notfGeneral")){
                    case "1":
                        swGenelBildirim.setChecked(true);
                        break;
                    case "0":
                        swGenelBildirim.setChecked(false);
                        break;
                }

            }catch(NullPointerException e){

                e.printStackTrace();

            }
        }
    }

    private class EditProfile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(myProfile.this);
            pDialog.setMessage("Lütfen bekleyiniz...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }


        @Override
        protected String doInBackground(String... params) {

            try{

                // Building Parameters
                List<NameValuePair> param = new ArrayList<>();
                param.add(new BasicNameValuePair("userid", userid));
                param.add(new BasicNameValuePair("name", etName.getText().toString()));
                param.add(new BasicNameValuePair("email", etEmail.getText().toString()));
                param.add(new BasicNameValuePair("sinif", spSinif.getSelectedItem().toString()));
                param.add(new BasicNameValuePair("city", spCity.getSelectedItem().toString()));
                json = jsonParser.makeHttpRequest(phpUrl3,
                        "POST", param);
                // check log cat for response
                // Log.d("Create Response", json.toString());

            }catch(Exception e){

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }
    }

    class SetSwitchStatus extends AsyncTask<Void,Void,Void> {

        protected Void doInBackground(Void... voids){

            try{

                // Building Parameters
                List<NameValuePair> params3 = new ArrayList<>();
                params3.add(new BasicNameValuePair("userid", userid));
                params3.add(new BasicNameValuePair("switch1Status", switch1Status));
                params3.add(new BasicNameValuePair("switch2Status", switch2Status));
                params3.add(new BasicNameValuePair("switch3Status", switch3Status));
                json = jsonParser.makeHttpRequest(phpUrl2,
                        "POST", params3);

            }catch(Exception e){

                e.printStackTrace();

            }
            return null;
        }
    }

    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            // e-posta formatı kontrol
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    private boolean validateInputs() {

        if (KEY_EMPTY.equals(etName.getText().toString())) {
            etName.setError("Ad Soyad boş olamaz");
            etName.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(etEmail.getText().toString())) {
            etEmail.setError("Eposta boş olamaz");
            etEmail.requestFocus();
            return false;
        }
        if (isValidEmail(etEmail.getText().toString())!=true) {
            etEmail.setError("Geçerli bir eposta adresi giriniz");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
