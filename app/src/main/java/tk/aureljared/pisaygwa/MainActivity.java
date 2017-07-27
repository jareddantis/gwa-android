package tk.aureljared.pisaygwa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    private String gwaUrl = "file:///android_asset/www/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // WebView settings
        webView = (WebView) findViewById(R.id.activity_main_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebContentsDebuggingEnabled(false);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        if (savedInstanceState == null)
            webView.loadUrl(gwaUrl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }
}
