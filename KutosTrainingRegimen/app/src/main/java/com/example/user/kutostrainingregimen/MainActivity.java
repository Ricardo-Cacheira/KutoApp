package com.example.user.kutostrainingregimen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends RootActivity {

    private Button walkingBtn;
    private Button tipsBtn;
    private Button overviewBtn;
    private Button minigameBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        walkingBtn = (Button) findViewById(R.id.walkingbtn);
        tipsBtn = (Button) findViewById(R.id.tipsbtn);
        overviewBtn = (Button) findViewById(R.id.overviewbtn);
        minigameBtn = (Button) findViewById(R.id.minigamebtn);

        walkingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (MainActivity.this, Walking.class);
                startActivity(intent);
            }
        });

        tipsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (MainActivity.this, Tips.class);
                startActivity(intent);
            }
        });

        overviewBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (MainActivity.this, Overview.class);
                startActivity(intent);
            }
        });

        minigameBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (MainActivity.this, Minigame.class);
                startActivity(intent);
            }
        });


    }
}
