package com.sanaltebesir.stb_student;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


public class paymentActivity extends AppCompatActivity {

    private String TAG = packageList.class.getSimpleName();
    private ProgressDialog pDialog;
    private int userid = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activity);
        WebView myWebview = (WebView) findViewById(R.id.payment_webview);
        String packagename = getIntent().getStringExtra("SELECTED_ITEM_NAME");
        String price = getIntent().getStringExtra("SELECTED_ITEM_PRICE");
        String limit = getIntent().getStringExtra("SELECTED_ITEM_LIMIT");
        TextView tw1 = (TextView)findViewById(R.id.paketadi);
        TextView tw2 = (TextView)findViewById(R.id.fiyat);
        TextView tw3 = (TextView)findViewById(R.id.soruhakki);
        tw1.setText(packagename);
        tw2.setText(price);
        tw3.setText(limit);
        WebSettings webSettings = myWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebview.setWebChromeClient(new WebChromeClient());
        myWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebview.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        myWebview.loadUrl("http://www.sanaltebesir.com/android/payment_form.php?userid="+userid+"");

    }


}
