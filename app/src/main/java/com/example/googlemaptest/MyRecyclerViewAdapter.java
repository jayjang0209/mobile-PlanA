package com.example.googlemaptest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{

    Context c;
    ArrayList<String> routes;
    ItemClickListener clickListener;

    public MyRecyclerViewAdapter(Context c, ArrayList<String> routes) {
        this.c = c;
        this.routes = routes;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.fragment_saved_routes, parent, false);
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        return new MyViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.start.setText(routes.get(position));
        holder.end.setText(routes.get(routes.size() - 1));
        holder.name.setText("Route " + (position+1));
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView start, end, name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);

//
        }

    }
}
