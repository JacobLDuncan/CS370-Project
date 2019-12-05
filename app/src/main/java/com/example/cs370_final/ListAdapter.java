package com.example.cs370_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return ListData.title.length;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView itemText;

        public ListViewHolder(View itemView){
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.Item_Text);
            itemView.setOnClickListener(this);

        }

        public void bindView(int position){
            itemText.setText(ListData.title[position]);

        }
        public void onClick (View view){

        }
    }


}
