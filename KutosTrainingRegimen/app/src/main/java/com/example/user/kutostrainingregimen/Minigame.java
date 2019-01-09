package com.example.user.kutostrainingregimen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

public class Minigame extends RootActivity {

    Button playButton;
    private Button back;
    private Button help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);

//        back = (Button) findViewById(R.id.back);
//        help = (Button) findViewById(R.id.help);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Minigame.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

 /*       help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Minigame.this, Popup.class));
            }
        });*/

        playButton = (Button) findViewById(R.id.playbtn);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Minigame.this, GameScreen.class);
                startActivity(intent);
            }
        });
    }
}
