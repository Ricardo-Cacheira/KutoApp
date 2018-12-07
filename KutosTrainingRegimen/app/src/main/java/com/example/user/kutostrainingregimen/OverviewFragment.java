package com.example.user.kutostrainingregimen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class OverviewFragment extends Fragment {

    private RecyclerView inventory;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_overview, container, false);

        ArrayList<Item> inventory = initInventory();

        this.inventory = (RecyclerView) rootView.findViewById(R.id.inventory);
        RecyclerView.LayoutManager mLayoutManager =  new LinearLayoutManager(getContext());
        this.inventory.setLayoutManager(mLayoutManager);

        adapter = new ItemAdapter(inventory);
        this.inventory.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You clicked this button", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return rootView;
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
