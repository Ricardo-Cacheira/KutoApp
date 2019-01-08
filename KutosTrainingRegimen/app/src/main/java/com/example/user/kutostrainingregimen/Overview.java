package com.example.user.kutostrainingregimen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Overview extends RootActivity {

    private RecyclerView inventory;
    private RecyclerView.Adapter adapter;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Overview.this, TrainingRegimen.class);
                finish();
                startActivity(intent);
            }
        });

        ArrayList<Item> inventory = initInventory();

        //this.inventory = (RecyclerView) findViewById(R.id.inventory);
        //RecyclerView.LayoutManager mLayoutManager =  new LinearLayoutManager(this);
        //this.inventory.setLayoutManager(mLayoutManager);

        //adapter = new ItemAdapter(inventory);
        //this.inventory.setAdapter(adapter);
    }

    private ArrayList<Item> initInventory()
    {
        ArrayList<Item> list = new ArrayList<Item>();

        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));
        list.add(new Item("Item Name","skill", "https://goo.gl/JNemi1"));

        return list;
    }

}
