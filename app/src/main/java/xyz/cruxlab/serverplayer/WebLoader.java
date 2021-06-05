package xyz.cruxlab.serverplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import xyz.cruxlab.serverplayer.databinding.ActivityWebLoaderBinding;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WebLoader extends AppCompatActivity {
    ActivityWebLoaderBinding binding;
    TinyDB tinyDB;
    boolean javascriptSupport = false;

    public static void start(Context context, String videoUrl) {
        context.startActivity(new Intent(context, WebLoader.class).putExtra("weburl", videoUrl).setFlags(FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebLoaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tinyDB = new TinyDB(getApplicationContext());
//        binding.content.webView.getSettings().setLoadWithOverviewMode(true);
        javascriptSupport = tinyDB.getBoolean("javascript");
        //binding.webView.getSettings().setJavaScriptEnabled(true);
//        binding.content.webView.setVerticalScrollBarEnabled(false);
        // binding.webView.setHorizontalScrollBarEnabled(false);

        binding.webView.loadUrl(getIntent().getStringExtra("weburl"));
        binding.webView.requestFocus(View.FOCUS_DOWN);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                try {
                    String extension = url.substring(url.lastIndexOf(".") + 1).toLowerCase();

                    if (extension.contains("mp4") || extension.contains("flv") || extension.contains("avi") || extension.contains("mkv")) {
                        // seems to a video
                        VideoPlayerActivity.start(getApplicationContext(), url);
                    } else {
                        binding.webView.loadUrl(url);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (binding.webView.canGoBackOrForward(1)) {
                binding.webView.goBackOrForward(1);
            } else {
                Toast.makeText(WebLoader.this, "Sorry, no history to go!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.enableJS) {
            if (javascriptSupport) {
                tinyDB.putBoolean("javascript", false);
                Toast.makeText(WebLoader.this, "JS Disabled. Load site again to effect.", Toast.LENGTH_SHORT).show();
            } else {
                tinyDB.putBoolean("javascript", true);
                Toast.makeText(WebLoader.this, "JS Enabled. Load site again to effect.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}