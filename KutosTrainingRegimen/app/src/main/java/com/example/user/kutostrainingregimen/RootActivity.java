package com.example.user.kutostrainingregimen;
import android.app.Activity;
import android.os.Bundle;

public class RootActivity extends Activity {

    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        onStartCount = 0;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slideleft_in,
                    R.anim.anim_slideleft_out);
        } else // already created so reverse animation
        {
            onStartCount = 1;
        }
    }

    @Override
    protected void onStart()
    {

        super.onStart();

        if (onStartCount > 0)
        {
            this.overridePendingTransition(R.anim.anim_slideright_in,
                    R.anim.anim_slideright_out);

        } else if (onStartCount == 0) {
            onStartCount++;
        }
    }
}