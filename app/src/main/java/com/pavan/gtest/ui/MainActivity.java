package com.pavan.gtest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pavan.gtest.R;
import com.pavan.gtest.data.YTdataModel;

import java.util.List;

import static com.pavan.gtest.data.Constants.API_KEY;
import static com.pavan.gtest.data.Constants.BASE_URL;

public class MainActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {


    private YouTubePlayerSupportFragmentX youTubePlayerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private MainViewModel viewModel;
    private YouTubePlayer player;
    private boolean isfirst = true;
    private String videoId;
    private boolean isFullSreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView titleTV = findViewById(R.id.yt_title_tv);
        final TextView descriptionTv = findViewById(R.id.yt_description_tv);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.yt_videos_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DatabaseReference reference = database.getReferenceFromUrl(BASE_URL);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        final VideosAdapter adapter = new VideosAdapter(this, viewModel);
        recyclerView.setAdapter(adapter);
        youTubePlayerView = YouTubePlayerSupportFragmentX.newInstance();
        youTubePlayerView.initialize(API_KEY, MainActivity.this);
//        youTubePlayerView = (YouTubePlayerSupportFragmentX) getSupportFragmentManager().findFragmentById(R.id.youtubeplayerview);


        viewModel.getyoutubeVideos(reference).observe(this, new Observer<List<YTdataModel>>() {
                    @Override
                    public void onChanged(List<YTdataModel> yTdataModels) {
                        progressBar.setVisibility(View.GONE);
                        adapter.update(yTdataModels);
                    }
                }
        );

        viewModel.getSelectedVideo().observe(this, new Observer<YTdataModel>() {
            @Override
            public void onChanged(YTdataModel yTdataModel) {
                if (isfirst) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.youtubeplayerview, youTubePlayerView)
                            .commit();
                    isfirst = false;
                }
                titleTV.setVisibility(View.VISIBLE);
                descriptionTv.setVisibility(View.VISIBLE);
                if (player != null) {
                    player.loadVideo(yTdataModel.getVideoId());
                }
                videoId = yTdataModel.getVideoId();
                titleTV.setText(yTdataModel.getTitle());
                descriptionTv.setText(yTdataModel.getDescription());
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

        player = youTubePlayer;
        if (!b) {
            youTubePlayer.loadVideo(videoId);
            youTubePlayer.play();
        }

        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullSreen = b;
                if (b) {
                    youTubePlayer.play();
                }
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    @Override
    public void onBackPressed() {
        if (isFullSreen) {
            player.setFullscreen(false);
            isFullSreen = false;
        } else
            super.onBackPressed();
    }

}