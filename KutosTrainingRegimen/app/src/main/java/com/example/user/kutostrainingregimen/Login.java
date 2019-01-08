package com.example.user.kutostrainingregimen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    Button login;
    EditText ip;
    EditText name;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.button);
        ip = (EditText) findViewById(R.id.ip);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TrainingRegimen.class);
                i.putExtra("ip", ip.getText().toString());
                i.putExtra("name",name.getText().toString());
                i.putExtra("pass",pass.getText().toString());
                startActivity(i);
            }
        });
    }
}
