package com.example.muhammad.movieapp.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.muhammad.movieapp.Model.Trailers;
import com.example.muhammad.movieapp.R;

import java.util.ArrayList;

/**
 * Created by user12 on 12/10/2016.
 */

public class TrailersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Trailers> trailersList;
    public TrailersAdapter (Context context, ArrayList<Trailers> trailersList)
    {this.context = context;
        this.trailersList = trailersList;
    }
    @Override
    public int getCount() {
        return trailersList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_trailers ,parent,false);
        TextView name = (TextView)convertView.findViewById(R.id.trailer_name);
        name.setText(trailersList.get(position).getName());

        return convertView;
    }


}
