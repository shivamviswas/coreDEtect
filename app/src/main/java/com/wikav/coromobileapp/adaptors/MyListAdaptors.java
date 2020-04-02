package com.wikav.coromobileapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wikav.coromobileapp.R;
import com.wikav.coromobileapp.models.myList;

import java.util.List;

public class MyListAdaptors extends RecyclerView.Adapter<MyListAdaptors.ViewHolder> {
    Context context;
    List<myList> list;

    public MyListAdaptors(Context context, List<myList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.frndName.setText(list.get(position).getFriendName());
        holder.date.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView frndName,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            frndName = itemView.findViewById(R.id.friendListName);
            date = itemView.findViewById(R.id.friendListDate);
        }
    }
}
