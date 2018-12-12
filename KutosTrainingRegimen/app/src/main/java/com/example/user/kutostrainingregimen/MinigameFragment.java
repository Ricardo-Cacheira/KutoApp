package com.example.user.kutostrainingregimen;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class MinigameFragment extends Fragment {

    public SharedPreferences sharedPref;
    Button playButton;
    private Button help;
    private TextView highscore;
    private TextView lastScore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_minigame, container, false);

        sharedPref = getContext().getSharedPreferences("myPrefs" , MODE_PRIVATE);

        help = (Button) rootView.findViewById(R.id.help);
        highscore = (TextView) rootView.findViewById(R.id.highscore);
        lastScore = (TextView) rootView.findViewById(R.id.lastScore);

        help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Popup.class));
            }
        });

        LoadHighscore();

        playButton = (Button) rootView.findViewById(R.id.playbtn);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getContext(), GameScreen.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadHighscore();
    }


    public void LoadHighscore()
    {
        int score = sharedPref.getInt("High",0);
        int last = sharedPref.getInt("Last",0);

        highscore.setText("HighScore: " + score);
        lastScore.setText("Last Score: " + last);
    }
}
