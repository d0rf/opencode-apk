package ai.opencode.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String DEFAULT_URL = "http://odroid.tail9b91d6.ts.net:3002";
    private static final String PREFS = "opencode";
    private static final String KEY_URL = "server_url";

    private WebView webView;
    private SharedPreferences prefs;
    private boolean dialogShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSupportZoom(false);
        s.setAllowFileAccess(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request.isForMainFrame()) {
                    view.loadData("<html><body style=\"background:#111;color:#ccc;font-family:sans-serif;"
                            + "display:flex;align-items:center;justify-content:center;height:100%\">"
                            + "<div style=\"text-align:center\">Serveur injoignable</div></body></html>",
                            "text/html", "utf-8");
                    showConnectionDialog(String.valueOf(error.getDescription()));
                }
            }
        });
        webView.loadUrl(getServerUrl());
    }

    private String getServerUrl() {
        return prefs.getString(KEY_URL, DEFAULT_URL);
    }

    private void showConnectionDialog(String errorDescription) {
        if (dialogShowing || isFinishing()) return;
        dialogShowing = true;
        new AlertDialog.Builder(this)
                .setTitle("Serveur injoignable")
                .setMessage(getServerUrl() + "\n\n" + errorDescription)
                .setCancelable(false)
                .setPositiveButton("Réessayer", (d, w) -> {
                    dialogShowing = false;
                    webView.loadUrl(getServerUrl());
                })
                .setNegativeButton("Changer l'adresse", (d, w) -> {
                    dialogShowing = false;
                    showUrlEditor();
                })
                .show();
    }

    private void showUrlEditor() {
        if (dialogShowing || isFinishing()) return;
        dialogShowing = true;
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(getServerUrl());
        input.setTextColor(Color.WHITE);
        new AlertDialog.Builder(this)
                .setTitle("Adresse du serveur")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    dialogShowing = false;
                    String url = input.getText().toString().trim();
                    if (!url.isEmpty()) {
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        prefs.edit().putString(KEY_URL, url).apply();
                    }
                    webView.loadUrl(getServerUrl());
                })
                .setNeutralButton("Défaut", (d, w) -> {
                    dialogShowing = false;
                    prefs.edit().remove(KEY_URL).apply();
                    webView.loadUrl(getServerUrl());
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
