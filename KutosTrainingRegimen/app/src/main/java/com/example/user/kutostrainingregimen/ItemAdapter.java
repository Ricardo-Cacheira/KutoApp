package com.example.user.kutostrainingregimen;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {

    private ArrayList<Item> items;

    public ItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =  (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_inventory,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Bind((ViewHolder) viewHolder,i);
    }


    public void Bind(@NonNull ViewHolder holder, int i) {
        Item item = items.get(i);

        holder.name.setText(item.getName());
        holder.skill.setText(item.getSkill());

        Picasso.get().load(item.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View view;
        public final TextView name;
        public final TextView skill;
        public final ImageView image;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            skill = view.findViewById(R.id.skill);
            image = view.findViewById(R.id.image);

        }
    }
}


