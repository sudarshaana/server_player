package xyz.cruxlab.serverplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.PlaybackStatsListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import xyz.cruxlab.serverplayer.databinding.ActivityVideoPlayerBinding;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.google.android.exoplayer2.Player.STATE_BUFFERING;

public class VideoPlayerActivity extends AppCompatActivity {
    private String VIDEO_URL = "";
    ActivityVideoPlayerBinding binding;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SubtitleView subtitles;
    private static final String TAG = "VideoPlayerActivity";

    public static void start(Context context, String videoUrl) {
        context.startActivity(new Intent(context, VideoPlayerActivity.class).putExtra("videoUrl", videoUrl).setFlags(FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onStart() {
        super.onStart();
        VIDEO_URL = getIntent().getStringExtra("videoUrl");
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void initializePlayer() {

//        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//        trackSelector.setParameters(new DefaultTrackSelector.ParametersBuilder()
//                .setRendererDisabled(C.TRACK_TYPE_VIDEO, false)
//                .build()
//        );


        subtitles = (SubtitleView) findViewById(R.id.subtitle);
        playerView = findViewById(R.id.video_view);
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(VIDEO_URL);
        player.setMediaItem(mediaItem);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();

        String url = null;
        try {
            url = URLDecoder.decode(VIDEO_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (url == null) url = VIDEO_URL;
        binding.trackName.setText(url.substring(url.lastIndexOf("/") + 1).replace("%20", " "));

//        player.addListener(new Player.EventListener() {
//            @Override
//            public void onIsLoadingChanged(boolean isLoading) {
//                if (isLoading)
//                    binding.progressBar.setVisibility(View.VISIBLE);
//                else
//                    binding.progressBar.setVisibility(View.GONE);
//            }
//        });
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == STATE_BUFFERING) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }

            }
        });

//        //playerView.hideController();
//        playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
//            @Override
//            public void onVisibilityChange(int visibility) {
//                if (visibility == View.VISIBLE) {
//                    playerView.hideController();
//                    Log.d(TAG, "onVisibilityChange: hide");
//                } else {
//                    playerView.showController();
//                    Log.d(TAG, "onVisibilityChange: ");
//                }
//            }
//        });
//
//        playerView.hideController();
//        playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
//            @Override
//            public void onVisibilityChange(int visibility) {
//
//            }
//        });

        //playerView.isControllerVisible();

        //playerView.set

//        player.addTextOutput(new TextOutput() {
//            @Override
//            public void onCues(List<Cue> cues) {
//                Log.e("cues are ", cues.toString());
//                if (subtitles != null) {
//                    subtitles.onCues(cues);
//                }
//            }
//        });

//        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, null, Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE);
//        MediaSource textMediaSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(sub_url), textFormat, C.TIME_UNSET);
        // player.text
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.stop();
            player.release();
            player = null;
        }
    }

}