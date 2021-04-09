package com.swansea.sindiso;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class User implements Parcelable {
    private Integer id;
    private String userName;
    private LatLng location;
    private String fullAddress;
    private String password;
    private ArrayList<Container> containers;
    private boolean studentStatus;
    private String email;

    public User(Integer id, String userName, String password, boolean studentStatus){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.studentStatus = studentStatus;
        containers = new ArrayList<Container>();
    }

    public User(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.userName = data[1];
        this.password = data[2];
        this.studentStatus = Boolean.parseBoolean(data[3]);
    }

    public User(Integer id, String userName, String password){
        this.id = id;
        this.userName = userName;
        this.password = password;
        containers = new ArrayList<Container>();
    }

    public LatLng getLocation() {
        return location;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        this.location = latLng;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail(){ return email; }

    public Integer getId() {
        return id;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isStudent() {
        return studentStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.id), this.userName, this.password, String.valueOf(this.studentStatus)});
    }

    public static final Parcelable.Creator<User> CREATOR= new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
// TODO Auto-generated method stub
            return new User(source);  //using parcelable constructor
        }

        @Override
        public User[] newArray(int size) {
// TODO Auto-generated method stub
            return new User[size];
        }
    };
}
