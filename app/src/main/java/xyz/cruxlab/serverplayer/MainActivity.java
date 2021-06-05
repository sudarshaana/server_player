package xyz.cruxlab.serverplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xyz.cruxlab.serverplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    private TinyDB tinyDB;
    List<String> stringList;// = new ArrayList<>();
    private UrlItemAdapter urlItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tinyDB = new TinyDB(getApplicationContext());


        stringList = tinyDB.getListString("URL_LIST");
        if (stringList == null) {
            Log.d(TAG, "onCreate: null");
        } else {
            Log.d(TAG, "onCreate: " + stringList.size() + stringList.toString());
            urlItemAdapter = new UrlItemAdapter(getApplicationContext(), stringList);
            binding.content.urlRV.setAdapter(urlItemAdapter);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotDialog(getApplicationContext());
            }
        });
    }

    private void showForgotDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setText("http://");
        taskEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        //taskEditText.setPadding(10, 10, 10, 10);
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add Website")
                .setMessage("Enter site URL")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String task = String.valueOf(taskEditText.getText());
                        //
                        if (taskEditText.getText().toString().length() > 7) {
                            stringList.add(taskEditText.getText().toString());
                            tinyDB.putListString("URL_LIST", (ArrayList<String>) stringList);
                            urlItemAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Enter a valid url", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


}