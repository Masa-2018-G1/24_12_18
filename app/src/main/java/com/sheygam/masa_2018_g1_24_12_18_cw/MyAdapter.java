package com.sheygam.masa_2018_g1_24_12_18_cw;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> list;

    public MyAdapter() {
        list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("Name " + i);
        }
    }

    public void remove(int pos){
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    public void move(int from, int to){
        String str = list.remove(from);
        list.add(to,str);
        notifyItemMoved(from,to);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MY_TAG", "onCreateViewHolder: ");

        RecyclerView.ViewHolder holder;
        if(viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_row, parent, false);
            holder = new MyViewHolder(view);
        }else{
            View view  = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_odd_row,parent,false);
            holder = new MyOddViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name = list.get(position);
        if(holder instanceof MyViewHolder){
            ((MyViewHolder)holder).nameTxt.setText(name);
        }else if(holder instanceof MyOddViewHolder){
            ((MyOddViewHolder)holder).nameTxt.setText(name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTxt;
        public MyViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name_txt);
        }
    }

    class MyOddViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTxt;
        public MyOddViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name_txt);
        }
    }
}
