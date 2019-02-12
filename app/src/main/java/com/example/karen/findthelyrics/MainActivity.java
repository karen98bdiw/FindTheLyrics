package com.example.karen.findthelyrics;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText artistNameText;
    private EditText musicNameText;
    private Button searchBtn;
    private String artistName;
    private String musicName;
    private MusixMatch musixMatch;
    private Track track;
    private TrackData trackData;
    private WorkTask workTask;
    private String lyricsText;
    private Lyrics lyrics;
    private int trackId;
    private boolean isMusicFinded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workTask = new WorkTask();

        artistNameText = findViewById(R.id.artistNameText);
        musicNameText = findViewById(R.id.musicNameText);
        searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artistName = artistNameText.getText().toString();
                musicName = musicNameText.getText().toString();
                if (TextUtils.isEmpty(artistName)) {
                    Toast.makeText(MainActivity.this, "please write corect artist name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(musicName)) {
                    Toast.makeText(MainActivity.this, "please write corect music name", Toast.LENGTH_SHORT).show();

                } else {
                    workTask.execute(artistName, musicName);
                }

            }
        });

    }

    private class WorkTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String apiKey = "4a77e00201db59855cb513390782fe6a";
            musixMatch = new MusixMatch(apiKey);
            try {
                track = musixMatch.getMatchingTrack(strings[0], strings[1]);

            } catch (MusixMatchException e) {
                e.printStackTrace();
            }

            if (track == null) {
                isMusicFinded = false;
            } else {
                trackData = track.getTrack();
                trackId = trackData.getTrackId();
                if(trackData.getHasLyrics() == 0){
                    isMusicFinded = false;
                    Toast.makeText(MainActivity.this, "sory.can't find lyrics", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        lyrics = musixMatch.getLyrics(trackId);
                        lyricsText = lyrics.getLyricsBody();
                        Log.e("hey2","hey" + lyrics.getLyricsCopyright());
                        Log.e("hey3","hey" + lyrics.getLyricsLang());
                        Log.e("hey","hey" + lyricsText);
                    } catch (MusixMatchException e) {
                        e.printStackTrace();
                    }
                    isMusicFinded = true;
                }


            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (isMusicFinded) {
                workTask.cancel(true);
                Intent intent = new Intent(MainActivity.this, ShowMusic.class);
                intent.putExtra("artistName", trackData.getArtistName());
                intent.putExtra("lyrics", lyricsText);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Can't find", Toast.LENGTH_SHORT).show();
                workTask.cancel(true);
                workTask = new WorkTask();
            }

        }

    }
}


