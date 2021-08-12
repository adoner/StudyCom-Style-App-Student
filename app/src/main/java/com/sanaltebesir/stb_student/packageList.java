package com.sanaltebesir.stb_student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class packageList extends AppCompatActivity {

    private String TAG = packageList.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    // URL to get contacts JSON
    private static String url = "http://www.sanaltebesir.com/android/student/pricing.php";
    ArrayList<HashMap<String, String>> priceList;
    public String packagename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        priceList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listview_pricing);
        Intent myIntent = getIntent();
        packagename = myIntent.getStringExtra("packagename");
        new packageList.GetPriceList().execute();

        // Set an item click listener for ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView temp = (TextView) view.findViewById(R.id.pricing_textview2);
                TextView temp2 = (TextView) view.findViewById(R.id.pricing_textview3);
                TextView temp3 = (TextView) view.findViewById(R.id.pricing_textview1);

                // Get the selected item text from ListView
                String selectedItem = temp.getText().toString();
                String selectedItem2 = temp2.getText().toString();
                String selectedItem3 = temp3.getText().toString();

                /*Intent intent = new Intent(packageList.this, paymentActivity.class);
                intent.putExtra("SELECTED_ITEM_NAME", selectedItem);
                intent.putExtra("SELECTED_ITEM_PRICE", selectedItem2);
                intent.putExtra("SELECTED_ITEM_LIMIT", selectedItem3);
                startActivity(intent);*/
            }
        });
    }

    private class GetPriceList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(packageList.this);
            pDialog.setMessage("Lütfen bekleyiniz...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {

                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray priceLists = jsonObj.getJSONArray("prices");

                    // looping through All Contacts
                    for (int i = 0; i < priceLists.length(); i++) {

                        JSONObject pl = priceLists.getJSONObject(i);

                        String packagename = pl.getString("packagename");
                        String questionlimit = pl.getString("questionlimit");
                        String price = pl.getString("price");
                        String about = pl.getString("about");

                        // tmp hash map for single contact
                        HashMap<String, String> pricelist = new HashMap<>();

                        // adding each child node to HashMap key => value
                        pricelist.put("packagename", packagename);
                        pricelist.put("questionlimit", questionlimit);
                        pricelist.put("price", price);
                        pricelist.put("about", about);
                        // adding contact to contact list
                        priceList.add(pricelist);

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
           try{

               ListAdapter adapter = new SimpleAdapter(
                   packageList.this, priceList,
                   R.layout.listitem_pricing, new String[]{"packagename","questionlimit", "price", "about"},
                   new int[]{R.id.pricing_textview2, R.id.pricing_textview1, R.id.pricing_textview3, R.id.pricing_textview4});
               lv.setAdapter(adapter);

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
