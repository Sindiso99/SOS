package com.swansea.sindiso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContainerAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private List<Container> containers;

    public ContainerAdapter(Context c, List<Container> containers){
        this.containers = containers;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ContainerAdapter(Context c, Container container){
        containers = new ArrayList<>();
        containers.add(container);
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return containers.size();
    }

    @Override
    public Container getItem(int position) {
        return containers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return containers.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.container_detail, parent, false);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.containerName_TextView);
            TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_TextView);

            String label = containers.get(position).getLabel();
            String description = containers.get(position).getDescription();

            nameTextView.setText(label);
            descriptionTextView.setText(description);
        }


        return convertView;
    }
}
