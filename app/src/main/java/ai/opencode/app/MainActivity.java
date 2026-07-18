package ai.opencode.app;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSupportZoom(false);
        s.setAllowFileAccess(false);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://odroid.tail9b91d6.ts.net:3002");
    }

    @Override
    public void onBackPressed() {
        WebView wv = (WebView) findViewById(android.R.id.content);
        if (wv != null && wv.canGoBack()) wv.goBack();
        else super.onBackPressed();
    }
}
