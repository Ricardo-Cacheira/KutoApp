package com.example.user.kutostrainingregimen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TipsFragment extends Fragment{

    ListView list;
    SearchView search;
    ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_tips, container, false);


        search = (SearchView) rootView.findViewById(R.id.search);
        list = (ListView) rootView.findViewById(R.id.list);


       adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.tips_array, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String text) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                adapter.getFilter().filter(text);

                return false;
            }
        });

        /*
        tips.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Toast.makeText(getContext(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        */

        return rootView;
    }
}
