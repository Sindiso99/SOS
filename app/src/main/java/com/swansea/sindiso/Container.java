package com.swansea.sindiso;

public class Container {
    private final Integer ID;
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer volume;

    public Container(Integer ID, Integer l, Integer h, Integer w){
        this.ID = ID;
        this.length = l;
        this.height = h;
        this.width = w;
        setVolume();
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
}
