package com.swansea.sindiso;

import android.os.Parcel;
import android.os.Parcelable;

public class Container implements Parcelable {
    private Integer id;
    private String label;
    private String description;
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer volume;
    private Integer ownerId;
    private final Integer CELL_SIZE = 50;

    public Container(Integer id, String label, Integer l, Integer h, Integer w, String description, Integer ownerId){
        this.id = id;
        this.label = label;
        this.length = l;
        this.height = h;
        this.width = w;
        this.description = description;
        this.ownerId = ownerId;
        setVolume();
    }

    public Container(Parcel in){
        String[] data = new String[7];
        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.label = data[1];
        this.length = Integer.parseInt(data[2]);
        this.height = Integer.parseInt(data[3]);
        this.width = Integer.parseInt(data[4]);
        this.description = data[5];
        this.ownerId = Integer.parseInt(data[6]);
        setVolume();
    }

    @Override
    public String toString() {
        return "Container{" +
                "label='" + label + '\'' +
                ", volume=" + volume +
                '}';
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    private boolean validUnit(Integer unit){
        if(unit >= 0){
            return true;
        } else {
            return false;
        }
    }

    public void setVolume(){
        volume = length * width * height;
    }

    public int lengthToCell() {
        if (length < CELL_SIZE) {
            return 1;
        } else {
            return ((int) Math.ceil((double)length / CELL_SIZE));
        }
    }

    public int widthToCell() {
        if (width < CELL_SIZE) {
            return 1;
        } else {
            return ((int) Math.ceil((double)width / CELL_SIZE));
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setWidth(Integer w){
        if(validUnit(w)){
            width = w;
        }
    }

    public void setHeight(Integer h){
        if (validUnit(h)){
            height = h;
        }
    }

    public void setLength(Integer l){
        if (validUnit(l)){
            length = l;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getVolume() {
        return volume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.id), this.label, String.valueOf(this.length), String.valueOf(this.height),
                String.valueOf(this.width), this.description, String.valueOf(this.ownerId)});
    }

    public static final Parcelable.Creator<Container> CREATOR= new Parcelable.Creator<Container>() {

        @Override
        public Container createFromParcel(Parcel source) {
// TODO Auto-generated method stub
            return new Container(source);  //using parcelable constructor
        }

        @Override
        public Container[] newArray(int size) {
// TODO Auto-generated method stub
            return new Container[size];
        }
    };
}
