package com.swansea.sindiso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private List<User> users;
    private Space space;
    private String description;

    public UserAdapter(Context c, List<User> users) {
        this.users = users;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public UserAdapter(Context c, List<User> users, Space space) {
        this.users = users;
        this.space = space;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.user_details, parent, false);
        }
            TextView userNameTextView = (TextView) convertView.findViewById(R.id.userName_TextView);
            TextView emailTextView = (TextView) convertView.findViewById(R.id.email_address_TextView);
            TextView fullAddressTextView = (TextView) convertView.findViewById(R.id.fullAddress_TextView);
            TextView userDescription = (TextView) convertView.findViewById(R.id.user_description);
            ImageView userIcon = (ImageView) convertView.findViewById(R.id.user_icon);

            String userName = users.get(position).getUserName();
            String email = users.get(position).getEmail();
            String fullAddress = users.get(position).getFullAddress();

            if (users.get(position).isStudent()) {
                description = "Number of boxes: " + users.get(position).getContainers().size();
                userIcon.setImageResource(R.drawable.box_icon);
            } else {
                description = "\"" + space.getDescription() + "\"";
                userIcon.setImageResource(R.drawable.house_icon);
            }
            userNameTextView.setText(userName);
            emailTextView.setText(email);
            fullAddressTextView.setText(fullAddress);
            userDescription.setText(description);

        return convertView;
    }
}
