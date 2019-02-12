package com.example.karen.findthelyrics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowMusic extends AppCompatActivity {

    private String lyrics;
    private String tittle;
    private TextView showMusicTittle;
    private TextView showMusicLyrics;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_music);

        showMusicLyrics = findViewById(R.id.showMusicLyrics);
        showMusicTittle = findViewById(R.id.showMusicTittle);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowMusic.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        lyrics = intent.getStringExtra("lyrics");
        tittle = intent.getStringExtra("artistName");

        showMusicTittle.setText(tittle);
        showMusicLyrics.setText(lyrics);

    }
}
