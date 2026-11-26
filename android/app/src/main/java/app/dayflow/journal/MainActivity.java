package app.dayflow.journal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/** Dayflow full screen in a WebView, plus a bridge so the page can refresh the home-screen widgets. */
public class MainActivity extends Activity {
    static final String BASE = "https://lamy18.github.io/Cool-app/journal/";
    private static final int REQ_FILE = 2;
    private static final int REQ_SAVE = 3;

    private WebView web;
    private ValueCallback<Uri[]> fileCallback;
    private String pendingBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setStatusBarColor(Color.parseColor("#050505"));
        w.setNavigationBarColor(Color.parseColor("#050505"));

        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#050505"));
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(true);
        s.setAllowFileAccess(false);

        web.addJavascriptInterface(new Bridge(), "DayflowAndroid");
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri u = request.getUrl();
                boolean ours = "lamy18.github.io".equals(u.getHost()) && u.getPath() != null && u.getPath().startsWith("/Cool-app/journal");
                if (ours) return false;
                // Other links (e.g. the other games) open in the normal browser
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception ignored) { }
                return true;
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                try {
                    startActivityForResult(params.createIntent(), REQ_FILE);
                } catch (Exception e) {
                    fileCallback = null;
                    return false;
                }
                return true;
            }
        });

        if (savedInstanceState != null) web.restoreState(savedInstanceState);
        else web.loadUrl(urlFor(getIntent()));
    }

    private static String urlFor(Intent intent) {
        String tab = intent == null ? null : intent.getStringExtra("tab");
        return BASE + (tab != null ? "#" + tab : "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String tab = intent.getStringExtra("tab");
        if (tab != null && tab.matches("[a-z]+")) web.evaluateJavascript("location.hash='#" + tab + "'", null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        web.saveState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WidgetUtil.updateAll(this);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_FILE && fileCallback != null) {
            fileCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            fileCallback = null;
        } else if (requestCode == REQ_SAVE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null && pendingBackup != null) {
                try (OutputStream out = getContentResolver().openOutputStream(data.getData())) {
                    out.write(pendingBackup.getBytes(StandardCharsets.UTF_8));
                    Toast.makeText(this, "Backup saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Couldn't save the backup", Toast.LENGTH_LONG).show();
                }
            }
            pendingBackup = null;
        }
    }

    /** Methods the Dayflow page can call as window.DayflowAndroid.* */
    private class Bridge {
        @JavascriptInterface
        public void updateWidget(String json) {
            getSharedPreferences(WidgetUtil.PREFS, MODE_PRIVATE).edit().putString(WidgetUtil.KEY, json).apply();
            WidgetUtil.updateAll(MainActivity.this);
        }

        @JavascriptInterface
        public void saveBackup(final String json, final String fileName) {
            runOnUiThread(() -> {
                pendingBackup = json;
                Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("application/json");
                i.putExtra(Intent.EXTRA_TITLE, fileName);
                try {
                    startActivityForResult(i, REQ_SAVE);
                } catch (Exception e) {
                    pendingBackup = null;
                    Toast.makeText(MainActivity.this, "No file app found to save the backup", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public boolean isApp() {
            return true;
        }
    }
}
