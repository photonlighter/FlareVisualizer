package com.example.cs121.flarevisualizer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// adapter for trigger list, which is a RecyclerView
public class TriggerListAdapter extends RecyclerView.Adapter<TriggerListAdapter.ViewHolder> {
    private String[] data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView row;

        public ViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.rowText);
        }
    }

    public TriggerListAdapter(String[] theData){
        data = theData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trigger_list_text_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.row.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
