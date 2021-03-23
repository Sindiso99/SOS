package com.swansea.sindiso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContainerAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private String[] containerNames;
    private String[] containerDescriptions;
    private String[] containerVolumes;

    public ContainerAdapter(Context c, String[] containerNames, String[] containerDescriptions, String[] containerVolumes){
        this.containerNames = containerNames;
        this.containerDescriptions = containerDescriptions;
        this.containerVolumes = containerVolumes;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return containerVolumes.length;
    }

    @Override
    public Object getItem(int position) {
        return containerNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.container_detail, parent, false);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.containerName_TextView);
            TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_TextView);
            TextView volumeTextView = (TextView) convertView.findViewById(R.id.volume_TextView);

            String name = containerNames[position];
            String description = containerDescriptions[position];
            String volume = containerVolumes[position];

            nameTextView.setText(name);
            descriptionTextView.setText(description);
            volumeTextView.setText(volume);
        }


        return convertView;
    }
}
