package com.wikav.coromobileapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wikav.coromobileapp.R;
import com.wikav.coromobileapp.models.InfectedModel;

import java.util.List;

public class InfectedPrsnAdaptor extends RecyclerView.Adapter<InfectedPrsnAdaptor.ViewHolder> {
    Context context;
    List<InfectedModel> list ;

    public InfectedPrsnAdaptor(Context context, List<InfectedModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.infected_prsn_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.infecctedName.setText(list.get(position).getFriendName());
        holder.date.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView infecctedName,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            infecctedName = itemView.findViewById(R.id.infectedListName);
            date = itemView.findViewById(R.id.infectedListDate);
        }
    }
}
